package dao;

import model.Karta;
import model.Kupac;
import model.enums.StatusKarte;
import model.enums.TipKarte;
import sort.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class KartaDAO {
    public static ArrayList<Karta> listaKarata = new ArrayList<>();


    public void ucitajKarte() {
        String line = "";
        String splitBy = ",";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("data/karte.csv"));
            while ((line = br.readLine()) != null)
            {
                String[] karta = line.split(splitBy);
                LocalDateTime datum = null;
                if (!karta[9].equals("null")) {
                    datum = LocalDateTime.parse(karta[9]);
                }
                Karta k = new Karta(karta[0], Integer.parseInt(karta[1]), LocalDateTime.parse(karta[2]),
                        Double.parseDouble(karta[3]), StatusKarte.valueOf(karta[4]), TipKarte.valueOf(karta[5]),
                        Boolean.parseBoolean(karta[6]), karta[7], karta[8], datum);
                listaKarata.add(k);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Karta> getNeobrisaneKarte() {
        ArrayList<Karta> karte = new ArrayList<>();
        for (Karta k : listaKarata) {
            if (!k.isObrisana()) {
                karte.add(k);
            }
        }
        return karte;
    }

    public Karta findKartaById(String id) {
        for (Karta karta : getNeobrisaneKarte()) {
            if (karta.getId().equals(id)) {
                return karta;
            }
        }
        return null;
    }

    public ArrayList<Karta> findKarteByManifestacijaAndProdavacAndRezervisana(int manifestacija, String prodavac) {
        ArrayList<Karta> karte = new ArrayList<>();
        for (Karta karta : getNeobrisaneKarte()) {
            if (karta.getManifestacija() == manifestacija && karta.getProdavac().equals(prodavac) &&
                    karta.getStatusKarte().equals(StatusKarte.REZERVISANA)) {
                karte.add(karta);
            }
        }
        return karte;
    }

    public ArrayList<Karta> findKarteByManifestacija(int manifestacija) {
        ArrayList<Karta> karte = new ArrayList<>();
        for (Karta karta : getNeobrisaneKarte()) {
            if (karta.getManifestacija() == manifestacija) {
                karte.add(karta);
            }
        }
        return karte;
    }

    public ArrayList<Karta> findKarteByKupac(String kupac) {
        ArrayList<Karta> karte = new ArrayList<>();
        for (Karta karta : getNeobrisaneKarte()) {
            if (karta.getKupac().equals(kupac)) {
                karte.add(karta);
            }
        }
        return karte;
    }

    public ArrayList<Karta> searchFilterSort(String naziv, long pocetniDatum, long krajnjiDatum, double pocetnaCena,
                                             double krajnjaCena, String tip, String status, String kriterijumSortiranja,
                                             boolean opadajuce, ManifestacijaDAO manifestacijaDAO) {

        ArrayList<Karta> pronadjene = new ArrayList<>();
        for(Karta k : getNeobrisaneKarte()) {

            String manifestacija = manifestacijaDAO.findManifestacijaById(k.getManifestacija()).getNaziv();
            long datumM = Timestamp.valueOf(k.getDatumIVremeManifestacije()).getTime();

            if(manifestacija.toUpperCase().contains(naziv.toUpperCase()) &&
                    k.getCena() >= pocetnaCena && k.getCena() <= krajnjaCena &&
                    datumM >= pocetniDatum && datumM <= krajnjiDatum) {
                if(!tip.equals("SVE")) {
                    if(!k.getTipKarte().equals(TipKarte.valueOf(tip))) {
                        continue;
                    }
                }
                if(!status.equals("SVE")) {
                    if(!k.getStatusKarte().equals(StatusKarte.valueOf(status))) {
                        continue;
                    }
                }
                pronadjene.add(k);
            }
        }

        switch (kriterijumSortiranja) {
            case "MANIFESTACIJA" -> pronadjene.sort(new KartePoManifestaciji());
            case "DATUM I VREME" -> pronadjene.sort(new KartePoDatumuIVremenu());
            case "CENA" -> pronadjene.sort(new KartePoCeni());
        }

        if (opadajuce) {
            Collections.reverse(pronadjene);
        }

        return pronadjene;
    }

    public void dodajKartu(Karta karta) throws IOException {
        FileWriter file = new FileWriter("data/karte.csv", true);
        String datum = "null";
        if (karta.getStatusKarte().equals(StatusKarte.ODUSTANAK)) {
            datum = String.valueOf(karta.getVremeOtkazivanja());
        }
        file.append(karta.getId()).append(",")
                .append(String.valueOf(karta.getManifestacija())).append(",")
                .append(String.valueOf(karta.getDatumIVremeManifestacije())).append(",")
                .append(String.valueOf(karta.getCena())).append(",")
                .append(String.valueOf(karta.getStatusKarte())).append(",")
                .append(String.valueOf(karta.getTipKarte())).append(",")
                .append(String.valueOf(karta.isObrisana())).append(",")
                .append(String.valueOf(karta.getKupac())).append(",")
                .append(String.valueOf(karta.getProdavac())).append(",")
                .append(datum).append("\n");
        file.flush();
        file.close();

        listaKarata.add(karta);
    }

    public void sacuvajKarte() throws IOException {
        FileWriter f = new FileWriter("data/karte.csv", false);
        for (Karta karta : listaKarata) {
            String datum = "null";
            if (karta.getStatusKarte().equals(StatusKarte.ODUSTANAK)) {
                datum = String.valueOf(karta.getVremeOtkazivanja());
            }
            f.append(karta.getId()).append(",")
                    .append(String.valueOf(karta.getManifestacija())).append(",")
                    .append(String.valueOf(karta.getDatumIVremeManifestacije())).append(",")
                    .append(String.valueOf(karta.getCena())).append(",")
                    .append(String.valueOf(karta.getStatusKarte())).append(",")
                    .append(String.valueOf(karta.getTipKarte())).append(",")
                    .append(String.valueOf(karta.isObrisana())).append(",")
                    .append(String.valueOf(karta.getKupac())).append(",")
                    .append(String.valueOf(karta.getProdavac())).append(",")
                    .append(datum).append("\n");
        }
        f.flush();
        f.close();
    }

    public ArrayList<Karta> odustaniOdKarte(Karta karta, Kupac kupac) throws IOException {
        karta.setStatusKarte(StatusKarte.ODUSTANAK);
        karta.setVremeOtkazivanja(LocalDateTime.now());
        sacuvajKarte();

        int stariBodovi = kupac.getBrojBodova();
        kupac.setBrojBodova((int) (stariBodovi - karta.getCena() / 1000 * 133 * 4));

        return getNeobrisaneKarte();
    }

    public Karta obrisiKartu(String id) throws IOException {
        Karta karta = findKartaById(id);
        karta.setObrisana(true);
        sacuvajKarte();

        return karta;
    }

    public void obrisiKarteZaManifestaciju(int idManifestacije) throws IOException {
        for (Karta k : getNeobrisaneKarte()) {
            if (k.getManifestacija() == idManifestacije) {
                k.setObrisana(true);
            }
        }
        sacuvajKarte();
    }

    public void obrisiKarteZaKupca(String korisnickoIme) throws IOException {
        for (Karta k : getNeobrisaneKarte()) {
            if (k.getKupac().equals(korisnickoIme)) {
                k.setObrisana(true);
            }
        }
        sacuvajKarte();
    }

    public void obrisiKarteZaProdavca(String korisnickoIme) throws IOException {
        for (Karta k : getNeobrisaneKarte()) {
            if (k.getProdavac().equals(korisnickoIme)) {
                k.setObrisana(true);
            }
        }
        sacuvajKarte();
    }

    public void azurirajSumnjiv(Kupac kupac) {
        int brojOtkazivanja = 0;
        for (Karta k : getNeobrisaneKarte()) {
            if (k.getKupac().equals(kupac.getKorisnickoIme()) && k.getStatusKarte().equals(StatusKarte.ODUSTANAK)) {
                ZonedDateTime zdt = ZonedDateTime.of(k.getVremeOtkazivanja(), ZoneId.systemDefault());
                long datumOtkazivanja = zdt.toInstant().toEpochMilli();

                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonthValue() - 2,
                        LocalDateTime.now().getDayOfMonth());
                long mesecDanaRanije = cal.getTimeInMillis();

                if (datumOtkazivanja > mesecDanaRanije) {
                    brojOtkazivanja++;
                }
            }
        }
        if (brojOtkazivanja > 5) {
            kupac.setSumnjiv(true);
        }
    }

    public void izmeniDatumIVreme(int manifestacija, LocalDateTime datumIVremeManifestacije) throws IOException {
        for (Karta k : listaKarata) {
            if (k.getManifestacija() == manifestacija) {
                k.setDatumIVremeManifestacije(datumIVremeManifestacije);
            }
        }
        sacuvajKarte();
    }
}
