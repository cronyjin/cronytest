package kr.co.user.weding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.user.weding.service.AuthService;
import kr.co.user.weding.service.ConstraintService;
import select.spring.exquery.service.ExqueryService;

@Service("authService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AuthServiceImpl implements AuthService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExqueryService exqueryService;
	
	@Autowired
	private ConstraintService constraintService;
	
	/**
	 * 사용자 입력
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertUser(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'USER_ID','colValue':'"+param.get("userId")+"'}";
		String con = "["
				 +"{'tableName':'BP_USER', 'pkList':["+pk+"], 'msgCode':'E026'}"
				+"]";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			//저장처리
			exqueryService.insert("nse.bpUser.insertUser", param);
			
			//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
			resultMap.put("pagePk", param.get("userId"));
			isExist = true;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}
	
	/**
	 * 사용자 수정
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updateUser(HashMap<String, Object> param) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isExist = false;
		
		//수정처리
		int resultCnt = exqueryService.update("nse.bpUser.updateUser", param);

		//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
		resultMap.put("pagePk", param.get("userId"));
		isExist =  resultCnt > 0 ;
		
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}

	/**
	 * 사용자삭제
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> deleteUser(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'USER_ID','colValue':'"+param.get("userId")+"'}";
		String con = "["
				 +"{'tableName':'BP_USER_PROGRAM', 'pkList':["+pk+"], 'msgCode':'E028'}"//사용자프로그램
				+"]";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			//저장처리
			int resultCnt = exqueryService.delete("nse.bpUser.deleteUser", param);

			isExist =  resultCnt > 0 ;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}

	/**
	 * 역할 입력
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertRole(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'ROLE_CD','colValue':'"+param.get("roleCd")+"'}";
		String con = "["
				 +"{'tableName':'BP_ROLE', 'pkList':["+pk+"], 'msgCode':'E001'}"
				+"]";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			//저장처리
			exqueryService.insert("nse.bpRole.insertRole", param);
			
			// json ArrayList를 Java List로 변환
			ArrayList<HashMap<String, Object>> alist = new ArrayList<HashMap<String,Object>>();
	       	ObjectMapper mapper = new ObjectMapper();
	       	alist = mapper.readValue(param.get("menuList").toString(), new TypeReference<ArrayList<HashMap<String, Object>>>(){});
			
	       	HashMap<String, Object> menuMap = new HashMap<String, Object>();
			menuMap.put("roleCd", param.get("roleCd"));	
			menuMap.put("ssUserId", param.get("ssUserId"));	

			for(int i = 0, len = alist.size(); i < len ; i++ ){
		
				menuMap.put("programCd", alist.get(i).get("programCd"));
				
				menuMap.put("exe", alist.get(i).get("exe"));
				menuMap.put("read", alist.get(i).get("read"));
				
				exqueryService.insert("nse.bpRole.insertRoleMenu", menuMap);
			}
			
			
			//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
			resultMap.put("pagePk", param.get("roleCd"));
			isExist = true;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}
	
	/**
	 * 역할 수정
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updateRole(HashMap<String, Object> param) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isExist = false;
		
		// json ArrayList를 Java List로 변환
		ArrayList<HashMap<String, Object>> alist = new ArrayList<HashMap<String,Object>>();
       	ObjectMapper mapper = new ObjectMapper();
       	alist = mapper.readValue(param.get("menuList").toString(), new TypeReference<ArrayList<HashMap<String, Object>>>(){});
       	
       	HashMap<String, Object> aMap = param;
		aMap.put("menuList", alist);
		
		HashMap<String, Object> menuMap = new HashMap<String, Object>();
		menuMap.put("roleCd", param.get("roleCd"));	
		menuMap.put("ssUserId", param.get("ssUserId"));	

		for(int i = 0, len = alist.size(); i < len ; i++ ){
	
			menuMap.put("programCd", alist.get(i).get("programCd"));
			
			
			menuMap.put("exe", alist.get(i).get("exe"));
			menuMap.put("read", alist.get(i).get("read"));
			
			exqueryService.update("nse.bpRole.updateRoleMenu", menuMap);
		}
				
		//수정처리
		int resultCnt = exqueryService.update("nse.bpRole.updateRole", param);

		//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
		resultMap.put("pagePk", param.get("roleCd"));
		isExist =  resultCnt > 0 ;
		
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}

	/**
	 * 역할 삭제
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> deleteRole(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'ROLE_CD','colValue':'"+param.get("roleCd")+"'}";
		String con = "";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			
			exqueryService.delete("nse.bpRole.deleteRoleMenu", param);
			int resultCnt = exqueryService.delete("nse.bpRole.deleteRole", param);

			
			isExist =  resultCnt > 0 ;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}

	/**
	 * 메뉴 입력
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertProgram(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'PROGRAM_CD','colValue':'"+param.get("programCd")+"'}";
		String con = "";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			
			HashMap<String, Object> sMap = new HashMap<String, Object>();
			List<HashMap<String,Object>> uList = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String,Object>> rList = new ArrayList<HashMap<String, Object>>();
			
			sMap = exqueryService.selectOne("nse.bpProgram.selectProgramNewOne", param);
			
			param.put("programCd", "m"+sMap.get("programCd"));
			
			//ORDER NUM 구하기
			HashMap<String,Object> pMap = exqueryService.selectOne("nse.bpProgram.selectMenuOrderNum", param);
			param.put("showOrder", pMap.get("orderNum"));

			
			
			//역할수 조회
			rList = exqueryService.selectList("nse.bpProgram.selectRoleList", param);
			//사용자수 조회
			uList = exqueryService.selectList("nse.bpProgram.selectUserList", param);
			//역할수 만큼 등록
			for(int i = 0; i < rList.size(); i++){
				param.put("roleCd", rList.get(i).get("roleCd"));
				exqueryService.insert("nse.bpProgram.insertProgramRole", param);
			}
			//사용자 수 만큼 등록
			for(int j = 0; j < uList.size(); j++){
				param.put("userId", uList.get(j).get("userId"));
				exqueryService.insert("nse.bpProgram.insertProgramUser", param);
			}
			
			//메뉴등록
			exqueryService.insert("nse.bpProgram.insertProgram", param);
			
			//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
			resultMap.put("pagePk", param.get("programCd"));
			
			isExist = true;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}
	
	/**
	 * 메뉴 수정
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updateProgram(HashMap<String, Object> param) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isExist = false;
		
		String sign;
		int newOrder = Integer.parseInt((String)param.get("newOrder"));
		int oldOrder = Integer.parseInt((String)param.get("oldOrder"));
		sign =  (newOrder < oldOrder) ? "+" : "-" ;
		if(newOrder == oldOrder){
			sign = null;
		}

		HashMap<String, Object> aMap = param;
		aMap.put("sign", sign);
		aMap.put("newOrder", newOrder);
		aMap.put("oldOrder", oldOrder);
		aMap.put("programCd", param.get("programCd"));
		if(sign != null){
			exqueryService.update("nse.bpProgram.updateProgramOrder", aMap);
		}
		
		int resultCnt = exqueryService.update("nse.bpProgram.updateProgram", aMap);

		
		if(param.get("showYn").equals("N")){
			exqueryService.update("nse.bpProgram.updateMainCdProgram", aMap);
		}
		
		//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
		resultMap.put("pagePk", param.get("programCd"));
		isExist =  resultCnt > 0 ;
		
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}

	/**
	 * 메뉴 삭제
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> deleteProgram(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'PROGRAM_CD','colValue':'"+param.get("programCd")+"'}";
		String con = "["
				 +"{'tableName':'BP_PROGRAM', 'pkList':[{'colName':'UPPER_PROGRAM_CD','colValue':'"+param.get("programCd")+"'}], 'msgCode':'E002'}"
				+"]";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			
			//표시순서 처리해줌
			String sign;
			int newOrder = 999; //임의의 큰수를 넣어줌
			int oldOrder = Integer.parseInt((String)param.get("oldOrder"));
			sign =  (newOrder < oldOrder) ? "+" : "-" ;
			if(newOrder == oldOrder){
				sign = null;
			}
			
			HashMap<String, Object> aMap = param;
			aMap.put("sign", sign);
			aMap.put("newOrder", newOrder);
			aMap.put("oldOrder", oldOrder);
			aMap.put("programCd", param.get("programCd"));
			if(sign != null){
				exqueryService.update("nse.bpProgram.updateProgramOrder", aMap);
			}
			
			//저장처리
//			int resultCnt = exqueryService.delete("nse.bpRole.deleteRole", param);
			
			exqueryService.delete("nse.bpProgram.deleteUserProgram", param);

			exqueryService.delete("nse.bpProgram.deleteRoleProgram", param);

			int resultCnt = exqueryService.delete("nse.bpProgram.deleteProgram", param);

			
			
			isExist =  resultCnt > 0 ;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}
	
	/**
	 * 사용자메뉴 입력
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertUserMenu(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'USER_ID','colValue':'"+param.get("userId")+"'}, {'colName':'PROGRAM_CD','colValue':'"+param.get("programCd")+"'}";
		String con = "["
				 +"{'tableName':'BP_USER_PROGRAM', 'pkList':["+pk+"], 'msgCode':'E001'}"
				+"]";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			//저장처리
//			exqueryService.insert("nse.bpRole.insertRole", param);
			
			// json ArrayList를 Java List로 변환
			ArrayList<HashMap<String, Object>> alist = new ArrayList<HashMap<String,Object>>();
	       	ObjectMapper mapper = new ObjectMapper();
	       	alist = mapper.readValue(param.get("menuList").toString(), new TypeReference<ArrayList<HashMap<String, Object>>>(){});
			
	       	HashMap<String, Object> menuMap = new HashMap<String, Object>();
			menuMap.put("userId", param.get("userId"));	
			menuMap.put("ssUserId", param.get("ssUserId"));	
			
			for(int i = 0, len = alist.size(); i < len ; i++ ){
		
				menuMap.put("programCd", alist.get(i).get("programCd"));
				
				menuMap.put("exe", alist.get(i).get("exe"));
				menuMap.put("read", alist.get(i).get("read"));
				
				exqueryService.insert("nse.bpUser.insertUserMenu", menuMap);
			}
			
			exqueryService.update("nse.bpUser.updateBizGrp", param);
			
			
			//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
			resultMap.put("pagePk", param.get("userId"));
			isExist = true;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}
	
	/**
	 * 사용자메뉴 수정
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updateUserMenu(HashMap<String, Object> param) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isExist = false;
		
		// json ArrayList를 Java List로 변환
		ArrayList<HashMap<String, Object>> alist = new ArrayList<HashMap<String,Object>>();
       	ObjectMapper mapper = new ObjectMapper();
       	alist = mapper.readValue(param.get("menuList").toString(), new TypeReference<ArrayList<HashMap<String, Object>>>(){});
       	
       	HashMap<String, Object> aMap = param;
		aMap.put("menuList", alist);
		
		HashMap<String, Object> menuMap = new HashMap<String, Object>();
		menuMap.put("userId", 	param.get("userId"));	
		menuMap.put("ssUserId", param.get("ssUserId"));	
		
		for(int i = 0, len = alist.size(); i < len ; i++ ){
	
			menuMap.put("programCd", alist.get(i).get("programCd"));
			
			
			menuMap.put("exe", alist.get(i).get("exe"));
			menuMap.put("read", alist.get(i).get("read"));
			
			exqueryService.update("nse.bpUser.updateUserMenu", menuMap);
			
			if(menuMap.get("exe").equals("N")){
				exqueryService.update("nse.bpUser.updateUserMainMenu", menuMap);
			}
		}
		
		exqueryService.update("nse.bpUser.updateBizGrp", param);

		
		//신규등록한 key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
		resultMap.put("pagePk", param.get("userId"));
		isExist =  true;
		
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}

	/**
	 * 사용자메뉴 삭제
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> deleteUserMenu(HashMap<String, Object> param) throws Exception {
		
		//constraint check
		String pk = "{'colName':'USER_ID','colValue':'"+param.get("userId")+"'}";
		String con = "";
		String consResult = constraintService.checkExistsV2(con.replaceAll("'", "\""));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultMsg", consResult);
		boolean isExist = false;
		if(consResult.equals("OK")){
			
			int resultCnt = exqueryService.delete("nse.bpUser.deleteUserMenu", param);

			
			exqueryService.update("nse.bpUser.updateUserMainMenuDel", param);
			
			exqueryService.update("nse.bpUser.deleteBizGrp", param);
			
			//key값을 pagePk로 다시 보내줘야 페이징 처리가 된다.
			resultMap.put("pagePk", param.get("userId"));
			
			isExist =  resultCnt > 0 ;
		}
		resultMap.put("isExist", isExist);
		return resultMap;
		
	}
	
	/**
	 * 개인정보수정 - 비밀번호 체크
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> perInfoPassCheck(HashMap<String, Object> param) throws Exception {

		boolean isPassCheck = false;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> aMap = exqueryService.selectOne("nse.bpUser.selectUserInfoPassOne", param);

		String clientPass = param.get("password").toString(); 
		String serverPass = aMap.get("password").toString();
		
		if( clientPass.equals(serverPass) ) {
			isPassCheck = true;
		}
		resultMap.put("isPassCheck", isPassCheck);
		return resultMap;
	}

	/**
	 * 개인정보수정 - 수정(개인정보 탭)
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updatePerInfoPersonal(HashMap<String, Object> param) throws Exception {

		boolean isExist = false;
		String msg = "V077";
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> aMap = perInfoPassCheck(param);
		if((Boolean)aMap.get("isPassCheck")){
			int cnt = exqueryService.update("nse.bpUser.updateUserInfo", param);
			
			if ( cnt > 0 ) {
				isExist = true;
				msg = "U002";
			}
		}else {
			
			msg = "V018";
		}
		
		resultMap.put("isExist", isExist);
		resultMap.put("msg", msg);			
		
		return resultMap;
		
	}
	
	/**
	 * 개인정보수정 - 수정(화면설정 탭)
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updatePerInfoDisplay(HashMap<String, Object> param) throws Exception {

		boolean isExist = false;
		String msg = "V077";

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		int i = exqueryService.update("nse.bpUser.updateUserInfo", param);
		
		if(i > 0){
			isExist = true;
			msg = "U002";
		}
		resultMap.put("isExist", isExist);
		resultMap.put("msg", msg);

		return resultMap;
		
	}
	
	/**
	 * 개인정보수정 - 수정(즐겨찾기 탭)
	 * @throws Exception  
	 */
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> updatePerInfoBookmark(HashMap<String, Object> param) throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> bookmarkList = new ArrayList<HashMap<String, Object>>();
       	ObjectMapper tagMapper = new ObjectMapper();
       	bookmarkList = tagMapper.readValue(param.get("bookmarkList").toString(), new TypeReference<ArrayList<HashMap<String, Object>>>(){});

       	//초기화
       	exqueryService.update("nse.bpUserProgram.updateUserBookmarkList1", param);
       	
       	//즐겨찾기메뉴등록
       	boolean isExist = false;
		String msg = "V077";
       	
       	try {
       		
       		int cnt = bookmarkList.size();
       		for(int i = 0; i < cnt ; i++ ){
       			HashMap<String, Object> tempMap = new HashMap<String, Object>();
       			tempMap.put("programCd", bookmarkList.get(i).get("programCd"));
       			tempMap.put("ssUserId", param.get("ssUserId"));       		
       			exqueryService.update("nse.bpUserProgram.updateUserBookmarkList2", tempMap);

       		}
       		isExist = true;
       		msg = "U002";
			
		} catch (Exception e) {
			
		}
       	
		resultMap.put("isExist", isExist);
		resultMap.put("msg", msg);

		return resultMap;
	}
	
}
