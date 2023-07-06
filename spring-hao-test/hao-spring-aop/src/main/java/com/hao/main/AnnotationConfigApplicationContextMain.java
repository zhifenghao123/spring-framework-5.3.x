
package com.hao.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hao.config.AppConfig;
import com.hao.service.BaseService;
import com.hao.service.UserService;

public class AnnotationConfigApplicationContextMain {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext(AppConfig.class);

		/*AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext();
		AbstractAutowireCapableBeanFactory abstractAutowireCapableBeanFactory =
				(AbstractAutowireCapableBeanFactory)annotationConfigApplicationContext.getBeanFactory();
		abstractAutowireCapableBeanFactory.setAllowCircularReferences(false);

		annotationConfigApplicationContext.register(AppConfig.class);
		annotationConfigApplicationContext.refresh();*/

		/*annotationConfigApplicationContext.getBean(UserDao.class).inserUser(new User());
		annotationConfigApplicationContext.getBean(UserDao.class).queryUserByAccountName("hao");
		annotationConfigApplicationContext.getBean(UserDao.class).queryUserByAccountNameAndPassword("hao","123456");
		annotationConfigApplicationContext.getBean(UserDao.class).queryUser();
		System.out.println("------------");*/


		BaseService baseService = (BaseService) annotationConfigApplicationContext.getBean("u");
		baseService.query();
		System.out.println(baseService instanceof UserService);


	}

}
