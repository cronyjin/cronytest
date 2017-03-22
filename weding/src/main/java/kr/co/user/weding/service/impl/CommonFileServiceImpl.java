package kr.co.user.weding.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.user.weding.service.CommonFileService;
import select.spring.exquery.dao.ExqueryDao;
import select.spring.util.ApplicationProperty;
import select.spring.util.FileUtils;

@Service("commonFileService")
public class CommonFileServiceImpl implements CommonFileService {
	
	@Inject
	@Named("exqueryDao")
	private ExqueryDao exqueryDao;
	
	private String uploadPath = ApplicationProperty.get("upload.path");
	
	private static final String  NEW_INIT 		= "newInit";
	private static final String  NEW_MODIFIED	= "newModified";
	private static final String  NOT_MODIFIED	= "notModified";
	private static final String  DATA_MODIFIED	= "dataModified";
	private static final String  DATA_DELETED	= "dataDeleted";
	
	
	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	public int insertWithFile(String key, HashMap<String, Object> param, MultipartFile file) {
		
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		
		// 파일정보 설정 & 실제 저장될 파일명
		String saveFileName = "";
		int saveId = 0;
		
		String fileState = (String)param.get(key + "State");
		// 신규입력 상태에서는 파일상태가 아래의 상태들만 가능함
		if (fileState.equals(NEW_INIT)) {
			// 처리할 내용 없음
			saveId = 0;
			
		} else if (fileState.equals(NEW_MODIFIED)) {
			// new 신규입력후 id 구함
			// 파일 저장
			try {
			saveFileName = FileUtils.saveUniqeName(file.getOriginalFilename(), uploadPath, file.getBytes());
			} catch (Exception e) {
				// TODO:: 저장오류 발생시 처리 보완
			}
			pMap.put("fileType", saveFileName.substring( saveFileName.lastIndexOf( "." ) + 1 ));
			pMap.put("orgFileName", file.getOriginalFilename());
			pMap.put("saveFileName", saveFileName);
			pMap.put("savePath", uploadPath);
			pMap.put("fileSize", file.getSize());
			exqueryDao.insert("nse.bpAttachFile.insert", pMap);
//			pMap.put("articleId", param.get("articleId"));
//			exqueryDao.insert("nse.bpBoardAttach.insert", pMap);
			saveId = ((Integer)pMap.get("fileId")).intValue();
		} 
		
		// 나머지 데이터 처리 로직
		
		return saveId;
	}
  
	
	@SuppressWarnings("unchecked")
	public int updateWithFile(String key, HashMap<String, Object> param, MultipartFile file) {

		HashMap<String, Object> pMap = new HashMap<String, Object>();
		
		// 파일정보 설정 & 실제 저장될 파일명
		String saveFileName = "";
		int saveId = 0;
		
		String fileState = (String)param.get(key + "State");
		// 아래 IF 부분은 전체 상태를 고려한 것으로, 메서드별로 일부만 사용하면 됩니다.
		if (fileState.equals(NEW_INIT) || fileState.equals(NOT_MODIFIED) ) {
			saveId = Integer.parseInt(param.get(key + "OrgFileId").toString());
			
		} else if (fileState.equals(NEW_MODIFIED)) {
			// new 신규입력후 id 구함
			// 파일 저장
			try {
			saveFileName = FileUtils.saveUniqeName(file.getOriginalFilename(), uploadPath, file.getBytes());
			} catch (Exception e) {
				// TODO:: 저장오류 발생시 처리 보
			}
			pMap.put("fileType", saveFileName.substring( saveFileName.lastIndexOf( "." ) + 1 ));
			pMap.put("orgFileName", file.getOriginalFilename());
			pMap.put("saveFileName", saveFileName);
			pMap.put("savePath", uploadPath);
			pMap.put("fileSize", file.getSize());
			exqueryDao.insert("nse.bpAttachFile.insert", pMap);
			saveId = ((Integer)pMap.get("fileId")).intValue();
			
		} else if (fileState.equals(DATA_MODIFIED)) {
			// org 파일의 정보를 읽어서 저장되어 있던 파일을 삭제
			saveId = Integer.parseInt(param.get(key + "OrgFileId").toString());
			pMap.put("fileId", saveId);
			HashMap<String, Object> orgFile = exqueryDao.selectOne("nse.bpAttachFile.getFileInfo", pMap);
			String orgSavedFileName = (String)orgFile.get("saveFileName");
			String orgSavedPath = (String)orgFile.get("savePath");
			FileUtils.remove(orgSavedFileName, orgSavedPath);
			
			// org FileId의 정보를 테이블에서 삭제  <-- 삭제후 입력시는 이 부분이 필요함, 단 아래의 update가 insert로 되어야 한다.
			// exqueryDao.delete("com.daehanins.knserp.attachfile.delete", pMap);

			// 새 파일 저장
			try {
			saveFileName = FileUtils.saveUniqeName(file.getOriginalFilename(), uploadPath, file.getBytes());
			} catch (Exception e) {
				// TODO:: 저장오류 발생시 처리 보
			}
			pMap.put("fileType", saveFileName.substring( saveFileName.lastIndexOf( "." ) + 1 ));
			pMap.put("orgFileName", file.getOriginalFilename());
			pMap.put("saveFileName", saveFileName);
			pMap.put("savePath", uploadPath);
			pMap.put("fileSize", file.getSize());
			
			// 새 파일의 정보를 테이블에 업데이트
			exqueryDao.update("nse.bpAttachFile.update", pMap);
			saveId = (Integer)pMap.get("fileId");
			
		} else if (fileState.equals(DATA_DELETED)) {
			// org 파일의 정보를 읽어서 저장되어 있던 파일을 삭제
			saveId = Integer.parseInt(param.get(key + "OrgFileId").toString());
			pMap.put("fileId", saveId);
			pMap.put("articleId", param.get("articleId"));
			HashMap<String, Object> orgFile = exqueryDao.selectOne("nse.bpAttachFile.getFileInfo", pMap);
			String orgSavedFileName = (String)orgFile.get("saveFileName");
			String orgSavedPath = (String)orgFile.get("savePath");
			FileUtils.remove(orgSavedFileName, orgSavedPath);
			// org FileId의 정보를 테이블에서 삭제
			exqueryDao.delete("nse.bpBoardAttach.delete", pMap);
			exqueryDao.delete("nse.bpAttachFile.delete", pMap);
			saveId = 0;

		}

		// 나머지 데이터 처리 로직
		
		return saveId;
	}

	@SuppressWarnings("unchecked")
	public int deleteWithFile(String key, HashMap<String, Object> param) {

		HashMap<String, Object> pMap = new HashMap<String, Object>();
		
		int saveId = 0;
		saveId = Integer.parseInt(param.get(key + "OrgFileId").toString());

		// 파일이 포함된 원본이 삭제되므로 state와 무관하게 org 삭제처리함
		// org 파일의 정보를 읽어서 저장되어 있던 파일을 삭제
		
		if (saveId == 0)  return 0;
		
		pMap.put("fileId", saveId); 
//		pMap.put("articleId", param.get("articleId"));
//		exqueryDao.delete("nse.bpBoardAttach.delete", pMap);
		
		HashMap<String, Object> orgFile = exqueryDao.selectOne("nse.bpAttachFile.getFileInfo", pMap);
		String orgSavedFileName = (String)orgFile.get("saveFileName");
		String orgSavedPath = (String)orgFile.get("savePath");
		FileUtils.remove(orgSavedFileName, orgSavedPath);
		// org FileId의 정보를 테이블에서 삭제
		exqueryDao.delete("nse.bpAttachFile.delete", pMap);

		return saveId;
	}
	
	@SuppressWarnings("unchecked")
	public int deleteWithFileId(String key, HashMap<String, Object> param) {
		
		HashMap<String, Object> pMap = new HashMap<String, Object>();
		
		int saveId = 0;
		saveId = Integer.parseInt(key);
		
		// 파일이 포함된 원본이 삭제되므로 state와 무관하게 org 삭제처리함
		// org 파일의 정보를 읽어서 저장되어 있던 파일을 삭제
		
		if (saveId == 0)  return 0;
		
		pMap.put("fileId", saveId);
		HashMap<String, Object> orgFile = exqueryDao.selectOne("nse.bpAttachFile.getFileInfo", pMap);
		String orgSavedFileName = (String)orgFile.get("saveFileName");
		String orgSavedPath = (String)orgFile.get("savePath");
		FileUtils.remove(orgSavedFileName, orgSavedPath);
		// org FileId의 정보를 테이블에서 삭제
		exqueryDao.delete("nse.bpAttachFile.delete", pMap);
		
		return saveId;
	}

	public HashMap<String, Object> readFile(String key, HashMap<String, Object> param) {

		HashMap<String, Object> pMap = new HashMap<String, Object>();

		int saveId = 0;
		saveId = Integer.parseInt(param.get(key + "OrgFileId").toString());

		pMap.put("fileId", saveId);
		HashMap<String, Object> orgFile = exqueryDao.selectOne("nse.bpAttachFile.getFileInfo", pMap);

		HashMap<String, Object> rMap = new HashMap<String, Object>();

		rMap.put(key + "State", 		NOT_MODIFIED);
		rMap.put(key + "OrgFileId", 	((Long)orgFile.get("fileId")).intValue());
		rMap.put(key + "OrgFileName", 	(String)orgFile.get("orgFileName"));
		return rMap;
		
	}
	

}
