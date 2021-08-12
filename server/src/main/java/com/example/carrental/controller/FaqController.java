package com.example.carrental.controller;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.service.FaqService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
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
      @CookieValue(name = "i18next") String language) throws NoContentException {
    var faqList = faqService.findAll(language);
    return new ResponseEntity<>(faqList, HttpStatus.OK);
  }

  @GetMapping(path = "/paged")
  public ResponseEntity<Page<FaqWithTranslationsResponse>> findAllPaged(Pageable pageable) {
    var faqs = faqService.findAllPaged(pageable);
    return new ResponseEntity<>(faqs, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> create(@Valid @RequestBody CreateFaqRequest createFaqRequest)
      throws EntityAlreadyExistsException {
    faqService.create(createFaqRequest);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateFaqRequest updateFaqRequest) {
    faqService.update(id, updateFaqRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> delete(@NotNull @Positive @PathVariable Long id) {
    faqService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
