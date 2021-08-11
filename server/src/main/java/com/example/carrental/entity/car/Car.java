package com.example.carrental.entity.car;

import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.order.Order;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Car implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String vin;
  @Column(name = "date_of_issue", nullable = false)
  private LocalDate dateOfIssue;
  @Column(nullable = false)
  private String color;
  @Column(name = "body_type", nullable = false)
  private CarBodyType bodyType;
  @Column(name = "is_automatic_transmission", nullable = false)
  private boolean isAutomaticTransmission;
  @Column(name = "engine_type", nullable = false)
  private CarEngineType engineType;
  @Column(name = "passengers_amt", nullable = false)
  private int passengersAmt;
  @Column(name = "baggage_amt", nullable = false)
  private int baggageAmt;
  @Column(name = "has_conditioner", nullable = false)
  private boolean hasConditioner;
  @Column(name = "cost_per_hour", nullable = false)
  private BigDecimal costPerHour;
  @Column(name = "in_rental", nullable = false)
  private boolean inRental;
  @Column(name = "car_image_link")
  private String carImageLink;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "changed_at")
  private LocalDateTime changedAt;

  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  @JoinColumn(name = "model_id", nullable = false)
  private CarModel model;

  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  @JoinColumn(name = "car_class_id", nullable = false)
  private CarClass carClass;

  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  @JoinColumn(name = "rental_location_id", nullable = false)
  private Location location;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "car"
  )
  private List<Order> orders;

  @Override
  public String toString() {
    return "Car{" +
        "id=" + id +
        ", vin='" + vin + '\'' +
        ", dateOfIssue=" + dateOfIssue +
        ", color='" + color + '\'' +
        ", bodyType=" + bodyType +
        ", isAutomaticTransmission=" + isAutomaticTransmission +
        ", engineType=" + engineType +
        ", passengersAmt=" + passengersAmt +
        ", baggageAmt=" + baggageAmt +
        ", hasConditioner=" + hasConditioner +
        ", costPerHour=" + costPerHour +
        ", carImageLink='" + carImageLink + '\'' +
        '}';
  }
}
