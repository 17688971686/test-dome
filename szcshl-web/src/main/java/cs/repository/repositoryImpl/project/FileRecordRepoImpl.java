package cs.repository.repositoryImpl.project;


import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.project.FileRecord_;
import org.springframework.stereotype.Repository;

import cs.domain.project.FileRecord;
import cs.repository.AbstractRepository;

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

}
