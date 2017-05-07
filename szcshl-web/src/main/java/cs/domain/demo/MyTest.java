package cs.domain.demo;

import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: My test!
 * User: Administrator
 * Date: 2017/5/4 16:44
 */
@Entity
@Table(name = "MY_TEST")
public class MyTest extends DomainBase {

	@Id
    private String id;
    @Column(nullable = false, columnDefinition = "varchar(255) comment '测试名'")
    private String testName;
    @Column(columnDefinition = "varchar(255) comment '测试01'")
    private String test01;
    @Column(columnDefinition = "varchar(255) comment '测试02'")
    private String test02;

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