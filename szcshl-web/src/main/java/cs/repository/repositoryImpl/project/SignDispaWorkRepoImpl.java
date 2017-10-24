package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.expert.*;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.model.PageModelDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 项目统计视图 数据操作实现类
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
@Repository
public class SignDispaWorkRepoImpl extends AbstractRepository<SignDispaWork, String> implements SignDispaWorkRepo {
    @Override
    public PageModelDto<SignDispaWork> reviewProject(String expertId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + SignDispaWork.class.getSimpleName() + " where " + SignDispaWork_.signid.getName() + " in (");
        hqlBuilder.append(" select " + ExpertReview_.businessId.getName() + " from " + ExpertReview.class.getSimpleName() + " where " + ExpertReview_.id.getName() + " in (");
        hqlBuilder.append(" select " + ExpertSelected_.expertReview.getName() + "." + ExpertReview_.id.getName() + " from " + ExpertSelected.class.getSimpleName() + " where ");
        hqlBuilder.append(" " + ExpertSelected_.isConfrim.getName() + "=:isConfrim");
        hqlBuilder.append(" and " + ExpertSelected_.isJoin.getName() + "=:isJoin");
        hqlBuilder.append(" and " + ExpertSelected_.expert.getName() + "." + Expert_.expertID.getName() + "=:expertId))");
        hqlBuilder.setParam("isConfrim" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("isJoin" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("expertId" , expertId);

        List<SignDispaWork> signDispaWorkList =this.findByHql(hqlBuilder);
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        pageModelDto.setValue(signDispaWorkList);
        pageModelDto.setCount(signDispaWorkList.size());
        return pageModelDto;
    }
}