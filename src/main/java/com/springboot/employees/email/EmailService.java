package com.springboot.employees.email;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Service
@EnableAsync
@Slf4j
public class EmailService {

	@Value("${spring.mail.username}")
	private String fromEmail;

	private String personal_message = "SATHISH CH";

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;

	/**
	 * Creates the templete.
	 *
	 * @param htmlFile the html file
	 * @param model    the model
	 * @return the string
	 */
	private String createTemplete(String htmlFile, Object model) {
		String html = "";
		try {
			Optional<String> value = Optional.of(htmlFile);
			if (value.isPresent()) {
				Template t = config.getTemplate(htmlFile);
				html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			}
		} catch (IOException | TemplateException e) {
			log.error(e.getMessage());
		}
		return html;
	}

	/**
	 * Send email.
	 *
	 * @param model      the model
	 * @param toMail     the to mail
	 * @param htmlFile   the html file
	 * @param strSubject the str subject
	 */
	public void sendEmail(Map<String, Object> model, String toMail, String htmlFile, String strSubject) {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			System.out.println(" in send mail :" + strSubject);
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			helper.setTo(toMail);
			helper.setText(createTemplete(htmlFile, model), true);
			helper.setSubject(strSubject);
			helper.setFrom(fromEmail, personal_message);
			mailSender.send(message);

		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Send email welcome.
	 *
	 * @param model  the model
	 * @param toMail the to mail
	 */
	@Async
	public void sendEmailWelcome(Map<String, Object> model, String toMail) {
		sendEmail(model, toMail, "welcome.html", "Welcome to Employee - sathish");
	}
}
