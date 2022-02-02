package org.academy.OnlineStoreDemo.mail;

public interface EmailService {

    void sendWelcomeMessage(String to, String firstName);
    void sendDeliverMessage (String to, String firstName);
}
