package cs.controller.sys;

import cs.common.Constant;
import cs.common.utils.SysFileUtil;
import cs.domain.sys.SysFile;
import cs.model.PageModelDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.SysFileService;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author lqs
 *         文件上传
 */
@Controller
@RequestMapping(name = "文件管理", path = "file")
public class FileController {
    private static Logger logger = Logger.getLogger(FileController.class);
    private String ctrlName = "file";

    @Autowired
    private SysFileService fileService;

    @RequestMapping(name = "获取文件数据", path = "", method = RequestMethod.GET)
    public @ResponseBody
    PageModelDto<SysFileDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SysFileDto> sysFileDtos = fileService.get(odataObj);
        return sysFileDtos;
    }

    @RequestMapping(name = "根据业务ID获取附件", path = "findByBusinessId", method = RequestMethod.GET)
    public @ResponseBody
    List<SysFileDto> findByBusinessId(@RequestParam(required = true) String businessId) {
        List<SysFileDto> sysfileDto = fileService.findByBusinessId(businessId);
        return sysfileDto;
    }

    @RequestMapping(name = "根据收文ID获取附件", path = "findBySysFileSignId", method = RequestMethod.GET)
    public @ResponseBody
    List<SysFileDto> findByFileSignId(@RequestParam(required = true) String signid) {
        List<SysFileDto> sysfileDto = fileService.findBySysFileSignId(signid);
        return sysfileDto;
    }

    @RequestMapping(name = "获取一个项目下的所有附件列表", path = "initFileUploadlist", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> initFileUploadlist(@RequestParam(required = true) String signid) {
        return fileService.initFileUploadlist(signid);
    }


    @RequestMapping(name = "文件上传", path = "fileUpload", method = RequestMethod.POST)
    public @ResponseBody
    SysFileDto upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                      @RequestParam(required = true) String businessId, String sysSignId,
                      String sysfileType,String sysMinType) throws IOException {
        SysFileDto sysFileDto = null;
        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());

        if (!multipartFile.isEmpty()) {
            sysFileDto = fileService.save(multipartFile.getBytes(), fileName, businessId, fileType, sysSignId, sysfileType,sysMinType);
        } else {
            logger.info("文件上传失败，无法获取文件信息！");
            throw new IOException(Constant.ERROR_MSG);
        }

        return sysFileDto;
    }

    @RequestMapping(name = "文件下载", path = "fileDownload", method = RequestMethod.GET)
    public @ResponseBody
    void fileDownload(@RequestParam(required = true) String sysfileId, HttpServletResponse response) throws IOException {
        logger.debug("==================附件下载==================");
        SysFile file = fileService.findFileById(sysfileId);
        String path = SysFileUtil.getUploadPath();
        String url = file.getFileUrl();
        String fileType = file.getFileType().toLowerCase(); //最小化
        String filename = file.getShowName();
        filename = URLDecoder.decode(filename, "UTF-8");
        //获取文件保存路径
        String pathUrl = path + url;
        String reg = ".*\\\\(.*)";
        //文件名
        String fileNames = pathUrl.replaceAll(reg, "$1");
        if (fileNames == null || fileNames.equals(" ")) {
            return;
        }
        //调用输出流
        File f = new File(pathUrl);
        if (!f.exists()) {
            return;
        }
        FileInputStream in = new FileInputStream(f);
        OutputStream out = response.getOutputStream();
        try {
            switch (fileType) {
                case ".jpg":
                    response.setHeader("Content-type", "application/.jpg");
                    break;
                case ".png":
                    response.setHeader("Content-type", "application/.png");
                    break;
                case ".gif":
                    response.setHeader("Content-type", "application/.gif");
                    break;
                case ".docx":
                    response.setHeader("Content-type", "application/.docx");
                    break;
                case ".doc":
                    response.setHeader("Content-type", "application/.doc");
                    break;
                case ".xlsx":
                    response.setHeader("Content-type", "application/.xlsx");
                    break;
                case ".xls":
                    response.setHeader("Content-type", "application/.xls");
                    break;
                case ".pdf":
                    response.setHeader("Content-type", "application/.pdf");
                    break;
                default:
                    response.setContentType("application/octet-stream");

            }
            /*if (fileType.equals(".jpg")) {
				response.setHeader("Content-type", "application/.jpg");
			}
			else if (fileType.equals(".png")) {
				response.setHeader("Content-type", "application/.png");
			} 
			else if(fileType.equals(".gif")){
				response.setHeader("Content-type", "application/.gif");
			}
			else if(fileType.equals(".docx")){
				response.setHeader("Content-type", "application/.docx");
			}
			else if(fileType.equals(".doc")){
				response.setHeader("Content-type", "application/.doc");
			}
			else if(fileType.equals(".xlsx")){
				response.setHeader("Content-type", "application/.xlsx");
			}
			else if(fileType.equals(".xls")){
				response.setHeader("Content-type", "application/.xls");
			}
			else if(fileType.equals(".pdf")){
				response.setHeader("Content-type", "application/.pdf");
			}
			else if (fileType.equals("xls")) {
				response.setContentType("applicationnd.ms-excel;charset=GBK");
				response.setHeader("Content-type", "application/x-msexcel");
			} else {
				response.setContentType("application/octet-stream");
			}*/

            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(filename.getBytes("GB2312"), "ISO8859-1"));

            byte b[] = new byte[1024];
            int len = -1;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
    }

    @RequiresPermissions("file#deleteSysFile#delete")
    @RequestMapping(name = "删除系统文件", path = "deleteSysFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSysFile(@RequestParam String id) {
        fileService.deleteById(id);
    }

    @RequestMapping(name = "文件删除", path = "delete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(required = true) String sysFileId) throws IOException {
        fileService.deleteById(sysFileId);
    }
}
