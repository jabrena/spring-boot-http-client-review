package info.jab.ms.controller;

import info.jab.ms.service.GodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyController3 {

    @Autowired
    private GodService godService;

    @GetMapping("/api/v1/http-interface")
    public List<String> getData() {
        return godService.getGods();
    }
}
