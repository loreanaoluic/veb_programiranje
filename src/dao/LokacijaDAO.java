package dao;

import model.Lokacija;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LokacijaDAO {
    public static ArrayList<Lokacija> listaLokacija = new ArrayList<>();

    public void ucitajLokacije() {
        String line = "";
        String splitBy = ",";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("data/lokacije.csv"));
            while ((line = br.readLine()) != null)
            {
                String[] lokacija = line.split(splitBy);
                Lokacija l = new Lokacija(Integer.parseInt(lokacija[0]), Double.parseDouble(lokacija[1]),
                        Double.parseDouble(lokacija[2]), lokacija[3]);
                listaLokacija.add(l);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Lokacija findLokacijaById(int id) {
        for (Lokacija lokacija : listaLokacija) {
            if (lokacija.getId() == id) {
                return lokacija;
            }
        }
        return null;
    }
}
