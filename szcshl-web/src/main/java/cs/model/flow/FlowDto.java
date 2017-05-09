package cs.model.flow;

import java.util.List;
import java.util.Map;

import cs.model.sys.UserDto;

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
	private String nextNodeAcivitiId;	//下一环节节点ID
	private String dealOption;			//处理意见
	private boolean end;				//是否已经结束
	private boolean seleteNode;		//是否是选择环节
	private String rollBackActiviti;	//回退环节
	private String backNodeDealUser;	//回退环节的处理人
	
	/******************    以下字段用于页面显示  ********************/
	private Node curNode;					//当前节点
	private List<Node> nextNode;			//下一节点
	private List<UserDto> nextDealUserList;	//下一环节处理人
	private Map<String,List<UserDto>> nextUserListMap;		//下一环节处理人(多选)	
			
	public Map<String, List<UserDto>> getNextUserListMap() {
		return nextUserListMap;
	}
	public void setNextUserListMap(Map<String, List<UserDto>> nextUserListMap) {
		this.nextUserListMap = nextUserListMap;
	}
	public List<UserDto> getNextDealUserList() {
		return nextDealUserList;
	}
	public void setNextDealUserList(List<UserDto> nextDealUserList) {
		this.nextDealUserList = nextDealUserList;
	}
	public Node getCurNode() {
		return curNode;
	}
	public void setCurNode(Node curNode) {
		this.curNode = curNode;
	}
	public List<Node> getNextNode() {
		return nextNode;
	}
	public void setNextNode(List<Node> nextNode) {
		this.nextNode = nextNode;
	}
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
		
	public String getNextNodeAcivitiId() {
		return nextNodeAcivitiId;
	}
	public void setNextNodeAcivitiId(String nextNodeAcivitiId) {
		this.nextNodeAcivitiId = nextNodeAcivitiId;
	}		
		
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	public boolean isSeleteNode() {
		return seleteNode;
	}
	public void setSeleteNode(boolean seleteNode) {
		this.seleteNode = seleteNode;
	}
	public String getRollBackActiviti() {
		return rollBackActiviti;
	}
	public void setRollBackActiviti(String rollBackActiviti) {
		this.rollBackActiviti = rollBackActiviti;
	}
		
	public String getBackNodeDealUser() {
		return backNodeDealUser;
	}
	public void setBackNodeDealUser(String backNodeDealUser) {
		this.backNodeDealUser = backNodeDealUser;
	}
	public FlowDto() {
		super();
	}	
}
