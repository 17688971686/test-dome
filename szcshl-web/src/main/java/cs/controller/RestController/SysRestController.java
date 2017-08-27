package cs.controller.RestController;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.model.project.SignDto;
import cs.service.project.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统接口controller
 * Created by ldm on 2017/8/25.
 */
@RestController
@RequestMapping("/intfc")
public class SysRestController {

    @Autowired
    private SignService signService;

    /**
     * 推送项目
     * @param signDto
     * @return
     */
    @RequestMapping(value = "/pushProject", method = RequestMethod.POST)
    public ResultMsg pushProject(@RequestBody SignDto signDto) {
        return signService.pushProject(signDto);
    }


}
