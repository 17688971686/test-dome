package cs.auto.core.config;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础配置抽象类
 *
 * @author tzg
 * @date 2017/5/7 15:29
 */
public abstract class AbstractGanConfig {

    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * 创建配置信息
     */
    public abstract void autoGenerate();

    /**
     * freemarker模板参数
     */
    protected Map<String, Object> paramMap = new HashMap<String, Object>(10);
    protected List<FileConfig> fileConfs = new ArrayList<FileConfig>();

    /**
     * 模板路径
     */
    protected String templatePath;

    /**
     * 输出目录路径
     */
    protected String ouputPath = "D:\\tmp"; // 默认在D盘创建一个零售目录（只支持Windows）

    /**
     * 是否覆盖
     */
    protected boolean fileOverride = false;

    /**
     * 是否打开输出目录
     */
    protected boolean open = true;

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public List<FileConfig> getFileConfs() {
        return fileConfs;
    }

    public void setFileConfs(List<FileConfig> fileConfs) {
        this.fileConfs = fileConfs;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getOuputPath() {
        return ouputPath;
    }

    public void setOuputPath(String ouputPath) {
        this.ouputPath = ouputPath;
    }

    public boolean isFileOverride() {
        return fileOverride;
    }

    public void setFileOverride(boolean fileOverride) {
        this.fileOverride = fileOverride;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    protected String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
