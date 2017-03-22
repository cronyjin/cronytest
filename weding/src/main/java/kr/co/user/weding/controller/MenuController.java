package kr.co.user.weding.controller;

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
 */
@Controller
public class MenuController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	   HttpSession session;

	//*====================== ticket  ====================*//
	/**
	 * ticket web
	 */
//	@RequestMapping(value = "/t" , method = {RequestMethod.GET, RequestMethod.POST})
//	public String ticket(@RequestParam HashMap<String, Object> param, Locale locale, ModelMap model) {
//		model.put("i", param.get("i"));
//		return "t:ticket";
//	}
			
	@RequestMapping(value = "/{someID}" , method = {RequestMethod.GET, RequestMethod.POST})
	public String test(@PathVariable(value="someID") final String uuid, Locale locale, ModelMap model) {
		model.put("i", uuid);
		return "t:ticket";
	}
	
	/**
	 * ticket web
	 */
	@RequestMapping(value = "/tnone" , method = {RequestMethod.GET, RequestMethod.POST})
	public String tnone(@RequestParam HashMap<String, Object> param, Locale locale, ModelMap model) {
		
		return "r:ticket_null";
	}
	
	
}
