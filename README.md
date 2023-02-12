# spring-boot-http-client-poc

[![CI Builds](https://github.com/jabrena/spring-boot-http-client-poc/actions/workflows/build.yaml/badge.svg)](https://github.com/jabrena/spring-boot-http-client-poc/actions/workflows/build.yaml)

A review of different ways to communicate with a REST endpoint

- [x] Spring RestTemplate
- [x] Spring WebClient
- [x] Spring Http Interfaces

## How to run in local?

```bash
sdk env
sdk install mvnd
mvnd verify 
mvnd spring-boot:run
```

## Dev Notes:

Excluding the dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
    <exclusions>
        <exclusion>
            <groupId>io.projectreactor.netty</groupId>
            <artifactId>reactor-netty-http</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

You will not have the following beans in the `Spring Bean Container` if you use Tomcat:

```
nettyWebServerFactoryCustomizer
org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration
org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration$NettyWebServerFactoryCustomizerConfiguration
org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorConfiguration$ReactorNetty
reactorClientHttpConnector
reactorClientResourceFactory
spring.netty-org.springframework.boot.autoconfigure.netty.NettyProperties
```

## References

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/service/annotation/GetExchange.html