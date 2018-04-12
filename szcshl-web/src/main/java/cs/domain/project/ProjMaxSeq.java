package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ldm on 2018/4/11.
 * 获取项目相关的最大序号
 */
@Entity
@Table(name = "cs_project_seq")
@DynamicUpdate(true)
public class ProjMaxSeq {
    /**
     * id
     */
    @Id
    private String id;
    /**
     * 年份
     */
    @Column(columnDefinition = "INTEGER")
    private int year;
    /**
     * 类型（1：表示评审登记评估类，2：表示评估登记概算类，3：表示发文除了设备清单，4表示发文的设备清单，5表示补充资料函）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String type;
    /**
     * 当前最大序号
     */
    @Column(columnDefinition = "INTEGER")
    private int seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
