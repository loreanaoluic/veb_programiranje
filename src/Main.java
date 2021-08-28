import com.google.gson.Gson;
import dao.*;
import model.*;
import model.enums.Pol;
import model.enums.Uloga;
import spark.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import static spark.Spark.*;

public class Main {
    static KorisnikDAO korisnikDAO;
    static TipKupcaDAO tipKupcaDAO;
    static KartaDAO kartaDAO;
    static ManifestacijaDAO manifestacijaDAO;
    static LokacijaDAO lokacijaDAO;

    private static Korisnik ulogovanKorisnik;
    private static Kupac ulogovanKupac;
    private static Prodavac ulogovanProdavac;

    public static void ucitaj() {
        korisnikDAO = new KorisnikDAO();
        tipKupcaDAO =new TipKupcaDAO();
        kartaDAO = new KartaDAO();
        manifestacijaDAO = new ManifestacijaDAO();
        lokacijaDAO = new LokacijaDAO();

        ulogovanKorisnik = null;
        ulogovanKupac = null;
        ulogovanProdavac = null;

        korisnikDAO.ucitajKorisnike();
        tipKupcaDAO.ucitajTipoveKupaca();
        kartaDAO.ucitajKarte();
        manifestacijaDAO.ucitajManifestacije();
        lokacijaDAO.ucitajLokacije();
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

        // KORISNICI

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
                return "Greska";
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
                    karte.add(kartaDAO.findKartaById(id));
                }
            } else if (ulogovanKorisnik.getUloga().equals(Uloga.PRODAVAC)) {
                for (String id : ulogovanProdavac.getKarte()) {
                    karte.add(kartaDAO.findKartaById(id));
                }
            }
            return g.toJson(karte);
        });


        get("/korisnici/svi-korisnici", (req, res) -> g.toJson(KorisnikDAO.getListaSvihKorisnika()));

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


        // MANIFESTACIJE

        get("/manifestacije", (req, res) -> g.toJson(ManifestacijaDAO.getListaManifestacija()));

        get("/manifestacije/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            return g.toJson(lokacijaDAO.findLokacijaById(manifestacijaDAO.findManifestacijaById(id).getLokacija()));
        });

        post("/manifestacije/manifestacije-prodavca", (req, res) -> {
            ArrayList<Manifestacija> sveManifestacije = ManifestacijaDAO.getListaManifestacija();
            ArrayList<Manifestacija> manifestacijeProdavca = new ArrayList<>();

            for (int idManifestacije : ulogovanProdavac.getManifestacije()) {
                for (Manifestacija manifestacija : sveManifestacije) {
                    if (idManifestacije == manifestacija.getId()) {
                        manifestacijeProdavca.add(manifestacija);
                    }
                }
            }
            return g.toJson(manifestacijeProdavca);
        });

        get("/manifestacije/pretraga", (req, res) -> {

            String naziv = req.queryParams("naziv").trim();
            long pocetniDatum = Long.parseLong(req.queryParams("pocetniDatum"));
            long krajnjiDatum = Long.parseLong(req.queryParams("krajnjiDatum"));

            String lokacija = req.queryParams("lokacija").trim();

            double pocetnaCena = Double.parseDouble(req.queryParams("pocetnaCena").trim());
            double krajnjaCena = Double.parseDouble(req.queryParams("krajnjaCena").trim());

            String tip = req.queryParams("tip").trim();
            String kriterijumSortiranja = req.queryParams("kriterijumSortiranja").trim();
            boolean opadajuce = Boolean.parseBoolean(req.queryParams("opadajuce").trim());
            boolean rasprodata = Boolean.parseBoolean(req.queryParams("rasprodata").trim());

            List<Manifestacija> manifestacije = manifestacijaDAO.searchFilterSort(naziv, pocetniDatum, krajnjiDatum,
                    lokacija, pocetnaCena, krajnjaCena, tip, kriterijumSortiranja, opadajuce, rasprodata, lokacijaDAO);

            res.type("application/json");
            return g.toJson(manifestacije);

        });

        post("/manifestacije/nova", (req, res) -> {
            var mapa = g.fromJson(req.body(), HashMap.class);

            Random rand = new Random();

            Lokacija lokacija = new Lokacija(rand.nextInt(1000), Double.parseDouble(String.valueOf(mapa.get("geografskaSirina"))),
                    Double.parseDouble(String.valueOf(mapa.get("geografskaDuzina"))), (String) mapa.get("adresa"));

            Manifestacija manifestacija = new Manifestacija(rand.nextInt(1000), (String) mapa.get("naziv"),
                    (String) mapa.get("tipManifestacije"), Integer.parseInt((String) mapa.get("brojMesta")),
                    LocalDateTime.parse((CharSequence) mapa.get("datumIVremeOdrzavanja")), Double.parseDouble((String) mapa.get("cenaRegular")), false, lokacija.getId(),
                    (String) mapa.get("poster"), false);

            for (Lokacija l : LokacijaDAO.listaLokacija) {
                if (l.getAdresa().equals(lokacija.getAdresa()) && l.getGeografskaDuzina() == lokacija.getGeografskaDuzina()
                        && l.getGeografskaSirina() == lokacija.getGeografskaSirina()) {
                    for (Manifestacija m : ManifestacijaDAO.listaManifestacija) {
                        if (m.getDatumIVremeOdrzavanja().equals(manifestacija.getDatumIVremeOdrzavanja())
                        && m.getLokacija() == l.getId()) {
                            return "Već postoji manifestacija na željenoj lokaciji u to vreme!";
                        }
                    }
                }
            }

            lokacijaDAO.dodajLokaciju(lokacija);
            manifestacijaDAO.dodajManifestaciju(manifestacija);

            List<Integer> manifestacije = ulogovanProdavac.getManifestacije();
            manifestacije.add(manifestacija.getId());
            ulogovanProdavac.setManifestacije(manifestacije);

            korisnikDAO.sacuvajKorisnike();

            return "Done";
        });

        post("/manifestacije/status/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Manifestacija manifestacija = manifestacijaDAO.findManifestacijaById(id);
            manifestacijaDAO.promeniStatusUAktivno(manifestacija);
            return manifestacija;
        });

        post("/manifestacije/izmenaManifestacije/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            var mapa = g.fromJson(req.body(), HashMap.class);

            Manifestacija novaManifestacija = new Manifestacija(id, String.valueOf(mapa.get("naziv")), String.valueOf(mapa.get("tipManifestacije")),
                   0, LocalDateTime.parse(String.valueOf(mapa.get("datumIVremeOdrzavanja"))),
                    Double.parseDouble(String.valueOf(mapa.get("cenaRegular"))), Boolean.parseBoolean(String.valueOf(mapa.get("status"))),
                    0, String.valueOf(mapa.get("poster")), false);

            return manifestacijaDAO.izmeniManifestaciju(novaManifestacija);

        });


        // KARTE

        get("/karte", (req, res) -> {
            return "Done";
        });

        get("/karte/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            return g.toJson(kartaDAO.findKarteByManifestacijaAndProdavacAndRezervisana(id, ulogovanProdavac.getKorisnickoIme()));
        });

    }
}
