package cs.repository.repositoryImpl.project;

import cs.domain.project.UnitScore;
import cs.domain.sys.Company;
import cs.model.project.UnitScoreDto;
import cs.repository.IRepository;

public interface UnitScoreRepo extends IRepository<UnitScore, String> {
    /**
     * 根据项目id查询单位评分
     * @param signid
     */
    UnitScore findUnitScore(String signid);
}
