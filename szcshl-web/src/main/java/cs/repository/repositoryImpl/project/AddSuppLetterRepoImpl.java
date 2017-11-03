package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Description: 项目资料补充函 数据操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */


@Repository
public class AddSuppLetterRepoImpl extends AbstractRepository<AddSuppLetter, String> implements AddSuppLetterRepo {

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    /**
     * 根据业务ID判断是否有补充资料函
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveSuppLetter(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(AddSuppLetter_.businessId.getName(),businessId));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult > 0)?true:false;
    }

    /**
     * 根据业务类型，查询最大序号
     * @param fileType
     * @return
     */
    @Override
    public  Integer findybMaxSeq(String fileType){
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + AddSuppLetter_.MonthlySeq.getName() + ") from cs_add_suppLetter" );
        sqlBuilder.append(" where " + AddSuppLetter_.fileType.getName() + " =:fileType ").setParam("fileType", fileType);
        return addSuppLetterRepo.returnIntBySql(sqlBuilder);
    }

}