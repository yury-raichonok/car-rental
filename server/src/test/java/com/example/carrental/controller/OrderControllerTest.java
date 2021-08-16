package com.example.carrental.controller;


import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import com.example.carrental.service.exceptions.PhoneNotSpecifiedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

  private static Pageable pageable;
  private static OrderInformationResponse orderInformationResponse;
  private static OrderNewResponse orderNewResponse;
  private static UserOrderResponse userOrderResponse;
  private static OrderTotalCostResponse orderTotalCostResponse;
  private static OrderTotalCostRequest orderTotalCostRequest;
  private static CreateOrderRequest createOrderRequest;
  private static OrderResponse orderResponse;
  private static OrderSearchRequest orderSearchRequest;
  private static OrderRejectRequest orderRejectRequest;
  private static OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @BeforeAll
  public static void setup() {
    pageable = Pageable.ofSize(10).withPage(0);
    orderInformationResponse = OrderInformationResponse.builder().id(1L).userId(1L)
        .userEmail("test@gmail.com").pickUpDate("date").returnDate("date")
        .totalCost(BigDecimal.valueOf(5)).paymentStatus("status").sentDate("date")
        .carBrandModel("name").carVin("vin").locationName("name").build();
    orderNewResponse = OrderNewResponse.builder().id(1L).userId(1L)
        .userEmail("test@gmail.com").pickUpDate("date").returnDate("date")
        .totalCost(BigDecimal.valueOf(5)).sentDate("date").carBrandModel("name").carVin("vin")
        .locationName("name").build();
    userOrderResponse = UserOrderResponse.builder().id(1L).pickUpDate("date")
        .returnDate("date").totalCost(BigDecimal.valueOf(5)).sentDate("date")
        .carBrandModel("name").carVin("vin").locationName("name").build();
    orderTotalCostResponse = OrderTotalCostResponse.builder().totalCost(BigDecimal.valueOf(5))
        .build();
    orderTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(5).pickUpDate(
        LocalDateTime.now().plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).build();
    createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).totalCost(5).carId(1L)
        .build();
    orderResponse = OrderResponse.builder().id(1L).userEmail("test@gmail.com")
        .pickUpDate("date").returnDate("date").totalCost(BigDecimal.valueOf(5))
        .paymentStatus("status").sentDate("date").carBrandModel("name").carVin("cin")
        .locationName("name").build();
    orderSearchRequest = new OrderSearchRequest();
    orderRejectRequest = OrderRejectRequest.builder().comments("name").build();
    orderCompleteWithPenaltyRequest = OrderCompleteWithPenaltyRequest.builder()
        .message("message").totalCost(5).build();
    createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusDays(1)).returnDate(LocalDateTime.now().plusDays(2)).totalCost(4).carId(1).build();
  }

  @AfterAll
  public static void teardown() {
    pageable = null;
    orderInformationResponse = null;
    orderNewResponse = null;
    userOrderResponse = null;
    orderTotalCostResponse = null;
    orderTotalCostRequest = null;
    createOrderRequest = null;
    orderResponse = null;
    orderSearchRequest = null;
    orderRejectRequest = null;
    orderCompleteWithPenaltyRequest = null;
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllCurrent_thenReturnResponse200() throws Exception {
    Page<OrderInformationResponse> page = new PageImpl<>(
        Collections.singletonList(orderInformationResponse));

    when(orderService.findAllCurrent(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/current?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAllCurrent_thenReturnResponse403()
      throws Exception {
    Page<OrderInformationResponse> page = new PageImpl<>(
        Collections.singletonList(orderInformationResponse));

    when(orderService.findAllCurrent(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/current?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllCurrent_thenReturnResponse401()
      throws Exception {
    Page<OrderInformationResponse> page = new PageImpl<>(
        Collections.singletonList(orderInformationResponse));

    when(orderService.findAllCurrent(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/current?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllFuture_thenReturnResponse200() throws Exception {
    Page<OrderInformationResponse> page = new PageImpl<>(
        Collections.singletonList(orderInformationResponse));

    when(orderService.findAllFuture(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/future?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAllFuture_thenReturnResponse403()
      throws Exception {
    Page<OrderInformationResponse> page = new PageImpl<>(
        Collections.singletonList(orderInformationResponse));

    when(orderService.findAllFuture(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/future?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllFuture_thenReturnResponse401()
      throws Exception {
    Page<OrderInformationResponse> page = new PageImpl<>(
        Collections.singletonList(orderInformationResponse));

    when(orderService.findAllFuture(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/future?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllNew_thenReturnResponse200() throws Exception {
    Page<OrderNewResponse> page = new PageImpl<>(Collections.singletonList(orderNewResponse));

    when(orderService.findAllNew(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAllNew_thenReturnResponse403()
      throws Exception {
    Page<OrderNewResponse> page = new PageImpl<>(Collections.singletonList(orderNewResponse));

    when(orderService.findAllNew(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNew_thenReturnResponse401()
      throws Exception {
    Page<OrderNewResponse> page = new PageImpl<>(Collections.singletonList(orderNewResponse));

    when(orderService.findAllNew(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenFindAllNewUserOrders_thenReturnResponse200() throws Exception {
    Page<UserOrderResponse> page = new PageImpl<>(Collections.singletonList(userOrderResponse));

    when(orderService.findAllNewUserOrders(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/user?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNewUserOrders_thenReturnResponse401()
      throws Exception {
    Page<UserOrderResponse> page = new PageImpl<>(Collections.singletonList(userOrderResponse));

    when(orderService.findAllNewUserOrders(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/user?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenFindAllUserOrdersHistory_thenReturnResponse200() throws Exception {
    Page<UserOrderResponse> page = new PageImpl<>(Collections.singletonList(userOrderResponse));

    when(orderService.findAllUserOrdersHistory(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/user/history?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllUserOrdersHistory_thenReturnResponse401()
      throws Exception {
    Page<UserOrderResponse> page = new PageImpl<>(Collections.singletonList(userOrderResponse));

    when(orderService.findAllUserOrdersHistory(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/orders/user/history?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewOrdersAmount_thenReturnResponse200() throws Exception {
    when(orderService.findNewOrdersAmount()).thenReturn(1);

    mockMvc.perform(get("/orders/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewOrdersAmount_thenReturnResponse403()
      throws Exception {
    when(orderService.findNewOrdersAmount()).thenReturn(1);

    mockMvc.perform(get("/orders/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewOrdersAmount_thenReturnResponse401()
      throws Exception {
    when(orderService.findNewOrdersAmount()).thenReturn(1);

    mockMvc.perform(get("/orders/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewOrdersAmountPerDay_thenReturnResponse200() throws Exception {
    when(orderService.findNewOrdersAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/orders/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewOrdersAmountPerDay_thenReturnResponse403()
      throws Exception {
    when(orderService.findNewOrdersAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/orders/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewOrdersAmountPerDay_thenReturnResponse401()
      throws Exception {
    when(orderService.findNewOrdersAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/orders/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenFindUserOrdersAmount_thenReturnResponse200() throws Exception {
    when(orderService.findUserOrdersAmount()).thenReturn(1);

    mockMvc.perform(get("/orders/user/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindUserOrdersAmount_thenReturnResponse401()
      throws Exception {
    when(orderService.findUserOrdersAmount()).thenReturn(1);

    mockMvc.perform(get("/orders/user/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenCalculateTotalCost_thenReturnResponse200() throws Exception {
    when(orderService.calculateTotalCost(orderTotalCostRequest)).thenReturn(orderTotalCostResponse);

    mockMvc.perform(post("/orders/calculate")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(orderTotalCostRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenInvalidRequest_whenCalculateTotalCost_thenReturnResponse406() throws Exception {
    when(orderService.calculateTotalCost(orderTotalCostRequest))
        .thenThrow(new OrderPeriodValidationException("Invalid rental period"));

    mockMvc.perform(post("/orders/calculate")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(orderTotalCostRequest)))
        .andDo(print())
        .andExpect(status().isNotAcceptable());
  }

  @Test
  void givenValidRequestUnauthorized_whenCalculateTotalCost_thenReturnResponse401()
      throws Exception {
    when(orderService.calculateTotalCost(orderTotalCostRequest)).thenReturn(orderTotalCostResponse);

    mockMvc.perform(post("/orders/calculate")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(orderTotalCostRequest)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    doNothing().when(orderService).create(createOrderRequest);

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithNotConfirmedDocuments_whenCreateFailed_thenReturnResponse400()
      throws Exception {
    doThrow(new DocumentsNotConfirmedException("Documents are not confirmed"))
        .when(orderService).create(createOrderRequest);

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Documents are not confirmed")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    doThrow(new OrderPeriodValidationException("Invalid rental period"))
        .when(orderService).create(createOrderRequest);

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithNoPhoneSpecified_whenCreateFailed_thenReturnResponse403()
      throws Exception {
    doThrow(new PhoneNotSpecifiedException("Phone is not specified"))
        .when(orderService).create(createOrderRequest);

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(content().string(containsString("Phone is not specified")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).create(createOrderRequest);

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    Page<OrderResponse> page = new PageImpl<>(Collections.singletonList(orderResponse));

    when(orderService.findAll(orderSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/orders/search")
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .content(objectMapper.writeValueAsString(orderSearchRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
      throws Exception {
    Page<OrderResponse> page = new PageImpl<>(Collections.singletonList(orderResponse));

    when(orderService.findAll(orderSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/orders/search")
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .content(objectMapper.writeValueAsString(orderSearchRequest)))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
      throws Exception {
    Page<OrderResponse> page = new PageImpl<>(Collections.singletonList(orderResponse));

    when(orderService.findAll(orderSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/orders/search")
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .content(objectMapper.writeValueAsString(orderSearchRequest)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenApproveOrderSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(orderService).approveOrder(1L);

    mockMvc.perform(put("/orders/approve/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenApproveOrderFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).approveOrder(1L);

    mockMvc.perform(put("/orders/approve/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenApproveOrderFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).approveOrder(1L);

    mockMvc.perform(put("/orders/approve/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCancelOrderAfterPaymentSuccessful_thenReturnResponse200()
      throws Exception {
    doNothing().when(orderService).cancelOrderAfterPayment(1L, orderRejectRequest);

    mockMvc.perform(put("/orders/cancel/{id}", 1)
        .content(objectMapper.writeValueAsString(orderRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenCancelOrderAfterPaymentFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).cancelOrderAfterPayment(1L, orderRejectRequest);

    mockMvc.perform(put("/orders/cancel/{id}", 1)
        .content(objectMapper.writeValueAsString(orderRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenCancelOrderAfterPaymentFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).cancelOrderAfterPayment(1L, orderRejectRequest);

    mockMvc.perform(put("/orders/cancel/{id}", 1)
        .content(objectMapper.writeValueAsString(orderRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCompleteOrderSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(orderService).completeOrder(1L);

    mockMvc.perform(put("/orders/complete/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenCompleteOrderFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).completeOrder(1L);

    mockMvc.perform(put("/orders/complete/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenCompleteOrderFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).completeOrder(1L);

    mockMvc.perform(put("/orders/complete/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCompleteOrderWithPenaltySuccessful_thenReturnResponse200()
      throws Exception {
    doNothing().when(orderService).completeOrderWithPenalty(1L, orderCompleteWithPenaltyRequest);

    mockMvc.perform(put("/orders/complete/{id}/penalty", 1)
        .content(objectMapper.writeValueAsString(orderCompleteWithPenaltyRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenCompleteOrderWithPenaltyFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).completeOrderWithPenalty(1L, orderCompleteWithPenaltyRequest);

    mockMvc.perform(put("/orders/complete/{id}/penalty", 1)
        .content(objectMapper.writeValueAsString(orderCompleteWithPenaltyRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenCompleteOrderWithPenaltyFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).completeOrderWithPenalty(1L, orderCompleteWithPenaltyRequest);

    mockMvc.perform(put("/orders/complete/{id}/penalty", 1)
        .content(objectMapper.writeValueAsString(orderCompleteWithPenaltyRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenRejectOrderSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(orderService).rejectOrder(1L, orderRejectRequest);

    mockMvc.perform(put("/orders/reject/{id}", 1)
        .content(objectMapper.writeValueAsString(orderRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenRejectOrderFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).rejectOrder(1L, orderRejectRequest);

    mockMvc.perform(put("/orders/reject/{id}", 1)
        .content(objectMapper.writeValueAsString(orderRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenRejectOrderFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).rejectOrder(1L, orderRejectRequest);

    mockMvc.perform(put("/orders/reject/{id}", 1)
        .content(objectMapper.writeValueAsString(orderRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenStartRentalPeriodSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(orderService).startRentalPeriod(1L);

    mockMvc.perform(put("/orders/start/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenStartRentalPeriodFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).startRentalPeriod(1L);

    mockMvc.perform(put("/orders/start/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenStartRentalPeriodFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).startRentalPeriod(1L);

    mockMvc.perform(put("/orders/start/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(orderService).update(1L, createOrderRequest);

    mockMvc.perform(put("/orders/{id}", 1)
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(orderService).update(1L, createOrderRequest);

    mockMvc.perform(put("/orders/{id}", 1L)
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(orderService).update(1L, createOrderRequest);

    mockMvc.perform(put("/orders/{id}", 1L)
        .content(objectMapper.writeValueAsString(createOrderRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequestAsUser_whenExportOrderToPDF_thenReturnResponse200()
      throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(orderService.exportOrderToPDF(1L)).thenReturn(response);

    mockMvc.perform(get("/orders/{id}/export", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_PDF));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequestAsUser_whenExportOrderToPDFFontNotFound_thenReturnResponse405()
      throws Exception {
    when(orderService.exportOrderToPDF(1L)).thenThrow(new FontNotFoundException("Font not found"));

    mockMvc.perform(get("/orders/{id}/export", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequestAsUser_whenExportOrderToPDFDocumentNotGenerated_thenReturnResponse405()
      throws Exception {
    when(orderService.exportOrderToPDF(1L))
        .thenThrow(new FontNotFoundException("Document not generated"));

    mockMvc.perform(get("/orders/{id}/export", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  void givenValidRequestUnauthorized_whenExportOrderToPDF_thenReturnResponse401()
      throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(orderService.exportOrderToPDF(1L)).thenReturn(response);

    mockMvc.perform(get("/orders/{id}/export", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}