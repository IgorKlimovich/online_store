package org.academy.OnlineStoreDemo.mail;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
    void sendWelcomeMessage(String to, String firstName);
    void sendDeliverMessage (String to, String firstName);
}
