package info.jab.ms.service;

import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface GodService {
    @GetExchange()
    List<String> getGods();
}