package cs.common.utils;

import cs.domain.sys.Ftp;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * @author ldm
 */
public class FtpUtil {

    private static Logger logger = Logger.getLogger(FtpUtil.class);
    private static int DEAFULT_REMOTE_PORT = 21;
    public static String ISO_CHARSET = "ISO-8859-1";
    public static String LOCAL_CHARSET = "GBK";
    public static String GBK_CHARSET = "GBK";
    public static String UTF_CHARSET = "UTF-8";
    private static FTPClient ftp;

    public static FTPClient getFtp() {
        return ftp;
    }

    /**
     * 获取ftp连接
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static boolean connectFtp(Ftp f) throws Exception {
        ftp = new FTPClient();
        boolean flag = false;
        int reply;
        if (f.getPort() == null) {
            ftp.connect(f.getIpAddr(), DEAFULT_REMOTE_PORT);
        } else {
            ftp.connect(f.getIpAddr(), f.getPort());
        }
        if (ftp.login(f.getUserName(), f.getPwd())) {
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
                System.out.println("ftp支持utf-8文件编码！");
            } else {
                LOCAL_CHARSET = "GBK";
                System.out.println("ftp不支持utf-8文件编码！");
            }

            //设置编码格式
            ftp.setControlEncoding(LOCAL_CHARSET);
            // 设置被动模式
            ftp.enterLocalPassiveMode();
            //设置缓冲为3M
            ftp.setBufferSize(3 * 1024);
            //文件类型为二进制文件
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return flag;
            }
            ftp.changeWorkingDirectory(f.getPath());
        }

        flag = true;
        return flag;
    }

    /**
     * 关闭ftp连接
     */
    public static void closeFtp() {
        if (ftp != null && ftp.isConnected()) {
            try {
                boolean result = ftp.logout();
                if (result) {
                    logger.info("成功退出ftp！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("退出FTP服务器异常！" + e.getMessage());
            } finally {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.warn("关闭FTP服务器连接异常！" + e.getMessage());
                }
            }
        }
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param remoteBaseDir FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename      上传到FTP服务器上的文件名
     * @param input         输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String remoteBaseDir, String filename, InputStream input) {
        boolean result = false;
        //切换到上传目录
        try {
            //涉及到中文问题 根据系统实际编码改变
            remoteBaseDir = new String(remoteBaseDir.getBytes(LOCAL_CHARSET), ISO_CHARSET);
            filename = new String(filename.getBytes(LOCAL_CHARSET), ISO_CHARSET);
            if (!ftp.changeWorkingDirectory(remoteBaseDir)) {
                //如果目录不存在创建目录
                String[] dirs = remoteBaseDir.replace(File.separator, "-").split("-");
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
            //上传文件
            if (!ftp.storeFile(filename, input)) {
                return result;
            }
            result = true;
        } catch (IOException e) {
            logger.error("附件上传异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
                                       String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            if (port != 0) {
                ftp.connect(host, port);
            } else {
                ftp.connect(host);
            }
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                String localFileName = new String(ff.getName().getBytes(ISO_CHARSET), GBK_CHARSET);
                if (localFileName.equals(fileName)) {
                    File localFile = new File(localPath + File.separator + localFileName);
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (Exception ioe) {
                }
            }
        }
        return result;
    }


    /**
     * 删除ftp上的文件
     *
     * @param
     * @return true || false
     * @author zsl
     */
    public static boolean removeFile(String remoteFilePath) {
        boolean result = false;
        try {
            remoteFilePath = new String(remoteFilePath.getBytes(LOCAL_CHARSET), ISO_CHARSET);
            ftp.deleteFile(remoteFilePath);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeFtp();
        }
        return result;
    }//end method removeFile

    /**
     * 确认附件是否存在
     *
     * @param remoteBaseDir
     * @param filename
     * @return
     */
    public static boolean checkFileExist(String remoteBaseDir, String filename) {
        boolean result = false;
        try {
            //涉及到中文问题 根据系统实际编码改变
            remoteBaseDir = new String(remoteBaseDir.getBytes(LOCAL_CHARSET), ISO_CHARSET);
            result = ftp.changeWorkingDirectory(remoteBaseDir);// 转移到FTP服务器目录
            if(result){
                result = false;
                FTPFile[] fs = ftp.listFiles();
                for (FTPFile f : fs) {
                    if (filename.equals(f.getName())) {
                        OutputStream outputStream = new FileOutputStream("D:"+File.separator+f.getName());
                        ftp.retrieveFile(f.getName(), outputStream);
                        outputStream.flush();
                        outputStream.close();
                        result = true;
                        break;
                    }
                }
            }else{
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }finally {
            closeFtp();
        }
        return result;
    }

    /**
     * 下载链接配置
     *
     * @param f
     * @param localBaseDir  本地目录
     * @param remoteBaseDir 远程目录
     * @throws Exception
     */
    public static void startDown(Ftp f, String localBaseDir, String remoteBaseDir) throws Exception {
        try {
            FTPFile[] files = null;
            //切换工作路径
            boolean changedir = ftp.changeWorkingDirectory(remoteBaseDir);
            if (changedir) {
                FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
                conf.setServerLanguageCode("zh");//中文
                files = ftp.listFiles();
                for (int i = 0; i < files.length; i++) {
                    try {
                        downloadFile(files[i], localBaseDir, remoteBaseDir);
                    } catch (Exception e) {
                        logger.error(e);
                        logger.error("<" + files[i].getName() + ">下载失败");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            logger.error("下载过程中出现异常");
        }
    }


    /**
     * 下载FTP文件
     * 当你需要下载FTP文件的时候，调用此方法
     * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
     *
     * @param ftpFile
     * @param relativeLocalPath
     * @param relativeRemotePath
     */
    private static void downloadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath) {
        try {
            if (ftpFile.isFile()) {
                String filename = new String(ftpFile.getName().getBytes("iso-8859-1"), "utf-8");//涉及到中文文件
                if (ftpFile.getName().indexOf("?") == -1) {
                    OutputStream outputStream = null;
                    try {
                        File locaFile = new File(relativeLocalPath + filename);
                        //判断文件是否存在，存在则返回
                        if (locaFile.exists()) {
                            return;
                        } else {
                            outputStream = new FileOutputStream(relativeLocalPath + filename);
                            ftp.retrieveFile(ftpFile.getName(), outputStream);
                            outputStream.flush();
                            outputStream.close();
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    } finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            logger.error("输出文件流异常");
                        }
                    }
                }
            } else {
                String newlocalRelatePath = relativeLocalPath + ftpFile.getName();
                String newRemote = new String(relativeRemotePath + ftpFile.getName());
                File fl = new File(newlocalRelatePath);
                if (!fl.exists()) {
                    fl.mkdirs();
                }
                newlocalRelatePath = newlocalRelatePath + '/';
                newRemote = newRemote + "/";
                String currentWorkDir = ftpFile.getName().toString();
                boolean changedir = ftp.changeWorkingDirectory(currentWorkDir);
                if (changedir) {
                    FTPFile[] files = null;
                    files = ftp.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        downloadFile(files[i], newlocalRelatePath, newRemote);
                    }
                }
                if (changedir) {
                    ftp.changeToParentDirectory();
                }
            }
        } catch (Exception e) {
            logger.info("文件下载失败！");
            e.printStackTrace();
        }
    }

    /**
     * 对ftp的路径进行格式化处理
     *
     * @param fdir
     * @return
     */
    public static String processDir(String fdir) {
        if (fdir.startsWith("\\")) {
            fdir = fdir.substring(fdir.indexOf("\\") + 1);
        }
        if (fdir.endsWith("\\")) {
            fdir = fdir.substring(0, fdir.lastIndexOf("\\"));
        }
        return fdir;
    }

    public static void main(String[] args) throws Exception {
        Ftp f = new Ftp();
        /*f.setIpAddr("172.30.36.117");
        f.setUserName("ftptest");
        f.setPwd("123456");*/
        f.setIpAddr("172.30.36.214");
        f.setUserName("szec");
        f.setPwd("863305");
        FtpUtil.connectFtp(f);
        String remote = File.separator + "ftp01" + File.separator + "1003" + File.separator + "评审报告";
        //附件上传测试
        //File file = new File("D:/鹏微公司服务器.txt");
        //FtpUtil.uploadFile(remote, "鹏微公司服务器.txt", new FileInputStream(file));

        System.out.println(FtpUtil.checkFileExist(remote,"深投审[2006]73号.doc"));
        //System.out.println(FtpUtil.removeFile(remote));
        //System.out.println(new String("评审报告".getBytes(GBK_CHARSET), ISO_CHARSET));

    }

}