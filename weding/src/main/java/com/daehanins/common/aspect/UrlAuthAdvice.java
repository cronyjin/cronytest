package com.daehanins.common.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//import kr.co.nse.weding.service.CommonAuthService;

public class UrlAuthAdvice extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(UrlAuthAdvice.class);
	
//	@Inject
//	@Named("commonAuthService")
//	private CommonAuthService commonAuthService;

	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler){
		
		String url = request.getRequestURI();
		String[] parseUrl = url.split("/");
		String menuUrl = parseUrl[parseUrl.length - 1];
		
		menuUrl = menuUrl.split(";")[0];	// JSessionId 붙는 경우 제거용 
		
		String rootPath   =  request.getContextPath();
		
		HttpSession session  =  request.getSession(false);
		
		try {
				if (session == null || session.getAttribute("ssAdminId") == null) {
					logger.debug("=== redirected by UrlAuthAdvice :: has not logged in !! ===");
					response.sendRedirect(rootPath + "/index");
					return false;
				}
	
//				String userId = (String)session.getAttribute("ssUserId");
//				if (!commonAuthService.isValidAccess(userId, menuUrl)) {
//					logger.debug("=== redirected by UrlAuthAdvice :: not authorized access!! ===");
//					//  response.sendRedirect(rootPath + "/index");
//					response.sendRedirect(rootPath + "/error");
//					return false;
//				}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
}
