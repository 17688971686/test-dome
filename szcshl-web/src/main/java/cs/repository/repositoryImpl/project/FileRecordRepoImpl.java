package cs.repository.repositoryImpl.project;


import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.FileRecord;
import cs.domain.project.FileRecord_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import static cs.common.Constant.*;

@Repository
public class FileRecordRepoImpl  extends AbstractRepository<FileRecord, String> implements FileRecordRepo {

    /**
     * 根据业务ID判断是否完成专家评分
     *
     * @param businessId
     * @return
     */
    @Override
    public boolean isFileRecord(String businessId) {
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append(" select count(*) from CS_FILE_RECORD t  where  signid =:signid ");
            sqlBuilder.setParam("signid", businessId);
            int resultInt = returnIntBySql(sqlBuilder);
            return (resultInt > 0) ? false : true;

    }

    /**
     * 获取最大收文编号
     * @param yearName
     * @param seqType
     * @return
     */
    @Override
    public int getMaxSeq(String yearName, String seqType) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+ FileRecord_.fileSeq.getName()+") from cs_file_record where to_char("+FileRecord_.fileDate.getName()+", 'yyyy') = :yearName ");
        sqlBuilder.setParam("yearName",yearName);
        //概算类
        if(Constant.FILE_RECORD_KEY.GD.getValue().equals(seqType)){
            sqlBuilder.append(" and "+FileRecord_.fileReviewstage.getName() +" = :stage ");
            sqlBuilder.setParam("stage",STAGE_BUDGET);
        //设备类
        }else if(Constant.FILE_RECORD_KEY.SD.getValue().equals(seqType)){
            sqlBuilder.append(" and ("+FileRecord_.fileReviewstage.getName() +" = :stage1 or "+FileRecord_.fileReviewstage.getName() +" = :stage2 or "+FileRecord_.fileReviewstage.getName() +" = :stage3)");
            sqlBuilder.setParam("stage1",DEVICE_BILL_HOMELAND).setParam("stage2",DEVICE_BILL_IMPORT).setParam("stage3",IMPORT_DEVICE);
        //评估类，资金申请报告、其它类
        }else{
            sqlBuilder.append(" and ("+FileRecord_.fileReviewstage.getName() +" = :stage1 or "+FileRecord_.fileReviewstage.getName() +" = :stage2 or "+FileRecord_.fileReviewstage.getName() +" = :stage3 or "+FileRecord_.fileReviewstage.getName() +" = :stage4)");
            sqlBuilder.setParam("stage1",STAGE_SUG).setParam("stage2",STAGE_STUDY).setParam("stage3",APPLY_REPORT).setParam("stage4",OTHERS);
        }
        return returnIntBySql(sqlBuilder);
    }

}
