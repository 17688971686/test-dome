package cs.service.sys;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.sys.FileLibrary;
import cs.domain.sys.FileLibrary_;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.model.PageModelDto;
import cs.model.sys.FileLibraryDto;
import cs.model.sys.SysFileDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.FileLibraryRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.BaseRowSet;
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
     * @param oDataObj
     * @return
     */
    @Override
    public List<FileLibraryDto> initFolder(ODataObj oDataObj,String libraryType) {
        Criteria criteria = fileLibraryRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        if((Constant.folderType.POLICY_LIBRARY.getValue()).equals(libraryType)){//政策标准库
            criteria.add(Restrictions.eq(FileLibrary_.fileType.getName(),Constant.folderType.POLICY_LIBRARY.getValue()));
        }
        if((Constant.folderType.FILE_LIBRARY.getValue()).equals(libraryType)){//质量管理文件库
            criteria.add(Restrictions.eq(FileLibrary_.fileType.getName(),Constant.folderType.FILE_LIBRARY.getValue()));
        }
        criteria.add(Restrictions.eq(FileLibrary_.fileNature.getName(),Constant.fileNatrue.FOLDER_TYPE.getValue()));

        List<FileLibrary> fileLibraryList = criteria.list();
        List<FileLibraryDto> fileLibraryDtoList = new ArrayList<>();
        if(fileLibraryList.size()>0){
            for(FileLibrary fl : fileLibraryList){
                FileLibraryDto fileLibraryDto = new FileLibraryDto();
                BeanCopierUtils.copyProperties(fl,fileLibraryDto);
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
    public ResultMsg addFolder(FileLibraryDto fileLibraryDto, String libraryType) {
        FileLibrary findFile=new FileLibrary();
        if(Constant.folderType.POLICY_LIBRARY.getValue().equals(libraryType)){
          findFile = fileLibraryRepo.findByFileNameAndParentId(fileLibraryDto.getParentFileId(),fileLibraryDto.getFileName(),Constant.fileNatrue.FOLDER_TYPE.getValue(),Constant.folderType.POLICY_LIBRARY.getValue());
        }
        if(Constant.folderType.FILE_LIBRARY.getValue().equals(libraryType)){
            findFile = fileLibraryRepo.findByFileNameAndParentId(fileLibraryDto.getParentFileId(),fileLibraryDto.getFileName(),Constant.fileNatrue.FOLDER_TYPE.getValue(),Constant.folderType.FILE_LIBRARY.getValue());
        }
        if(findFile ==null) {
            FileLibrary fileLibrary = new FileLibrary();
            //创建文件目录格式 ：根目录/质量管理文件库/文件夹名
            String fileLibraryPath = SysFileUtil.getUploadPath();//获取根目录
            String url = "";
            if (fileLibraryDto.getParentFileId() != null) {
                url = File.separator + findFileUrlById(fileLibraryDto.getParentFileId()) + File.separator + fileLibraryDto.getFileName();
            } else {
                if(Constant.folderType.POLICY_LIBRARY.getValue().equals(libraryType)){
                    url = File.separator + Constant.SysFileType.POLICYLIBRARY.getValue()+ File.separator + fileLibraryDto.getFileName();
                }
                if(Constant.folderType.FILE_LIBRARY.getValue().equals(libraryType)){
                    url = File.separator + Constant.SysFileType.FILELIBRARY.getValue()+ File.separator + fileLibraryDto.getFileName();
                }
            }

            String fileUrl = SysFileUtil.generatRelativeUrl(fileLibraryPath , url ,null ,null ,fileLibraryDto.getFileName());

            BeanCopierUtils.copyPropertiesIgnoreNull(fileLibraryDto, fileLibrary);
            fileLibrary.setFileId(UUID.randomUUID().toString());
            fileLibrary.setCreatedBy(SessionUtil.getLoginName());
            fileLibrary.setCreatedDate(new Date());
            fileLibrary.setModifiedBy(SessionUtil.getLoginName());
            fileLibrary.setModifiedDate(new Date());
            fileLibrary.setFileUrl( url);
            fileLibrary.setFileNature(Constant.fileNatrue.FOLDER_TYPE.getValue());

            if(Constant.folderType.POLICY_LIBRARY.getValue().equals(libraryType)){
                fileLibrary.setFileType(Constant.folderType.POLICY_LIBRARY.getValue());
            }
            if(Constant.folderType.FILE_LIBRARY.getValue().equals(libraryType)){
                fileLibrary.setFileType(Constant.folderType.FILE_LIBRARY.getValue());
            }
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
    public FileLibraryDto saveFile(FileLibraryDto fileLibraryDto,String libraryType) {
        FileLibrary findFile=new FileLibrary();
        if(Constant.folderType.POLICY_LIBRARY.getValue().equals(libraryType)){
            findFile = fileLibraryRepo.findByFileNameAndParentId(fileLibraryDto.getParentFileId(),fileLibraryDto.getFileName(),Constant.fileNatrue.FOLDER_TYPE.getValue(),Constant.folderType.POLICY_LIBRARY.getValue());
        }
        if(Constant.folderType.FILE_LIBRARY.getValue().equals(libraryType)){
            findFile = fileLibraryRepo.findByFileNameAndParentId(fileLibraryDto.getParentFileId(),fileLibraryDto.getFileName(),Constant.fileNatrue.FOLDER_TYPE.getValue(),Constant.folderType.FILE_LIBRARY.getValue());
        }
        if(findFile ==null) {
        FileLibrary fileLibrary = new FileLibrary();
        String fileUrl="";
        if(fileLibraryDto.getFileId()!=null){
            fileLibrary.setParentFileId(fileLibraryDto.getParentFileId());
        }else{
            fileUrl=File.separator + fileLibraryDto.getFileName();
        }

        BeanCopierUtils.copyPropertiesIgnoreNull(fileLibraryDto,fileLibrary);
        fileLibrary.setFileId(UUID.randomUUID().toString());
        fileLibrary.setCreatedBy(SessionUtil.getLoginName());
        fileLibrary.setCreatedDate(new Date());
        fileLibrary.setModifiedBy(SessionUtil.getLoginName());
        fileLibrary.setModifiedDate(new Date());
        if(Constant.folderType.FILE_LIBRARY.getValue().equals(libraryType)){
            fileLibrary.setFileType(Constant.folderType.FILE_LIBRARY.getValue());
        }
        if(Constant.folderType.POLICY_LIBRARY.getValue().equals(libraryType)){
            fileLibrary.setFileType(Constant.folderType.POLICY_LIBRARY.getValue());
        }
        fileLibrary.setFileUrl(findFileUrlById(fileLibraryDto.getParentFileId()) + fileUrl);
        fileLibrary.setFileNature(Constant.fileNatrue.FILE_TYPE.getValue());
        fileLibraryRepo.save(fileLibrary);
        FileLibraryDto fld = new FileLibraryDto();
        fld.setFileId(fileLibrary.getFileId());
        return fld;
        }else{
            throw new IllegalArgumentException(String.format("文件：%s 已经存在，请重新输入",fileLibraryDto.getFileName()));
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
    public void updateFile(FileLibraryDto fileLibraryDto) {
        FileLibrary fileLibrary = fileLibraryRepo.getById(fileLibraryDto.getFileId());
        BeanCopierUtils.copyProperties(fileLibraryDto,fileLibrary);
        fileLibrary.setModifiedDate(new Date());
        fileLibrary.setModifiedBy(SessionUtil.getLoginName());
        fileLibraryRepo.save(fileLibrary);
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

    @Override
    public void deleteRootDirectory(String parentFileId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("delete from " + FileLibrary.class.getSimpleName() + " f1 where not exists (");
        hqlBuilder.append("select 1 from "+ FileLibrary.class.getSimpleName() + " f2 where f2." + FileLibrary_.parentFileId.getName() + "=:parentFileId )");
        hqlBuilder.append(" and f1."+ FileLibrary_.fileId.getName() + "=:fileId");
        hqlBuilder.setParam("parentFileId",parentFileId);
        hqlBuilder.setParam("fileId",parentFileId);
        int count = fileLibraryRepo.executeHql(hqlBuilder);
        if(count <=0){
            throw new IllegalArgumentException(String.format("该根目录包含子目录，不能将其删除！"));
        }
    }

    /**
     * 初始化文件列表
     * @param oDataObj
     * @param fileId
     * @return
     */
    @Override
    public PageModelDto<FileLibraryDto> initFileList(ODataObj oDataObj, String fileId) {
        PageModelDto<FileLibraryDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = fileLibraryRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(FileLibrary_.parentFileId.getName(),fileId));
        criteria.add(Restrictions.eq(FileLibrary_.fileNature.getName(),Constant.fileNatrue.FILE_TYPE.getValue()));
        List<FileLibrary> fileLibraryList = criteria.list();
        //统计总数
        Integer totalResult=((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if(oDataObj.getSkip() > 0){
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if(oDataObj.getTop() > 0){
            criteria.setMaxResults(oDataObj.getTop());
        }

        //处理orderby
        if(Validate.isString(oDataObj.getOrderby())){
            if(oDataObj.isOrderbyDesc()){
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
            }else{
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
            }
        }
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
        pageModelDto.setValue(fileLibraryDtoList);

        return pageModelDto;
    }
}