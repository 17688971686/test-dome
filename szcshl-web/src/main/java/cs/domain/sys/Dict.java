package cs.domain.sys;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cs.domain.DomainBase;

/**
 * 字典类
 * @author lqs
 * */

@Entity
@Table(name = "cs_dict")
public class Dict extends DomainBase{

	//主键
	@Id
	@Column(columnDefinition="VARCHAR(50)")
	private String dictId;
	//父级
	@Column(columnDefinition="VARCHAR(50)")
	private String parentId;
	//字典编码
	@Column(columnDefinition="VARCHAR(50)")
	private String dictCode;
	//字典值，业务表保存
	@Column(columnDefinition="VARCHAR(64)")
	private String dictKey;
	//字典名称，字面值
	@Column(columnDefinition="VARCHAR(100)")
	private String dictName;
	
	//排序
	@Column(columnDefinition="VARCHAR(100)")
	private String dictDesc;
	//是否在用,0表示在用，1表示禁用，默认0
	@Column(nullable = false,columnDefinition="VARCHAR(1)")
	private String isUsed = "0";
	//字典类型，0表示字典分类，1表示字典数据,不可为空
	@Column(nullable=true, columnDefinition="VARCHAR(1)")
	private String dictType = "0";
	
	//是否是系统字典，系统字典项不允许删除，应用运行时初始化,0是系统字典，1是应用字典，默认是1
	@Column(columnDefinition="VARCHAR(1)")
	private String isSysDict = "1";
	
	public String getDictId() {
		return dictId;
	}
	public void setDictId(String dictId) {
		this.dictId = dictId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDicKey() {
		return dictKey;
	}
	public void setDicKey(String dicKey) {
		this.dictKey = dicKey;
	}
	public String getDicName() {
		return dictName;
	}
	public void setDicName(String dicName) {
		this.dictName = dicName;
	}

	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public String getDictKey() {
		return dictKey;
	}
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	public String getDictDesc() {
		return dictDesc;
	}
	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}
	public String getIsSysDict() {
		return isSysDict;
	}
	public void setIsSysDict(String isSysDict) {
		this.isSysDict = isSysDict;
	}

	
}
