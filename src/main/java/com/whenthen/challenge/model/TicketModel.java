package com.whenthen.challenge.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketModel {
    public CommentModel comment;
    public String priority;
    public String subject;
}
