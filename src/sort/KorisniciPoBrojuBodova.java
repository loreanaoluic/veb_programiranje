package sort;

import dao.KorisnikDAO;
import model.Korisnik;
import model.Kupac;
import model.enums.Uloga;

import java.util.Comparator;

public class KorisniciPoBrojuBodova implements Comparator<Korisnik> {
    @Override
    public int compare(Korisnik o1, Korisnik o2) {
        if (o1.getUloga().equals(Uloga.KUPAC) && o2.getUloga().equals(Uloga.KUPAC)) {
            KorisnikDAO korisnikDAO = new KorisnikDAO();
            korisnikDAO.ucitajKorisnike();
            Kupac k1 = korisnikDAO.findKupacByUsername(o1.getKorisnickoIme());
            Kupac k2 = korisnikDAO.findKupacByUsername(o2.getKorisnickoIme());
            return Integer.compare(k1.getBrojBodova(), k2.getBrojBodova());
        }
        return 0;
    }
}
