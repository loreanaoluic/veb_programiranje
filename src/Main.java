import com.google.gson.Gson;
import dao.*;
import model.*;
import model.enums.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static spark.Spark.*;

public class Main {
    static KorisnikDAO korisnikDAO;
    static TipKupcaDAO tipKupcaDAO;
    static KartaDAO kartaDAO;
    static ManifestacijaDAO manifestacijaDAO;
    static LokacijaDAO lokacijaDAO;
    static KomentarDAO komentarDAO;

    private static Korisnik ulogovanKorisnik;
    private static Kupac ulogovanKupac;
    private static Prodavac ulogovanProdavac;

    private static ArrayList<ShoppingCartItem> shoppingCartItems;

    public static void ucitaj() {
        korisnikDAO = new KorisnikDAO();
        tipKupcaDAO =new TipKupcaDAO();
        kartaDAO = new KartaDAO();
        manifestacijaDAO = new ManifestacijaDAO();
        lokacijaDAO = new LokacijaDAO();
        komentarDAO = new KomentarDAO();

        ulogovanKorisnik = null;
        ulogovanKupac = null;
        ulogovanProdavac = null;

        shoppingCartItems = new ArrayList<>();

        korisnikDAO.ucitajKorisnike();
        tipKupcaDAO.ucitajTipoveKupaca();
        kartaDAO.ucitajKarte();
        manifestacijaDAO.ucitajManifestacije();
        lokacijaDAO.ucitajLokacije();
        komentarDAO.ucitajKomentare();

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
                KorisnikDAO.listaSvihKorisnika.add(novi);

                List<String> karte = new ArrayList<>();
                List<Integer> manifestacije = new ArrayList<>();

                if (ulogovanKorisnik != null && ulogovanKorisnik.getUloga().equals(Uloga.ADMIN)) {
                    Prodavac noviProdavac = new Prodavac(novi.getKorisnickoIme(), novi.getLozinka(), novi.getIme(), novi.getPrezime(),
                            novi.getPol(), novi.getDatumRodjenja(), Uloga.PRODAVAC, false, false, manifestacije, karte);
                    korisnikDAO.dodajProdavca(noviProdavac);
                    KorisnikDAO.listaProdavaca.add(noviProdavac);
                    return "Done prodavac";
                }
                Kupac noviKupac = new Kupac(novi.getKorisnickoIme(), novi.getLozinka(), novi.getIme(), novi.getPrezime(),
                        novi.getPol(), novi.getDatumRodjenja(), Uloga.KUPAC, false, false, karte, 0, 1, false);

                korisnikDAO.dodajKupca(noviKupac);
                KorisnikDAO.listaKupaca.add(noviKupac);
                return "Done kupac";
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


        get("/korisnici/svi-korisnici", (req, res) -> {
            ArrayList<Object> korisnici = new ArrayList<>();
            korisnici.addAll(korisnikDAO.getNeobrisaneAdmine());
            korisnici.addAll(korisnikDAO.getNeobrisaneProdavce());
            korisnici.addAll(korisnikDAO.getNeobrisaneKupce());
            return g.toJson(korisnici);
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

        post("/korisnici/izmena-lozinke", (req, res) -> {
            var mapa = g.fromJson(req.body(), HashMap.class);
            if (!ulogovanKorisnik.getLozinka().equals((String)mapa.get("stara"))) {
                return "Greska";
            }

            ulogovanKorisnik.setLozinka((String)mapa.get("nova"));
            korisnikDAO.izmeniKorisnika(ulogovanKorisnik);

            return g.toJson(ulogovanKorisnik);
        });

        post("/korisnici/pretraga", (req, res) -> {

            String ime = req.queryParams("ime").trim();
            String prezime = req.queryParams("prezime").trim();
            String korisnickoIme = req.queryParams("korisnickoIme").trim();
            String kriterijumSortiranja = req.queryParams("kriterijumSortiranja").trim();
            String uloga = req.queryParams("uloga").trim();
            boolean opadajuce = Boolean.parseBoolean(req.queryParams("opadajuce").trim());
            boolean sumnjivi = Boolean.parseBoolean(req.queryParams("sumnjivi").trim());

            List<Korisnik> korisnici = korisnikDAO.searchFilterSort(ime, prezime, korisnickoIme, kriterijumSortiranja,
                    uloga, opadajuce, sumnjivi);

            res.type("application/json");
            return g.toJson(korisnici);

        });

        post("/korisnici/obrisi/:id", (req, res) -> {
            String korisnickoIme = req.params("id");
            Korisnik korisnik = korisnikDAO.findUserByUsername(korisnickoIme);
            if (korisnik.getUloga().equals(Uloga.KUPAC)) {
                kartaDAO.obrisiKarteZaKupca(korisnik.getKorisnickoIme());
            } else if (korisnik.getUloga().equals(Uloga.PRODAVAC)) {
                Prodavac prodavac = korisnikDAO.findProdavacByUsername(korisnik.getKorisnickoIme());
                manifestacijaDAO.obrisiManifestacijeZaProdavca(prodavac.getManifestacije());
                kartaDAO.obrisiKarteZaProdavca(korisnik.getKorisnickoIme());
            }
            korisnikDAO.obrisiKorisnika(korisnickoIme);
            return "Done";
        });

        post("/korisnici/blokiraj/:id", (req, res) -> {
            String korisnickoIme = req.params("id");
            korisnikDAO.blokirajKorisnika(korisnickoIme);
            return "Done";
        });

        post("/korisnici/odblokiraj/:id", (req, res) -> {
            String korisnickoIme = req.params("id");
            korisnikDAO.odblokirajKorisnika(korisnickoIme);
            return "Done";
        });


        // MANIFESTACIJE

        get("/manifestacije", (req, res) -> g.toJson(manifestacijaDAO.getNeobrisaneManifestacije()));

        get("/manifestacijeMapa", (req, res) -> g.toJson(manifestacijaDAO.getMapaNeobrisaneManifestacija()));

        get("/manifestacije/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            return g.toJson(lokacijaDAO.findLokacijaById(manifestacijaDAO.findManifestacijaById(id).getLokacija()));
        });

        post("/manifestacije/manifestacije-prodavca", (req, res) -> {
            ArrayList<Manifestacija> sveManifestacije = manifestacijaDAO.getNeobrisaneManifestacije();
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

        post("/manifestacije/pretraga", (req, res) -> {

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
                    Double.parseDouble(String.valueOf(mapa.get("geografskaDuzina"))), (String) mapa.get("adresa"), false);

            Manifestacija manifestacija = new Manifestacija(rand.nextInt(1000), (String) mapa.get("naziv"),
                    (String) mapa.get("tipManifestacije"), Integer.parseInt((String) mapa.get("brojMesta")),
                    LocalDateTime.parse((CharSequence) mapa.get("datumIVremeOdrzavanja")), Double.parseDouble((String) mapa.get("cenaRegular")), false, lokacija.getId(),
                    (String) mapa.get("poster"), false);

            for (Lokacija l : lokacijaDAO.getNeobrisaneLokacije()) {
                if (l.getAdresa().equals(lokacija.getAdresa())) {
                    for (Manifestacija m : manifestacijaDAO.getNeobrisaneManifestacije()) {
                        if (m.getDatumIVremeOdrzavanja().getYear() == manifestacija.getDatumIVremeOdrzavanja().getYear()
                                && m.getDatumIVremeOdrzavanja().getMonth() == manifestacija.getDatumIVremeOdrzavanja().getMonth()
                                && m.getDatumIVremeOdrzavanja().getDayOfMonth() == manifestacija.getDatumIVremeOdrzavanja().getDayOfMonth()
                                && m.getLokacija() == l.getId()) {
                            return "Već postoji manifestacija na željenoj lokaciji u to vreme!";
                        }
                    }
                }
            }

            ZonedDateTime zdt1 = ZonedDateTime.of(manifestacija.getDatumIVremeOdrzavanja(), ZoneId.systemDefault());
            long date = zdt1.toInstant().toEpochMilli();

            if (date < System.currentTimeMillis()) {
                return "Odabrani datum je u prošlosti!";
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

        post("/manifestacije/izmenaManifestacije/:id/:idLokacije", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            int idLokacije = Integer.parseInt(req.params("idLokacije"));
            var mapa = g.fromJson(req.body(), HashMap.class);

            double broj = Double.parseDouble(String.valueOf(mapa.get("brojMesta")));

            Lokacija novaLokacija = new Lokacija(idLokacije, Double.parseDouble(String.valueOf(mapa.get("geografskaSirina"))),
                    Double.parseDouble(String.valueOf(mapa.get("geografskaDuzina"))), (String) mapa.get("adresa"),false);

            Manifestacija novaManifestacija = new Manifestacija(id, String.valueOf(mapa.get("naziv")), String.valueOf(mapa.get("tipManifestacije")),
                    (int)broj, LocalDateTime.parse(String.valueOf(mapa.get("datumIVremeOdrzavanja"))),
                    Double.parseDouble(String.valueOf(mapa.get("cenaRegular"))), Boolean.parseBoolean(String.valueOf(mapa.get("status"))),
                    idLokacije, String.valueOf(mapa.get("poster")), false);


            for (Lokacija l : lokacijaDAO.getNeobrisaneLokacije()) {
                if (l.getAdresa().equals(novaLokacija.getAdresa()) && l.getId() != idLokacije) {
                    for (Manifestacija m : manifestacijaDAO.getNeobrisaneManifestacije()) {
                        if (m.getDatumIVremeOdrzavanja().getYear() == novaManifestacija.getDatumIVremeOdrzavanja().getYear()
                                && m.getDatumIVremeOdrzavanja().getMonth() == novaManifestacija.getDatumIVremeOdrzavanja().getMonth()
                                && m.getDatumIVremeOdrzavanja().getDayOfMonth() == novaManifestacija.getDatumIVremeOdrzavanja().getDayOfMonth()
                                && m.getLokacija() == l.getId()) {
                            return -2;
                        }
                    }
                }
            }

            ZonedDateTime zdt1 = ZonedDateTime.of(novaManifestacija.getDatumIVremeOdrzavanja(), ZoneId.systemDefault());
            long date = zdt1.toInstant().toEpochMilli();

            if (date < System.currentTimeMillis()) {
                return -1;
            }

            lokacijaDAO.izmeniLokaciju(novaLokacija);

            kartaDAO.izmeniDatumIVreme(id, novaManifestacija.getDatumIVremeOdrzavanja());

            return manifestacijaDAO.izmeniManifestaciju(novaManifestacija);

        });

        post("/manifestacije/obrisi/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Manifestacija manifestacija = manifestacijaDAO.obrisiManifestaciju(id);
            kartaDAO.obrisiKarteZaManifestaciju(id);
            lokacijaDAO.obrisiLokacijuZaManifestaciju(manifestacija.getLokacija());
            return "Done";
        });


        // KARTE

        get("/karte", (req, res) -> "Done");

        get("/karte/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            if (ulogovanKorisnik.getUloga().equals(Uloga.PRODAVAC)) {
                return g.toJson(kartaDAO.findKarteByManifestacijaAndProdavacAndRezervisana(id, ulogovanProdavac.getKorisnickoIme()));
            }
            else if (ulogovanKorisnik.getUloga().equals(Uloga.ADMIN)) {
                return g.toJson(kartaDAO.findKarteByManifestacija(id));
            }
            return "Error";
        });

        get("/karteKupca", (req, res) -> g.toJson(kartaDAO.findKarteByKupac(ulogovanKupac.getKorisnickoIme())));

        post("/karte/pretraga", (req, res) -> {

            String naziv = req.queryParams("naziv").trim();
            long pocetniDatum = Long.parseLong(req.queryParams("pocetniDatum"));
            long krajnjiDatum = Long.parseLong(req.queryParams("krajnjiDatum"));

            double pocetnaCena = Double.parseDouble(req.queryParams("pocetnaCena").trim());
            double krajnjaCena = Double.parseDouble(req.queryParams("krajnjaCena").trim());

            String tip = req.queryParams("tip").trim();
            String status = req.queryParams("status").trim();
            String kriterijumSortiranja = req.queryParams("kriterijumSortiranja").trim();
            boolean opadajuce = Boolean.parseBoolean(req.queryParams("opadajuce").trim());

            List<Karta> karte = kartaDAO.searchFilterSort(naziv, pocetniDatum, krajnjiDatum,
                    pocetnaCena, krajnjaCena, tip, status, kriterijumSortiranja, opadajuce, manifestacijaDAO);

            res.type("application/json");
            return g.toJson(karte);

        });

        post("/karte/rezervisi/:id", (req, res) -> {

            int id = Integer.parseInt(req.params("id"));
            var mapa = g.fromJson(req.body(), HashMap.class);

            Manifestacija manifestacija = manifestacijaDAO.findManifestacijaById(id);

            Random rand = new Random();

            if (manifestacija.getStatus() && manifestacija.getBrojMesta() >=
                    Integer.parseInt(String.valueOf(mapa.get("kolicina")))) {
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem(rand.nextInt(1000), id, manifestacija.getCenaRegular(),
                        Integer.parseInt(String.valueOf(mapa.get("kolicina"))), TipKarte.valueOf((String) mapa.get("tipKarte")));
                shoppingCartItems.add(shoppingCartItem);

                return "Done";

            }

            return "Error";

        });

        post("/karte/korpa", (req, res) -> g.toJson(shoppingCartItems));

        post("/karte/cena", (req, res) -> {
            double ukupnaCena = 0;

            for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
                ukupnaCena += shoppingCartItem.getUkupnaCena();
            }

            return ukupnaCena;
        });

        post("/karte/cenaSaPopustom", (req, res) -> {
            double cena = Double.parseDouble(req.queryParams("cena").trim());
            return tipKupcaDAO.izracunajCenuSaPopustom(ulogovanKupac.getTip(), cena);
        });

        post("/karte/rezervisi", (req, res) -> {

            for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
                for (int i = 0; i < shoppingCartItem.getKolicina(); i++) {
                    Manifestacija manifestacija = manifestacijaDAO.findManifestacijaById(shoppingCartItem.getManifestacija());
                    Prodavac prodavac = korisnikDAO.findProdavacByManifestacija(manifestacija.getId());

                    String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                            +"lmnopqrstuvwxyz";
                    Random rnd = new Random();
                    StringBuilder sb = new StringBuilder(10);
                    for (int i2 = 0; i2 < 10; i2++)
                        sb.append(chars.charAt(rnd.nextInt(chars.length())));

                    Karta karta = new Karta(sb.toString(), manifestacija.getId(), manifestacija.getDatumIVremeOdrzavanja(),
                            shoppingCartItem.getUkupnaCena(), StatusKarte.REZERVISANA, shoppingCartItem.getTipKarte(),
                            false, ulogovanKupac.getKorisnickoIme(), prodavac.getKorisnickoIme(), null);
                    kartaDAO.dodajKartu(karta);
                    korisnikDAO.dodajKarteKupcu(karta.getId(), ulogovanKupac);
                    korisnikDAO.dodajKarteProdavcu(karta.getId(), prodavac);
                    korisnikDAO.dodajBodoveKupcu(ulogovanKupac, shoppingCartItem.getUkupnaCena());
                    tipKupcaDAO.azurirajTip(ulogovanKupac);
                    korisnikDAO.sacuvajKorisnike();
                }
            }
            shoppingCartItems.clear();
            return g.toJson(ulogovanKupac);
        });

        post("/karte/odustani", (req, res) -> {
            shoppingCartItems.clear();
            return "Done";
        });

        post("/karte/obrisi/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            for (int i = 0; i < shoppingCartItems.size(); i++) {
                if (shoppingCartItems.get(i).getId() == id) {
                    shoppingCartItems.remove(i);
                }
            }
            return "Done";
        });

        post("/karte/odustani/:id", (req, res) -> {
            String id = req.params("id");
            Karta karta = kartaDAO.findKartaById(id);

            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(karta.getDatumIVremeManifestacije().getYear(),
                    karta.getDatumIVremeManifestacije().getMonthValue()-1,
                    karta.getDatumIVremeManifestacije().getDayOfMonth());
            cal.add(Calendar.DATE, -7);

            if (System.currentTimeMillis() > cal.getTimeInMillis()) {
                return "Error";
            }

            ArrayList<Karta> karte = kartaDAO.odustaniOdKarte(karta, ulogovanKupac);
            tipKupcaDAO.azurirajTip(ulogovanKupac);
            kartaDAO.azurirajSumnjiv(ulogovanKupac);
            korisnikDAO.sacuvajKorisnike();

            return g.toJson(karte);
        });

        post("/karte/osvezi", (req, res) -> g.toJson(ulogovanKupac));

        post("/karte/obrisi-admin/:id", (req, res) -> {
            String id = req.params("id");
            kartaDAO.obrisiKartu(id);
            return "Done";
        });


        // KOMENTARI

        get("/komentari/:id", (req, res) -> {
            int manifestacija = Integer.parseInt(req.params("id"));
            if (ulogovanKorisnik.getUloga().equals(Uloga.KUPAC)) {
                return g.toJson(komentarDAO.findKomentareByManifestacijaAndStatusPrihvacen(manifestacija));
            }
            return g.toJson(komentarDAO.findKomentareByManifestacija(manifestacija));

        });

        post("/komentari/prihvati/:id", (req, res) -> {
            komentarDAO.prihvatiKomentar(Integer.parseInt(req.params("id")));
            return "Done";
        });

        post("/komentari/odbij/:id", (req, res) -> {
            komentarDAO.odbijKomentar(Integer.parseInt(req.params("id")));
            return "Done";
        });

        post("/komentari/posalji/:id", (req, res) -> {
            int manifestacija = Integer.parseInt(req.params("id"));
            var mapa = g.fromJson(req.body(), HashMap.class);
            Random rand = new Random();
            Komentar komentar = new Komentar(rand.nextInt(1000), ulogovanKupac.getKorisnickoIme(), manifestacija,
                    String.valueOf(mapa.get("tekstKomentara")), Double.parseDouble(String.valueOf(mapa.get("ocena"))),
                    false, StatusKomentara.NA_CEKANJU);
            komentarDAO.dodajKomentar(komentar);
            komentarDAO.sacuvajKomentare();
            return "Done";
        });
    }

}
