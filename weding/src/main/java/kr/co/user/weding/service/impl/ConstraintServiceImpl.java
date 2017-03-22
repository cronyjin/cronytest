package kr.co.user.weding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import select.spring.exquery.service.ExqueryService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.user.weding.service.ConstraintService;

@Service("constraintService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ConstraintServiceImpl implements ConstraintService {

	@Autowired
	private ExqueryService exqueryService;

	private static final String MSG_OK = "OK";
	
	// param = { conslist: [ {tableName, colName, colValue, msgCode } ...] }
	public String checkExists(HashMap<String, Object> param, String listName) throws Exception {

		List<HashMap<String, Object>> consList = new ArrayList<HashMap<String, Object>>();
	   	ObjectMapper tagMapper = new ObjectMapper();

	   	String consResult = "OK";
		if (param.get(listName) != null) {
			consList = tagMapper.readValue(param.get(listName).toString(), new TypeReference<ArrayList<HashMap<String, Object>>>(){});
			if (consList.size() > 0) {
				consResult = this.checkExists(consList);
			}
		}
		return consResult;
	}

	// paramList = [ {tableName, colName, colValue, msgCode } ... ]
	public String checkExists(List<HashMap<String, Object>> paramList) {
		String resultMsg = MSG_OK;
		for (HashMap<String, Object> param : paramList) {
			resultMsg = runCheckExists(param);
			if (!resultMsg.equals(MSG_OK)) {
				break;
			}
		}
		return resultMsg;
	}
	

	// param = { conslist: [ {tableName, pkList: [ {colName, colValue}, .... ] , msgCode } ...] }
	public String checkExistsV2(HashMap<String, Object> param, String listName) throws Exception {

		List<HashMap<String, Object>> consList = new ArrayList<HashMap<String, Object>>();
		
		String consResult = "OK";

		if (param.get(listName) == null) {
			return consResult;
		}
		
	   	ObjectMapper tagMapper = new ObjectMapper();

	   	String jsonString = param.get(listName).toString();
		JsonNode rootNode = tagMapper.readTree(jsonString);
		Iterator<JsonNode> conListIter = rootNode.elements();
		
		while (conListIter.hasNext()) {
			JsonNode tableItem = conListIter.next();
			JsonNode tableName = tableItem.findPath("tableName");
			JsonNode pkList    =  tableItem.findPath("pkList");
			JsonNode msgCode   = tableItem.findPath("msgCode");
			
			HashMap<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("tableName", tableName.textValue());
			pMap.put("msgCode", msgCode.textValue());
			
			List<HashMap<String, Object>> colItemList = new ArrayList<HashMap<String, Object>>();
			Iterator<JsonNode> pkListIter = pkList.elements();
			while (pkListIter.hasNext()) {
				JsonNode colItem = pkListIter.next();
				JsonNode colName = colItem.findPath("colName");
				JsonNode colValue = colItem.findPath("colValue");
				HashMap<String, Object> colItemMap = new HashMap<String, Object>();
				colItemMap.put("colName", colName.textValue());
				colItemMap.put("colValue", colValue.textValue());
				colItemList.add(colItemMap);
			}
			pMap.put("pkList", colItemList);
			consList.add(pMap);
		}
		
		if (consList.size() > 0) {
			consResult = this.checkExistsV2(consList);
		}
		return consResult;
	}
	
	// param = { conslist: [ {tableName, pkList: [ {colName, colValue}, .... ] , msgCode } ...] }
	public String checkExistsV2(String param) throws Exception {
		
		List<HashMap<String, Object>> consList = new ArrayList<HashMap<String, Object>>();
		
		String consResult = "OK";
		
		if ("".equals(param)) {
			return consResult;
		}
		
		ObjectMapper tagMapper = new ObjectMapper();
		
		String jsonString = param;
		JsonNode rootNode = tagMapper.readTree(jsonString);
		Iterator<JsonNode> conListIter = rootNode.elements();
		
		while (conListIter.hasNext()) {
			JsonNode tableItem = conListIter.next();
			JsonNode tableName = tableItem.findPath("tableName");
			JsonNode pkList    =  tableItem.findPath("pkList");
			JsonNode msgCode   = tableItem.findPath("msgCode");
			
			HashMap<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("tableName", tableName.textValue());
			pMap.put("msgCode", msgCode.textValue());
			
			List<HashMap<String, Object>> colItemList = new ArrayList<HashMap<String, Object>>();
			Iterator<JsonNode> pkListIter = pkList.elements();
			while (pkListIter.hasNext()) {
				JsonNode colItem = pkListIter.next();
				JsonNode colName = colItem.findPath("colName");
				JsonNode colValue = colItem.findPath("colValue");
				HashMap<String, Object> colItemMap = new HashMap<String, Object>();
				colItemMap.put("colName", colName.textValue());
				colItemMap.put("colValue", colValue.textValue());
				colItemList.add(colItemMap);
			}
			pMap.put("pkList", colItemList);
			consList.add(pMap);
		}
		
		if (consList.size() > 0) {
			consResult = this.checkExistsV2(consList);
		}
		return consResult;
	}

	
	// paramList = [ {tableName, pkList: [{colName, colValue}, ..... ] , msgCode } ... ]
	public String checkExistsV2(List<HashMap<String, Object>> paramList) {
		String resultMsg = MSG_OK;
		for (HashMap<String, Object> param : paramList) {
			resultMsg = runCheckExistsV2(param);
			if (!resultMsg.equals(MSG_OK)) {
				break;
			}
		}
		return resultMsg;
	}
	
	// param = {tableName, colName, colValue, msgCode }
	private String runCheckExists(HashMap<String, Object> param) {
		String resultMsg = MSG_OK;
		if (exqueryService.checkExists(param)) {
			resultMsg = (String)param.get("msgCode");
		}
		return resultMsg;
	}	

	// param = {tableName, pkList: [{colName, colValue}, ..... ] , msgCode }
	private String runCheckExistsV2(HashMap<String, Object> param) {
		String resultMsg = MSG_OK;
		if (exqueryService.checkExistsV2(param)) {
			resultMsg = (String)param.get("msgCode");
		}
		return resultMsg;
	}

}
