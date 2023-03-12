
package com.hao.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("u")
public class UserService implements BaseService {
	@Autowired
	private AccountService accountService;

	public UserService (){
		System.out.println("Constructor from UserService");
	}
	@Override
	public void query() {
		System.out.println("query User-------------------");
	}

	@PostConstruct
	public void initCallback(){
		System.out.println("init callback for UserService....");
	}
}
