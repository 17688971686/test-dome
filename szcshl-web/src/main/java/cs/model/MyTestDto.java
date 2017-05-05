package cs.model;

import cs.domain.MyTest;

import java.util.Date;

/**
 * Description: 页面数据模型
 * User: Administrator
 * Date: 2017/5/4 17:54
 */
public class MyTestDto extends BaseDto2<MyTest> {

    private String testId;
    private String testName;
    private Date createTime;
    private Date updateTime;

    public MyTestDto() {
    }

    public MyTestDto(MyTest source) {
        super(source);
    }

    @Override
    protected Class<MyTest> getCls() {
        return MyTest.class;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}