package info.jab.ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service("javahttpclient")
public class MyServiceJavaHttpClientImpl implements MyService {

    private static final Logger logger = LoggerFactory.getLogger(MyServiceJavaHttpClientImpl.class);

    @Value("${address}")
    private String address;

    @Override
    public List<String> getGods() {

        List<String> responseBody = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());
                
                if (jsonNode.isArray()) {
                    for (JsonNode node : jsonNode) {
                        responseBody.add(node.toString());
                    }
                } else {
                    responseBody.add(jsonNode.toString());
                }
            }
    
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        return responseBody.stream().map(god -> god.replace("\"", "")).toList();
    }
}
