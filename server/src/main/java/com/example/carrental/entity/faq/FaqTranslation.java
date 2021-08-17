package com.example.carrental.entity.faq;

import java.io.Serializable;
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

/**
 * Represents translations for specified FAQs questions and answers.
 *
 * @author Yury Raichonak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "faq_translation")
public class FaqTranslation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String question;
  @Column(nullable = false)
  private String answer;
  @Column(nullable = false)
  private String language;
  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY
  )
  @JoinColumn(name = "faq_id")
  private Faq faq;
}
