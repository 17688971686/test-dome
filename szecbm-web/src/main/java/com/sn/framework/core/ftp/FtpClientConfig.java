package com.sn.framework.core.ftp;

import lombok.Data;

@Data
public class FtpClientConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    // 1=upload
    // 2=download
    private int transType;
    private String chartset;
    private String ftpRoot;

    public FtpClientConfig() {
    }

    public FtpClientConfig(String host, int port, String username,
                           String password, int transType,String ftpRoot) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.transType = transType;
        this.ftpRoot = ftpRoot;
    }


    @Override
    public String toString() {
        return "FtpClientConfig [host=" + host + ", port=" + port + ", username=" + username
                + ", transType=" + transType + "]";
    }

}
