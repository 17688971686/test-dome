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
import org.apache.lucene.util.RamUsageEstimator;
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
            fld.setFileId(fileLibrary.getFileId());
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
     * 初始化文件夹下的所有文件
     * @param oDataObj
     * @param  fileId
     * @return
     */
    @Override
    public PageModelDto<FileLibraryDto> initFileList(ODataObj oDataObj , String fileId) {
        PageModelDto<FileLibraryDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = fileLibraryRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(FileLibrary_.parentFileId.getName() , fileId));
        criteria.add(Restrictions.eq(FileLibrary_.fileNature.getName(),Constant.libraryType.FILE_TYPE.getValue()));
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