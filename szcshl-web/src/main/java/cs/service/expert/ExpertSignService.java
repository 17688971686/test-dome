package cs.service.expert;

import cs.domain.expert.ExpertSign;
import cs.domain.project.SignDispaWork;
import cs.model.expert.ExpertSignDto;

import java.util.List;

/**
 * Created by shenning on 2018/1/19.
 */
public interface ExpertSignService {

    /**
     * 通过专家id 获取专家评审过的项目信息
     * @param expertId
     * @return
     */
    List<ExpertSignDto> reviewProject(String expertId);

}
