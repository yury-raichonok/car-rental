package com.example.carrental.entity.rentalDetails;

import com.example.carrental.entity.location.Location;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "rental_details")
public class RentalDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phone;

  @OneToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @Column(name = "from_day_to_week_coefficient", nullable = false)
  private BigDecimal fromDayToWeekCoefficient;
  @Column(name = "from_week_coefficient", nullable = false)
  private BigDecimal fromWeekCoefficient;
  @Column(name = "payment_bill_validity_period_in_minutes", nullable = false)
  private int paymentBillValidityPeriodInMinutes;
}
