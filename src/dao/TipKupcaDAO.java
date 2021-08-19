package dao;

import model.TipKupca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TipKupcaDAO {
    public static ArrayList<TipKupca> listaTipovaKupaca = new ArrayList<>();

    public void ucitajTipoveKupaca() {
        String line = "";
        String splitBy = ",";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("data/tipoviKupaca.csv"));
            while ((line = br.readLine()) != null)
            {
                String[] tipKupca = line.split(splitBy);
                TipKupca tip = new TipKupca(Integer.parseInt(tipKupca[0]), tipKupca[1], Double.parseDouble(tipKupca[2]),
                        Integer.parseInt(tipKupca[3]));
                listaTipovaKupaca.add(tip);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public TipKupca findTipKupca(int id) {
        for (TipKupca tip : listaTipovaKupaca) {
            if (tip.getId() == id) {
                return tip;
            }
        }
        return null;
    }
}
