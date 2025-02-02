package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final long TOKEN_EXPIRATION_HOURS = 24;
    private static final long SSE_TIMEOUT = 300000L;

    public SseEmitter createEmitter(String email) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitters.put(email, emitter);
        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));
        return emitter;
    }

    public void sendVerificationEmail(User user) {
        String verificationToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set("verification" + verificationToken, user.getEmail(),TOKEN_EXPIRATION_HOURS, TimeUnit.HOURS);

        user.setVerified(false);

        userRepository.save(user);

        try{
            mailService.sendVerificationMail(user.getEmail(), verificationToken);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void verifyEmail(String verificationToken) {
        String redisKey = "verification" + verificationToken;
        String userEmail = redisTemplate.opsForValue().get(redisKey);

        if(userEmail == null){
            throw new IllegalArgumentException("Invalid or expired verification token");
        }
        User user  = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        user.setVerified(true);
        userRepository.save(user);

        redisTemplate.delete(redisKey);

    }
}
