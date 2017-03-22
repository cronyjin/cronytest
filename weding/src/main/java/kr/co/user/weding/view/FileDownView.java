package kr.co.user.weding.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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


public class FileDownView extends AbstractView {

	private static final Logger logger = LoggerFactory.getLogger(FileDownView.class);
	
	private static final String CHARSET = "utf-8";
	
	@Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
         
        File file = (File) model.get("file");
        String fileName = (String) model.get("fileName");

		try{
		MagicMatch mm = Magic.getMagicMatch(fileName.getBytes(), false);
			response.setContentType(mm.getMimeType()+"; charset="+CHARSET);
		}catch(MagicMatchNotFoundException me){
			response.setContentType("application/octet-stream; charset="+CHARSET);
		}

        response.setContentLength((int)file.length());
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		String userAgent = request.getHeader("User-Agent");
		
		if (userAgent != null && userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5
	      response.setHeader("Content-Disposition", "filename="+URLEncoder.encode(fileName, "UTF-8") + ";");
	    } else if (userAgent != null && userAgent.indexOf("MSIE") > -1) { // MS IE
	      response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fileName, "UTF-8") + ";");
	    } else { // etc browser
	      response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes(CHARSET), "latin1") + ";");
	    }
		
		response.setHeader("Cache-Control","no-cache,no-store");
		response.setHeader("Cache-Control", "max-age=0");
		
		OutputStream out = response.getOutputStream();
        FileInputStream fis = null;
        
        try
        {
       		fis = new FileInputStream(file);
            FileCopyUtils.copy(fis,out);            
        } catch(IOException e) {
        	logger.warn("fileView:: file not found" + fileName);
        }
        finally
        {
            if(fis != null) fis.close();
        }
        out.flush();
    }

}

