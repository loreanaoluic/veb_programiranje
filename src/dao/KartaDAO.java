package dao;

import model.Karta;
import model.enums.StatusKarte;
import model.enums.TipKarte;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
                Karta k = new Karta(karta[0], Integer.parseInt(karta[1]), LocalDateTime.parse(karta[2]),
                        Double.parseDouble(karta[3]), StatusKarte.valueOf(karta[4]), TipKarte.valueOf(karta[5]),
                        Boolean.parseBoolean(karta[6]), karta[7], karta[8]);
                listaKarata.add(k);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Karta findKartaById(String id) {
        for (Karta karta : listaKarata) {
            if (karta.getId().equals(id)) {
                return karta;
            }
        }
        return null;
    }

    public ArrayList<Karta> findKarteByManifestacijaAndProdavacAndRezervisana(int manifestacija, String prodavac) {
        ArrayList<Karta> karte = new ArrayList<>();
        for (Karta karta : listaKarata) {
            if (karta.getManifestacija() == manifestacija && karta.getProdavac().equals(prodavac) &&
                    karta.getStatusKarte().equals(StatusKarte.REZERVISANA)) {
                karte.add(karta);
            }
        }
        return karte;
    }
}
