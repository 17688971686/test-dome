package cs.service.demo;

import cs.common.service.IService;
import cs.domain.demo.MyTest;
import cs.model.PageModelDto;
import cs.model.demo.MyTestDto;
import cs.repository.odata.ODataObj;

/**
 * Description: my test 业务操作接口
 * User: Administrator
 * Date: 2017/5/4 18:21
 */
public interface MyTestService extends IService<MyTest, MyTestDto> {

    /**
     * 获取数据
     * @param odataObj
     * @return
     */
    PageModelDto<MyTestDto> getDto(ODataObj odataObj);

}
