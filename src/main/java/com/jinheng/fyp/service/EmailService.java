package com.jinheng.fyp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.jinheng.fyp.bean.EmailDetails;

/**
 * Setup email protocol and send email. Specify email to be sent according to mode<br>
 * Field: targetEmail, mode, List info
 * 
 * @author chongjinheng
 */
@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private Properties emailSettings;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Setup email protocol and send email. Specify email to be sent according to mode<br>
	 * 
	 * @author chongjinheng
	 */
	public void sendEmail(final String targetEmail, final int mode, final EmailDetails info) throws MailException {
		class MultithreadEmail implements Runnable {

			final Map<String, Object> model = new HashMap<String, Object>();
			final String[] subject = emailSettings.getProperty("subjects").split(",");
			final String[] template = emailSettings.getProperty("templates").split(",");

			@Override
			public void run() {
				try {
					model.put("targetEmail", targetEmail);
					model.put("info", info);

					MimeMessagePreparator preparator = new MimeMessagePreparator() {

						public void prepare(MimeMessage mimeMessage) throws Exception {
							MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
							message.setTo(targetEmail);
							message.setFrom(new InternetAddress("noreply@parking.locator.gmail.com"));
							message.setSubject(subject[mode]);
							message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "emailTemplates/" + template[mode], "UTF-8", model), true);
						}
					};
					mailSender.send(preparator);
					logger.info("Email Sent to " + targetEmail);

				} catch (MailException e) {
					logger.error("Error in sendEmail :" + e);
				}
			}

		}
		Thread t = new Thread(new MultithreadEmail());
		t.start();

	}
}
