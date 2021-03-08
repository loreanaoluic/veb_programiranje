package beans;

import java.time.LocalDateTime;

import beans.enums.StatusKarte;
import beans.enums.TipKarte;

public class Karta {
	private String id;
	private Manifestacija manifestacija;
	private LocalDateTime datumIVremeManifestacije;
	private double cena;
	private String kupac;
	private StatusKarte status;
	private TipKarte tip;
	
	public Karta() {
		super();
	}
	
	public Karta(String id, Manifestacija manifestacija, LocalDateTime datumIVremeManifestacije, double cena, String kupac,
			StatusKarte status, TipKarte tip) {
		super();
		this.id = id;
		this.manifestacija = manifestacija;
		this.datumIVremeManifestacije = datumIVremeManifestacije;
		this.cena = cena;
		this.kupac = kupac;
		this.status = status;
		this.tip = tip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Manifestacija getManifestacija() {
		return manifestacija;
	}

	public void setManifestacija(Manifestacija manifestacija) {
		this.manifestacija = manifestacija;
	}

	public LocalDateTime getDatumIVremeManifestacije() {
		return datumIVremeManifestacije;
	}

	public void setDatumIVremeManifestacije(LocalDateTime datumIVremeManifestacije) {
		this.datumIVremeManifestacije = datumIVremeManifestacije;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(int cena) {
		this.cena = cena;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public StatusKarte getStatus() {
		return status;
	}

	public void setStatus(StatusKarte status) {
		this.status = status;
	}

	public TipKarte getTip() {
		return tip;
	}

	public void setTip(TipKarte tip) {
		this.tip = tip;
	}

	@Override
	public String toString() {
		return "Karta [id=" + id + ", manifestacija=" + manifestacija + ", datumIVremeManifestacije="
				+ datumIVremeManifestacije + ", cena=" + cena + ", kupac=" + kupac + ", status=" + status + ", tip="
				+ tip + "]";
	}
	
}
