# spring-boot-http-client-poc

[![CI Builds](https://github.com/jabrena/spring-boot-http-client-poc/actions/workflows/build.yaml/badge.svg)](https://github.com/jabrena/spring-boot-http-client-poc/actions/workflows/build.yaml)

**Cloud IDEs:**

[![](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/jabrena/spring-boot-http-client-poc)

---

## How to build in local

```bash
mvn clean verify
mvn clean test -pl servlet/
```

## Motivation

Spring Frameworks provide a set of Clients to interact with HTTP Protocol.
This repository tries to explore how to use the different HTTP Clients provided by the Spring Ecosystem.

- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html

## History

In Java, you can interact with http using the classic Object URLConnection:

```java
public List<String> getGods() {

    List<String> responseBody = new ArrayList<>();

    try {
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
    
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBuilder.toString());
            
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    responseBody.add(node.toString());
                }
            } else {
                responseBody.add(jsonNode.toString());
            }
        }
        connection.disconnect();

    } catch (IOException e) {
        logger.error(e.getMessage(), e);
    }

    return responseBody.stream().map(god -> god.replace("\"", "")).toList();
}
```

- Javadoc: https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html

But this approach involve several low-level parts for a simple http interaction.

In order to help the developers, Spring ecosystem has delivered features to offer HTTP support for Servlet environments from the beginning using the client *RESTTemplate* which implements the Template Design pattern.

- Javadoc Spring Framework 5, Spring Boot 2.x: https://docs.spring.io/spring-framework/docs/5.3.9/javadoc-api/org/springframework/web/client/RestTemplate.html
- Javadoc Spring Framework 6, Spring Boot 3.x: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html

```java
public List<String> getGods() {
    ResponseEntity<List<String>> result =
            restTemplate.exchange(
                    address,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

    return result.getBody();
}
```

In Java 11, the language published a new way to interact with http protocol

```java
public List<String> getGods() {

    List<String> responseBody = new ArrayList<>();

    try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    responseBody.add(node.toString());
                }
            } else {
                responseBody.add(jsonNode.toString());
            }
        }

    } catch (IOException | InterruptedException e) {
        logger.error(e.getMessage(), e);
    }

    return responseBody.stream().map(god -> god.replace("\"", "")).toList();
}
```

- Javadocs: https://docs.oracle.com/en%2Fjava%2Fjavase%2F11%2Fdocs%2Fapi%2F%2F/java.net.http/java/net/http/HttpClient.html

In the last Decade, Java ecosystem evolved with the reactive programming paradym and Spring ecosystem adapted to it and in 2013, Spring released the first GA release for Reactor: 

- https://mvnrepository.com/artifact/org.projectreactor/reactor-spring/1.0.0.RELEASE

and in 2017, Spring Boot released the reactive support for it:

- https://mvnrepository.com/artifact/org.springframework/spring-webflux

and webflux provided a Reactive client:

```java
public Mono<List<String>> getGods() {
    return webClient
            .get()
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
}
```

in 2022 with the release of Spring Framework 6.1, it included **HTTP Interfaces**

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/service/annotation/GetExchange.html

```java
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface GodService {
    @GetExchange()
    List<String> getGods();
}
```

in 2024, Spring Boot 3.2 release, it included a new HTTP Client based on fluent API design named **RestClient**.

- Javadoc Spring Framework 6, Spring Boot 3.2 >: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestClient.html

```java
public List<String> getGods() {        
    ResponseEntity<List<String>> result =
        this.restClient
            .get()
            .uri(address)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<>() {});
    return result.getBody();
}
```

Note: Obviously, to that Spring Solutions, you can configure:

```java
@Configuration(proxyBeanMethods = false)
public class WebConfiguration {

    //Spring RestTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    //Spring RestClient
    @Bean
    RestClient restClient(RestTemplate restTemplate) {
        return RestClient.create(restTemplate);
    }

    /*
    @Bean
    RestClient restClient(RestClient.Builder builder) {
       return builder.build();
    }
    */

    //Http Interfaces
    @Bean(name = "http-interface-rest-client")
    GodService godServiceRestClient(RestClient client) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build()
                .createClient(GodService.class);
    }
}
```

## What is the level of activity about RestClient?

- https://github.com/spring-projects/spring-boot/issues?q=is%3Aissue+restclient+is%3Aclosed

## How to use the client:

From the documentation, you could use the new client in several ways:

- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html

```java

//General usage
RestClient defaultClient = RestClient.create();

RestClient customClient = RestClient.builder()
  .requestFactory(new HttpComponentsClientHttpRequestFactory())
  .messageConverters(converters -> converters.add(new MyCustomMessageConverter()))
  .baseUrl("https://example.com")
  .defaultUriVariables(Map.of("variable", "foo"))
  .defaultHeader("My-Header", "Foo")
  .requestInterceptor(myCustomInterceptor)
  .requestInitializer(myCustomInitializer)
  .build();

//GET Retrieving a Primitive type
String result = restClient.get() 
  .uri("https://example.com") 
  .retrieve() 
  .body(String.class);

//GET Retrieving an Single Object
int id = ...;
Pet pet = restClient.get()
  .uri("https://petclinic.example.com/pets/{id}", id) 
  .accept(APPLICATION_JSON) 
  .retrieve()
  .body(Pet.class);

//GET Retrieve a List of Objects
List<Article> articles = restClient.get()
  .uri(uriBase + "/articles")
  .retrieve()
  .body(new ParameterizedTypeReference<>() {});

//POST Sending a new Object as payload
Pet pet = new Pet(); 
ResponseEntity<Void> response = restClient.post() 
  .uri("https://petclinic.example.com/pets/new") 
  .contentType(APPLICATION_JSON) 
  .body(pet) 
  .retrieve()
  .toBodilessEntity();



//Error handling
String result = restClient.get() 
  .uri("https://example.com/this-url-does-not-exist") 
  .retrieve()
  .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
      throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders()) 
  })
  .body(String.class);

//Complex stuff
Pet result = restClient.get()
  .uri("https://petclinic.example.com/pets/{id}", id)
  .accept(APPLICATION_JSON)
  .exchange((request, response) -> { 
    if (response.getStatusCode().is4xxClientError()) { 
      throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders()); 
    }
    else {
      Pet pet = convertResponse(response); 
      return pet;
    }
  });
```

## Another alternatives

- https://square.github.io/okhttp/
- https://hc.apache.org/httpcomponents-client-5.3.x/index.html

## Further details

- https://github.com/jabrena/spring-boot-user-beans

## References

- https://en.wikipedia.org/wiki/HTTP
- https://start.spring.io/
- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html
- https://docs.spring.io/spring-framework/docs/
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestClient.html
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/service/annotation/GetExchange.html
- https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/client/RestClient.java
- https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/client/DefaultRestClientBuilder.java
- https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/client/DefaultRestClient.java
- https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/test/java/org/springframework/web/client/RestClientIntegrationTests.java
- https://github.com/spring-projects/spring-framework/issues/29552
- https://spring.io/guides/gs/consuming-rest/