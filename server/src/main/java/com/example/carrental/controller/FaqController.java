package com.example.carrental.controller;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.service.FaqService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/faqs")
@Validated
public class FaqController {

  private final FaqService faqService;

  @GetMapping
  public ResponseEntity<List<FaqResponse>> findAll(
      @CookieValue(name = "i18next") String language) {
    var faqList = faqService.findAll(language);
    if (faqList.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(faqList, HttpStatus.OK);
  }

  @GetMapping(path = "/paged")
  public ResponseEntity<Page<FaqWithTranslationsResponse>> findAllPaged(Pageable pageable) {
    var faqs = faqService.findAllPaged(pageable);
    return new ResponseEntity<>(faqs, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<String> create(@Valid @RequestBody CreateFaqRequest createFaqRequest) {
    try {
      var response = faqService.create(createFaqRequest);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateFaqRequest updateFaqRequest) {
    try {
      var response = faqService.update(id, updateFaqRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<String> delete(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = faqService.delete(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
