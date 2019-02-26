package cs.common.constants;

/**
 * @author: ldm
 * @date: 2019/2/25 0025 下午 16:25
 * @description:
 */
public class ProjectConstant {
    /**
     * 默认收文工作日
     */
    public static Float WORK_DAY_15 = 15f;

    /**
     * 默认工作日
     */
    public static Float WORK_DAY_12 = 12f;

    /**
     * 25
     */
    public static Float WORK_DAY_25 = 25f;

    /**
     * 归档工作日
     */
    public static Float WORK_DAY_30 = 30f;
    /**
     * 3
     */
    public static Float WORK_DAY_3 = 3f;

    /**
     * 自定义项目评审阶段定义（9个阶段）
     */
    public enum REVIEW_STATE_ENUM{
        /**
         * 登记赋码
         */
        REGISTERCODE("REGISTERCODE","登记赋码"),
        /**
         * 项目建议书
         */
        STAGESUG("STAGESUG","项目建议书"),
        /**
         * 可行性研究报告
         */
        STAGESTUDY("STAGESTUDY","可行性研究报告"),
        /**
         * 项目概算
         */
        STAGEBUDGET("STAGEBUDGET","项目概算"),
        /**
         * 项目概算-协审
         */
        STAGEBUDGET_XS("STAGEBUDGET_XS","项目概算-协审"),
        /**
         * 资金申请报告
         */
        STAGEREPORT("STAGEREPORT","资金申请报告"),
        /**
         * 设备清单（国产）
         */
        STAGEHOMELAND("STAGEHOMELAND","设备清单（国产）"),
        /**
         * 设备清单（进口）
         */
        STAGEIMPORT("STAGEIMPORT","设备清单（进口）"),
        /**
         * 进口设备
         */
        STAGEDEVICE("STAGEDEVICE","进口设备"),
        /**
         * 其它
         */
        STAGEOTHER("STAGEOTHER","其它");

        /**
         * 英文编码
         */
        private String enCode;
        /**
         * 中文编码
         */
        private String zhCode;

        REVIEW_STATE_ENUM(String enCode,String zhCode){
            this.enCode = enCode;
            this.zhCode = zhCode;
        }

        public String getEnCode() {
            return enCode;
        }

        public void setEnCode(String enCode) {
            this.enCode = enCode;
        }

        public String getZhCode() {
            return zhCode;
        }

        public void setZhCode(String zhCode) {
            this.zhCode = zhCode;
        }

        /**
         * 根据英文编码获取评审阶段信息
         * @param enCode
         * @return
         */
        public static REVIEW_STATE_ENUM getByEnCode(String enCode){
            for(REVIEW_STATE_ENUM reviewStateEnum : values()){
                if (reviewStateEnum.getEnCode() == enCode) {
                    return reviewStateEnum;
                }
            }
            return null;
        }

        /**
         * 根据中文编码获取评审阶段信息
         * @param zhCode
         * @return
         */
        public static REVIEW_STATE_ENUM getByZhCode(String zhCode){
            for(REVIEW_STATE_ENUM reviewStateEnum : values()){
                if (reviewStateEnum.getZhCode() == zhCode) {
                    return reviewStateEnum;
                }
            }
            return null;
        }
    }

    /**
     * 警示灯状态
     *
     * @author MCL
     * @date 2017年6月29日 下午12:03:19
     */
    public enum CAUTION_LIGHT_ENUM{
        /**
         * 不显示
         */
        NO_LIGHT("0","不显示"),
        /**
         * 在办
         */
        PROCESS("1","在办"),
        /**
         * 已发文dispatch
         */
        DISPATCHED("2","已发文"),
        /**
         * 已发送存档
         */
        ARCHIVE("3","已发送存档"),
        /**
         * 暂停
         */
        PAUSE("4","暂停"),
        /**
         * 少于三个工作日
         */
        UNDER3_WORKDAY("5","少于三个工作日"),
        /**
         * 发文超期
         */
        DISPATCH_OVER("6","发文超期"),
        /**
         * 超过25个工作日未存档
         */
        OVER25_WORKDAY_ARCHIVE("7","超过25个工作日未存档"),
        /**
         * 存档超期
         */
        ARCHIVE_OVER("8","存档超期");

        private String codeValue;
        private String caution;

        CAUTION_LIGHT_ENUM(String codeValue,String caution) {
            this.codeValue = codeValue;
            this.caution = caution;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public void setCaution(String caution) {
            this.caution = caution;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public String getCaution() {
            return caution;
        }

        /**
         * 根据状态码获取亮灯状态信息
         * @param codeValue
         * @return
         */
        public static CAUTION_LIGHT_ENUM getByCode(String codeValue){
            for(CAUTION_LIGHT_ENUM cautionLightEnum : values()){
                if (cautionLightEnum.getCodeValue().equals(codeValue)) {
                    return cautionLightEnum;
                }
            }
            return null;
        }
    }

    /**
     * 项目业务类型
     *
     * @author ldm
     */
    public enum BUSINESS_TYPE {
        /**
         * 表示项目概算
         */
        GX,
        /**
         * 表示项目评估
         */
        PX,
        /**
         * 表示项目签收
         */
        SIGN,
        /**
         * 表示课题研究
         */
        TOPIC,
        /**
         * 项目签收工作方案
         */
        SIGN_WP,
        /**
         * 课题研究工作方案
         */
        TOPIC_WP;
    }

    /**
     * 存档编号KEY值
     * 评估类、资金申请报告、其他类：PD，概算类：GD，设备类：SD
     */
    public enum FILE_RECORD_KEY {
        /**
         * 课题编号
         */
        KT,
        /**
         * 课题归档编号
         */
        KD,
        /**
         * 月报简报
         */
        YD,
        /**
         * 评估类，资金申请报告，其它类
         */
        PD,
        /**
         * 概算类
         */
        GD,
        /**
         * 设备类
         */
        SD;
    }

    /**
     * 项目类型小类
     */
    public enum PROJECT_TYPE_ENUM{
        市政工程,
        房建工程,
        信息工程,
        设备采购,
        其他;
    }
}
