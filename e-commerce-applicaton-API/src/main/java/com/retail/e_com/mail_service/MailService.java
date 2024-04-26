package com.retail.e_com.mail_service;

import java.util.Date;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.retail.e_com.utility.MessageModel;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailService {
	
	private JavaMailSender javaMailSender;
	
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMailMessage(MessageModel model) throws MessagingException {
		MimeMessage message1 = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message1,true);
		helper.setTo(model.getTo());
		helper.setSubject("OTP Verification");
		helper.setSentDate(new Date());
		helper.setText(model.getText() , true);//for any html languauge input set true
		javaMailSender.send(message1);
	}

}
