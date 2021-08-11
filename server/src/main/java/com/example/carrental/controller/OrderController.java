package com.example.carrental.controller;

import com.example.carrental.controller.dto.order.CreateOrderRequest;
import com.example.carrental.controller.dto.order.OrderCompleteWithPenaltyRequest;
import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderRejectRequest;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.exceptions.CarAlreadyBookedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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

  @PostMapping(path = "/search")
  public ResponseEntity<Page<OrderResponse>> findAll(
      @Valid @RequestBody OrderSearchRequest orderSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAll(orderSearchRequest, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

  @GetMapping(path = "/new")
  public ResponseEntity<Page<OrderNewResponse>> findAllNew(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var ordersPage = orderService.findAllNew(pageable, language);
    return new ResponseEntity<>(ordersPage, HttpStatus.OK);
  }

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

  @PostMapping(path = "/calculate")
  public ResponseEntity<?> calculateTotalCost(
      @RequestBody OrderTotalCostRequest orderTotalCostRequest) {
    try {
      var totalCost = orderService.calculateTotalCost(orderTotalCostRequest);
      return new ResponseEntity<>(totalCost, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<String> create(@RequestBody CreateOrderRequest createOrderRequest) {
    try {
      var response = orderService.create(createOrderRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (DocumentsNotConfirmedException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (CarAlreadyBookedException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  @PutMapping(path = "/reject/{id}")
  public ResponseEntity<?> rejectOrder(@NotNull @Positive @PathVariable Long id,
      @RequestBody OrderRejectRequest orderRejectRequest) {
    try {
      var response = orderService.rejectOrder(id, orderRejectRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/approve/{id}")
  public ResponseEntity<?> approveOrder(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = orderService.approveOrder(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/complete/{id}")
  public ResponseEntity<?> completeOrder(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = orderService.completeOrder(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }


  @PutMapping(path = "/complete/{id}/penalty")
  public ResponseEntity<?> completeOrderWithPenalty(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest) {
    try {
      var response = orderService.completeOrderWithPenalty(id, orderCompleteWithPenaltyRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<?> update(@NotNull @Positive @PathVariable Long id,
      @Valid CreateOrderRequest createOrderRequest) {
    try {
      var response = orderService.update(id, createOrderRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/start/{id}")
  public ResponseEntity<?> startRentalPeriod(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = orderService.startRentalPeriod(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/cancel/{id}")
  public ResponseEntity<?> cancelOrderAfterPayment(@NotNull @Positive @PathVariable Long id,
      @RequestBody OrderRejectRequest orderRejectRequest) {
    try {
      var response = orderService.cancelOrderAfterPayment(id, orderRejectRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/user")
  public ResponseEntity<?> findAllNewUserOrders(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var ordersPage = orderService.findAllNewUserOrders(pageable, language);
      return new ResponseEntity<>(ordersPage, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/user/history")
  public ResponseEntity<?> findAllUserOrdersHistory(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var ordersPage = orderService.findAllUserOrdersHistory(pageable, language);
      return new ResponseEntity<>(ordersPage, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/{id}/export")
  public ResponseEntity<?> exportOrderToPDF(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = orderService.exportOrderToPDF(id);
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_PDF)
          .body(response);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (FontNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
  }
}
