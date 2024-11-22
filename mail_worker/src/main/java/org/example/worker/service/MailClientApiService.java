package org.example.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailClientApiService {

    private final JavaMailSender mailSender;

    public boolean sendMail(String destinationEmail, String message) {

        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(destinationEmail);
            mailMessage.setSubject("Automated Message");
            mailMessage.setText(message);

            mailSender.send(mailMessage);

            log.info("Email sent successfully to {}", destinationEmail);

            return true;

        } catch (Exception e) {

            log.error("Failed to send email to {}: {}", destinationEmail, e.getMessage());

            return false;
        }
    }
}
