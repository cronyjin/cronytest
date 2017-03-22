package kr.co.user.weding.service;

import java.util.HashMap;
import java.util.List;


public interface MessageService {

	/**
	 * 현재 5명 대기인원이 남았으니 "도미노피자"로 와주세요. 미루기/취소:http://we.kr/578ueyb
	 */
	public void setMessageBy5(String storeId)  throws Exception ; 
	
}
