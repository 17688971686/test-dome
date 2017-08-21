package cs.service.flow;

import cs.common.Constant;
import cs.common.utils.Validate;
import cs.model.flow.Node;

import java.util.List;
import java.util.Map;

/**
 * 下一环节优化，主要是筛出已经确定的项
 *
 * Created by ldm on 2017/8/21.
 */
public enum FlowNextNodeFilter {
    SIGN_FW{
        //项目发文，有项目负责人，则隐藏部长审核环节
        @Override
        public List<Node> filterNextNode(Map<String,Object> businessMap,List<Node> nextNodeList) {
            if(Validate.isMap(businessMap)){
                if(businessMap.get("prilUserList") != null){
                    for(int i=0;i<nextNodeList.size();i++){
                        if((nextNodeList.get(i).getActivitiId()).equals(Constant.FLOW_SIGN_BMLD_QRFW))
                            nextNodeList.remove(i);
                    }
                }
            }
            return nextNodeList;
        }
    },
    SIGN_GD{
        //项目归档，如果有第二负责人，则隐藏确认归档环节
        @Override
        public List<Node> filterNextNode(Map<String,Object> businessMap,List<Node> nextNodeList) {
            if(businessMap.get("checkFileUser") != null){
                for(int i=0;i<nextNodeList.size();i++){
                    if((nextNodeList.get(i).getActivitiId()).equals(Constant.FLOW_SIGN_QRGD))
                        nextNodeList.remove(i);
                }
            }
            return nextNodeList;
        }
    };

    public abstract List<Node> filterNextNode(Map<String,Object> businessMap, List<Node> nextNodeList);

    /**
     * 获取过滤策略
     *
     * @param strategyName
     * @return
     */
    public static FlowNextNodeFilter getInstance(String strategyName) {
        switch (strategyName.toUpperCase()){
            case Constant.FLOW_SIGN_FW:
                return FlowNextNodeFilter.valueOf(Constant.FLOW_SIGN_FW);
            case Constant.FLOW_SIGN_GD:
                return FlowNextNodeFilter.valueOf(Constant.FLOW_SIGN_GD);
            default:
                    return null;
        }

    }
}
