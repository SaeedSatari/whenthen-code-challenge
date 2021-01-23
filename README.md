# whenthen-code-challenge

This project created to solve https://whenthen.com/ coding challenge.

## Getting started

### Prerequisites:

- Java 11
- Spring Boot 2.4.2
- Maven as our project management and comprehension tool

### Tools:
- IntelliJ IDEA
- Open Api and Swagger for describing RESTful APIs expressed
- Lombok 

### Clone the repository

```bash
    git clone https://github.com/SaeedSatari/whenthen-code-challenge.git
```

### Run the app using maven

```bash
    cd whenthen-code-challenge
    mvn spring-boot:run
```

### Accessing to the API using swagger

You can access and try the api using this link: 

```bash
    http://localhost:8080/swagger-ui.html
```

### Accessing to the API using curl

```curl
    curl --location --request POST 'localhost:8080/api/tickets' \
    --header 'Authorization: Basic ZWFtb24xMjFAZ21haWwuY29tOjh0ZXI4ViFhaVl5NUpockI=' \
    --header 'Content-Type: application/json' \
    --data-raw '{
      "name":"Saeed",
      "email":"saeedstari93@gmail.com",
      "subject":"test subject",
      "message":"this is a test message"
    }'
```

- I got the input data from the request and send it to the Zendesk API to create a new Support Ticket at first.
- On success, I used the Zendesk API to list all the open tickets and group them by priority. 
- Finally, I returned a response object with the count of each priority.