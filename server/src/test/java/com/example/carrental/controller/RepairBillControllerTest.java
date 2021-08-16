package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.service.RepairBillService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RepairBillControllerTest {

  private static RepairBillResponse repairBillResponse;
  private static RepairBillSearchRequest repairBillSearchRequest;
  private static RepairBillHistoryResponse repairBillHistoryResponse;
  private static RepairBillNewResponse repairBillNewResponse;
  private static Pageable pageable;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RepairBillService repairBillService;

  @BeforeAll
  public static void setup() {
    repairBillResponse = RepairBillResponse.builder().id(1L).userEmail("test@gmail.com")
        .sentDate("date").totalCost(BigDecimal.valueOf(5)).message("message").paymentDate("date")
        .orderId(1L).carBrandModel("name").carVin("vin").locationName("name").build();
    repairBillSearchRequest = new RepairBillSearchRequest();
    repairBillHistoryResponse = RepairBillHistoryResponse.builder().id(1L).sentDate("date")
        .totalCost(BigDecimal.valueOf(5)).message("message").paymentDate("date")
        .orderId(1L).carBrandModel("name").carVin("vin").locationName("name").build();
    repairBillNewResponse = RepairBillNewResponse.builder().id(1L).sentDate("date")
        .totalCost(BigDecimal.valueOf(5)).orderId(1L).carBrandModel("name").carVin("vin")
        .locationName("name").build();
    pageable = Pageable.ofSize(10).withPage(0);
  }

  @AfterAll
  public static void teardown() {
    repairBillResponse = null;
    repairBillSearchRequest = null;
    repairBillHistoryResponse = null;
    repairBillNewResponse = null;
    pageable = null;
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    Page<RepairBillResponse> page = new PageImpl<>(Collections.singletonList(repairBillResponse));

    when(repairBillService.findAll(repairBillSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/repairbills")
        .content(objectMapper.writeValueAsString(repairBillSearchRequest))
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
      throws Exception {
    Page<RepairBillResponse> page = new PageImpl<>(Collections.singletonList(repairBillResponse));

    when(repairBillService.findAll(repairBillSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/repairbills")
        .content(objectMapper.writeValueAsString(repairBillSearchRequest))
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
      throws Exception {
    Page<RepairBillResponse> page = new PageImpl<>(Collections.singletonList(repairBillResponse));

    when(repairBillService.findAll(repairBillSearchRequest, ENGLISH)).thenReturn(page);

    mockMvc.perform(post("/repairbills")
        .content(objectMapper.writeValueAsString(repairBillSearchRequest))
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenFindAllUserBillsHistory_thenReturnResponse200() throws Exception {
    Page<RepairBillHistoryResponse> page = new PageImpl<>(
        Collections.singletonList(repairBillHistoryResponse));

    when(repairBillService.findAllUserBillsHistory(pageable, ENGLISH))
        .thenReturn(page);

    mockMvc.perform(get("/repairbills/user?page={page}&size={size}", 0, 10)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllUserBillsHistory_thenReturnResponse401()
      throws Exception {
    Page<RepairBillHistoryResponse> page = new PageImpl<>(
        Collections.singletonList(repairBillHistoryResponse));

    when(repairBillService.findAllUserBillsHistory(pageable, ENGLISH))
        .thenReturn(page);

    mockMvc.perform(get("/repairbills/user?page={page}&size={size}", 0, 10)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenFindAllNewUserBills_thenReturnResponse200() throws Exception {
    Page<RepairBillNewResponse> page = new PageImpl<>(
        Collections.singletonList(repairBillNewResponse));

    when(repairBillService.findAllNewUserBills(pageable, ENGLISH))
        .thenReturn(page);

    mockMvc.perform(get("/repairbills/user/new?page={page}&size={size}", 0, 10)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNewUserBills_thenReturnResponse401()
      throws Exception {
    Page<RepairBillNewResponse> page = new PageImpl<>(
        Collections.singletonList(repairBillNewResponse));

    when(repairBillService.findAllNewUserBills(pageable, ENGLISH))
        .thenReturn(page);

    mockMvc.perform(get("/repairbills/user/new?page={page}&size={size}", 0, 10)
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenFindUserRepairBillsAmount_thenReturnResponse200()
      throws Exception {
    when(repairBillService.findNewUserRepairBillsAmount()).thenReturn(1);

    mockMvc.perform(get("/repairbills/user/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindUserRepairBillsAmount_thenReturnResponse401()
      throws Exception {
    when(repairBillService.findNewUserRepairBillsAmount()).thenReturn(1);

    mockMvc.perform(get("/repairbills/user/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenPayBill_thenReturnResponse200() throws Exception {
    doNothing().when(repairBillService).payBill(1L);

    mockMvc.perform(post("/repairbills/pay/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenPayBill_thenReturnResponse401()
      throws Exception {
    doNothing().when(repairBillService).payBill(1L);

    mockMvc.perform(post("/repairbills/pay/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}