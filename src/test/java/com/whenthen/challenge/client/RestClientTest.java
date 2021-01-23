package com.whenthen.challenge.client;

import com.whenthen.challenge.model.CommentModel;
import com.whenthen.challenge.model.RequestModel;
import com.whenthen.challenge.model.TicketModel;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RestClientTest {

    @InjectMocks
    private RestClient restClient;
    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName("createTicketAndCountPriorities, when every thing is fine, should returns number of normal priorities")
    void createTicketAndCountPriorities_whenEveryThingIsFine_shouldReturnsNumberOfNormalPriorities() {
        CommentModel commentModel = CommentModel.builder().body("dummy message").build();
        TicketModel ticketModel = TicketModel.builder().priority("normal").subject("dummy subject").comment(commentModel).build();
        RequestModel requestModel = RequestModel.builder().ticket(ticketModel).build();

        String encodedCredentials = new String(Base64.encodeBase64("eamon121@gmail.com:8ter8V!aiYy5JhrB".getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<RequestModel> createTicketRequest = new HttpEntity<>(requestModel, httpHeaders);
        HttpEntity<MultiValueMap<String, String>> listTicketRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> createResponse = new ResponseEntity<>("{\n" +
                "   \"ticket\":{\n" +
                "      \"url\":\"https://wt-technical-test.zendesk.com/api/v2/tickets/65.json\",\n" +
                "      \"subject\":\"subject\",\n" +
                "      \"description\":\"message message message message message message message message message message message message \",\n" +
                "      \"priority\":\"urgent\",\n" +
                "      \"status\":\"open\"\n" +
                "   },\n" +
                "}", HttpStatus.OK);

        ResponseEntity<String> listTicketResponse = new ResponseEntity<>("{\n" +
                "   \"tickets\":[\n" +
                "      {\n" +
                "         \"url\":\"https://wt-technical-test.zendesk.com/api/v2/tickets\",\n" +
                "         \"subject\":\"Sample ticket\",\n" +
                "         \"priority\":\"normal\",\n" +
                "         \"status\":\"open\"\n" +
                "      }\n" +
                "   ]\n" +
                "}", HttpStatus.OK);

        when(restTemplate.exchange("https://wt-technical-test.zendesk.com/api/v2/tickets", HttpMethod.POST, createTicketRequest, String.class)).thenReturn(createResponse);
        when(restTemplate.exchange("https://wt-technical-test.zendesk.com/api/v2/tickets", HttpMethod.GET, listTicketRequest, String.class)).thenReturn(listTicketResponse);

        Map<String, Long> persisted = restClient.createTicketAndCountPriorities(requestModel);

        assertThat(persisted.size()).isNotNull();
        assertThat(persisted.get("normal")).isEqualTo(1);
    }
}