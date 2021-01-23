package com.whenthen.challenge.controller;

import com.whenthen.challenge.client.RestClient;
import com.whenthen.challenge.model.CommentModel;
import com.whenthen.challenge.model.RequestModel;
import com.whenthen.challenge.model.TicketModel;
import com.whenthen.challenge.model.TicketRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
@AllArgsConstructor
public class ExampleController {

    private final RestClient restClient;

    @PostMapping(path = "/tickets")
    public Map<String, Long> createSupportTicket(@RequestBody TicketRequest request) {

        CommentModel commentModel = new CommentModel();
        commentModel.setBody(request.getMessage());

        TicketModel ticketModel = new TicketModel();
        ticketModel.setPriority("urgent");
        ticketModel.setSubject(request.getSubject());
        ticketModel.setComment(commentModel);

        RequestModel requestModel = new RequestModel();
        requestModel.setTicket(ticketModel);

        return restClient.createTicketAndCountPriorities(requestModel);
    }
}
