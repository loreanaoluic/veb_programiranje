package sort;

import model.Korisnik;

import java.util.Comparator;

public class KorisniciPoPrezimenu implements Comparator<Korisnik> {
    @Override
    public int compare(Korisnik o1, Korisnik o2) {
        return o1.getPrezime().compareTo(o2.getPrezime());
    }
}
