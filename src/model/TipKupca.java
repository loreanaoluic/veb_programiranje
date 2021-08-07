package model;

public class TipKupca {
	private int id;
	private String nazivTipa;
	private double popust;
	private int bodovi;
	
	public TipKupca() {

	}

	public TipKupca(int id, String nazivTipa, double popust, int bodovi) {
		this.id = id;
		this.nazivTipa = nazivTipa;
		this.popust = popust;
		this.bodovi = bodovi;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNazivTipa() {
		return nazivTipa;
	}

	public void setNazivTipa(String nazivTipa) {
		this.nazivTipa = nazivTipa;
	}

	public double getPopust() {
		return popust;
	}

	public void setPopust(double popust) {
		this.popust = popust;
	}

	public int getBodovi() {
		return bodovi;
	}

	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
	}
}
