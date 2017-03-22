package kr.co.user.weding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.daehanins.common.NbpmsConst;

import kr.co.user.weding.service.CommonService;
import select.spring.exquery.service.ExqueryService;

@Service("commonService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CommonServiceImpl implements CommonService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;

	@Autowired
	private HttpSession session;

	/**
	 * 기본정보 조회
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> startMenu(HashMap<String, Object> param) throws Exception {
		
    	// session check
    	try{
    		if(session == null || session.getAttribute("ssUserId") == null) {
    			throw new Exception();
    		}
    	} catch (Exception e) {
    		throw new Exception();
    	}

		HashMap<String, Object> rMap = new HashMap<String, Object>();
		HashMap<String, Object> cMap = new HashMap<String, Object>();
		
		// 메시지 가져오기
		List<HashMap<String, Object>> mlist = exqueryService.selectList("nse.bpMessage.selectByParam", param);
    	rMap.put("msg", mlist);
    	
    	// 날짜 가져오기
    	HashMap<String, Object> aMap = exqueryService.selectOne("nse.bpCommon.getToday", param);
    	rMap.put("date", aMap);
    	
    	// 메뉴 가져오기
    	// 메뉴A
    	List<HashMap<String, Object>> alist  = exqueryService.selectList("nse.bpProgram.selectUserProgramListSession", param);
    	rMap.put("menuA", alist);
    	
    	// 메뉴B (즐겨찾기)
    	param.put("favoriteYn", "Y");
    	List<HashMap<String, Object>> blist  = exqueryService.selectList("nse.bpProgram.selectUserProgramListSession", param);
    	rMap.put("menuB", blist);

    	// 시스템설정 가져오기
    	cMap = exqueryService.selectOne("nse.bpConf.selectConf", param);
    	rMap.put("logUse", 		cMap.get("logUse").toString());
    	
    	// user정보 가져오기
    	rMap.put("userId", 		param.get("ssUserId").toString());
    	rMap.put("userLang",	param.get("ssUserLang").toString());

    	return rMap;
	}
	
	/**
	 * 숙성실 기본정보 조회
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> startAgeMenu(HashMap<String, Object> param) throws Exception {
		
		// session check
		session.setAttribute("ssUserId", "anonymous");
		session.setAttribute("ssUserLang", "ch");
		param.put("ssSqlLang", session.getAttribute("ssSqlLang"));
		param.put("ssUserLang", session.getAttribute("ssUserLang"));
		
		HashMap<String, Object> rMap = new HashMap<String, Object>();
		HashMap<String, Object> cMap = new HashMap<String, Object>();
		
		// 메시지 가져오기
		List<HashMap<String, Object>> mlist = exqueryService.selectList("nse.bpMessage.selectByParam", param);
		rMap.put("msg", mlist);
		
		// 날짜 가져오기
		HashMap<String, Object> aMap = exqueryService.selectOne("nse.bpCommon.getToday", param);
		rMap.put("date", aMap);
		
		
		// 시스템설정 가져오기
		cMap = exqueryService.selectOne("nse.bpConf.selectConf", param);
		rMap.put("logUse", 		cMap.get("logUse").toString());
		
		// user정보 가져오기
//		rMap.put("userId", 		param.get("ssUserId").toString());
		rMap.put("userLang",	param.get("ssUserLang").toString());
		
		return rMap;
	}	
	
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public int getPageNo(HashMap<String, Object> param){
		return Integer.parseInt(param.get("pageNo").toString());
	}
	
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public int getPageSize(HashMap<String, Object> param){
		return Integer.parseInt(param.get("pageSize").toString());
	}
	
	/**
	 * 표시순서 정렬 업데이트
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public void sortShowOrder(String sqlMap, HashMap<String, Object> param) throws Exception {
		String sign;
		int newOrder = Integer.parseInt(param.get("newOrder").toString());
		int oldOrder = Integer.parseInt(param.get("oldOrder").toString());
		sign =  (newOrder < oldOrder) ? "+" : "-" ;
		if(newOrder == oldOrder){
			sign = null;
		}
		HashMap<String, Object> aMap = param;
		aMap.put("sign", sign);
		aMap.put("newOrder", newOrder);
		aMap.put("oldOrder", oldOrder);

		if(sign != null){
			exqueryService.update(sqlMap, aMap);
		}
		
	}
	
	
	/**
	 * codeGbCd값으로 code 전체값 추출하여 select box타입으로 반환 
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getCodeSelectValue(HashMap<String, Object> param) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> rList = new ArrayList<HashMap<String, Object>>();
		resultMap.put("codeGbCd", param.get("codeGbCd"));
		rList = exqueryService.selectList("nse.bpAlarm.deleteAlarm", param);
		resultMap.put("isExist", true);
		resultMap.put("result", rList);
		return resultMap;	
	}
	
	/**
	 * 메뉴별로 설정사항을 model에 담아서 리턴한다.
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public void setMenuConfigValue(String menuUrl, ModelMap model){
		
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		aMap.put("url", menuUrl);
		HashMap<String, Object> bMap = exqueryService.selectOne("nse.bpProgram.selectMenuOne", aMap);
		if(bMap != null) {
			model.addAttribute("comYearSearchRange",	bMap.get("yearSearchRange"));
			model.addAttribute("comDaySearchRange",		bMap.get("daySearchRange"));
			model.addAttribute("comWorkgroupSearchYn",	bMap.get("workgroupSearchYn"));
			model.addAttribute("comChartColor1",		bMap.get("chartColor1"));
			model.addAttribute("comChartColor2",		bMap.get("chartColor2"));
			model.addAttribute("comChartColor3",		bMap.get("chartColor3"));
			model.addAttribute("comChartColor4",		bMap.get("chartColor4"));
			model.addAttribute("comPageRowCnt",			bMap.get("pageRowCnt"));
			model.addAttribute("comPageShowCnt",		bMap.get("pageShowCnt"));
			model.addAttribute("comMaxFileSize",		bMap.get("maxFileSize"));
			model.addAttribute("comExcelDownYn",		bMap.get("excelDownYn"));
			model.addAttribute("comDecimalPoint",		bMap.get("decimalPoint"));
			
			if(bMap.get("yearSearchRange") == null || Integer.parseInt( bMap.get("yearSearchRange").toString()) == 0){
				// 기본 1년
				model.addAttribute("comYearSearchRange",		"1");
			} else {
				model.addAttribute("comYearSearchRange",		bMap.get("yearSearchRange"));
			}
			
			if(bMap.get("daySearchRange") == null || Integer.parseInt( bMap.get("daySearchRange").toString()) == 0){
				// 15일
				model.addAttribute("comDaySearchRange",			"15");
			} else {
				model.addAttribute("comDaySearchRange",			bMap.get("daySearchRange"));
			}			
			
			if(bMap.get("pageRowCnt") == null || Integer.parseInt( bMap.get("pageRowCnt").toString()) == 0){
				// 1 row 20 개
				model.addAttribute("comPageRowCnt",			"20");
			} else {
				model.addAttribute("comPageRowCnt",			bMap.get("pageRowCnt"));
			}
			
			if(bMap.get("pageShowCnt") == null || Integer.parseInt( bMap.get("pageShowCnt").toString()) < 3){
				// 페이지 버튼 갯수
				model.addAttribute("comPageShowCnt",		"3");
			} else {
				model.addAttribute("comPageShowCnt",		bMap.get("pageShowCnt"));
			}
			
			if(bMap.get("pollingTime") == null || Integer.parseInt( bMap.get("pollingTime").toString()) == 0){
				// 기본 5분
				model.addAttribute("comPollingTime",		"5");
			} else {
				model.addAttribute("comPollingTime",		bMap.get("pollingTime"));
			}
			
			if(bMap.get("maxFileSize") == null || Integer.parseInt( bMap.get("maxFileSize").toString()) == 0){
				// 파일업로드 사이즈
				model.addAttribute("comMaxFileSize",		"10");
			} else {
				model.addAttribute("comMaxFileSize",		bMap.get("maxFileSize"));
			}
			
			if(bMap.get("decimalPoint") == null || Integer.parseInt( bMap.get("decimalPoint").toString()) == 0){
				// 소수점
				model.addAttribute("comDecimalPoint",		"0");
			} else {
				model.addAttribute("comDecimalPoint",		bMap.get("decimalPoint"));
			}
			
			if(bMap.get("chartColor1") == null){
				// 차트칼라1
				model.addAttribute("comChartColor1",		"d21e1f");
			}
			
			if(bMap.get("chartColor2") == null){
				// 차트칼라1
				model.addAttribute("comChartColor2",		"d21e1f");
			}
			
			if(bMap.get("chartColor3") == null){
				// 차트칼라1
				model.addAttribute("comChartColor3",		"d21e1f");
			}
			
		}
	}
	
	/**
	 * Get Date-Tile
	 * @param String startDate, String endDate
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public String getDateTitle(String startDate, String endDate){
		
		String str = "";
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		resultMap = exqueryService.selectOne("nse.bpCommon.selectDateFormat", queryMap);
		
		return (String)resultMap.get("dateFormat");
	}

	/**
	 * 메인화면 조회
	 * @param String startDate, String endDate
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> getMainInfo(HashMap<String, Object> param){
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> aMap = new HashMap<String, Object>();
		HashMap<String, Object> r1Map = new HashMap<String, Object>();
		HashMap<String, Object> r2Map = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> r2List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> r3List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> r4List = new ArrayList<HashMap<String, Object>>();
		
		//설비종합효율조회
		
		//최상위 공정
		aMap.put("procId", NbpmsConst.PROC_ROOT_ID);
		aMap.put("ssSqlLang", param.get("ssSqlLang"));
		r1Map = exqueryService.selectOne("nse.bpProdProcOee.selectMainOee", aMap);
		
		
		//완제품조회 
		r2List = exqueryService.selectList("nse.bpGoods.selectMainGoodsSum", aMap);

		//필터현황조회 
//		r3List = analService.selectFilter(param);
		r3List = exqueryService.selectList("nse.bpAnalTag.selectMainFilter", aMap);
		
		//부품점검현황 
		r4List = exqueryService.selectList("nse.bpParts.selctPartsCountMain", aMap);

		//공지사항 
		r2Map = exqueryService.selectOne("nse.bpBoard.selectNoticeMain", aMap);
		
		resultMap.put("r1", r1Map);
		resultMap.put("r2", r2List);
		resultMap.put("r3", r3List);
		resultMap.put("r4", r4List);
		resultMap.put("r5", r2Map);
		
		return resultMap;
	}
	
}
