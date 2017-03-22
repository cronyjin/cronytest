package com.daehanins.common;

import java.util.HashMap;
import java.util.List;

public class RemoteResult {
	private int code = 1;	// 0 ~ -999 는 에러 
	private String message = "OK";
	private Object result;

	public RemoteResult() {

	}

	public RemoteResult(HashMap<String, Object> result) {
		this.result = result;
	}

	public RemoteResult(List<HashMap<String, Object>> result) {
		this.result = result;
	}

	public RemoteResult(int result) {
		this.result = result;
	}

	public RemoteResult(int code, String message, HashMap<String, Object> result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public RemoteResult(int code, String message, List<HashMap<String, Object>> result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public RemoteResult(int code, String message, int result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
		
}
