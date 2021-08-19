import com.google.gson.Gson;
import dao.KartaDAO;
import dao.KorisnikDAO;
import dao.TipKupcaDAO;
import model.*;
import model.enums.Pol;
import model.enums.Uloga;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class Main {
    static KorisnikDAO korisnikDAO;
    static TipKupcaDAO tipKupcaDAO;
    static KartaDAO kartaDAO;

    private static Korisnik ulogovanKorisnik;
    private static Kupac ulogovanKupac;
    private static Prodavac ulogovanProdavac;

    public static void ucitaj() {
        korisnikDAO = new KorisnikDAO();
        tipKupcaDAO =new TipKupcaDAO();
        kartaDAO = new KartaDAO();

        ulogovanKorisnik = null;
        ulogovanKupac = null;
        ulogovanProdavac = null;

        korisnikDAO.ucitajKorisnike();
        tipKupcaDAO.ucitajTipoveKupaca();
        kartaDAO.ucitajKarte();
    }

    public static void main(String[] args) throws Exception {
        port(9090);
        Gson g = new Gson();

        staticFiles.externalLocation(new File("./static").getCanonicalPath());

        after((req, res) -> res.type("application/json"));

        get("/test", (req, res) -> {
            return "Works";
        });

        ucitaj();

        post("/korisnici/registracija", (req, res) -> {
            var mapa = g.fromJson(req.body(), HashMap.class);
            Korisnik novi = new Korisnik((String) mapa.get("korisnickoIme"), (String) mapa.get("lozinka"), (String) mapa.get("ime"),
                    (String) mapa.get("prezime"), Pol.valueOf((String)mapa.get("pol")), LocalDate.parse((String)mapa.get("datumRodjenja")),
                    Uloga.KUPAC, false, false);

            Korisnik korisnik = korisnikDAO.findUserByUsername(novi.getKorisnickoIme());
            if(korisnik != null) {
                return "Već postoji korisnik sa unetim korisničkim imenom!";
            }
            else if (novi.getKorisnickoIme().equals("") || novi.getIme().equals("") || novi.getPrezime().equals("") ||
                    novi.getLozinka().equals("")) {
                return "Neophodno je popuniti sva polja!";
            }
            else {
                List<String> karte = new ArrayList<>();
                List<Integer> manifestacije = new ArrayList<>();

                if (ulogovanKorisnik != null && ulogovanKorisnik.getUloga().equals(Uloga.ADMIN)) {
                    Prodavac noviProdavac = new Prodavac(novi.getKorisnickoIme(), novi.getLozinka(), novi.getIme(), novi.getPrezime(),
                            novi.getPol(), novi.getDatumRodjenja(), Uloga.PRODAVAC, false, false, manifestacije, karte);
                    korisnikDAO.dodajProdavca(noviProdavac);
                    return "Done";
                }
                Kupac noviKupac = new Kupac(novi.getKorisnickoIme(), novi.getLozinka(), novi.getIme(), novi.getPrezime(),
                        novi.getPol(), novi.getDatumRodjenja(), Uloga.KUPAC, false, false, karte, 0, 3);

                korisnikDAO.dodajKupca(noviKupac);
                return "Done";
            }
        });

        post("/korisnici/login", (req, res) -> {
            var mapa = g.fromJson(req.body(), HashMap.class);
            Korisnik korisnik = korisnikDAO.findUserByUsernameAndPassword((String)mapa.get("korisnickoIme"),
                    (String)mapa.get("lozinka"));
            if (korisnik != null) {
                res.type("application/json");
                if (korisnik.getBlokiran()) {
                    return "Nalog blokiran!";
                } else if (korisnik.getObrisan()) {
                    return "Nalog obrisan!";
                }
                ulogovanKorisnik = korisnik;
                if (korisnik.getUloga().equals(Uloga.ADMIN)) {
                    return g.toJson(korisnik);
                } else if (korisnik.getUloga().equals(Uloga.KUPAC)) {
                    ulogovanKupac = korisnikDAO.findKupacByUsername(korisnik.getKorisnickoIme());
                    return g.toJson(ulogovanKupac);
                } else if (korisnik.getUloga().equals(Uloga.PRODAVAC)) {
                    ulogovanProdavac = korisnikDAO.findProdavacByUsername(korisnik.getKorisnickoIme());
                    return g.toJson(ulogovanProdavac);
                }
                return null;
            } else {
                return "Pogrešno korisničko ime/lozinka.";
            }
        });

        get("/korisnici/logout", (req, res) -> {
            ulogovanKorisnik = null;
            return "Done";
        });

        get("/korisnici/tipKupca", (req, res) -> {
            if (ulogovanKorisnik.getUloga().equals(Uloga.KUPAC)) {
                TipKupca tipKupca = tipKupcaDAO.findTipKupca(ulogovanKupac.getTip());
                return g.toJson(tipKupca);
            }
            return "";
        });

        get("/korisnici/karte", (req, res) -> {
            ArrayList<Karta> karte = new ArrayList<>();
            if (ulogovanKorisnik.getUloga().equals(Uloga.KUPAC)) {
                for (String id : ulogovanKupac.getKarte()) {
                    karte.add(kartaDAO.findKarta(id));
                }
            } else if (ulogovanKorisnik.getUloga().equals(Uloga.PRODAVAC)) {
                for (String id : ulogovanProdavac.getKarte()) {
                    karte.add(kartaDAO.findKarta(id));
                }
            }
            return g.toJson(karte);
        });

        post("/korisnici/izmena", (req, res) -> {
            var mapa = g.fromJson(req.body(), HashMap.class);
            Korisnik korisnik = korisnikDAO.findUserByUsername((String)mapa.get("korisnickoIme"));
            korisnik.setIme((String)mapa.get("ime"));
            korisnik.setPrezime((String)mapa.get("prezime"));
            korisnik.setPol(Pol.valueOf((String)mapa.get("pol")));
            korisnik.setDatumRodjenja(LocalDate.parse((String)mapa.get("datumRodjenja")));
            korisnikDAO.izmeniKorisnika(korisnik);
            ulogovanKorisnik = korisnik;
            if (korisnik.getUloga().equals(Uloga.KUPAC)) {
                ulogovanKupac = korisnikDAO.findKupacByUsername(korisnik.getKorisnickoIme());
                return g.toJson(ulogovanKupac);
            } else if (korisnik.getUloga().equals(Uloga.PRODAVAC)) {
                ulogovanProdavac = korisnikDAO.findProdavacByUsername(korisnik.getKorisnickoIme());
                return g.toJson(ulogovanProdavac);
            }
            return g.toJson(korisnik);
        });
    }
}
