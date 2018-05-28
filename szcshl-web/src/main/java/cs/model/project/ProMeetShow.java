package cs.model.project;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 用于首页展示前日起往后的5个工作日的调研和会议统计信息
 *
 * @author zsl
 */
public class ProMeetShow {
    private String proName1; //项目名称
    private String proName2;
    private String proName3;
    private String proName4;
    private String proName5;

    public String getProName1() {
        return proName1;
    }

    public void setProName1(String proName1) {
        this.proName1 = proName1;
    }

    public String getProName2() {
        return proName2;
    }

    public void setProName2(String proName2) {
        this.proName2 = proName2;
    }

    public String getProName3() {
        return proName3;
    }

    public void setProName3(String proName3) {
        this.proName3 = proName3;
    }

    public String getProName4() {
        return proName4;
    }

    public void setProName4(String proName4) {
        this.proName4 = proName4;
    }

    public String getProName5() {
        return proName5;
    }

    public void setProName5(String proName5) {
        this.proName5 = proName5;
    }

}
