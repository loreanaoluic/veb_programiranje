package model;

import model.enums.Pol;
import model.enums.Uloga;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Kupac extends Korisnik {
	
	private List<String> karte;
	private int brojBodova;
	private int tip;
	private boolean sumnjiv;
	
	public Kupac() {
		super();
	}

	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, LocalDate datumRodjenja,
				 Uloga uloga, Boolean blokiran, Boolean obrisan, List<String> karte, int brojBodova, int tip, boolean sumnjiv) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, uloga, blokiran, obrisan);
		this.karte = karte;
		this.brojBodova = brojBodova;
		this.tip = tip;
		this.sumnjiv = sumnjiv;
	}

	public List<String> getKarte() {
		return karte;
	}

	public void setKarte(List<String> karte) {
		this.karte = karte;
	}

	public int getBrojBodova() {
		return brojBodova;
	}

	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}

	public int getTip() {
		return tip;
	}

	public void setTip(int tip) {
		this.tip = tip;
	}

	public boolean isSumnjiv() {
		return sumnjiv;
	}

	public void setSumnjiv(boolean sumnjiv) {
		this.sumnjiv = sumnjiv;
	}
}
