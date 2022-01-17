package org.academy.OnlineStoreDemo.mail.impl;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.mail.EmailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final SimpleMailMessage simpleMailMessage;

    public EmailServiceImpl(JavaMailSender emailSender, SimpleMailMessage simpleMailMessage) {
        this.emailSender = emailSender;
        this.simpleMailMessage = simpleMailMessage;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

        simpleMailMessage.setFrom("garic.8761@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        emailSender.send(simpleMailMessage);
        log.info("in send message: message {} sended to {}", text,to);
    }

    public void sendWelcomeMessage(String to, String firstName){
        simpleMailMessage.setFrom("garic.8761@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Welcome");
        String message = String.format("Hello %s welcome to My Shop", firstName);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
        log.info("in send welcome message: message {} send to new user {}",message,firstName);
    }

    public void sendDeliverMessage (String to, String firstName){
        simpleMailMessage.setFrom("garic.8761@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Successfull!");
        String message = String.format("Hello %s, your order has been successfully completed.",firstName);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
        log.info("in send deliver message: message {} send to new user {}",message,firstName);
    }
}
