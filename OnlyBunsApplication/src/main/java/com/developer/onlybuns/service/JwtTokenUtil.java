package com.developer.onlybuns.service;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
    private String secret = "tajna_lozinka";
    private long expirationTime = 3600000L;

    public JwtTokenUtil() {
    }

    public String generateToken(Korisnik user) {
        Map<String, Object> claims = new HashMap();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("ime", user.getIme());
        claims.put("prezime", user.getPrezime());
        claims.put("uloga", user.getUloga());
        //claims.putAll(user.getAdditionalClaims());
        return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + this.expirationTime)).signWith(SignatureAlgorithm.HS256, this.secret.getBytes()).compact();
    }

    public boolean validateToken(String token, Korisnik user) {
        String username = this.getUsernameFromToken(token);
        return username.equals(user.getEmail()) && !this.isTokenExpired(token);
    }

    public String getUsernameFromToken(String token) {
        return this.getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        return (Claims)Jwts.parser().setSigningKey(this.secret.getBytes()).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }
}
