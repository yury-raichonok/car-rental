package com.example.carrental.service.impl;

import com.example.carrental.service.SmsSenderService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwilioSmsSenderServiceImpl implements SmsSenderService {

  @Override
  public void sendSms(PhoneNumber to, PhoneNumber from, String token) {
    MessageCreator creator = Message.creator(to, from, token);
    creator.create();
  }
}
