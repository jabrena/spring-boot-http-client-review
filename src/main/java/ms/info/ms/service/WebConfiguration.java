package ms.info.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebConfiguration {

    //Spring RestTemplate
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public void setRestTemplateBuilder(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder.build();
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
    @Bean
    GodService godService(WebClient client) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(client))
                .build();

        return httpServiceProxyFactory.createClient(GodService.class);
    }

}