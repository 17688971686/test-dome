package cs.mobile.service;

import cs.common.Constant;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.model.PageModelDto;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 项目统计业务层
 * Author: mcl
 * Date: 2018/3/3 12:53
 */
@Service
public class ProStatisticsServiceImpl implements ProStatisticsService {

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;

    /**
     * 获取项目查询列表，通过当前页数和每页显示条数进行分页查询
     * @param pageNum
     * @param pageSize
     * @param search
     * @returnList
     */
    @Override
    public List<SignDispaWork> getSignList(int pageNum, int pageSize , String search) {


        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();

        //排除未正式签收的项目
        criteria.add(Restrictions.eq(SignDispaWork_.issign.getName() , Constant.EnumState.YES.getValue()));

        //排除已删除的项目
        criteria.add(Restrictions.ne(SignDispaWork_.signState.getName() , Constant.EnumState.DELETE.getValue()));

        if(Validate.isString(search)){
            criteria.add(Restrictions.like(SignDispaWork_.projectname.getName() , "%" + search.trim()  + "%"));
        }

        //设置查询的从第几条到第几条
        criteria.setFirstResult((pageNum-1)*pageSize);
        criteria.setMaxResults(pageNum*pageSize);

        return criteria.list();
    }
}