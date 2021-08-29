package sort;

import model.Korisnik;

import java.util.Comparator;

public class KorisniciPoKorisnickomImenu implements Comparator<Korisnik> {
    @Override
    public int compare(Korisnik o1, Korisnik o2) {
        return o1.getKorisnickoIme().compareTo(o2.getKorisnickoIme());
    }
}
