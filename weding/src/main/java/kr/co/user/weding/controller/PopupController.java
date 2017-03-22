package kr.co.user.weding.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import select.spring.exquery.service.ExqueryService;
import select.spring.util.ApplicationProperty;

@Controller
public class PopupController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	   HttpSession session;

	
	//*==================팝업======================*//
	
	/**
	 * 공통코드 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "testPop1", method = RequestMethod.POST)
	public String testPop1(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> testPop1 started!");
		
		return "p:testPop1";
	}	
	
	/**
	 * 일괄입력 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popExcelUpload", method = RequestMethod.POST)
	public String popExcelUpload(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popExcelUpload started!");
		
		return "p:popExcelUpload";
	}	
	
	/**
	 * 메뉴위치선택 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popAuthMenuPositionCd", method = RequestMethod.POST)
	public String popAuthMenuPositionCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popAuthMenuPositionCd started!");
		
		return "p:popAuthMenuPositionCd";
	}	
	/**
	 * 메뉴위치선택 팝업(메뉴관리에서만 사용)
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popAuthMenuPositionAll", method = RequestMethod.POST)
	public String popAuthMenuPositionAll(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popAuthMenuPositionAll started!");
		
		return "p:popAuthMenuPositionAll";
	}	
	
	/**
	 * 메뉴선택 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popAuthMenuCd", method = RequestMethod.POST)
	public String popAuthMenuCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popAuthMenuCd started!");
		
		return "p:popAuthMenuCd";
	}	
	
	/**
	 * CTQ위치선택 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popCtqPositionCd", method = RequestMethod.POST)
	public String popCtqPositionCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popCtqPositionCd started!");
		
		return "p:popCtqPositionCd";
	}	
	
	/**
	 * FTA위치선택 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popFtaPositionCd", method = RequestMethod.POST)
	public String popFtaPositionCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popFtaPositionCd started!");
		
		return "p:popFtaPositionCd";
	}	
	
	/**
	 * 역할메뉴 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popAuthRoleCd", method = RequestMethod.POST)
	public String popAuthRoleCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popAuthRoleCd started!");
		
		return "p:popAuthRoleCd";
	}	
	
	/**
	 * 사용자 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popAuthUserCd", method = RequestMethod.POST)
	public String popAuthUserCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popAuthUserCd started!");
		
		return "p:popAuthUserCd";
	}	
	
	/**
	 * CTP선택 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popCtp", method = RequestMethod.POST)
	public String popCtp(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popCtp started!");
		
		return "p:popCtp";
	}	
	
	/**
	 * 생산공정코드 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popTreeCd", method = RequestMethod.POST)
	public String popTreeCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popTreeCd started!");
		
		return "p:popTreeCd";
	}	
	
	/**
	 * 생산공정코드(위치선택) 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popTreePositionCd", method = RequestMethod.POST)
	public String popTreePositionCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popTreePositionCd started!");
		
		return "p:popTreePositionCd";
	}
	
	/**
	 * 설비코드 선택 팝업
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popProcEquipCd", method = RequestMethod.POST)
	public String popProcEquipCd(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popProcEquipCd started!");
		
		return "p:popProcEquipCd";
	}	
	
	/**
	 * 크게보기
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popImageView", method = RequestMethod.POST)
	public String popImageView(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popImageView started!");
		model.put("filePath", ApplicationProperty.get("upload.viewPath"));
		return "p:popImageView";
	}	
	
	/**
	 * 크게보기 (리스트형)
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popImageListView", method = RequestMethod.POST)
	public String popImageListView(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popImageListView started!");
		model.put("filePath", ApplicationProperty.get("upload.viewPath"));
		return "p:popImageListView";
	}	
	
	/**
	 * 바코드출력 미리보기 
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "popBacodView", method = RequestMethod.POST)
	public String popBacodView(@RequestParam Map<String, Object> param,  ModelMap model) {
		log.info("=====> popBacodView started!");
		return "p:popBacodView";
	}
	
}
