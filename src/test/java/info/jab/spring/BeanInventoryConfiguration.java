package info.jab.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@TestConfiguration
public class BeanInventoryConfiguration {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	public record Tuple(String beanName, String pkg) {}
	public record BeanInventory(List<Tuple> beans) {}

	@Bean
	public BeanInventory getBeanInventory(ConfigurableApplicationContext applicationContext) {
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		return new BeanInventory(Arrays.stream(allBeanNames)
				.map(str -> getTuple(applicationContext, str))
				.toList());
	}

	private Tuple getTuple(ConfigurableApplicationContext applicationContext, String beanName) {
		String pkg = Objects.toString(applicationContext.getType(beanName).getCanonicalName());
		return new Tuple(beanName, Objects.isNull(pkg) ? "Unknown" : pkg);
	}

}