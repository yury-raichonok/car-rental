package com.example.carrental.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

  void sendEmail(String to, String body, String topic);
}
