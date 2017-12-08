package cs.common;

/**
 * 接口返回字符Code信息
 * Created by ldm on 2017/12/8.
 */
public class IFResultCode {

    /**
     * 以下主要用于接口对接
     */
    public static enum IFMsgCode {
        SZEC_SAVE_OK("SAVE_OK","保存成功！"),
        SZEC_SAVE_ERROR("SAVE_ERROR","保存异常！"),
        SZEC_DEAL_ERROR("DEAL_ERROR","处理异常！"),
        //以下是收文阶段的返回码
        SZEC_SIGN_01("SIGN_01","对象为空！"),
        SZEC_SIGN_02("SIGN_02","收文编号为空！"),
        SZEC_SIGN_03("SIGN_03","评审阶段标识为空！"),
        SZEC_SIGN_04("SIGN_04","评审阶段标识不正确！"),
        //以下是课题研究的返回码
        SZEC_TOPIC_01("TOPIC_01","对象为空！"),
        SZEC_TOPIC_02("TOPIC_02","流程实例ID为空！"),
        SZEC_TOPIC_03("TOPIC_03","流程实例ID不正确！"),
        SZEC_TOPIC_04("TOPIC_04","评审阶段标识不正确！"),;

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
}
