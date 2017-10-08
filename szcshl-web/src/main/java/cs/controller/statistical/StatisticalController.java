package cs.controller.statistical;

import cs.ahelper.IgnoreAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: 统计图表
 * Author: mcl
 * Date: 2017/9/8 16:53
 */
@Controller
@RequestMapping(name="统计图表" ,path="statistical")
@IgnoreAnnotation
public class StatisticalController {

    public String ctrlName ="statistical";


    @RequestMapping(name="图表" , path ="html/list" , method = RequestMethod.GET)
    public String list(){
        return ctrlName + "/list";
    }
}