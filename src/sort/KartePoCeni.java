package sort;

import model.Karta;

import java.util.Comparator;

public class KartePoCeni implements Comparator<Karta> {

    @Override
    public int compare(Karta o1, Karta o2) {
        return Double.compare(o1.getCena(), o2.getCena());
    }
}
