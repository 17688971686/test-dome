package cs.common.ftp;

import cs.domain.sys.Ftp;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FtpUtils {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);
    private static final String GHK_CHARSET = "GBK";
    private static final String UTF_CHARSET = "UTF-8";
    private FtpClientPool ftpClientPool = new FtpClientPool(new FtpClientFactory(),new FtpPoolConfig());

    private FTPClient getFtpClient(FtpClientPool ftpClientPool, FtpClientConfig config) throws Exception {
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
        try{
           FTPClient client = ftpClientPool.borrowObject(k);
           if(client != null){
               return true;
           }
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean putFile(FtpClientConfig k,String remoteBaseDir,String filename, InputStream in) throws IOException {
        boolean result = false;
        try {
            FTPClient ftp = getFtpClient(ftpClientPool,k);
            checkCharset(ftp,k);
            // 内网设置为被动模式
            ftp.enterLocalPassiveMode();
            //涉及到中文问题 根据系统实际编码改变
            remoteBaseDir = new String(remoteBaseDir.getBytes(k.getChartset()==null?GHK_CHARSET:k.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            filename = new String(filename.getBytes(k.getChartset()==null?GHK_CHARSET:k.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            if (!ftp.changeWorkingDirectory(remoteBaseDir)) {
                //如果目录不存在创建目录
                String[] dirs = remoteBaseDir.replace(File.separator, "@").split("@");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += File.separator + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            if (!ftp.storeFile(filename, in)) {
                throw new IOException("无法上传附件【" + filename + "】请检查文件服务器地址和权限信息是否正确。");
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
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
        try{
            FTPClient ftp = getFtpClient(ftpClientPool,config);
            checkCharset(ftp,config);
            remoteFilePath = new String(remoteFilePath.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            return ftp.deleteFile(remoteFilePath);
        }catch (Exception e){
            logger.info("删除附件异常："+e.getMessage());
            return false;
        }
    }

    /**
     * 附件下载
     * @param remoteFilePath
     * @param fileName
     * @param config
     * @param out
     * @return
     */
    public boolean downLoadFile(String remoteFilePath,String fileName, FtpClientConfig config,OutputStream out){
        try{
            FTPClient ftp = getFtpClient(ftpClientPool,config);
            ftp.enterLocalPassiveMode();
            checkCharset(ftp,config);
            remoteFilePath = new String(remoteFilePath.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            boolean sucess = ftp.changeWorkingDirectory(remoteFilePath);
            if(sucess){
                fileName = new String(fileName.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
                sucess = ftp.retrieveFile(fileName, out);
                if(sucess){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception e){
            logger.info("下载附件异常："+e.getMessage());
            return false;
        }
    }

    public boolean checkFileExist(String remoteFilePath, String filename,FtpClientConfig config) {
        boolean result = false;
        try {
            FTPClient ftp = getFtpClient(ftpClientPool,config);
            ftp.enterLocalPassiveMode();
            checkCharset(ftp,config);
            remoteFilePath = new String(remoteFilePath.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
            result = ftp.changeWorkingDirectory(remoteFilePath);
            if(result){
                result = false;
                filename = new String(filename.getBytes(config.getChartset()), FTP.DEFAULT_CONTROL_ENCODING);
                // 检验文件是否存在
                InputStream is = ftp.retrieveFileStream(filename);
                if(is == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE){
                    return false;
                }

                if(is != null){
                    is.close();
                    ftp.completePendingCommand();
                }
                return true;
            }
        } catch (Exception e) {

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

    public static void main(String[] args) throws Exception {
        try {
            Ftp f = new Ftp();
            f.setIpAddr("172.30.36.217");
            f.setUserName("ftptest");
            f.setPwd("123456");
            FtpUtils ftpUtils = new FtpUtils();
            FtpClientConfig k = ConfigProvider.getDownloadConfig(f);
           //文件上传测试
            /*String remote = File.separator + "ftp217" + File.separator + "test" + File.separator + "委附件";
            File loaclFile = new File("D:\\鹏微公司服务器.txt");
            boolean uploadResult = ftpUtils.putFile( k,remote,"鹏微公司服务器4.txt",new FileInputStream(loaclFile));
            System.out.print("附件上传结果："+uploadResult);*/
            //文件删除
            /*String remote = File.separator + "ftp217" + File.separator + "test" + File.separator + "委附件" +  File.separator+"鹏微公司服务器4.txt";
            boolean deleteResult = ftpUtils.remove(remote,k);
            System.out.print("附件删除结果结果："+deleteResult);*/
            //文件下载
           /* String downLoadUrl = File.separator + "通知公告" + File.separator + "2c9ea45f60fdbc3e0160fdc02f910000";
            String downLoadName = "2018_1_16_14_1988294872.txt";
            File downFile = new File("D:\\鹏微公司服务器down.txt");
            boolean downResult = ftpUtils.downLoadFile(downLoadUrl,downLoadName,k,new FileOutputStream(downFile));
            System.out.print("附件下载结果："+downResult);*/
            //验证文件是否存在
            String checkFileUrl = File.separator + "通知公告" + File.separator + "2c9ea45f60fdbc3e0160fdc02f910000";
            String checkFileName = "2018_1_16_14_1988294872.txt";
            boolean downResult = ftpUtils.checkFileExist(checkFileUrl,checkFileName,k);
            System.out.print("验证文件是否存在："+downResult);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
