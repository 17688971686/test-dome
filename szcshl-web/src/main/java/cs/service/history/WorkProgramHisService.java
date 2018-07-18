package cs.service.history;

import cs.domain.project.WorkProgram;

public interface WorkProgramHisService{

    /**
     * 工作方案留痕
     * @param workProgram
     * @return
     */
    boolean copyWorkProgram(WorkProgram workProgram,String signId) throws RuntimeException;

}
