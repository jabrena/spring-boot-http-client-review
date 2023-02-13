package ms.info.ms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
class BeanInventoryTests {

	@TestConfiguration
	static class BeanInventoryConfiguration {

		@Autowired
		private ConfigurableApplicationContext applicationContext;

		record BeanInventory(List<String> beans) {}

		@Bean
		public BeanInventory getBeanInventory(ConfigurableApplicationContext applicationContext) {
			String[] allBeanNames = applicationContext.getBeanDefinitionNames();
			return new BeanInventory(Arrays.stream(allBeanNames).toList());
		}
	}

	@Autowired
	private BeanInventoryConfiguration.BeanInventory beanInventory;

	@Test
	void should_be_stable_number_of_beans() {

		System.out.println("ECO");

		//Given
		int expectedBeans = 158;

		//When
		AtomicInteger counter = new AtomicInteger();
		String space = " ";
		beanInventory.beans().stream()
				.sorted()
				.map(beanName -> new StringBuilder()
						.append(counter.incrementAndGet())
						.append(space)
						.append(beanName)
						.toString())
				.forEach(System.out::println);

		//Then
		then(beanInventory.beans().size()).isEqualTo(expectedBeans);
	}

}
