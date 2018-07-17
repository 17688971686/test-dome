package cs.service.history;

import cs.domain.project.WorkProgram;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class WorkProgramHisServiceImpl implements WorkProgramHisService {
    private static Logger log = Logger.getLogger(WorkProgramHisServiceImpl.class);

    @Override
    public boolean copyWorkProgram(WorkProgram workProgram) {
        try{
            //1、工作方案留痕

            //2、会议预定留痕

            //3、专家抽取方案流程

            //4、选择的专家留痕

            //5、抽取条件留痕
            return true;
        }catch (Exception e){
            log.error("工作方案留痕异常："+e.getMessage());
        }
        return false;
    }
}
