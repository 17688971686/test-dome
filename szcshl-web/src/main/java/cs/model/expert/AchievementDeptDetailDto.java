package cs.model.expert;

/**
 * 部门业绩明细
 *
 * @author zsl
 */
public class AchievementDeptDetailDto {
    private String userId;
    //姓名
    private String name;
    //发文数
    private Integer mainDisSum;
    //协办发文数
    private Integer assistDisSum;
    //主办人项目数
    private Integer mainProCount;
    //协办人项目数
    private Integer assistProCount;
    //部门id
    private String deptIds;

    //部门deptNames
    private String deptNames;


    String year;

    String quarter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMainDisSum() {
        return mainDisSum;
    }

    public void setMainDisSum(Integer mainDisSum) {
        this.mainDisSum = mainDisSum;
    }

    public Integer getAssistDisSum() {
        return assistDisSum;
    }

    public void setAssistDisSum(Integer assistDisSum) {
        this.assistDisSum = assistDisSum;
    }

    public Integer getMainProCount() {
        return mainProCount;
    }

    public void setMainProCount(Integer mainProCount) {
        this.mainProCount = mainProCount;
    }

    public Integer getAssistProCount() {
        return assistProCount;
    }

    public void setAssistProCount(Integer assistProCount) {
        this.assistProCount = assistProCount;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }
}
