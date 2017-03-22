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

import kr.co.user.weding.service.UserService;
import select.spring.exquery.service.ExqueryService;

@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl implements UserService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private HttpSession session;

	
	/**
	 * dddddd
	 * @param :"name : 이름 , phonNumber : 핸드폰번호"
	 * @get: ?name=&phonNumber=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> testInfo(HashMap<String, Object> param) throws Exception {
		
		
		List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
	
		l = exqueryService.selectList("nse.weStore.selectUser", param);
		
		
		return l;
	}
	/**
	 * 로그인하기 
	 * @param :"name : 이름 , phonNumber : 핸드폰번호"
	 * @get: ?name=&phonNumber=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getUserInfo(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		
		//핸드폰번호가 이미 등록되어있는지 조회 
		checkMap = exqueryService.selectOne("nse.weUser.selectUserId", param);
		
		if(checkMap == null){
			//사용자정보 없음 
			resultMap.put("joinYn", "false");
			resultMap.put("reason", "가입된 사용자가 아닙니다.");
		} else {
			//사용자 정보 있음.
			resultMap.put("joinYn", "true");
			resultMap.put("userId", checkMap.get("userId"));
			resultMap.put("reason", "가입된사용자입니다.");
		}
		
		return resultMap;
	}
	
	/**
	 * 인증번호받기 
	 * @param :phonNumber : 핸드폰번호
	 * @get: ?phonNumber=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getConfirmNumber(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		//###########임시 인증번호 고정 [9999]############
		String tempNumber = param.get("phonNumber").toString();
		if(tempNumber.equals("01022313380")){
			String msg = "[웨이팅]test 인증번호는 [9999] 입니다.";
			
			param.put("confirmNo", "9999");
			param.put("msg", msg);
			
			//메세지 발송 
			exqueryService.insert("nse.weMessage.insertMessageTran", param);
			//발송정보를 저장한다.
			exqueryService.insert("nse.weUser.insertSendConfirm", param);
			
			resultMap.put("result", "true");
			resultMap.put("reason", "인증번호가 발송되었습니다.");
		} else {
			
			
			//당일 발송한 횟수가 5회를 넘지 않는지 체크한다.
			checkMap = exqueryService.selectOne("nse.weUser.selectSendComfirmCount", param);
			
			int sendCnt = Integer.parseInt(checkMap.get("cnt").toString());
			
			//발송횟수가 5보다 작다면.
			if(sendCnt < 5){
				
				Random random = new Random();
		        
				int result = random.nextInt(10000)+1000;
				 
				if(result>10000){
				    result = result - 1000;
				}
				String msg = "[웨이팅] 로그인 인증번호는 ["+result+"] 입니다.";
				
				param.put("confirmNo", result);
				param.put("msg", msg);
				
				
				//메세지 발송 
				exqueryService.insert("nse.weMessage.insertMessageTran", param);
				//발송정보를 저장한다.
				exqueryService.insert("nse.weUser.insertSendConfirm", param);
				
				resultMap.put("result", "true");
				resultMap.put("reason", "인증번호가 발송되었습니다.");
			} else {
				resultMap.put("result", "false");
				resultMap.put("reason", "일일발송 가능한 횟수는 5회 입니다.");
			}
			
			
		}
		
		return resultMap;
	}
	
	/**
	 * 인증번호확인
	 * @param :"phonNumber:휴대폰번호, confirmNumber:인증번호"
	 * @get: ?phonNumber=&confirmNumber=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getConfirm(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		//발송한 인증번호를 조회한다.
		checkMap = exqueryService.selectOne("nse.weUser.selectComfirmNumber", param);
		
		if(checkMap == null){
			resultMap.put("result", "false");
			resultMap.put("reason", "발송된 인증번호가 존재하지 않습니다.");
		} else {
			int secondCount = Integer.parseInt(checkMap.get("secondCount").toString());
			//발송된 시간이 180초가 지났는지 체크한다.
			if(secondCount > 180){
				resultMap.put("result", "false");
				resultMap.put("reason", "인증번호는 180초 이내에 입력하여야 합니다. 다시 시도하여 주세요.");
			} else {

				
				//발송된 번호와 입력한 번호가 맞는지 체크한다.
				String confirmNo = checkMap.get("confirmNo").toString();
				String confirmNumber = param.get("confirmNumber").toString();
				if(confirmNo.equals(confirmNumber)){
					//인증성공
					resultMap.put("result", "true");
					resultMap.put("reason", "인증에 성공하였습니다.");
					
//					setUserInfo(param);
				} else {
					//인증번호다름
					resultMap.put("result", "false");
					resultMap.put("reason", "인증번호가 다릅니다.");
				}
			}
		}
		
		
		
		return resultMap;
	}
	
	/**
	 * 약관조회 
	 * @param :termCode : 조회할약관코드 <clause, personal, location>
	 * @get: ?termCode=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getTerms(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		//발송한 인증번호를 조회한다.
		checkMap = exqueryService.selectOne("nse.weUser.selectTerms", param);
		
		if(checkMap == null){
			resultMap.put("result", "false");
			resultMap.put("detail", "조회된 메세지가 존재하지 않습니다.");
		} else {
			resultMap.put("result", "false");
			resultMap.put("detail", checkMap.get("etcMsg"));
		}
		
		
		
		return resultMap;
	}
	
	/**
	 * 가입완료 정보저장  
	 * @param :"name : 이름, phonNumber:휴대폰번호"
	 * @get: ?name=&phonNumber=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setUserInfo(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		//오늘 인증번호를 발송한 내역이있는지 조회한다.
		checkMap = exqueryService.selectOne("nse.weUser.selectComfirmNumber", param);
		if(checkMap == null){
			//인증번호 발송한 내역없음
			resultMap.put("result", "false");
			resultMap.put("reason", "인증번호발송내역이 존재하지 않습니다.");
		} else {
			//인증번호가 3600초 이내에 있는지 조회 
			int secondCount = Integer.parseInt(checkMap.get("secondCount").toString());
			if(secondCount > 3600){
				//인증번호 발급한지 한시간이 지났음. 
				resultMap.put("result", "false");
				resultMap.put("reason", "인증번호 발급시간이 초과되었습니다.");
			} else {
				//다시한번 가입된 내역이 없는지 확인한다.
				checkMap.clear();
				checkMap = exqueryService.selectOne("nse.weUser.selectUserId", param);
				if(checkMap == null){
					//유저정보 저장 
					exqueryService.insert("nse.weUser.insertUserInfo", param);
					checkMap = exqueryService.selectOne("nse.weUser.selectUserId", param);
					resultMap.put("result", "true");
					resultMap.put("userId", checkMap.get("userId"));
					resultMap.put("reason", "가입이 완료되었습니다.");
				} else {
					//이미등록된 사용자가 존재함 
					resultMap.put("result", "false");
					resultMap.put("reason", "이미 등록된 사용자가 존재합니다.");
				}
			}
		}
		
		
		return resultMap;
	}
	
	/**
	 * 나의정보조회 
	 * @param :"userId : 사용자ID"
	 * @get: ?name=&phonNumber=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getSettingUserInfo(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		resultMap = exqueryService.selectOne("nse.weUser.getSettingUserInfo", param);

		
		
		return resultMap;
	}
	
	/**
	 * 사용자명 변경 
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setUserName(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		exqueryService.update("nse.weUser.setUserName", param);
		
		resultMap.put("result", "true");
		resultMap.put("reason", "변경 되었습니다.");
		
		return resultMap;
	}
	
}
