package kr.co.user.weding.service;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelUploadService {
	
	/**
	 * 접점일괄등록
	 * @return HashMap
	 */
	public HashMap<String, Object> insertExcelUploadTag(HashMap<String, Object> param, MultipartFile file);
	
	/**
	 * 설비 일괄입력 
	 * @return HashMap
	 */
	public HashMap<String, Object> insertExcelUploadEquip(HashMap<String, Object> param, MultipartFile file);
	
	/**
	 * 설비-접점 일괄입력 
	 * @return HashMap
	 */
	public HashMap<String, Object> insertExcelUploadEquipTag(HashMap<String, Object> param, MultipartFile file);
	
	/**
	 * 사용자관리 일괄입력 
	 * @return HashMap
	 */
	public HashMap<String, Object> insertExcelUploadUser(HashMap<String, Object> param, MultipartFile file);
	
}