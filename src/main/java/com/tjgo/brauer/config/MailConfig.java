package com.tjgo.brauer.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.tjgo.brauer.mail.Mailer;

@Configuration
@ComponentScan(basePackageClasses = Mailer.class)
@PropertySource({"classpath:env/mail-${ambiente:local}.properties"})

/*Windows*/
@PropertySource(value = { "file:${USERPROFILE}/.brauer-mail.properties" }, ignoreResourceNotFound = true)

/*IOS*/
/*@PropertySource ( value  = { "file://${HOME}/.brauer-mail.properties" }, ignoreResourceNotFound  =  true )*/
public class MailConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSender mailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.mailgun.org");
		mailSender.setPort(587);
		mailSender.setUsername(env.getProperty("email.username"));
		mailSender.setPassword(env.getProperty("email.password"));
		
		System.out.println(">>>> username: " + env.getProperty("email.username"));
		/*System.out.println(">>>> password: " + env.getProperty("email.password"));*/
		System.out.println(">>>> Configure: Environment/variável -ambiente- em -Run Configurations- do eclipse!");
		System.out.println(">>>> Ou configure: USERPROFILE  /.brauer-mail.properties!");
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", false);
		props.put("mail.smtp.connectiontimeout", 10000);  //milisegundos
		
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}

}
