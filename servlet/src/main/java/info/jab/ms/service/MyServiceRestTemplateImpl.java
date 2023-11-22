package info.jab.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("restemplate")
public class MyServiceRestTemplateImpl implements MyService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${address}")
    private String address;

    @Override
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
}
