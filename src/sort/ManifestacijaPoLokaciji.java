package sort;

import dao.LokacijaDAO;
import model.Manifestacija;

import java.util.Comparator;

public class ManifestacijaPoLokaciji implements Comparator<Manifestacija> {

    @Override
    public int compare(Manifestacija o1, Manifestacija o2) {
        LokacijaDAO lokacijaDAO = new LokacijaDAO();
        lokacijaDAO.ucitajLokacije();
        return lokacijaDAO.findLokacijaById(o1.getLokacija()).getAdresa()
                .compareTo(lokacijaDAO.findLokacijaById(o2.getLokacija()).getAdresa());
    }
}
