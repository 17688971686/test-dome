package cs.common.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cs.common.Constant;

/**
 * 文件工具
 * @author lqs
 * */
public class SysFileUtil {
	private static String FILE_UPLOAD_PATH = "file_upload_path";		
	
	public static String getUploadPath(){
		PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
		String uploadPath = propertyUtil.readProperty(FILE_UPLOAD_PATH);			
		return  uploadPath == null?"E:\\szcshl_upload":uploadPath;
	}
	/**
	 * 
	 * 根据业务类型(urlGenerator)生成不同规则的文件路径
	 * @param fileLocation 文件存放的根目录
	 * */
	public static String generatRelativeUrl(String fileLocation, String module,String businessId, String fileName) {
		String url = fileLocation;
		String relativeUrl = "";
		if(module == null||module.isEmpty()){
			module = "default";
		}
		//文件存放的格式,根目录/模块/日期/业务ID
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");	
		
		relativeUrl += (File.separator + module + File.separator);
		relativeUrl += dateFormat.format(date) + File.separator + businessId ;
		
		File isFileExists = new File(url+File.separator+relativeUrl);
		if(isFileExists.exists()){
			if(!isFileExists.isDirectory()){
				isFileExists.mkdirs();
			}
		}else{
			isFileExists.mkdirs();
		}
		
		String extendName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		String distFileName = UUID.randomUUID().toString().replaceAll("-", "").concat(extendName);
		relativeUrl += File.separator + distFileName;
						
		return relativeUrl;
	}

}
