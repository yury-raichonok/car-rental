package com.example.carrental.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

  private final OrderService orderService;

  @GetMapping(path = "/current")
  public ResponseEntity<Page<OrderInformationResponse>> findAllCurrent(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAllCurrent(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @GetMapping(path = "/future")
  public ResponseEntity<Page<OrderInformationResponse>> findAllFuture(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAllFuture(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @GetMapping(path = "/new")
  public ResponseEntity<Page<OrderNewResponse>> findAllNew(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAllNew(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @GetMapping(path = "/user")
  public ResponseEntity<Page<UserOrderResponse>> findAllNewUserOrders(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAllNewUserOrders(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @GetMapping(path = "/user/history")
  public ResponseEntity<Page<UserOrderResponse>> findAllUserOrdersHistory(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAllUserOrdersHistory(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @PostMapping(path = "/calculate")
  public ResponseEntity<OrderTotalCostResponse> calculateTotalCost(
      @Valid @RequestBody OrderTotalCostRequest orderTotalCostRequest)
      throws OrderPeriodValidationException {
    var totalCost = orderService.calculateTotalCost(orderTotalCostRequest);
    return new ResponseEntity<>(totalCost, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateOrderRequest createOrderRequest)
      throws DocumentsNotConfirmedException, OrderPeriodValidationException {
    orderService.create(createOrderRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/search")
  public ResponseEntity<Page<OrderResponse>> findAll(
      @Valid @RequestBody OrderSearchRequest orderSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAll(orderSearchRequest, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @PutMapping(path = "/approve/{id}")
  public ResponseEntity<HttpStatus> approveOrder(@NotNull @Positive @PathVariable Long id) {
    orderService.approveOrder(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/cancel/{id}")
  public ResponseEntity<HttpStatus> cancelOrderAfterPayment(
      @NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderRejectRequest orderRejectRequest) {
    orderService.cancelOrderAfterPayment(id, orderRejectRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/complete/{id}")
  public ResponseEntity<HttpStatus> completeOrder(@NotNull @Positive @PathVariable Long id) {
    orderService.completeOrder(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/complete/{id}/penalty")
  public ResponseEntity<HttpStatus> completeOrderWithPenalty(
      @NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest) {
    orderService.completeOrderWithPenalty(id, orderCompleteWithPenaltyRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/reject/{id}")
  public ResponseEntity<HttpStatus> rejectOrder(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderRejectRequest orderRejectRequest) {
    orderService.rejectOrder(id, orderRejectRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/start/{id}")
  public ResponseEntity<HttpStatus> startRentalPeriod(@NotNull @Positive @PathVariable Long id) {
    orderService.startRentalPeriod(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid CreateOrderRequest createOrderRequest) {
    orderService.update(id, createOrderRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(path = "/{id}/export")
  public ResponseEntity<ByteArrayResource> exportOrderToPDF(
      @NotNull @Positive @PathVariable Long id)
      throws FontNotFoundException, DocumentNotGeneratedException {
    var response = orderService.exportOrderToPDF(id);
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(response);
  }
}
