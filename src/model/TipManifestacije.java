package model;

public class TipManifestacije {
    private int id;
    private String nazivTipa;

    public TipManifestacije() {
    }

    public TipManifestacije(int id, String nazivTipa) {
        this.id = id;
        this.nazivTipa = nazivTipa;
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
}
