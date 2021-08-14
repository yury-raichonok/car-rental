package com.example.carrental.entity.rentaldetails;

import com.example.carrental.entity.user.User;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rental_request")
public class RentalRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "request_type", nullable = false)
  private RentalRequestType rentalRequestType;
  @Column(name = "sent_date", nullable = false)
  private LocalDateTime sentDate;
  @Column(name = "consideration_date")
  private LocalDateTime considerationDate;
  @Column(nullable = false)
  private boolean considered;
  @Column
  private String comments;
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
