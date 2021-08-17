package com.example.carrental.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * JwtToken helper class.
 * <p>
 * This class provides logic for parsing and generating JWT Tokens.
 * </p>
 * @author Yury Raichonak
 */
@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

  private final ApplicationPropertiesConfig applicationPropertiesConfig;

  /**
   * @param token JWT token from request.
   * @return claims set.
   */
  private Claims getAllClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(applicationPropertiesConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  /**
   * @param token JWT token from request.
   * @return parsed user email.
   */
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

  /**
   * @param username user username.
   * @return generated JWT Token, using secret key and signature algorithm.
   */
  public String generateToken(String username) {

    return Jwts.builder().setIssuer(applicationPropertiesConfig.getName()).setSubject(username).setIssuedAt(new Date())
        .setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, applicationPropertiesConfig
            .getJwtSecret())
        .compact();
  }

  /**
   * @return generated token expiration date.
   */
  private Date generateExpirationDate() {
    return new Date(new Date().getTime() + applicationPropertiesConfig.getJwtExpirationMs() * 1000);
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String email = getEmailFromToken(token);
    return (
        email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }

  /**
   * @param token JWT token from request.
   * @return boolean value is token expired.
   */
  public boolean isTokenExpired(String token) {
    Date expireDate=getExpirationDate(token);
    return expireDate.before(new Date());
  }

  /**
   * @param token JWT token from request.
   * @return token expiration date.
   */
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

  /**
   * @param request from client.
   * @return parsed JWT Token.
   */
  public String getToken( HttpServletRequest request ) {
    String authHeader = getAuthHeaderFromHeader( request );
    if ( authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  /**
   * @param request from client.
   * @return authorization header.
   */
  public String getAuthHeaderFromHeader( HttpServletRequest request ) {
    return request.getHeader("Authorization");
  }
}
