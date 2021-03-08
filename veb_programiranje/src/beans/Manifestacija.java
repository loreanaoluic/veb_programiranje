package beans;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import beans.enums.StatusManifestacije;
import beans.enums.TipManifestacije;

public class Manifestacija {
	private String naziv;
	private TipManifestacije tipManifestacije;
	private int brojMesta;
	private LocalDateTime datumIVremeOdrzavanja;
	private double cenaRegularKarte;
	private StatusManifestacije status;
	private Lokacija lokacija;
	private BufferedImage posterManifestacije;
	
	public Manifestacija() {
		super();
	}

	public Manifestacija(String naziv, TipManifestacije tipManifestacije, int brojMesta,
			LocalDateTime datumIVremeOdrzavanja, double cenaRegularKarte, StatusManifestacije status,
			Lokacija lokacija, BufferedImage posterManifestacije) {
		super();
		this.naziv = naziv;
		this.tipManifestacije = tipManifestacije;
		this.brojMesta = brojMesta;
		this.datumIVremeOdrzavanja = datumIVremeOdrzavanja;
		this.cenaRegularKarte = cenaRegularKarte;
		this.status = status;
		this.lokacija = lokacija;
		this.posterManifestacije = posterManifestacije;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public TipManifestacije getTipManifestacije() {
		return tipManifestacije;
	}

	public void setTipManifestacije(TipManifestacije tipManifestacije) {
		this.tipManifestacije = tipManifestacije;
	}

	public int getBrojMesta() {
		return brojMesta;
	}

	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}

	public LocalDateTime getDatumIVremeOdrzavanja() {
		return datumIVremeOdrzavanja;
	}

	public void setDatumIVremeOdrzavanja(LocalDateTime datumIVremeOdrzavanja) {
		this.datumIVremeOdrzavanja = datumIVremeOdrzavanja;
	}

	public double getCenaRegularKarte() {
		return cenaRegularKarte;
	}

	public void setCenaRegularKarte(double cenaRegularKarte) {
		this.cenaRegularKarte = cenaRegularKarte;
	}

	public StatusManifestacije getStatus() {
		return status;
	}

	public void setStatus(StatusManifestacije status) {
		this.status = status;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	public BufferedImage getPosterManifestacije() {
		return posterManifestacije;
	}

	public void setPosterManifestacije(BufferedImage posterManifestacije) {
		this.posterManifestacije = posterManifestacije;
	}

	@Override
	public String toString() {
		return "Manifestacija [naziv=" + naziv + ", tipManifestacije=" + tipManifestacije + ", brojMesta=" + brojMesta
				+ ", datumIVremeOdrzavanja=" + datumIVremeOdrzavanja + ", cenaRegularKarte=" + cenaRegularKarte
				+ ", status=" + status + ", lokacija=" + lokacija + ", posterManifestacije=" + posterManifestacije
				+ "]";
	}
	
}
