package sort;

import model.Korisnik;

import java.util.Comparator;

public class KorisniciPoImenu implements Comparator<Korisnik> {
    @Override
    public int compare(Korisnik o1, Korisnik o2) {
        return o1.getIme().compareTo(o2.getIme());
    }
}
