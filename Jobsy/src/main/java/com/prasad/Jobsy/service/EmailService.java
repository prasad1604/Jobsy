package com.prasad.Jobsy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String name){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to Jobsy!");
        message.setText("Hello "+ name + "\n\n Thanks for registering with us! \n\n Regards,\nJobsy Team");
        mailSender.send(message);
    }

    public void sendResetOtpEmail(String toEmail,String otp,String name){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");
        message.setText("Hello, "+ name +"\n\n Your OTP for password reset is: " + otp + "\n This OTP is valid for 15 minutes.\n\n Regards,\nJobsy Team");
        mailSender.send(message);
    }

    public void sendOtpEmail(String toEmail,String otp,String name){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Account Verification OTP");
        message.setText("Hello, "+ name +"\n\n Your OTP for account verification is: " + otp + "\n This OTP is valid for 24 hours.\n\n Regards,\nJobsy Team");
        mailSender.send(message);
    }  
}
