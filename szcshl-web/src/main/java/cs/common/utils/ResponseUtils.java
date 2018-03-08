package cs.common.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by shenning on 2018/1/15.
 */
public class ResponseUtils {

    public static void setResponeseHead(String fileType,HttpServletResponse response){
        switch (fileType) {
            case ".jpg":
                response.setHeader("Content-type", "application/.jpg");
                break;
            case ".png":
                response.setHeader("Content-type", "application/.png");
                break;
            case ".gif":
                response.setHeader("Content-type", "application/.gif");
                break;
            case ".docx":
                response.setHeader("Content-type", "application/.docx");
                break;
            case ".doc":
                response.setHeader("Content-type", "application/.doc");
                break;
            case ".xlsx":
                response.setHeader("Content-type", "application/.xlsx");
                break;
            case ".xls":
                response.setHeader("Content-type", "application/.xls");
                break;
            case ".pdf":
                response.setHeader("Content-type", "application/.pdf");
                break;
            default:
                response.setContentType("application/octet-stream");
        }
    }
}
