package cs.common.utils;

import cs.common.constants.FlowConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程工具类
 *
 * @author ldm
 */
public class ActivitiUtil {

    /**
     * 设置是否做工作方案
     *
     * @param variables
     * @param value
     * @param brandId
     **/
    public static void setWorkParam(Map<String, Object> variables, String brandId, boolean value) {
        if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(brandId)) {
            variables.put(FlowConstant.SignFlowParams.WORK_PLAN1.getValue(), value);
        } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(brandId)) {
            variables.put(FlowConstant.SignFlowParams.WORK_PLAN2.getValue(), value);
        } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(brandId)) {
            variables.put(FlowConstant.SignFlowParams.WORK_PLAN3.getValue(), value);
        } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(brandId)) {
            variables.put(FlowConstant.SignFlowParams.WORK_PLAN4.getValue(), value);
        }
    }

    public static void setFlowBrandLead(Map<String, Object> variables, int brandId, String leaderId) {
        switch (brandId) {
            case 2:
                variables.put(FlowConstant.SignFlowParams.BRANCH2.getValue(), true);
                variables.put(FlowConstant.SignFlowParams.USER_BZ2.getValue(), leaderId);
                break;
            case 3:
                variables.put(FlowConstant.SignFlowParams.BRANCH3.getValue(), true);
                variables.put(FlowConstant.SignFlowParams.USER_BZ3.getValue(), leaderId);
                break;
            case 4:
                variables.put(FlowConstant.SignFlowParams.BRANCH4.getValue(), true);
                variables.put(FlowConstant.SignFlowParams.USER_BZ4.getValue(), leaderId);
                break;
            default:
                ;
        }
    }

    public static void setFlowPriUser(Map<String, Object> variables, String branchIndex, String assigneeValue) {
        //设定下一环节处理人
        if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
            variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
        } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
            variables.put(FlowConstant.SignFlowParams.USER_FZR2.getValue(), assigneeValue);
        } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
            variables.put(FlowConstant.SignFlowParams.USER_FZR3.getValue(), assigneeValue);
        } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
            variables.put(FlowConstant.SignFlowParams.USER_FZR4.getValue(), assigneeValue);
        }
    }

    public static Map<String, Object> setAssigneeValue(String key, String assignee) {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put(key, assignee);
        return map;
    }

    /**
     * 毫秒转化时分秒毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        if (ms == null) {
            return "";
        }
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }


}
