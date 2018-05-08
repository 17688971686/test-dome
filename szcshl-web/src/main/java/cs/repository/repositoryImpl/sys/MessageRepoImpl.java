package cs.repository.repositoryImpl.sys;

import cs.common.HqlBuilder;
import cs.domain.sys.Message;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepoImpl extends AbstractRepository<Message, String> implements MessageRepo {

    @Override
    public void deleteMsgRecvList(String messageId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from CS_MESSAGE_RECV where MSGID = :messageId");
        sqlBuilder.setParam("messageId",messageId);
        executeSql(sqlBuilder);
    }

}
