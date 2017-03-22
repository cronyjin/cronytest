package kr.co.user.weding.service;

import java.util.HashMap;

import org.springframework.ui.ModelMap;

public interface CommonService {

	/**
	 * 기본정보 조회
	 */
	public HashMap<String, Object> startMenu(HashMap<String, Object> param)  throws Exception ; 
	
	/**
	 * 숙성관리 기본정보 조회
	 */
	public HashMap<String, Object> startAgeMenu(HashMap<String, Object> param)  throws Exception ; 

	/**
	 * Get PageNo
	 * @param param
	 * @return
	 */
	public int getPageNo(HashMap<String, Object> param);
	
	/**
	 * Get PageSize
	 * @param param
	 * @return
	 */
	public int getPageSize(HashMap<String, Object> param);	

	/**
	 * 표시순서 정렬 업데이트
	 * @throws Exception 
	 */
	public void sortShowOrder(String sqlMap, HashMap<String, Object> param) throws Exception;
	
	/**
	 * 메뉴별로 설정사항을 model에 담아서 리턴한다.
	 * @param menuUrl
	 * @param model
	 * @throws Exception
	 */
	public void setMenuConfigValue(String menuUrl, ModelMap model);


	/**
	 * Get Date-Tile
	 * @param String startDate, String endDate
	 * @return
	 */
	public String getDateTitle(String startDate, String endDate);
	
	/**
	 * 메인화면 조회
	 * @param String startDate, String endDate
	 * @return
	 */
	public HashMap<String, Object> getMainInfo(HashMap<String, Object> param);
}
