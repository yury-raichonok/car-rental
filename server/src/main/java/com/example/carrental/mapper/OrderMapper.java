package com.example.carrental.mapper;

import com.example.carrental.controller.dto.bill.CreatePaymentBillRequest;
import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.UserOrderResponse;
import com.example.carrental.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  String ORDER_DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "userEmail", source = "order.user.email")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "paymentStatus", source = "order.paymentStatus.status")
  @Mapping(target = "comments", source = "order.comments")
  @Mapping(target = "sentDate", source = "order.sentDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "rentalStatus", source = "order.rentalStatus.status")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  @Mapping(target = "paymentDate", source = "order.paymentDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "denyingDate", source = "order.denyingDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "paymentBillId", source = "order.paymentBill.id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "repairBillId", source = "order.repairBill.id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  OrderResponse orderToOrderResponse(Order order);

  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "userId", source = "order.user.id")
  @Mapping(target = "userEmail", source = "order.user.email")
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "sentDate", source = "order.sentDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  OrderNewResponse orderToOrderNewResponse(Order order);

  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "userId", source = "order.user.id")
  @Mapping(target = "userEmail", source = "order.user.email")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "paymentStatus", source = "order.paymentStatus.status")
  @Mapping(target = "sentDate", source = "order.sentDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "paymentDate", source = "order.paymentDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "paymentBillId", source = "order.paymentBill.id")
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  OrderInformationResponse orderToOrderInformationResponse(Order order);

  @Mapping(target = "orderId", source = "order.id")
  CreatePaymentBillRequest orderToCreatePaymentBillRequest(Order order);

  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "sentDate", source = "order.sentDate", dateFormat = ORDER_DATE_FORMAT_PATTERN)
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  UserOrderResponse orderToNewUserOrderResponse(Order order);

  @Named("formatCarBrandModel")
  default String formatCarBrandModel(Order order) {
    return String.format("%s %s", order.getCar().getModel().getBrand().getName(),
        order.getCar().getModel().getName());
  }
}
