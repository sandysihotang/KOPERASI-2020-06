package io.github.sandy.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

public class MailSender {

    public void run(JavaMailSender javaMailSender){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("sandysihotang868@gmail.com");
        msg.setSubject("Confirm Register");
        msg.setText("Your email have been registered!");
        javaMailSender.send(msg);

    }
    public void sendEmailEnableAccount(JavaMailSender javaMailSender, String email){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Account telah diaktifkan");
        msg.setText("Acoount anda telah diaktifkan oleh admin <br> anda sudah dapat login kesistem");
        javaMailSender.send(msg);
    }
    public void sendEmailSetStateKoperasi(JavaMailSender javaMailSender, String email, String message){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Account Koperasi");
        msg.setText(message);
        javaMailSender.send(msg);
    }

    public void sendEmailNonActiveAccountMember(JavaMailSender javaMailSender, String email, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Account Koperasi");
        msg.setText(message);
        javaMailSender.send(msg);
    }
}
