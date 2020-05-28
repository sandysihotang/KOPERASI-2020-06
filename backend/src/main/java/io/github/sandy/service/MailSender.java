package io.github.sandy.service;

import io.github.sandy.gdrive.GmailQuickStart;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class MailSender {

    public void run(JavaMailSender javaMailSender){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("sandysihotang868@gmail.com");
        msg.setSubject("Confirm Register");
        msg.setText("Your email have been registered!");
        javaMailSender.send(msg);

    }
    public void sendEmailEnableAccount(String email) throws GeneralSecurityException, MessagingException, IOException {
        GmailQuickStart gmailQuickStart = new GmailQuickStart();
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addFrom(new InternetAddress[]{new InternetAddress("TobaKo")});
        mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
        mimeMessage.setSubject("Account telah diaktifkan");
        mimeMessage.setText("Acoount anda telah diaktifkan oleh admin \n anda sudah dapat login kesistem");
        gmailQuickStart.sendMessage("me", mimeMessage);
    }
    public void sendEmailSetStateKoperasi(String email, String text,String message) throws GeneralSecurityException, MessagingException, IOException {
        GmailQuickStart gmailQuickStart = new GmailQuickStart();
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addFrom(new InternetAddress[]{new InternetAddress("TobaKo")});
        mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
        mimeMessage.setSubject("Account Koperasi");
        mimeMessage.setText(message + "\n\n" + text);
        gmailQuickStart.sendMessage("me", mimeMessage);
    }

    public void sendEmailNonActiveAccountMember(String email, String message) throws GeneralSecurityException, MessagingException, IOException {
        GmailQuickStart gmailQuickStart = new GmailQuickStart();
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addFrom(new InternetAddress[]{new InternetAddress("TobaKo")});
        mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
        mimeMessage.setSubject("Account Koperasi");
        mimeMessage.setText(message);
        gmailQuickStart.sendMessage("me", mimeMessage);
    }
}
