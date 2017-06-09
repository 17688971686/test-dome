package cs.model.project;

import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnitUser;
import cs.model.BaseDto;
import cs.model.BaseDto2;
    
    
    
    
    


/**
 * Description: 协审单位用户 页面数据模型
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
public class AssistUnitUserDto extends BaseDto {

    private String id;
    private String userName;
    private String phoneNum;
    private String position;
    private AssistUnit assistUnit;

    public AssistUnitUserDto() {
    }
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public AssistUnit getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(AssistUnit assistUnit) {
        this.assistUnit = assistUnit;
    }

}