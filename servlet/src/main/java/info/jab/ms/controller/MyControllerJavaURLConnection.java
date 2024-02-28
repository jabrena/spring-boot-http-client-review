package info.jab.ms.controller;

import info.jab.ms.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyControllerJavaURLConnection {

    @Autowired
    @Qualifier("javaurlconnection")
    private MyService myService;

    @GetMapping("/api/v1/java-urlconnection")
    public List<String> getData() {
        return myService.getGods();
    }
}
