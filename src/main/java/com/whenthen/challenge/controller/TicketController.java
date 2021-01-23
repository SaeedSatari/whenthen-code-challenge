package com.whenthen.challenge.controller;

import com.whenthen.challenge.client.RestClient;
import com.whenthen.challenge.model.CommentModel;
import com.whenthen.challenge.model.RequestModel;
import com.whenthen.challenge.model.TicketModel;
import com.whenthen.challenge.model.request.TicketRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin
@AllArgsConstructor
public class TicketController {

    private final RestClient restClient;

    @PostMapping(path = "/tickets")
    public Map<String, Long> createSupportTicket(@RequestBody TicketRequest request) {
        CommentModel commentModel = CommentModel.builder().body(request.getMessage()).build();
        TicketModel ticketModel = TicketModel.builder().priority(getRandomPriority()).subject(request.getSubject()).comment(commentModel).build();
        RequestModel requestModel = RequestModel.builder().ticket(ticketModel).build();
        return restClient.createTicketAndCountPriorities(requestModel);
    }

    private String getRandomPriority(){
        List<String> priorities = new ArrayList<>();
        priorities.add("urgent");
        priorities.add("high");
        priorities.add("normal");
        priorities.add("low");
        Random rand = new Random();
        return priorities.get(rand.nextInt(priorities.size()));
    }
}
