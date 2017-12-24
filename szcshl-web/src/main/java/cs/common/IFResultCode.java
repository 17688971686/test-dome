package cs.common;

/**
 * 接口返回字符Code信息
 * Created by ldm on 2017/12/8.
 */
public class IFResultCode {
    public static final String FGW_PROJECT_IFS = "FGW_PROJECT_IFS";
    public static final String LOCAL_URL = "LOCAL_URL";
    /**
     * 以下主要用于接口对接
     */
    public static enum IFMsgCode {
        SZEC_SAVE_OK("SAVE_OK","保存成功！"),
        SZEC_SAVE_ERROR("SAVE_ERROR","保存异常！"),
        SZEC_DEAL_ERROR("DEAL_ERROR","处理异常！"),
        SZEC_SEND_OK("SEND_OK","发送成功！"),
        SZEC_SEND_ERROR("SEND_ERROR","发送失败！"),
        //以下是收文阶段的返回码
        SZEC_SIGN_01("SIGN_01","对象为空！"),
        SZEC_SIGN_02("SIGN_02","收文编号为空！"),
        SZEC_SIGN_03("SIGN_03","评审阶段标识为空！"),
        SZEC_SIGN_04("SIGN_04","评审阶段标识不正确！"),
        SZEC_SIGN_05("SIGN_05","项目数据保存成功，但是附件保存有错误！"),
        //以下是课题研究的返回码
        SZEC_TOPIC_01("TOPIC_01","对象为空！"),
        SZEC_TOPIC_02("TOPIC_02","流程实例ID为空！"),
        SZEC_TOPIC_03("TOPIC_03","流程实例ID不正确！"),
        SZEC_TOPIC_04("TOPIC_04","评审阶段标识不正确！"),
        //以下是项目信息回调吗
        SZEC_SFGW_01("SFGW_01","回调url为空！"),
        SZEC_SFGW_02("SFGW_02","项目编号为空！"),
        SZEC_SFGW_03("SFGW_03","发文信息为空！"),
        SZEC_SFGW_04("SFGW_04","项目编号为空！"),
        SZEC_SFGW_05("SFGW_05","项目编号为空！");

        private String code;
        private String value;

        IFMsgCode(String code,String value) {
            this.code = code;
            this.value = value;
        }
        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 评审方式接口对照码
     *
     */
    public static enum PSFS{
        PSFS_SELF("2","自评"),
        PSFS_MEETING("1","专家评审会"),
        PSFS_LEETER("3","专家函评");

        private String code;
        private String value;

        PSFS(String code,String value) {
            this.code = code;
            this.value = value;
        }
        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static String getCodeByValue(String value){
            PSFS[] enums = PSFS.values();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i].getValue().equals(value)) {
                    return enums[i].getCode();
                }
            }
            return null;
        }
    }

    /**
     * 评审过程办理环节接口对照码
     * 1：收文
     * 2：分办
     * 3：评审方案
     * 4：发文
     */
    public static enum PSGCBLHJ{
        SIGN_QS("1",FlowConstant.FLOW_SIGN_QS),
        SIGN_BMFB1("2",FlowConstant.FLOW_SIGN_BMFB1),
        SIGN_BMFB2("2",FlowConstant.FLOW_SIGN_BMFB2),
        SIGN_BMFB3("2",FlowConstant.FLOW_SIGN_BMFB3),
        SIGN_BMFB4("2",FlowConstant.FLOW_SIGN_BMFB4),
        SIGN_XMFZR1("3",FlowConstant.FLOW_SIGN_XMFZR1),
        SIGN_XMFZR2("3",FlowConstant.FLOW_SIGN_XMFZR2),
        SIGN_XMFZR3("3",FlowConstant.FLOW_SIGN_XMFZR3),
        SIGN_XMFZR4("3",FlowConstant.FLOW_SIGN_XMFZR4),
        SIGN_FW("4",FlowConstant.FLOW_SIGN_FW),
        SIGN_FWBH("4",FlowConstant.FLOW_SIGN_FWBH);

        private String code;
        private String value;

        PSGCBLHJ(String code,String value) {
            this.code = code;
            this.value = value;
        }
        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static String getCodeByValue(String value){
            PSGCBLHJ[] enums = PSGCBLHJ.values();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i].getValue().equals(value)) {
                    return enums[i].getCode();
                }
            }
            return null;
        }
    }

    /**
     * 委里接口返回码
     */
    public static enum RECODE{
        OK("1","上传成功"),
        ERROR("-1","***校验不通过");

        private String code;
        private String value;

        RECODE(String code,String value) {
            this.code = code;
            this.value = value;
        }
        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
