package com.springboot.employees.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

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

	// With Attachments
	public void sendEmail(Map<String, Object> model, String toMail, String htmlFile, String strSubject,
			ByteArrayOutputStream excelStream, ByteArrayOutputStream pdfStream) {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			helper.setTo(toMail);
			helper.setText(createTemplete(htmlFile, model), true);
			helper.setSubject(strSubject);
			helper.setFrom(fromEmail, personal_message);

			// Attach the Excel file
			ByteArrayDataSource excelDataSource = new ByteArrayDataSource(excelStream.toByteArray(),
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			helper.addAttachment("excel_attachment.xlsx", excelDataSource);

			// Attach the PDF file
			ByteArrayDataSource pdfDataSource = new ByteArrayDataSource(pdfStream.toByteArray(), "application/pdf");
			helper.addAttachment("pdf_attachment.pdf", pdfDataSource);

			mailSender.send(message);

		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}
	
	public void sendEmail(Map<String, Object> model, String toMail, String htmlFile, String strSubject,
		ByteArrayOutputStream imageStream) {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			helper.setTo(toMail);
			helper.setText(createTemplete(htmlFile, model), true);
			helper.setSubject(strSubject);
			helper.setFrom(fromEmail, personal_message);

			// Attach the image file
			ByteArrayDataSource imageDataSource = new ByteArrayDataSource(imageStream.toByteArray(), "image/jpeg"); 
			// Modify the MIME type accordingly (e.g., "image/jpeg" for JPEG files , for PNG "image/png" )
			helper.addAttachment("image_attachment.jpeg", imageDataSource);

			mailSender.send(message);

		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * method for attachments.
	 * 
	 * @param model
	 * @param toMail
	 * @param htmlFile
	 * @param strSubject
	 * @param bos
	 */
	public void sendEmailAttachment(Map<String, Object> model, String htmlFile, String strSubject,
			ByteArrayOutputStream bos) {

			MimeMessage message = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
						StandardCharsets.UTF_8.name());

				helper.setTo(model.get("toMail").toString());
				helper.setText(createTemplete(htmlFile, model), true);
				helper.setSubject(strSubject);
				helper.setFrom(fromEmail, personal_message);

				// Attach the file
				ByteArrayDataSource dataSource = new ByteArrayDataSource(bos.toByteArray(), model.get("type").toString()); 
				helper.addAttachment(model.get("fileName").toString(), dataSource);

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
	
	/**
	 * Send email with attachment.
	 *
	 * @param model  the model
	 * @param ByteArrayOutputStream excelStream
	 * @param ByteArrayOutputStream pdfStream
	 * @param toMail the to mail
	 */
	@Async
	public void sendEmailWelcome(Map<String, Object> model, String toMail,
			ByteArrayOutputStream excelStream, ByteArrayOutputStream pdfStream) {
		sendEmail(model, toMail, "welcome.html", "Welcome to Employee - sathish",excelStream,pdfStream);
	}
	
	@Async
	public void sendEmailWelcome(Map<String, Object> model, String toMail,
			ByteArrayOutputStream imageStream) {
		sendEmail(model, toMail, "welcome.html", "Welcome to Employee - sathish",imageStream);
	}
	
	@Async
	public void sendEmailAttachment(Map<String, Object> model, ByteArrayOutputStream bos) {
		sendEmailAttachment(model, "welcome.html", "Welcome to Employee - sathish",bos);
	}
}
