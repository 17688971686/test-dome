package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.ExcelTools;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignDispaWorkService;
import cs.service.sys.HeaderService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目详情信息视图控制器
 * Created by ldm on 2017/9/17 0017.
 */
@Controller
@RequestMapping(name = "收文", path = "signView")
@MudoleAnnotation(name = "项目管理",value = "permission#sign")
public class SignDispaWorkController {

    @Autowired
    private SignDispaWorkService signDispaWorkService;

    @Autowired
    private HeaderService headerService;


    //@RequiresPermissions("signView#getSignList#post")
    @RequiresAuthentication
    @RequestMapping(name = "项目查询统计", path = "getSignList", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<SignDispaWork> getSignList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = signDispaWorkService.getCommQurySign(odataObj);
        return pageModelDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#unMergeWPSign#post")
    @RequestMapping(name = "待选合并评审项目", path = "unMergeWPSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeWPSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.unMergeWPSign(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#getMergeSignBySignId#post")
    @RequestMapping(name = "获取已选合并评审项目", path = "getMergeSignBySignId", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDispaWork> getMergeSignBySignId(@RequestParam(required = true) String signId) {
        return signDispaWorkService.getMergeWPSignBySignId(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#unMergeDISSign#post")
    @RequestMapping(name = "待选合并发文项目", path = "unMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeDISSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.unMergeDISSign(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#getMergeDISSign#post")
    @RequestMapping(name = "获取已选合并发文项目", path = "getMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> getMergeDISSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.getMergeDISSignBySignId(signId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存合并项目", path = "mergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg mergeSign(@RequestParam(required = true) String signId,
                               @RequestParam(required = true) String mergeIds, @RequestParam(required = true) String mergeType) {
        return signDispaWorkService.mergeSign(signId, mergeIds, mergeType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#cancelMergeSign#post")
    @RequestMapping(name = "解除合并评审发文", path = "cancelMergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg cancelMergeSign(@RequestParam(required = true) String signId, String cancelIds,
                                     @RequestParam(required = true) String mergeType) {
        return signDispaWorkService.cancelMergeSign(signId, cancelIds, mergeType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#deleteAllMerge#post")
    @RequestMapping(name = "删除所有合并项目", path = "deleteAllMerge", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteAllMerge(@RequestParam(required = true) String signId, @RequestParam(required = true) String mergeType) {
        return signDispaWorkService.deleteAllMerge(signId, mergeType);
    }

    @RequiresAuthentication
    @RequestMapping(name = "项目统计导出", path = "excelExport", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void excelExport(HttpServletResponse resp, @RequestParam String filterData, @RequestParam String fileName) {

        ExcelTools excelTools = new ExcelTools();
//        List<SignDispaWork> signDispaWorkList = new ArrayList<>();

//        for (SignDispaWork sdw : signDispaWorks) {
//            signDispaWorkList.add(sdw);
//        }
        try {
            String title = java.net.URLDecoder.decode(fileName,"UTF-8");
            String filters = java.net.URLDecoder.decode(filterData,"UTF-8");
            ServletOutputStream sos = resp.getOutputStream();
            List<HeaderDto> headerDtoList = headerService.findHeaderListSelected("项目类型");
            List<SignDispaWork> signDispaWorkList = signDispaWorkService.getSignDispaWork(filters);
            String[] headerPair = new String[headerDtoList.size()];
            for (int i = 0; i < headerDtoList.size(); i++) {
                headerPair[i] = headerDtoList.get(i).getHeaderName() + "=" + headerDtoList.get(i).getHeaderKey();
            }
            HSSFWorkbook wb = excelTools.createExcelBook(title, headerPair, signDispaWorkList, SignDispaWork.class);

            resp.setContentType("application/vnd.ms-excel;charset=GBK");
            resp.setHeader("Content-type", "application/x-msexcel");
            resp.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((title + ".xls").getBytes("GB2312"), "ISO-8859-1");

            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
            wb.write(sos);
            sos.flush();
            sos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresPermissions("signView#html/signList#get")
    @RequestMapping(name = "项目查询统计", path = "html/signList", method = RequestMethod.GET)
    public String signList() {
        return "sign/signList";
    }
}
