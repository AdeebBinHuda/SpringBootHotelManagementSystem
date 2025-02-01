package com.hotel.SpringBootHotelbooking.jwt;

import com.hotel.SpringBootHotelbooking.entity.User;
import com.hotel.SpringBootHotelbooking.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import java.util.function.Function;



@Service
public class JwtService {

    @Autowired
    private TokenRepository tokenRepository;

    private final String SECURITY_KEY = " ";


    private Claims extrectAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private SecretKey getSigninKey(){
        byte[]  keyBytes = Decoders.BASE64URL.decode(SECURITY_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        return Jwts
                .builder()
                .setSubject(user.getEmail())  //set Email as subject
                .claim("role",user.getRole())   // Add user role to payload
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set token issue time
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000))  //Set token expire time
                .signWith(getSigninKey())     // sign the token with secret key
                .compact();  //Build and compect the token into string

    }

    public String extrectUserName(String token){
        return extrectClaim(token,Claims::getSubject);
    }

    private boolean isTokenExpired(String token){
        return extrectExpiration(token).before(new Date());
    }

    private Date extrectExpiration(String token) {

        return extrectClaim(token , Claims::getExpiration);
    }

    public boolean isValid(String token, UserDetails user){
        String userName = extrectUserName(token);

        boolean validtoken = tokenRepository
                .findByToken(token)
                .map(t -> !t.isLogOut())
                .orElse(false);
        return (userName.equals(user.getUsername())&& !isTokenExpired(token) && validtoken);
    }

    //extrect specific clame from the token claim
    public <T> T extrectClaim(String token, Function<Claims, T>resolver){
        Claims claims = extrectAllClaims(token);
        return resolver.apply(claims);
    }

    // get user role from token
    public String extrectUserRole(String token){
        return  extrectClaim(token , claims -> claims.get("role",String.class));
    }

}
