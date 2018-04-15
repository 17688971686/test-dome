package cs.model.expert;

/**
 * Created by Administrator on 2018/3/18.
 */
public class ExpertNewTypeDto {

    private String id;

    private String expertType;//专家类别

    private String maJorBig;//突出专业（大类）

    private String maJorSmall;//突出专业（小类）


    private String  expertNewInfoId;

    private String conditionId;

    private String expertSelectedId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpertType() {
        return expertType;
    }

    public void setExpertType(String expertType) {
        this.expertType = expertType;
    }

    public String getMaJorBig() {
        return maJorBig;
    }

    public void setMaJorBig(String maJorBig) {
        this.maJorBig = maJorBig;
    }

    public String getMaJorSmall() {
        return maJorSmall;
    }

    public void setMaJorSmall(String maJorSmall) {
        this.maJorSmall = maJorSmall;
    }

    public String getExpertNewInfoId() {
        return expertNewInfoId;
    }

    public void setExpertNewInfoId(String expertNewInfoId) {
        this.expertNewInfoId = expertNewInfoId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getExpertSelectedId() {
        return expertSelectedId;
    }

    public void setExpertSelectedId(String expertSelectedId) {
        this.expertSelectedId = expertSelectedId;
    }
}
