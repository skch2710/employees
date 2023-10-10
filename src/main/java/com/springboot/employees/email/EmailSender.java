package com.springboot.employees.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
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
public class EmailSender {

	@Value("${spring.mail.username}")
	private String fromEmail;

	private String personalMessage = "SATHISH CH";

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;

	/**
	 * Creates the template.
	 *
	 * @param htmlFile the html file
	 * @param model    the model
	 * @return the string
	 */
	private String createTemplate(String htmlFile, Object model) {
	    try {
	        Template t = config.getTemplate(htmlFile);
	        return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
	    } catch (IOException | TemplateException e) {
	        log.error(e.getMessage());
	        return "";
	    }
	}


	/**
	 * Send email.
	 * @param model
	 * @param bos
	 */
	public void sendEmail(Map<String, Object> model,ByteArrayOutputStream bos) {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			helper.setTo(model.get("toMail").toString());
			helper.setText(createTemplate(model.get("htmlFile").toString(), model), true);
			helper.setSubject(model.get("subject").toString());
			helper.setFrom(fromEmail, personalMessage);
			// Add the embedded image signature to the email message.
			FileSystemResource rsc = new FileSystemResource("src/main/resources/images/image.png");
			helper.addInline("signature", rsc);
			if(bos!=null) {
				// Attach the file
				ByteArrayDataSource dataSource = new ByteArrayDataSource(bos.toByteArray(), model.get("type").toString());
				helper.addAttachment(model.get("fileName").toString(), dataSource);	
			}
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
	public void sendEmailWelcome(Map<String, Object> model) {
		sendEmail(model, null);
	}
	
	@Async
	public void sendEmailAttach(Map<String, Object> model,ByteArrayOutputStream bos) {
		sendEmail(model, bos);
	}
}
