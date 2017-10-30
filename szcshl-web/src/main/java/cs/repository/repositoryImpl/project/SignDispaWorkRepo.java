package cs.repository.repositoryImpl.project;

import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.repository.IRepository;

import java.util.List;
import java.util.Map;

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

    /**
     * 通过时间段 获取项目信息，用于项目查询统计分析
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String , Object[]>> findByTime(String startTime , String endTime);

}
