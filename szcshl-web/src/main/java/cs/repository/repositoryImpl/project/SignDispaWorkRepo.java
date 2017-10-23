package cs.repository.repositoryImpl.project;

import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.repository.IRepository;

/**
 * Description: 项目统计视图 数据操作实现接口
 * author: ldm
 * Date: 2017-7-10 9:35:22
 */
public interface SignDispaWorkRepo extends IRepository<SignDispaWork, String> {

    /**
     * 通过专家id获取评审过的项目信息
     * @param expertId
     * @return
     */
    PageModelDto<SignDispaWork> reviewProject(String expertId);
}
