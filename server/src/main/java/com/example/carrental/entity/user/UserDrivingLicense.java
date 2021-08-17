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

/**
 * Represents User Driving License.
 *
 * @author Yury Raichonak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "driving_license")
public class UserDrivingLicense implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
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
