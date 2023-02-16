package info.jab.ms;

import info.jab.spring.BeanInventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@Import(BeanInventory.class)
@TestPropertySource(properties = { "spring.main.allow-circular-references=true" }) //TODO Remove ASAP
class BeanInventoryTests {

	@Autowired
	private BeanInventory beanInventory;

	@Test
	void should_be_stable_number_of_user_beans() {

		//Given
		int expectedBeans = 8;
		String rootPackage = "info.jab.ms";

		//When
		AtomicInteger counter = new AtomicInteger();
		var userBeans = beanInventory.getBeans().stream()
				.filter(t -> t.pkg().contains(rootPackage))
				.sorted(Comparator.comparing(BeanInventory.BeanInfo::name))
				.peek(bean -> expand.apply(counter, bean))
				.toList();

		//Then
		then(userBeans.size()).isEqualTo(expectedBeans);
	}

	BiFunction<AtomicInteger, BeanInventory.BeanInfo, Object> expand = (counter, bean) -> {
		String space = " ";
		var message = new StringBuilder()
				.append(counter.incrementAndGet())
				.append(space)
				.append(bean.name())
				.append(space)
				.append(bean.pkg())
				.toString();
		System.out.println(message);
		return message;
	};

	@Test
	void should_be_stable_number_of_not_user_beans() {

		//Given
		int expectedBeans = 150;
		String rootPackage = "info.jab.ms";

		//When
		AtomicInteger counter = new AtomicInteger();
		var userBeans = beanInventory.getBeans().stream()
				.filter(Predicate.not(bean -> bean.pkg().contains(rootPackage)))
				.sorted(Comparator.comparing(BeanInventory.BeanInfo::name))
				.peek(bean -> expand.apply(counter, bean))
				.toList();

		//Then
		then(userBeans.size()).isEqualTo(expectedBeans);
	}

}
