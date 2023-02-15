package info.jab.ms;

import info.jab.spring.BeanInventoryConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
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

		//TODO it is missing the Bean from a http interface
		int expectedBeans = 7;
		String rootPackage = "info.jab.ms";

		//When
		AtomicInteger counter = new AtomicInteger();
		var userBeans = beanInventory.beans().stream()
				.filter(t -> t.pkg().contains(rootPackage))
				.peek(bean -> expand.apply(counter, bean))
				.toList();

		//Then
		then(userBeans.size()).isEqualTo(expectedBeans);
	}

	BiFunction<AtomicInteger, BeanInventoryConfiguration.Tuple, String> expand = (counter, bean) -> {
		String space = " ";
		var message = new StringBuilder()
				.append(counter.incrementAndGet())
				.append(space)
				.append(bean.beanName())
				.append(space)
				.append(bean.pkg())
				.toString();
		System.out.println(message);
		return message;
	};

	@Disabled
	@Test
	void should_be_stable_number_of_not_user_beans() {

		//Given
		int expectedBeans = 145;
		String rootPackage = "info.jab.ms";

		//When
		AtomicInteger counter = new AtomicInteger();
		String space = " ";
		var userBeans = beanInventory.beans().stream()
				.filter(Predicate.not(bean -> bean.pkg().contains(rootPackage)))
				.sorted(Comparator.comparing(BeanInventoryConfiguration.Tuple::beanName))
				.peek(bean -> expand.apply(counter, bean))
				.toList();

		//Then
		then(userBeans.size()).isEqualTo(expectedBeans);
	}

}
