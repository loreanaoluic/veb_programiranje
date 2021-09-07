package dao;

import model.Komentar;
import model.enums.StatusKomentara;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class KomentarDAO {
    public static ArrayList<Komentar> listaKomentara = new ArrayList<>();

    public void ucitajKomentare() {
        String line = "";
        String splitBy = ",";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("data/komentari.csv"));
            while ((line = br.readLine()) != null)
            {
                String[] komentar = line.split(splitBy);
                Komentar k = new Komentar(Integer.parseInt(komentar[0]), komentar[1], Integer.parseInt(komentar[2]),
                        komentar[3], Double.parseDouble(komentar[4]), Boolean.parseBoolean(komentar[5]),
                        StatusKomentara.valueOf(komentar[6]));
                listaKomentara.add(k);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Komentar findKomentarById(int id) {
        for (Komentar komentar : getNeobrisaneKomentare()) {
            if (komentar.getId() == id) {
                return komentar;
            }
        }
        return null;
    }

    public ArrayList<Komentar> findKomentareByManifestacija(int manifestacija) {
        ArrayList<Komentar> komentari = new ArrayList<>();
        for (Komentar komentar : getNeobrisaneKomentare()) {
            if (komentar.getManifestacija() == manifestacija) {
                komentari.add(komentar);
            }
        }
        return komentari;
    }

    public ArrayList<Komentar> findKomentareByManifestacijaAndStatusPrihvacen(int manifestacija) {
        ArrayList<Komentar> komentari = new ArrayList<>();
        for (Komentar komentar : getNeobrisaneKomentare()) {
            if (komentar.getManifestacija() == manifestacija && komentar.getStatus().equals(StatusKomentara.PRIHVACEN)) {
                komentari.add(komentar);
            }
        }
        return komentari;
    }

    public void dodajKomentar(Komentar komentar) throws IOException {
        FileWriter file = new FileWriter("data/komentari.csv", true);
        file.append(String.valueOf(komentar.getId())).append(",")
                .append(komentar.getKupac()).append(",")
                .append(String.valueOf(komentar.getManifestacija())).append(",")
                .append(komentar.getTekstKomentara()).append(",")
                .append(String.valueOf(komentar.getOcena())).append(",")
                .append(String.valueOf(komentar.isObrisan())).append(",")
                .append(String.valueOf(komentar.getStatus())).append("\n");
        file.flush();
        file.close();

        listaKomentara.add(komentar);
    }

    public void sacuvajKomentare() throws IOException {
        FileWriter f = new FileWriter("data/komentari.csv", false);
        for (Komentar komentar : listaKomentara) {
            f.append(String.valueOf(komentar.getId())).append(",")
                    .append(komentar.getKupac()).append(",")
                    .append(String.valueOf(komentar.getManifestacija())).append(",")
                    .append(komentar.getTekstKomentara()).append(",")
                    .append(String.valueOf(komentar.getOcena())).append(",")
                    .append(String.valueOf(komentar.isObrisan())).append(",")
                    .append(String.valueOf(komentar.getStatus())).append("\n");
        }
        f.flush();
        f.close();
    }

    public ArrayList<Komentar> getNeobrisaneKomentare() {
        ArrayList<Komentar> komentari = new ArrayList<>();
        for (Komentar k : listaKomentara) {
            if (!k.isObrisan()) {
                komentari.add(k);
            }
        }
        return komentari;
    }

    public void prihvatiKomentar(int k) throws IOException {
        Komentar komentar = findKomentarById(k);
        komentar.setStatus(StatusKomentara.PRIHVACEN);
        sacuvajKomentare();
    }

    public void odbijKomentar(int k) throws IOException {
        Komentar komentar = findKomentarById(k);
        komentar.setStatus(StatusKomentara.ODBIJEN);
        sacuvajKomentare();
    }
}
