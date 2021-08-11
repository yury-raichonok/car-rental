package com.example.carrental.entity.user;

import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.rentalDetails.RentalRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class User implements Serializable, UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(
      nullable = false,
      unique = true
  )
  private String email;
  @Column(nullable = false)
  private String password;
  @ManyToOne(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "role_id")
  private UserRole role;
  @Column(name = "is_email_confirmed", nullable = false)
  private Boolean isEmailConfirmed = false;
  @Column(nullable = false)
  private Boolean locked = false;
  @Column(nullable = false)
  private Boolean enabled = true;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "changed_at")
  private LocalDateTime changedAt;
  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private UserPassport passport;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private UserDrivingLicense drivingLicense;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private List<UserPhone> phones;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private Set<Order> orders;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private Set<Notification> notifications;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private Set<UserConfirmationToken> tokens;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "user"
  )
  private Set<RentalRequest> rentalRequests;

  public User(String email, String password, UserRole role, LocalDateTime createdAt) {
    this.email = email;
    this.password = password;
    this.role = role;
    this.createdAt = createdAt;
  }

  public User(Long id, String email, String password,
      UserRole role, Boolean locked, Boolean enabled, LocalDateTime createdAt) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
    this.locked = locked;
    this.enabled = enabled;
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", email='" + email + '\'' +
        '}';
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    GrantedAuthority authority = new SimpleGrantedAuthority(role.getAuthority());
    return Collections.singletonList(authority);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
