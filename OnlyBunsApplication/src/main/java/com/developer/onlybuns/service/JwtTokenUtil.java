/*package com.developer.onlybuns.service;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
        claims.put("korisnickoIme", user.getKorisnickoIme());
        claims.put("uloga", user.getUloga());
        //claims.putAll(user.getAdditionalClaims());
        return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + this.expirationTime)).signWith(SignatureAlgorithm.HS256, this.secret.getBytes()).compact();
    }

    public boolean validateToken(String token, Korisnik user) {
        String username = this.getUsernameFromToken(token);
        return username.equals(user.getEmail()) && !this.isTokenExpired(token);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        // JWT se prosledjuje kroz header 'Authorization' u formatu:
        // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // preuzimamo samo token (vrednost tokena je nakon "Bearer " prefiksa)
        }

        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    public String getUsernameFromToken(String token) {
        String username;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

   // public String getUsernameFromToken(String token) {
   //     return this.getAllClaimsFromToken(token).getSubject();
  //  }

    private Claims getAllClaimsFromToken(String token) {
        return (Claims)Jwts.parser().setSigningKey(this.secret.getBytes()).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }
}*/
