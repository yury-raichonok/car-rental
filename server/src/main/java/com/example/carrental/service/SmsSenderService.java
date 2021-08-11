package com.example.carrental.service;

import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public interface SmsSenderService {

  String sendSms(PhoneNumber to, PhoneNumber from, String token);
}
