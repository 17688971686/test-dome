package cs.repository.repositoryImpl.project;

import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.model.project.AddRegisterFileDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 登记补充资料 数据操作实现类
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
@Repository
public class AddRegisterFileRepoImpl extends AbstractRepository<AddRegisterFile, String> implements AddRegisterFileRepo {

    /**
     * 根据业务ID查询
     * @param businessId
     * @return
     */
    @Override
    public List<AddRegisterFile> findByBusinessId(String businessId) {
        return findByIds(AddRegisterFile_.businessId.getName(),businessId,null);
    }
}