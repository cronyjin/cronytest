package com.daehanins.common.aspect;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

public class UserInfoParamAdvice implements MethodInterceptor{

	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpSession session;
	
	@SuppressWarnings("unchecked")
	@Override
	public final Object invoke(MethodInvocation invocation) throws Throwable {

		Annotation[][] aaa = invocation.getMethod().getParameterAnnotations();

		int argIdx = 0;
		for(Annotation[] aa : aaa){
			for(Annotation a : aa){
				// 실행될 메서드에서 @RequestParam 유형을 검색 					
				if(a instanceof RequestParam){
					Object argument = invocation.getArguments()[argIdx];
					
					if(argument instanceof Map == true && session.getAttribute("ssUserId") != null){
						((Map<String,Object>)argument).put("ssUserId", 		session.getAttribute("ssUserId"));
						((Map<String,Object>)argument).put("ssUserName",  	session.getAttribute("ssUserName"));
						((Map<String,Object>)argument).put("ssLoginType",  	session.getAttribute("ssLoginType"));
					}
				}
			}
			argIdx++;
		}		
		//return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
		return invocation.proceed();
	}

}
