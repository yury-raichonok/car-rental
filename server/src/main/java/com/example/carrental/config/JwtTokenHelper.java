package com.example.carrental.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper {

  @Value("${app.name}")
  private String appName;

  @Value("${app.jwtSecret}")
  private String secretKey;

  @Value("${app.jwtExpirationMs}")
  private int expiresIn;

  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

  private Claims getAllClaimsFromToken(String token) {
    Claims claims;
    try {
       claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  public String getEmailFromToken(String token) {
    String email;
    var claims = getAllClaimsFromToken(token);
    try {
      email = claims.getSubject();
    } catch (Exception e) {
      email = null;
    }
    return email;
  }

  public String generateToken(String username) {

    return Jwts.builder().setIssuer(appName).setSubject(username).setIssuedAt(new Date())
        .setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, secretKey)
        .compact();
  }

  private Date generateExpirationDate() {
    return new Date(new Date().getTime() + expiresIn * 1000);
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String email = getEmailFromToken(token);
    return (
        email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }

  public boolean isTokenExpired(String token) {
    Date expireDate = getExpirationDate(token);
    return expireDate.before(new Date());
  }

  private Date getExpirationDate(String token) {
    Date expireDate;
    try {
      final Claims claims = getAllClaimsFromToken(token);
      expireDate = claims.getExpiration();
    } catch (Exception e) {
      expireDate = null;
    }
    return expireDate;
  }

  public String getToken( HttpServletRequest request ) {
    String authHeader = getAuthHeaderFromHeader( request );
    if ( authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  public String getAuthHeaderFromHeader( HttpServletRequest request ) {
    return request.getHeader("Authorization");
  }
}
