package org.example.confidentialite.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    @Retryable(
            retryFor = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    public void sendEngagementEmailAsync(byte[] fileBytes, String filename, String subject) throws MessagingException {
        log.info("Attempting to send email for file: {}", filename);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("adamoulehiane2@gmail.com");
        helper.setTo("oulehiane.adam@ensi.ma");
        helper.setSubject(subject);
        helper.setText("Veuillez trouver le document ci-joint.");

        helper.addAttachment(
                filename != null ? filename : "Engagement.pdf",
                new ByteArrayResource(fileBytes)
        );

        mailSender.send(message);
        log.info("Email sent successfully for file: {}", filename);
    }

    @Recover
    public void recoverFromEmailFailure(Throwable t, byte[] fileBytes, String filename, String subject) {
        log.error("CRITICAL: All 3 attempts to send email for file '{}' failed. Error: {}", filename, t.getMessage());

    }
}