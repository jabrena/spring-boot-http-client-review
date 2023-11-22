package info.jab.spring;

import jakarta.annotation.PostConstruct;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Simon Basle
 * @Author Juan Antonio Breña Moral
 */
@TestConfiguration
public class BeanInventory {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	public record BeanInfo(String name, String pkg) {}
	private List<BeanInfo> beans;

	@PostConstruct
	private void after() {

		final String[] beanNames = applicationContext.getBeanDefinitionNames();
		List<BeanInfo> tempList = new ArrayList<>();
		for (String beanName : beanNames) {
			//Avoid the circular reference issue
			if(beanName.contains("BeanInventory")) {
				continue;
			}
			final Object beanObject = applicationContext.getBean(beanName);
			Class<?> targetClass = AopUtils.getTargetClass(beanObject);

			if (AopUtils.isJdkDynamicProxy(beanObject)) {
				Class<?>[] proxiedInterfaces = AopProxyUtils.proxiedUserInterfaces(beanObject);
				Assert.isTrue(proxiedInterfaces.length == 1, "Only one proxied interface expected");
				targetClass = proxiedInterfaces[0];
			}

			tempList.add(new BeanInfo(beanName, targetClass.getPackageName()));
		}
		beans = Collections.unmodifiableList(tempList);
	}

	public List<BeanInfo> getBeans() {
		return beans;
	}

}