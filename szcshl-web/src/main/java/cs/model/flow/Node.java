package cs.model.flow;

/**
 * 流程节点信息
 * @author ldm
 *
 */
public class Node {

	private String activitiId;
	private String activitiName;
	private boolean isConcurrent;	//是否并行流程
	
	public String getActivitiId() {
		return activitiId;
	}
	public void setActivitiId(String activitiId) {
		this.activitiId = activitiId;
	}
	public String getActivitiName() {
		return activitiName;
	}
	public void setActivitiName(String activitiName) {
		this.activitiName = activitiName;
	}

	public boolean getIsConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(boolean isConcurrent) {
		this.isConcurrent = isConcurrent;
	}
}
