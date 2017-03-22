package kr.co.user.weding.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.daehanins.common.NbpmsConst;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.user.weding.service.CommonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import select.spring.exquery.service.ExqueryService;

@Controller
@RequestMapping(value="excel")

public class ExcelDownloadController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    ApplicationContext context;

	@Autowired
	private ExqueryService exqueryService;
	
	@Autowired
	private CommonService commonService;
	
	
	private String mapRoot = "nse";
	
	/**
	 * 엑셀 현재 날짜 가져오기
	 * @return
	 */
	public String getNowDateTime()
	{
	    java.util.Date toDay = new java.util.Date();

		String szType = "yyyyMMdd";

	    SimpleDateFormat sdf = new SimpleDateFormat(szType);
	    return sdf.format(toDay);
	}
	/**
	 * 엑셀 현재 날짜 가져오기(엑셀에서사용)
	 * @return
	 */
	public String getNowDateTimeExcel()
	{
	    java.util.Date toDay = new java.util.Date();

		String szType = "yyyy-MM-dd";

	    SimpleDateFormat sdf = new SimpleDateFormat(szType);
	    return sdf.format(toDay);
	}
/**
 * @엑셀 다운로드를 위한 공통 컨트롤러
 */
	/*
	 *	@ 엑셀다운로드 / 공통
	 * */
	@RequestMapping(value="common", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String commonDownload(@RequestParam HashMap<String,Object> param, ModelMap model) throws Throwable {
		
		// dest 유형 1 :  "map:sqlmapId" 
		// dest 유형 2 :  "srv:service.method"
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		
		String dest = (String) param.get("dest");
		
		if(!dest.equals("")){

			String exeType, sqlMapId, serviceName;
			List<HashMap<String, Object>> alist ;
	
	    	String[] destParam = dest.split(":");
	    	exeType = destParam[0];
	    	//  실행타입이 map 인 경우 바로 exqueryService 실행 
	    	if (exeType.equals("map")) {
	        	sqlMapId = mapRoot + "." + destParam[1];
	        	alist = exqueryService.selectList(sqlMapId, param);
	    	} 
	    	// 실행타입이 srv 인 경우 지정한 서비스.메서드() 실행 
	    	else {  // exeType == "srv"
	    		serviceName = destParam[1];
	        	String[] serviceParam = serviceName.split("\\.");
	        	String beanName = serviceParam[0];
	        	String methodName = serviceParam[1];
	
	        	Object data = null; // 서비스의 실행결과
	    		try {
	    			Object bean = context.getBean(beanName);
	    			data =  bean.getClass().getMethod(methodName, new Class[] {HashMap.class}).invoke(bean, new Object[] {param});	
	    			
	    		} catch (Exception e) {
	    			log.debug("Error::RemoteController.runService()", e);
	    		}
	    		alist = (List<HashMap<String, Object>>)data;
	    	}
	
	    	aMap.put("list", alist);

		}
    	String title = (String)param.get("title");
		String fileName = (String)param.get("file");
		String fname = title +  "_" + getNowDateTime() + ".xls";

		
		param.put("excelDate", getNowDateTimeExcel());
		aMap.put("disp", param);
		
		model.put("data", aMap);
		model.put("templateFileName", fileName);
		model.put("destFileName", fname);
		
		return "jxlsView";
		
	}
	
	
	/*
	 *	@ 엑셀다운로드 / 공정조건비교
	 * */
	@RequestMapping(value="processComp", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String processComp(@RequestParam HashMap<String,Object> param, ModelMap model) throws Throwable {
		
		List<HashMap<String, Object>> compList = exqueryService.selectList("nse.bpTagValueSum.selectComparingList", param); 
		
		String title = (String)param.get("title");		
		String fname = title +  "_" + getNowDateTime() + ".xls";
		String fileName = "processCompReport.xls";
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		
		param.put("excelDate", 	getNowDateTimeExcel());
		aMap.put("disp", 		param);
		aMap.put("list", 		compList);
		
		model.put("data", 		aMap);
		model.put("templateFileName", fileName);
		model.put("destFileName", fname);
		
		return "jxlsView";
		
	}
	
	
}
