package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Message;
import cs.repository.IRepository;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
public interface MessageRepo extends IRepository<Message,String> {
    /**
     * 删除短信接收人信息
     * @param messageId
     */
    void deleteMsgRecvList(String messageId);
}
