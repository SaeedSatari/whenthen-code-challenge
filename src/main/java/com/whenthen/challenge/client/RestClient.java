package com.whenthen.challenge.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whenthen.challenge.model.PriorityModel;
import com.whenthen.challenge.model.RequestModel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class RestClient {

    private final RestTemplate restTemplate;

    private static final String REST_SERVICE_URL = "https://wt-technical-test.zendesk.com/api/v2/tickets";
    private static final String HEADER_PREFIX = "Basic ";
    private static final String USER_CREDENTIALS = "eamon121@gmail.com:8ter8V!aiYy5JhrB";

    private static HttpHeaders getHeaders() {
        String encodedCredentials = getEncodedCredentials();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + encodedCredentials);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private static String getEncodedCredentials() {
        return new String(Base64.encodeBase64(USER_CREDENTIALS.getBytes()));
    }

    public Map<String, Long> createTicketAndCountPriorities(RequestModel requestModel) {
        HttpStatus statusCode = createTicketAndGetHttpStatus(requestModel);
        List<JSONObject> openTickets = getOpenTickets(statusCode);
        List<PriorityModel> listPriorities = getPriorities(openTickets);
        return countPriorities(listPriorities);
    }

    private HttpStatus createTicketAndGetHttpStatus(RequestModel requestModel) {
        HttpEntity<RequestModel> createTicketRequest = new HttpEntity<>(requestModel, getHeaders());
        ResponseEntity<String> createResponse = restTemplate.exchange(REST_SERVICE_URL, HttpMethod.POST, createTicketRequest, String.class);
        return createResponse.getStatusCode();
    }

    @SneakyThrows
    private List<JSONObject> getOpenTickets(HttpStatus statusCode) {
        List<JSONObject> openTickets = new ArrayList<>();

        if (statusCode.is2xxSuccessful()) {
            JSONArray tickets = getTickets();
            for (int i = 0; i < tickets.length(); i++) {
                JSONObject ticket = tickets.getJSONObject(i);
                String status = ticket.get("status").toString();
                if (status.equals("open")) {
                    openTickets.add(ticket);
                }
            }
        }
        return openTickets;
    }

    @SneakyThrows
    private JSONArray getTickets() {
        HttpEntity<MultiValueMap<String, String>> listTicketRequest = new HttpEntity<>(getHeaders());
        ResponseEntity<String> listTicketResponse = restTemplate.exchange(REST_SERVICE_URL, HttpMethod.GET, listTicketRequest, String.class);
        JSONObject listTicketResponseBody = new JSONObject(listTicketResponse.getBody());
        return listTicketResponseBody.getJSONArray("tickets");
    }

    private List<PriorityModel> getPriorities(List<JSONObject> openTickets) {
        ObjectMapper mapper = getObjectMapper();
        PriorityModel[] priorityModels = new PriorityModel[0];
        try {
            priorityModels = mapper.readValue(openTickets.toString(), PriorityModel[].class);
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        List<PriorityModel> listPriorities = new LinkedList<>(Arrays.asList(priorityModels));
        listPriorities.removeIf(priorityModel -> priorityModel.getPriority() == null);
        return listPriorities;
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    private Map<String, Long> countPriorities(List<PriorityModel> listPriorities) {
        return listPriorities.stream().collect(
                Collectors.groupingBy(PriorityModel::getPriority, Collectors.counting()));
    }
}
