package com.example.carrental.entity.car;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents Car Model.
 *
 * @author Yury Raichonak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "model")
public class CarModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "changed_at")
  private LocalDateTime changedAt;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "brand_id", nullable = false)
  private CarBrand brand;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "model"
  )
  private List<Car> cars;

  @Override
  public String toString() {
    return "CarModel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
