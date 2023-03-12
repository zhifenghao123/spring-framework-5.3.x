
package com.hao.main;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;


public class xmlBeanFactoryMain {
	public static void main(String[] args) {
		@SuppressWarnings({"unchecked", "deprecation"})
		BeanFactory xmlBeanFactory = new org.springframework.beans.factory.xml.XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beanFactoryTest.xml");
	}
}
