package kr.co.user.weding.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import kr.co.user.weding.service.CommonService;
import kr.co.user.weding.service.StoreService;
import select.spring.exquery.service.ExqueryService;

@Controller
@RequestMapping( value="common")
public class CommonController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StoreService storeService;

	@RequestMapping( value="startMenu", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, Object> startMenu(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		log.debug( "startMenu =============>" +  param );
		return commonService.startMenu(param);
    }
	
	/*
	 * @대기표받기
	 * param:{음식점ID:storeId, 대기자이름:userName, 대기자ID:userId, 인원수:wait}
	 * get:?storeId=&userName=&userId=&wait=
	 */
	@RequestMapping( value="getTerms", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ArrayList<HashMap<String, Object>> getTerms(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		System.out.println(param);
		
		List<HashMap<String, Object>> bikedanglist = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
	
		//subject, name
		bikedanglist = exqueryService.selectList("nse.bpUser.selectStore", param);
		
		
		return (ArrayList<HashMap<String, Object>>) bikedanglist;
	}
	
	
}
