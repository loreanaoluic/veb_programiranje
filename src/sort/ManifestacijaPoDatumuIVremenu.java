package sort;

import model.Manifestacija;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class ManifestacijaPoDatumuIVremenu implements Comparator<Manifestacija> {

    @Override
    public int compare(Manifestacija o1, Manifestacija o2) {
        ZonedDateTime zdt1 = ZonedDateTime.of(o1.getDatumIVremeOdrzavanja(), ZoneId.systemDefault());
        long date1 = zdt1.toInstant().toEpochMilli();

        ZonedDateTime zdt2 = ZonedDateTime.of(o2.getDatumIVremeOdrzavanja(), ZoneId.systemDefault());
        long date2 = zdt2.toInstant().toEpochMilli();

        return Long.compare(date1, date2);
    }
}
