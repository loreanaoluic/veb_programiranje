package sort;

import dao.ManifestacijaDAO;
import model.Karta;

import java.util.Comparator;

public class KartePoManifestaciji implements Comparator<Karta> {

    @Override
    public int compare(Karta o1, Karta o2) {
        ManifestacijaDAO manifestacijaDAO = new ManifestacijaDAO();
        manifestacijaDAO.ucitajManifestacije();
        return manifestacijaDAO.findManifestacijaById(o1.getManifestacija()).getNaziv()
                .compareTo(manifestacijaDAO.findManifestacijaById(o2.getManifestacija()).getNaziv());
    }
}
