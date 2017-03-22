package kr.co.user.weding.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import kr.co.user.weding.service.CommonService;
import select.spring.exquery.service.ExqueryService;

@Controller
public class LoginController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	   HttpSession session;
	
	@Autowired
	private CommonService commonService;
	
	private static final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";
	
	
		
	//*====================== index  ====================*//
	/**
	 * index
	 */
	@RequestMapping(value = "/index")
	public String index(Locale locale, ModelMap model) {
		
		log.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		
		model.put("serverTime", formattedDate );
		
		return "g:login";
	}
	
	
	//*====================== 로그인  ====================*//
	/**
	 * 로그인
	 */
	@RequestMapping(value = "/login", method=RequestMethod.POST)
	public String login(HttpServletRequest request,  HttpServletResponse response, ModelMap model) {
		
		log.info("/login started!");
		log.debug( "param =============>" +  model );

		Locale locale;
		locale = new Locale("ko", "KR");
		localeResolver.setLocale(request, response, locale);
		log.debug("locale ======>" + locale);
		
		boolean bool = true;

		HashMap<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("ssAdminId", 	request.getParameter("ssAdminId"));
		pMap.put("ssAdminPass", 	request.getParameter("ssAdminPass"));
		pMap.put("ssLoginType", 	request.getParameter("ssLoginType"));
		
		String clientIp = request.getHeader(HEADER_X_FORWARDED_FOR);
		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")){
		  clientIp = request.getRemoteAddr();
		}
		
		if( pMap.get("ssAdminPass") != null && pMap.get("ssAdminId") != null) {
			
			HashMap<String, Object> aMap = exqueryService.selectOne("nse.weAdmin.selectUserSession", pMap);
			if(aMap == null){
				bool = false;
			} else {
				if( !pMap.get("ssAdminPass").toString().equals( aMap.get("ssAdminPass").toString() ) ){
					bool = false;
				} else {
					
					// 로그인 성공 : 세션에 저장 -----------
					session.setAttribute("ssAdminId", 		aMap.get("ssAdminId").toString());
					session.setAttribute("ssAdminName", 	aMap.get("ssAdminName").toString());
					session.setAttribute("ssStoreId", 	aMap.get("storeId").toString());
					//--------------------------------
					
				}
			}
		} else {
			// url 주소에서 login.do 를 직접입력하고 들어왔을때..
			return "g:login";
		}
		if(!bool){
			model.put("loginCheck", "err");
			return "g:login";
		}
		
		
		//사용자별 초기화면을 조회한다.
		String userMainUrl = "";
		String loginType = request.getParameter("ssLoginType");
		System.out.println(loginType);
		if(loginType.equals("anal")){
			userMainUrl = "muMain";
		} else {
			userMainUrl = "tablet";
		}
		
		String mainUrl = "redirect:/" + userMainUrl;
		return mainUrl;
	}

	/**
	 * 로그아웃
	 */
	@RequestMapping(value = "logout")
	public String logout(@RequestParam Map<String, Object> param, ModelMap model) {
		
		log.info("/logout executed!");
		log.debug( "param =============>" +  param );
		
		if (session.getAttribute("ssAdminId") != null) {
			session.removeAttribute("ssAdminId");
			session.removeAttribute("ssAdminName");
			session.removeAttribute("ssStoreId");

			HashMap<String, Object> aMap = (HashMap<String,Object>)param;
		}
		
		return "redirect:/index";
	}

	@RequestMapping(value = "error")
	public String error(HttpServletRequest request, ModelMap model) {
		
		log.info("/error started!");
		return "e:error";
	}
		
	
}
