package com.example.carrental.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

  String sendEmail(String to, String body, String topic);
}
