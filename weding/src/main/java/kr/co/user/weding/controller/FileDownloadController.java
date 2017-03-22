package kr.co.user.weding.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import select.spring.exquery.dao.ExqueryDao;
import select.spring.exquery.vo.ExFileInfo;

@Controller
public class FileDownloadController {
	
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@Inject
	@Named("exqueryDao")
	private ExqueryDao exqueryDao;
	
	/**
	 * 파일다운로드 실행 
	 */
	@RequestMapping("/file/download.do")
	public String downJuchulRepot(@RequestParam HashMap<String,Object> param, ModelMap model) throws Throwable {
		
		HashMap<String, Object> fileInfo = exqueryDao.selectOne("nse.bpBoardAttach.selectFileInfo", param);

		String fullName =  fileInfo.get("savePath").toString() + fileInfo.get("saveFileName").toString();
		File file = new File(fullName);
		
		model.put("file", file);
		model.put("fileName", (String) fileInfo.get("orgFileName"));
		model.put("fileType", (String) fileInfo.get("fileType"));

		return "fileDownView";
	}
	/**
	 * 파일다운로드 실행 
	 * todo: file다운 로그남기기 위해서 임시 추가.
	 */
	@RequestMapping("/file/downloadLog.do")
	public String downJuchulRepotLog(@RequestParam HashMap<String,Object> param, ModelMap model) throws Throwable {
		
		HashMap<String, Object> fileInfo = exqueryDao.selectOne("nse.bpBoardAttach.selectFileInfo", param);
		
		String fullName =  fileInfo.get("savePath").toString() + fileInfo.get("saveFileName").toString();
		File file = new File(fullName);
		
		model.put("file", file);
		model.put("fileName", (String) fileInfo.get("orgFileName"));
		model.put("fileType", (String) fileInfo.get("fileType"));
		
		return "fileDownView";
	}

	@RequestMapping(value = "/fileForm.do", method = RequestMethod.GET)
	public String fileForm() {
		logger.info("/fileForm test started!");
		
		return "f:fileForm";
	}

	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
	public String fileUpload(HttpServletRequest request) {
		MultipartHttpServletRequest mpRequest = (MultipartHttpServletRequest) request;
		Iterator<String> fileNameIterator = mpRequest.getFileNames();
		
		String name = request.getParameter("name");
		while (fileNameIterator.hasNext()) {
			MultipartFile multiFile = mpRequest.getFile((String) fileNameIterator.next());
			
			long fileSize = multiFile.getSize();
			String fileName = multiFile.getName();
			String orgFileName = multiFile.getOriginalFilename();
			String contentType = multiFile.getContentType();
			
			if (multiFile.getSize() > 0) {
				logger.info("/fileUpload test started!" + multiFile.getSize() );
			}
		}
		
		logger.info("/fileUpload test started!");
		
		
		return "f:fileUpload";
	}

	@RequestMapping(value = "/fileUpload2.do", method = RequestMethod.POST)
	public String fileUpload2(@RequestParam Map<String, Object> param,  ModelMap model) {
		MultipartFile uploadFile = (MultipartFile)param.get("file_upload");
		
		logger.info("/fileUpload test started!");
		
		
		return "f:fileUpload";
	}

	
}
