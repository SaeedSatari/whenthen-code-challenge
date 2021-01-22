package com.whenthen.challenge.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whenthen.challenge.model.PrioModel;
import com.whenthen.challenge.model.RootModel;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RestClient {

    private final RestTemplate restTemplate;

    public static final String REST_SERVICE_URL = "https://wt-technical-test.zendesk.com/api/v2/tickets";

    private static HttpHeaders getHeaders() {
        String userCredentials = "eamon121@gmail.com:8ter8V!aiYy5JhrB";
        String encodedCredentials =
                new String(Base64.encodeBase64(userCredentials.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    public Map<String, Long> createTicket(RootModel rootModel) throws JSONException, JsonProcessingException {
        HttpEntity<RootModel> createRequest = new HttpEntity<>(rootModel, getHeaders());

        ResponseEntity<String> createResponse = restTemplate.exchange(REST_SERVICE_URL, HttpMethod.POST, createRequest, String.class);

        HttpStatus statusCode = createResponse.getStatusCode();

        List<JSONObject> openTickets = new ArrayList<>();

        HttpEntity<MultiValueMap<String, String>> listTicketRequest = new HttpEntity<>(getHeaders());
        if (statusCode.is2xxSuccessful()) {
            ResponseEntity<String> listTicketResponse = restTemplate.exchange(REST_SERVICE_URL, HttpMethod.GET, listTicketRequest, String.class);
            JSONObject listTicketResponseBody = new JSONObject(listTicketResponse.getBody());
            JSONArray tickets = listTicketResponseBody.getJSONArray("tickets");
            for (int i = 0; i < tickets.length(); i++) {
                JSONObject ticket = tickets.getJSONObject(i);
                String status = ticket.get("status").toString();
                if (status.equals("open")) {
                    openTickets.add(ticket);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        List<PrioModel> list = new LinkedList<>(Arrays.asList(mapper.readValue(openTickets.toString(), PrioModel[].class)));

        list.removeIf(prioModel -> prioModel.getPriority() == null);

        return list.stream().collect(
                Collectors.groupingBy(PrioModel::getPriority, Collectors.counting()));
    }
}
