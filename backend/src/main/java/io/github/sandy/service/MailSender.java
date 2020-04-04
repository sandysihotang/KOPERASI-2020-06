package io.github.sandy.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

public class MailSender {

    public void run(JavaMailSender javaMailSender){
        System.out.println("Send email .....");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("sandysihotang868@gmail.com");
        msg.setSubject("Confirm Register");
        msg.setText("Your email have been registered!");
        javaMailSender.send(msg);

    }
}
