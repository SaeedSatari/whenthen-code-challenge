package com.whenthen.challenge.controller;

import com.whenthen.challenge.client.RestClient;
import com.whenthen.challenge.model.CommentModel;
import com.whenthen.challenge.model.RequestModel;
import com.whenthen.challenge.model.TicketModel;
import com.whenthen.challenge.model.request.TicketRequest;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("api/tickets")
public class TicketController {

    private final RestClient restClient;

    @PostMapping
    @ApiOperation(value = "create a support ticket and call zendesk api to get list of the messages and count priorities"
            , nickname = "create-support-ticket-count-priorities")
    public Map<String, Long> createSupportTicketAndCountPriorities(@RequestBody TicketRequest request) {
        CommentModel commentModel = CommentModel.builder().body(request.getMessage()).build();
        TicketModel ticketModel = TicketModel.builder().priority(getRandomPriority()).subject(request.getSubject()).comment(commentModel).build();
        RequestModel requestModel = RequestModel.builder().ticket(ticketModel).build();
        return restClient.createTicketAndCountPriorities(requestModel);
    }

    // I created this method, because I did not know how to set priority field from given request
    private String getRandomPriority() {
        List<String> priorities = new ArrayList<>();
        priorities.add("urgent");
        priorities.add("high");
        priorities.add("normal");
        priorities.add("low");
        Random rand = new Random();
        return priorities.get(rand.nextInt(priorities.size()));
    }
}
