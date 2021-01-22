package com.whenthen.challenge.model;

import lombok.Data;

@Data
public class TicketModel {
    public CommentModel comment;
    public String priority;
    public String subject;
}
