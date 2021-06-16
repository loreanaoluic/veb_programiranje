package beans;

public class Komentar {
	private int id;
	private Kupac kupac;
	private Manifestacija manifestacija;
	private String tekstKomentara;
	private double ocena;
	
	public Komentar() {
		super();
	}

	public Komentar(int id, Kupac kupac, Manifestacija manifestacija, String tekstKomentara, double ocena) {
		super();
		this.id = id;
		this.kupac = kupac;
		this.manifestacija = manifestacija;
		this.tekstKomentara = tekstKomentara;
		this.ocena = ocena;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public Manifestacija getManifestacija() {
		return manifestacija;
	}

	public void setManifestacija(Manifestacija manifestacija) {
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

	@Override
	public String toString() {
		return "Komentar [id=" + id + ", kupac=" + kupac + ", manifestacija=" + manifestacija + ", tekstKomentara="
				+ tekstKomentara + ", ocena=" + ocena + "]";
	}
	
}