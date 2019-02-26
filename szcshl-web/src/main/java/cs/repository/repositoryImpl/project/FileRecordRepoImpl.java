package cs.repository.repositoryImpl.project;


import cs.common.HqlBuilder;
import cs.common.constants.ProjectConstant;
import cs.common.utils.StringUtil;
import cs.domain.project.FileRecord;
import cs.domain.project.FileRecord_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * 项目归档dao层业务处理接口
 * @author ldm
 */
@Repository
public class FileRecordRepoImpl extends AbstractRepository<FileRecord, String> implements FileRecordRepo {

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
        return (resultInt > 0);

    }

    /**
     * 获取最大收文编号
     *
     * @param yearName
     * @param seqType
     * @return
     */
    @Override
    public int getMaxSeq(String yearName, String seqType) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + FileRecord_.fileSeq.getName() + ") from cs_file_record ");
        //根据存档编号：的年份获取当前年份最大序号，而不是根据存档日期获取最大序号（update:2018-08-17）FILENO
        int yearNameL = yearName.length();
        sqlBuilder.append(" where fileNo like :fileNo ").setParam("fileNo", "%" + seqType + StringUtil.getSubString(yearName, yearNameL - 2, yearNameL) + "%");
        ProjectConstant.FILE_RECORD_KEY fileRecordKey = ProjectConstant.FILE_RECORD_KEY.valueOf(seqType);
        switch (fileRecordKey) {
            /**
             * 概算类
             */
            case GD:
                sqlBuilder.append(" and " + FileRecord_.fileReviewstage.getName() + " = :stage ");
                sqlBuilder.setParam("stage", ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode());
                break;
            /**
             * 设备类
             */
            case SD:
                sqlBuilder.append(" and (" + FileRecord_.fileReviewstage.getName() + " = :stage1 ");
                sqlBuilder.append(" or " + FileRecord_.fileReviewstage.getName() + " = :stage2 ");
                sqlBuilder.append(" or " + FileRecord_.fileReviewstage.getName() + " = :stage3)");
                sqlBuilder.setParam("stage1", ProjectConstant.REVIEW_STATE_ENUM.STAGEHOMELAND.getZhCode());
                sqlBuilder.setParam("stage2", ProjectConstant.REVIEW_STATE_ENUM.STAGEIMPORT.getZhCode());
                sqlBuilder.setParam("stage3", ProjectConstant.REVIEW_STATE_ENUM.STAGEDEVICE.getZhCode());
                break;
            /**
             * 评估类，资金申请报告、其它类,登记赋码
             */
            default:
                sqlBuilder.append(" and (" + FileRecord_.fileReviewstage.getName() + " = :stage1 ");
                sqlBuilder.append(" or " + FileRecord_.fileReviewstage.getName() + " = :stage2 ");
                sqlBuilder.append(" or " + FileRecord_.fileReviewstage.getName() + " = :stage3 ");
                sqlBuilder.append(" or " + FileRecord_.fileReviewstage.getName() + " = :stage4");
                sqlBuilder.append(" or " + FileRecord_.fileReviewstage.getName() + " = :stage5)");
                sqlBuilder.setParam("stage1", ProjectConstant.REVIEW_STATE_ENUM.REGISTERCODE.getZhCode());
                sqlBuilder.setParam("stage2", ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode());
                sqlBuilder.setParam("stage3", ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode());
                sqlBuilder.setParam("stage4", ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode());
                sqlBuilder.setParam("stage4", ProjectConstant.REVIEW_STATE_ENUM.STAGEOTHER.getZhCode());
        }
        return returnIntBySql(sqlBuilder);
    }

}
