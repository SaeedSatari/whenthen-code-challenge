package com.whenthen.challenge.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestModel {
    private TicketModel ticket;
}
