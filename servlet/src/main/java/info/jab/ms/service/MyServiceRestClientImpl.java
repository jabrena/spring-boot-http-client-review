package info.jab.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import java.util.List;

@Service("restclient")
public class MyServiceRestClientImpl implements MyService {

    @Autowired
    private RestClient restClient;

    @Value("${address}")
    private String address;

    @Override
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
}
