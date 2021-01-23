package com.whenthen.challenge.model.request;

import lombok.Data;

@Data
public class TicketRequest {

    private String name;
    private String email;
    private String subject;
    private String message;
}