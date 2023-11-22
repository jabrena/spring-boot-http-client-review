package info.jab.ms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
public class WebConfiguration {

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
}