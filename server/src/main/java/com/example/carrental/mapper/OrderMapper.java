package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;

import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.UserOrderResponse;
import com.example.carrental.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface for mapping Order entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

  /**
   * @param order data.
   * @return OrderResponse DTO.
   */
  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "userEmail", source = "order.user.email")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "paymentStatus", source = "order.paymentStatus.status")
  @Mapping(target = "comments", source = "order.comments")
  @Mapping(target = "sentDate", source = "order.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "rentalStatus", source = "order.rentalStatus.status")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  @Mapping(target = "paymentDate", source = "order.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "denyingDate", source = "order.denyingDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "paymentBillId", source = "order.paymentBill.id",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "repairBillId", source = "order.repairBill.id",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  OrderResponse orderToOrderResponse(Order order);

  /**
   * @param order data.
   * @return OrderNewResponse DTO.
   */
  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "userId", source = "order.user.id")
  @Mapping(target = "userEmail", source = "order.user.email")
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "sentDate", source = "order.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  OrderNewResponse orderToOrderNewResponse(Order order);

  /**
   * @param order data.
   * @return OrderInformationResponse DTO.
   */
  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "userId", source = "order.user.id")
  @Mapping(target = "userEmail", source = "order.user.email")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "paymentStatus", source = "order.paymentStatus.status")
  @Mapping(target = "sentDate", source = "order.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "paymentDate", source = "order.paymentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "paymentBillId", source = "order.paymentBill.id")
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  OrderInformationResponse orderToOrderInformationResponse(Order order);

  /**
   * @param order data.
   * @return UserOrderResponse DTO.
   */
  @Mapping(target = "id", source = "order.id")
  @Mapping(target = "pickUpDate", source = "order.pickUpDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "returnDate", source = "order.returnDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "order.totalCost")
  @Mapping(target = "sentDate", source = "order.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "carBrandModel", source = "order", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "order.car.vin")
  @Mapping(target = "locationName", source = "order.location.name")
  UserOrderResponse orderToNewUserOrderResponse(Order order);

  /**
   * @param order data.
   * @return order car brand model as String.
   */
  @Named("formatCarBrandModel")
  default String formatCarBrandModel(Order order) {
    return String.format("%s %s", order.getCar().getModel().getBrand().getName(),
        order.getCar().getModel().getName());
  }
}
