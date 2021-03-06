package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;

import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.entity.bill.PaymentBill;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface for mapping Payment Bill entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface PaymentBillMapper {

  /**
   * @param paymentBill data.
   * @return PaymentBillResponse DTO.
   */
  @Mapping(target = "id", source = "paymentBill.id")
  @Mapping(target = "userEmail", source = "paymentBill.order.user.email")
  @Mapping(target = "sentDate", source = "paymentBill.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "expirationTime", source = "paymentBill.expirationTime",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "paymentBill.totalCost")
  @Mapping(target = "orderId", source = "paymentBill.order.id")
  @Mapping(target = "carBrandModel", source = "paymentBill",
      qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "paymentBill.order.car.vin")
  @Mapping(target = "locationName", source = "paymentBill.order.location.name")
  @Mapping(target = "status", source = "paymentBill", qualifiedByName = "getPaymentBillStatus")
  @Mapping(target = "paymentDate", source = "paymentBill.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  PaymentBillResponse paymentBillToPaymentBillResponse(PaymentBill paymentBill);

  /**
   * @param paymentBill data.
   * @return UserPaymentBillsResponse DTO.
   */
  @Mapping(target = "id", source = "paymentBill.id")
  @Mapping(target = "sentDate", source = "paymentBill.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "paymentBill.totalCost")
  @Mapping(target = "orderId", source = "paymentBill.order.id")
  @Mapping(target = "carBrandModel", source = "paymentBill",
      qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "paymentBill.order.car.vin")
  @Mapping(target = "locationName", source = "paymentBill.order.location.name")
  @Mapping(target = "paymentDate", source = "paymentBill.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  UserPaymentBillsResponse paymentBillToUserPaymentBillsResponse(PaymentBill paymentBill);

  /**
   * @param paymentBill data.
   * @return UserNewPaymentBillsResponse DTO.
   */
  @Mapping(target = "id", source = "paymentBill.id")
  @Mapping(target = "sentDate", source = "paymentBill.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "expirationTime", source = "paymentBill.expirationTime",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "paymentBill.totalCost")
  @Mapping(target = "orderId", source = "paymentBill.order.id")
  @Mapping(target = "carBrandModel", source = "paymentBill",
      qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "paymentBill.order.car.vin")
  @Mapping(target = "locationName", source = "paymentBill.order.location.name")
  UserNewPaymentBillsResponse paymentBillToUserNewPaymentBillsResponse(PaymentBill paymentBill);

  /**
   * @param paymentBill data.
   * @return payment bill car brand model as String.
   */
  @Named("formatCarBrandModel")
  default String formatCarBrandModel(PaymentBill paymentBill) {
    return String.format("%s %s", paymentBill.getOrder().getCar().getModel().getBrand().getName(),
        paymentBill.getOrder().getCar().getModel().getName());
  }

  /**
   * @param paymentBill data.
   * @return payment bill status as Boolean.
   */
  @Named("getPaymentBillStatus")
  default boolean getPaymentBillStatus(PaymentBill paymentBill) {
    return !paymentBill.getExpirationTime().isBefore(LocalDateTime.now());
  }
}
