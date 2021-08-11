package com.example.carrental.entity.user;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "passport")
public class UserPassport implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "first_name", nullable = false)
  private String firstName;
  @Column(name = "middle_name", nullable = false)
  private String middleName;
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @Column(name = "dob", nullable = false)
  private LocalDate dateOfBirth;
  @Column(name = "passport_series", nullable = false)
  private String passportSeries;
  @Column(name = "passport_number", nullable = false)
  private String passportNumber;
  @Column(name = "date_of_issue", nullable = false)
  private LocalDate dateOfIssue;
  @Column(name = "validity_period", nullable = false)
  private LocalDate validityPeriod;
  @Column(name = "organization_that_issued", nullable = false)
  private String organizationThatIssued;
  @Column(nullable = false)
  private UserDocumentStatus status;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "changed_at")
  private LocalDateTime changedAt;
  @Column(name = "confirmed_at")
  private LocalDateTime confirmedAt;
  @Column(name = "documents_file_link")
  private String documentsFileLink;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
