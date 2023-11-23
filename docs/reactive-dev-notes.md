# Dev Notes:

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