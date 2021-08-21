package dao;

import model.Manifestacija;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
}
