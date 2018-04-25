package cs.service.restService;

import cs.common.ResultMsg;
import cs.domain.project.DispatchDoc;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.model.project.CommentDto;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;

import java.util.Map;

/**
 * 项目接收数据接口
 * Created by ldm on 2017/12/20.
 */
public interface SignRestService {

    /**
     * 接收委里传输过来的数据
     * @param signDto
     * @param isGetFiles 是否接受附件
     * @return
     */
    ResultMsg pushProject(SignDto signDto,boolean isGetFiles);

    /**
     * 接收委里传输过来的预签收数据
     * @param signDto
     * @param isGetFiles 是否接受附件
     * @return
     */
    ResultMsg pushPreProject(SignDto signDto,boolean isGetFiles);


    /**
     * 回调项目信息给发改委
     * @param sign
     * @return
     */
    ResultMsg setToFGW(SignDto sign, WorkProgramDto mainWP, DispatchDocDto dispatchDoc, String fgwUrl, Map<String,CommentDto> commentDtoMap);

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
