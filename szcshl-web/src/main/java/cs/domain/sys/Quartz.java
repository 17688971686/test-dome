package cs.domain.sys;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 系统定时器配置管理
 */
@Entity
@Table(name = "cs_quartz")
@DynamicUpdate(true)
public class Quartz extends DomainBase {

    @Id
    @GeneratedValue(generator= "QuartzGenerator")
    @GenericGenerator(name= "QuartzGenerator",strategy = "uuid")
    private String id;

    /**
     * 定时器名称
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String quartzName;


    /**
     * 类名称（包含包名）
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String className;


    /**
     * 方法名称
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String methodName;


    /**
     * 执行表达式
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String cronExpression;

    /**
     * 当前状态（9：正在执行，0：停止执行）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String curState;

    /**
     * 是否在用(9:是，0：否)
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isEnable;

    /**
     * 表达式描述
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String descInfo;
    
    /**
     * 执行方式(9:自动，0：手动)
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String runWay;  //执行方式： 自动    或  手动 （默认为手动）
    
    public String getRunWay() {
		return runWay;
	}

	public void setRunWay(String runWay) {
		this.runWay = runWay;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuartzName() {
        return quartzName;
    }

    public void setQuartzName(String quartzName) {
        this.quartzName = quartzName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCurState() {
        return curState;
    }

    public void setCurState(String curState) {
        this.curState = curState;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getDescInfo() {
        return descInfo;
    }

    public void setDescInfo(String descInfo) {
        this.descInfo = descInfo;
    }
}
