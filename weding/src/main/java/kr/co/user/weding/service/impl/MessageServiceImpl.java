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

import kr.co.user.weding.service.MessageService;
import select.spring.exquery.service.ExqueryService;

@Service("messageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MessageServiceImpl implements MessageService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private HttpSession session;

	

	/**
	 * 현재 N명 대기인원이 남았으니 "도미노피자"로 와주세요. 미루기/취소:http://we.kr/578ueyb
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public void setMessageBy5(String storeId) throws Exception {
		
		//메세지처리 (순서변경에따른처리)
		String STORE_ID = storeId;
		HashMap<String, Object> storeMap = new HashMap<String, Object>();
		HashMap<String, Object> userMap = new HashMap<String, Object>();
		HashMap<String, Object> codeMap = new HashMap<String, Object>();
		HashMap<String, Object> store2Map = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		storeMap.put("storeId", STORE_ID);
		
		//사용자정보를 가지고 매장정보를 찾는다.
		//현재 상점에 대기순서가 5가 될 사용자를 찾는다.
		userMap = exqueryService.selectOne("nse.weStore.selectStoreUserNumber5One", storeMap);
		if(userMap != null){
			String userPhone = userMap.get("mobileNo").toString();//사용자명 
			store2Map = exqueryService.selectOne("nse.weStore.selectStoreOne", storeMap);
			String storeName = store2Map.get("storeName").toString();//업소명
			//난수 조회
			codeMap = exqueryService.selectOne("nse.weStore.selectUserCode", userMap);
			String urlCode = codeMap.get("code").toString();//난수7자리 
			
			int callCnt = Integer.parseInt(store2Map.get("callCnt").toString());
			
//			String msg = "현재 5명 대기인원이 남았으니 \""+storeName+"\"로 와주세요. 미루기/취소:http://we.kr/"+urlCode;
			String msg = "[웨이팅] \""+storeName+"\"에 대기인원이 "+callCnt+"명 남았습니다 미루기/취소:wdg.kr/"+urlCode;

			param.put("msg", msg);
			param.put("phonNumber", userPhone);
			//메세지 발송 
			exqueryService.insert("nse.weMessage.insertMessageTran", param);
			userMap.clear();
			codeMap.clear();
			store2Map.clear();
			param.clear();
		}
			
			storeMap.clear();
		//##
			
	}
}
