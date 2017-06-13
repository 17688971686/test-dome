package cs.model.sys;

import cs.model.BaseDto;


/**
 * Description: 系统参数 页面数据模型
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
public class SysConfigDto extends BaseDto {

    private String id;
    private String configName;
    private String configKey;
    private String configValue;
    private String descInfo;
    private String isShow;

    public SysConfigDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescInfo() {
        return descInfo;
    }

    public void setDescInfo(String descInfo) {
        this.descInfo = descInfo;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}