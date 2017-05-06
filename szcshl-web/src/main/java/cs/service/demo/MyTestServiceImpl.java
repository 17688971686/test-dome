package cs.service.demo;

import cs.common.service.ServiceImpl;
import cs.domain.demo.MyTest;
import cs.model.PageModelDto;
import cs.model.demo.MyTestDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.demo.MyTestRepo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: my test 业务操作实现类
 * User: Administrator
 * Date: 2017/5/4 18:22
 */
@Service
public class MyTestServiceImpl extends ServiceImpl<MyTest, MyTestDto, MyTestRepo> implements MyTestService {

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