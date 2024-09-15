package com.example.emailservice.Consumer;

import com.example.emailservice.dto.SendEmailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.util.ToStringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SendEmailEventConsumer {
    private ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //Exact same name you have used in publishing(Publisher/Producer)...Refer UserService
    @KafkaListener(topics = "send_Email", groupId = "emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        //objectMapper.readValue(message, Message is extracted in what format (i.e) use the same sendEmailDTO)
        SendEmailDTO sendEmailDto = objectMapper.readValue(message, SendEmailDTO.class);
        String from = sendEmailDto.getFrom();
        String to = sendEmailDto.getTo();
        String subject = sendEmailDto.getSubject();
        String body = sendEmailDto.getBody();

        System.out.println("Sending email process starts");

        //Here, we use -> send Email in Java SMTP with TLS Authentication
        /**
         Outgoing Mail (SMTP) Server
         requires TLS or SSL: smtp.gmail.com (use authentication)
         Use Authentication: Yes
         Port for TLS/STARTTLS: 587
         */

        final String fromEmail = "krishengineerxyz@scaler.com"; //requires valid gmail id
        final String password = "jkj jlkjl jlkj ljkj"; // correct password for gmail id
        final String toEmail = "vidhuengineerxyz@scaler.com"; // can be any email id

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host -> (key, value)
        props.put("mail.smtp.port", "587"); //TLS Port -> (key, value)
        props.put("mail.smtp.auth", "true"); //enable authentication -> (key, value)
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS -> (key, value)

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                //password return type is String but we every word store it in array
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, to, subject, body);//we are get it from line no 27 to 31
    }

}
