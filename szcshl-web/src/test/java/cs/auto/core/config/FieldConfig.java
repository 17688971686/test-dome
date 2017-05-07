package cs.auto.core.config;

/**
 * Description: 实体字段信息
 * User: tzg
 * Date: 2017/5/6 17:44
 */
public class FieldConfig {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 是否ID
     */
    private Boolean isId = false;
    /**
     * 是否必填项
     */
    private Boolean required = false;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsId() {
        return isId;
    }

    public void setIsId(Boolean isId) {
        this.isId = isId;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}