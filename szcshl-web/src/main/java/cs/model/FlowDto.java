package cs.model;

/**
 * 流程处理参数
 * @author ldm
 *
 */
public class FlowDto {

	private String taskId;				//任务ID	
	private String processInstanceId;	//流程实例ID
	
	private String nextGroup;			//下一环节处理组（角色）
	private String nextDealUser;		//下一环节处理人
	private String dealOption;			//处理意见

	
	public String getDealOption() {
		return dealOption;
	}
	public void setDealOption(String dealOption) {
		this.dealOption = dealOption;
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
	public String getNextGroup() {
		return nextGroup;
	}
	public void setNextGroup(String nextGroup) {
		this.nextGroup = nextGroup;
	}
	public String getNextDealUser() {
		return nextDealUser;
	}
	public void setNextDealUser(String nextDealUser) {
		this.nextDealUser = nextDealUser;
	}
	
	
}
