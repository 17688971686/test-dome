package cs.controller.party;

import com.ibm.icu.text.DecimalFormat;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.*;
import cs.domain.party.PartyManager;
import cs.domain.party.PartyManager_;
import cs.domain.project.SignDispaWork;
import cs.domain.sys.Dict;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.party.PartyManageRepo;
import cs.service.party.PartyManagerService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

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
    public void exportSignInSheet(HttpServletResponse resp){
        ServletOutputStream sos = null;
        InputStream is = null ;
        try{
            List<PartyManager> partyManagerList = partyManageRepo.findAll();
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

    @RequiresAuthentication
    @RequestMapping(name = "批量导入" , path = "importFile" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg importFile( HttpServletRequest req , @RequestParam(name = "file") MultipartFile file){
        ExcelReader er = null ;
        String returnMsg = "导入数据成功！<br/>";
        try{
            er  = new ExcelReader();

            List<PartyManager> partyManagerList = new ArrayList<>();
            int index = 2;
            List<Map<Integer , String >> list = er.readExcelContent(file.getInputStream() , 3 , 0);

            for(Map<Integer , String > map : list){
                if(map.get(0) == null || "".equals(map.get(0)) || map.get(4) == null || "".equals(map.get(4))){
                    returnMsg += "第" + index + "行：【党员名称、身份证号】不能为空<br/>";
                }else if(map.get(4) != null && partyManagerService.existByIdCar(map.get(4))){
                    returnMsg += "第" + index + "行：【" + map.get(1) + "、" +  map.get(4) + "】有冲突。<br/>";
                }else{
                    PartyManager partyManager = new PartyManager() ;
                    partyManager.setPmId(UUID.randomUUID().toString());
                    Date now =  new Date();
                    partyManager.setCreatedBy(SessionUtil.getDisplayName());
                    partyManager.setCreatedDate(now);
                    partyManager.setModifiedBy(SessionUtil.getDisplayName());
                    partyManager.setModifiedDate(now);
                    partyManager.setPmName(map.get(1));
                    partyManager.setPmSex(map.get(2) == null ? "" : map.get(2)); // 性别
                    partyManager.setPmNation(map.get(3) == null ? "" : map.get(3)); //民族
                    partyManager.setPmIDCard((map.get(4) == null || "".equals(map.get(4))) ? "" : new BigDecimal(map.get(4)).toPlainString()); // 身份证 (可能身份证号回是科学计数法（如：3.40256010353E11），所以需要转换)
                    partyManager.setPmBirthday( DateUtils.converToDate(map.get(5) , "yyyy-MM-dd")); // 出生日期
                    partyManager.setPmEducation(map.get(6) == null ? "" : map.get(6)); // 学历
                    if(map.get(7) != null || !"".equals(map.get(7))){
                        if("正式党员".equals(map.get(7))){
                            partyManager.setPmCategory("1"); // 人员类别
                        }else if("预备党员".equals(map.get(7))){
                            partyManager.setPmCategory("2"); // 人员类别
                        }
                    }
                    partyManager.setPmJoinPartyDate(DateUtils.converToDate(map.get(8) , "yyyy-MM-dd")); //入党日期
                    partyManager.setPmTurnToPatryDate(DateUtils.converToDate(map.get(9) , "yyyy-MM-dd")); //转正日期
                    partyManager.setIntroducer(map.get(10) == null ? "" : map.get(10)); // 入党介绍人
                    partyManager.setIsInOrg("是".equals(map.get(11)) ? "9" : "0"); // 组织关系是否在我委
                    partyManager.setJoinOrgDate(DateUtils.converToDate(map.get(12) , "yyyy-MM-dd")); //转入党组织日期
                    partyManager.setOutOrgDate(DateUtils.converToDate(map.get(13) , "yyyy-MM-dd")); // 转出党组织日期
                    partyManager.setPmPartyBranch(map.get(14) == null ? "" : map.get(14)); // 现所在党组织
                    partyManager.setJoinWorkDate(DateUtils.converToDate(map.get(15) , "yyyy-MM-dd")); // 参加工作日期
                    partyManager.setPmWorkPost(map.get(16) == null ? "" : map.get(16)); // 工作岗位
                    partyManager.setIsEnrolled("是".equals(map.get(17)) ? "9" : "0"); // 是否在编
                    partyManager.setPmPhone((map.get(18) == null || "".equals(map.get(18))) ? "" :  new BigDecimal(map.get(18)).toPlainString()); // 联系电话（手机号）
                    partyManager.setPmTel(map.get(19) == null ? "" : map.get(19)); // 固定电话
                    partyManager.setPmAddress(map.get(20) == null ? "" : map.get(20)); // 家庭住址
                    partyManagerService.saveParty(partyManager);
//                    partyManagerList.add(partyManager);
                }
                index ++ ;
            }
//            partyManagerService.batchSave(partyManagerList);
        }catch (Exception e){
            returnMsg = "数据导入失败";
            e.printStackTrace();
        }
        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "数据导入成功!" , returnMsg );
    }

    @RequiresAuthentication
    @RequestMapping(name = "导出党员信息表" , path = "exportPartyInfo" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void exportPartyInfo(HttpServletResponse resp){
        String fileName = "党员信息表";
        ExcelTools excelTools = new ExcelTools();
        try{
            String title = java.net.URLDecoder.decode(fileName,"UTF-8");
            ServletOutputStream sos = resp.getOutputStream();
            List<PartyManager> partyManagerList = partyManageRepo.findAll();
            List<PartyManagerDto> partyManagerDtoList = new ArrayList<>();
            if(partyManagerList != null && partyManagerList.size() > 0){
                for(PartyManager p : partyManagerList){
                    PartyManagerDto dto = new PartyManagerDto();
                    BeanCopierUtils.copyPropertiesIgnoreNull( p, dto);
                    dto.setPmCategory("1".equals(p.getPmCategory() )  ? "正式党员" : ("2".equals(p.getPmCategory() ) ? "预备党员" : "") );
                    dto.setIsEnrolled(p.getIsEnrolled() == "9" ? "是" : "否");
                    dto.setPmJoinPartyDateStr(DateUtils.toStringDay(p.getPmJoinPartyDate()));
                    dto.setPmTurnToPatryDateStr(DateUtils.toStringDay(p.getPmTurnToPatryDate()));
                    partyManagerDtoList.add(dto);
                }
            }

            String[] headerPair = new String[]{"姓名=pmName" , "性别=pmSex" , "身份证号=pmIDCard" , "民族=pmNation" , "学历=pmEducation" , "手机号=pmPhone" , "固定电话=pmTel" , "人员类别=pmCategory" , "入党日期=pmJoinPartyDateStr" , "转正日期=pmTurnToPatryDateStr" , "在编情况=isEnrolled"};
            HSSFWorkbook wb = excelTools.createExcelBook(title, headerPair, partyManagerDtoList, PartyManagerDto.class);
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