package kr.co.user.weding.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.user.weding.service.CommonFileService;
import kr.co.user.weding.service.CommonService;
import kr.co.user.weding.service.ExcelUploadService;
import select.spring.exquery.service.ExqueryService;

@Controller
@RequestMapping( value="excelUpload")
public class ExcelUploadController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private ExqueryService exqueryService;

	@Inject
	@Named("commonFileService")
	private CommonFileService commonFileService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ExcelUploadService excelUploadService;
	
	/**
	 * 접점일괄등록
	 * @return HashMap
	 */
	@RequestMapping( value="insertExcelUploadTag", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> insertExcelUploadTag(@RequestParam HashMap<String, Object> param, @RequestParam(value="openFile", required=false) MultipartFile file){
		log.debug( "insertExcelUploadTag =============>" +  param );
		
		return excelUploadService.insertExcelUploadTag(param, file);
		
    }
	
	/**
	 * 설비 일괄입력 
	 * @return HashMap
	 */
	@RequestMapping( value="insertExcelUploadEquip", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> insertExcelUploadEquip(@RequestParam HashMap<String, Object> param, @RequestParam(value="openFile", required=false) MultipartFile file){
		log.debug( "insertExcelUploadEquip =============>" +  param );
		
		return excelUploadService.insertExcelUploadEquip(param, file);
		
    }
	
	/**
	 * 설비-접점 일괄입력 
	 * @return HashMap
	 */
	@RequestMapping( value="insertExcelUploadEquipTag", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> insertExcelUploadEquipTag(@RequestParam HashMap<String, Object> param, @RequestParam(value="openFile", required=false) MultipartFile file){
		log.debug( "insertExcelUploadEquipTag =============>" +  param );
		
		return excelUploadService.insertExcelUploadEquipTag(param, file);
		
	}
	
	/**
	 * 사용자관리 일괄입력 
	 * @return HashMap
	 */
	@RequestMapping( value="insertExcelUploadUser", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> insertExcelUploadUser(@RequestParam HashMap<String, Object> param, @RequestParam(value="openFile", required=false) MultipartFile file){
		log.debug( "insertExcelUploadUser =============>" +  param );
		
		return excelUploadService.insertExcelUploadUser(param, file);
		
	}

}
