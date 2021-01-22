package com.whenthen.challenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whenthen.challenge.client.RestClient;
import com.whenthen.challenge.model.CommentModel;
import com.whenthen.challenge.model.RootModel;
import com.whenthen.challenge.model.TicketModel;
import com.whenthen.challenge.model.TicketRequest;
import lombok.AllArgsConstructor;
import org.json.JSONException;
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
    public Map<String, Long> createSupportTicket(@RequestBody TicketRequest request) throws JSONException, JsonProcessingException {

        CommentModel commentModel = new CommentModel();
        commentModel.setBody(request.getMessage());

        TicketModel ticketModel = new TicketModel();
        ticketModel.setPriority("urgent");
        ticketModel.setSubject(request.getSubject());
        ticketModel.setComment(commentModel);

        RootModel rootModel = new RootModel();
        rootModel.setTicket(ticketModel);

        return  restClient.createTicket(rootModel);
    }
}
