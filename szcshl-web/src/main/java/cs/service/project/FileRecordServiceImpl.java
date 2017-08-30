package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.FileRecord;
import cs.domain.project.FileRecord_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.sys.User;
import cs.model.project.FileRecordDto;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public ResultMsg save(FileRecordDto fileRecordDto) {
        if (Validate.isString(fileRecordDto.getSignId())) {
            FileRecord fileRecord = null;

            Date now = new Date();
            if (!Validate.isString(fileRecordDto.getFileRecordId())) {
                fileRecord = new FileRecord();
                fileRecordDto.setFileDate(fileRecordDto.getFileDate() == null ? now : fileRecordDto.getFileDate());
                fileRecordDto.setPrintDate(fileRecordDto.getPrintDate() == null ? now : fileRecordDto.getPrintDate());

                BeanCopierUtils.copyProperties(fileRecordDto, fileRecord);
                fileRecord.setFileRecordId(UUID.randomUUID().toString());
                fileRecord.setCreatedBy(SessionUtil.getLoginName());
                fileRecord.setCreatedDate(now);
                fileRecord.setFileDate(now);

                fileRecordDto.setFileRecordId(fileRecord.getFileRecordId());
            } else {
                fileRecord = fileRecordRepo.findById(fileRecordDto.getFileRecordId());
                BeanCopierUtils.copyPropertiesIgnoreNull(fileRecordDto, fileRecord);
            }
            fileRecord.setModifiedBy(SessionUtil.getLoginName());
            fileRecord.setModifiedDate(now);

            //获取收文信息
            Sign sign = signRepo.findById(Sign_.signid.getName(),fileRecordDto.getSignId());
            //设置归档编号
            if (!Validate.isString(fileRecord.getFileNo())) {
                String fileNumValue = "";
                int maxSeq = findCurMaxSeq(fileRecord.getFileDate());
                if(maxSeq < 1000){
                    fileNumValue = String.format("%03d", Integer.valueOf(maxSeq+1));
                }else{
                    fileNumValue = (maxSeq+1)+"";
                }
                //归档编号=发文年份+档案类型+存档年份+存档顺序号
                fileNumValue = DateUtils.converToString(sign.getExpectdispatchdate(),"yyyy")+ ProjectUtils.getFileRecordTypeByStage(sign.getReviewstage())
                        +DateUtils.converToString(fileRecord.getFileDate(),"yy")+fileNumValue;
                fileRecord.setFileNo(fileNumValue);
            }
            //更新收文信息
            sign.setFilenum(fileRecord.getFileNo());
            sign.setFileRecord(fileRecord);
            sign.setProcessState(Constant.SignProcessState.DO_FILE.getValue());
            fileRecord.setSign(sign);

            fileRecordRepo.save(fileRecord);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！",fileRecordDto);
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
        //根据属性查找ID
        FileRecord fileRecord = fileRecordRepo.findById("signid",signid);
        if (fileRecord != null && Validate.isString(fileRecord.getFileRecordId())) {
            BeanCopierUtils.copyProperties(fileRecord, fileRecordDto);
        } else {
            //如果是新增，则要初始化
            User priUser = signPrincipalService.getMainPriUser(signid);
           
            Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
            fileRecordDto.setProjectName(sign.getProjectname());
            fileRecordDto.setProjectCode(sign.getProjectcode());
            fileRecordDto.setFileReviewstage(sign.getReviewstage());//评审阶段
            fileRecordDto.setProjectCompany(sign.getBuiltcompanyName());//编制单位名称
            fileRecordDto.setFileNumber(sign.getDocnum());//文号
            //项目是否曾经暂停
            fileRecordDto.setIsStachProject(sign.getIsProjectState()==null? Constant.EnumState.NO.getValue():sign.getIsProjectState());
            fileRecordDto.setProjectChargeUser(priUser == null ? "" : priUser.getDisplayName());
            //设置默认文件标题
            String fileTitle = "《";
            fileTitle += sign.getProjectname() == null ? "" : sign.getProjectname();
            fileTitle += (sign.getReviewstage() == null ? "" : sign.getReviewstage());
            fileTitle += "》";
            fileTitle += (sign.getIsAdvanced() == null ? "" : sign.getIsAdvanced());
            fileRecordDto.setFileTitle(fileTitle);


        }
        return fileRecordDto;
    }

    /**
     * 根据归档日期，获取存档顺序号
     * @param fileRecordDate
     * @return
     */
    private int findCurMaxSeq(Date fileRecordDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+ FileRecord_.fileSeq.getName()+") from cs_file_record where TO_CHAR("+FileRecord_.fileDate.getName()+",'yyyy') = :fileRecordDate ");
        sqlBuilder.setParam("fileRecordDate", DateUtils.converToString(fileRecordDate,"yyyy"));
        return fileRecordRepo.returnIntBySql(sqlBuilder);
    }
}
