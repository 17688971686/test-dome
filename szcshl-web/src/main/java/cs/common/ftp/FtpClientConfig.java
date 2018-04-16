package cs.common.ftp;

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

    @Override
    public String toString() {
        return "FtpClientConfig [host=" + host + ", port=" + port + ", username=" + username
                + ", transType=" + transType + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FtpClientConfig)) return false;

        FtpClientConfig that = (FtpClientConfig) o;

        if (port != that.port) return false;
        if (transType != that.transType) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (chartset != null ? !chartset.equals(that.chartset) : that.chartset != null) return false;
        return ftpRoot != null ? ftpRoot.equals(that.ftpRoot) : that.ftpRoot == null;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + transType;
        result = 31 * result + (chartset != null ? chartset.hashCode() : 0);
        result = 31 * result + (ftpRoot != null ? ftpRoot.hashCode() : 0);
        return result;
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
}
