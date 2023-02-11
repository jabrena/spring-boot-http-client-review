package ms.info.ms.controller;

import ms.info.ms.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyController3 {

    @Autowired
    @Qualifier("http-interface")
    private MyService myService;

    @GetMapping("/api/v1/http-interface")
    public List<String> getData() {
        return myService.getGods();
    }
}
