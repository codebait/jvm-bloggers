package pl.tomaszdziurko.jvm_bloggers.mailing;


import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class SendGridMailSender {

    public static final String FROM_NAME = "JVM Bloggers";

    private final SendGrid sendgrid;
    private final String senderAddress;

    @Autowired
    public SendGridMailSender(SendGrid sendgrid, @Value("${sendgrid.fromEmail}") String senderAddress) {
        this.sendgrid = sendgrid;
        this.senderAddress = senderAddress;
    }

    public void sendEmail(String recipientAddress, String subject, String htmlContent) {
        SendGrid.Email email = prepareEmail(recipientAddress, subject, htmlContent);
        try {
            SendGrid.Response response = sendgrid.send(email);
            log.info("Sending mail '{}' to {}: " +  response.getMessage(), email.getSubject(), Arrays.toString(email.getTos()));
        } catch (SendGridException e) {
            log.error("Error when sending email to  " + Arrays.toString(email.getTos()) + ", msg = " + e.getMessage(), e);
        }
    }

    private SendGrid.Email prepareEmail(String recipientAddress, String subject, String htmlContent) {
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(recipientAddress);
        email.setFrom(senderAddress);
        email.setFromName(FROM_NAME);
        email.setSubject(subject);
        email.setHtml(htmlContent);
        return email;
    }
}
