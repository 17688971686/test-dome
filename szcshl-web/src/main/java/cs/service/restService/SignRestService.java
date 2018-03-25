package cs.service.restService;

import cs.common.ResultMsg;
import cs.domain.project.DispatchDoc;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;

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
     * 接收委里传输过来的预签收数据
     * @param signDto
     * @return
     */
    ResultMsg pushPreProject(SignDto signDto);


    /**
     * 回调项目信息给发改委
     * @param sign
     * @return
     */
    ResultMsg setToFGW(SignDto sign, WorkProgramDto mainWP, DispatchDocDto dispatchDoc, String fgwUrl);

    /**
     * 获取回传给委里的接口地址
     * @return
     */
    String getReturnUrl();

    /***
     * 获取委里预签收接口地址
     * @return
     */
    String getPreReturnUrl();
}
