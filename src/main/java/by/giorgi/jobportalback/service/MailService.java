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

    public void sendVerificationMail(String to,String token,String userType)  {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom("patashuri2003@gmail.com");
            helper.setTo(to);
            helper.setSubject("დაადასტურეთ თქვენი ელექტრონული ფოსტა");
            String verificationLink = getVerificationUrl(token,userType);
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
            <h2 style="color: #333;">დაადასტურეთ თქვენი ელ.ფოსტა</h2>
            <p>გმადლობთ რეგისტრაციისთვის! გთხოვთ დააჭიროთ ქვემოთ მოცემულ ღილაკს თქვენი ელ.ფოსტის დასადასტურებლად:</p>
            <div style="text-align: center; margin: 30px 0;">
                <a href="%s" 
                   style="background-color: #4CAF50; color: white; padding: 12px 24px; 
                          text-decoration: none; border-radius: 5px; display: inline-block;">
                    ელ.ფოსტის დადასტურება
                </a>
            </div>
            <p style="color: #666; font-size: 14px;">
                თუ თქვენ არ შექმენით ანგარიში, შეგიძლიათ უგულებელყოთ ეს ელ.ფოსტა.
            </p>
            <p style="color: #666; font-size: 14px;">
                ეს ბმული გაუქმდება 24 საათში.
            </p>
        </div>
        """.formatted(verificationLink);
    }
    private String getVerificationUrl(String key,String userType) throws MessagingException {
        return fromCurrentContextPath().path("/auth/verify/" + key).queryParam("userType",userType).toUriString();
    }
}
