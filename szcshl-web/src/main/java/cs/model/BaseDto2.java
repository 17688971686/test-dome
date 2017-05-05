package cs.model;

import cs.common.Util;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public abstract class BaseDto2<T> {
	
	private String createdDate;
	private String createdBy;
	
	private String modifiedDate;
	private String modifiedBy;
	
	//流程参数
	private String taskId;				//任务ID	
	private String processInstanceId;	//流程实例ID

	protected abstract Class<T> getCls();

	public T getDomain() {
		T target;
		try {
			if (getCls() == null) return null;
			target = getCls().newInstance();
		} catch (Exception e) {
			return null;
		}
		BeanUtils.copyProperties(this, target);
		return target;
	}

	public <T> T getDomain(Class<T> cls) {
		T target;
		try {
			target = cls.newInstance();
		} catch (Exception e) {
			return null;
		}
		BeanUtils.copyProperties(this, target);
		return target;
	}

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = Util.formatDate(createdDate);
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = Util.formatDate(modifiedDate);
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
