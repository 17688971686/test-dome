package cs.auto.core.config;

/**
 * 生成的类信息
 *
 * @author tzg
 * @date 2017/5/7 15:47
 */
public class FileConfig {

    private String fileName;
    private String templatePath;
    private String outputPath;

    public FileConfig() {
    }

    public FileConfig(String fileName, String templatePath, String outputPath) {
        this.fileName = fileName;
        this.templatePath = templatePath;
        this.outputPath = outputPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
