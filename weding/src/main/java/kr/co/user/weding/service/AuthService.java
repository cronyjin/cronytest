package kr.co.user.weding.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface AuthService {

	/**
	 * 사용자 입력
	 */
	public HashMap<String, Object> insertUser(HashMap<String, Object> param) throws Exception;
	/**
	 * 사용자 수정
	 */
	public HashMap<String, Object> updateUser(HashMap<String, Object> param) throws Exception;
	/**
	 * 사용자 수정
	 */
	public HashMap<String, Object> deleteUser(HashMap<String, Object> param) throws Exception;
	
	/**
	 * 역할 입력
	 */
	public HashMap<String, Object> insertRole(HashMap<String, Object> param) throws Exception;
	/**
	 * 역할 수정
	 */
	public HashMap<String, Object> updateRole(HashMap<String, Object> param) throws Exception;
	/**
	 * 역할 수정
	 */
	public HashMap<String, Object> deleteRole(HashMap<String, Object> param) throws Exception;

	/**
	 * 메뉴 입력
	 */
	public HashMap<String, Object> insertProgram(HashMap<String, Object> param) throws Exception;
	/**
	 * 메뉴 수정
	 */
	public HashMap<String, Object> updateProgram(HashMap<String, Object> param) throws Exception;
	/**
	 * 메뉴 수정
	 */
	public HashMap<String, Object> deleteProgram(HashMap<String, Object> param) throws Exception;
	
	/**
	 * 사용자메뉴 입력
	 */
	public HashMap<String, Object> insertUserMenu(HashMap<String, Object> param) throws Exception;
	/**
	 * 사용자메뉴 수정
	 */
	public HashMap<String, Object> updateUserMenu(HashMap<String, Object> param) throws Exception;
	/**
	 * 사용자메뉴 수정
	 */
	public HashMap<String, Object> deleteUserMenu(HashMap<String, Object> param) throws Exception;

	/**
	 * 개인정보수정 - 비밀번호 확인
	 */
	public HashMap<String, Object> perInfoPassCheck(HashMap<String, Object> param) throws Exception;
	/**
	 * 개인정보수정 - 수정(개인정보 탭)
	 */
	public HashMap<String, Object> updatePerInfoPersonal(HashMap<String, Object> param) throws Exception;

	/**
	 * 개인정보수정 - 수정(화면설정 탭)
	 */
	public HashMap<String, Object> updatePerInfoDisplay(HashMap<String, Object> param) throws Exception;

	/**
	 * 개인정보수정 - 수정(즐겨찾기 탭)
	 */	
	public HashMap<String, Object> updatePerInfoBookmark(HashMap<String, Object> param) throws Exception;

}
