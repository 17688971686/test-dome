package cs.controller.sys;

import cs.ahelper.RealPathResolver;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/16 0016.
 */
@Controller
@RequestMapping(name = "富文本编辑器", path = "froala")
public class FroalaController {
    private static final String froala_img_path = "contents/upload/froala";
    @Autowired
    private RealPathResolver realPathResolver;

    /**
     * 需要返回HashMap 格式的，且形如
     {"link":"http://i.froala.com/images/missing.png"}
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(name = "富文本图片上传", path = "uploadImg", method = RequestMethod.POST)
    @ResponseBody
    Map<String, String> uploadImg(@RequestParam("file") MultipartFile multipartFile,String rootPath){
        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        Map<String, String> map = new HashMap<>();
        try {
            String imgId = UUID.randomUUID().toString();
            String savePath = realPathResolver.get(froala_img_path);
            File isFileExists = new File(savePath);
            if (!isFileExists.exists()) {
                isFileExists.mkdirs();      //如果目录不存在，则创建
            }
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),new File(savePath+"/"+imgId+fileType));
            map.put("link", rootPath+"/"+froala_img_path+"/"+imgId+fileType);
        } catch (Exception e) {

        }
        return map;
    }
}
