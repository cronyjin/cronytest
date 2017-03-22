package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;

public interface SystemService {

	/**
	 * 접점 입력
	 */
	public HashMap<String, Object> selectConfDataBase(HashMap<String, Object> param) throws Exception;


	/**
	 * 시스템 설정 업데이트
	 */
	public HashMap<String, Object> updateConf(HashMap<String, Object> param);

}