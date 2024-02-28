package info.jab.ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service("javaurlconnection")
public class MyServiceJavaURLConnectionImpl implements MyService {

    private static final Logger logger = LoggerFactory.getLogger(MyServiceJavaURLConnectionImpl.class);

    @Value("${address}")
    private String address;

    @Override
    public List<String> getGods() {

        List<String> responseBody = new ArrayList<>();

        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();
        
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBuilder.toString());
                
                if (jsonNode.isArray()) {
                    for (JsonNode node : jsonNode) {
                        responseBody.add(node.toString());
                    }
                } else {
                    responseBody.add(jsonNode.toString());
                }
            }
            connection.disconnect();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return responseBody.stream().map(god -> god.replace("\"", "")).toList();
    }
}
