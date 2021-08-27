package sort;

import model.Manifestacija;

import java.util.Comparator;

public class ManifestacijaPoNazivu implements Comparator<Manifestacija> {

    @Override
    public int compare(Manifestacija o1, Manifestacija o2) {
        return o1.getNaziv().compareTo(o2.getNaziv());
    }
}
