package com.example.giveandtake.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration public class MailConfig
{
    @Value("${spring.mail.id}")
    private static String emailid;
    @Value("${spring.mail.password}")
    private static String emailpassword;


    @Bean
    public static JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);
        mailSender.setUsername(emailid);
        mailSender.setPassword(emailpassword);
        mailSender.setDefaultEncoding("utf-8");
        Properties prop = new Properties();


        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.debug", "true");
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.ssl.enable", true);
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.auth",true);
        prop.put("mail.smtp.starttls.enable",true);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSender.setJavaMailProperties(prop);

        return mailSender; } }

