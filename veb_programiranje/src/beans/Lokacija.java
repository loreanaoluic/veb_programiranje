package beans;

public class Lokacija {
	private double geografskaDuzina;
	private double geografskaSirina;
	private String adresa;
	
	public Lokacija() {
		super();
	}

	public Lokacija(double geografskaDuzina, double geografskaSirina, String adresa) {
		super();
		this.geografskaDuzina = geografskaDuzina;
		this.geografskaSirina = geografskaSirina;
		this.adresa = adresa;
	}

	public double getGeografskaDuzina() {
		return geografskaDuzina;
	}

	public void setGeografskaDuzina(double geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}

	public double getGeografskaSirina() {
		return geografskaSirina;
	}

	public void setGeografskaSirina(double geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	@Override
	public String toString() {
		return "Lokacija [geografskaDuzina=" + geografskaDuzina + ", geografskaSirina=" + geografskaSirina + ", adresa="
				+ adresa + "]";
	}
	
}
