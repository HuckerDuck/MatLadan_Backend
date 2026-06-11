package com.fredrik.matladan.security.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromAddress;

    public void sendVerificationOtp(String toEmail, String otp) {
        String subject = "Verifiera ditt MatLådan-konto";
        String body = """
                Hej!
                
                Välkommen till MatLådan! Din verifieringskod är:
                
                %s
                
                Koden är giltig i 15 minuter.
                
                Om du inte skapade ett konto kan du bortse från detta mail.
                
                Hälsningar,
                MatLådan
                """.formatted(otp);
        sendEmail(toEmail, subject, body);
    }

    public void sendPasswordResetOtp(String toEmail, String otp) {
        String subject = "Återställ ditt MatLådan-lösenord";
        String body = """
                Hej!
                
                Din återställningskod är:
                
                %s
                
                Koden är giltig i 15 minuter.
                
                Om du inte begärde detta kan du bortse från detta mail.
                
                Hälsningar,
                MatLådan
                """.formatted(otp);
        sendEmail(toEmail, subject, body);
    }

    public void sendHouseholdInvite(String toEmail, String inviterEmail, String householdName, String token) {
        String subject = "Du har blivit inbjuden till " + householdName + " på MatLådan";
        String body = """
            Hej!
            
            %s har bjudit in dig att dela matförråd på MatLådan.
            
            Använd den här koden för att gå med:
            
            %s
            
            Koden är giltig i 24 timmar.
            
            Hälsningar,
            MatLådan
            """.formatted(inviterEmail, token);
        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Email sent to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Could not send email. Please try again later.");
        }
    }


}