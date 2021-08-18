package com.example.carrental.service;

import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

/**
 * The service for SMS sending.
 * <p>
 * This interface describes actions on SMS.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface SmsSenderService {

  void sendSms(PhoneNumber phoneNumber, String token);
}
