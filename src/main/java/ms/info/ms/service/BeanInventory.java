package ms.info.ms.service;

import jakarta.annotation.PostConstruct;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class BeanInventory {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @PostConstruct
    public void after() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        AtomicInteger counter = new AtomicInteger();
        String space = " ";

        Arrays.stream(allBeanNames)
                .sorted()
                .map(beanName -> new StringBuilder()
                        .append(counter.incrementAndGet())
                        .append(space)
                        .append(beanName)
                        .toString())
                .forEach(System.out::println);
    }

}
