package kr.co.user.weding.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.user.weding.service.StoreService;
import kr.co.user.weding.service.TicketService;
import kr.co.user.weding.service.UserService;
import kr.co.user.weding.service.AdminService;
import select.spring.exquery.service.ExqueryService;

@Controller
@RequestMapping( value="data")
public class DataController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    HttpSession session;
	
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserService userService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private AdminService adminService;
	
	/* #################################################################################################################
	 * userService
	 */
	
	/**sasadfsdf테스트 ㅇㅇㅇㅇ 로그인하기 
	 * @param :"name : 이름 , phonNumber : 핸드폰번호"
	 * @get: ?name=&phonNumber=
	 */
	@RequestMapping( value="testInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> testInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return userService.testInfo(param);
	}
	
	/**
	 * 로그인하기 
	 * @param :"name : 이름 , phonNumber : 핸드폰번호"
	 * @get: ?name=&phonNumber=
	 */
	@RequestMapping( value="getUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getUserInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		
		
		return userService.getUserInfo(param);
	}
	
	/**
	 * 인증번호받기 
	 * @param :phonNumber : 핸드폰번호
	 * @get: ?phonNumber=
	 */
	@RequestMapping( value="getConfirmNumber", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getConfirmNumber(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return userService.getConfirmNumber(param);
	}

	/**
	 * 인증번호확인
	 * @param :"phonNumber:휴대폰번호, confirmNumber:인증번호"
	 * @get: ?phonNumber=&confirmNumber=
	 */
	@RequestMapping( value="getConfirm", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getConfirm(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return userService.getConfirm(param);
	}
	
	/**
	 * 약관조회 
	 * @param :termCode : 조회할약관코드 <clause, personal, location>
	 * @get: ?termCode=
	 */
	@RequestMapping( value="getTerms", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getTerms(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return userService.getTerms(param);
	}

	/**
	 * 가입완료 정보저장  
	 * @param :"name : 이름, phonNumber:휴대폰번호"
	 * @get: ?name=&phonNumber=
	 */
	@RequestMapping( value="setUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setUserInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return userService.setUserInfo(param);
	}
	
	/**
	 * 나의정보조회 
	 * @param :"userId : 사용자ID"
	 * @get: ?name=&phonNumber=
	 */
	@RequestMapping( value="getSettingUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getSettingUserInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return userService.getSettingUserInfo(param);
	}
	

	/**
	 * 사용자명 변경
	 */
	@RequestMapping( value="setUserName", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setUserName(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return userService.setUserName(param);
	}
	/* #################################################################################################################
	 * storeService
	 */
	/**
	 * 음식점목록 조회 
	 * @param :"userId:사용자ID
				lat:위도
				lon:경도
				showOrder:정렬순서
				<   maxWait:많은대기자순
				     minWait:적은대기자
				     rate:높은별점    >"
	 * @get: ?userId=&lat=&lon=&showOrder=
	 */
	@RequestMapping( value="getStoreList", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getStoreList(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getStoreList(param);
	}
	
	/**
	 * 음식점목록 조회 <대기팀, 소요시간만 조회>
	 * @param :none
	 */
	@RequestMapping( value="getStoreInfoList", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getStoreInfoList(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getStoreInfoList(param);
	}
	
	/**
	 * 리뷰 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@RequestMapping( value="getReview", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getReview(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getReview(param);
	}
	
	/**
	 * 상점상세정보 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@RequestMapping( value="getStoreDetail", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getStoreDetail(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getStoreDetail(param);
	}
	
	/**
	 * 상점 위도경도조회
	 * @param : none
	 */
	@RequestMapping( value="getStoreInfoMap", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getStoreInfoMap(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getStoreInfoMap(param);
	}
	
	/**
	 * 매장 이미지 조회
	 * @param : storeId
	 */
	@RequestMapping( value="getStoreImg", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getStoreImg(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getStoreImg(param);
	}
	
	/**
	 * 히스토리조회
	 * @param : storeId
	 */
	@RequestMapping( value="getHistory", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getHistory(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getHistory(param);
	}

	/**
	 * 상점상세정보 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@RequestMapping( value="getStoreDetailOne", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getStoreDetailOne(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return storeService.getStoreDetailOne(param);
	}
	
	/* #################################################################################################################
	 * ticketService
	 */
	
	/**
	 * 대기표받기
	 * @param :storeId : 음식점ID, userName : 대기자이름, userId : 대기자ID, wait : 인원수
	 * @get: ?storeId=&userName=&userId=&wait=
	 */
	@RequestMapping( value="setTicket", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setTicket(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.setTicket(param);
	}
	
	/**
	 * WEB 대기정보
	 * @param :i : 변환된 대기표ID
	 */
	@RequestMapping( value="getTicketInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getTicketInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.getTicketInfo(param);
	}
	
	/**
	 * 기다리는 정보가 있는지 조회 
	 * @param :userId:사용자ID
	 */
	@RequestMapping( value="getIsTicket", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getIsTicket(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.getIsTicket(param);
	}
	
	/**
	 * 마지막 기다린 정보 조회 
	 * @param :userId:사용자ID
	 */
	@RequestMapping( value="getLastTicketInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getLastTicketInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.getLastTicketInfo(param);
	}
	
	/**
	 * 대기정보 조회 
	 * @param :userId:사용자ID
	 */
	@RequestMapping( value="getWaitTicket", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getWaitTicket(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.getWaitTicket(param);
	}
	
	/**
	 * 미루기
	 * @param :userId:사용자ID
	 */
	@RequestMapping( value="setDelayTicket", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setDelayTicket(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.setDelayTicket(param);
	}
	
	/**
	 * 대기표 취소
	 * @param :userId:사용자ID
	 */
	@RequestMapping( value="setCancel", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setCancel(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return ticketService.setCancel(param);
	}
	
	/**
	 * 대기표 취소
	 * @param :userId:사용자ID
	 */
	@RequestMapping( value="setCancelByAdmin", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setCancelByAdmin(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return ticketService.setCancelByAdmin(param);
	}

	/**
	 * 리뷰등록
	 */
	@RequestMapping( value="setReview", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setReview(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.setReview(param);
	}
	/* #################################################################################################################
	 * adminService
	 */

	/**
	 * 관리자 로그인 
	 */
	@RequestMapping( value="getLogin", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getLogin(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.getLogin(param);
	}
	
	/**
	 * 매장상태조회 
	 */
	@RequestMapping( value="getStoreState", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getStoreState(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.getStoreState(param);
	}
	
	/**
	 * 영업 시작/중지 
	 */
	@RequestMapping( value="setStoreState", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setStoreState(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.setStoreState(param);
	}
	
	/**
	 * 대기정보 
	 */
	@RequestMapping( value="getStoreWait", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getStoreWait(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.getStoreWait(param);
	}
	

	/**
	 * 입장완료처리
	 */
	@RequestMapping( value="setWaitEnd", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setWaitEnd(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.setWaitEnd(param);
	}
	
	/**
	 * 패스콜
	 */
	@RequestMapping( value="setPassCall", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setPassCall(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.setPassCall(param);
	}
	/**
	 * 미리콜 
	 */
	@RequestMapping( value="setMiriCall", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setMiriCall(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.setMiriCall(param);
	}
	/**
	 * 입장콜 
	 */
	@RequestMapping( value="setEnterCall", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setEnterCall(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.setEnterCall(param);
	}
	/**
	 * 관리자매장정보조회 
	 */
	@RequestMapping( value="getAdminStoreInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getAdminStoreInfo(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.getAdminStoreInfo(param);
	}
	

	/**
	 * 공지사항 조회
	 */
	@RequestMapping( value="getNotice", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<HashMap<String, Object>> getNotice(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return adminService.getNotice(param);
	}
	
	/**
	 * 대기표받기<관리자>
	 */
	@RequestMapping( value="setTicketByAdmin", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setTicketByAdmin(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		return ticketService.setTicketByAdmin(param);
	}
	
	
	/* #################################################################################################################
	 * tablet Service
	 */
	
	/**
	 * 상점정보 조회
	 */
	@RequestMapping( value="getTabletInfo", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getStoreInfoByTablet(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		System.out.println(param);
		param.put("storeId", session.getAttribute("ssStoreId"));
		return storeService.getStoreDetailOneByTablet(param);
	}
	
	/**
	 * 대기표받기<테블릿>
	 */
	@RequestMapping( value="setTicketByTablet", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> setTicketByTablet(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		param.put("storeId", session.getAttribute("ssStoreId"));
//		param.put("name", "방문객");
		return ticketService.setTicketByAdmin(param);
	}
	
	/**
	 * 옵션이 있는지 체크 <일반>
	 */
	@RequestMapping( value="getIsExistOptionByTablet", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getIsExistOptionByTablet(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		param.put("storeId", session.getAttribute("ssStoreId"));
//		param.put("name", "방문객");
		return ticketService.getIsExistOption(param);
	}
	
	/**
	 * 옵션이 있는지 체크 <테블릿>
	 */
	@RequestMapping( value="getIsExistOption", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public HashMap<String, Object> getIsExistOption(@RequestParam HashMap<String, Object> param,  ModelMap model) throws Exception {
		return ticketService.getIsExistOption(param);
	}
}
