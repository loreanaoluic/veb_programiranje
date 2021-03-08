package beans;

import java.time.LocalDate;
import java.util.List;

import beans.enums.Pol;
import beans.enums.Uloga;

public class Prodavac extends Korisnik {
	private List<String> manifestacije;
	
	public Prodavac() {
		super();
	}

	public Prodavac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, LocalDate datumRodjenja,
			Uloga uloga, List<String> manifestacije) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, uloga);
		this.manifestacije = manifestacije;
	}

	public List<String> getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(List<String> manifestacije) {
		this.manifestacije = manifestacije;
	}

	@Override
	public String toString() {
		return "Prodavac [manifestacije=" + manifestacije + "]";
	}
	
}
