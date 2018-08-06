package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.sys.FileLibraryDto;
import cs.model.sys.PolicyDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.FileLibraryService;
import cs.service.sys.PolicyService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Description: 文件库
 * Author: mcl
 * Date: 2017/8/21 14:53
 */
@Controller
@RequestMapping(name="文件库",path="fileLibrary")
@MudoleAnnotation(name = "公告资料管理",value = "permission#annountment")
public class FileLibraryController {

    private String ctrlName = "fileLibrary";

    @Autowired
    private FileLibraryService fileLibraryService;

    @Autowired
    private PolicyService policyService;

    //@RequiresPermissions("fileLibrary#initFileFolder#get")
    @RequiresAuthentication
    @RequestMapping(name="初始化-文件夹",path="initFileFolder",method = RequestMethod.GET)
    @ResponseBody
    public List<FileLibraryDto> initFileFolder(@RequestParam String fileType) throws ParseException {

        return fileLibraryService.initFolder(fileType);
    }

    //@RequiresPermissions("fileLibrary#initFileList#post")
    @RequiresAuthentication
    @RequestMapping(name="获取文件夹下所有文件",path="initFileList",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg initFileList(@RequestParam String fileId , @RequestParam String fileType)  {
        return fileLibraryService.initFileList( fileId , fileType);
    }

    //@RequiresPermissions("fileLibrary#findFileById#get")
    @RequiresAuthentication
    @RequestMapping(name="通过id获取文件",path ="findFileById" , method=RequestMethod.GET)
    @ResponseBody
    public FileLibraryDto findFileById(@RequestParam  String fileId){
        return fileLibraryService.findFileById(fileId);
    }


    //@RequiresPermissions("fileLibrary#addFileFolder#post")
    @RequiresAuthentication
    @RequestMapping(name="新建文件夹",path ="addFileFolder",method=RequestMethod.POST)
    @ResponseBody
    public ResultMsg addFileFolder(@RequestBody  FileLibraryDto fileLibraryDto){
        return fileLibraryService.addFolder(fileLibraryDto);
    }

    //@RequiresPermissions("fileLibrary#saveFile#post")
    @RequiresAuthentication
    @RequestMapping(name="新建文件",path ="saveFile",method=RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveFile(@RequestBody  FileLibraryDto fileLibraryDto){
       return  fileLibraryService.saveFile(fileLibraryDto);
    }


    //@RequiresPermissions("fileLibrary#updateFile#put")
    @RequiresAuthentication
    @RequestMapping(name="修改文件",path ="updateFile",method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg updateFile(@RequestBody  FileLibraryDto fileLibraryDto){
        return fileLibraryService.updateFile(fileLibraryDto);
    }

    //@RequiresPermissions("fileLibrary#deleteFile#delete")
    @RequiresAuthentication
    @RequestMapping(name="修改文件",path ="deleteFile",method = RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteFile(@RequestParam String fileId){
        fileLibraryService.deleteFile(fileId);
    }


    //@RequiresPermissions("fileLibrary#deleteRootDirectory#delete")
    @RequiresAuthentication
    @RequestMapping(name="删除根目录",path ="deleteRootDirectory",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteRootDirectory(@RequestParam String parentFileId){
        return fileLibraryService.deleteRootDirectory(parentFileId);
    }

    //@RequiresPermissions("fileLibrary#getFileUrlById#get")
    @RequiresAuthentication
    @RequestMapping(name="获取文件路径",path="getFileUrlById",method = RequestMethod.GET)
    @ResponseBody
    public FileLibraryDto getFileUrlById(@RequestParam String fileId){
        String fileUrl = fileLibraryService.findFileUrlById(fileId);
        FileLibraryDto fileLibraryDto = new FileLibraryDto();
        fileLibraryDto.setFileUrl(fileUrl);
        return fileLibraryDto ;
    }

    @RequiresAuthentication
    @RequestMapping(name="查询所有文件列表，并分等级查询",path="getFiles",method = RequestMethod.POST)
    @ResponseBody
    public Map<String , Object> getFiles(){
        return fileLibraryService.getFiles();
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取政策指标库数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<PolicyDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return policyService.get(odataObj);
    }

   @RequiresAuthentication
    @RequestMapping(name = "创建政策指标库", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody PolicyDto record) {
        return policyService.save(record);
    }

    /**
     * begin html
     */
    @RequiresPermissions("fileLibrary#html/fileLibrary#get")
    @RequestMapping(name="质量管理文件库", path ="html/fileLibrary",method= RequestMethod.GET)
    public String list(){
        return ctrlName + "/fileLibrary";
    }


    //@RequiresPermissions("fileLibrary#html/fileList#get")
    @RequiresAuthentication
    @RequestMapping(name="质量管理文件库-文件列表",path ="html/fileList",method=RequestMethod.GET)
    public String fileList(){
        return ctrlName + "/fileList";
    }

    //@RequiresPermissions("fileLibrary#html/fileEdit#get")
    @RequiresAuthentication
    @RequestMapping(name="质量管理文件库-新建文件",path="html/fileEdit",method=RequestMethod.GET)
    public String fileEdit(){
        return ctrlName + "/fileEdit";
    }


    @RequiresPermissions("fileLibrary#html/policyLibrary#get")
    @RequestMapping(name="政策标准库",path="html/policyLibrary",method=RequestMethod.GET)
    public String policyLibrary(){
        return ctrlName + "/policyLibrary";
    }

    //@RequiresPermissions("fileLibrary#html/policyList#get")
    @RequiresAuthentication
    @RequestMapping(name="政策标准库-文件列表",path ="html/policyList",method=RequestMethod.GET)
    public String policyList(){
        return ctrlName + "/policyList";
    }

    //@RequiresPermissions("fileLibrary#html/policyEdit#get")
    @RequiresAuthentication
    @RequestMapping(name="政策标准库-新建文件",path="html/policyEdit",method=RequestMethod.GET)
    public String policyEdit(){
        return ctrlName + "/policyEdit";
    }

    @RequiresAuthentication
    @RequestMapping(name="新建政策标准库",path="html/addPolicyLibrary",method=RequestMethod.GET)
    public String policyAdd(){
        return ctrlName + "/addPolicyLibrary";
    }

    @RequiresAuthentication
    @RequestMapping(name="文件指标库列表",path="html/documentList",method=RequestMethod.GET)
    public String documentList(){
        return ctrlName + "/documentList";
    }
}