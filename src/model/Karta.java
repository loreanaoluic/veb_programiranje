package model;

import model.enums.StatusKarte;
import model.enums.TipKarte;

import java.time.LocalDateTime;

public class Karta {
	private String id;
	private int manifestacija;
	private LocalDateTime datumIVremeManifestacije;
	private double cena;
	private StatusKarte statusKarte;
	private TipKarte tipKarte;
	private boolean obrisana;
	private String kupac;
	private String prodavac;
	private LocalDateTime vremeOtkazivanja;

	public Karta() {

	}

	public Karta(String id, int manifestacija, LocalDateTime datumIVremeManifestacije, double cena, StatusKarte statusKarte,
				 TipKarte tipKarte, boolean obrisana, String kupac, String prodavac, LocalDateTime vremeOtkazivanja) {
		this.id = id;
		this.manifestacija = manifestacija;
		this.datumIVremeManifestacije = datumIVremeManifestacije;
		this.cena = cena;
		this.statusKarte = statusKarte;
		this.tipKarte = tipKarte;
		this.obrisana = obrisana;
		this.kupac = kupac;
		this.prodavac = prodavac;
		this.vremeOtkazivanja = vremeOtkazivanja;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getManifestacija() {
		return manifestacija;
	}

	public void setManifestacija(int manifestacija) {
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

	public void setCena(double cena) {
		this.cena = cena;
	}

	public StatusKarte getStatusKarte() {
		return statusKarte;
	}

	public void setStatusKarte(StatusKarte statusKarte) {
		this.statusKarte = statusKarte;
	}

	public TipKarte getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(TipKarte tipKarte) {
		this.tipKarte = tipKarte;
	}

	public boolean isObrisana() {
		return obrisana;
	}

	public void setObrisana(boolean obrisana) {
		this.obrisana = obrisana;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public String getProdavac() {
		return prodavac;
	}

	public void setProdavac(String prodavac) {
		this.prodavac = prodavac;
	}

	public LocalDateTime getVremeOtkazivanja() {
		return vremeOtkazivanja;
	}

	public void setVremeOtkazivanja(LocalDateTime vremeOtkazivanja) {
		this.vremeOtkazivanja = vremeOtkazivanja;
	}
}
