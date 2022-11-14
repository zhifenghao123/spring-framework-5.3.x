
package com.hao.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HaoAspect {

	@Pointcut("execution(* com.hao.service..*.*(..))")
	public void servicePointCut(){
	}

	@Pointcut("execution(* com.hao.dao..*.*(..))")
	public void daoPointCut() {
	}

	@Pointcut("args(java.lang.String,java.lang.String)")
	public void daoPointCutArgs() {

	}

	@Pointcut("@annotation(com.hao.annotation.DataAccessLock)")
	public void daoPointCutDA() {

	}

	@Pointcut("execution(* com.hao.dao.*.*(..)) && !daoPointCutArgs() && !daoPointCutDA()")
	public void daoPointCutOperation() {

	}

	/*@Pointcut("this(com.hao.service.BaseService)")
	public void daoPointCutThis() {

	}*/
	/**
	 * 如果是class的话，需要{@link com.hao.config.AppConfig}中作如下设置：@EnableAspectJAutoProxy(proxyTargetClass = true)
	 */
	@Pointcut("this(com.hao.service.UserService)")
	public void daoPointCutThis() {

	}





	@Before("servicePointCut()")
	public void serviceAdvice() {
		System.out.println("aop_service: befor business logic");
	}

	@After("daoPointCut()")
	public void daoAdviceService() {
		System.out.println("aop_dao: After dao logic");
	}

	@After("daoPointCutArgs()")
	public void daoAdviceArgs() {
		System.out.println("aop_dao: After dao logic.,args");
	}

	@Around("daoPointCutDA()")
	public int daoAdviceDA(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		System.out.println("aop_dao:data access lock!!!-------start");
		int result = (Integer) proceedingJoinPoint.proceed();
		System.out.println((result + " rows was affected"));
		System.out.println("aop_dao:data access lock!!!------end");
		return result;
	}

	@After("daoPointCutOperation()")
	public void daoAdviceOperation() {
		System.out.println("aop_dao: aop operation");
	}

	@After("daoPointCutThis()")
	public void daoAdviceThis() {
		System.out.println("aop_dao: I was aspected by This");
	}
}
