package com.daehanins.common.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import select.spring.exquery.service.ExqueryService;

public class UrlParamAdvice implements MethodInterceptor{

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpSession session;
	
	@Autowired
	private ExqueryService exqueryService;
	
	@SuppressWarnings("unchecked")
	@Override
	public final Object invoke(MethodInvocation invocation) throws Throwable {

		HashMap<String, Object> aMap = new HashMap<String, Object>();
		
		String menuUrl = request.getServletPath();
		String menuTab = request.getParameter("menuTab");
		
		// html body tag에 class를 삽입하기 위함
		String langStyle = "";
//		if(session.getAttribute("ssUserLang") != null){
//			langStyle = session.getAttribute("ssUserLang").toString();
//			if(langStyle.equals("ko")){
//				langStyle = "";
//				aMap.put("ssSqlLang", 	"");
//			} else {
//				// 중국어인 경우에는 반영됨.
//				langStyle = "ch";
//				aMap.put("ssSqlLang", 	"_A");
//			}
//		}
		
		if (menuUrl != null && menuUrl.length() > 0) {
			menuUrl = menuUrl.substring(1);
			aMap.put("menuUrl", 	menuUrl);
		}

		String requestUrl = request.getRequestURL().toString();
		String rootPath = requestUrl;
		int pos = requestUrl.lastIndexOf(menuUrl);
		if (pos > 0) {
			rootPath = requestUrl.substring(0, pos);
		}

		//로그인한 사용자 
		String userId = session.getAttribute("ssAdminId").toString();
		aMap.put("ssUserId", userId);
				
		// 메서드의 argument 타입 ModelMap에 값을 추가한다. 
		Object[] invoArgs = invocation.getArguments();

		for(int i = 0; i < invoArgs.length; i++){
			if (invoArgs[i] instanceof ModelMap) {
//				if(menuUrl != null){
//					((Map<String,Object>)invoArgs[i]).put("ssMenuNavi", 	exqueryService.selectList("nse.bpProgram.selectMenuNavi", aMap));	
//				}
				((Map<String,Object>)invoArgs[i]).put("ssMenuUrl", 		menuUrl);	
				((Map<String,Object>)invoArgs[i]).put("ssMenuTab", 		menuTab);	
				((Map<String,Object>)invoArgs[i]).put("ssContextPath", 	rootPath);
				((Map<String,Object>)invoArgs[i]).put("ssLangStyle", 	langStyle);

				//화면 스타일을 조회한다.
//				HashMap<String, Object> pMap = exqueryService.selectOne("nse.bpProgram.selectUserStyle", aMap);
//				if(pMap==null){
				HashMap<String, Object> pMap = new HashMap<String, Object>();
					pMap.put("styleType", "BLACK");
//				}
				String userStyle = (String)pMap.get("styleType");
				userStyle = userStyle.toLowerCase();
				((Map<String,Object>)invoArgs[i]).put("ssUserStyle", userStyle);	     
			}
		}

		// 메서드의 Annotation이 RequestParam인 argument에 값을 추가한다. 
		/*
		Annotation[][] aaa = invocation.getMethod().getParameterAnnotations();
		int argIdx = 0;
		for(Annotation[] aa : aaa){
			for(Annotation a : aa){
				if(a instanceof RequestParam){
					Object argument = invocation.getArguments()[argIdx];
					((Map<String,Object>)argument).put("ssContextPath", rootPath);
					break;
				}
			}
			argIdx++;
		}		
		*/
		
		//return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
		return invocation.proceed();
	
	}

}
