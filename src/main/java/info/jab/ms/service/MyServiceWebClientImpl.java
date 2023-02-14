package info.jab.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service("webclient")
public class MyServiceWebClientImpl implements MyService {

    @Autowired
    private WebClient webClient;

    @Override
    public List<String> getGods() {
        return webClient
                .get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
}
