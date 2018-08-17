package com.sn.framework.module.sys.domain;

import com.sn.framework.core.DomainBase;

import javax.persistence.*;

/**
 * Description: 系统变量信息   数据映射实体
 *
 * @author: tzg
 * @date: 2018/1/23 20:03
 */
@Entity(name = "cs_sys_variable")
public class SysVariable extends DomainBase {

    private static final long serialVersionUID = -1470082666390301878L;

    @Id
    @Column(length = 64)
    private String varId;
    /** 参数分类 */
    @Column(length = 64, nullable = false)
    private String varCategory;
    /** 参数值类型 */
    @Column(length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
    private SysVariableType varType;
    /** 参数编码 */
    @Column(length = 128, nullable = false, unique = true)
    private String varCode;
    /** 参数名 */
    @Column(length = 128, nullable = false)
    private String varName;
    /** 参数值 */
    @Column(length = 128, nullable = false)
    private String varValue;
    /** 备注 */
    @Column
    private String remark;
    /** 变量数据的类型 */
    @Column
    private int varDataType;

    public final Object getValue() {

        return varType.getValue(varValue);
    }

    public String getVarId() {
        return varId;
    }

    public void setVarId(String varId) {
        this.varId = varId;
    }

    public String getVarCategory() {
        return varCategory;
    }

    public void setVarCategory(String varCategory) {
        this.varCategory = varCategory;
    }

    public SysVariableType getVarType() {
        return varType;
    }

    public void setVarType(SysVariableType varType) {
        this.varType = varType;
    }

    public String getVarCode() {
        return varCode;
    }

    public void setVarCode(String varCode) {
        this.varCode = varCode;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarValue() {
        return varValue;
    }

    public void setVarValue(String varValue) {
        this.varValue = varValue;
        if (null == this.varType) {
            this.varType = SysVariableType.STRING;
        }
    }

    public void setVarValue(Boolean varValue) {
        this.varValue = varValue.toString();
        this.varType = SysVariableType.BOOLEAN;
    }

    public void setVarValue(Integer varValue) {
        this.varValue = varValue.toString();
        this.varType = SysVariableType.INTEGER;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getVarDataType() {
        return varDataType;
    }

    public void setVarDataType(int varDataType) {
        this.varDataType = varDataType;
    }
}