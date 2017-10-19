package cs.model.expert;

import java.util.List;

/**
 * 专家评审基本情况详细统计
 *
 * @author zsl
 */
public class ExpertReviewCondDto {
    private String expertID;//专家ID
    private String expertNo;//专家ID
    private String name;//专家姓名
    private String comPany;//工作单位
    private String beginTime;
    private  String endTime;
    private String reviewtype;
    List<ExpertReviewCondBusDto> expertReviewCondBusDtoList;
    public String getExpertID() {
        return expertID;
    }

    public void setExpertID(String expertID) {
        this.expertID = expertID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComPany() {
        return comPany;
    }

    public void setComPany(String comPany) {
        this.comPany = comPany;
    }

    public List<ExpertReviewCondBusDto> getExpertReviewCondBusDtoList() {
        return expertReviewCondBusDtoList;
    }

    public void setExpertReviewCondBusDtoList(List<ExpertReviewCondBusDto> expertReviewCondBusDtoList) {
        this.expertReviewCondBusDtoList = expertReviewCondBusDtoList;
    }

    public String getExpertNo() {
        return expertNo;
    }

    public void setExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReviewtype() {
        return reviewtype;
    }

    public void setReviewtype(String reviewtype) {
        this.reviewtype = reviewtype;
    }
}
