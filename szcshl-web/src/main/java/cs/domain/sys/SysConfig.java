package cs.domain.sys;

import cs.domain.DomainBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cs_sysconfig")
public class SysConfig extends DomainBase {
    @Id
    @GeneratedValue(generator = "plansignGenerator")
    @GenericGenerator(name = "plansignGenerator", strategy = "uuid2")
    private String id;

    /**
     * 参数名
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String configName;

    /**
     * 参数key
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String configKey;

    /**
     * 参数值
     */
    @Column(columnDefinition = "VARCHAR(256)")
    private String configValue;

    /**
     * 描述
     */
    @Column(columnDefinition = "VARCHAR(256)")
    private String descInfo;

    /**
     * 是否显示
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isShow;


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
