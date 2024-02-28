package com.example.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.websocket.Decoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "AAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBCCCCCCCCCCC";


    public String extracUserEmail(String jwt) {
        return extracClaim(jwt,Claims::getSubject);
    }

    public <T> T extracClaim(String jwt, Function<Claims,T> ClaimResolver){
        final Claims claims = extracAllClaims(jwt);
        return ClaimResolver.apply(claims);

    }

    public boolean TokenIsValid(String jwt, UserDetails userDetails){
        final String username= extracUserEmail(jwt);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extracExpiration(jwt).before(new Date());
    }

    private Date extracExpiration(String jwt) {
        return extracClaim(jwt,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String,Object> extraClaim, UserDetails userDetails){
        return Jwts.
                builder()
                .setClaims(extraClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+450000000))
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extracAllClaims (String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInkey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);

    }
}
