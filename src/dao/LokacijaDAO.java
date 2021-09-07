package dao;

import model.Lokacija;
import model.Manifestacija;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
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
                        Double.parseDouble(lokacija[2]), lokacija[3], Boolean.parseBoolean(lokacija[4]));
                listaLokacija.add(l);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Lokacija findLokacijaById(int id) {
        for (Lokacija lokacija : getNeobrisaneLokacije()) {
            if (lokacija.getId() == id) {
                return lokacija;
            }
        }
        return null;
    }

    public void dodajLokaciju(Lokacija lokacija) throws IOException {
        FileWriter file = new FileWriter("data/lokacije.csv", true);
        file.append(String.valueOf(lokacija.getId())).append(",")
                .append(String.valueOf(lokacija.getGeografskaSirina())).append(",")
                .append(String.valueOf(lokacija.getGeografskaDuzina())).append(",")
                .append(lokacija.getAdresa()).append(",")
                .append(String.valueOf(lokacija.isObrisana())).append("\n");
        file.flush();
        file.close();

        listaLokacija.add(lokacija);
    }

    public void sacuvajLokacije() throws IOException {
        FileWriter f = new FileWriter("data/lokacije.csv", false);
        for (Lokacija lokacija : listaLokacija) {
            f.append(String.valueOf(lokacija.getId())).append(",")
                    .append(String.valueOf(lokacija.getGeografskaSirina())).append(",")
                    .append(String.valueOf(lokacija.getGeografskaDuzina())).append(",")
                    .append(lokacija.getAdresa()).append(",")
                    .append(String.valueOf(lokacija.isObrisana())).append("\n");
        }
        f.flush();
        f.close();
    }

    public void izmeniLokaciju(Lokacija novaLokacija) throws IOException {
        Lokacija lokacija = findLokacijaById(novaLokacija.getId());
        lokacija.setGeografskaDuzina(lokacija.getGeografskaDuzina());
        lokacija.setGeografskaSirina(lokacija.getGeografskaSirina());
        lokacija.setAdresa(novaLokacija.getAdresa());
        sacuvajLokacije();
    }

    public ArrayList<Lokacija> getNeobrisaneLokacije() {
        ArrayList<Lokacija> lokacije = new ArrayList<>();
        for (Lokacija l : listaLokacija) {
            if (!l.isObrisana()) {
                lokacije.add(l);
            }
        }
        return lokacije;
    }

    public void obrisiLokacijuZaManifestaciju(int idLokacije) throws IOException {
        for (Lokacija l : getNeobrisaneLokacije()) {
            if (l.getId() == idLokacije) {
                l.setObrisana(true);
            }
        }
        sacuvajLokacije();
    }
}
