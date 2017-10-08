package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.Constant;
import cs.model.PageModelDto;
import cs.model.sys.FileLibraryDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.FileLibraryService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 文件库
 * Author: mcl
 * Date: 2017/8/21 14:53
 */
@Controller
@RequestMapping(name="文件库",path="fileLibrary")
@MudoleAnnotation(name = "文件库",value = "permission#fileMng")
public class FileLibraryController {

    private String ctrlName = "library";

    @Autowired
    private FileLibraryService fileLibraryService;

    //@RequiresPermissions("fileLibrary#initFileFolder#get")
    @RequiresAuthentication
    @RequestMapping(name="初始化质量管理文件库-文件夹",path="initFileFolder",method = RequestMethod.GET)
    @ResponseBody
    public List<FileLibraryDto> initFileFolder(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return fileLibraryService.initFolder(oDataObj,Constant.folderType.FILE_LIBRARY.getValue());
    }

    //@RequiresPermissions("fileLibrary#initPolicyFolder#get")
    @RequiresAuthentication
    @RequestMapping(name="初始化政策标准库-文件夹",path="initPolicyFolder",method = RequestMethod.GET)
    @ResponseBody
    public List<FileLibraryDto> initPolicyFolder(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return fileLibraryService.initFolder(oDataObj,Constant.folderType.POLICY_LIBRARY.getValue());
    }

    //@RequiresPermissions("fileLibrary#initFileList#post")
    @RequiresAuthentication
    @RequestMapping(name="获取文件夹下所有文件",path="initFileList",method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<FileLibraryDto> initFileList(HttpServletRequest request) throws ParseException {
        String fileId = request.getParameter("fileId");
        ODataObj oDataObj = new ODataObj(request);
        return fileLibraryService.initFileList(oDataObj,fileId);
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
    @RequestMapping(name="新建文件夹-质量管理文件库",path ="addFileFolder",method=RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.CREATED)
    public void addFileFolder(@RequestBody  FileLibraryDto fileLibraryDto){
        fileLibraryService.addFolder(fileLibraryDto,Constant.folderType.FILE_LIBRARY.getValue());

    }

    //@RequiresPermissions("fileLibrary#addPolicyFolder#post")
    @RequiresAuthentication
    @RequestMapping(name="新建文件夹-政策标准库",path ="addPolicyFolder",method=RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.CREATED)
    public void addPolicyFolder(@RequestBody  FileLibraryDto fileLibraryDto){
        fileLibraryService.addFolder(fileLibraryDto,Constant.folderType.POLICY_LIBRARY.getValue());

    }

    //@RequiresPermissions("fileLibrary#saveFile#post")
    @RequiresAuthentication
    @RequestMapping(name="新建文件-质量管理文件库",path ="saveFile",method=RequestMethod.POST)
    @ResponseBody
    public FileLibraryDto saveFile(@RequestBody  FileLibraryDto fileLibraryDto){
       return  fileLibraryService.saveFile(fileLibraryDto,Constant.folderType.FILE_LIBRARY.getValue());
    }

    //@RequiresPermissions("fileLibrary#savePolicyFile#post")
    @RequiresAuthentication
    @RequestMapping(name="新建文件-政策标准库",path ="savePolicyFile",method=RequestMethod.POST)
    @ResponseBody
    public FileLibraryDto savePolicyFile(@RequestBody  FileLibraryDto fileLibraryDto){
        return  fileLibraryService.saveFile(fileLibraryDto,Constant.folderType.POLICY_LIBRARY.getValue());
    }

    //@RequiresPermissions("fileLibrary#updateFile#put")
    @RequiresAuthentication
    @RequestMapping(name="修改文件",path ="updateFile",method = RequestMethod.PUT)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void updateFile(@RequestBody  FileLibraryDto fileLibraryDto){
        fileLibraryService.updateFile(fileLibraryDto);
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
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteRootDirectory(@RequestParam String parentFileId){
        fileLibraryService.deleteRootDirectory(parentFileId);
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

    /**
     * begin html
     */
    @RequiresPermissions("fileLibrary#html/fileLibrary#get")
    @RequestMapping(name="质量文件库", path ="html/fileLibrary",method= RequestMethod.GET)
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
        return ctrlName + "/fileList";
    }

    //@RequiresPermissions("fileLibrary#html/policyEdit#get")
    @RequiresAuthentication
    @RequestMapping(name="政策标准库-新建文件",path="html/policyEdit",method=RequestMethod.GET)
    public String policyEdit(){
        return ctrlName + "/fileEdit";
    }
}