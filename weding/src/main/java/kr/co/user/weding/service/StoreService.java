package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;


public interface StoreService {

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
	public List<HashMap<String, Object>> getStoreList(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 음식점목록 조회 <대기팀, 소요시간만 조회>
	 * @param :none
	 */
	public List<HashMap<String, Object>> getStoreInfoList(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 리뷰 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	public HashMap<String, Object> getReview(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 상점상세정보 조회 
	 * @param :storeId:상점ID
	 * @get: ?storeId=
	 */
	public HashMap<String, Object> getStoreDetail(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 상점 위도경도조회
	 * @param :
	 */
	public List<HashMap<String, Object>> getStoreInfoMap(HashMap<String, Object> param)  throws Exception ; 

	/**
	 * 매장 이미지 조회
	 * @param :
	 */
	public List<HashMap<String, Object>> getStoreImg(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 히스토리 조회 
	 * @param :
	 */
	public List<HashMap<String, Object>> getHistory(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 상점상세정보 조회 
	 */
	public HashMap<String, Object> getStoreDetailOne(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 상점상세정보 조회 
	 */
	public HashMap<String, Object> getStoreDetailOneByTablet(HashMap<String, Object> param)  throws Exception ; 
}
