package com.example.carrental.mapper;

import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.entity.bill.PaymentBill;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface PaymentBillMapper {

  String BILL_DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

  @Mapping(target = "id", source = "paymentBill.id")
  @Mapping(target = "userEmail", source = "paymentBill.order.user.email")
  @Mapping(target = "sentDate", source = "paymentBill.sentDate", dateFormat = BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "expirationTime", source = "paymentBill.expirationTime", dateFormat = BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "paymentBill.totalCost")
  @Mapping(target = "orderId", source = "paymentBill.order.id")
  @Mapping(target = "carBrandModel", source = "paymentBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "paymentBill.order.car.vin")
  @Mapping(target = "locationName", source = "paymentBill.order.location.name")
  @Mapping(target = "status", source = "paymentBill", qualifiedByName = "getPaymentBillStatus")
  @Mapping(target = "paymentDate", source = "paymentBill.paymentDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, dateFormat = BILL_DATE_FORMAT_PATTERN)
  PaymentBillResponse paymentBillToPaymentBillResponse(PaymentBill paymentBill);

  @Mapping(target = "id", source = "paymentBill.id")
  @Mapping(target = "sentDate", source = "paymentBill.sentDate", dateFormat = BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "paymentBill.totalCost")
  @Mapping(target = "orderId", source = "paymentBill.order.id")
  @Mapping(target = "carBrandModel", source = "paymentBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "paymentBill.order.car.vin")
  @Mapping(target = "locationName", source = "paymentBill.order.location.name")
  @Mapping(target = "paymentDate", source = "paymentBill.paymentDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, dateFormat = BILL_DATE_FORMAT_PATTERN)
  UserPaymentBillsResponse paymentBillToUserPaymentBillsResponse(PaymentBill paymentBill);

  @Mapping(target = "id", source = "paymentBill.id")
  @Mapping(target = "sentDate", source = "paymentBill.sentDate", dateFormat = BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "expirationTime", source = "paymentBill.expirationTime", dateFormat = BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "paymentBill.totalCost")
  @Mapping(target = "orderId", source = "paymentBill.order.id")
  @Mapping(target = "carBrandModel", source = "paymentBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "paymentBill.order.car.vin")
  @Mapping(target = "locationName", source = "paymentBill.order.location.name")
  UserNewPaymentBillsResponse paymentBillToUserNewPaymentBillsResponse(PaymentBill paymentBill);

  @Named("formatCarBrandModel")
  default String formatCarBrandModel(PaymentBill paymentBill) {
    return String.format("%s %s", paymentBill.getOrder().getCar().getModel().getBrand().getName(),
        paymentBill.getOrder().getCar().getModel().getName());
  }

  @Named("getPaymentBillStatus")
  default boolean getPaymentBillStatus(PaymentBill paymentBill) {
    return !paymentBill.getExpirationTime().isBefore(LocalDateTime.now());
  }
}
