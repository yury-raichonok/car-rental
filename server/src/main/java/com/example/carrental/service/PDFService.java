package com.example.carrental.service;

import com.example.carrental.entity.order.Order;
import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
public interface PDFService {

  ByteArrayResource exportOrderToPDF(Order order)
      throws FontNotFoundException, DocumentNotGeneratedException;
}