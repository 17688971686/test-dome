package cs.mobile.service;

import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;

import java.util.List;

/**
 * Created by MCL
 * 2018/3/3
 */
public interface ProStatisticsService {

    /**
     * 获取项目查询列表，通过当前页数和每页显示条数进行分页查询
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    List<SignDispaWork> getSignList(int pageNum , int pageSize , String search);
}
