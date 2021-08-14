package dao;

import model.Korisnik;
import model.Kupac;
import model.Prodavac;
import model.enums.Pol;
import model.enums.Uloga;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void dodajKupca(Kupac noviKupac) throws IOException {
        FileWriter file = new FileWriter("data/korisnici.csv", true);
        file.append("\n").append(noviKupac.getKorisnickoIme()).append(",").append(noviKupac.getLozinka()).append(",")
                .append(noviKupac.getIme()).append(",").append(noviKupac.getPrezime()).append(",")
                .append(String.valueOf(noviKupac.getPol())).append(",")
                .append(String.valueOf(noviKupac.getDatumRodjenja()))
                .append(",").append(String.valueOf(noviKupac.getUloga())).append(",")
                .append(String.valueOf(noviKupac.getBlokiran())).append(",")
                .append(String.valueOf(noviKupac.getObrisan())).append(",");
        int counter = 0;
        for (String karta : noviKupac.getKarte()) {
            file.append(karta);
            counter++;
            if (counter != noviKupac.getKarte().size()) {
                file.append(" ");
            }
        }
        file.append(",").append(String.valueOf(noviKupac.getBrojBodova())).append(",")
                .append(String.valueOf(noviKupac.getTip()));
        file.flush();
        file.close();
    }

    public void dodajProdavca(Prodavac noviProdavac) throws IOException {
        FileWriter file = new FileWriter("data/korisnici.csv", true);
        file.append("\n").append(noviProdavac.getKorisnickoIme()).append(",").append(noviProdavac.getLozinka()).append(",")
                .append(noviProdavac.getIme()).append(",").append(noviProdavac.getPrezime()).append(",")
                .append(String.valueOf(noviProdavac.getPol())).append(",")
                .append(String.valueOf(noviProdavac.getDatumRodjenja()))
                .append(",").append(String.valueOf(noviProdavac.getUloga())).append(",")
                .append(String.valueOf(noviProdavac.getBlokiran())).append(",")
                .append(String.valueOf(noviProdavac.getObrisan())).append(",");
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
        file.flush();
        file.close();
    }
}
