package cs.domain.sys;

import cs.domain.DomainBase;

import javax.persistence.*;

/**
 * @author ldm
 *         系统文件
 */
@Entity
@Table(name = "cs_sysfile")
public class SysFile extends DomainBase {
    //主键
    @Id
    private String sysFileId;

    @Column(columnDefinition = "varchar(64) NOT NULL")
    private String businessId;

    @Column(columnDefinition = "varchar(256) NOT NULL")
    private String fileUrl;

    @Column(columnDefinition = "varchar(256) NOT NULL")
    private String showName;

    @Column(columnDefinition = "Long")
    private Long fileSize;

    @Column(columnDefinition = "varchar(16)")
    private String fileType;

    /**
     * 主键ID，例如，收文为收文ID，课题研究为课题研究ID
     */
    @Column(columnDefinition = "varchar(64)")
    private String mainId;

    /**
     * 主要业务名称，所有统一类型的附件，放在同一个文件夹（例如：收文为sign,课题为 topic,....）
     */
    private String mainType;
    /**
     * 文件模块类型（例如，收文环节，为收文，工作方案环节，为工作方案，跟businessId对应）
     */
    @Column(columnDefinition = "varchar(255)")
    private String sysfileType;

    /**
     * 文件业务类型（例如，评审方案，会签准备材料等）
     */
    @Column(columnDefinition = "varchar(255)")
    private String sysBusiType;

    @ManyToOne
    @JoinColumn(name = "ftpId")
    private Ftp ftp;


    public String getSysFileId() {
        return sysFileId;
    }

    public void setSysFileId(String sysFileId) {
        this.sysFileId = sysFileId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSysfileType() {
        return sysfileType;
    }

    public void setSysfileType(String sysfileType) {
        this.sysfileType = sysfileType;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getSysBusiType() {
        return sysBusiType;
    }

    public void setSysBusiType(String sysBusiType) {
        this.sysBusiType = sysBusiType;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }


    public SysFile(String sysFileId, String businessId, String fileUrl, String showName, Long fileSize, String fileType,
                   String mainId, String mainType, String sysfileType, String sysBusiType) {
        this.sysFileId = sysFileId;
        this.businessId = businessId;
        this.fileUrl = fileUrl;
        this.showName = showName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.mainId = mainId;
        this.mainType = mainType;
        this.sysfileType = sysfileType;
        this.sysBusiType = sysBusiType;
    }

    public SysFile(String sysFileId, String businessId, String fileUrl, String showName, Long fileSize, String fileType, String mainId, String mainType, String sysfileType, String sysBusiType,Ftp ftp) {
        this.sysFileId = sysFileId;
        this.businessId = businessId;
        this.fileUrl = fileUrl;
        this.showName = showName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.mainId = mainId;
        this.mainType = mainType;
        this.sysfileType = sysfileType;
        this.sysBusiType = sysBusiType;
        this.ftp = ftp;

    }

    public SysFile() {
        super();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Ftp getFtp() {
        return ftp;
    }

    public void setFtp(Ftp ftp) {
        this.ftp = ftp;
    }
}
