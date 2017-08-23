package cs.common.utils;

/**
 * 项目工具类
 * Created by ldm on 2017/8/22.
 */
public class ProjectUtils {

    /**
     * 根据项目阶段，获取归档编号的类型（评估类、资金申请报告、其他类：PD，概算类：GD，设备类：SD）
     * @param signStage
     * @return
     */
    public static String getFileRecordTypeByStage(String signStage){
        if(Validate.isString(signStage)){
            if(signStage.indexOf("概算") > -1){
                return "GD";
            }
            if(signStage.indexOf("设备") > -1){
                return "SD";
            }

            return "PD";
        }else{
            return "";
        }

    }
}
