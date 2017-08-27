package cs.common.utils;

import java.text.DecimalFormat;

/**
 * 附件工具类，只显示文件，文件夹过滤
 */
public class FileUtils {

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
}
