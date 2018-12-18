package cs.common.utils;


/**
 * Description: ExcelJxlsUtls
 *
 * @author: zsl
 * @date: 2018/5/30 15:38
 */
public class ExcelJxlsUtls {


    /**
     * 获取协审标志
     * @param isAssist
     * @return
     */
    public static String getAssist(String isAssist) {
        String assistFlag = "";
        if(StringUtil.isBlank( isAssist) || "0".equals(isAssist)){
                assistFlag = "否";

        }else{
            assistFlag = "是";
        }
        return assistFlag;
    }

}
