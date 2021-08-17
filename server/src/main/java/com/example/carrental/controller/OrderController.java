package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

import com.example.carrental.controller.dto.order.CreateOrderRequest;
import com.example.carrental.controller.dto.order.OrderCompleteWithPenaltyRequest;
import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderRejectRequest;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostResponse;
import com.example.carrental.controller.dto.order.UserOrderResponse;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import com.example.carrental.service.exceptions.PhoneNotSpecifiedException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for rental Orders REST endpoints.
 * <p>
 * This class handles the CRUD operations for Orders, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

  private final OrderService orderService;

  /**
   * Handle the /orders/current endpoint.
   * @param pageable page of orders.
   * @param language selected language.
   * @return list of all current orders.
   */
  @GetMapping(path = "/current")
  public ResponseEntity<Page<OrderInformationResponse>> findAllCurrent(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var ordersPage = orderService.findAllCurrent(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  /**
   * Handle the /orders/future endpoint.
   * @param pageable page of orders.
   * @param language selected language.
   * @return list of future orders.
   */
  @GetMapping(path = "/future")
  public ResponseEntity<Page<OrderInformationResponse>> findAllFuture(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var ordersPage = orderService.findAllFuture(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  /**
   * Handle the /orders/new endpoint.
   * @param pageable page of orders.
   * @param language selected language.
   * @return list of all new orders.
   */
  @GetMapping(path = "/new")
  public ResponseEntity<Page<OrderNewResponse>> findAllNew(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var ordersPage = orderService.findAllNew(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  /**
   * Handle the /orders/user endpoint.
   * @param pageable page of orders.
   * @param language selected language.
   * @return list of all new user orders.
   */
  @GetMapping(path = "/user")
  public ResponseEntity<Page<UserOrderResponse>> findAllNewUserOrders(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var ordersPage = orderService.findAllNewUserOrders(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  /**
   * Handle the /orders/user/history endpoint.
   * @param pageable page of orders.
   * @param language selected language.
   * @return list of all user finished orders.
   */
  @GetMapping(path = "/user/history")
  public ResponseEntity<Page<UserOrderResponse>> findAllUserOrdersHistory(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var ordersPage = orderService.findAllUserOrdersHistory(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  /**
   * Handle the /orders/amount endpoint.
   * @return new orders amount.
   */
  @GetMapping(path = "/amount")
  public ResponseEntity<Integer> findNewOrdersAmount() {
    var orders = orderService.findNewOrdersAmount();
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  /**
   * Handle the /orders/amount/day endpoint.
   * @return new orders amount per day.
   */
  @GetMapping(path = "/amount/day")
  public ResponseEntity<Integer> findNewOrdersAmountPerDay() {
    var orders = orderService.findNewOrdersAmountPerDay();
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  /**
   * Handle the /orders/user/amount endpoint.
   * @return user orders amount.
   */
  @GetMapping(path = "/user/amount")
  public ResponseEntity<Integer> findUserOrdersAmount() {
    var orders = orderService.findUserOrdersAmount();
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  /**
   * Handle the /orders/calculate endpoint.
   * @param orderTotalCostRequest request with parameters.
   * @return calculated order total cost.
   * Return one of the following status codes:
   * 200: successfully calculated order cost.
   * 406: unable to calculate order total cost, because specified invalid rental period.
   * @throws OrderPeriodValidationException if specified invalid rental period.
   */
  @PostMapping(path = "/calculate")
  public ResponseEntity<OrderTotalCostResponse> calculateTotalCost(
      @Valid @RequestBody OrderTotalCostRequest orderTotalCostRequest)
      throws OrderPeriodValidationException {
    var totalCost = orderService.calculateTotalCost(orderTotalCostRequest);
    return new ResponseEntity<>(totalCost, HttpStatus.OK);
  }

  /**
   * Handle the /orders/calculate endpoint.
   * @param createOrderRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully created new order.
   * 400: unable to create order, because user documents are not confirmed.
   * 403: unable to create order, because user contact phone is not specified.
   * 406: unable to create order, because specified invalid rental period.
   * @throws DocumentsNotConfirmedException if user documents are not confirmed.
   * @throws OrderPeriodValidationException if specified invalid rental period.
   * @throws PhoneNotSpecifiedException if user contact phone is not specified.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateOrderRequest createOrderRequest)
      throws DocumentsNotConfirmedException, OrderPeriodValidationException,
      PhoneNotSpecifiedException {
    orderService.create(createOrderRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/search endpoint.
   * @param orderSearchRequest request with parameters.
   * @param language selected language.
   * @return page of orders.
   */
  @PostMapping(path = "/search")
  public ResponseEntity<Page<OrderResponse>> findAll(
      @Valid @RequestBody OrderSearchRequest orderSearchRequest,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var ordersPage = orderService.findAll(orderSearchRequest, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  /**
   * Handle the /orders/approve/{id} endpoint.
   * @param id of the order which approved.
   * @return status 200 if order successfully approved.
   */
  @PutMapping(path = "/approve/{id}")
  public ResponseEntity<HttpStatus> approveOrder(@NotNull @Positive @PathVariable Long id) {
    orderService.approveOrder(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/cancel/{id} endpoint.
   * @param id of the order which canceled after payment.
   * @param orderRejectRequest request with parameters.
   * @return status 200 if order successfully canceled after payment.
   */
  @PutMapping(path = "/cancel/{id}")
  public ResponseEntity<HttpStatus> cancelOrderAfterPayment(
      @NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderRejectRequest orderRejectRequest) {
    orderService.cancelOrderAfterPayment(id, orderRejectRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/complete/{id} endpoint.
   * @param id of the order which completed.
   * @return status 200 if order successfully completed.
   */
  @PutMapping(path = "/complete/{id}")
  public ResponseEntity<HttpStatus> completeOrder(@NotNull @Positive @PathVariable Long id) {
    orderService.completeOrder(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/complete/{id}/penalty endpoint.
   * @param id of the order which completed with penalty.
   * @param orderCompleteWithPenaltyRequest request with parameters.
   * @return status 200 if order successfully completed with penalty.
   */
  @PutMapping(path = "/complete/{id}/penalty")
  public ResponseEntity<HttpStatus> completeOrderWithPenalty(
      @NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest) {
    orderService.completeOrderWithPenalty(id, orderCompleteWithPenaltyRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/reject/{id} endpoint.
   * @param id of the order which rejected.
   * @param orderRejectRequest request with parameters.
   * @return status 200 if order successfully rejected.
   */
  @PutMapping(path = "/reject/{id}")
  public ResponseEntity<HttpStatus> rejectOrder(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderRejectRequest orderRejectRequest) {
    orderService.rejectOrder(id, orderRejectRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/start/{id} endpoint.
   * @param id of the order which started.
   * @return status 200 if order successfully started.
   */
  @PutMapping(path = "/start/{id}")
  public ResponseEntity<HttpStatus> startRentalPeriod(@NotNull @Positive @PathVariable Long id) {
    orderService.startRentalPeriod(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/{id} endpoint.
   * @param id of the order which updated.
   * @param createOrderRequest request with parameters.
   * @return status 200 if order successfully updated.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid CreateOrderRequest createOrderRequest) {
    orderService.update(id, createOrderRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /orders/{id}/export endpoint.
   * @param id of the order which exported to PDF.
   * @return PDF file with order parameters.
   * Return one of the following status codes:
   * 200: if order successfully exported to PDF.
   * 405: unable to export order to PDF, document is not generated or font not fount.
   * @throws FontNotFoundException if font for PDF document not found.
   * @throws DocumentNotGeneratedException if document not generated.
   */
  @GetMapping(path = "/{id}/export")
  public ResponseEntity<ByteArrayResource> exportOrderToPDF(
      @NotNull @Positive @PathVariable Long id)
      throws FontNotFoundException, DocumentNotGeneratedException {
    var response = orderService.exportOrderToPDF(id);
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(response);
  }
}
