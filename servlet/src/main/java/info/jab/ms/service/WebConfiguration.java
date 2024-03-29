package info.jab.ms.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
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