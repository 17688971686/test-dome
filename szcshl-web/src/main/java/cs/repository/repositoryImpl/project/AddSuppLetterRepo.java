package cs.repository.repositoryImpl.project;

import cs.domain.project.AddSuppLetter;
import cs.repository.IRepository;

import java.util.Date;

/**
 * Description: 项目资料补充函 数据操作实现接口
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
public interface AddSuppLetterRepo extends IRepository<AddSuppLetter, String> {
    boolean isHaveSuppLetter(String businessId);

    /**
     * 根据业务类型，查询最大序号
     * @param fileType
     * @return
     */
    Integer findybMaxSeq(String fileType);
}
