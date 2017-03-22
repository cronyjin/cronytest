package com.daehanins.common;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.ApplicationContext;

import select.spring.exquery.service.ExqueryService;

@Controller
@RequestMapping("col")
public class RemoteController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
    ApplicationContext context;
	
	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private HttpSession session;

	private String mapRoot = "nse";
	
    @RequestMapping("remote")
    @ResponseBody
    public HashMap<String,Object> remote(@RequestParam HashMap<String, Object> param) throws Exception {
    	
    	// session check
    	try{
    		if(session == null || session.getAttribute("ssAdminId") == null){
    			throw new Exception();
    		}
    	} catch (Exception e) {
    		throw new Exception();
    	}
    	
    	// dest 포맷 1:  map:sqlMapId:cmd   		cmd는 rl, ro, rp, c, u, d      "map:engCdList:rp"
    	// dest 포맷 2:  srv:service.method:cmd  cmd는 rl, ro, rp, c, u, d      "srv:service.method:rp"

    	String dest = (String) param.get("colDest");
    	
    	String exeType, sqlMapId, serviceName, cmd;
    	HashMap<String, Object> result;

    	String[] destParam = dest.split(":");
    	exeType = destParam[0];
    	if (exeType.equals("map")) {
        	sqlMapId = mapRoot + "." + destParam[1];
        	cmd = destParam[2];
        	result = this.runSqlmap(sqlMapId, cmd, param);
    	} else { // exeType == "srv"
        	serviceName = destParam[1];
        	result = this.runService(serviceName, param);
    	}
    	
    	return result;
    }
    
    private HashMap<String,Object> runSqlmap(String sqlMapId, String cmd, HashMap<String, Object> param) throws Exception {
    	
    	HashMap<String, Object> rMap = new HashMap<String,Object>();
    	int rCount = 0;

    	// read one record
    	if (cmd.equals("r1")) {
    		rMap = exqueryService.selectOneCap(sqlMapId, param);
    	} 
    	// read paging list
    	else if (cmd.equals("rp")) {
    		int pageNo = Integer.parseInt((String) param.get("pageNo"));
    		int pageSize = Integer.parseInt((String) param.get("pageSize"));
    		rMap = exqueryService.selectPagingList(sqlMapId, param, pageNo, pageSize);
    	} 
    	// insert (create)
    	else if (cmd.equals("c")) {
    		rCount = exqueryService.insert(sqlMapId, param);
    		rMap.put("count", rCount);
    	} 
    	// update
    	else if (cmd.equals("u")) {
    		rCount = exqueryService.update(sqlMapId, param);
    		rMap.put("count", rCount);
    	} 
    	// delete
    	else if (cmd.equals("d")) {
    		rCount = exqueryService.delete(sqlMapId, param);
    		rMap.put("count", rCount);
    	} 
    	// read list 
    	else if (cmd.equals("rn")){
    		rMap = exqueryService.selectListCap(sqlMapId, param);
    	}
    	// 기본은 "n" 
    	else {
    		rMap = exqueryService.selectListCap(sqlMapId, param);
    	}
    	return rMap;
    }

    @SuppressWarnings("unchecked")
	private HashMap<String,Object> runService(String serviceName, HashMap<String, Object> param) throws Exception {
    	
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
		return (HashMap<String,Object>)data;
		
    }

}

