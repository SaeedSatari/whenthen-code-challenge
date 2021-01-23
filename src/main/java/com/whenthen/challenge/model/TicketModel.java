package com.whenthen.challenge.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketModel {
    private CommentModel comment;
    private String priority;
    private String subject;
}
