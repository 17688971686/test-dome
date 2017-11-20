package cs.repository.repositoryImpl.monthly;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 月报简报 数据操作实现类
 * author: ldm
 * Date: 2017-9-8 11:23:41
 */
@Repository
public class MonthlyNewsletterRepoImpl extends AbstractRepository<MonthlyNewsletter, String> implements MonthlyNewsletterRepo {
    /**
     * 查询年度
     * @param businessId
     * @return
     */
    @Deprecated
    @Override
    public  String findYear(String businessId){
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(MonthlyNewsletter_.id.getName(),businessId));
        List<MonthlyNewsletter> monthlyNewsletterList=criteria.list();
        String year=monthlyNewsletterList.get(0).getReportMultiyear();
        return year;
    }

    /**
     * 更改月报简报状态
     * @param id
     * @param value
     * @return
     */
    @Override
    public ResultMsg updateMonthlyType(String id, String value) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+MonthlyNewsletter.class.getSimpleName()+" set "+MonthlyNewsletter_.monthlyType.getName()+"=:monthlyType ");
        hqlBuilder.setParam("monthlyType",value);
        hqlBuilder.bulidPropotyString("where",MonthlyNewsletter_.id.getName(),id);
        executeHql(hqlBuilder);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
    }
}