package cs.domain.demo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: My test!
 * User: Administrator
 * Date: 2017/5/4 16:44
 */
@Entity
@Table(name = "MY_TEST")
public class MyTest implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    private String testId;
    @Column(nullable = false)
    private String testName;
    @Column( nullable = false, columnDefinition = "date")
    private Date createTime;
    @Column(columnDefinition = "date")
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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