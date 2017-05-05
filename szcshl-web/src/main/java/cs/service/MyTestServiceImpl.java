package cs.service;

import cs.common.service.AbstractServiceImpl;
import cs.domain.MyTest;
import cs.model.MyTestDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.MyTestRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: my test 业务操作实现类
 * User: Administrator
 * Date: 2017/5/4 18:22
 */
@Service
public class MyTestServiceImpl extends AbstractServiceImpl<MyTest, MyTestDto, MyTestRepo> implements MyTestService {

    @Override
    public PageModelDto<MyTestDto> getDto(ODataObj odataObj) {
        PageModelDto<MyTestDto> page = new PageModelDto<MyTestDto>();
        List<MyTest> dmList = baseRepo.findByOdata(odataObj);
        List<MyTestDto> tdoList = new ArrayList<MyTestDto>(dmList.size());
        MyTestDto target = null;
        for (MyTest d : dmList) {
            target = new MyTestDto(d);
            tdoList.add(target);
        }
        page.setValue(tdoList);
        page.setCount(odataObj.getCount());

        logger.info("查询用户数据");
        return page;
    }
}