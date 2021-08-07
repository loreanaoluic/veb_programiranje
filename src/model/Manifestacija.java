package model;

import java.time.LocalDateTime;

public class Manifestacija {
	private int id;
	private String naziv;
	private int tipManifestacije;
	private int brojMesta;
	private LocalDateTime datumIVremeOdrzavanja;
	private double cenaRegular;
	private boolean status;
	private int lokacija;
	private String poster;
	private boolean obrisana;

	public Manifestacija() {

	}

	public Manifestacija(int id, String naziv, int tipManifestacije, int brojMesta, LocalDateTime datumIVremeOdrzavanja, double cenaRegular, boolean status, int lokacija, String poster, boolean obrisana) {
		this.id = id;
		this.naziv = naziv;
		this.tipManifestacije = tipManifestacije;
		this.brojMesta = brojMesta;
		this.datumIVremeOdrzavanja = datumIVremeOdrzavanja;
		this.cenaRegular = cenaRegular;
		this.status = status;
		this.lokacija = lokacija;
		this.poster = poster;
		this.obrisana = obrisana;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getTipManifestacije() {
		return tipManifestacije;
	}

	public void setTipManifestacije(int tipManifestacije) {
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

	public double getCenaRegular() {
		return cenaRegular;
	}

	public void setCenaRegular(double cenaRegular) {
		this.cenaRegular = cenaRegular;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getLokacija() {
		return lokacija;
	}

	public void setLokacija(int lokacija) {
		this.lokacija = lokacija;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public boolean isObrisana() {
		return obrisana;
	}

	public void setObrisana(boolean obrisana) {
		this.obrisana = obrisana;
	}
}
