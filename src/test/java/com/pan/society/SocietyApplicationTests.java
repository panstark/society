package com.pan.society;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.pan.society.vo.Address;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;

//
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SocietyApplicationTests {

	@Test
	public void contextLoads() {

		System.out.println(Address.class.getSimpleName().substring(0, Address.class.getSimpleName().length() - 3));
		Address address = new Address();
		String getterMethodName = "getPostcode";

		try {
			System.out.println(MethodUtils.invokeExactMethod(Address.class, getterMethodName).toString());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
