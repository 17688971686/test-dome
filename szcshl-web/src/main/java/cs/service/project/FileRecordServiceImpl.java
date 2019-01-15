package cs.service.project;

import cs.common.RandomGUID;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.*;
import cs.domain.sys.User;
import cs.model.project.AddRegisterFileDto;
import cs.model.project.FileRecordDto;
import cs.repository.repositoryImpl.project.AddRegisterFileRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileRecordServiceImpl implements FileRecordService {
    private static Logger log = Logger.getLogger(FileRecordServiceImpl.class);
    @Autowired
    private FileRecordRepo fileRecordRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;

    @Autowired
    private AddRegisterFileRepo addRegisterFileRepo;

    @Override
    @Transactional
    public ResultMsg save(FileRecordDto fileRecordDto) {
        if (Validate.isString(fileRecordDto.getSignId())) {
            FileRecord fileRecord = null;

            Date now = new Date();
            if (!Validate.isString(fileRecordDto.getFileRecordId())) {
                fileRecord = new FileRecord();
//                fileRecordDto.setFileDate(fileRecordDto.getFileDate() == null ? now : fileRecordDto.getFileDate());
                fileRecordDto.setPrintDate(fileRecordDto.getPrintDate() == null ? now : fileRecordDto.getPrintDate());
                BeanCopierUtils.copyProperties(fileRecordDto, fileRecord);
                fileRecord.setFileRecordId((new RandomGUID()).valueAfterMD5);
                fileRecord.setCreatedBy(SessionUtil.getLoginName());
                fileRecord.setCreatedDate(now);
//                fileRecord.setFileDate(now);
                fileRecordDto.setFileRecordId(fileRecord.getFileRecordId());
            } else {
                fileRecord = fileRecordRepo.findById(fileRecordDto.getFileRecordId());
                BeanCopierUtils.copyProperties(fileRecordDto,fileRecord);
                //先删除拟补充资料函
//                addRegisterFileRepo.deleteById(AddRegisterFile_.businessId.getName(), fileRecordDto.getSignId());
            }
            fileRecord.setModifiedBy(SessionUtil.getUserId());
            fileRecord.setModifiedDate(now);

            //获取收文信息
            Sign sign = signRepo.findById(Sign_.signid.getName(),fileRecordDto.getSignId());

            //更新收文信息
            sign.setFilenum(fileRecord.getFileNo());
            sign.setFileRecord(fileRecord);
            //sign.setProcessState(Constant.SignProcessState.DO_FILE.getValue()); //不用这个状态，用不到
            fileRecord.setSign(sign);

            //5、添加拟补充资料函
           /* if (Validate.isList(fileRecordDto.getRegisterFileDto())) {
                List<AddRegisterFile> registerFileList = new ArrayList<>(fileRecordDto.getRegisterFileDto().size());
                for (AddRegisterFileDto fdto : fileRecordDto.getRegisterFileDto()) {
                    AddRegisterFile addRegisterFile = new AddRegisterFile();
                    BeanCopierUtils.copyProperties(fdto, addRegisterFile);
                    addRegisterFile.setBusinessId(fileRecordDto.getSignId());
                    addRegisterFile.setCreatedBy(SessionUtil.getUserId());
                    addRegisterFile.setCreatedDate(now);
                    addRegisterFile.setModifiedBy(SessionUtil.getUserId());
                    addRegisterFile.setModifiedDate(now);
                    registerFileList.add(addRegisterFile);
                    BeanCopierUtils.copyProperties(addRegisterFile, fdto);
                }
                addRegisterFileRepo.bathUpdate(registerFileList);
            }*/

           //存档日期是确认存档环节，档案员确认存档后才生成的时间
           if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.FILER.getValue())){
               fileRecord.setFileDate(now);
           }
            fileRecordRepo.save(fileRecord);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",fileRecord.getFileRecordId());
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取项目信息！");
        }

    }

    /**
     * 根据收文ID初始化归档信息
     *
     * @param signid
     * @return
     */
    @Override
    public FileRecordDto initBySignId(String signid) {
        FileRecordDto fileRecordDto = new FileRecordDto();
        List<AddRegisterFileDto> dtoList = new ArrayList<>();
        //根据属性查找ID
        FileRecord fileRecord = fileRecordRepo.findById("signid",signid);
        if (fileRecord != null && Validate.isString(fileRecord.getFileRecordId())) {
            BeanCopierUtils.copyProperties(fileRecord, fileRecordDto);
            //查询补充资料函信息
            List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(),signid,null);
            if(Validate.isList(registerFileList)){
                registerFileList.forEach(rgf -> {
                    AddRegisterFileDto dto = new AddRegisterFileDto();
                    BeanCopierUtils.copyProperties(rgf,dto);
                    dtoList.add(dto);
                });

            }
        } else {
            //如果是新增，则要初始化
            User priUser = signPrincipalService.getMainPriUser(signid);
           
            Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
            fileRecordDto.setSignId(signid);
            fileRecordDto.setProjectName(sign.getProjectname());
            fileRecordDto.setProjectCode(sign.getProjectcode());
            fileRecordDto.setFileReviewstage(sign.getReviewstage());//评审阶段
            fileRecordDto.setProjectCompany(sign.getDesigncompanyName());//编制单位名称
            fileRecordDto.setFileNumber(sign.getDocnum());//文号
            //项目是否曾经暂停
            fileRecordDto.setIsStachProject(sign.getIsProjectState()==null? Constant.EnumState.NO.getValue():sign.getIsProjectState());
           //是否有登记补充资料
            fileRecordDto.setIsSupplementary(sign.getIsSupplementary()==null? Constant.EnumState.NO.getValue():sign.getIsSupplementary() );
            fileRecordDto.setProjectChargeUser(priUser == null ? "" : priUser.getDisplayName());
            //设置默认文件标题
            String fileTitle = "《";
            fileTitle += sign.getProjectname() == null ? "" : sign.getProjectname();
            fileTitle += (sign.getReviewstage() == null ? "" : sign.getReviewstage());
            fileTitle += "》";
            fileTitle += (sign.getIsAdvanced() == null ? "" : sign.getIsAdvanced());
            fileRecordDto.setFileTitle(fileTitle);
            //是否协审
            fileRecordDto.setIsassistproc(sign.getIsassistproc());
        }

        fileRecordDto.setRegisterFileDto(dtoList);
        return fileRecordDto;
    }

}
