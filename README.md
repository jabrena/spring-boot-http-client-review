# spring-boot-http-client-poc

[![CI Builds](https://github.com/jabrena/spring-boot-http-client-poc/actions/workflows/build.yaml/badge.svg)](https://github.com/jabrena/spring-boot-http-client-poc/actions/workflows/build.yaml)

## Motivation

Spring Frameworks provide a set of Clients to interact with HTTP Protocol.
This repository tries to explore how to use the different HTTP Clients provided by the Spring Ecosystem.

- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html

## History

Traditionally Spring ecosystem has delivered features to offer HTTP support for Servlet environments from the beginning using the client *RESTTemplate* which implements the Template Design pattern.

- Javadoc Spring Framework 5, Spring Boot 2.x: https://docs.spring.io/spring-framework/docs/5.3.9/javadoc-api/org/springframework/web/client/RestTemplate.html
- Javadoc Spring Framework 6, Spring Boot 3.x: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html

```java
@SpringBootApplication
public class ConsumingRestApplication {

	private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConsumingRestApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Quote quote = restTemplate.getForObject(
					"http://localhost:8080/api/random", Quote.class);
			log.info(quote.toString());
		};
	}
}
```

In the last Decade, Java ecosystem evolved with the reactive programming paradym and Spring ecosystem adapted to it and in 2013, Spring released the first GA release for Reactor: 

- https://mvnrepository.com/artifact/org.projectreactor/reactor-spring/1.0.0.RELEASE

and in 2017, Spring Boot released the reactive support for it:

- https://mvnrepository.com/artifact/org.springframework/spring-webflux

```java
@Service
public class MyService {

	private final WebClient webClient;

	public MyService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://example.org").build();
	}

	public Mono<Details> someRestCall(String name) {
		return this.webClient.get().url("/{name}/details", name)
						.retrieve().bodyToMono(Details.class);
	}

}
```

- Example from: https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/html/boot-features-webclient.html


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
```

## What is the level of activity about RestClient?

- https://github.com/spring-projects/spring-boot/issues?q=is%3Aissue+restclient+is%3Aclosed

## How to use the client:

```java
String result = restClient.get() 
  .uri("https://example.com") 
  .retrieve() 
  .body(String.class);

int id = ...;
Pet pet = restClient.get()
  .uri("https://petclinic.example.com/pets/{id}", id) 
  .accept(APPLICATION_JSON) 
  .retrieve()
  .body(Pet.class);

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

## Project structure:

- Servlet: spring-boot-starter-web
- Hybrid: spring-boot-starter-web + spring-boot-starter-webflux

## How to run in local?

```bash
sdk env
sdk install mvnd
mvnd verify 
```

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