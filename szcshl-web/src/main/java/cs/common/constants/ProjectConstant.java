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
     * 存档编号KEY值
     * 评估类、资金申请报告、其他类：PD，概算类：GD，设备类：SD
     */
    public static enum FILE_RECORD_KEY {
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

}
