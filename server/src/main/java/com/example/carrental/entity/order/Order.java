package com.example.carrental.entity.order;

import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.bill.RepairBill;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.user.User;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ord")
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "pick_up_date")
  private LocalDateTime pickUpDate;
  @Column(name = "return_date")
  private LocalDateTime returnDate;
  @Column(name = "total_cost")
  private BigDecimal totalCost;
  @Column(name = "payment_status")
  private OrderPaymentStatus paymentStatus;
  @Column
  private String comments;
  @Column(name = "sent_date")
  private LocalDateTime sentDate;
  @Column(name = "payment_date")
  private LocalDateTime paymentDate;
  @Column(name = "denying_date")
  private LocalDateTime denyingDate;
  @Column(name = "rental_status")
  private OrderRentalStatus rentalStatus;
  @OneToOne(
      mappedBy = "order",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  private PaymentBill paymentBill;

  @OneToOne(
      mappedBy = "order",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  private RepairBill repairBill;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "car_id")
  private Car car;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", pickUpDate=" + pickUpDate +
        ", returnDate=" + returnDate +
        ", totalCost=" + totalCost +
        ", paymentStatus=" + paymentStatus +
        ", comments='" + comments + '\'' +
        ", sentDate=" + sentDate +
        ", paymentDate=" + paymentDate +
        ", denyingDate=" + denyingDate +
        '}';
  }
}
