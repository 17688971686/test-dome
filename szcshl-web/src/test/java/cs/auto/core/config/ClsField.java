package cs.auto.core.config;

/**
 * Description: 实体字段信息
 * User: tzg
 * Date: 2017/5/6 17:44
 */
public class ClsField {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}