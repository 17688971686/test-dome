package com.sn.framework.core.ftp;


import com.sn.framework.module.sys.domain.Ftp;

/**
 * Created by ldm on 2018/1/23.
 */
public class ConfigProvider {
    private static int DEAFULT_REMOTE_PORT = 21;

    public static FtpClientConfig getUploadConfig(Ftp ftp) {
        FtpClientConfig config = new FtpClientConfig();
        config.setHost(ftp.getIpAddr());
        config.setPort(ftp.getPort() == null?DEAFULT_REMOTE_PORT:ftp.getPort());
        config.setUsername(ftp.getUserName());
        config.setPassword(ftp.getPwd());
        config.setTransType(1);
        config.setFtpRoot(ftp.getPath());
        return config;
    }

    public static FtpClientConfig getDownloadConfig(Ftp ftp) {
        FtpClientConfig config = new FtpClientConfig();
        config.setHost(ftp.getIpAddr());
        config.setPort(ftp.getPort() == null?DEAFULT_REMOTE_PORT:ftp.getPort());
        config.setUsername(ftp.getUserName());
        config.setPassword(ftp.getPwd());
        config.setTransType(2);
        return config;
    }
}
