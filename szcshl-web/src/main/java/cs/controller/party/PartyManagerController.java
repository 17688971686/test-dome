package cs.controller.party;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.ResponseUtils;
import cs.common.utils.SysFileUtil;
import cs.common.utils.TemplateUtil;
import cs.common.utils.Tools;
import cs.domain.party.PartyManager;
import cs.domain.party.PartyManager_;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.party.PartyManageRepo;
import cs.service.party.PartyManagerService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 党务管理控制层
 * Author: mcl
 * Date: 2018/3/8 11:12
 */
@Controller
@RequestMapping(name = "党务管理" , path = "partyManager")
@MudoleAnnotation(name = "党务管理",value = "permission#partyManager")
public class PartyManagerController {

    private String ctrlName = "party";

    @Autowired
    private PartyManagerService partyManagerService;

    @Autowired
    private PartyManageRepo partyManageRepo;

    @RequiresAuthentication
    @RequestMapping(name="查询党员信息列表" , path = "findByOData" , method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PartyManagerDto> findByOData(HttpServletRequest request){
        ODataObj oDataObj = null;
        try {
            oDataObj = new ODataObj(request);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PageModelDto<PartyManagerDto> pageModelDto = partyManagerService.findByOData(oDataObj);
        return pageModelDto;

    }

    @RequiresAuthentication
    @RequestMapping(name="通过ID查询党员信息" , path = "findById" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findById(@RequestParam String pmId){
        return partyManagerService.findPartyById(pmId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存党员信息" , path = "createParty" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg createParty(@RequestBody PartyManagerDto partyManagerDto){
        return partyManagerService.createParty(partyManagerDto);
    }

    @RequiresAuthentication
    @RequestMapping(name="更新党员信息" , path = "updateParty" , method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg updateParty(@RequestBody PartyManagerDto partyManagerDto){
        return partyManagerService.updateParty(partyManagerDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "导出签到表" , path = "exportSignInSheet" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportSignInSheet(HttpServletResponse resp, @RequestParam String pmIds){
        ServletOutputStream sos = null;
        InputStream is = null ;
        try{
        List<PartyManager> partyManagerList = partyManageRepo.findByIds(PartyManager_.pmId.getName() , pmIds , null);
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("partyList" , partyManagerList);
        dataMap.put("listSize" , partyManagerList == null ? 0 : partyManagerList.size() );
        String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
         File file = TemplateUtil.createDoc(dataMap , Constant.Template.SIGN_IN_SHEET.getKey() , path);
         String fileName = "签到表.doc" ;
         ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey() , resp);
        resp.setHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        int bytesum = 0 , byteread = 0 ;
        is = new FileInputStream(file);
        sos = resp.getOutputStream();
        byte[] buffer = new byte[1024];
            while ( (byteread = is.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                sos.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (sos != null) {
                        sos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }

    @RequiresAuthentication
    @RequestMapping(name = "党务信息表导出-word模板" , path = "exportPartyWord" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportPartyWord(@RequestParam String pmId , HttpServletResponse response){

        ServletOutputStream ouputStream= null;
        InputStream inStream = null;
        try{
            PartyManagerDto partyManagerDto = (PartyManagerDto) partyManagerService.findPartyById(pmId).getReObj();
            Map<String, Object> archivesData = TemplateUtil.entryAddMap(partyManagerDto);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(archivesData, Constant.Template.PARTYDETAIL.getKey(), path);
            String fileName = "党员基本信息采集表.doc";
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey(), response);
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            //获取相对路径
            int bytesum = 0;
            int byteread = 0;
            inStream = new FileInputStream(file); //读入原文件
            ouputStream = response.getOutputStream();
            byte[] buffer = new byte[1444];
            while ( (byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                ouputStream.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ouputStream != null) {
                    ouputStream.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除党员信息" , path= "deleteParty" , method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteParty(@RequestParam String pmId){
        partyManagerService.deleteParty(pmId);
    }

    @RequiresPermissions("partyManager#html/partyList#get")
    @RequestMapping(name = "党员信息录入页" , path = "html/partyEdit" , method = RequestMethod.GET)
    public String edit(){
        return ctrlName + "/partyEdit";
    }

    @RequiresPermissions("partyManager#html/partyList#get")
    @RequestMapping(name = "党员信息列表" , path = "html/partyList" , method = RequestMethod.GET)
    public String partyList(){
        return ctrlName + "/partyList";
    }

}