package ms.info.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("http-interface")
public class MyServiceImpl3 implements MyService {

    @Autowired
    private GodService personService;

    @Override
    public List<String> getGods() {
        return personService.getGods();
    }
}
