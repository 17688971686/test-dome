package cs.model.flow;

import java.util.List;
import java.util.Map;

import cs.model.sys.UserDto;

/**
 * 流程处理参数
 *
 * @author ldm
 */
public class FlowDto {

    private String taskId;               //任务ID
    private String processKey;           //流程类型
    private String processInstanceId;    //流程实例ID
    private String nextNodeAcivitiId;    //下一环节节点ID
    private String dealOption;           //处理意见
    private boolean end;                 //是否已经结束
    private boolean isSuspended;         //是否已经暂停
    private String rollBackActiviti;     //回退环节
    private String backNodeDealUser;     //回退环节的处理人
    private Map<String, Object> businessMap;    //业务参数

    /******************    以下字段用于页面显示  ********************/
    private Node curNode;                    //当前节点
    private List<Node> nextNode;             //下一节点

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

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public Map<String, Object> getBusinessMap() {
        return businessMap;
    }

    public void setBusinessMap(Map<String, Object> businessMap) {
        this.businessMap = businessMap;
    }


    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }


    public FlowDto() {
        super();
    }

    public FlowDto(String dealOption) {
        this.dealOption = dealOption;
    }

}
