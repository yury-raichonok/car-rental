package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

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

/**
 * The controller for FAQs REST endpoints.
 * <p>
 * This class handles the CRUD operations for FAQs, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/faqs")
@Validated
public class FaqController {

  private final FaqService faqService;

  /**
   * Handle the /faqs endpoint.
   * @param language selected language.
   * @return list of all FAQs.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified FAQs.
   * @throws NoContentException if list of FAQs is empty.
   */
  @GetMapping
  public ResponseEntity<List<FaqResponse>> findAll(
      @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) throws NoContentException {
    var faqList = faqService.findAll(language);
    return new ResponseEntity<>(faqList, HttpStatus.OK);
  }

  /**
   * Handle the /faqs/paged endpoint.
   * @param pageable page of FAQs.
   * @return page of all FAQs with translations.
   */
  @GetMapping(path = "/paged")
  public ResponseEntity<Page<FaqWithTranslationsResponse>> findAllPaged(Pageable pageable) {
    var faqs = faqService.findAllPaged(pageable);
    return new ResponseEntity<>(faqs, HttpStatus.OK);
  }

  /**
   * Handle the /faqs endpoint.
   * @param createFaqRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully created new FAQ.
   * 406: unable to create FAQ, because FAQ with same question already exists.
   * @throws EntityAlreadyExistsException if FAQ with same question already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(@Valid @RequestBody CreateFaqRequest createFaqRequest)
      throws EntityAlreadyExistsException {
    faqService.create(createFaqRequest);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Handle the /faqs/{id} endpoint.
   * @param id of the FAQ which updated.
   * @param updateFaqRequest request with parameters.
   * @return code 200 is successfully created new FAQ.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateFaqRequest updateFaqRequest) {
    faqService.update(id, updateFaqRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /faqs/{id} endpoint.
   * @param id of the FAQ which deleted.
   * @return code 200 is successfully deleted.
   */
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> delete(@NotNull @Positive @PathVariable Long id) {
    faqService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
