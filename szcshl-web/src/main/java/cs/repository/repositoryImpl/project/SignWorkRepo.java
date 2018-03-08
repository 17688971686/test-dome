package cs.repository.repositoryImpl.project;

import cs.domain.project.SignWork;
import cs.model.project.SignWorkDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by shenning on 2018/1/20.
 */
public interface SignWorkRepo extends IRepository<SignWork, String> {
    /**
     * 查询在办项目的工作方案专家抽取信息
     * @param odataObj
     * @return
     */
    List<SignWork> fingSignWorkList(ODataObj odataObj);
}
