package com.developer.onlybuns.dto;

public class ObjavaRequestDTO {
    private String opis;
    private String grad;
    private String drzava;
    private String slika; // Sada čuvamo samo putanju do slike

    // Getteri i setteri
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public String getGrad() { return grad; }
    public void setGrad(String grad) { this.grad = grad; }

    public String getDrzava() { return drzava; }
    public void setDrzava(String drzava) { this.drzava = drzava; }

    public String getSlika() { return slika; }
    public void setSlika(String slika) { this.slika = slika; }

}
