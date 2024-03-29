package com.mocadev.studyolle.mail;

import java.io.InputStream;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-02-25
 **/
@Profile("local")
@Component
@Slf4j
public class ConsoleMailSender implements JavaMailSender {

	@Override
	public MimeMessage createMimeMessage() {
		return null;
	}

	@Override
	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		return null;
	}

	@Override
	public void send(MimeMessage mimeMessage) throws MailException {

	}

	@Override
	public void send(MimeMessage... mimeMessages) throws MailException {

	}

	@Override
	public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

	}

	@Override
	public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

	}

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		log.info("mail = {}", simpleMessage);
	}

	@Override
	public void send(SimpleMailMessage... simpleMessages) throws MailException {

	}
}
