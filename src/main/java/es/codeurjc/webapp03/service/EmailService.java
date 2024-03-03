package es.codeurjc.webapp03.service;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import es.codeurjc.webapp03.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Mustache.Compiler mustache;

    @Autowired
    private UserRepository userRepository;

    public void sendEmail(String to, String subject, String text) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(to);
        helper.setSubject(subject);

        String htmlContent = loadHtmlContent(to);
        helper.setText(htmlContent, true); // true = text is html

        javaMailSender.send(message);
    }

    private String loadHtmlContent(String to) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/templates/mailPasswordTemplate.html");
        String htmlContent = Files.readString(Paths.get(resource.getURI()));

        // get username
        String username = userRepository.findByEmail(to).getUsername();

        Map<String, String> values = new HashMap<>();
        values.put("username", username);

        Template template = mustache.compile(htmlContent);
        return template.execute(values);

    }
}