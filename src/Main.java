import com.google.gson.Gson;
import dao.KorisnikDAO;
import model.Korisnik;
import model.Kupac;
import model.Prodavac;
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
    private static Korisnik ulogovanKorisnik;

    public static void ucitaj() {
        korisnikDAO = new KorisnikDAO();
        ulogovanKorisnik = null;
        korisnikDAO.ucitajKorisnike();
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
                return g.toJson(korisnik);
            }else {
                return "Pogrešno korisničko ime/lozinka.";
            }
        });

        get("/korisnici/logout", (req, res) -> {
            ulogovanKorisnik = null;
            return "Done";
        });
    }
}
