package cs.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ldm
 * @Description: 输入输出流转换
 * Created by ldm on 2018/10/15 0015.
 */
public class IOStreamUtil {

    /**
     * inputStream转outputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parse(InputStream in) throws Exception {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    /**
     * outputStream转inputStream
     *
     * @param out
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parse(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * inputStream转String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String parse_String(InputStream in) throws Exception {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream.toString();
    }

    /**
     * OutputStream 转String
     *
     * @param out
     * @return
     * @throws Exception
     */
    public static String parse_String(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    /**
     * String转inputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parse_inputStream(String in) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
        return input;
    }

    /**
     * String 转outputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parse_outputStream(String in) throws Exception {
        return parse(parse_inputStream(in));
    }


    /**
     * 下载文件，返回输入流。
     *
     * @param apiUrl api接口
     * @return (文件)输入流
     * @throws Exception
     */
    public static InputStream getStreamDownloadOutFile(String apiUrl) throws Exception {
        InputStream is = null;
        //创建默认http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //采用默认请求配置
        RequestConfig requestConfig = RequestConfig.DEFAULT;
        //通过get方法下载文件流
        HttpGet request = new HttpGet(apiUrl);
        //设置请头求配置
        request.setConfig(requestConfig);
        try {
            //执行请求，接收返回信息
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            //获取执行状态
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                request.abort();
            } else {
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    //获取返回内容
                    is = entity.getContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.abort();
        }
        return is;

    }
}
