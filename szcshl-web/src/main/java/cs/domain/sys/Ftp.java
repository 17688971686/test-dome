package cs.domain.sys;
import javax.persistence.*;


/**
 * ftp链接常量
 */
@Entity
@Table(name = "cs_ftp")
public class Ftp {
    @Id
    private String ftpId;

    //ip地址
    @Column(columnDefinition = "varchar(30)")
    private String ipAddr;

    //端口号
    @Column(columnDefinition = "Integer")
    private Integer port;

    //用户名
    @Column(columnDefinition = "varchar(30)")
    private String userName;

    //密码
    @Column(columnDefinition = "varchar(60)")
    private String pwd;

    /**
     * 根路径
     */
    @Column(columnDefinition = "varchar(100)")
    private String path;

    /**
     * 是否当前在用 9表示是，0表示否
     */
    @Column(columnDefinition = "varchar(2) default 0")
    private String enable;

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFtpId() {
        return ftpId;
    }

    public void setFtpId(String ftpId) {
        this.ftpId = ftpId;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

}
