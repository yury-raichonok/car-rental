package com.example.carrental.service.impl;

import com.example.carrental.config.TwilioConfig;
import com.example.carrental.service.SmsSenderService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The service for sending SMS with Twilio.
 *
 * @author Yury Raichonak
 */
@Service
@RequiredArgsConstructor
public class TwilioSmsSenderServiceImpl implements SmsSenderService {

  private final TwilioConfig twilioConfig;

  /**
   * @param phoneNumber of receiver.
   * @param token for phone confirmation.
   */
  @Override
  public void sendSms(PhoneNumber phoneNumber, String token) {
    var creator = Message
        .creator(phoneNumber, new PhoneNumber(twilioConfig.getTrialNumber()), token);
    creator.create();
  }
}
