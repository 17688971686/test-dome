package cs.service.postdoctor;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.postdoctor.PostdoctoralStaffDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 博士后 业务操作接口
 * author: zsl
 * Date: 2018-10-23 15:04:55
 * @author Administrator
 */
public interface PostdoctoralStaffService {

    PageModelDto<PostdoctoralStaffDto> get(ODataObj odataObj);

    ResultMsg save(PostdoctoralStaffDto record);

    void update(PostdoctoralStaffDto record);


    ResultMsg delete(String id);

    PostdoctoralStaffDto findDetailById(String id);

    ResultMsg approvePostdoctoralStaff(String id , String status);

    ResultMsg backPostdoctoralStaff(String id , String status);


}
