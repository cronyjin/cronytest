package kr.co.user.weding.service.impl;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.user.weding.service.CommonFileService;
import kr.co.user.weding.service.CommonService;
import kr.co.user.weding.service.ExcelUploadService;
import select.spring.exquery.service.ExqueryService;

@Service("excelUploadService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ExcelUploadServiceImpl implements ExcelUploadService{

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
	
	@Autowired
	private ExqueryService exqueryService;

	@Inject
	@Named("commonFileService")
	private CommonFileService commonFileService;

	@Autowired
	private CommonService commonService;

	
	/**
	 * 접점일괄등록
	 * @return HashMap
	 */

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertExcelUploadTag(HashMap<String, Object> param, MultipartFile file){
			
		
		HashMap<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("result", true);
		// 전달할 key명칭을 정의
		String keyNames[] = new String[]{"tagId", "tagCd", "tagName", "tagNameA", "tagPe", "measureValCd", "unit", "tagAddress", "delYn"};
		// 데이터 시작 줄 번호 , 첫줄부터는 0, 둘째줄부터는 1
		int startRowNum = 2;
			
			try {
				// 파일 데이터로 부터 바로 엑셀내용을 읽어 들임 (파일로 저장하지 않고 처리)
				POIFSFileSystem inStream = new POIFSFileSystem(new ByteArrayInputStream(file.getBytes()));
				
				// 워크북을 생성
				HSSFWorkbook workbook = new HSSFWorkbook(inStream);
//				XSSFWorkbook workbook = new XSSFWorkbook(inStream);

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yy/m/d h:mm"));

				// 시트 갯수를 구함, 시트는 0 부터 시작
				int sheetCnt = workbook.getNumberOfSheets();
				System.out.println("sheetCnt : "+sheetCnt);
				
				// 전체 시트
				for (int sheetNo = 0; sheetNo < sheetCnt; sheetNo++) {
					
					//시트 이름과 시트번호를 추출
					
					HSSFSheet sheet = workbook.getSheetAt(sheetNo);
					
					int sheetRowCnt = sheet.getPhysicalNumberOfRows();
					System.out.println("sheetRowCnt : "+sheetRowCnt);
					
					// 전달할 파라미터 맵 선언
					HashMap<String, Object> pMap = new HashMap<String, Object>();				
					
					// 데이터 입력전 삭제처리 필요한 경우
					//exqueryDao.insert("com.youlchon.fems.preDeleteSample", pMap);				

					// 전체 Row 순환 처리 
					for (int rowNum = startRowNum; rowNum < sheetRowCnt; rowNum++) {
						HSSFRow currRow = sheet.getRow(rowNum);

						if (currRow == null)	continue;
						
						// 1 Row를 등록할 파라미터 초기화
						pMap = new HashMap<String, Object>();
						
						// 셀의 수 구함
//						int cellCnt = currRow.getPhysicalNumberOfCells();
						int cellCnt = keyNames.length;
						System.out.println("cellCnt : "+cellCnt);
						
						// 개발 Row의 셀을 데이터 타입에 따라 처리
						for (int cellNum = 0; cellNum < cellCnt; cellNum++) {
							HSSFCell currCell = currRow.getCell(cellNum);
							
							if (currCell == null)	continue;
							
							String cellValue = null;
							
							switch (currCell.getCellType()) {
								case HSSFCell.CELL_TYPE_FORMULA:
									// cellValue = String.valueOf(currCell.getCellFormula());
									cellValue = String.valueOf(currCell.getNumericCellValue());
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf(currCell.getNumericCellValue());
									if (cellNum == 0)
										cellValue = sdf.format(currCell.getDateCellValue());
									break;
								case HSSFCell.CELL_TYPE_STRING:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = String.valueOf(currCell.getStringCellValue());
									break;
								case HSSFCell.CELL_TYPE_BLANK:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = "";
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									cellValue = String.valueOf(currCell.getBooleanCellValue());
									break;
								case HSSFCell.CELL_TYPE_ERROR:
									cellValue = String.valueOf(currCell.getErrorCellValue());
									break;
								default:
							}
							
							pMap.put(keyNames[cellNum], cellValue);
							pMap.put("ssUserId", param.get("ssUserId"));
							
							//NULL처리
							if(pMap.get("measureUnit") == null){
								pMap.put("measureUnit", "");
							}
							
						} // 셀처리 끝

						System.out.println("pMap : "+pMap.size());
						if(pMap.size() > 0){
							if(pMap.get("tagCd").equals("") || pMap.get("tagName").equals("")){
								System.out.println("this Hash is emety");
							} else {
								if(pMap.get("delYn").equals("Y")){
									//삭제
									System.out.println("this Hash is Delete in TagCd");
//									exqueryService.delete("nsefems.nfTag.deleteTagCd", pMap);
								} else {
									//merge처리
									System.out.println("this Hash is merge in TagCd");
//									exqueryService.update("nsefems.nfTag.mergeTagCode", pMap);
								}
								
							} 
						}
						
						
					} // 전체 Row 순환 처리 
				} // 시트처리 끝
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				rMap.put("result", false);
				rMap.put("msg", "V009");
			}
		
		
		return rMap;
	}
	
	/**
	 * 설비 일괄입력 
	 * @return HashMap
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertExcelUploadEquip(HashMap<String, Object> param, MultipartFile file){

		HashMap<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("result", "true");
		// 전달할 key명칭을 정의
		String keyNames[] = new String[]{"", "procId", "equipId", "plantCd", "equipName", "equipNameA", "funcLocId", "ctVal", "oeeYn", "baseEquipYn"};
		// 데이터 시작 줄 번호 , 첫줄부터는 0, 둘째줄부터는 1
		int startRowNum = 2;
			
			try {
				// 파일 데이터로 부터 바로 엑셀내용을 읽어 들임 (파일로 저장하지 않고 처리)
				POIFSFileSystem inStream = new POIFSFileSystem(new ByteArrayInputStream(file.getBytes()));
				
				// 워크북을 생성
				HSSFWorkbook workbook = new HSSFWorkbook(inStream);
//				XSSFWorkbook workbook = new XSSFWorkbook(inStream);

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yy/m/d h:mm"));

				// 시트 갯수를 구함, 시트는 0 부터 시작
				int sheetCnt = workbook.getNumberOfSheets();
				System.out.println("sheetCnt : "+sheetCnt);
				
				// 전체 시트
				for (int sheetNo = 0; sheetNo < sheetCnt; sheetNo++) {
					
					//시트 이름과 시트번호를 추출
					
					HSSFSheet sheet = workbook.getSheetAt(sheetNo);
					
					int sheetRowCnt = sheet.getPhysicalNumberOfRows();
					System.out.println("sheetRowCnt : "+sheetRowCnt);
					
					// 전달할 파라미터 맵 선언
					HashMap<String, Object> pMap = new HashMap<String, Object>();				
					
					// 데이터 입력전 삭제처리 필요한 경우
					//exqueryDao.insert("com.youlchon.fems.preDeleteSample", pMap);				

					// 전체 Row 순환 처리 
					for (int rowNum = startRowNum; rowNum < sheetRowCnt; rowNum++) {
						HSSFRow currRow = sheet.getRow(rowNum);

						if (currRow == null)	continue;
						
						// 1 Row를 등록할 파라미터 초기화
						pMap = new HashMap<String, Object>();
						
						// 셀의 수 구함
//						int cellCnt = currRow.getPhysicalNumberOfCells();
						int cellCnt = keyNames.length;
						System.out.println("cellCnt : "+cellCnt);
						
						// 개발 Row의 셀을 데이터 타입에 따라 처리
						for (int cellNum = 0; cellNum < cellCnt; cellNum++) {
							HSSFCell currCell = currRow.getCell(cellNum);
							
							if (currCell == null)	continue;
							
							String cellValue = null;
							
							switch (currCell.getCellType()) {
								case HSSFCell.CELL_TYPE_FORMULA:
									// cellValue = String.valueOf(currCell.getCellFormula());
									cellValue = String.valueOf(currCell.getNumericCellValue());
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf(currCell.getNumericCellValue());
									if (cellNum == 0)
										cellValue = sdf.format(currCell.getDateCellValue());
									break;
								case HSSFCell.CELL_TYPE_STRING:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = String.valueOf(currCell.getStringCellValue());
									break;
								case HSSFCell.CELL_TYPE_BLANK:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = "";
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									cellValue = String.valueOf(currCell.getBooleanCellValue());
									break;
								case HSSFCell.CELL_TYPE_ERROR:
									cellValue = String.valueOf(currCell.getErrorCellValue());
									break;
								default:
									
							}
							
							pMap.put(keyNames[cellNum], cellValue);
							pMap.put("updId", param.get("ssUserId"));
							pMap.put("updName", param.get("ssUserName"));
							
							//NULL처리
							if(pMap.get("funcLocId") == null){
								pMap.put("funcLocId", "");
							}
							if(pMap.get("ctVal") == null){
								pMap.put("ctVal", "");
							}
							
						} // 셀처리 끝

						System.out.println("pMap : "+pMap.size());
						if(pMap.size() > 0){
							if(pMap.get("procId").equals("") || pMap.get("equipId").equals("")){
								System.out.println("this Hash is emety");
							} else {
								//merge처리
								System.out.println("this Hash is merge in equipId");
								exqueryService.update("nsefems.bpProcEquip.mergeTagCode", pMap);						
							} 
						}
						
						
					} // 전체 Row 순환 처리 
				} // 시트처리 끝
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				rMap.put("result", false);
				rMap.put("msg", "V009");
			}
		
		
		return rMap;
	}
	
	/**
	 * 설비-접점 일괄입력 
	 * @return HashMap
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertExcelUploadEquipTag(HashMap<String, Object> param, MultipartFile file){
		
		HashMap<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("result", "true");
		// 전달할 key명칭을 정의
		String keyNames[] = new String[]{ "", "procId", "equipId", "tagId", "measureTypeCd", "details", "alarmCd"};
		// 데이터 시작 줄 번호 , 첫줄부터는 0, 둘째줄부터는 1
		int startRowNum = 2;
			
			try {
				// 파일 데이터로 부터 바로 엑셀내용을 읽어 들임 (파일로 저장하지 않고 처리)
				POIFSFileSystem inStream = new POIFSFileSystem(new ByteArrayInputStream(file.getBytes()));
				
				// 워크북을 생성
				HSSFWorkbook workbook = new HSSFWorkbook(inStream);
//				XSSFWorkbook workbook = new XSSFWorkbook(inStream);

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yy/m/d h:mm"));

				// 시트 갯수를 구함, 시트는 0 부터 시작
				int sheetCnt = workbook.getNumberOfSheets();
				System.out.println("sheetCnt : "+sheetCnt);
				
				// 전체 시트
				for (int sheetNo = 0; sheetNo < sheetCnt; sheetNo++) {
					
					//시트 이름과 시트번호를 추출
					
					HSSFSheet sheet = workbook.getSheetAt(sheetNo);
					
					int sheetRowCnt = sheet.getPhysicalNumberOfRows();
					System.out.println("sheetRowCnt : "+sheetRowCnt);
					
					// 전달할 파라미터 맵 선언
					HashMap<String, Object> pMap = new HashMap<String, Object>();				
					
					// 데이터 입력전 삭제처리 필요한 경우
					//exqueryDao.insert("com.youlchon.fems.preDeleteSample", pMap);				

					// 전체 Row 순환 처리 
					for (int rowNum = startRowNum; rowNum < sheetRowCnt; rowNum++) {
						HSSFRow currRow = sheet.getRow(rowNum);

						if (currRow == null)	continue;
						
						// 1 Row를 등록할 파라미터 초기화
						pMap = new HashMap<String, Object>();
						
						// 셀의 수 구함
//						int cellCnt = currRow.getPhysicalNumberOfCells();
						int cellCnt = keyNames.length;
						System.out.println("cellCnt : "+cellCnt);
						
						// 개발 Row의 셀을 데이터 타입에 따라 처리
						for (int cellNum = 0; cellNum < cellCnt; cellNum++) {
							HSSFCell currCell = currRow.getCell(cellNum);
							
							if (currCell == null)	continue;
							
							String cellValue = null;
							
							switch (currCell.getCellType()) {
								case HSSFCell.CELL_TYPE_FORMULA:
									// cellValue = String.valueOf(currCell.getCellFormula());
									cellValue = String.valueOf(currCell.getNumericCellValue());
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf(currCell.getNumericCellValue());
									if (cellNum == 0)
										cellValue = sdf.format(currCell.getDateCellValue());
									break;
								case HSSFCell.CELL_TYPE_STRING:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = String.valueOf(currCell.getStringCellValue());
									break;
								case HSSFCell.CELL_TYPE_BLANK:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = "";
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									cellValue = String.valueOf(currCell.getBooleanCellValue());
									break;
								case HSSFCell.CELL_TYPE_ERROR:
									cellValue = String.valueOf(currCell.getErrorCellValue());
									break;
								default:
									
							}
							
							pMap.put(keyNames[cellNum], cellValue);
							pMap.put("ssUserId", param.get("ssUserId"));
							
							//NULL처리
							if(pMap.get("details") == null){
								pMap.put("details", "");
							}
							if(pMap.get("alarmCd") == null){
								pMap.put("alarmCd", "");
							}
							
						} // 셀처리 끝

						System.out.println("pMap : "+pMap.size());
						if(pMap.size() > 0){
							if(pMap.get("procId").equals("") || pMap.get("equipId").equals("") || pMap.get("tagId").equals("")){
								System.out.println("this Hash is emety");
							} else {
								//merge처리
								System.out.println("this Hash is merge in procId");
								exqueryService.update("nse.bpProcEquipTag.mergeProcEquipTag", pMap); 					
							} 
						}

					} // 전체 Row 순환 처리 
				} // 시트처리 끝
				
			} catch (Exception e) {
				e.printStackTrace();
				rMap.put("result", false);
				rMap.put("msg", "V009");
			}
		
		return rMap;
	}
	
	/**
	 * 사용자관리 일괄입력 
	 * @return HashMap
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	public HashMap<String, Object> insertExcelUploadUser(HashMap<String, Object> param, MultipartFile file){
		
		HashMap<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("result", "true");
		// 전달할 key명칭을 정의
		String keyNames[] = new String[]{ "", "userId", "userName", "password", "corpNo", "deptName", "duty",
										  "phoneNo", "mobileNo", "email", "bizCharge", "bizGrp", "mainCd", "accessYn"};
		// 데이터 시작 줄 번호 , 첫줄부터는 0, 둘째줄부터는 1
		int startRowNum = 2;
			
			try {
				// 파일 데이터로 부터 바로 엑셀내용을 읽어 들임 (파일로 저장하지 않고 처리)
				POIFSFileSystem inStream = new POIFSFileSystem(new ByteArrayInputStream(file.getBytes()));
				
				// 워크북을 생성
				HSSFWorkbook workbook = new HSSFWorkbook(inStream);
//				XSSFWorkbook workbook = new XSSFWorkbook(inStream);

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yy/m/d h:mm"));

				// 시트 갯수를 구함, 시트는 0 부터 시작
				int sheetCnt = workbook.getNumberOfSheets();
				System.out.println("sheetCnt : "+sheetCnt);
				
				// 전체 시트
				for (int sheetNo = 0; sheetNo < sheetCnt; sheetNo++) {
					
					//시트 이름과 시트번호를 추출
					
					HSSFSheet sheet = workbook.getSheetAt(sheetNo);
					
					int sheetRowCnt = sheet.getPhysicalNumberOfRows();
					System.out.println("sheetRowCnt : "+sheetRowCnt);
					
					// 전달할 파라미터 맵 선언
					HashMap<String, Object> pMap = new HashMap<String, Object>();				
					
					// 데이터 입력전 삭제처리 필요한 경우
					//exqueryDao.insert("com.youlchon.fems.preDeleteSample", pMap);				

					// 전체 Row 순환 처리 
					for (int rowNum = startRowNum; rowNum < sheetRowCnt; rowNum++) {
						HSSFRow currRow = sheet.getRow(rowNum);

						if (currRow == null)	continue;
						
						// 1 Row를 등록할 파라미터 초기화
						pMap = new HashMap<String, Object>();
						
						// 셀의 수 구함
//						int cellCnt = currRow.getPhysicalNumberOfCells();
						int cellCnt = keyNames.length;
						System.out.println("cellCnt : "+cellCnt);
						
						// 개발 Row의 셀을 데이터 타입에 따라 처리
						for (int cellNum = 0; cellNum < cellCnt; cellNum++) {
							HSSFCell currCell = currRow.getCell(cellNum);
							
							if (currCell == null)	continue;
							
							String cellValue = null;
							
							switch (currCell.getCellType()) {
								case HSSFCell.CELL_TYPE_FORMULA:
									// cellValue = String.valueOf(currCell.getCellFormula());
									cellValue = String.valueOf(currCell.getNumericCellValue());
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									cellValue = String.valueOf(currCell.getNumericCellValue());
									if (cellNum == 0)
										cellValue = sdf.format(currCell.getDateCellValue());
									break;
								case HSSFCell.CELL_TYPE_STRING:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = String.valueOf(currCell.getStringCellValue());
									break;
								case HSSFCell.CELL_TYPE_BLANK:
									//cellValue = "String value=" + currCell.getStringCellValue();
									cellValue = "";
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									cellValue = String.valueOf(currCell.getBooleanCellValue());
									break;
								case HSSFCell.CELL_TYPE_ERROR:
									cellValue = String.valueOf(currCell.getErrorCellValue());
									break;
								default:
									
							}
							
							pMap.put(keyNames[cellNum], cellValue);
							pMap.put("ssUserId", param.get("ssUserId"));
							
							//NULL처리
							if(pMap.get("userName") == null){
								pMap.put("userName", "");
							}
							if(pMap.get("password") == null){
								pMap.put("password", "");
							}
							if(pMap.get("corpNo") == null){
								pMap.put("corpNo", "");
							}
							if(pMap.get("deptName") == null){
								pMap.put("deptName", "");
							}
							if(pMap.get("duty") == null){
								pMap.put("duty", "");
							}
							if(pMap.get("phoneNo") == null){
								pMap.put("phoneNo", "");
							}
							if(pMap.get("mobileNo") == null){
								pMap.put("mobileNo", "");
							}
							if(pMap.get("email") == null){
								pMap.put("email", "");
							}
							if(pMap.get("bizCharge") == null){
								pMap.put("bizCharge", "");
							}
							if(pMap.get("bizGrp") == null){
								pMap.put("bizGrp", "");
							}
							if(pMap.get("mainCd") == null){
								pMap.put("mainCd", "");
							}
							if(pMap.get("accessYn") == null){
								pMap.put("accessYn", "");
							}
							
						} // 셀처리 끝

						System.out.println("pMap : "+pMap.size());
						if(pMap.size() > 0){
							if(pMap.get("userId").equals("")){
								System.out.println("this Hash is emety");
							} else {
								//merge처리
								System.out.println("this Hash is merge in userId");
								exqueryService.update("nse.bpUser.mergeUser", pMap); 					
							} 
						}

					} // 전체 Row 순환 처리 
				} // 시트처리 끝
				
			} catch (Exception e) {
				e.printStackTrace();
				rMap.put("result", false);
				rMap.put("msg", "V009");
			}
		
		return rMap;
	}
}
