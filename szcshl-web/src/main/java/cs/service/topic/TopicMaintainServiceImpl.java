package cs.service.topic;

import cs.ahelper.projhelper.ProjUtil;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.domain.topic.TopicMaintain;
import cs.domain.topic.TopicMaintain_;
import cs.model.topic.TopicMaintainDto;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import cs.repository.repositoryImpl.topic.TopicMaintainRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 课题维护 业务操作实现类
 * author: zsl
 * Date: 2018-6-22 15:40:48
 */
@Service
public class TopicMaintainServiceImpl implements TopicMaintainService {
    @Autowired
    private TopicMaintainRepo topicMaintainRepo;

    @Autowired
    private TopicInfoRepo topicInfoRepo;

    /**
     * 课题详情信息，自定义维护信息和课题流程信息
     * @return
     */
    @Override
    public List<TopicMaintainDto> findTopicAll(String userId,String year,String quarter) {
        String[] queryTimes = ProjUtil.getQueryTime(year,quarter);
        String beginTime = queryTimes[0],endTime = queryTimes[1];

        //1、查询课题维护信息
        Criteria criteria = topicMaintainRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(TopicMaintain_.userId.getName(),userId));
        criteria.add(Restrictions.ge(TopicMaintain_.endTime.getName(), DateUtils.converToDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
        criteria.add(Restrictions.le(TopicMaintain_.endTime.getName(), DateUtils.converToDate(endTime,DateUtils.DATE_TIME_PATTERN)));
        List<TopicMaintain> topicMaintainList = criteria.list();

        //2、查询课题流程的信息
        Criteria criteria1 = topicInfoRepo.getExecutableCriteria();
        criteria1.add(Restrictions.eq(TopicInfo_.state.getName(), Constant.EnumState.YES.getValue()));
        criteria1.add(Restrictions.isNull(TopicInfo_.isMaintain.getName()));
        criteria.add(Restrictions.ge(TopicInfo_.endTime.getName(), DateUtils.converToDate(beginTime,DateUtils.DATE_TIME_PATTERN)));
        criteria.add(Restrictions.le(TopicInfo_.endTime.getName(), DateUtils.converToDate(endTime,DateUtils.DATE_TIME_PATTERN)));
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append(" (select count(p.BUSIID) from cs_flow_principal p where p.BUSIID = " + criteria1.getAlias() + "_.ID AND P.USERID = ?)>0");
        criteria1.add(Restrictions.sqlRestriction(sqlSB.toString(),userId, StringType.INSTANCE));

        sqlSB.setLength(0);
        sqlSB.append(" (select count(f.topid) from cs_topic_filing f where f.topid = this_.ID ");
        sqlSB.append(" and extract(year from f.filingdate) = extract(year from sysdate) )>0 ");
        criteria1.add(Restrictions.sqlRestriction(sqlSB.toString()));
        List<TopicInfo> topicInfoList =  criteria1.list();
        //3、合并课题信息
        List<TopicMaintainDto> topicMaintainDtoList = new ArrayList<TopicMaintainDto>();
        topicInfoList.forEach(topicInfo -> {
            TopicMaintainDto topicMaintainDto = new TopicMaintainDto();
            topicMaintainDto.setTopicId(topicInfo.getId());
            topicMaintainDto.setTopicName(topicInfo.getTopicName());
            topicMaintainDto.setBusinessType("1");
            topicMaintainDto.setEndTime(topicInfo.getFiling().getFilingDate());
            topicMaintainDtoList.add(topicMaintainDto);
        });
        topicMaintainList.forEach(topicMaintain -> {
            TopicMaintainDto topicMaintainDto = new TopicMaintainDto();
            BeanCopierUtils.copyProperties(topicMaintain,topicMaintainDto);
            topicMaintainDtoList.add(topicMaintainDto);
        });
        return topicMaintainDtoList;
    }
}