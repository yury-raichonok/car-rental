package com.example.carrental.entity.location;

import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.rentalDetails.RentalDetails;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Location implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(name = "coordinate_x", nullable = false)
  private double coordinateX;
  @Column(name = "coordinate_y", nullable = false)
  private double coordinateY;
  @Column(nullable = false)
  private int zoom;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "changed_at")
  private LocalDateTime changedAt;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "location"
  )
  private Set<Car> cars;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "location"
  )
  private Set<Order> order;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "location"
  )
  private RentalDetails rentalDetails;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "location"
  )
  private List<LocationTranslation> locationTranslations;

  @Override
  public String toString() {
    return "Location{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", coordinateX=" + coordinateX +
        ", coordinateY=" + coordinateY +
        ", zoom=" + zoom +
        '}';
  }
}
