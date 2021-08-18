package com.example.carrental.service;

import org.springframework.stereotype.Service;

/**
 * The service for Emails.
 * <p>
 * This interface describes actions on Emails.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface EmailService {

  void sendEmail(String to, String body, String topic);
}
