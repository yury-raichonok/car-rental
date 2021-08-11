package com.example.carrental.service.impl;

import static com.example.carrental.mapper.OrderMapper.ORDER_DATE_FORMAT_PATTERN;
import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_MIDDLE;
import static com.itextpdf.text.Element.ALIGN_RIGHT;

import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.rentalDetails.RentalDetails;
import com.example.carrental.service.PDFService;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {

  public ByteArrayResource exportOrderPDF(Order order, RentalDetails rentalDetails)
      throws FontNotFoundException {
    var document = new Document();
    var baos = new ByteArrayOutputStream();
    try {
      PdfWriter.getInstance(document, baos);
      document.open();

      String fontFilePath = "./src/main/resources/fonts/arial.ttf";
      BaseFont bf;
      try {
        bf = BaseFont.createFont(fontFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      } catch (IOException e) {
        log.error("Font Arial on path \"{}\" not found", fontFilePath);
        throw new FontNotFoundException(String.format("Font Arial on path \"%s\" not found",
            fontFilePath));
      }

      var titleFont = new Font(bf, 20);
      var textFont = new Font(bf, 14);
      var tableHeaderFont = new Font(bf, 14, Font.NORMAL, BaseColor.WHITE);

      var titleParagraph = new Paragraph("CarRental", titleFont);
      titleParagraph.setAlignment(ALIGN_RIGHT);
      document.add(titleParagraph);

      var rentalAddressParagraph = new Paragraph(String.format("Address: %s",
          rentalDetails.getLocation().getName()), textFont);
      rentalAddressParagraph.setAlignment(ALIGN_RIGHT);
      document.add(rentalAddressParagraph);

      var rentalEmailParagraph = new Paragraph(String.format("Email: %s",
          rentalDetails.getEmail()), textFont);
      rentalEmailParagraph.setAlignment(ALIGN_RIGHT);
      document.add(rentalEmailParagraph);

      var rentalPhoneParagraph = new Paragraph(String.format("Phone: %s",
          rentalDetails.getPhone()), textFont);
      rentalPhoneParagraph.setAlignment(ALIGN_RIGHT);
      document.add(rentalPhoneParagraph);
      document.add(Chunk.NEWLINE);

      var orderNumberParagraph = new Paragraph(String.format("Order №: %d", order.getId()),
          textFont);
      orderNumberParagraph.setAlignment(ALIGN_RIGHT);
      document.add(orderNumberParagraph);

      var dateParagraph = new Paragraph(String.format("Date: %s", LocalDateTime.now().format(
          DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT_PATTERN))), textFont);
      dateParagraph.setAlignment(ALIGN_RIGHT);
      document.add(dateParagraph);

      var placeParagraph = new Paragraph(order.getLocation().getName(), textFont);
      placeParagraph.setAlignment(ALIGN_RIGHT);
      document.add(placeParagraph);

      var customerParagraph = new Paragraph(String.format("Customer: %s %s",
          order.getUser().getPassport().getFirstName(),
          order.getUser().getPassport().getLastName()),
          textFont);
      customerParagraph.setAlignment(ALIGN_RIGHT);
      document.add(customerParagraph);

      var customerEmailParagraph = new Paragraph(String.format("Customer email: %s",
          order.getUser().getEmail()), textFont);
      customerEmailParagraph.setAlignment(ALIGN_RIGHT);
      document.add(customerEmailParagraph);
      document.add(Chunk.NEWLINE);

      var orderInfoTable = new PdfPTable(5);
      orderInfoTable.setWidthPercentage(100);

      var carCell = new PdfPCell(new Paragraph("Car", tableHeaderFont));
      carCell.setBackgroundColor(new BaseColor(234, 92, 82));
      carCell.setBorderColor(new BaseColor(234, 92, 82));
      carCell.setPadding(10);
      carCell.setHorizontalAlignment(ALIGN_CENTER);
      carCell.setVerticalAlignment(ALIGN_MIDDLE);

      var carVinCell = new PdfPCell(new Paragraph("Car VIN", tableHeaderFont));
      carVinCell.setBackgroundColor(new BaseColor(234, 92, 82));
      carVinCell.setBorderColor(new BaseColor(234, 92, 82));
      carVinCell.setPadding(10);
      carVinCell.setHorizontalAlignment(ALIGN_CENTER);
      carVinCell.setVerticalAlignment(ALIGN_MIDDLE);

      var pickUpDateCell = new PdfPCell(new Paragraph("Pick-up date", tableHeaderFont));
      pickUpDateCell.setBackgroundColor(new BaseColor(234, 92, 82));
      pickUpDateCell.setBorderColor(new BaseColor(234, 92, 82));
      pickUpDateCell.setPadding(10);
      pickUpDateCell.setHorizontalAlignment(ALIGN_CENTER);
      pickUpDateCell.setVerticalAlignment(ALIGN_MIDDLE);

      var returnUpDateCell = new PdfPCell(new Paragraph("Return date date", tableHeaderFont));
      returnUpDateCell.setBackgroundColor(new BaseColor(234, 92, 82));
      returnUpDateCell.setBorderColor(new BaseColor(234, 92, 82));
      returnUpDateCell.setPadding(10);
      returnUpDateCell.setHorizontalAlignment(ALIGN_CENTER);
      returnUpDateCell.setVerticalAlignment(ALIGN_MIDDLE);

      var totalHoursCell = new PdfPCell(new Paragraph("Total hours", tableHeaderFont));
      totalHoursCell.setBackgroundColor(new BaseColor(234, 92, 82));
      totalHoursCell.setBorderColor(new BaseColor(234, 92, 82));
      totalHoursCell.setPadding(10);
      totalHoursCell.setHorizontalAlignment(ALIGN_CENTER);
      totalHoursCell.setVerticalAlignment(ALIGN_MIDDLE);

      var carValueCell = new PdfPCell(
          new Paragraph(String.format("%s %s", order.getCar().getModel().getBrand().getName(),
              order.getCar().getModel().getName())));
      carValueCell.setBorderColor(new BaseColor(234, 92, 82));
      carValueCell.setPadding(10);
      carValueCell.setHorizontalAlignment(ALIGN_CENTER);
      carValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var carVinValueCell = new PdfPCell(new Paragraph(order.getCar().getVin()));
      carVinValueCell.setBorderColor(new BaseColor(234, 92, 82));
      carVinValueCell.setPadding(10);
      carVinValueCell.setHorizontalAlignment(ALIGN_CENTER);
      carVinValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var pickUpDateValueCell = new PdfPCell(new Paragraph(
          order.getPickUpDate().format(DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT_PATTERN))));
      pickUpDateValueCell.setBorderColor(new BaseColor(234, 92, 82));
      pickUpDateValueCell.setPadding(10);
      pickUpDateValueCell.setHorizontalAlignment(ALIGN_CENTER);
      pickUpDateValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var returnUpDateValueCell = new PdfPCell(new Paragraph(
          order.getReturnDate().format(DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT_PATTERN))));
      returnUpDateValueCell.setBorderColor(new BaseColor(234, 92, 82));
      returnUpDateValueCell.setPadding(10);
      returnUpDateValueCell.setHorizontalAlignment(ALIGN_CENTER);
      returnUpDateValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var totalHoursValueCell = new PdfPCell(new Paragraph(String
          .valueOf(Duration.between(order.getPickUpDate(), order.getReturnDate()).toHours())));
      totalHoursValueCell.setBorderColor(new BaseColor(234, 92, 82));
      totalHoursValueCell.setPadding(10);
      totalHoursValueCell.setHorizontalAlignment(ALIGN_CENTER);
      totalHoursValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      orderInfoTable.addCell(carCell);
      orderInfoTable.addCell(carVinCell);
      orderInfoTable.addCell(pickUpDateCell);
      orderInfoTable.addCell(returnUpDateCell);
      orderInfoTable.addCell(totalHoursCell);
      orderInfoTable.addCell(carValueCell);
      orderInfoTable.addCell(carVinValueCell);
      orderInfoTable.addCell(pickUpDateValueCell);
      orderInfoTable.addCell(returnUpDateValueCell);
      orderInfoTable.addCell(totalHoursValueCell);
      document.add(orderInfoTable);
      document.add(Chunk.NEWLINE);

      var totalCostParagraph = new Paragraph(String.format("Total cost: %s BYN",
          order.getTotalCost().doubleValue()), textFont);
      totalCostParagraph.setAlignment(ALIGN_RIGHT);
      document.add(totalCostParagraph);

      document.close();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    try {
      baos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ByteArrayResource(baos.toByteArray());
  }
}
