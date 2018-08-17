package cs.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import cs.common.RandomGUID;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sys.Log_;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.domain.sys.Log;
import cs.model.PageModelDto;
import cs.model.sys.LogDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.LogRepo;

@Service
public class LogServiceImpl implements LogService {
    private static Logger logger = Logger.getLogger(LogServiceImpl.class);

    @Autowired
    private LogRepo logRepo;

    @Override
    @Transactional
    public PageModelDto<LogDto> get(ODataObj odataObj) {
        List<Log> logList = logRepo.findByOdata(odataObj);
        List<LogDto> logDtoList = new ArrayList<>();
        if (Validate.isList(logList)) {
            logList.forEach(ll -> {
                LogDto logDto = new LogDto();
                BeanCopierUtils.copyProperties(ll, logDto);
                logDtoList.add(logDto);
            });
        }
        PageModelDto<LogDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(logDtoList);

        logger.info("查询日志数据");
        return pageModelDto;
    }

    @Override
    public PageModelDto<LogDto> findFgwSignLog(ODataObj odataObj){
        PageModelDto<LogDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = logRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);

            criteria.add(Restrictions.like(Log_.module.getName(),"%委里推送数据接口%"));
        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        //处理orderby
        if (Validate.isString(odataObj.getOrderby())) {
            if (odataObj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(odataObj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(odataObj.getOrderby()).asc());
            }
        }
        List<Log> logList = criteria.list();
        List<LogDto> logDtoList = new ArrayList<>();
        if (Validate.isList(logList)) {
            logList.forEach(ll -> {
                LogDto logDto = new LogDto();
                BeanCopierUtils.copyProperties(ll, logDto);
                logDtoList.add(logDto);
            });
        }
        pageModelDto.setValue(logDtoList);
        return pageModelDto;
    }

    @Override
    public void save(Log log) {
        /*if(!Validate.isString(log.getId())){
            log.setId((new RandomGUID().valueAfterMD5));
        }*/
        logRepo.save(log);
    }
}
