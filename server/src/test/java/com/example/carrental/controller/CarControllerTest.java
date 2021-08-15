package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.car.CarAdminSearchResponse;
import com.example.carrental.controller.dto.car.CarByIdResponse;
import com.example.carrental.controller.dto.car.CarProfitableOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarEngineType;
import com.example.carrental.service.CarService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarService carService;

  @Test
  void givenValidRequest_findAllProfitableOffers_thenReturnResponse200() throws Exception {
    var response = Arrays.asList(CarProfitableOfferResponse.builder().id(1L).brand("name").build(),
        CarProfitableOfferResponse.builder().id(2L).brand("name1").build());
    when(carService.findAllProfitableOffers(ENGLISH)).thenReturn(response);

    mockMvc.perform(get("/cars/profitable").cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].brand", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_findAllProfitableOffersNoContent_thenReturnResponse204() throws Exception {
    when(carService.findAllProfitableOffers(ENGLISH)).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/cars/profitable").cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_findCarById_thenReturnResponse200() throws Exception {
    var response = CarByIdResponse.builder().id(1L).brand("name").build();
    when(carService.findCarById(1L, ENGLISH)).thenReturn(response);

    mockMvc.perform(get("/cars/search/{id}", 1).cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenInvalidRequest_findCarById_thenReturnResponse400() throws Exception {
    when(carService.findCarById(1L, ENGLISH)).thenThrow(new IllegalStateException("No such car"));

    mockMvc.perform(get("/cars/search/{id}", 1).cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("No such car"));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    var createCarRequest = CreateCarRequest.builder().brandId(1L).modelId(1L).vin("vin")
        .locationId(1L).carClassId(1L).dateOfIssue(LocalDate.now()).color("color")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5)
        .baggageAmt(3).autoTransmission(true).hasConditioner(true).isInRental(true).costPerHour(
            BigDecimal.valueOf(5)).build();
    doNothing().when(carService).create(createCarRequest);

    mockMvc.perform(post("/cars")
        .content(objectMapper.writeValueAsString(createCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedClassName_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    var createCarRequest = CreateCarRequest.builder().brandId(1L).modelId(1L).vin("vin")
        .locationId(1L).carClassId(1L).dateOfIssue(LocalDate.now()).color("color")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5)
        .baggageAmt(3).autoTransmission(true).hasConditioner(true).isInRental(true).costPerHour(
            BigDecimal.valueOf(5)).build();
    doThrow(new EntityAlreadyExistsException("Entity with same vin already exists"))
        .when(carService).create(createCarRequest);

    mockMvc.perform(post("/cars")
        .content(objectMapper.writeValueAsString(createCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same vin already exists")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    var createCarRequest = CreateCarRequest.builder().brandId(1L).modelId(1L).vin("vin")
        .locationId(1L).carClassId(1L).dateOfIssue(LocalDate.now()).color("color")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5)
        .baggageAmt(3).autoTransmission(true).hasConditioner(true).isInRental(true).costPerHour(
            BigDecimal.valueOf(5)).build();

    mockMvc.perform(post("/cars")
        .content(objectMapper.writeValueAsString(createCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenValidRequest_whenSearchCars_thenReturnResponse200() throws Exception {
    CarSearchRequest carSearchRequest = new CarSearchRequest();
    var responses = Arrays.asList(CarSearchResponse.builder().id(1L).brand("name").model("name")
            .carClass("name").yearOfIssue("2000").bodyType("name").engineType("name").passengersAmt(5)
            .baggageAmt(3).costPerHour(BigDecimal.valueOf(5)).locationName("name").build(),
        CarSearchResponse.builder().id(2L).brand("name").model("name").carClass("name")
            .yearOfIssue("2000").bodyType("name").engineType("name").passengersAmt(5).baggageAmt(3)
            .costPerHour(BigDecimal.valueOf(5)).locationName("name").build());
    when(carService.searchCars(carSearchRequest, ENGLISH)).thenReturn(new PageImpl<>(responses));

    mockMvc.perform(post("/cars/search")
        .content(objectMapper.writeValueAsString(carSearchRequest))
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenSearchCarsByAdmin_thenReturnResponse200() throws Exception {
    CarSearchRequest carSearchRequest = new CarSearchRequest();
    var responses = Arrays.asList(CarAdminSearchResponse.builder().id(1L).vin("vin").brand("brand")
            .model("model").carClass("class").carClassId(1L).dateOfIssue(LocalDate.of(2000, 1, 1))
            .bodyType("name").isAutomaticTransmission(true).color("name").engineType("name")
            .passengersAmt(5).baggageAmt(5).hasConditioner(true).costPerHour(5).locationId(1L)
            .locationName("name").isInRental(true).build(),
        CarAdminSearchResponse.builder().id(1L).vin("vin")
            .brand("brand").model("model").carClass("class").carClassId(1L)
            .dateOfIssue(LocalDate.of(2000, 1, 1)).bodyType("name")
            .isAutomaticTransmission(true).color("name").engineType("name").passengersAmt(5)
            .baggageAmt(5).hasConditioner(true).costPerHour(5).locationId(1L).locationName("name")
            .isInRental(true).build());

    when(carService.searchCarsByAdmin(carSearchRequest, ENGLISH))
        .thenReturn(new PageImpl<>(responses));

    mockMvc.perform(post("/cars/search/admin")
        .content(objectMapper.writeValueAsString(carSearchRequest))
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenSearchCarsByAdminFailed_thenReturnResponse401()
      throws Exception {
    CarSearchRequest carSearchRequest = new CarSearchRequest();

    mockMvc.perform(post("/cars/search/admin")
        .content(objectMapper.writeValueAsString(carSearchRequest))
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUploadCarImageSuccessful_thenReturnResponse200() throws Exception {
    var carFile = new MockMultipartFile("carFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(carService).uploadCarImage(1L, carFile);

    mockMvc.perform(multipart("/cars/{id}/upload/image", 1L).file(carFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUploadCarImageFailed_thenReturnResponse400() throws Exception {
    var carFile = new MockMultipartFile("carFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doThrow(new IllegalStateException("Bad request")).when(carService)
        .uploadCarImage(1L, carFile);

    mockMvc.perform(multipart("/cars/{id}/upload/image", 1L).file(carFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUploadCarImageFailed_thenReturnResponse403() throws Exception {
    var carFile = new MockMultipartFile("carFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(carService).uploadCarImage(1L, carFile);

    mockMvc.perform(multipart("/cars/{id}/upload/image", 1L).file(carFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUploadCarImageFailed_thenReturnResponse401()
      throws Exception {
    var carFile = new MockMultipartFile("carFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(carService).uploadCarImage(1L, carFile);

    mockMvc.perform(multipart("/cars/{id}/upload/image", 1L).file(carFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();

    doNothing().when(carService).update(1L, updateCarRequest);

    mockMvc.perform(put("/cars/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequestWithExistingName_whenUpdateFailed_thenReturnResponse406()
      throws Exception {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();

    doThrow(new EntityAlreadyExistsException("Entity with same vin already exists"))
        .when(carService).update(1L, updateCarRequest);

    mockMvc.perform(put("/cars/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same vin already exists")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateFailed_thenReturnResponse400()
      throws Exception {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();

    doThrow(new IllegalStateException("Bad request")).when(carService)
        .update(1L, updateCarRequest);

    mockMvc.perform(put("/cars/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateFailed_thenReturnResponse403()
      throws Exception {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();

    doNothing().when(carService).update(1L, updateCarRequest);

    mockMvc.perform(put("/cars/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateFailed_thenReturnResponse401()
      throws Exception {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();

    doNothing().when(carService).update(1L, updateCarRequest);

    mockMvc.perform(put("/cars/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}