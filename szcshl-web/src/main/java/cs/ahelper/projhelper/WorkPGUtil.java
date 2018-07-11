package cs.ahelper.projhelper;

import cs.common.constants.Constant;
import cs.domain.project.WorkProgram;

import java.util.Date;

/**
 * Created by ldm on 2018/5/6 0006.
 */
public class WorkPGUtil {
    private WorkProgram workProgram;

    protected WorkPGUtil(WorkProgram workProgram) {
        this.workProgram = workProgram;
    }

    public static WorkPGUtil create(WorkProgram workProgram) {
        return new WorkPGUtil(workProgram);
    }

    /**
     * 是否合并评审工作方案
     * @return
     */
    public boolean isMergeWP(){
        String mergeType = workProgram.getIsSigle();
        if(Constant.MergeType.REVIEW_MERGE.getValue().equals(mergeType)){
            return true;
        }
        return false;
    }

    /**
     * 是否合并评审主工作方案
     * @return
     */
    public boolean isMainWP(){
        String isMainWP = workProgram.getIsMainProject();
        if(Constant.EnumState.YES.getValue().equals(isMainWP)){
            return true;
        }
        return false;
    }

    /**
     * 清空部长审批意见
     * @return
     */
    public WorkPGUtil resetMinisterOption(){
        workProgram.setMinisterSuggesttion("");
        workProgram.setMinisterDate(null);
        workProgram.setMinisterName("");
        return this;
    }

    /**
     * 设置部长意见
     * @param option
     * @param date
     * @param signName
     * @return
     */
    public WorkPGUtil setMinisterOption(String option, Date date, String signName){
        workProgram.setMinisterSuggesttion(option);
        workProgram.setMinisterDate(date);
        workProgram.setMinisterName(signName);
        return this;
    }

    /**
     * 清空分管领导审批意见
     * @return
     */
    public WorkPGUtil resetLeaderOption(){
        workProgram.setLeaderSuggesttion("");
        workProgram.setLetterDate(null);
        workProgram.setLeaderName("");
        return this;
    }

    /**
     * 设置分管主任意见
     * @return
     */
    public WorkPGUtil setLeaderOption(String option, Date date, String signName){
        workProgram.setLeaderSuggesttion(option);
        workProgram.setLetterDate(date);
        workProgram.setLeaderName(signName);
        return this;
    }

    public WorkProgram getWorkProgram() {
        return workProgram;
    }

    public void setWorkProgram(WorkProgram workProgram) {
        this.workProgram = workProgram;
    }
}
