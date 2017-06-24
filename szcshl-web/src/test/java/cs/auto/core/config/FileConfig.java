package cs.auto.core.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成的类信息
 *
 * @author tzg
 * @date 2017/5/7 15:47
 */
public class FileConfig {

    private String fileName;        // 文件名
    private String suffix;          // 文件名后缀
    private String fileType;        // 文件类型（文件后缀）
    private String layer;      // 模块名/包名
    private String templateName;    // 模板名称，包含后缀
    private String outputPath;      // 输出路径

    private Map<String, Object> info = new HashMap<String, Object>(10);

//    public FileConfig() {
//    }

    public FileConfig(FileConst fileConst) {
        this.fileName = "%s" + fileConst.getSuffix();
        this.suffix = fileConst.getSuffix();
        this.fileType = fileConst.getFileType();
        this.layer = fileConst.getLayer();
        this.templateName = fileConst.getTemplateName();
    }

    public FileConfig(String suffix, String fileType, String layer, String templatePath) {
        this.fileName = "%s" + suffix;
        this.suffix = suffix;
        this.fileType = fileType;
        this.layer = layer;
        this.templateName = templatePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
