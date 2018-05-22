package cs.service.sys;

import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Message;
import cs.domain.sys.MessageRecv;
import cs.domain.sys.User;
import cs.model.sys.MessageDto;
import cs.model.sys.MessageRecvDto;
import cs.repository.repositoryImpl.sys.MessageRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.sharing.SharingPlatlformServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
@Service
public class MessageServiceImpl implements MessageService{
    private static Logger logger = Logger.getLogger(SharingPlatlformServiceImpl.class);
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public ResultMsg save(MessageDto record) {
        try{
            List<MessageRecvDto> recvDtoList = record.getMessageRecvDtoList();
            if(!Validate.isList(recvDtoList)){
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"没有选择接收人！");
            }else{
                Message domain = new Message();
                Date now = new Date();
                //1、如果有ID，则要先删除之前的权限
                if(!Validate.isString(record.getId())){
                    domain.setId(UUID.randomUUID().toString());
                    domain.setCreatedBy(SessionUtil.getUserId());
                    domain.setCreatedDate(now);
                }else{
                    //先删除负责人
                    messageRepo.deleteMsgRecvList(record.getId());
                    messageRepo.findById(record.getId());
                }
                BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
                domain.setModifiedBy(SessionUtil.getDisplayName());
                domain.setModifiedDate(now);

                List<MessageRecv> recvList = new ArrayList<>();
                for(MessageRecvDto messageRecvDto : recvDtoList){
                    MessageRecv messageRecv = new  MessageRecv();
                    User user = userRepo.getCacheUserById(messageRecvDto.getUserId());
                    BeanCopierUtils.copyProperties(messageRecvDto,messageRecv);
                    messageRecv.setUserName(user.getDisplayName());
                    messageRecv.setPhoneNum(user.getUserMPhone());
                    recvList.add(messageRecv);
                }
                domain.setMessageRecvList(recvList);

                messageRepo.save(domain);
            }
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"保存成功！");
        }catch (Exception e){
            logger.error("保存信息错误！");
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败！");
        }

    }
}
