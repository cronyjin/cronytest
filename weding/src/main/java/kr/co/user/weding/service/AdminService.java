package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;


public interface AdminService {

	/**
	 * 관리자 로그인 
	 */
	public HashMap<String, Object> getLogin(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 매장 상태조회
	 */
	public HashMap<String, Object> getStoreState(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 매장 시작/중지 
	 */
	public HashMap<String, Object> setStoreState(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 대기정보 
	 */
	public List<HashMap<String, Object>> getStoreWait(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 입장완료처리 
	 */
	public HashMap<String, Object> setWaitEnd(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 패스콜 
	 */
	public HashMap<String, Object> setPassCall(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 미리콜  
	 */
	public HashMap<String, Object> setMiriCall(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 입장콜  
	 */
	public HashMap<String, Object> setEnterCall(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 관리자매장정보조회
	 */
	public HashMap<String, Object> getAdminStoreInfo(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 공지사항조회
	 */
	public List<HashMap<String, Object>> getNotice(HashMap<String, Object> param)  throws Exception ; 
}
