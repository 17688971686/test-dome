package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.UnitScore;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Map;

/**
 * Description: 单位评分 业务操作接口
 * author: hjm
 * Date: 2018-3-27 15:26:51
 */
public interface UnitScoreService {

    /**
     * 判断是否有单位评分表的数据，没有就添加，
     * @param designcompanyName 编制单位名称
     * @param singid 业务id
     */
    void decide(String designcompanyName,String singid);


}
