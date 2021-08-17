package com.example.carrental.entity.notification;

import com.example.carrental.entity.user.User;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

/**
 * Represents Notifications.
 *
 * @author Yury Raichonak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Notification implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String message;
  @Enumerated(EnumType.STRING)
  @Column(name = "notification_type", nullable = false)
  private NotificationType notificationType;
  @Column(name = "sent_date", nullable = false)
  private LocalDateTime sentDate;
  @Column(nullable = false)
  private NotificationStatus status;
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
