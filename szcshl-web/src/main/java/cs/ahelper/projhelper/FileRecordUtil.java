package cs.ahelper.projhelper;


import cs.common.constants.ProjectConstant;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;

/**
 * 项目归档工具类
 */
public class FileRecordUtil {

    protected FileRecordUtil() {

    }

    public static FileRecordUtil create() {
        return new FileRecordUtil();
    }

    /**
     * 针对新增归档而定
     * 根据评审阶段获取归档表单信息
     *
     * @param stage        评审阶段
     * @param isassistproc 是否协审
     * @return
     */
    public String getFormNumByStage(String stage, boolean isassistproc) {
        //默认表单为其它阶段
        String formNum = "QT";
        if (StringUtil.isBlank(stage)) {
            return formNum;
        }
        //根据阶段查询对应关联阶段项目
        ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByZhCode(stage.trim());
        if (Validate.isObject(reviewStateEnum)) {
            switch (reviewStateEnum) {
                /**
                 * 项目建议书
                 */
                case REGISTERCODE:
                    formNum = "QR-4.11-04-A1";
                    break;
                /**
                 * 项目建议书
                 */
                case STAGESUG:
                    formNum = "QR-4.3-11-A7";
                    break;
                /**
                 * 可研
                 */
                case STAGESTUDY:
                    formNum = "QR-4.4-09-A7";
                    break;
                /**
                 * 概算
                 */
                case STAGEBUDGET:
                    if (isassistproc) {
                        formNum = "QR-4.7-21-A5";
                    } else {
                        formNum = "QR-4.7-20-A7";
                    }
                    break;
                /**
                 * 资金申请报告
                 */
                case STAGEREPORT:
                    formNum = "QR-4.11-04-A1";
                    break;
                /**
                 * 设备清单（国产）
                 */
                case STAGEHOMELAND:
                    formNum = "QR-4.9-10-A0";
                    break;
                /**
                 * 设备清单（进口）
                 */
                case STAGEIMPORT:
                    formNum = "QR-4.9-10-A0";
                    break;
                /**
                 * 进口设备
                 */
                case STAGEDEVICE:
                    formNum = "QR-4.10-04-A0";
                    break;
                default:
                    ;
            }
        }
        return formNum;
    }
}
