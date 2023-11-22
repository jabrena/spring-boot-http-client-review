package info.jab.ms.controller;

import info.jab.ms.service.GodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyController3 {

    @Qualifier("http-interface-web-client")
    @Autowired
    private GodService godService;

    @GetMapping("/api/v1/http-interface-web-client")
    public List<String> getData() {
        return godService.getGods();
    }
}
