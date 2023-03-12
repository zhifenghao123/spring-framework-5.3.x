
package com.hao.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("a")
public class AccountService {
	@Autowired
	private BaseService userService;
	//private UserService userService;

	@PostConstruct
	public void initCallback(){
		System.out.println("init callback for AccountService....");
	}
}
