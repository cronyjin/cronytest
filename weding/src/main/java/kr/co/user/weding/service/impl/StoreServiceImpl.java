package kr.co.user.weding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.daehanins.common.NbpmsConst;

import kr.co.user.weding.service.StoreService;
import select.spring.exquery.service.ExqueryService;

@Service("storeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StoreServiceImpl implements StoreService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private HttpSession session;

	

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
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getStoreList(HashMap<String, Object> param){
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
			
		List<HashMap<String, Object>> resultlist = new ArrayList<HashMap<String, Object>>();
		
		//정렬순서 처리
		String orderStr = "";
		if(param.get("showOrder").equals("maxWait")){
			orderStr = "ORDER BY OPEN_STATE DESC, WAIT_CNT DESC";
		} else if(param.get("showOrder").equals("minWait")){
			orderStr = "ORDER BY OPEN_STATE DESC, WAIT_CNT ASC";
		} else if(param.get("showOrder").equals("rate")){
			orderStr = "ORDER BY OPEN_STATE DESC, RATE DESC";
		} else {
			orderStr = "ORDER BY OPEN_STATE DESC, WAIT_CNT DESC";
		}
		
		param.put("orderStr", orderStr);
		
		// gps 수신 불가일때 처리 
		double lat = Double.parseDouble(param.get("lat").toString());
		double lon = Double.parseDouble(param.get("lon").toString());
		
		if(lat == 0 && lon == 0){
			//위치정보 없음 처리 
			resultlist = exqueryService.selectList("nse.weStore.selectStoreAllListSimple", param);
		} else {
			//매장목록 조회 
			resultlist = exqueryService.selectList("nse.weStore.selectStoreAllList", param);
		}
		
		
		return resultlist;
	}
	
	/**
	 * 음식점목록 조회 <대기팀, 소요시간만 조회>
	 * @param :none
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getStoreInfoList(HashMap<String, Object> param){
			
		List<HashMap<String, Object>> resultlist = new ArrayList<HashMap<String, Object>>();
		
		//매장목록 조회 
		resultlist = exqueryService.selectList("nse.weStore.selectStoreAllListSimpleTest", param);
		
		
		return resultlist;
	}
	
	/**
	 * 리뷰 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getReview(HashMap<String, Object> param){
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
			
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<HashMap<String, Object>> resultlist = new ArrayList<HashMap<String, Object>>();
		
		
		result = exqueryService.selectOne("nse.weStore.selectReviewPrev", param);
		//리뷰 조회
		resultlist = exqueryService.selectList("nse.weStore.selectReview", param);
		
		result.put("list", resultlist);
		
		return result;
	}
	
	/**
	 * 상점상세정보 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getStoreDetail(HashMap<String, Object> param){
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
		result = exqueryService.selectOne("nse.weStore.selectStoreDetails", param);
		
		aList = getStoreImg(param);
		result.put("img", aList);
		
		return result;
	}
	
	/**
	 * 상점 위도경도조회 
	 * @param :
	 * @get: 
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getStoreInfoMap(HashMap<String, Object> param){
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result = exqueryService.selectList("nse.weStore.getStoreInfoMap", param);
		

		return result;
	}

	/**
	 * 매장 이미지 조회
	 * @param :
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getStoreImg(HashMap<String, Object> param){
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result = exqueryService.selectList("nse.weStore.getStoreImg", param);

		return result;
	}
	
	/**
	 * 지난 이력 조회
	 * @param :
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public List<HashMap<String, Object>> getHistory(HashMap<String, Object> param){
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result = exqueryService.selectList("nse.weStore.selectHistory", param);
		
		return result;
	}
	
	/**
	 * 상점상세정보 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getStoreDetailOne(HashMap<String, Object> param){
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> result = new HashMap<String, Object>();
	
		result = exqueryService.selectOne("nse.weStore.selectStoreInfoOne", param);
		
		
		return result;
	}
	
	/**
	 * 상점상세정보 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getStoreDetailOneByTablet(HashMap<String, Object> param){
		//입력받은 파라메터의 값들이 존재하는지 정상적인지에 대한 validation method 만들것 
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result = exqueryService.selectOne("nse.weStore.selectStoreInfoOneByTablet", param);
		
		
		return result;
	}
}
