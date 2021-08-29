package sort;

import model.Karta;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class KartePoDatumuIVremenu implements Comparator<Karta> {
    @Override
    public int compare(Karta o1, Karta o2) {
        ZonedDateTime zdt1 = ZonedDateTime.of(o1.getDatumIVremeManifestacije(), ZoneId.systemDefault());
        long date1 = zdt1.toInstant().toEpochMilli();

        ZonedDateTime zdt2 = ZonedDateTime.of(o2.getDatumIVremeManifestacije(), ZoneId.systemDefault());
        long date2 = zdt2.toInstant().toEpochMilli();

        return Long.compare(date1, date2);
    }
}
