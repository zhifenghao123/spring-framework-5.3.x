
package com.hao.dao;

import org.springframework.stereotype.Component;

import com.hao.annotation.DataAccessLock;
import com.hao.entity.User;

@Component
public class UserDao {

	@DataAccessLock
	public int inserUser(User user) {
		System.out.println("UserDao#insertUser(user)");
		return 1;
	}

	public User queryUserByAccountName(String accountName) {
		System.out.println("UserDao#queryUserByAccountName(accountName)");
		return null;
	}
	public User queryUserByAccountNameAndPassword(String accountName,String password) {
		System.out.println("UserDao#queryUserByAccountNameAndPassword(accountName,password)");
		return null;
	}

	public void queryUser() {
		User user = new User();

		user.setUserName("haozhifeng");
		user.setPassword("123456");
		user.setEmail("111@qq.com");

		//System.out.println(user);
		System.out.println("UserDao#queryUser()");

	}


}
