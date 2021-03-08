package beans;

import java.time.LocalDate;
import java.util.List;

import beans.enums.Pol;
import beans.enums.Uloga;

public class Kupac extends Korisnik {
	private List<String> sveKarte;
	private int brojBodova;
	private TipKupca tipKupca;
	
	public Kupac() {
		super();
	}

	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, LocalDate datumRodjenja,
			Uloga uloga, List<String> sveKarte, int brojBodova, TipKupca tipKupca) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, uloga);
		this.sveKarte = sveKarte;
		this.brojBodova = brojBodova;
		this.tipKupca = tipKupca;
	}

	public List<String> getSveKarte() {
		return sveKarte;
	}

	public void setSveKarte(List<String> sveKarte) {
		this.sveKarte = sveKarte;
	}

	public int getBrojBodova() {
		return brojBodova;
	}

	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}

	public TipKupca getTipKupca() {
		return tipKupca;
	}

	public void setTipKupca(TipKupca tipKupca) {
		this.tipKupca = tipKupca;
	}

	@Override
	public String toString() {
		return "Kupac [sveKarte=" + sveKarte + ", brojBodova=" + brojBodova + ", tipKupca=" + tipKupca + "]";
	}
	
}
