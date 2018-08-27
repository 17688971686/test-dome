package com.sn.framework.core.ftp;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getChartset() {
        return chartset;
    }

    public void setChartset(String chartset) {
        this.chartset = chartset;
    }

    public String getFtpRoot() {
        return ftpRoot;
    }

    public void setFtpRoot(String ftpRoot) {
        this.ftpRoot = ftpRoot;
    }

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
