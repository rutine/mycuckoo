package com.mycuckoo.aop;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 功能说明: TODO(这里用一句话描述这个类的作用)
 * 
 * @author rutine
 * @time Sep 23, 2014 9:00:07 PM
 * @version 2.0.0
 */
@Component
@Aspect
public class SpringAOPTest {
	
	@Around("execution(* com.mycuckoo.aop.*.*(..))")
	public void interspector() {
		System.out.println("hi, good night...");
	}
}
