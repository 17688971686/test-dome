package cs.common.ftp;

import cs.domain.sys.Ftp;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author ldm
 */
public class FtpUtils {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);
    private static final String GHK_CHARSET = "GBK";
    private static final String UTF_CHARSET = "UTF-8";
    private FtpClientPool ftpClientPool = new FtpClientPool(new FtpClientFactory(),new FtpPoolConfig());

    public FtpClientPool getFtpClientPool() {
        return ftpClientPool;
    }

    public void setFtpClientPool(FtpClientPool ftpClientPool) {
        this.ftpClientPool = ftpClientPool;
    }

    public FTPClient getFtpClient(FtpClientPool ftpClientPool, FtpClientConfig config) throws Exception {
        FTPClient client = null;
        try {
            client = ftpClientPool.borrowObject(config);
        } catch (Exception e) {
            logger.error("获取FTPClient对象异常 " + toFtpInfo(config),e);
            throw e;
        }
        return client;
    }


    private String performPerFile(FTPClient client, String fileName) throws Exception{
        String content = "";
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            client.enterLocalPassiveMode();
            boolean downSuc = client.retrieveFile(fileName, os);
            if(downSuc){
                InputStream inputstream = new ByteArrayInputStream(os.toByteArray());
                content = IOUtils.toString(inputstream, "UTF-8");
                if (inputstream != null) {
                    inputstream.close();
                }
                if(os!=null){
                    os.close();
                }
            }else{
                logger.info("下载报文失败:" + fileName);
                throw new Exception("下载报文失败:" + fileName);
            }
        } catch (Exception e1) {
            logger.error("ftp 通信错误 或 文件IO出错, 下载文件成功状态:"+client.getRemoteAddress(), e1);
            throw e1;
        }

        return content;
    }

    public boolean checkLink(FtpClientConfig k){
        FTPClient client = null;
        try{
           client = ftpClientPool.borrowObject(k);
           if(client != null){
               return true;
           }
        }catch (Exception e){
            return false;
        }finally {
            if(client != null){
                ftpClientPool.returnObject(k,client);
            }
        }
        return true;
    }
    public boolean putFile(FTPClient ftpClient,FtpClientConfig k,String remoteBaseDir,String filename, InputStream in) throws IOException {
        boolean result = false;
        try {
            checkCharset(ftpClient,k);
            // 内网设置为被动模式
            ftpClient.enterLocalPassiveMode();
            //涉及到中文问题 根据系统实际编码改变
            remoteBaseDir = new String(remoteBaseDir.getBytes(k.getChartset()==null?GHK_CHARSET:k.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            filename = new String(filename.getBytes(k.getChartset()==null?GHK_CHARSET:k.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            if (!ftpClient.changeWorkingDirectory(remoteBaseDir)) {
                //如果目录不存在创建目录
                String[] dirs = remoteBaseDir.replace(File.separator, "@").split("@");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += File.separator + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            if (!ftpClient.storeFile(filename, in)) {
                throw new IOException("无法上传附件【" + filename + "】请检查文件服务器地址和权限信息是否正确。");
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != in){
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return result;
    }

    /**
     * Removes the file from the FTP server.
     *
     * @param remoteFilePath
     *            server file name (with absolute path)
     * @throws IOException
     *             on I/O errors
     */
    public boolean remove(String remoteFilePath, FtpClientConfig config){
        FTPClient ftp = null;
        boolean doResult = false;
        try{
            ftp = getFtpClient(ftpClientPool,config);
            checkCharset(ftp,config);
            remoteFilePath = new String(remoteFilePath.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            doResult = ftp.deleteFile(remoteFilePath);
        }catch (Exception e){
            logger.info("删除附件异常："+e.getMessage());
        }finally {
            if(ftp != null){
                ftpClientPool.returnObject(config,ftp);
            }
        }
        return doResult;
    }

    /**
     * 附件下载
     * @param remoteFilePath
     * @param fileName
     * @param config
     * @param out
     * @return
     */
    public boolean downLoadFile(FTPClient ftp,String remoteFilePath,String fileName, FtpClientConfig config,OutputStream out){
        boolean downLoadSucess = false;
        try{
            ftp.enterLocalPassiveMode();
            checkCharset(ftp,config);
            remoteFilePath = new String(remoteFilePath.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            downLoadSucess = ftp.changeWorkingDirectory(remoteFilePath);
            if(downLoadSucess){
                fileName = new String(fileName.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
                downLoadSucess = ftp.retrieveFile(fileName, out);
            }
        }catch (Exception e){
            logger.info("下载附件异常："+e.getMessage());
        }
        return downLoadSucess;
    }

    public boolean checkFileExist(FTPClient ftp,String remoteFilePath, String filename,FtpClientConfig config) {
        boolean result = false;
        InputStream is = null;
        try {
            ftp.enterLocalPassiveMode();
            checkCharset(ftp,config);
            remoteFilePath = new String(remoteFilePath.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            result = ftp.changeWorkingDirectory(remoteFilePath);
            if(result){
                result = false;
                filename = new String(filename.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
                // 检验文件是否存在
                is = ftp.retrieveFileStream(filename);
                if(is == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE){
                    return false;
                }
                result = true;
            }
        } catch (Exception e) {
            logger.info("校验附件是否存在异常："+e.getMessage());
        }finally {
            if(is != null){
                try {
                    is.close();
                    ftp.completePendingCommand();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void checkCharset(FTPClient client,FtpClientConfig config){
        try {
            if (FTPReply.isPositiveCompletion(client.sendCommand("OPTS UTF8", "ON"))) {
                config.setChartset(UTF_CHARSET);
                logger.info("ftp支持utf-8文件编码！");
            } else {
                config.setChartset(GHK_CHARSET);
                logger.info("ftp不支持utf-8文件编码！");
            }
        }catch (Exception e){

        }
    }

    private String toFtpInfo(FtpClientConfig k) {
        StringBuffer sb = new StringBuffer();
        sb.append("[")
                .append(k.getHost())
                .append(":")
                .append(k.getPort())
                .append("]")
                .append("/");
        return sb.toString();
    }


}
