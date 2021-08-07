package model;

import model.enums.Pol;
import model.enums.Uloga;

import java.time.LocalDate;
import java.util.List;

public class Prodavac extends Korisnik{
	
	private List<Integer> manifestacije;
	private List<String> karte;
	
	public Prodavac() {
		super();
	}

	public Prodavac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, LocalDate datumRodjenja, Uloga uloga, Boolean blokiran, Boolean obrisan, List<Integer> manifestacije, List<String> karte) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, uloga, blokiran, obrisan);
		this.manifestacije = manifestacije;
		this.karte = karte;
	}

	public List<Integer> getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(List<Integer> manifestacije) {
		this.manifestacije = manifestacije;
	}

	public List<String> getKarte() {
		return karte;
	}

	public void setKarte(List<String> karte) {
		this.karte = karte;
	}
}
