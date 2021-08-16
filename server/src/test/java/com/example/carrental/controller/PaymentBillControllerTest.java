package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.service.PaymentBillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentBillControllerTest {

  private static PaymentBillResponse paymentBillResponse;
  private static PaymentBillSearchRequest paymentBillSearchRequest;
  private static UserNewPaymentBillsResponse userNewPaymentBillsResponse;
  private static Pageable pageable;
  private static UserPaymentBillsResponse userPaymentBillsResponse;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PaymentBillService paymentBillService;

  @BeforeAll
  public static void setup() {
    paymentBillResponse = PaymentBillResponse.builder().id(1L).userEmail("test@gmail.com")
        .sentDate("date").expirationTime("date").totalCost(BigDecimal.valueOf(5)).orderId(1L)
        .carBrandModel("name").carVin("vin").locationName("name").status(true).build();
    paymentBillSearchRequest = new PaymentBillSearchRequest();
    userNewPaymentBillsResponse = UserNewPaymentBillsResponse.builder().id(1L).sentDate("date")
        .expirationTime("date").totalCost(5).orderId(1L).carBrandModel("name").carVin("vin")
        .locationName("name").build();
    pageable = Pageable.ofSize(10).withPage(0);
    userPaymentBillsResponse = UserPaymentBillsResponse.builder().id(1L).sentDate("date")
        .totalCost(5).paymentDate("date").orderId(1L).carBrandModel("name").carVin("vin")
        .locationName("name").build();
  }

  @AfterAll
  public static void teardown() {
    paymentBillResponse = null;
    paymentBillSearchRequest = null;
    userNewPaymentBillsResponse = null;
    userPaymentBillsResponse = null;
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    Page<PaymentBillResponse> page = new PageImpl<>(Collections.singletonList(paymentBillResponse));

    when(paymentBillService.findAll(paymentBillSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/paymentbills")
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .content(objectMapper.writeValueAsString(paymentBillSearchRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
      throws Exception {
    Page<PaymentBillResponse> page = new PageImpl<>(Collections.singletonList(paymentBillResponse));

    when(paymentBillService.findAll(paymentBillSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/paymentbills")
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .content(objectMapper.writeValueAsString(paymentBillSearchRequest)))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
      throws Exception {
    Page<PaymentBillResponse> page = new PageImpl<>(Collections.singletonList(paymentBillResponse));

    when(paymentBillService.findAll(paymentBillSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/paymentbills")
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .content(objectMapper.writeValueAsString(paymentBillSearchRequest)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenFindAllNewUserBills_thenReturnResponse200() throws Exception {
    Page<UserNewPaymentBillsResponse> page = new PageImpl<>(
        Collections.singletonList(userNewPaymentBillsResponse));

    when(paymentBillService.findAllNewUserBills(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/paymentbills/user/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNewUserBills_thenReturnResponse401()
      throws Exception {
    Page<UserNewPaymentBillsResponse> page = new PageImpl<>(
        Collections.singletonList(userNewPaymentBillsResponse));

    when(paymentBillService.findAllNewUserBills(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/paymentbills/user/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenFindAllUserBillsHistory_thenReturnResponse200() throws Exception {
    Page<UserPaymentBillsResponse> page = new PageImpl<>(
        Collections.singletonList(userPaymentBillsResponse));

    when(paymentBillService.findAllUserBillsHistory(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/paymentbills/user?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllUserBillsHistory_thenReturnResponse401()
      throws Exception {
    Page<UserPaymentBillsResponse> page = new PageImpl<>(
        Collections.singletonList(userPaymentBillsResponse));

    when(paymentBillService.findAllUserBillsHistory(pageable, ENGLISH)).thenReturn(page);

    mockMvc.perform(get("/paymentbills/user?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenFindNewUserPaymentBillsAmount_thenReturnResponse200()
      throws Exception {
    when(paymentBillService.findNewUserBillsAmount()).thenReturn(1);

    mockMvc.perform(get("/paymentbills/user/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewOrdersAmount_thenReturnResponse401()
      throws Exception {
    when(paymentBillService.findNewUserBillsAmount()).thenReturn(1);

    mockMvc.perform(get("/paymentbills/user/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenApproveOrderSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(paymentBillService).approveWithoutPayment(1L);

    mockMvc.perform(put("/paymentbills/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenApproveOrderFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(paymentBillService).approveWithoutPayment(1L);

    mockMvc.perform(put("/paymentbills/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenApproveOrderFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(paymentBillService).approveWithoutPayment(1L);

    mockMvc.perform(put("/paymentbills/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenPayBill_thenReturnResponse200() throws Exception {
    doNothing().when(paymentBillService).payBill(1L);

    mockMvc.perform(post("/paymentbills/pay/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenPayBill_thenReturnResponse401()
      throws Exception {
    doNothing().when(paymentBillService).payBill(1L);

    mockMvc.perform(post("/paymentbills/pay/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}