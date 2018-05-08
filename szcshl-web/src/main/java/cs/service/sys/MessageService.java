package cs.service.sys;

import cs.common.ResultMsg;
import cs.model.sys.MessageDto;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
public interface MessageService {

    ResultMsg save(MessageDto record);
}
