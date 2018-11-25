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
        return propertyUtil.readProperty(FILE_LIBRARY_PATH);
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

//        sql.append( " where " + FileLibrary_.parentFileId.getName() + " in ( select fileId from cs_fileLibrary where "  + FileLibrary_.parentFileId.getName() + " is null )");

        sql.append(" where "  + FileLibrary_.parentFileId.getName() + " is null ");

        //1、查询一级文件
        List<FileLibrary> fileLibraryList1 = fileLibraryRepo.findBySql(sql);

        //一级文件
        if(fileLibraryList1 != null && fileLibraryList1.size() > 0 ){
            List<FileLibraryDto> fileLibraryDtoList1 = new ArrayList<>();
            for(FileLibrary fileLibrary : fileLibraryList1) {
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(fileLibrary, fileLibraryDto);

                //2、查询二级文件
                List<FileLibrary> fileLibraryList2 = findChildFile(fileLibrary);
                if (fileLibraryList2 != null && fileLibraryList2.size() > 0) {
                    //二级
                    List<FileLibraryDto> fileLibraryDtoList2 = new ArrayList<>();

                    //遍历二级
                    for (FileLibrary fileLibrary2 : fileLibraryList2) {

                        FileLibraryDto fileLibraryDto2 = new FileLibraryDto();
                        //如果是文件，则三级为空，直接存到四级
                       /* if(Constant.libraryType.FILE_TYPE.getValue().equals(fileLibrary2.getFileNature())){
                            List<FileLibraryDto> list3 = new ArrayList<>();
                            List<FileLibraryDto> list4 = new ArrayList<>();
                            //定义四级对象和集合
                            FileLibraryDto dto4 = new FileLibraryDto();
                            BeanCopierUtils.copyProperties(fileLibrary2, dto4);
                            list4.add(dto4);

                            //定义三级对象以及集合 ， 并将四级集合存入三级对象的集合中
                            FileLibraryDto dto3 = new FileLibraryDto();
                            dto3.setFileLibraryList(list4);
                            list3.add(dto3);
                            //将三级集合存入二级对象的集合中
                            fileLibraryDto2.setFileLibraryList(list3);
                            fileLibraryDtoList2.add(fileLibraryDto2);

                        }else{*/

                            BeanCopierUtils.copyProperties(fileLibrary2, fileLibraryDto2);
                            fileLibraryDtoList2.add(fileLibraryDto2);
                            //2、三级文件
                            List<FileLibrary> fileLibraryList3 = findChildFile(fileLibrary2);

                            if (fileLibraryList3 != null && fileLibraryList3.size() > 0) {
                                List<FileLibraryDto> fileLibraryDtoList3 = new ArrayList<>();
                                for (FileLibrary fileLibrary3 : fileLibraryList3) {
                                    FileLibraryDto fileLibraryDto3 = new FileLibraryDto();

                                    //判断是否是文件，若是则直接添加到最后一级
                                    if(Constant.libraryType.FILE_TYPE.getValue().equals(fileLibrary3.getFileNature())){
                                        FileLibraryDto fileLibraryDto4 = new FileLibraryDto();
                                        BeanCopierUtils.copyProperties(fileLibrary3, fileLibraryDto4);
                                        List<FileLibraryDto> list4 = new ArrayList<>();
                                        list4.add(fileLibraryDto4);
                                        fileLibraryDto3.setFileLibraryList(list4);
                                        fileLibraryDtoList3.add(fileLibraryDto3);
                                    }else{
                                        BeanCopierUtils.copyProperties(fileLibrary3, fileLibraryDto3);
                                        fileLibraryDtoList3.add(fileLibraryDto3);
                                        //2、四级文件
                                        List<FileLibrary> fileLibraryList4 = findChildFile(fileLibrary3);
                                        List<FileLibraryDto> fileLibraryDtoList4 = new ArrayList<>();
                                        if (fileLibraryList4 != null && fileLibraryList4.size() > 0) {

                                            for (FileLibrary fileLibrary4 : fileLibraryList4) {
                                                FileLibraryDto fileLibraryDto4 = new FileLibraryDto();
                                                BeanCopierUtils.copyProperties(fileLibrary4, fileLibraryDto4);
                                                List<SysFileDto> sysFileDtoList = sysFileService.findByBusinessId(fileLibrary4.getFileId());
                                                fileLibraryDto4.setSysFileDtoList(sysFileDtoList);
                                                fileLibraryDtoList4.add(fileLibraryDto4);

                                            }
                                            fileLibraryDto3.setFileLibraryList(fileLibraryDtoList4);
                                        }
                                    }


                                }
                                fileLibraryDto2.setFileLibraryList(fileLibraryDtoList3);
                            }
//                        }

                    }
//                    fileLibraryDto.setFileLibraryList(fileLibraryDtoList2);
                    resultMap.put(fileLibraryDto.getFileName() , fileLibraryDtoList2);
                }
//                fileLibraryDtoList1.add(fileLibraryDto);
            }
//            resultMap.put("data" , fileLibraryDtoList1);
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