package cs.ueditor.upload;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import cs.ueditor.PathFormat;
import cs.ueditor.define.AppInfo;
import cs.ueditor.define.BaseState;
import cs.ueditor.define.FileType;
import cs.ueditor.define.State;

public final class Base64Uploader {

	public static State save(String content, Map<String, Object> conf) {
		
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"));
		
		savePath = savePath + suffix;
		//原方法，先注释
		//String physicalPath = (String) conf.get("rootPath") + savePath;
		//修改保存路径，以磁盘路径为准   //home//wzdzbz//develop//web-home//wzdzbz-home
		String physicalPath = "D://szec/annountment/" + savePath;

		State storageState = StorageManager.saveBinaryFile(data, physicalPath);

		if (storageState.isSuccess()) {
			//storageState.putInfo("url", PathFormat.format(savePath));
			storageState.putInfo("url", savePath);
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}