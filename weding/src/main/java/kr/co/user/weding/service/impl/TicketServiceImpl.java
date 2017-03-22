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

import kr.co.user.weding.service.TicketService;
import kr.co.user.weding.service.MessageService;
import select.spring.exquery.service.ExqueryService;

@Service("ticketService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TicketServiceImpl implements TicketService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private HttpSession session;

	

	/**
	 * 대기표받기
	 * @param :storeId : 음식점ID, userName : 대기자이름, userId : 대기자ID, wait : 인원수
	 * @get: ?storeId=&userName=&userId=&wait=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setTicket(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
			
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		
		//존재하는 사용자 인지 조회
		checkMap = exqueryService.selectOne("nse.weStore.selectExistUser", param);
		if(checkMap != null){
			//음식점이 영업하고 있는지 확인
			checkMap.clear();
			checkMap = exqueryService.selectOne("nse.weStore.selectStoreState", param);
			
			if(checkMap == null || !checkMap.get("openState").equals("OPEN")){
				//영업중이 아님.
				resultMap.put("result", "false");
				resultMap.put("reason", "해당 매장이 영업중이 아닙니다.");
			} else {
				//체크맵 클리어 
				checkMap.clear();
				//해당사용자가 이미 대기중인 대기표가 있는지 확인 
				checkMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
				
				if(checkMap != null){
					//이미 대기표가 있음 
					resultMap.put("result", "false");
//					if(param.get("engYn").equals("Y")){
//						resultMap.put("reason", "You are already in waiting list ");
//					} else {
						resultMap.put("reason", "이미 대기중인 대기표가 있습니다.");
//					}
				} else {
					//대기하기 정상 진행 
					
					//거리가 500M미만인지 체크 
//					int distance = 500;
//					if(){
						//WE_TICKET 저장하기 전에 대기순서를 정하여야 한다.
						checkMap = exqueryService.selectOne("nse.weStore.selectStoreTicketOrder", param);
						param.put("orderId", checkMap.get("orderId"));
						
						//WE_TICKET_SUMMARY 저장
						exqueryService.insert("nse.weStore.insertWeTicketSummary", param);
						//WE_TICKET 저장
						exqueryService.insert("nse.weStore.insertWeTicket", param);
						
						
						//code가 중복되지 않을때까지 반복
						boolean isCodeChk = true;
						while(isCodeChk){
							//코드생성
							Random rnd =new Random();
							StringBuffer buf =new StringBuffer();
							 
							for(int i=0;i<7;i++){
							    if(rnd.nextBoolean()){
							        buf.append((char)((int)(rnd.nextInt(26))+97));
							    }else{
							        buf.append((rnd.nextInt(10))); 
							    }
							}
							String code = buf.toString();
							boolean startsWith = code.startsWith("mu");
							param.put("code", code);
							//난수를 생성했는데 데이터베이스에 조회해서 중복되는지 체크
							checkMap.clear();
							checkMap = exqueryService.selectOne("nse.weStore.selectCheckCode", param);
							
							if(!startsWith && checkMap == null){
								isCodeChk = false;
								//WE_IDCODE 저장 
								exqueryService.insert("nse.weStore.insertWeIdcode", param);
							}
						}
						
						
						//메세지발송 처리
						//사용자 핸드폰 번호 조회 
						checkMap = exqueryService.selectOne("nse.weStore.selectUserOne", param);
						String userName = checkMap.get("userName").toString();//사용자명 
						String userPhone = checkMap.get("mobileNo").toString();//사용자명 
						checkMap.clear();
						checkMap = exqueryService.selectOne("nse.weStore.selectStoreOne", param);
						String storeName = checkMap.get("storeName").toString();//업소명
						String waitCnt = param.get("orderId").toString();//나의 대기순서
						String urlCode = param.get("code").toString();//난수7자리 
						
//						String msg = "\""+userName+"님\" \""+storeName+"\" "+waitCnt+"번째 대기자입니다. 실시간 확인하기:http://wdg.kr/t?i="+urlCode;
						String msg = "";
						String engYn = param.get("engYn").toString();
//						if(engYn.equals("Y")){
//							msg = "[waiting] You are on the line on \""+storeName+"\"! "+waitCnt+" teams. View your turn:wdg.kr/"+urlCode;
//						} else {
							msg = "[웨이팅] \""+storeName+"\"에 "+waitCnt+"번째 대기자입니다. 실시간확인:wdg.kr/"+urlCode;
//						}
						param.put("msg", msg);
						param.put("phonNumber", userPhone);
						//메세지 발송 
						exqueryService.insert("nse.weMessage.insertMessageTran", param);
						
						//http://weding.gg/t?i=47cg588
						String reasonStr = "";
//						if(engYn.equals("Y")){
//							reasonStr = "Your ticket was issued.";
//						} else {
							reasonStr = "대기표 발급이 완료 되었습니다.";
//						}
						resultMap.put("result", "ture");
						resultMap.put("reason", reasonStr);
						
//					} else {
//						resultMap.put("result", "false");
//						resultMap.put("reason", ""+distance+"m 이내에서 대기표 발급이 가능합니다.");
//					}
					
					
				}
				
			}
		} else {
			resultMap.put("result", "false");
			resultMap.put("reason", "존재하지 않는 사용자 입니다.");
		}
		
		
		
		
		
		return resultMap;
	}
	
	/**
	 * 대기표받기<관리자>
	 * @param :storeId : 음식점ID, userName : 대기자이름, userId : 대기자ID, wait : 인원수
	 * @get: ?storeId=&userName=&userId=&wait=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setTicketByAdmin(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		System.out.println(param);
		
		HashMap<String, Object> checkMap = new HashMap<String, Object>();
		
		HashMap<String, Object> adMap = new HashMap<String, Object>();
		
		//휴대폰번호를 가지고 사용자 ID 조회한다.
		adMap = exqueryService.selectOne("nse.weUser.selectUserId", param);
		if(adMap == null){
			//최초사용자, 회원가입 ->  user_id 반환 
			exqueryService.insert("nse.weUser.insertUserInfo", param);
			//param에 자동으로 담김,
			param.put("userId", param.get("newId").toString());
		} else {
			//이미사용한 사용자 user_id 반환 
			param.put("userId", adMap.get("userId").toString());
		}
		
		
		param.put("userName", param.get("name").toString());
		
		//존재하는 사용자 인지 조회
//		checkMap = exqueryService.selectOne("nse.weStore.selectExistUser", param);
//		if(checkMap != null){
			//음식점이 영업하고 있는지 확인
//			checkMap.clear();
			checkMap = exqueryService.selectOne("nse.weStore.selectStoreState", param);
			
			if(checkMap == null || !checkMap.get("openState").equals("OPEN")){
				//영업중이 아님.
				resultMap.put("result", "false");
				resultMap.put("reason", "해당 매장이 영업중이 아닙니다.");
			} else {
				//체크맵 클리어 
				checkMap.clear();
				//해당사용자가 이미 대기중인 대기표가 있는지 확인 
				checkMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
				
				if(checkMap != null){
					//이미 대기표가 있음 
					resultMap.put("result", "false");
					if(param.get("engYn").equals("Y")){
						resultMap.put("reason", "You are already in waiting list ");
					} else {
						resultMap.put("reason", "이미 대기중인 대기표가 있습니다.");
					}
				} else {
					//대기하기 정상 진행 
					
					//WE_TICKET 저장하기 전에 대기순서를 정하여야 한다.
					checkMap = exqueryService.selectOne("nse.weStore.selectStoreTicketOrder", param);
					param.put("orderId", checkMap.get("orderId"));
					
					//WE_TICKET_SUMMARY 저장
					exqueryService.insert("nse.weStore.insertWeTicketSummaryByAdmin", param);
					//WE_TICKET 저장
					exqueryService.insert("nse.weStore.insertWeTicketByAdmin", param);
					
					
					//code가 중복되지 않을때까지 반복
					boolean isCodeChk = true;
					while(isCodeChk){
						//코드생성
						Random rnd =new Random();
						StringBuffer buf =new StringBuffer();
						
						for(int i=0;i<7;i++){
							if(rnd.nextBoolean()){
								buf.append((char)((int)(rnd.nextInt(26))+97));
							}else{
								buf.append((rnd.nextInt(10))); 
							}
						}
						String code = buf.toString();
						boolean startsWith = code.startsWith("mu");
						param.put("code", code);
						//난수를 생성했는데 데이터베이스에 조회해서 중복되는지 체크
						checkMap.clear();
						checkMap = exqueryService.selectOne("nse.weStore.selectCheckCode", param);
						if(!startsWith && checkMap == null){
							isCodeChk = false;
							//WE_IDCODE 저장 
							exqueryService.insert("nse.weStore.insertWeIdcode", param);
						}
					}
					
					
					//메세지발송 처리
					//사용자 핸드폰 번호 조회 
					checkMap = exqueryService.selectOne("nse.weStore.selectUserOne", param);
					String userName = checkMap.get("userName").toString();//사용자명 
					String userPhone = checkMap.get("mobileNo").toString();//사용자명 
					checkMap.clear();
					checkMap = exqueryService.selectOne("nse.weStore.selectStoreOne", param);
					String storeName = checkMap.get("storeName").toString();//업소명
					String waitCnt = param.get("orderId").toString();//나의 대기순서
					String urlCode = param.get("code").toString();//난수7자리 
					String callCnt = checkMap.get("callCnt").toString();//호출 대기인원수 
					
//					String msg = "\""+userName+"님\" \""+storeName+"\" "+waitCnt+"번째 대기자입니다. 실시간 확인하기:http://wdg.kr/t?i="+urlCode;
					String msg = "";
					String engYn = param.get("engYn").toString();
					if(engYn.equals("Y")){
						msg = "[waiting] You are on the line on \""+storeName+"\"! "+waitCnt+" teams. View your turn:wdg.kr/"+urlCode;
					} else {
						msg = "[웨이팅] \""+storeName+"\"에 "+waitCnt+"번째 대기자입니다. 실시간확인:wdg.kr/"+urlCode;
					}
					param.put("msg", msg);
					param.put("phonNumber", userPhone);
					//메세지 발송 
					exqueryService.insert("nse.weMessage.insertMessageTran", param);
					
					//http://weding.gg/t?i=47cg588
					String reasonStr = "";
					if(engYn.equals("Y")){
						reasonStr = "Your ticket was issued.";
					} else {
						reasonStr = "대기표 발급이 완료 되었습니다.";
					}
					resultMap.put("result", "ture");
					resultMap.put("reason", reasonStr);
					resultMap.put("callCnt", callCnt);
				}
				
			}
//		} 
//	else {
//			resultMap.put("result", "false");
//			resultMap.put("reason", "존재하지 않는 사용자 입니다.");
//		}
		
		
		
		
		
		return resultMap;
	}
	
	/**
	 * WEB 대기정보
	 * @param :i : 변환된 대기표ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getTicketInfo(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		HashMap<String, Object> qMap = new HashMap<String, Object>();
		
		
		qMap = exqueryService.selectOne("nse.weStore.selectIsExistTicketStat", param); 
		
		if(qMap != null){
			HashMap<String, Object> aMap = new HashMap<String, Object>();
			// 대기정보 가져오기
			aMap = exqueryService.selectOne("nse.weStore.selectStoreInfoWeb", param);
			if(aMap != null){
				param.put("storeId", aMap.get("storeId"));
				resultMap.put("storeName", aMap.get("storeName"));
				resultMap.put("userName", aMap.get("userName"));
				resultMap.put("wait", aMap.get("wait"));
				// 오늘총 대기팀수
				aMap = exqueryService.selectOne("nse.weStore.selectTodayTotalCount", param);
				resultMap.put("totalWaitCnt", aMap.get("totalWaitCnt"));
				// 남은팀수
				aMap = exqueryService.selectOne("nse.weStore.selectWaitCount", param);
				resultMap.put("waitCnt", aMap.get("waitCnt"));
				
				resultMap.put("result", true);
			} else {
				//잘못된 코드정보 
				resultMap.put("result", false);
			}
		} else {
			//잘못된 코드정보 
			resultMap.put("result", false);
		}
		

		return resultMap;
	}

	/**
	 * 기다리는 정보가 있는지 조회 
	 * @param :userId:사용자ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getIsTicket(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		HashMap<String, Object> cMap = new HashMap<String, Object>();
		// 대기표가 있는지 조회 
		aMap = exqueryService.selectOne("nse.weStore.selectExistUserTicket", param);
		if(aMap != null){//대기표있음
			resultMap.put("lastState", "exist");
			resultMap.put("detail", "대기표를 가지고 있습니다.");
			
			//##대기표정보##
			cMap = getWaitTicket(param);
			
			resultMap.put("ticket", cMap);
		} else {
			//대기표 있음 
			//최근 5시간이력이 있는지 조회 
			aMap = exqueryService.selectOne("nse.weStore.getLastTicketTime", param);
			
			if(aMap!=null){
				int lastTime = Integer.parseInt(aMap.get("lastTime").toString());
				if(lastTime < 5){
					//5시간이내 
					resultMap.put("lastState", "lastExist");
					resultMap.put("detail", "대기표를 가지고 있지 않지만 최근5시간 이내 이력을 가지고 있습니다.");
					
					//##마지막정보##
					cMap = getLastTicketInfo(param);
					
					resultMap.put("ticket", cMap);
				} else {
					//5시간지남 
					resultMap.put("lastState", "none");
					resultMap.put("detail", "대기표를 가지고 있지 않고 5시간 이후 이력이 존재합니다. ");
					
					resultMap.put("ticket", cMap);
				}
			} else {
				resultMap.put("lastState", "none");
				resultMap.put("detail", "대기표를 가지고 있지 않고 최근이력이 존재하지 않습니다.");
				
				resultMap.put("ticket", cMap);
			}
			
		}

		return resultMap;
	}
	
	/**
	 * 마지막 기다린 정보 조회 
	 * @param :userId:사용자ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getLastTicketInfo(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		// 대기표가 있는지 조회 
		resultMap = exqueryService.selectOne("nse.weStore.getLastTicketInfo", param);
		
		return resultMap;
	}
	

	/**
	 * 대기정보 조회 
	 * @param :userId:사용자ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getWaitTicket(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
			// 대기정보 가져오기
		resultMap = exqueryService.selectOne("nse.weStore.getWaitInfoBasic", param);
		if(resultMap != null){
			aMap = exqueryService.selectOne("nse.weStore.getWaitInfoTIme", param);
			resultMap.put("expectTime", aMap.get("expectTime"));
			resultMap.put("waitCnt", aMap.get("waitCnt"));
			resultMap.put("result", true);
		} else {
			//잘못된 코드정보 
			resultMap = new HashMap<String, Object>();
			resultMap.put("result", false);
		}

		return resultMap;
	}
	
	/**
	 * 미루기
	 * @param :userId:사용자ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setDelayTicket(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		//해당사용자의 대기표가 유효한지 체크
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		aMap = exqueryService.selectOne("nse.weStore.getUserExistTicket", param);
		//해당사용자의 미루기가 가능한지 체크
		if(aMap != null && aMap.get("delayYn").equals("N")){
			String storeId = aMap.get("storeId").toString();
			//대기표가 존재함 미루기가 가능함 
			//이동할 값 구하기(+5 이지만 max가 5보다 작을경우 max값)
			aMap.clear();
			aMap = exqueryService.selectOne("nse.weStore.getMyTicketId", param);
			int myId = Integer.parseInt(aMap.get("myId").toString());
			aMap.clear();
			aMap = exqueryService.selectOne("nse.weStore.getMaxTicketId", param);
			int maxId = Integer.parseInt(aMap.get("maxId").toString());
			
			int newId = 0;
			if((maxId - myId) >= 5){
				newId = 5 + myId;
			} else {
				newId = (maxId-myId) + myId;
			}
			
			aMap.put("newId", newId);
			aMap.put("myId", myId);
			aMap.put("userId", param.get("userId"));
			aMap.put("storeId", storeId);
			//myId 보다 크고 newId 보다 작거나 같은것들 -1
			exqueryService.update("nse.weStore.setOrderChange", aMap);
			//자기자신 처리 
			exqueryService.update("nse.weStore.setOrderChangeMyTicket", aMap);
			
			HashMap<String, Object> tMap = new HashMap<String, Object>();
			tMap = exqueryService.selectOne("nse.weStore.selectCallCnt", aMap);
			int callCnt = Integer.parseInt(tMap.get("callCnt").toString());
			//요청 순서가 5미만일경우만 
			if(myId <= callCnt){
				//메세지처리
				messageService.setMessageBy5(storeId);
			}
			
			resultMap.put("result", "true");
			resultMap.put("detail", "미루기가 처리 되었습니다.");
		} else {
			//대기표가 존재하지 않음
			resultMap.put("result", "false");
			resultMap.put("detail", "이미 미루기를 사용하여 미루기를 할 수 없습니다.");
		}
		
		return resultMap;
	}
	
	/**
	 * 티켓 취소
	 * @param :userId:사용자ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setCancel(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			//해당사용자의 대기표가 유효한지 체크
			HashMap<String, Object> aMap = new HashMap<String, Object>();
			aMap = exqueryService.selectOne("nse.weStore.getUserExistTicket", param);
			if(aMap != null){
				
				HashMap<String, Object> cMap = new HashMap<String, Object>();
				cMap = exqueryService.selectOne("nse.weStore.getMyTicketId", param);
				int myId = Integer.parseInt(cMap.get("myId").toString());
				
				//WE_TICKET_SUMMARY 정보 변경 
				exqueryService.update("nse.weStore.updateTicketInfo", param);
				//표시순서를 다시 정렬한다.
				//myId 보다 큰것들 -1
				exqueryService.update("nse.weStore.setOrderChange2", aMap);
				//WE_TICKET 정보 삭제
				exqueryService.delete("nse.weStore.deleteTicket", param);
				//WE_IDCODE 정보삭제
				exqueryService.delete("nse.weStore.deleteTicketIdCode", param);
				
				
				HashMap<String, Object> tMap = new HashMap<String, Object>();
				tMap = exqueryService.selectOne("nse.weStore.selectCallCnt", aMap);
				int callCnt = Integer.parseInt(tMap.get("callCnt").toString());
				//요청 순서가 5미만일경우만 
				if(myId <= callCnt){
					//메세지처리
					messageService.setMessageBy5(aMap.get("storeId").toString());
				}
				
				
				resultMap.put("result", "true");
				resultMap.put("detail", "취소되었습니다.");
			} else {
				resultMap.put("result", "false");
				resultMap.put("detail", "대기표가 존재하지 않습니다.");
			}
			
			
		} catch(Exception e){
			resultMap.put("result", "false");
			resultMap.put("detail", "취소처리 되지 않았습니다. 다시 시도해 주세요.");
		}
		
		
		return resultMap;
	}
	

	/**
	 * 티켓 취소 (관리자)
	 * @param :userId:사용자ID
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setCancelByAdmin(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			//해당사용자의 대기표가 유효한지 체크
			HashMap<String, Object> aMap = new HashMap<String, Object>();
			aMap = exqueryService.selectOne("nse.weStore.getUserExistTicket", param);
			if(aMap != null){
				
				HashMap<String, Object> cMap = new HashMap<String, Object>();
				cMap = exqueryService.selectOne("nse.weStore.getMyTicketId", param);
				int myId = Integer.parseInt(cMap.get("myId").toString());
				
				//WE_TICKET_SUMMARY 정보 변경 
				exqueryService.update("nse.weStore.updateTicketInfo", param);
				//표시순서를 다시 정렬한다.
				//myId 보다 큰것들 -1
				exqueryService.update("nse.weStore.setOrderChange2", aMap);
				//WE_TICKET 정보 삭제
				exqueryService.delete("nse.weStore.deleteTicket", param);
				//WE_IDCODE 정보삭제
				exqueryService.delete("nse.weStore.deleteTicketIdCode", param);
				
				HashMap<String, Object> tMap = new HashMap<String, Object>();
				tMap = exqueryService.selectOne("nse.weStore.selectCallCnt", aMap);
				int callCnt = Integer.parseInt(tMap.get("callCnt").toString());
				//요청 순서가 5미만일경우만 
				if(myId <= callCnt){
					//메세지처리
					messageService.setMessageBy5(aMap.get("storeId").toString());
				}
				
				//관리자에 의한 취소 처리 
				
				param.put("storeId", aMap.get("storeId").toString());
				//사용자 핸드폰 번호 조회 
				HashMap<String, Object> checkMap = new HashMap<String, Object>();
				checkMap = exqueryService.selectOne("nse.weStore.selectUserOne", param);
				String userName = checkMap.get("userName").toString();//사용자명 
				String userPhone = checkMap.get("mobileNo").toString();//사용자명 
				checkMap.clear();
				checkMap = exqueryService.selectOne("nse.weStore.selectStoreOne", param);
				String storeName = checkMap.get("storeName").toString();//업소명
				
//				String msg = "\""+userName+"님\" \""+storeName+"\" "+waitCnt+"번째 대기자입니다. 실시간 확인하기:http://wdg.kr/t?i="+urlCode;
				String msg = "[웨이팅]\""+storeName+"\"의 대기표가 관리자에 의해서 취소 되었습니다.";
				
				param.put("msg", msg);
				param.put("phonNumber", userPhone);
				//메세지 발송 
				exqueryService.insert("nse.weMessage.insertMessageTran", param);
				
				
				resultMap.put("result", "true");
				resultMap.put("reason", "취소되었습니다.");
			} else {
				resultMap.put("result", "false");
				resultMap.put("reason", "대기표가 존재하지 않습니다.");
			}
			
			
		} catch(Exception e){
			resultMap.put("result", "false");
			resultMap.put("reason", "취소처리 되지 않았습니다. 다시 시도해 주세요.");
		}
		
		
		return resultMap;
	}
	
	/**
	 * 리뷰 남기기 
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> setReview(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			//해당사용자가 리뷰를 남겼는지 조회
			HashMap<String, Object> aMap = new HashMap<String, Object>();
			aMap = exqueryService.selectOne("nse.weStore.selectUserReview", param);
			if(Integer.parseInt(aMap.get("cnt").toString()) == 0){
				
				//유효성 체크 -> 존재하는 티켓인지 사용자가 맞는지 조회 
				aMap = exqueryService.selectOne("nse.weStore.selectUserReviewValidation", param);
				if(aMap != null){
					//리뷰등록
					exqueryService.insert("nse.weStore.insertReview", param);
					
					resultMap.put("result", "true");
					resultMap.put("reason", "리뷰가 등록 되었습니다.");
				} else {
					resultMap.put("result", "false");
					resultMap.put("reason", "잘못된 사용자 대기표 정보입니다. ");
				}
				
			} else {
				resultMap.put("result", "false");
				resultMap.put("reason", "이미 리뷰를 등록 하셨습니다.");
			}
			
			
		} catch(Exception e){
			resultMap.put("result", "false");
			resultMap.put("reason", "등록되지 않았습니다. 다시 시도해 주세요.");
		}
		
		
		return resultMap;
	}
	
	/**
	 * 리뷰 남기기 
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getIsExistOption(HashMap<String, Object> param) throws Exception {
		//return Object
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			//옵션 사용여부를 조회 
			HashMap<String, Object> aMap = new HashMap<String, Object>();
			aMap = exqueryService.selectOne("nse.weStore.selectIsExistOption", param);
			if(aMap.get("optionYn").equals("Y")){
				
				//옵션사용 업체. 옵션조회 
				List<HashMap<String, Object>> optionList = new ArrayList<HashMap<String, Object>>();
				optionList = exqueryService.selectList("nse.weStore.selectOptionList", param);
				if(optionList != null){
					resultMap.put("result", "true");
					resultMap.put("optionList", optionList);
					resultMap.put("reason", "옵션리스트 리턴");
				} else {
					resultMap.put("result", "false");
					resultMap.put("reason", "옵션을 사용하지만, 옵션내역이 존재하지 않습니다.");
				}
				
			} else {
				resultMap.put("result", "false");
				resultMap.put("reason", "옵션을 사용하지 않습니다.");
			}
			
			
		} catch(Exception e){
			resultMap.put("result", "false");
			resultMap.put("reason", "알수없는 오류가 발생했습니다. 다시 시도해 주세요.");
		}
		
		
		return resultMap;
	}
}
