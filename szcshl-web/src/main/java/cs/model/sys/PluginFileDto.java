package cs.model.sys;

import cs.common.utils.FileUtils;

import java.io.File;

/**
 * 插件管理
 */
public class PluginFileDto {

    private String fileName;
    private String filePath;
    private String relativePath;
    private Long fileSize;
    private String fileLength;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFileLength() {
        return fileLength;
    }

    public void setFileLength(String fileLength) {
        this.fileLength = fileLength;
    }

    public PluginFileDto(File file,String relativePath){
        this.fileName = file.getName();
        this.filePath = file.getPath();
        this.fileSize = file.length();
        this.fileLength = FileUtils.getFileSize(this.fileSize);
        this.relativePath = relativePath + "/"+ this.fileName;
    }
}
