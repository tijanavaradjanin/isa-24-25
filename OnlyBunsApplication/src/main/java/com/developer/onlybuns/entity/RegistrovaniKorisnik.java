package com.developer.onlybuns.entity;


import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="registrovaniKorisnik")
public class RegistrovaniKorisnik extends Korisnik implements UserDetails {
    @Column(name="info", nullable = false)
    private String info;

    public Map<String, Object> getAdditionalClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("info", getInfo());
        return claims;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
