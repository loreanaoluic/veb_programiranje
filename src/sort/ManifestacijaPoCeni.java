package sort;

import model.Manifestacija;

import java.util.Comparator;

public class ManifestacijaPoCeni implements Comparator<Manifestacija> {

    @Override
    public int compare(Manifestacija o1, Manifestacija o2) {
        return Double.compare(o1.getCenaRegular(), o2.getCenaRegular());
    }
}
