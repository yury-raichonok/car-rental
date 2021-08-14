package com.example.carrental.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

  private final ApplicationConfig applicationConfig;

  private Claims getAllClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(applicationConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  public String getEmailFromToken(String token) {
    String email;
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      email = claims.getSubject();
    } catch (Exception e) {
      email = null;
    }
    return email;
  }

  public String generateToken(String username) {

    return Jwts.builder().setIssuer(applicationConfig.getName()).setSubject(username).setIssuedAt(new Date())
        .setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, applicationConfig.getJwtSecret())
        .compact();
  }

  private Date generateExpirationDate() {
    return new Date(new Date().getTime() + applicationConfig.getJwtExpirationMs() * 1000);
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String email = getEmailFromToken(token);
    return (
        email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }

  public boolean isTokenExpired(String token) {
    Date expireDate=getExpirationDate(token);
    return expireDate.before(new Date());
  }

  private Date getExpirationDate(String token) {
    Date expireDate;
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      expireDate = claims.getExpiration();
    } catch (Exception e) {
      expireDate = null;
    }
    return expireDate;
  }

  public Date getIssuedAtDateFromToken(String token) {
    Date issueAt;
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      issueAt = claims.getIssuedAt();
    } catch (Exception e) {
      issueAt = null;
    }
    return issueAt;
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
