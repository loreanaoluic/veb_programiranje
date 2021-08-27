package dao;

import model.Lokacija;
import model.Manifestacija;
import sort.ManifestacijaPoCeni;
import sort.ManifestacijaPoDatumuIVremenu;
import sort.ManifestacijaPoLokaciji;
import sort.ManifestacijaPoNazivu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class ManifestacijaDAO {
    public static ArrayList<Manifestacija> listaManifestacija = new ArrayList<>();

    public void ucitajManifestacije() {
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/manifestacije.csv"));
            while ((line = br.readLine()) != null) {
                String[] manifestacija = line.split(splitBy);
                Manifestacija m = new Manifestacija(Integer.parseInt(manifestacija[0]), manifestacija[1], manifestacija[2],
                        Integer.parseInt(manifestacija[3]), LocalDateTime.parse(manifestacija[4]), Double.parseDouble(manifestacija[5]),
                        Boolean.parseBoolean(manifestacija[6]), Integer.parseInt(manifestacija[7]), manifestacija[8],
                        Boolean.parseBoolean(manifestacija[9]));
                listaManifestacija.add(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sort();
    }

    public static ArrayList<Manifestacija> getListaManifestacija() {
        return listaManifestacija;
    }

    public Manifestacija findManifestacijaById (int id) {
        for (Manifestacija manifestacija : listaManifestacija) {
            if (manifestacija.getId() == id) {
                return manifestacija;
            }
        }
        return null;
    }

    public static void sort() {
        Collections.sort(listaManifestacija);
        Collections.reverse(listaManifestacija);
    }

    public ArrayList<Manifestacija> getAktuelneManifestacije() {
        ArrayList<Manifestacija> manifestacije = new ArrayList<>();
        for (Manifestacija m : listaManifestacija) {
            if (!m.isObrisana()) {
                manifestacije.add(m);
            }
        }
        return manifestacije;
    }

    public ArrayList<Manifestacija> searchFilterSort(String naziv, long pocetniDatum, long krajnjiDatum, String lokacija,
                                                     double pocetnaCena, double krajnjaCena, String tip,
                                                     String kriterijumSortiranja, boolean opadajuce, boolean rasprodata,
                                                     LokacijaDAO lokacijaDAO) {

        ArrayList<Manifestacija> pronadjene = new ArrayList<>();
        for(Manifestacija m : getAktuelneManifestacije()) {

            String lokacijaM = lokacijaDAO.findLokacijaById(m.getId()).getAdresa();
            long datumM = Timestamp.valueOf(m.getDatumIVremeOdrzavanja()).getTime();

            if(m.getNaziv().toUpperCase().contains(naziv.toUpperCase()) &&
                    lokacijaM.toUpperCase().contains(lokacija.toUpperCase()) &&
                    m.getCenaRegular() >= pocetnaCena && m.getCenaRegular() <= krajnjaCena &&
                    datumM >= pocetniDatum && datumM <= krajnjiDatum) {
                if(!rasprodata) {
                    if(m.getBrojMesta() == 0) {
                        continue;
                    }
                }
                if(!tip.equals("SVE")) {
                    if(!m.getTipManifestacije().equals(tip)) {
                        continue;
                    }
                }
                pronadjene.add(m);
            }
        }

        switch (kriterijumSortiranja) {
            case "NAZIV" -> Collections.sort(pronadjene, new ManifestacijaPoNazivu());
            case "DATUM I VREME" -> Collections.sort(pronadjene, new ManifestacijaPoDatumuIVremenu());
            case "CENA" -> Collections.sort(pronadjene, new ManifestacijaPoCeni());
            case "LOKACIJA" -> Collections.sort(pronadjene, new ManifestacijaPoLokaciji());
        }

        if (opadajuce) {
            Collections.reverse(pronadjene);
        }

        return pronadjene;
    }
}