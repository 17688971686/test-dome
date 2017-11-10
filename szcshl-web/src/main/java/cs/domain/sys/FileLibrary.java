package cs.domain.sys;

import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Description: 文件夹管理
 * Author: mcl
 * Date: 2017/8/21 11:46
 */
@Entity
@Table(name="cs_fileLibrary")
public class FileLibrary extends DomainBase{

    @Id
    private String fileId ;//Id


    /**
     * 文件路径
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String fileUrl;

    /**
     * 文件名称
     */
    @Column(columnDefinition = "VARCHAR(128)")
    private String fileName;

    /**
     * 文件性质  ： FOLDER : 文件夹      FILE : 文件
     */
    @Column(columnDefinition = "VARCHAR(16)")
    private String fileNature;

    /**
     * 文件库类型  POLICY:政策标准文件库    QUALITY:质量管理文件库
     */
    @Column(columnDefinition = "VARCHAR(16)")
    private String fileType;

    /**
     * 父id
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String parentFileId;

    /**
     * 文件内容
     */
    @Column(columnDefinition = "CLOB")
    private String fileContent;

    /**
     * 排序
     */
    @Column(columnDefinition = "INTEGER")
    private Integer fileSort;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getParentFileId() {
        return parentFileId;
    }

    public void setParentFileId(String parentFileId) {
        this.parentFileId = parentFileId;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public Integer getFileSort() {
        return fileSort;
    }

    public void setFileSort(Integer fileSort) {
        this.fileSort = fileSort;
    }

    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileNature() {
        return fileNature;
    }

    public void setFileNature(String fileNature) {
        this.fileNature = fileNature;
    }
}