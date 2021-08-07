package model;

public class Lokacija {
	private int id;
	private double geografskaSirina;
	private double geografskaDuzina;
	private String adresa;
	
	public Lokacija() {

	}

	public Lokacija(int id, double geografskaSirina, double geografskaDuzina, String adresa) {
		this.id = id;
		this.geografskaSirina = geografskaSirina;
		this.geografskaDuzina = geografskaDuzina;
		this.adresa = adresa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getGeografskaSirina() {
		return geografskaSirina;
	}

	public void setGeografskaSirina(double geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}

	public double getGeografskaDuzina() {
		return geografskaDuzina;
	}

	public void setGeografskaDuzina(double geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
}
