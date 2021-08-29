package model;

import model.enums.TipKarte;

public class ShoppingCartItem {
    private int id;
    private int manifestacija;
    private double cenaRegular;
    private int kolicina;
    private TipKarte tipKarte;
    private double ukupnaCena;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(int id, int manifestacija, double cenaRegular, int kolicina, TipKarte tipKarte) {
        this.id = id;
        this.manifestacija = manifestacija;
        this.cenaRegular = cenaRegular;
        this.kolicina = kolicina;
        this.tipKarte = tipKarte;

        if (tipKarte.equals(TipKarte.REGULAR)) {
            this.ukupnaCena = cenaRegular * kolicina;
        } else if (tipKarte.equals(TipKarte.FAN_PIT)) {
            this.ukupnaCena = (cenaRegular * 2) * kolicina;
        } else if (tipKarte.equals(TipKarte.VIP)) {
            this.ukupnaCena = (cenaRegular * 4) * kolicina;
        }

    }

    public int getManifestacija() {
        return manifestacija;
    }

    public void setManifestacija(int manifestacija) {
        this.manifestacija = manifestacija;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public TipKarte getTipKarte() {
        return tipKarte;
    }

    public void setTipKarte(TipKarte tipKarte) {
        this.tipKarte = tipKarte;
    }

    public double getCenaRegular() {
        return cenaRegular;
    }

    public void setCenaRegular(double cenaRegular) {
        this.cenaRegular = cenaRegular;
    }

    public double getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(double ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

