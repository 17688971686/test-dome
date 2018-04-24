package cs.repository.repositoryImpl.topic;

import cs.common.HqlBuilder;
import cs.domain.topic.Filing;
import cs.domain.topic.Filing_;
import cs.domain.topic.TopicInfo_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 课题归档 数据操作实现类
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
@Repository
public class FilingRepoImpl extends AbstractRepository<Filing, String> implements FilingRepo {

    /**
     * 生成最大收文编号
     * @param yearName
     * @return
     */
    @Override
    public int findCurMaxSeq(String yearName) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + TopicInfo_.topicSeq.getName() + ") from cs_topic_filing where to_char(" + Filing_.filingDate.getName()+" , 'yyyy') = :yearName ");
        sqlBuilder.setParam("yearName",yearName);
        return returnIntBySql(sqlBuilder);

    }
}