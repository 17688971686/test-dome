package cs.service.restService;

import cs.common.ResultMsg;
import cs.model.project.SignDto;

/**
 * 项目接收数据接口
 * Created by ldm on 2017/12/20.
 */
public interface SignRestService {

    ResultMsg pushProject(SignDto signDto);
}
