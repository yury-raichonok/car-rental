package com.example.carrental.service.impl;

import static com.example.carrental.mapper.OrderMapper.ORDER_DATE_FORMAT_PATTERN;
import static com.itextpdf.text.BaseColor.WHITE;
import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_MIDDLE;
import static com.itextpdf.text.Element.ALIGN_RIGHT;
import static com.itextpdf.text.Font.NORMAL;
import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.rentalDetails.RentalDetails;
import com.example.carrental.service.PDFService;
import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
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

  public static final String ARIAL_FONT_PATH = "./src/main/resources/fonts/arial.ttf";
  public static final int FONT_SIZE = 14;
  public static final int TITLE_FONT_SIZE = 20;
  public static final int TEXT_PADDING = 10;
  public static final int TABLE_WIDTH_PERCENTS = 100;
  public static final int TABLE_COLUMNS_AMOUNT = 5;
  public static final BaseColor BASE_COLOR = new BaseColor(234, 92, 82);

  public ByteArrayResource exportOrderToPDF(Order order, RentalDetails rentalDetails)
      throws FontNotFoundException, DocumentNotGeneratedException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      var document = new Document();
      PdfWriter.getInstance(document, byteArrayOutputStream);
      document.open();

      var baseFont = BaseFont.createFont(ARIAL_FONT_PATH, IDENTITY_H, EMBEDDED);

      var titleFont = new Font(baseFont, TITLE_FONT_SIZE);
      var textFont = new Font(baseFont, FONT_SIZE);
      var tableHeaderFont = new Font(baseFont, FONT_SIZE, NORMAL, WHITE);

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

      var orderInfoTable = new PdfPTable(TABLE_COLUMNS_AMOUNT);
      orderInfoTable.setWidthPercentage(TABLE_WIDTH_PERCENTS);

      var carCell = new PdfPCell(new Paragraph("Car", tableHeaderFont));
      carCell.setBackgroundColor(BASE_COLOR);
      carCell.setBorderColor(BASE_COLOR);
      carCell.setPadding(TEXT_PADDING);
      carCell.setHorizontalAlignment(ALIGN_CENTER);
      carCell.setVerticalAlignment(ALIGN_MIDDLE);

      var carVinCell = new PdfPCell(new Paragraph("Car VIN", tableHeaderFont));
      carVinCell.setBackgroundColor(BASE_COLOR);
      carVinCell.setBorderColor(BASE_COLOR);
      carVinCell.setPadding(TEXT_PADDING);
      carVinCell.setHorizontalAlignment(ALIGN_CENTER);
      carVinCell.setVerticalAlignment(ALIGN_MIDDLE);

      var pickUpDateCell = new PdfPCell(new Paragraph("Pick-up date", tableHeaderFont));
      pickUpDateCell.setBackgroundColor(BASE_COLOR);
      pickUpDateCell.setBorderColor(BASE_COLOR);
      pickUpDateCell.setPadding(TEXT_PADDING);
      pickUpDateCell.setHorizontalAlignment(ALIGN_CENTER);
      pickUpDateCell.setVerticalAlignment(ALIGN_MIDDLE);

      var returnUpDateCell = new PdfPCell(new Paragraph("Return date date", tableHeaderFont));
      returnUpDateCell.setBackgroundColor(BASE_COLOR);
      returnUpDateCell.setBorderColor(BASE_COLOR);
      returnUpDateCell.setPadding(TEXT_PADDING);
      returnUpDateCell.setHorizontalAlignment(ALIGN_CENTER);
      returnUpDateCell.setVerticalAlignment(ALIGN_MIDDLE);

      var totalHoursCell = new PdfPCell(new Paragraph("Total hours", tableHeaderFont));
      totalHoursCell.setBackgroundColor(BASE_COLOR);
      totalHoursCell.setBorderColor(BASE_COLOR);
      totalHoursCell.setPadding(TEXT_PADDING);
      totalHoursCell.setHorizontalAlignment(ALIGN_CENTER);
      totalHoursCell.setVerticalAlignment(ALIGN_MIDDLE);

      var carValueCell = new PdfPCell(
          new Paragraph(String.format("%s %s", order.getCar().getModel().getBrand().getName(),
              order.getCar().getModel().getName())));
      carValueCell.setBorderColor(BASE_COLOR);
      carValueCell.setPadding(TEXT_PADDING);
      carValueCell.setHorizontalAlignment(ALIGN_CENTER);
      carValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var carVinValueCell = new PdfPCell(new Paragraph(order.getCar().getVin()));
      carVinValueCell.setBorderColor(BASE_COLOR);
      carVinValueCell.setPadding(TEXT_PADDING);
      carVinValueCell.setHorizontalAlignment(ALIGN_CENTER);
      carVinValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var pickUpDateValueCell = new PdfPCell(new Paragraph(
          order.getPickUpDate().format(DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT_PATTERN))));
      pickUpDateValueCell.setBorderColor(BASE_COLOR);
      pickUpDateValueCell.setPadding(TEXT_PADDING);
      pickUpDateValueCell.setHorizontalAlignment(ALIGN_CENTER);
      pickUpDateValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var returnUpDateValueCell = new PdfPCell(new Paragraph(
          order.getReturnDate().format(DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT_PATTERN))));
      returnUpDateValueCell.setBorderColor(BASE_COLOR);
      returnUpDateValueCell.setPadding(TEXT_PADDING);
      returnUpDateValueCell.setHorizontalAlignment(ALIGN_CENTER);
      returnUpDateValueCell.setVerticalAlignment(ALIGN_MIDDLE);

      var totalHoursValueCell = new PdfPCell(new Paragraph(String
          .valueOf(Duration.between(order.getPickUpDate(), order.getReturnDate()).toHours())));
      totalHoursValueCell.setBorderColor(BASE_COLOR);
      totalHoursValueCell.setPadding(TEXT_PADDING);
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
      return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    } catch (DocumentException e) {
      log.error("Printout for order {} not generated. Exception: {}", order.getId(),
          e.getMessage());
      throw new DocumentNotGeneratedException(String
          .format("Printout for order %s not generated. Exception: %s", order.getId(),
              e.getMessage()));
    } catch (IOException e) {
      log.error("IOException: {}", e.getMessage());
      throw new FontNotFoundException(String.format("IOException: %s", e.getMessage()));
    }
  }
}
