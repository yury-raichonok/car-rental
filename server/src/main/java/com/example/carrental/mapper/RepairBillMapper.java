package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;
import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;

import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.entity.bill.RepairBill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface for mapping Repair Bill entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface RepairBillMapper {

  /**
   * @param repairBill data.
   * @return RepairBillResponse DTO.
   */
  @Mapping(target = "id", source = "repairBill.id")
  @Mapping(target = "userEmail", source = "repairBill.order.user.email")
  @Mapping(target = "sentDate", source = "repairBill.sentDate",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "repairBill.totalCost")
  @Mapping(target = "message", source = "repairBill.message")
  @Mapping(target = "orderId", source = "repairBill.order.id")
  @Mapping(target = "carBrandModel", source = "repairBill",
      qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "repairBill.order.car.vin")
  @Mapping(target = "locationName", source = "repairBill.order.location.name")
  @Mapping(target = "paymentDate", source = "repairBill.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  RepairBillResponse repairBillToRepairBillResponse(RepairBill repairBill);

  /**
   * @param repairBill data.
   * @return RepairBillNewResponse DTO.
   */
  @Mapping(target = "id", source = "repairBill.id")
  @Mapping(target = "sentDate", source = "repairBill.sentDate",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "repairBill.totalCost")
  @Mapping(target = "message", source = "repairBill.message")
  @Mapping(target = "orderId", source = "repairBill.order.id")
  @Mapping(target = "carBrandModel", source = "repairBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "repairBill.order.car.vin")
  @Mapping(target = "locationName", source = "repairBill.order.location.name")
  RepairBillNewResponse repairBillToRepairBillNewResponse(RepairBill repairBill);

  /**
   * @param repairBill data.
   * @return RepairBillHistoryResponse DTO.
   */
  @Mapping(target = "id", source = "repairBill.id")
  @Mapping(target = "sentDate", source = "repairBill.sentDate",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "repairBill.totalCost")
  @Mapping(target = "message", source = "repairBill.message")
  @Mapping(target = "orderId", source = "repairBill.order.id")
  @Mapping(target = "carBrandModel", source = "repairBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "repairBill.order.car.vin")
  @Mapping(target = "locationName", source = "repairBill.order.location.name")
  @Mapping(target = "paymentDate", source = "repairBill.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  RepairBillHistoryResponse repairBillToRepairBillHistoryResponse(RepairBill repairBill);

  /**
   * @param repairBill data.
   * @return repair bill order car brand model as String.
   */
  @Named("formatCarBrandModel")
  default String formatCarBrandModel(RepairBill repairBill) {
    return String.format("%s %s", repairBill.getOrder().getCar().getModel().getBrand().getName(),
        repairBill.getOrder().getCar().getModel().getName());
  }
}
