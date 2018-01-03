package cs.common.utils;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import cs.common.Constant;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static String getFileSize(Long fileS){
        String size = "";
        if(fileS == null){
            return size;
        }
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
        //如果是本地
        if(Validate.isString(fileLocation)){
            String url = fileLocation;
           File isFileExists = new File(url + File.separator + relativeUrl);

            if (isFileExists.exists()) {
                if (!isFileExists.isDirectory()) {
                    isFileExists.mkdirs();
                }
            } else {
                isFileExists.mkdirs();
            }
        }

        //如果有文件名，则加上文件名
        if(Validate.isString(fileName)){
            String extendName = fileName;
            //若是文件夹，则不需要切割
            if(fileName.indexOf(".") >0){
                extendName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            }
            String distFileName = Tools.generateRandomFilename().concat(extendName);
            relativeUrl += File.separator + distFileName;
        }
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

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(60*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        try{
            downLoadFromUrl("http://dlsw.baidu.com/sw-search-sp/soft/39/15453/zuiqiangwubishurufa.1403771155.rar",
                    "baidu.rar","D:/szec_uploadfile/");
        }catch (Exception e) {
            System.out.print(e.getMessage());
            // TODO: handle exception
        }
    }
}
