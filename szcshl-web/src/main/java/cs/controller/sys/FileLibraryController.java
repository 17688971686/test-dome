package cs.controller.sys;

import cs.model.PageModelDto;
import cs.model.sys.FileLibraryDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.FileLibraryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 质量管理文件库
 * Author: mcl
 * Date: 2017/8/21 14:53
 */
@Controller
@RequestMapping(name="质量管理文件库",path="fileLibrary")
public class FileLibraryController {

    private String ctrlName = "library";

    @Autowired
    private FileLibraryService fileLibraryService;

    @RequiresPermissions("fileLibrary#initFolder#get")
    @RequestMapping(name="初始化文件夹",path="initFolder",method = RequestMethod.GET)
    @ResponseBody
    public List<FileLibraryDto> initFolder(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        return fileLibraryService.initFolder(oDataObj);
    }


    @RequiresPermissions("fileLibrary#initFileList#post")
    @RequestMapping(name="获取文件夹下所有文件",path="initFileList",method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<FileLibraryDto> initFileList(HttpServletRequest request) throws ParseException {
        String fileId = request.getParameter("fileId");
        ODataObj oDataObj = new ODataObj(request);
        return fileLibraryService.initFileList(oDataObj,fileId);
    }


    @RequiresPermissions("fileLibrary#findFileById#get")
    @RequestMapping(name="通过id获取文件",path ="findFileById" , method=RequestMethod.GET)
    @ResponseBody
    public FileLibraryDto findFileById(@RequestParam  String fileId){
        return fileLibraryService.findFileById(fileId);
    }


    @RequiresPermissions("fileLibrary#addFolder#post")
    @RequestMapping(name="新建文件夹",path ="addFolder",method=RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.CREATED)
    public void addFolder(@RequestBody  FileLibraryDto fileLibraryDto){
        fileLibraryService.addFolder(fileLibraryDto);

    }

    @RequiresPermissions("fileLibrary#saveFile#post")
    @RequestMapping(name="新建文件",path ="saveFile",method=RequestMethod.POST)
    @ResponseBody
    public FileLibraryDto saveFile(@RequestBody  FileLibraryDto fileLibraryDto){
       return  fileLibraryService.saveFile(fileLibraryDto);
    }

    @RequiresPermissions("fileLibrary#updateFile#put")
    @RequestMapping(name="修改文件",path ="updateFile",method = RequestMethod.PUT)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void updateFile(@RequestBody  FileLibraryDto fileLibraryDto){
        fileLibraryService.updateFile(fileLibraryDto);
    }

    @RequiresPermissions("fileLibrary#deleteFile#delete")
    @RequestMapping(name="修改文件",path ="deleteFile",method = RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteFile(@RequestParam String fileId){
        fileLibraryService.deleteFile(fileId);
    }


    @RequiresPermissions("fileLibrary#deleteRootDirectory#delete")
    @RequestMapping(name="删除根目录",path ="deleteRootDirectory",method = RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteRootDirectory(@RequestParam String parentFileId){
        fileLibraryService.deleteRootDirectory(parentFileId);
    }

    @RequiresPermissions("fileLibrary#getFileUrlById#get")
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
    @RequestMapping(name="文件库列表", path ="html/fileLibrary",method= RequestMethod.GET)
    public String list(){
        return ctrlName + "/fileLibrary";
    }


    @RequiresPermissions("fileLibrary#html/fileList#get")
    @RequestMapping(name="文件列表",path ="html/fileList",method=RequestMethod.GET)
    public String fileList(){
        return ctrlName + "/fileList";
    }

    @RequiresPermissions("fileLibrary#html/fileEdit#get")
    @RequestMapping(name="新建文件",path="html/fileEdit",method=RequestMethod.GET)
    public String fileEdit(){
        return ctrlName + "/fileEdit";
    }
}