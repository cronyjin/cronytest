package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;

public interface ConstraintService {
	
	public String checkExists(HashMap<String, Object> param, String listName) throws Exception;

	public String checkExists(List<HashMap<String, Object>> paramList);

	public String checkExistsV2(HashMap<String, Object> param, String listName) throws Exception;

	public String checkExistsV2(List<HashMap<String, Object>> paramList);
	
	public String checkExistsV2(String param) throws Exception;

}
