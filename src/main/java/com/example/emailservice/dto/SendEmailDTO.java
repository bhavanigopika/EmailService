package com.example.emailservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailDTO {
    private String from;
    private String to;
    private String subject;
    private String body;
}
