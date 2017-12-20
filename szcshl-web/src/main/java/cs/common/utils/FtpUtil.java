package cs.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by zsl on 2017/11/2 0002.
 */
public class FtpUtil {

    private static Logger logger = Logger.getLogger(FtpUtil.class);

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
            ftp.connect(f.getIpAddr(), 21);
        } else {
            ftp.connect(f.getIpAddr(), f.getPort());
        }
        ftp.login(f.getUserName(), f.getPwd());
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
        flag = true;
        return flag;
    }

    /**
     * 关闭ftp连接
     */
    public static void closeFtp() {
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ftp上传文件
     *
     * @param f
     * @throws Exception
     */
    public static void upload(File f) throws Exception {
        if (f.isDirectory()) {
            ftp.makeDirectory(f.getName());
            ftp.changeWorkingDirectory(f.getName());
            String[] files = f.list();
            for (String fstr : files) {
                File file1 = new File(f.getPath() + "/" + fstr);
                if (file1.isDirectory()) {
                    upload(file1);
                    ftp.changeToParentDirectory();
                } else {
                    File file2 = new File(f.getPath() + "/" + fstr);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        } else {
            File file2 = new File(f.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), input);
            input.close();
        }
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
                String filename = new String(ftpFile.getName().getBytes("GBK"), "utf-8");//涉及到中文文件
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
            remoteBaseDir = new String(remoteBaseDir.getBytes("GBK"), "iso-8859-1");
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            if (!ftp.changeWorkingDirectory(remoteBaseDir)) {
                //如果目录不存在创建目录
                String[] dirs = remoteBaseDir.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
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
            closeFtp();
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
                String localFileName = new String(ff.getName().getBytes("ISO-8859-1"), "GBK");
                if (localFileName.equals(fileName)) {
                    File localFile = new File(localPath + "/" + localFileName);

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
            remoteFilePath = new String(remoteFilePath.getBytes("GBK"), "iso-8859-1");
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
            remoteBaseDir = new String(remoteBaseDir.getBytes("GBK"), "iso-8859-1");
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            ftp.changeWorkingDirectory(remoteBaseDir);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile f : fs) {
                if (filename.equals(f.getName())) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * 对ftp的路径进行格式化处理
     *
     * @param fdir
     * @return
     */
    public static String processDir(String fdir) {
        fdir = fdir.replace("/", "\\").replace("\\\\", "\\");
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
        f.setIpAddr("172.16.13.146");
        f.setUserName("ftptest");
        f.setPwd("123456");
        FtpUtil.connectFtp(f);
        String remote = "/通知公告/无归类文件/tomcat.rar";
        //
        //FtpUtil.upload(file);//把文件上传在ftp上
        //FtpUtil.startDown(f, "d:/", remoteUrl);//下载ftp文件测试
        /*File file = new File("D:/鹏微公司服务器.txt");
        FtpUtil.uploadFile(remote, "鹏微公司服务器.txt", new FileInputStream(file));*/

        //System.out.println(FtpUtil.checkFileExist(remote,"tomcat.rar"));
        System.out.println(FtpUtil.removeFile(remote));
    }

}