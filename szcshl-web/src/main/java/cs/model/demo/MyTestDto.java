package cs.model.demo;

import cs.domain.demo.MyTest;
import cs.model.BaseDto2;

/**
 * Description: 页面数据模型
 * User: tzg
 * Date: 2017/5/4 17:54
 */
public class MyTestDto extends BaseDto2<MyTest> {

    private String id;
    private String testName;
    private String test01;
    private String test02;

    public MyTestDto() {
    }

    public MyTestDto(MyTest source) {
        super(source);
    }

    @Override
    protected Class<MyTest> getCls() {
        return MyTest.class;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTest01() {
        return test01;
    }

    public void setTest01(String test01) {
        this.test01 = test01;
    }

    public String getTest02() {
        return test02;
    }

    public void setTest02(String test02) {
        this.test02 = test02;
    }
}