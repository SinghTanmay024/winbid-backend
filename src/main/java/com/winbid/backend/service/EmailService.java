package com.winbid.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;


    @Autowired  // Constructor injection is preferred
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendThankYouEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Thank You for Registering with WinBid!");
        message.setText("Dear " + username + ",\n\n" +
                "Thank you for registering with WinBid! We're excited to have you on board.\n\n" +
                "Start exploring our auctions and happy bidding!\n\n" +
                "Best regards,\n" +
                "The WinBid Team");

        mailSender.send(message);
    }
}
