package kr.co.user.weding.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelPoiView extends AbstractExcelView {
	 @Override
    protected void buildExcelDocument(Map<String, Object> ModelMap, HSSFWorkbook workbook, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
         
		 final int PRE_FIXED_COLUMN_CNT = 8;
		 
    	  String excelName = ModelMap.get("FileName").toString();
    	  String alienChk = ModelMap.get("alienChk").toString();
          HSSFSheet worksheet = null;
          HSSFRow row = null;


        excelName=URLEncoder.encode(excelName,"UTF-8");

          worksheet = workbook.createSheet(excelName+ " WorkSheet");

         // @SuppressWarnings("unchecked")
        //  List<BoardArticle> list = (List<BoardArticle>)ModelMap.get("excelList");

          List<HashMap<String, Object>> list =  (List<HashMap<String, Object>>)ModelMap.get("list");
          List<HashMap<String, Object>> paramList =  (List<HashMap<String, Object>>)ModelMap.get("paramList");
          
          row = worksheet.createRow(0);

          row.createCell(0).setCellValue("밀롤번호");
          row.createCell(1).setCellValue("밀롤코드");

          row.createCell(2).setCellValue("밀롤길이");
          row.createCell(3).setCellValue("제품코드");
          row.createCell(4).setCellValue("제품명");
          
          row.createCell(5).setCellValue("시작시간");
          row. createCell(6).setCellValue("종료시간");
          row.createCell(7).setCellValue("두깨평활도");
          row.createCell(8).setCellValue("THICKNESS");

          int titleNum = 9;
          
          for(int j=0; j < paramList.size() ; j++){
        	  
        	  row.createCell(titleNum).setCellValue(paramList.get(j).get("tagName")+"("+paramList.get(j).get("tagCd")+")_AVG");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue(paramList.get(j).get("tagName")+"("+paramList.get(j).get("tagCd")+")_MIN");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue(paramList.get(j).get("tagName")+"("+paramList.get(j).get("tagCd")+")_MAX");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue(paramList.get(j).get("tagName")+"("+paramList.get(j).get("tagCd")+")_STD_DEV");
        	  titleNum++;
        	  
        	  
          }
//          이물리스트 타이틀
          if(alienChk.equals("Y")){
    		  row.createCell(titleNum).setCellValue("이물1_1");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물1_2");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물1_3");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물1_4");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물1_5");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물2_1");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물2_2");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물2_3");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물2_4");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물2_5");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물3_1");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물3_2");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물3_3");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물3_4");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물3_5");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물4_1");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물4_2");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물4_3");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물4_4");
        	  titleNum++;
        	  row.createCell(titleNum).setCellValue("이물4_5");
        	  titleNum++;
    	  }
          int rowNo = 0;
          for(int i=0; i<list.size(); i++){
        	  
              rowNo += 1;  
        	  row = worksheet.createRow(rowNo);
        	  
        	  
              row.createCell(0).setCellValue(list.get(i).get("mrNo").toString());
               row.createCell(1).setCellValue(list.get(i).get("mrCd").toString());

               row.createCell(2).setCellValue(list.get(i).get("procQty").toString());
               row.createCell(3).setCellValue(list.get(i).get("itemCode").toString());
               row.createCell(4).setCellValue(list.get(i).get("itemName").toString());
               
                row.createCell(5).setCellValue(list.get(i).get("startYmdhis").toString());
                 row.createCell(6).setCellValue(list.get(i).get("endYmdhis").toString());
                 row.createCell(7).setCellValue(list.get(i).get("thickSmothRate").toString());
                 row.createCell(8).setCellValue(list.get(i).get("thickness").toString());
                 
                 
                 int cellPos = PRE_FIXED_COLUMN_CNT;
                 
                 for(int j=0; j < paramList.size() ; j++){
                	 
                	 String tagCd = (String)paramList.get(j).get("tagCd");
                	 tagCd = tagCd.toLowerCase();
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get(tagCd + "Avg" ).toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get(tagCd + "Min" ).toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get(tagCd + "Max" ).toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get(tagCd + "StdDev" ).toString());
                }

                 if(alienChk.equals("Y")){
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien11").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien12").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien13").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien14").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien15").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien21").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien22").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien23").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien24").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien25").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien31").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien32").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien33").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien34").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien35").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien41").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien42").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien43").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien44").toString());
                	 cellPos++;
                	 row.createCell(cellPos).setCellValue(list.get(i).get("alien45").toString());
                 } 
          }

          response.setContentType("Application/Msexcel");
          response.setHeader("Content-Disposition", "ATTachment; Filename="+excelName+".xls");

      
    }

}

