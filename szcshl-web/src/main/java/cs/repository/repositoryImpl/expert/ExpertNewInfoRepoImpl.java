package cs.repository.repositoryImpl.expert;

import cs.domain.expert.ExpertNewInfo;
import cs.domain.expert.ExpertOffer;
import cs.model.expert.ExpertNewInfoDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 专家聘书 数据操作实现类
 * author: ldm
 * Date: 2017-6-19 19:13:35
 */
@Repository
public class ExpertNewInfoRepoImpl extends AbstractRepository<ExpertNewInfo, String> implements ExpertNewInfoRepo {
}