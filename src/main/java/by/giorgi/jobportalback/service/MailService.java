package by.giorgi.jobportalback.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@AllArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

//    @Value("${spring.mail.username}")
//    private String fromEmail;

    public void sendVerificationMail(String to,String token)  {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom("patashuri2003@gmail.com");
            helper.setTo(to);
            helper.setSubject("Verify Your Email Address");
            String verificationLink = getVerificationUrl(token,"email");
            System.out.println(verificationLink);
            String emailContent = getVerificationEmailTemplate(verificationLink);
            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    };
    private String getVerificationEmailTemplate(String verificationLink) {
        return """
            <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #333;">Verify Your Email Address</h2>
                <p>Thank you for registering! Please click the button below to verify your email address:</p>
                <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" 
                       style="background-color: #4F46E5; color: white; padding: 12px 24px; 
                              text-decoration: none; border-radius: 5px; display: inline-block;">
                        Verify Email
                    </a>
                </div>
                <p style="color: #666; font-size: 14px;">
                    If you didn't create an account, you can safely ignore this email.
                </p>
                <p style="color: #666; font-size: 14px;">
                    This link will expire in 24 hours.
                </p>
            </div>
            """.formatted(verificationLink);
    }
    private String getVerificationUrl(String key,String type) throws MessagingException {
        return fromCurrentContextPath().path("/auth/verify/" + key).toUriString();
    }
}
