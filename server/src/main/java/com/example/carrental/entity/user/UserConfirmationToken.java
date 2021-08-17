package com.example.carrental.entity.user;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents Confirmation Token.
 *
 * @author Yury Raichonak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_confirmation_token")
public class UserConfirmationToken implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String token;
  @Column(name = "creation_time", nullable = false)
  private LocalDateTime creationTime;
  @Column(name = "expiration_time", nullable = false)
  private LocalDateTime expirationTime;
  @Column(name = "confirmation_time", nullable = false)
  private LocalDateTime confirmationTime;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public UserConfirmationToken(String token, LocalDateTime creationTime,
      LocalDateTime expirationTime, User user) {
    this.token = token;
    this.creationTime = creationTime;
    this.expirationTime = expirationTime;
    this.user = user;
  }
}
