package cs.threadtask;

import cs.domain.sys.Log;
import cs.model.sys.SysFileDto;
import cs.service.sys.SysFileService;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 附件下载定时器
 * @author: ldm
 * @date: 2019-03-07 上午 9:38
 * @description:
 */
public class FileDownLoadThread implements Runnable{

    /**
     * 附件队列,用ConcurrentLinkedQueue保证线程安全
     */
    private ConcurrentLinkedQueue<SysFileDto> fileQueue;
    /**
     * service服务器
     */
    private SysFileService sysFileService;

    private String businessId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 附件大类
     */
    private String mainType;
    /**
     * 附件小类
     */
    private String busiType;
    /**
     * 操作日记信息
     */
    private Log log;

    public FileDownLoadThread(SysFileService sysFileService,ConcurrentLinkedQueue<SysFileDto> fileQueue,String businessId,
                              String userId, String mainType, String busiType,Log log) {
        this.sysFileService = sysFileService;
        this.fileQueue = fileQueue;
        this.businessId = businessId;
        this.userId = userId;
        this.mainType = mainType;
        this.busiType = busiType;
        this.log = log;
    }

    @Override
    public void run() {
        sysFileService.downRemoteFileList(businessId, fileQueue, userId, mainType, busiType,log);
    }
}
