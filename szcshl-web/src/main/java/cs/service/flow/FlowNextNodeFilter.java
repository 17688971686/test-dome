package cs.service.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs.common.constants.FlowConstant;
import cs.model.flow.Node;

/**
 * 下一环节优化，主要是筛出已经确定的项
 * @author ldm
 * @date  2017/8/21.
 */
public enum FlowNextNodeFilter {
    /**
     * 项目发文，有项目负责人，则隐藏部长审核环节
     * @param businessMap
     * @param nextNodeList
     * @return
     */
    SIGN_FW {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            if (businessMap.get("prilUserList") != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_QRFW)) {
                        resultList.add(nextNodeList.get(i));
                    }
                }
                //没有第二负责人，到部长审核发文环节
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_BMLD_QRFW)) {
                        resultList.add(nextNodeList.get(i));
                    }
                }
            }
            return resultList;
        }
    },
    /**
     * 项目归档，如果有第二负责人，则隐藏确认归档环节
     */
    SIGN_GD {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            if (businessMap.get("checkFileUser") != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_DSFZR_QRGD)) {
                        resultList.add(nextNodeList.get(i));
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_QRGD)) {
                        resultList.add(nextNodeList.get(i));
                    }
                }
            }
            return resultList;
        }
    },
    /**
     * 部门分办，只显示一个就好
     */
    SIGN_FGLD_FB {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            resultList.add(nextNodeList.get(0));
            return resultList;
        }
    },
    /**
     * 项目负责人确认发文
     */
    SIGN_QRFW {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            //有协办部门
            if (businessMap.get("hasAssistDept") != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_BMLD_QRFW_XB)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_BMLD_QRFW)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }
            return resultList;
        }
    },
    /**
     * 协办部长审批发文
     */
    SIGN_BMLD_QRFW_XB {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            for (int i = 0; i < nextNodeList.size(); i++) {
                if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_BMLD_QRFW)) {
                    resultList.add(nextNodeList.get(i));
                    break;
                }
            }
            return resultList;
        }
    },
    /**
     * 主办部长审批
     */
    SIGN_BMLD_QRFW {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            //有协办分管领导
            if (businessMap.get(FlowConstant.SignFlowParams.HAVE_XB.getValue()) != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_FGLD_QRFW_XB)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_FGLD_QRFW)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }
            return resultList;
        }
    },
    /**
     * 协办分管领导审批发文
     */
    SIGN_FGLD_QRFW_XB {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            for (int i = 0; i < nextNodeList.size(); i++) {
                if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_FGLD_QRFW)) {
                    resultList.add(nextNodeList.get(i));
                    break;
                }
            }
            return resultList;
        }
    },
    /**
     * 生成发文编号
     */
    SIGN_FWBH {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            //有协办分管领导
            if (businessMap.get(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue()) != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_CWBL)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SIGN_GD)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }
            return resultList;
        }
    },
    //以下是档案借阅环节
    ARC_SQ {
        //档案借阅填报
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            if (businessMap.get(FlowConstant.FlowParams.BZ_FZ.getValue()) != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_ARC_BZ_SP)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_ARC_FGLD_SP)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }
            return resultList;
        }
    },
    /**
     * 分管领导审批档案借阅
     */
    ARC_FGLD_SP {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            boolean zrfl = businessMap.get(FlowConstant.FlowParams.ZR_FZ.getValue()) != null?true:false;
            boolean usergdy = businessMap.get(FlowConstant.FlowParams.USER_GDY.getValue()) != null?true:false;
            if(zrfl ||  usergdy){
                for (int i = 0; i < nextNodeList.size(); i++) {
                    //主任分支
                    if (zrfl && (nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_ARC_ZR_SP)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                        //归档员
                    }else if(!zrfl && (nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_ARC_GDY)){
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }

            return resultList;
        }
    },
    /**
     * 主办部长审批拟补充资料函
     */
    SPL_BZ_SP {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            boolean isFgldFz = businessMap.get(FlowConstant.FlowParams.FGLD_FZ.getValue()) != null ? true : false;
            for (int i = 0; i < nextNodeList.size(); i++) {
                //直接到分管领导环节
                if (isFgldFz && (nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SPL_FGLD_SP)) {
                    resultList.add(nextNodeList.get(i));
                    break;
                //协办领导会签环节
                } else if (!isFgldFz && (nextNodeList.get(i).getActivitiId()).equals(FlowConstant.FLOW_SPL_LD_HQ)) {
                    resultList.add(nextNodeList.get(i));
                    break;
                }
            }
            return resultList;
        }
    },
    /**
     * ===============以下是课题流程
     * 主办部长审批拟补充资料函
     */
    TOPIC_ZRSH_JH {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            boolean sendFgw = businessMap.get(FlowConstant.FlowParams.SEND_FGW.getValue()) != null ? true : false;
            for (int i = 0; i < nextNodeList.size(); i++) {
                //直接到分管领导环节
                if (sendFgw && (nextNodeList.get(i).getActivitiId()).equals(FlowConstant.TOPIC_BFGW)) {
                    resultList.add(nextNodeList.get(i));
                    break;
                    //协办领导会签环节
                } else if (!sendFgw && (nextNodeList.get(i).getActivitiId()).equals(FlowConstant.TOPIC_GZFA)) {
                    resultList.add(nextNodeList.get(i));
                    break;
                }
            }
            return resultList;
        }
    },
    /**
     * ===============以下是通知公告
     * 通知公告填报
     */
    ANNOUNT_TZ {
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            if (businessMap.get(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue()) != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.ANNOUNT_FZ)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.ANNOUNT_BZ)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }
            return resultList;
        }
    },
    /**
     * ===============以下月报填报
     * 月报填报
     */
    MONTH_YB{
        @Override
        public List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList) {
            List<Node> resultList = new ArrayList<>(1);
            if (businessMap.get(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue()) != null) {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.MONTH_FG)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nextNodeList.size(); i++) {
                    if ((nextNodeList.get(i).getActivitiId()).equals(FlowConstant.MONTH_BZ)) {
                        resultList.add(nextNodeList.get(i));
                        break;
                    }
                }
            }
            return resultList;
        }
    };

    public abstract List<Node> filterNextNode(Map<String, Object> businessMap, List<Node> nextNodeList);

    /**
     * 获取过滤策略
     *
     * @param strategyName
     * @return
     */
    public static FlowNextNodeFilter getInstance(String strategyName) {
        switch (strategyName.toUpperCase()) {
            case FlowConstant.FLOW_SIGN_FW:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_FW);
            case FlowConstant.FLOW_SIGN_GD:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_GD);
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_FGLD_FB);
            case FlowConstant.FLOW_SIGN_QRFW:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_QRFW);
            case FlowConstant.FLOW_SIGN_BMLD_QRFW_XB:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_BMLD_QRFW_XB);
            case FlowConstant.FLOW_SIGN_BMLD_QRFW:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_BMLD_QRFW);
            case FlowConstant.FLOW_SIGN_FGLD_QRFW_XB:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_FGLD_QRFW_XB);
            case FlowConstant.FLOW_SIGN_FWBH:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SIGN_FWBH);
            //以下是档案借阅环节
            case FlowConstant.FLOW_ARC_SQ:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_ARC_SQ);
            case FlowConstant.FLOW_ARC_FGLD_SP:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_ARC_FGLD_SP);

            //以下是拟补充资料函环节
            case FlowConstant.FLOW_SPL_BZ_SP:
                return FlowNextNodeFilter.valueOf(FlowConstant.FLOW_SPL_BZ_SP);
            //以下是课题环节
            case FlowConstant.TOPIC_ZRSH_JH:
                return FlowNextNodeFilter.valueOf(FlowConstant.TOPIC_ZRSH_JH);
            //以下是通知公告环节
            case FlowConstant.ANNOUNT_TZ:
                return FlowNextNodeFilter.valueOf(FlowConstant.ANNOUNT_TZ);
            //以下是月报环节
            case FlowConstant.MONTH_YB:
                return FlowNextNodeFilter.valueOf(FlowConstant.MONTH_YB);

            default:
                return null;
        }

    }
}
