package model;

public class Komentar {
	private int id;
	private String kupac;
	private int manifestacija;
	private String tekstKomentara;
	private double ocena;
	private boolean obrisan;
	
	public Komentar() {}

	public Komentar(int id, String kupac, int manifestacija, String tekstKomentara, double ocena, boolean obrisan) {
		this.id = id;
		this.kupac = kupac;
		this.manifestacija = manifestacija;
		this.tekstKomentara = tekstKomentara;
		this.ocena = ocena;
		this.obrisan = obrisan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public int getManifestacija() {
		return manifestacija;
	}

	public void setManifestacija(int manifestacija) {
		this.manifestacija = manifestacija;
	}

	public String getTekstKomentara() {
		return tekstKomentara;
	}

	public void setTekstKomentara(String tekstKomentara) {
		this.tekstKomentara = tekstKomentara;
	}

	public double getOcena() {
		return ocena;
	}

	public void setOcena(double ocena) {
		this.ocena = ocena;
	}

	public boolean isObrisan() {
		return obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
}
