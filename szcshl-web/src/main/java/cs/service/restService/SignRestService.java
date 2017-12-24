package cs.service.restService;

import cs.common.ResultMsg;
import cs.domain.project.Sign;
import cs.model.project.SignDto;

/**
 * 项目接收数据接口
 * Created by ldm on 2017/12/20.
 */
public interface SignRestService {

    /**
     * 接收委里传输过来的数据
     * @param signDto
     * @return
     */
    ResultMsg pushProject(SignDto signDto);

    /**
     * 回调项目信息给发改委
     * @param sign
     * @return
     */
    ResultMsg setToFGW(Sign sign,String url,String loaclUrl);
}
