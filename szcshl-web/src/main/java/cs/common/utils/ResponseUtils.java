package cs.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sn.framework.common.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.sn.framework.common.StringUtil.UTF_8;

/**
 * Created by shenning on 2018/1/15.
 */
public class ResponseUtils {
    private final static Logger logger = LoggerFactory.getLogger(ResponseUtils.class);
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

    public static void printJsonMsg(HttpServletResponse response, HttpStatus httpStatus, Object result) {
        if (logger.isDebugEnabled()) {
            logger.debug("返回json格式的消息");
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setHeader("Charset", UTF_8.name());
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(httpStatus.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            if (result instanceof CharSequence) {
                writer.print(result);
            } else {
                writer.print(JacksonUtils.objectToString(result));
            }
            writer.flush();
        } catch (JsonProcessingException e) {
            logger.error("响应消息转换失败", e);
        } catch (IOException e) {
            logger.error("响应处理失败", e);
        } finally {
            if (null != writer) {
                writer.close();
                try {
                    response.flushBuffer();
                } catch (IOException e) {
                    logger.error("响应异常", e);
                }
            }
        }
    }
}
