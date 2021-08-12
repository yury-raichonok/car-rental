package com.example.carrental.mapper;

import static com.example.carrental.mapper.PaymentBillMapper.BILL_DATE_FORMAT_PATTERN;

import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.entity.bill.RepairBill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface RepairBillMapper {

  String REPAIR_BILL_DATE_FORMAT_PATTERN = "dd.MM.yyyy";

  @Mapping(target = "id", source = "repairBill.id")
  @Mapping(target = "userEmail", source = "repairBill.order.user.email")
  @Mapping(target = "sentDate", source = "repairBill.sentDate",
      dateFormat = REPAIR_BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "repairBill.totalCost")
  @Mapping(target = "message", source = "repairBill.message")
  @Mapping(target = "orderId", source = "repairBill.order.id")
  @Mapping(target = "carBrandModel", source = "repairBill",
      qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "repairBill.order.car.vin")
  @Mapping(target = "locationName", source = "repairBill.order.location.name")
  @Mapping(target = "paymentDate", source = "repairBill.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, dateFormat = BILL_DATE_FORMAT_PATTERN)
  RepairBillResponse repairBillToRepairBillResponse(RepairBill repairBill);

  @Mapping(target = "id", source = "repairBill.id")
  @Mapping(target = "sentDate", source = "repairBill.sentDate",
      dateFormat = REPAIR_BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "repairBill.totalCost")
  @Mapping(target = "message", source = "repairBill.message")
  @Mapping(target = "orderId", source = "repairBill.order.id")
  @Mapping(target = "carBrandModel", source = "repairBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "repairBill.order.car.vin")
  @Mapping(target = "locationName", source = "repairBill.order.location.name")
  RepairBillNewResponse repairBillToRepairBillNewResponse(RepairBill repairBill);

  @Mapping(target = "id", source = "repairBill.id")
  @Mapping(target = "sentDate", source = "repairBill.sentDate",
      dateFormat = REPAIR_BILL_DATE_FORMAT_PATTERN)
  @Mapping(target = "totalCost", source = "repairBill.totalCost")
  @Mapping(target = "message", source = "repairBill.message")
  @Mapping(target = "orderId", source = "repairBill.order.id")
  @Mapping(target = "carBrandModel", source = "repairBill", qualifiedByName = "formatCarBrandModel")
  @Mapping(target = "carVin", source = "repairBill.order.car.vin")
  @Mapping(target = "locationName", source = "repairBill.order.location.name")
  @Mapping(target = "paymentDate", source = "repairBill.paymentDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, dateFormat = BILL_DATE_FORMAT_PATTERN)
  RepairBillHistoryResponse repairBillToRepairBillHistoryResponse(RepairBill repairBill);

  @Named("formatCarBrandModel")
  default String formatCarBrandModel(RepairBill repairBill) {
    return String.format("%s %s", repairBill.getOrder().getCar().getModel().getBrand().getName(),
        repairBill.getOrder().getCar().getModel().getName());
  }
}
