package beans;

import beans.enums.ImeTipaKupca;

public class TipKupca {
	private ImeTipaKupca imeTipa;
	private int popust;
	private int brojBodova;
	
	public TipKupca() {
		super();
	}

	public TipKupca(ImeTipaKupca imeTipa, int popust, int brojBodova) {
		super();
		this.imeTipa = imeTipa;
		this.popust = popust;
		this.brojBodova = brojBodova;
	}

	public ImeTipaKupca getImeTipa() {
		return imeTipa;
	}

	public void setImeTipa(ImeTipaKupca imeTipa) {
		this.imeTipa = imeTipa;
	}

	public int getPopust() {
		return popust;
	}

	public void setPopust(int popust) {
		this.popust = popust;
	}

	public int getBrojBodova() {
		return brojBodova;
	}

	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}

	@Override
	public String toString() {
		return "TipKupca [imeTipa=" + imeTipa + ", popust=" + popust + ", brojBodova=" + brojBodova + "]";
	}
	
}
