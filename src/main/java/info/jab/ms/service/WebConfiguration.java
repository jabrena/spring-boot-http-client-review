package info.jab.ms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;

@Configuration(proxyBeanMethods = false)
public class WebConfiguration {

    //Spring RestTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    //Spring RestClient
    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    //Spring Web Client
    @Value("${address}")
    private String address;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(address)
                .build();
    }

    //Spring http interfaces
    @Bean(name = "http-interface-web-client")
    GodService godServiceWebClient(WebClient client) {
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.forClient(client))
                .build()
                .createClient(GodService.class);
    }

    @Bean(name = "http-interface-rest-client")
    GodService godServiceRestClient(RestClient client) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build()
                .createClient(GodService.class);
    }

}