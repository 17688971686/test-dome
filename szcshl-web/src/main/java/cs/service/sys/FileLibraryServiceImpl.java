package cs.service.sys;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.sys.FileLibrary;
import cs.domain.sys.FileLibrary_;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.sys.FileLibraryDto;
import cs.model.sys.SysFileDto;
import cs.repository.repositoryImpl.sys.FileLibraryRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * Description:
 * Author: mcl
 * Date: 2017/8/21 16:26
 */
@Service
public class FileLibraryServiceImpl implements  FileLibraryService{

    private static String FILE_LIBRARY_PATH ="file_library_path";

    @Autowired
    private FileLibraryRepo fileLibraryRepo;

    @Autowired
    private  SysFileService sysFileService;

    @Autowired
    private SysFileRepo sysFileRepo;

    public String getPath(){
        PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
        String libraryPath = propertyUtil.readProperty(FILE_LIBRARY_PATH);
        return libraryPath;
    }

    /**
     * 初始化文件夹
     * @return
     */
    @Override
    public List<FileLibraryDto> initFolder(String libraryType) {
        Criteria criteria = fileLibraryRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(FileLibrary_.fileType.getName(), libraryType));
//        criteria.add(Restrictions.eq(FileLibrary_.fileNature.getName(),Constant.libraryType.FOLDER_TYPE.getValue()));

        List<FileLibrary> fileLibraryList = criteria.list();
        List<FileLibraryDto> fileLibraryDtoList = new ArrayList<>();
        if(fileLibraryList.size()>0){
            for(FileLibrary fl : fileLibraryList){
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(fl,fileLibraryDto);
                List<SysFileDto> sysFileDtoList = sysFileService.findByBusinessId(fl.getFileId());
                if(sysFileDtoList != null && sysFileDtoList .size()>0){

                    fileLibraryDto.setSysFileDtoList(sysFileDtoList);
                }
                fileLibraryDtoList.add(fileLibraryDto);
            }
        }
        return fileLibraryDtoList;
    }

    /**
     * 添加文件夹
     * @param fileLibraryDto
     */
    @Override
    public ResultMsg addFolder(FileLibraryDto fileLibraryDto) {
        FileLibrary findFile=new FileLibrary();
        //通过父id，文件名 ，文件性质，文件类型， 判断该文件是否存在
        Boolean exitFile = fileLibraryRepo.findByFileNameAndParentId(fileLibraryDto.getParentFileId(),fileLibraryDto.getFileName(),
                Constant.libraryType.FOLDER_TYPE.getValue() , fileLibraryDto.getFileType());
        if(!exitFile) {
            FileLibrary fileLibrary = new FileLibrary();
            //创建文件目录格式 ：根目录/质量管理文件库/文件夹名
            //获取根目录
            String fileLibraryPath = SysFileUtil.getUploadPath();
            String url = "";
            if (fileLibraryDto.getParentFileId() != null) {
                url = File.separator + findFileUrlById(fileLibraryDto.getParentFileId()) + File.separator + fileLibraryDto.getFileName();
            } else {
                //政策标准文件库
                if((Constant.libraryType.POLICY_LIBRARY.getValue()).equals(fileLibraryDto.getFileType())){
                    url = File.separator + Constant.SysFileType.POLICYLIBRARY.getValue()+ File.separator + fileLibraryDto.getFileName();
                }
                //质量管理文件库
                if((Constant.libraryType.QUALITY_LIBRARY.getValue()).equals(fileLibraryDto.getFileType())){
                    url = File.separator + Constant.SysFileType.FILELIBRARY.getValue()+ File.separator + fileLibraryDto.getFileName();
                }
            }

            String fileUrl = SysFileUtil.generatRelativeUrl(fileLibraryPath , url ,null ,null ,fileLibraryDto.getFileName());

            BeanCopierUtils.copyPropertiesIgnoreNull(fileLibraryDto, fileLibrary);
            fileLibrary.setFileId(UUID.randomUUID().toString());
            fileLibrary.setCreatedBy(SessionUtil.getDisplayName());
            fileLibrary.setCreatedDate(new Date());
            fileLibrary.setModifiedBy(SessionUtil.getDisplayName());
            fileLibrary.setModifiedDate(new Date());
            fileLibrary.setFileUrl( url);
            //设置为文件夹类型
            fileLibrary.setFileNature(Constant.libraryType.FOLDER_TYPE.getValue());

            fileLibraryRepo.save(fileLibrary);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"创建成功！");
        }else{
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),String.format("文件夹：%s 已经存在，请重新输入",fileLibraryDto.getFileName()));
        }
    }

    /**
     * 添加文件
     * @param fileLibraryDto
     */
    @Override
    public ResultMsg saveFile(FileLibraryDto fileLibraryDto) {
        FileLibrary findFile=new FileLibrary();
        Boolean exitFile = fileLibraryRepo.findByFileNameAndParentId(fileLibraryDto.getParentFileId()
                ,fileLibraryDto.getFileName(),Constant.libraryType.FILE_TYPE.getValue(),fileLibraryDto.getFileType());
        if(!exitFile) {
            FileLibrary fileLibrary = new FileLibrary();
            String fileUrl=File.separator + findFileUrlById(fileLibraryDto.getParentFileId()) + File.separator + fileLibraryDto.getFileName();;

            BeanCopierUtils.copyPropertiesIgnoreNull(fileLibraryDto,fileLibrary);
            fileLibrary.setFileId(UUID.randomUUID().toString());
            fileLibrary.setCreatedBy(SessionUtil.getDisplayName());
            fileLibrary.setCreatedDate(new Date());
            fileLibrary.setModifiedBy(SessionUtil.getDisplayName());
            fileLibrary.setModifiedDate(new Date());
            fileLibrary.setFileUrl( fileUrl);
            //设置为 文件类型
            fileLibrary.setFileNature(Constant.libraryType.FILE_TYPE.getValue());
            fileLibraryRepo.save(fileLibrary);
            FileLibraryDto fld = new FileLibraryDto();
            BeanCopierUtils.copyPropertiesIgnoreNull(fileLibrary , fld);
//            fld.setFileId(fileLibrary.getFileId());
            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "操作成功！" , fld);
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue()  , String.format("文件：%s 已经存在，请重新输入",fileLibraryDto.getFileName()) , null);
        }

    }

    @Override
    public String findFileUrlById(String fileId) {
        /*HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select "+ FileLibrary_.fileUrl.getName() + "，"+ FileLibrary_.fileName.getName() + " from cs_fileLibrary where "+ FileLibrary_.fileId.getName()+"=:fileId");
        hqlBuilder.setParam("fileId",fileId);
        List<Object[]>  list = fileLibraryRepo.getObjectArray(hqlBuilder);
        String fileUrl = "";
        if(Validate.isList(list)){
            fileUrl = list.get(0)[0].toString();
        }*/
        String fileUrl = "";
        FileLibrary fileLibrary = fileLibraryRepo.findById( FileLibrary_.fileId.getName(),fileId);
        if(fileLibrary != null){
            fileUrl = fileLibrary.getFileUrl();
        }
        return fileUrl;
    }

    @Override
    public FileLibraryDto findFileById(String fileId) {
        FileLibrary fileLibrary = fileLibraryRepo.getById(fileId);
        FileLibraryDto fileLibraryDto = new FileLibraryDto();
        BeanCopierUtils.copyProperties(fileLibrary,fileLibraryDto);
        return fileLibraryDto;
    }

    @Override
    public ResultMsg updateFile(FileLibraryDto fileLibraryDto) {
        FileLibrary fileLibrary = fileLibraryRepo.getById(fileLibraryDto.getFileId());
        BeanCopierUtils.copyProperties(fileLibraryDto,fileLibrary);
        fileLibrary.setModifiedDate(new Date());
        fileLibrary.setModifiedBy(SessionUtil.getLoginName());
        fileLibraryRepo.save(fileLibrary);
        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , null);
    }

    @Override
    public void deleteFile(String fileId) {
        FileLibrary fileLibrary = fileLibraryRepo.getById(fileId);
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("from "+ SysFile.class.getSimpleName()+ " where "+ SysFile_.businessId.getName()+"=:businessId");
        hqlBuilder.setParam("businessId",fileId);
        List<SysFile> sysFileList = sysFileRepo.findByHql(hqlBuilder);
        String path = SysFileUtil.getUploadPath();
        if(sysFileList !=null && sysFileList.size()>0){
            sysFileList.forEach(f->{
                sysFileRepo.delete(f);
//                SysFileUtil.deleteFile(path+f.getFileUrl());
            });
        }
        fileLibraryRepo.delete(fileLibrary);
    }

    /**
     * 删除文件夹
     * @param parentFileId
     * @return
     */
    @Override
    public ResultMsg deleteRootDirectory(String parentFileId) {
//        String[] ids = parentFileId.split(",");
//        for(String fileId : ids){
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("delete from " + FileLibrary.class.getSimpleName() + " f1 where not exists (");
        hqlBuilder.append("select 1 from "+ FileLibrary.class.getSimpleName() + " f2 where f2." + FileLibrary_.parentFileId.getName() + "=:parentFileId )");
        hqlBuilder.append(" and f1."+ FileLibrary_.fileId.getName() + "=:fileId");
        hqlBuilder.setParam("parentFileId",parentFileId);
        hqlBuilder.setParam("fileId",parentFileId);
        int count = fileLibraryRepo.executeHql(hqlBuilder);
        if(count <=0){
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "该根目录包含子目录，不能将其删除！" , null );
        }
//        }

        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "删除成功！" , null);
    }

    /**
     * 文件指标库数据查询
     * @return
     */
    @Override
    public Map<String , Object> getFiles() {
        Map<String , Object> resultMap = new HashMap<>();
        HqlBuilder sql = HqlBuilder.create();
        //1、先查询一级文件
        sql.append("select * from  cs_fileLibrary " );

        sql.append( " where " + FileLibrary_.parentFileId.getName() + " is null ");
//        sql.append(" where " + FileLibrary_.fileNature.getName() + "=:fileNature");
//        sql.setParam("fileNature" , Constant.libraryType.FILE_TYPE.getValue());

        //1、查询最后一级文件，即第五级
        List<FileLibrary> fileLibraryList1 = fileLibraryRepo.findBySql(sql);

        List<Object> list2 = new ArrayList<>();
        List<Object> list3 = new ArrayList<>();
        List<Object> list4 = new ArrayList<>();
        List<Object> list5 = new ArrayList<>();

        //一级文件
        if(fileLibraryList1 != null && fileLibraryList1.size() > 0 ){
            List<FileLibraryDto> fileLibraryDtoList1 = new ArrayList<>();
            for(FileLibrary fileLibrary : fileLibraryList1) {
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(fileLibrary, fileLibraryDto);
                fileLibraryDtoList1.add(fileLibraryDto);
                //2、二级文件
                List<FileLibrary> fileLibraryList2 = findChildFile(fileLibrary);
                if (fileLibraryList2 != null && fileLibraryList2.size() > 0) {
                    List<FileLibraryDto> fileLibraryDtoList2 = new ArrayList<>();
                    for (FileLibrary fileLibrary2 : fileLibraryList2) {
                        FileLibraryDto fileLibraryDto2 = new FileLibraryDto();
                        BeanCopierUtils.copyProperties(fileLibrary2, fileLibraryDto2);
                        fileLibraryDtoList2.add(fileLibraryDto2);
                        //2、三级文件
                        List<FileLibrary> fileLibraryList3 = findChildFile(fileLibrary2);
                        if (fileLibraryList3 != null && fileLibraryList3.size() > 0) {
                            List<FileLibraryDto> fileLibraryDtoList3 = new ArrayList<>();
                            for (FileLibrary fileLibrary3 : fileLibraryList3) {
                                FileLibraryDto fileLibraryDto3 = new FileLibraryDto();
                                BeanCopierUtils.copyProperties(fileLibrary3, fileLibraryDto3);
                                fileLibraryDtoList3.add(fileLibraryDto3);
                                //2、四级文件
                                List<FileLibrary> fileLibraryList4 = findChildFile(fileLibrary3);
                                if (fileLibraryList4 != null && fileLibraryList4.size() > 0) {
                                    List<FileLibraryDto> fileLibraryDtoList4 = new ArrayList<>();
                                    for (FileLibrary fileLibrary4 : fileLibraryList4) {
                                        FileLibraryDto fileLibraryDto4 = new FileLibraryDto();
                                        BeanCopierUtils.copyProperties(fileLibrary4, fileLibraryDto4);
                                        fileLibraryDtoList4.add(fileLibraryDto4);
                                        //2、五级文件
                                        List<FileLibrary> fileLibraryList5 = findChildFile(fileLibrary2);
                                        if (fileLibraryList5 != null && fileLibraryList5.size() > 0) {
                                            List<FileLibraryDto> fileLibraryDtoList5 = new ArrayList<>();
                                            for (FileLibrary fileLibrary5 : fileLibraryList5) {
                                                FileLibraryDto fileLibraryDto5 = new FileLibraryDto();
                                                BeanCopierUtils.copyProperties(fileLibrary5, fileLibraryDto5);
                                                fileLibraryDtoList5.add(fileLibraryDto5);
                                            }
                                            list5.add(fileLibraryDtoList5);
                                        }else{
                                            resultMap.put("five" , null);
                                        }
                                    }
                                    list4.add(fileLibraryDtoList4);
                                }else{
                                    resultMap.put("four" , null);
                                }
                            }
                            list3.add(fileLibraryDtoList3);

                        }else{
                            resultMap.put("three" , null);
                        }
                    }
                    list2.add(fileLibraryDtoList2);

                }else{
                    resultMap.put("two" , null);
                }
            }
            resultMap.put("one" , fileLibraryDtoList1);
            resultMap.put("two" , list2);
            resultMap.put("three" , list3);
            resultMap.put("four" , list4);
            resultMap.put("five" , list5);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findFiles() {
        Map<String , Object> resultMap = new HashMap<>();

        HqlBuilder sql = HqlBuilder.create();
        //1、先查询最后一级文件
        sql.append("select * from  cs_fileLibrary " );
        sql.append( " where " + FileLibrary_.fileNature.getName() + "=:fileNature");
        sql.setParam("fileNature" , Constant.libraryType.FILE_TYPE.getValue());
        List<FileLibrary> fileLibraryList5 = fileLibraryRepo.findBySql(sql);

        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        List<Object> list3 = new ArrayList<>();
        List<Object> list4 = new ArrayList<>();
        List<Object> list5 = new ArrayList<>();

        if(fileLibraryList5 != null && fileLibraryList5.size() > 0 ){
            List<FileLibraryDto> fileLibraryDtoList1 = new ArrayList<>();
            for(int i =0 ; i < fileLibraryList5.size() ; i++ ){
                FileLibrary fileLibrary = fileLibraryList5.get(i);
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(fileLibrary ,  fileLibraryDto);
                fileLibraryDtoList1.add(fileLibraryDto);

                List<FileLibrary> fileLibraryList2 = findChildFile(fileLibrary);
                if(fileLibraryList2 !=  null && fileLibraryList2.size() > 0 ){
//                    for()
                }
            }
        }
        return resultMap;
    }

    private List<FileLibrary>  findChildFile(FileLibrary fileLibrary){
        HqlBuilder sql = HqlBuilder.create();
        sql.append(" select * from  cs_fileLibrary "  );
        sql.append(" where " + FileLibrary_.parentFileId.getName() + " =:parentFileId");
        sql.setParam("parentFileId" , fileLibrary.getFileId());
        List<FileLibraryDto> fileLibraryDtoList = new ArrayList<>();
        List<FileLibrary> fileLibraryList = fileLibraryRepo.findBySql(sql);
       /* if(fileLibraryList != null && fileLibraryList.size() > 0 ){

            for(FileLibrary f : fileLibraryList){
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(f , fileLibraryDto);
                fileLibraryDtoList.add(fileLibraryDto);
            }
        }*/
        return fileLibraryList;
    }

    /**
     * 初始化文件夹下的所有文件
     * @param fileType
     * @param  fileId
     * @return
     */
    @Override
    public ResultMsg initFileList( String fileId , String fileType) {
        Criteria criteria = fileLibraryRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(FileLibrary_.parentFileId.getName() , fileId));
        criteria.add(Restrictions.eq(FileLibrary_.fileType.getName() , fileType));
        criteria.add(Restrictions.eq(FileLibrary_.fileNature.getName(),Constant.libraryType.FILE_TYPE.getValue()));
        List<FileLibrary> fileLibraryList = criteria.list();
        List<FileLibraryDto> fileLibraryDtoList = new ArrayList<>();
        if(fileLibraryList!=null && fileLibraryList.size()>0){
            for(FileLibrary fileLibrary : fileLibraryList){
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(fileLibrary,fileLibraryDto);
                List<SysFileDto> sysFileDtoList = sysFileService.findByBusinessId(fileLibrary.getFileId());
                fileLibraryDto.setSysFileDtoList(sysFileDtoList);
                fileLibraryDtoList.add(fileLibraryDto);
            }
        }
        if(fileLibraryDtoList !=null && fileLibraryDtoList.size() > 0){
            return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "查询数据成功!" , fileLibraryDtoList);
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "查询数据失败！" , null);
        }

    }
}