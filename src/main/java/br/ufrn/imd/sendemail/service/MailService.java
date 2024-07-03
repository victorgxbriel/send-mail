package br.ufrn.imd.sendemail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void senEmailWithAttachment(String to, String subject, String text, File file) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@email.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            
            javaMailSender.send(message);
        } catch (MessagingException messagingException) {
            System.out.println(messagingException.getMessage());
        }
    }
}
