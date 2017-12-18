package cs.common.utils;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import cs.common.Constant;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * 文件工具
 *
 * @author lqs
 */
public class SysFileUtil {
    private static Logger logger = Logger.getLogger(SysFileUtil.class);

    private static String FILE_UPLOAD_PATH = "file_upload_path";

    public static String getFileSize(long fileS){
        String size = "";
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1024) {
            size = df.format((double) fileS) + "BT";
        } else if (fileS < 1048576) {
            size = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            size = df.format((double) fileS / 1048576) + "MB";
        } else {
            size = df.format((double) fileS / 1073741824) +"GB";
        }
        return  size;
    }
    /**
     * 根据附件主类划分
     * @return
     */
    public static String getUploadPath() {
        PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
        String uploadPath = propertyUtil.readProperty(FILE_UPLOAD_PATH);
        return  Validate.isString(uploadPath)?uploadPath : "C:\\szec_uploadfile";
    }

    /**
     * 根据业务类型(urlGenerator)生成不同规则的文件路径
     *
     * @param fileLocation 文件存放的根目录
     */
    public static String generatRelativeUrl(String fileLocation,String mainType,String mainId, String sysBusiType, String fileName) {
        String url = fileLocation;
        String relativeUrl = "";
        if(!Validate.isString(mainType)){
            mainType = "NO_MAIN_TYPE_FILE";
        }
        //文件存放的格式,根目录/主业务ID/业务模块/文件名
        relativeUrl += (File.separator + mainType);
        //如果有主业务ID ，则加上
        if(Validate.isString(mainId)){
            relativeUrl += (File.separator+mainId);
        }
        //如果有业务模块，则加上业务模块
        if(Validate.isString(sysBusiType)){
            relativeUrl += (File.separator+sysBusiType);
        }
        File isFileExists = new File(url + File.separator + relativeUrl);

        if (isFileExists.exists()) {
            if (!isFileExists.isDirectory()) {
                isFileExists.mkdirs();
            }
        } else {
            isFileExists.mkdirs();
        }
        String extendName = fileName;
        //若是文件夹，则不需要切割
        if(fileName.indexOf(".") >0){
            extendName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        String distFileName = UUID.randomUUID().toString().replaceAll("-", "").concat(extendName);
        relativeUrl += File.separator + distFileName;

        return relativeUrl;
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            }else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}
