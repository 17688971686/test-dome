package cs.service.postdoctor;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctoralBaseDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 博士后 业务操作接口
 * author: zsl
 * Date: 2018-10-23 15:04:55
 * @author Administrator
 */
public interface PostdoctoralBaseService {

    PageModelDto<PostdoctoralBaseDto> get(ODataObj odataObj);

    ResultMsg save(PostdoctoralBaseDto record);

    void update(PostdoctoralBaseDto record);


    ResultMsg delete(String id);

    PostdoctoralBaseDto findDetailById(String id);


}
