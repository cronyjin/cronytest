package kr.co.user.weding.service;

import java.util.HashMap;


public interface TicketService {

	/**
	 * 대기표받기
	 * @param :storeId : 음식점ID, userName : 대기자이름, userId : 대기자ID, wait : 인원수
	 */
	public HashMap<String, Object> setTicket(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 대기표받기 <관리자>
	 */
	public HashMap<String, Object> setTicketByAdmin(HashMap<String, Object> param)  throws Exception ; 
	/**
	 * 옵션체크 
	 */
	public HashMap<String, Object> getIsExistOption(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * WEB 대기정보
	 * @param :i : 변환된 대기표ID
	 */
	public HashMap<String, Object> getTicketInfo(HashMap<String, Object> param)  throws Exception ; 

	/**
	 * 기다리는 정보가 있는지 조회 
	 * @param :userId:사용자ID
	 */
	public HashMap<String, Object> getIsTicket(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 마지막 기다린 정보 조회 
	 * @param :userId:사용자ID
	 */
	public HashMap<String, Object> getLastTicketInfo(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 대기정보 조회 
	 * @param :userId:사용자ID
	 */
	public HashMap<String, Object> getWaitTicket(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 미루기
	 * @param :userId:사용자ID
	 */
	public HashMap<String, Object> setDelayTicket(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 티켓 취소 
	 * @param :userId:사용자ID
	 */
	public HashMap<String, Object> setCancel(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 티켓 취소 <관리자>
	 * @param :userId:사용자ID
	 */
	public HashMap<String, Object> setCancelByAdmin(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 리뷰작성
	 */
	public HashMap<String, Object> setReview(HashMap<String, Object> param)  throws Exception ; 
}
