package com.example.carrental.entity.user;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents User Role.
 *
 * @author Yury Raichonak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class UserRole implements Serializable, GrantedAuthority {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String role;
  @Column(name = "role_description")
  private String roleDescription;
  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "role"
  )
  private Set<User> users;

  @Override
  public String toString() {
    return "UserRole{" +
        "id=" + id +
        ", role='" + role + '\'' +
        ", roleDescription='" + roleDescription + '\'' +
        '}';
  }

  @Override
  public String getAuthority() {
    return role;
  }
}
