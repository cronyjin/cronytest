package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;


public interface UserService {

	
	/**
	 * tset
	 * @param :"name : 이름 , phonNumber : 핸드폰번호"
	 * @get: ?name=&phonNumber=
	 */
	public List<HashMap<String, Object>> testInfo(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 로그인하기 
	 * @param :"name : 이름 , phonNumber : 핸드폰번호"
	 * @get: ?name=&phonNumber=
	 */
	public HashMap<String, Object> getUserInfo(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 인증번호받기 
	 * @param :phonNumber : 핸드폰번호
	 * @get: ?phonNumber=
	 */
	public HashMap<String, Object> getConfirmNumber(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 인증번호확인
	 * @param :"phonNumber:휴대폰번호, confirmNumber:인증번호"
	 * @get: ?phonNumber=&confirmNumber=
	 */
	public HashMap<String, Object> getConfirm(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 약관조회 
	 * @param :termCode : 조회할약관코드 <clause, personal, location>
	 * @get: ?termCode=
	 */
	public HashMap<String, Object> getTerms(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 가입완료 정보저장  
	 * @param :"name : 이름, phonNumber:휴대폰번호"
	 * @get: ?name=&phonNumber=
	 */
	public HashMap<String, Object> setUserInfo(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 나의정보조회 
	 * @param :"userId : 사용자ID"
	 * @get: ?name=&phonNumber=
	 */
	public HashMap<String, Object> getSettingUserInfo(HashMap<String, Object> param) throws Exception ; 
	/**
	 * 사용자명 변경
	 */
	public HashMap<String, Object> setUserName(HashMap<String, Object> param) throws Exception ; 

	
	
}
