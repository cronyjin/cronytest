package kr.co.user.weding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.user.weding.service.AdminService;
import kr.co.user.weding.service.MessageService;
import kr.co.user.weding.service.TicketService;
import select.spring.exquery.service.ExqueryService;

@Service("adminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AdminServiceImpl implements AdminService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;
	@Autowired
	private MessageService messageService;

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private HttpSession session;

	

	/**
	 * 관리자 로그인
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getLogin(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
			
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		
		//존재하는 사용자 인지 조회
		checkMap = exqueryService.selectOne("nse.weAdmin.selectAdminId", param);
		
		if(checkMap == null){
			resultMap.put("result", false);
			resultMap.put("reason", "존재하지 않는 아이디 이거나 암호가 다릅니다.");
		} else {

			resultMap.put("result", true);
			resultMap.put("reason", "로그인 성공");
			resultMap.put("storeId", checkMap.get("storeId"));
		}
		
		return resultMap;
	}
	/**
	 * 매장 시작/중지
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setStoreState(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		
		String state = param.get("state").toString();
		state = state.toUpperCase();
		param.put("change", state);
		
		//존재하는 사용자 인지 조회
		exqueryService.update("nse.weAdmin.updateStoreState", param);
		
		resultMap.put("result", true);
		resultMap.put("reason", "변경되었습니다. ");
		
		return resultMap;
	}
	/**
	 * 매장 상태조회
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getStoreState(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		//존재하는 사용자 인지 조회
		resultMap = exqueryService.selectOne("nse.weAdmin.selectStoreState", param);
		
		return resultMap;
	}
	
	/**
	 * 대기정보
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getStoreWait(HashMap<String, Object> param) throws Exception {
		//return Object
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		//존재하는 사용자 인지 조회
		resultList = exqueryService.selectList("nse.weAdmin.selectStoreWait", param);
		
		return resultList;
	}
	
	/**
	 * 입장완료처리
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setWaitEnd(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		// 대기표가 있는지 조회 
		aMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
		if(aMap != null){//대기표있음
			
			//WE_TICKET_SUMMARY 정보 변경 
			exqueryService.update("nse.weStore.updateTicketInfoEnd", param);
			//표시순서를 다시 정렬한다.
			//myId 보다 큰것들 -1
			exqueryService.update("nse.weStore.setOrderChange2", aMap);
			//WE_TICKET 정보 삭제
			exqueryService.delete("nse.weStore.deleteTicket", param);
			//WE_IDCODE 정보삭제
			exqueryService.delete("nse.weStore.deleteTicketIdCode", param);
			
			
			int myId = Integer.parseInt(aMap.get("orderId").toString());
			
			HashMap<String, Object> tMap = new HashMap<String, Object>();
			tMap = exqueryService.selectOne("nse.weStore.selectCallCnt", aMap);
			int callCnt = Integer.parseInt(tMap.get("callCnt").toString());
			//요청 순서가 5미만일경우만 
			if(myId <= callCnt){
				//메세지처리 (순서변경에 따른 처리)
				messageService.setMessageBy5(aMap.get("storeId").toString());
			}
			
			
			resultMap.put("result", true);
			resultMap.put("reason", "입장처리 되었습니다.");
			
		} else {
			resultMap.put("result", false);
			resultMap.put("reason", "처리가능한 대기표를 가지고 있지 않습니다.");
		}
		
		return resultMap;
		
	}
	
	/**
	 * 패스콜
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setPassCall(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		// 대기표가 있는지 조회 
		aMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
		
		if(aMap != null){//대기표있음
			
			//대기표 미루기 처리 
			HashMap<String, Object> tMap = ticketService.setDelayTicket(param);
			String isResult = tMap.get("result").toString();
			if(isResult.equals("true")){
				
				HashMap<String, Object> cMap = exqueryService.selectOne("nse.weUser.getSettingUserInfo", param);
				String userPhone = cMap.get("mobileNo").toString();
				
				HashMap<String, Object> store2Map = exqueryService.selectOne("nse.weStore.selectStoreOne", aMap);
				String storeName = store2Map.get("storeName").toString();//업소명
				//난수 조회
				HashMap<String, Object> codeMap = exqueryService.selectOne("nse.weStore.selectUserCode", param);
				String urlCode = codeMap.get("code").toString();//난수7자리 
				
				String msg = "[웨이팅]\""+storeName+"\"noshow 순서변경 되었습니다. 확인:wdg.kr/"+urlCode;
				

				param.put("msg", msg);
				param.put("phonNumber", userPhone);
				//메세지 발송 
				exqueryService.insert("nse.weMessage.insertMessageTran", param);
				
				resultMap.put("result", true);
				resultMap.put("reason", "패스콜 처리되었습니다.");
				
			} else {
				//이미 미루기를 사용하여 미루기를 할 수 없음 
				resultMap.put("result", false);
				resultMap.put("reason", "이미 패스콜 처리되어 더이상 패스콜 처리할 수 없습니다.");
			}
			
			
			
		} else {
			resultMap.put("result", false);
			resultMap.put("reason", "처리가능한 대기표를 가지고 있지 않습니다.");
		}
		
		return resultMap;
		
	}
	
	/**
	 * 미리콜
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setMiriCall(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		// 대기표가 있는지 조회 
		aMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
		
		if(aMap != null){//대기표있음
			
			//미리콜 처리 
			HashMap<String, Object> cMap = exqueryService.selectOne("nse.weUser.getSettingUserInfo", param);
			String userPhone = cMap.get("mobileNo").toString();
			
			HashMap<String, Object> store2Map = exqueryService.selectOne("nse.weStore.selectStoreOne", aMap);
			String storeName = store2Map.get("storeName").toString();//업소명
			//난수 조회
			HashMap<String, Object> codeMap = exqueryService.selectOne("nse.weStore.selectUserCode", param);
			String urlCode = codeMap.get("code").toString();//난수7자리 
			
			String msg = "[웨이팅]\""+storeName+"\"에 요청하신 자리가 바로 준비될 예정이니 근처로 오시기 바랍니다.";

			param.put("msg", msg);
			param.put("phonNumber", userPhone);
			//메세지 발송 
			exqueryService.insert("nse.weMessage.insertMessageTran", param);
			
			resultMap.put("result", true);
			resultMap.put("reason", "미리콜 처리되었습니다.");
				
			
			
			
		} else {
			resultMap.put("result", false);
			resultMap.put("reason", "처리가능한 대기표를 가지고 있지 않습니다.");
		}
		
		return resultMap;
		
	}
	/**
	 * 입장콜
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setEnterCall(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		// 대기표가 있는지 조회 
		aMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
		
		if(aMap != null){//대기표있음
			
			//입장콜 처리 
			HashMap<String, Object> cMap = exqueryService.selectOne("nse.weUser.getSettingUserInfo", param);
			String userPhone = cMap.get("mobileNo").toString();
			
			HashMap<String, Object> store2Map = exqueryService.selectOne("nse.weStore.selectStoreOne", aMap);
			String storeName = store2Map.get("storeName").toString();//업소명
			//난수 조회
			HashMap<String, Object> codeMap = exqueryService.selectOne("nse.weStore.selectUserCode", param);
			String urlCode = codeMap.get("code").toString();//난수7자리 
			
			String msg = "[웨이팅]\""+storeName+"\"에 입장 순서가 되었습니다. 가게로 오셔서 직원의 안내를 받으시기 바랍니다.";
			
			                                                                   

			param.put("msg", msg);
			param.put("phonNumber", userPhone);
			//메세지 발송 
			exqueryService.insert("nse.weMessage.insertMessageTran", param);
			
			resultMap.put("result", true);
			resultMap.put("reason", "입장콜 처리되었습니다.");
			
			
			
		} else {
			resultMap.put("result", false);
			resultMap.put("reason", "처리가능한 대기표를 가지고 있지 않습니다.");
		}
		
		return resultMap;
		
	}
	
	/**
	 * 관리자매장정보조회
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getAdminStoreInfo(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		resultMap = exqueryService.selectOne("nse.weAdmin.selectAdminStoreInfo", param);
		
		if(resultMap ==null){
			resultMap = new HashMap<String, Object>();
			resultMap.put("result", "존재하지 않는 ID입니다. ");
		}
		
		
		return resultMap;
		
	}

	/**
	 * 공지사항조회
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getNotice(HashMap<String, Object> param) throws Exception {
		//return Object
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		//존재하는 사용자 인지 조회
		resultList = exqueryService.selectList("nse.weAdmin.selectNotice", param);
		
		return resultList;
	}
}
