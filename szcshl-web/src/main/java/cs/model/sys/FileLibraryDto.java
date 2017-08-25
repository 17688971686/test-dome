package cs.model.sys;

import cs.domain.sys.SysFile;
import cs.model.BaseDto;

import java.util.List;

/**
 * Description: 质量管理文件库
 * Author: mcl
 * Date: 2017/8/21 10:43
 */
public class FileLibraryDto extends BaseDto{

    private String fileId ;//Id

    private String fileName;//文件名称

    private String fileUrl;

    private String fileType;//文件库类型   1:文件库   2：政策库

    private String fileNature;//文件性质  1：文件夹   2：文件

    private String parentFileId;//父文件id

    private String fileContent;//文件内容

    private Integer fileSort;//排序

    private List<SysFileDto> sysFileDtoList ;

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

    public List<SysFileDto> getSysFileDtoList() {
        return sysFileDtoList;
    }

    public void setSysFileDtoList(List<SysFileDto> sysFileDtoList) {
        this.sysFileDtoList = sysFileDtoList;
    }
}