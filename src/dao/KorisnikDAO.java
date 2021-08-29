package dao;

import model.*;
import model.enums.Pol;
import model.enums.StatusKarte;
import model.enums.TipKarte;
import model.enums.Uloga;
import sort.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KorisnikDAO {

    public static ArrayList<Korisnik> listaSvihKorisnika = new ArrayList<>();
    public static ArrayList<Kupac> listaKupaca = new ArrayList<>();
    public static ArrayList<Prodavac> listaProdavaca = new ArrayList<>();
    public static ArrayList<Korisnik> listaAdmina = new ArrayList<>();

    public void ucitajKorisnike() {

        String line = "";
        String splitBy = ",";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("data/korisnici.csv"));
            while ((line = br.readLine()) != null)
            {
                String[] korisnik = line.split(splitBy);
                Korisnik noviKorisnik = new Korisnik(korisnik[0], korisnik[1], korisnik[2], korisnik[3],
                        Pol.valueOf(korisnik[4]), LocalDate.parse(korisnik[5]), Uloga.valueOf(korisnik[6]),
                        Boolean.parseBoolean(korisnik[7]), Boolean.parseBoolean(korisnik[8]));
                listaSvihKorisnika.add(noviKorisnik);

                if (noviKorisnik.getUloga().equals(Uloga.KUPAC)) {
                    ArrayList<String> karte = new ArrayList<>();

                    if (korisnik[9].contains(" ")) {
                        String[] nizKarata = korisnik[9].split(" ");
                        karte.addAll(Arrays.asList(nizKarata));
                    } else {
                        karte.add(korisnik[9]);
                    }

                    Kupac kupac = new Kupac(noviKorisnik.getKorisnickoIme(), noviKorisnik.getLozinka(),
                            noviKorisnik.getIme(), noviKorisnik.getPrezime(), noviKorisnik.getPol(),
                            noviKorisnik.getDatumRodjenja(), noviKorisnik.getUloga(), noviKorisnik.getBlokiran(),
                            noviKorisnik.getObrisan(), karte, Integer.parseInt(korisnik[10]), Integer.parseInt(korisnik[11]));

                    listaKupaca.add(kupac);
                }
                else if (noviKorisnik.getUloga().equals(Uloga.ADMIN)) {
                    listaAdmina.add(noviKorisnik);
                }
                else if (noviKorisnik.getUloga().equals(Uloga.PRODAVAC)) {
                    ArrayList<Integer> manifestacije = new ArrayList<>();
                    ArrayList<String> karte = new ArrayList<>();

                    if (korisnik[9].contains(" ")) {
                        String[] nizManifestacija = korisnik[9].split(" ");
                        for (String manifestacija : nizManifestacija) {
                            manifestacije.add(Integer.parseInt(manifestacija));
                        }
                    } else {
                        manifestacije.add(Integer.parseInt(korisnik[9]));
                    }

                    if (korisnik[10].contains(" ")) {
                        String[] nizKarata = korisnik[10].split(" ");
                        karte.addAll(Arrays.asList(nizKarata));
                    } else {
                        karte.add(korisnik[10]);
                    }

                    Prodavac prodavac = new Prodavac(noviKorisnik.getKorisnickoIme(), noviKorisnik.getLozinka(),
                            noviKorisnik.getIme(), noviKorisnik.getPrezime(), noviKorisnik.getPol(),
                            noviKorisnik.getDatumRodjenja(), noviKorisnik.getUloga(), noviKorisnik.getBlokiran(),
                            noviKorisnik.getObrisan(), manifestacije, karte);

                    listaProdavaca.add(prodavac);

                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Korisnik findUserByUsernameAndPassword(String korisnickoIme, String lozinka) {
        for (Korisnik k : listaSvihKorisnika) {
            if (k.getKorisnickoIme().equals(korisnickoIme) && k.getLozinka().equals(lozinka)) {
                return k;
            }
        }
        return null;
    }

    public Korisnik findUserByUsername(String korisnickoIme) {
        for (Korisnik k : listaSvihKorisnika) {
            if (k.getKorisnickoIme().equals(korisnickoIme)) {
                return k;
            }
        }
        return null;
    }

    public Kupac findKupacByUsername(String korisnickoIme) {
        for (Kupac k : listaKupaca) {
            if (k.getKorisnickoIme().equals(korisnickoIme)) {
                return k;
            }
        }
        return null;
    }

    public Prodavac findProdavacByUsername(String korisnickoIme) {
        for (Prodavac k : listaProdavaca) {
            if (k.getKorisnickoIme().equals(korisnickoIme)) {
                return k;
            }
        }
        return null;
    }

    public Prodavac findProdavacByManifestacija(int manifestacija) {
        for (Prodavac p : listaProdavaca) {
            for (int m : p.getManifestacije()) {
                if (m == manifestacija) {
                    return p;
                }
            }
        }
        return null;
    }

    public void dodajKorisnika(Korisnik novi) throws IOException {
        FileWriter file = new FileWriter("data/korisnici.csv", true);
        file.append(novi.getKorisnickoIme()).append(",").append(novi.getLozinka()).append(",")
                .append(novi.getIme()).append(",").append(novi.getPrezime()).append(",")
                .append(String.valueOf(novi.getPol())).append(",")
                .append(String.valueOf(novi.getDatumRodjenja()))
                .append(",").append(String.valueOf(novi.getUloga())).append(",")
                .append(String.valueOf(novi.getBlokiran())).append(",")
                .append(String.valueOf(novi.getObrisan()));
        file.flush();
        file.close();

    }

    public void dodajKupca(Kupac noviKupac) throws IOException {
        dodajKorisnika(new Korisnik(noviKupac.getKorisnickoIme(), noviKupac.getLozinka(), noviKupac.getIme(),
                noviKupac.getPrezime(), noviKupac.getPol(), noviKupac.getDatumRodjenja(), noviKupac.getUloga(),
                noviKupac.getBlokiran(), noviKupac.getObrisan()));
        FileWriter file = new FileWriter("data/korisnici.csv", true);
        file.append(",");
        int counter = 0;
        for (String karta : noviKupac.getKarte()) {
            file.append(karta);
            counter++;
            if (counter != noviKupac.getKarte().size()) {
                file.append(" ");
            }
        }
        file.append(",").append(String.valueOf(noviKupac.getBrojBodova())).append(",")
                .append(String.valueOf(noviKupac.getTip())).append("\n");
        file.flush();
        file.close();

    }

    public void dodajProdavca(Prodavac noviProdavac) throws IOException {
        dodajKorisnika(new Korisnik(noviProdavac.getKorisnickoIme(), noviProdavac.getLozinka(), noviProdavac.getIme(),
                noviProdavac.getPrezime(), noviProdavac.getPol(), noviProdavac.getDatumRodjenja(), noviProdavac.getUloga(),
                noviProdavac.getBlokiran(), noviProdavac.getObrisan()));
        FileWriter file = new FileWriter("data/korisnici.csv", true);
        file.append(",");
        int counter = 0;
        for (Integer manifestacija : noviProdavac.getManifestacije()) {
            file.append(Integer.toString(manifestacija));
            counter++;
            if (counter != noviProdavac.getManifestacije().size()) {
                file.append(" ");
            }
        }
        file.append(",");
        int counter2 = 0;
        for (String karta : noviProdavac.getKarte()) {
            file.append(karta);
            counter2++;
            if (counter2 != noviProdavac.getKarte().size()) {
                file.append(" ");
            }
        }
        file.append("\n");
        file.flush();
        file.close();

    }

    public void izmeniKorisnika(Korisnik izmenjen) throws IOException {
        Korisnik stari = findUserByUsername(izmenjen.getKorisnickoIme());
        listaSvihKorisnika.set(listaSvihKorisnika.indexOf(stari), izmenjen);

        if (izmenjen.getUloga().equals(Uloga.KUPAC)) {
            Kupac kupac = findKupacByUsername(izmenjen.getKorisnickoIme());
            listaKupaca.remove(kupac);
            Kupac novi = new Kupac(izmenjen.getKorisnickoIme(), izmenjen.getLozinka(), izmenjen.getIme(),
                    izmenjen.getPrezime(), izmenjen.getPol(), izmenjen.getDatumRodjenja(), izmenjen.getUloga(),
                    izmenjen.getBlokiran(), izmenjen.getObrisan(), kupac.getKarte(), kupac.getBrojBodova(), kupac.getTip());
            listaKupaca.add(novi);
        } else if (izmenjen.getUloga().equals(Uloga.PRODAVAC)) {
            Prodavac prodavac = findProdavacByUsername(izmenjen.getKorisnickoIme());
            listaProdavaca.remove(prodavac);
            Prodavac novi = new Prodavac(izmenjen.getKorisnickoIme(), izmenjen.getLozinka(), izmenjen.getIme(),
                    izmenjen.getPrezime(), izmenjen.getPol(), izmenjen.getDatumRodjenja(), izmenjen.getUloga(),
                    izmenjen.getBlokiran(), izmenjen.getObrisan(), prodavac.getManifestacije(), prodavac.getKarte());
            listaProdavaca.add(novi);
        } else if (izmenjen.getUloga().equals(Uloga.ADMIN)) {
            listaAdmina.remove(stari);
            listaAdmina.add(izmenjen);
        }

        sacuvajKorisnike();
    }

    public void sacuvajKorisnike() throws IOException {
        FileWriter fw = new FileWriter("data/korisnici.csv",false);
        fw.flush();
        fw.close();
        for (Korisnik admin : listaAdmina) {
            dodajKorisnika(admin);
            FileWriter f = new FileWriter("data/korisnici.csv",true);
            f.append("\n");
            f.flush();
            f.close();
        }
        for (Prodavac prodavac : listaProdavaca) {
            dodajProdavca(prodavac);
        }
        for (Kupac kupac : listaKupaca) {
            dodajKupca(kupac);
        }
    }

    public static ArrayList<Korisnik> getListaSvihKorisnika() {
        return listaSvihKorisnika;
    }

    public ArrayList<Korisnik> searchFilterSort(String ime, String prezime, String korisnickoIme, String kriterijumSortiranja,
                                             String uloga, boolean opadajuce) {

        ArrayList<Korisnik> pronadjene = new ArrayList<>();
        for(Korisnik k : listaSvihKorisnika) {

            if(k.getIme().toUpperCase().contains(ime.toUpperCase()) &&
                    k.getPrezime().toUpperCase().contains(prezime.toUpperCase()) &&
                    k.getKorisnickoIme().toUpperCase().contains(korisnickoIme.toUpperCase())) {
                if(!uloga.equals("SVE")) {
                    if(!k.getUloga().equals(Uloga.valueOf(uloga))) {
                        continue;
                    }
                }
                pronadjene.add(k);
            }
        }

        switch (kriterijumSortiranja) {
            case "IME" -> pronadjene.sort(new KorisniciPoImenu());
            case "PREZIME" -> pronadjene.sort(new KorisniciPoPrezimenu());
            case "KORISNICKO_IME" -> pronadjene.sort(new KorisniciPoKorisnickomImenu());
            case "BROJ_BODOVA" -> pronadjene.sort(new KorisniciPoBrojuBodova());
        }

        if (opadajuce) {
            Collections.reverse(pronadjene);
        }

        return pronadjene;
    }

    public void dodajKarteProdavcu(String karta, Prodavac prodavac) throws IOException {
        List<String> karte = prodavac.getKarte();
        karte.add(karta);
        prodavac.setKarte(karte);
        sacuvajKorisnike();
    }

    public void dodajKarteKupcu(String karta, Kupac kupac) throws IOException {
        List<String> karte = kupac.getKarte();
        karte.add(karta);
        kupac.setKarte(karte);
        sacuvajKorisnike();
    }

    public void dodajBodoveKupcu(Kupac kupac, double cena) throws IOException {
        double c = kupac.getBrojBodova() + cena / 1000 * 133;
        kupac.setBrojBodova((int) c);
        sacuvajKorisnike();
    }
}
