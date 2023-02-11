package ms.info.ms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service("webclient")
public class MyServiceImpl2 implements MyService {

    @Value("${address}")
    private String address;

    @Override
    public List<String> getGods() {
        var result = WebClient.builder()
                .baseUrl(address)
                .build()
                .get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        return result;
    }
}
