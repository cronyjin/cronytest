package kr.co.admin.wating.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.daehanins.common.NbpmsConst;

import kr.co.user.weding.service.CommonService;
import select.spring.exquery.service.ExqueryService;
import select.spring.util.ApplicationProperty;

/**
 * Handles requests for the application home page.
 * 커밋테스트232255
 */
@Controller
public class AdminMenuController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	   HttpSession session;

	
	//*====================== tablet  ====================*//
	/**
	 * tablet web
	 */
	@RequestMapping(value = "/tablet" , method = {RequestMethod.GET, RequestMethod.POST})
	public String tablet(@RequestParam HashMap<String, Object> param, Locale locale, ModelMap model) {
		return "m:ipad";
	}
		
		
	//*====================== 메인 프로그램 ====================*//
	
	
	/**
	 * 메인
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "muMain")
	public String main(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.debug( "/muMain started! ===========>" +  param );
		log.debug( "/muMain started! ===========>" +  model );
		
//		commonService.setMenuConfigValue("muMain", model);
		
		return "f:muMain";
	}

	/**
	 * 일별대기자통계
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "muDaylyAnal")
	public String muDaylyAnal(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.debug( "/muDaylyAnal started! ===========>" +  param );
		
		return "f:muDaylyAnal";
	}

	//*====================== 테스트 프로그램 ====================*//
	
	/**
	 * 팝업 테스트
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "testPop")
	public String testPop(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.debug( "/testPop started! ===========>" +  param );
		return "f:testPop";
	}
	
}
