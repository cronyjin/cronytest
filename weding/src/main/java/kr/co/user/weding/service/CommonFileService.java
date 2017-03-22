package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import select.spring.exquery.vo.ExFileInfo;

public interface CommonFileService {

	public int insertWithFile(String key, HashMap<String, Object> param, MultipartFile file);
	
	public int updateWithFile(String key, HashMap<String, Object> param, MultipartFile file);
	
	public int deleteWithFile(String key, HashMap<String, Object> param);
	
	public int deleteWithFileId(String key, HashMap<String, Object> param);
	
	public HashMap<String, Object> readFile(String key, HashMap<String, Object> param);
	

}
