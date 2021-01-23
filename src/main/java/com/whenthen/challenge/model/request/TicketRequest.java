package com.whenthen.challenge.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketRequest {

    @ApiModelProperty(example = "Saeed Sattari", required = true)
    private String name;

    @ApiModelProperty(example = "saeedsatari93@gmail.com", required = true)
    private String email;

    @ApiModelProperty(example = "Test subject", required = true)
    private String subject;

    @ApiModelProperty(example = "Test message from client", required = true)
    private String message;
}