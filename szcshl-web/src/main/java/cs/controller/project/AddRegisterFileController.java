package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.project.AddRegisterFileService;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description: 登记补充资料 控制层 author: ldm Date: 2017-8-3 15:26:51
 */
@Controller
@RequestMapping(name = "登记补充资料", path = "addRegisterFile")
@IgnoreAnnotation
public class AddRegisterFileController {

    String ctrlName = "addRegisterFile";
    @Autowired
    private AddRegisterFileService addRegisterFileService;

    @Autowired
    private SignRepo signRepo;

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AddRegisterFileDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddRegisterFileDto> addRegisterFileDtos = addRegisterFileService.get(odataObj);
        if (!Validate.isObject(addRegisterFileDtos)) {
            addRegisterFileDtos = new PageModelDto<>();
        }
        return addRegisterFileDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#initFindByOData#get")
    @RequestMapping(name = "初始化登记表数据", path = "initFindByOData", method = RequestMethod.GET)
    @ResponseBody
    public List<AddRegisterFileDto> initFindByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        List<AddRegisterFileDto> addRegisterFileList = addRegisterFileService.initRegisterFileData(odataObj);
        if (!Validate.isList(addRegisterFileList)) {
            addRegisterFileList = new ArrayList<>();
        }
        return addRegisterFileList;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#save#post")
    @RequestMapping(name = "创建记录", path = "save", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg create(@RequestBody AddRegisterFileDto[] addRegisterFileDtos) {
        ResultMsg resultMsg = addRegisterFileService.bathSave(addRegisterFileDtos);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#initprintdata#post")
    @RequestMapping(name = "初始化打印資料頁面", path = "initprintdata", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> initpint(@RequestParam String signid) {
        Map<String, Object> map = addRegisterFileService.initprint(signid);
        if (!Validate.isMap(map)) {
            map = new HashMap<>();
        }
        return map;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#findByBusinessId#post")
    @RequestMapping(name = "根据业务ID查询补充资料信息", path = "findByBusinessId", method = RequestMethod.POST)
    @ResponseBody
    public List<AddRegisterFileDto> findByBusinessId(@RequestParam String businessId) {
        List<AddRegisterFileDto> registerFileDtoList = addRegisterFileService.findByBusinessId(businessId);
        if (!Validate.isList(registerFileDtoList)) {
            registerFileDtoList = new ArrayList<>();
        }
        return registerFileDtoList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    @ResponseBody
    public AddRegisterFileDto findById(@RequestParam String id) {
        AddRegisterFileDto addRegisterFileDto = addRegisterFileService.findById(id);
        if (!Validate.isObject(addRegisterFileDto)) {
            addRegisterFileDto = new AddRegisterFileDto();
        }
        return addRegisterFileDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#deleteFile#delete")
    @RequestMapping(name = "删除记录", path = "deleteFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String ids) {
        addRegisterFileService.deleteRegisterFile(ids);
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#findbySuppdate#post")
    @RequestMapping(name = "根据日期查找资料", path = "findbySuppdate", method = RequestMethod.POST)
    @ResponseBody
    public List<AddRegisterFileDto> findbySuppdate(@RequestParam String suppDate) {
        List<AddRegisterFileDto> list = addRegisterFileService.findbySuppdate(suppDate);
        if (!Validate.isList(list)) {
            list = new ArrayList<>();
        }
        return list;
    }

    @RequiresAuthentication
    @RequestMapping(name = "打印补充资料列表", path = "printAddRegisterFile/{businessId}/{ids}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void printAddRegisterFile(@PathVariable String businessId, @PathVariable String ids, HttpServletResponse response) {
        InputStream inputStream = null;
        File file = null;
        File printFile = null;

        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            String filePath = path.substring(0, path.lastIndexOf(".")) + Constant.Template.PDF_SUFFIX.getKey();
            String fileName = "";

            Sign signss = signRepo.findById(Sign_.signid.getName(), businessId);
            List<AddRegisterFile> addRegisterFileList2 = addRegisterFileService.findByIdAndBusType(ids, 3);
            Map<String, Object> addFileData = new HashMap<>();
            addFileData.put("addFileList", addRegisterFileList2);
            addFileData.put("signNum", signss.getSignNum());
            addFileData.put("projectname", signss.getProjectname());
            addFileData.put("builtcompanyName", signss.getBuiltcompanyName());
            addFileData.put("projectcode", signss.getProjectcode());
            addFileData.put("strDate", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
            file = TemplateUtil.createDoc(addFileData, Constant.Template.ADD_REGISTER_FILE.getKey(), path);

            if (file != null) {
                OfficeConverterUtil.convert2PDF(file.getAbsolutePath(), filePath);
            }

            printFile = new File(filePath);
            inputStream = new BufferedInputStream(new FileInputStream(printFile));

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);  //读取文件流
            inputStream.close();

            response.reset();  //重置结果集
            response.setContentType("application/pdf");
            response.addHeader("Content-Length", "" + printFile.length());  //返回头 文件大小
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            //获取返回体输出权
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            os.write(buffer); // 输出文件
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (file != null) {
                    Tools.deleteFile(file);
                }
                if (printFile != null) {
                    Tools.deleteFile(printFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // begin#html
    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#html/list#get")
    @RequestMapping(name = "列表页面", path = "list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#addRegisterFile/edit#get")
    @RequestMapping(name = "编辑页面", path = "edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
    // end#html

}