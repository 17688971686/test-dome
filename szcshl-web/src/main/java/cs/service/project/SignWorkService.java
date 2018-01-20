package cs.service.project;

import cs.model.project.SignWorkDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by shenning on 2018/1/20.
 */
public interface SignWorkService {
    /**
     * 查询在办项目的工作方案专家抽取信息
     * @param odataObj
     * @return
     */
    List<SignWorkDto> fingSignWorkList(ODataObj odataObj);
}
