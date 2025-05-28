package vn.fu_ohayo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {
    JavaMailSender mailSender;
    UserRepository userRepository;
    JwtService jwtService;
    public void sendEmail(String toEmail, String token) {
        try {
            String htmlContent = loadHtmlTemplate();
            String link = "http://localhost:8080/mail/mail-confirm?token=" + token;
            htmlContent = htmlContent.replace("{{TOKEN_LINK}}", link);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🔒 Xác nhận đăng ký tài khoản");
            helper.setText(htmlContent, true); // true = nội dung là HTML

            mailSender.send(message);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);

        }
    }
    public void sendEmailAgain(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));
        String token = jwtService.generateAccessToken(user.getUserId(), email, null);
        sendEmail(email,token);
    }

        private String loadHtmlTemplate() throws IOException {
            ClassPathResource resource = new ClassPathResource("templates/verification-email.html");
            return new String(Files.readAllBytes(resource.getFile().toPath()));
        }
    }
