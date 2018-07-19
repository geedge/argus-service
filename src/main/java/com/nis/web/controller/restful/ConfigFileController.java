package com.nis.web.controller.restful;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.web.service.AuditLogThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nis.domain.restful.ConfigFile;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.MD5Utils;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: ConfigFileController
 * @Description: 配置文件上传
 * @author (DDM)
 * @date 2016年9月19日 下午6:09:33
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/file/v1")
@Api(value = "ConfigFileController", description = "配置文件上传")
public class ConfigFileController extends BaseRestController {
	Logger logger = LoggerFactory.getLogger(ConfigSourcesController.class);
	SimpleDateFormat ymdhms=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	ServicesRequestLogService servicesRequestLogService;
	@RequestMapping(value = "/fileUploadSources", method = RequestMethod.POST)
	@ApiOperation(value = "文件上传", httpMethod = "POST", response = Map.class, notes = "多文件上传")
	@ApiParam(value = "文件上传", name = "filesUpload", required = true)
	public Map fileUploadSources(ConfigFile configFile,HttpServletRequest request,
									HttpServletResponse response){
		long start=System.currentTimeMillis();
			System.out.println("operator:"+configFile.getOperator()+
							"version:"+configFile.getVersion()+
							"opAction:"+configFile.getOpAction()+
							"opTime:"+configFile.getOpTime()
							);
		JSONArray fileArray=new JSONArray();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, configFile,fileArray);
		String serviceDir=request.getRealPath("upload");
        try {
        	MultipartFile[] files=configFile.getFiles();
        	if(files != null && files.length > 0){
        		for (MultipartFile file : files) {
            		//记录上传过程起始时的时间，用来计算上传时间  
                    int pre = (int) System.currentTimeMillis();  
                    //取得上传文件  
                    if(file != null){  
                        //取得当前上传文件的文件名称  
                        String myFileName = file.getOriginalFilename();  
                        if(!"".equals(myFileName.trim())){  
                        	JSONObject fileObject=new JSONObject();
                        	myFileName=myFileName.trim();
                            //定义上传路径  
                            File serviceFile = new File(serviceDir,myFileName);  
                            //上传的文件已经存在，如果需要另存，请取消注释
//                            if(serviceFile.exists()){
//                            	myFileName=myFileName.substring(0, myFileName.lastIndexOf(".")-1)+"_new"+
//                            			myFileName.substring(myFileName.lastIndexOf("."), myFileName.length());
//                            	serviceFile=new File(serviceDir,myFileName);
//                            }
                            file.transferTo(serviceFile);
                            
                            fileObject.put("fileName", myFileName);
                        	fileObject.put("fileNameMd5", MD5Utils.getMD5Checksum(serviceDir+"/"+myFileName));
                        	fileObject.put("uploadTime", ymdhms.format(new Date()));
                            fileArray.add(fileObject);
                        }  
                    }  
                    //记录上传该文件后的时间  
                    int finaltime = (int) System.currentTimeMillis();  
                    System.out.println(finaltime - pre);  
    			}
        	}
//        	thread.setContent(fileArray);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e.toString());
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"文件上传失败");
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "文件上传成功",fileArray);
	}
	
	@RequestMapping(value = "/fileDownLoadSources", method = RequestMethod.GET)
	@ApiOperation(value = "文件下载", httpMethod = "GET", response = Map.class, notes = "文件下载")
	@ApiParam(value = "文件下载", name = "fileDownLoad", required = true)
	public Map fileDownLoadSources(String fileName,
							HttpServletRequest request,
							HttpServletResponse response){
			long start=System.currentTimeMillis();
			JSONObject fileObject=new JSONObject();
			AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, fileObject);
			BufferedInputStream bis = null;  
			BufferedOutputStream bos = null; 
			String downLoadPath=null;
			if(!StringUtil.isBlank(fileName)){
			}else{
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"文件名称为空");
	        }
	        try {
	        	 fileName=fileName.trim();
	        	 response.setContentType("text/html;charset=utf-8");  
	        	 request.setCharacterEncoding("UTF-8");  
	        	 
	        	 String ctxPath = request.getRealPath("upload");  
	        	 downLoadPath = ctxPath +"/"+ fileName;  
	        	 long fileLength = new File(downLoadPath).length();  
	        	 
	        	 response.setContentType("application/x-msdownload;");  
	        	 response.setHeader("Content-disposition", "attachment; filename="  
	        			 + new String(fileName.getBytes("utf-8"), "ISO8859-1"));  
	        	 response.setHeader("Content-Length", String.valueOf(fileLength));  
	        	 
	        	 bis = new BufferedInputStream(new FileInputStream(downLoadPath));  
	        	 bos = new BufferedOutputStream(response.getOutputStream());  
	        	 
	        	 byte[] buff = new byte[2048];  
	        	 int bytesRead;  
	        	 while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
	        		 bos.write(buff, 0, bytesRead);  
	        	 } 
			} catch (Exception e) {
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				logger.error(e.toString());
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"文件下载失败");
			} finally {  
		        try {
		        	if (bis != null)  
				           bis.close();  
			        if (bos != null)  
			           bos.close();
				} catch (Exception e2) {
					thread.setExceptionInfo(e2.getMessage()+" "+e2.getCause());
					logger.error(e2.toString());
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"文件下载失败");
				}  
			}
	 
        	try {
        		fileObject.put("fileName", downLoadPath);
				fileObject.put("fileNameMd5", MD5Utils.getMD5Checksum(downLoadPath));
				fileObject.put("downloadTime", ymdhms.format(new Date()));
			} catch (NoSuchAlgorithmException e) {
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "文件下载",fileName);
	}
}
