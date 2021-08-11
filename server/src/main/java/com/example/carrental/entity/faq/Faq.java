package com.example.carrental.entity.faq;

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
public class Faq implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String question;
  @Column(nullable = false)
  private String answer;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "changed_at")
  private LocalDateTime changedAt;
  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "faq"
  )
  private List<FaqTranslation> faqTranslations;

  @Override
  public String toString() {
    return "Faq{" +
        "id=" + id +
        ", question='" + question + '\'' +
        ", answer='" + answer + '\'' +
        '}';
  }
}
