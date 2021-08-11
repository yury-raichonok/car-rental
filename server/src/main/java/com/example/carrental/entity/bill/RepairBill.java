package com.example.carrental.entity.bill;

import com.example.carrental.entity.order.Order;
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
@Table(name = "repair_bill")
public class RepairBill implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String message;
  @Column(name = "total_cost", nullable = false)
  private BigDecimal totalCost;
  @Column(name = "sent_date", nullable = false)
  private LocalDateTime sentDate;
  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @OneToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Override
  public String toString() {
    return "Bill{" +
        "id=" + id +
        ", message='" + message + '\'' +
        ", totalCost=" + totalCost +
        ", sentDate=" + sentDate +
        ", paymentDate=" + paymentDate +
        '}';
  }
}
