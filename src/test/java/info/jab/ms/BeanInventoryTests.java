package info.jab.ms;

import info.jab.spring.BeanInventoryConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@Import(BeanInventoryConfiguration.class)
class BeanInventoryTests {

	@Autowired
	private BeanInventoryConfiguration.BeanInventory beanInventory;

	@Test
	void should_be_stable_number_of_user_beans() {

		//Given
		int expectedBeans = 7;
		String rootPackage = "info.jab.ms";

		//When
		AtomicInteger counter = new AtomicInteger();
		String space = " ";
		var userBeans = beanInventory.beans().stream()
				.filter(t -> t.pkg().contains(rootPackage))
				.sorted(Comparator.comparing(BeanInventoryConfiguration.Tuple::beanName))
				.map(beanName -> new StringBuilder()
						.append(counter.incrementAndGet())
						.append(space)
						.append(beanName.beanName())
						.append(space)
						.append(beanName.pkg())
						.toString())
				.peek(System.out::println)
				.toList();

		//Then
		then(userBeans.size()).isEqualTo(expectedBeans);
	}

	@Disabled
	@Test
	void should_be_stable_number_of_not_user_beans() {

		//Given
		int expectedBeans = 152;
		String rootPackage = "info.jab.ms";

		//When
		AtomicInteger counter = new AtomicInteger();
		String space = " ";
		BiPredicate<String, String> filter = (x, y) -> x.contains(y);
		BiPredicate<String, String> filterNegated = filter.negate();
		var userBeans = beanInventory.beans().stream()
				.filter(bean -> filterNegated.test(bean.pkg(), rootPackage))
				.sorted(Comparator.comparing(BeanInventoryConfiguration.Tuple::beanName))
				.map(beanName -> new StringBuilder()
						.append(counter.incrementAndGet())
						.append(space)
						.append(beanName.beanName())
						.append(space)
						.append(beanName.pkg())
						.toString())
				.peek(System.out::println)
				.toList();

		//Then
		then(userBeans.size()).isEqualTo(expectedBeans);
	}

}
