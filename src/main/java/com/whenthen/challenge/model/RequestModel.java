package com.whenthen.challenge.model;

import lombok.Data;

@Data
public class RequestModel {

    private String name;
    private String email;
    private String subject;
    private String message;
}