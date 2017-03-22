package kr.co.user.weding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.user.weding.service.CommonService;
import kr.co.user.weding.service.ConstraintService;
import kr.co.user.weding.service.SystemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import select.spring.exquery.dao.ExqueryDao;
import select.spring.exquery.service.ExqueryService;

@Service("systemService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemServiceImpl implements SystemService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;
	
	@Autowired
	private ConstraintService constraintService;
	
	@Autowired
	private CommonService commonService;
	
	
	/**
	 * 데이터베이스 현황
	 * @return HashMap
	 */	
	public HashMap<String, Object> selectConfDataBase(@RequestParam HashMap<String, Object> param) {
		
		HashMap<String, Object> rMap = new HashMap<String, Object>();
		
		HashMap<String, Object> aMap = exqueryService.selectOne("nse.bpCommon.getToday", param);
		rMap.put("currTime",	aMap.get("fullDateFormat"));
		
		List<HashMap<String, Object>> alist = exqueryService.selectList("nse.bpConf.selectConfDataBaseA", param);
		rMap.put("alist", alist);
		
		List<HashMap<String, Object>> blist = exqueryService.selectList("nse.bpConf.selectConfDataBaseB", param);
		rMap.put("blist", blist);
		
		return rMap;
	}
	/**
	 * 시스템설정 수정 (Update)
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updateConf(HashMap<String, Object> param){
		
		boolean result = true;

		HashMap<String, Object> aMap = param;
		List<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
		
		try{
			aMap.put("updId", param.get("ssUserId").toString());			
			aMap.put("code", "LOG_USE");
			aMap.put("val", param.get("logUse"));
			exqueryService.update("nse.bpSysConfig.updateConf", aMap);
		
		}catch(Exception e){
			System.out.println(e);
			result = false;
		}
		
		HashMap<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("result", result);
    	return rMap;
	}
}