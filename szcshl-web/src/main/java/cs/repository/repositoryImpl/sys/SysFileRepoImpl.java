package cs.repository.repositoryImpl.sys;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.FtpUtil;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Ftp;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.repository.AbstractRepository;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenning
 */
@Repository
public class SysFileRepoImpl extends AbstractRepository<SysFile, String> implements SysFileRepo {
    private static Logger logger = Logger.getLogger(SysFileRepoImpl.class);

    @Override
    public List<SysFile> findByMainId(String mainId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SysFile_.mainId.getName(), mainId));
        return criteria.list();
    }

    @Override
    public List<SysFile> queryFileList(String mainId, String sysBusiType) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SysFile_.mainId.getName(), mainId));
        criteria.add(Restrictions.eq(SysFile_.sysBusiType.getName(), sysBusiType));
        List<SysFile> sysFiles = criteria.list();
        return sysFiles;
    }

    /**
     * 删除附件记录信息（4个参数都不能为空，以免删除数据）
     *
     * @param mainId
     * @param businessId
     * @param sysBusiType
     * @param showName
     */
    @Override
    public void delete(String mainId, String businessId, String sysBusiType, String showName) {
        if (Validate.isString(mainId) && Validate.isString(businessId) && Validate.isString(sysBusiType) && Validate.isString(showName)) {
            Criteria criteria = getExecutableCriteria();
            criteria.add(Restrictions.eq(SysFile_.mainId.getName(), mainId));
            criteria.add(Restrictions.eq(SysFile_.businessId.getName(), businessId));
            criteria.add(Restrictions.eq(SysFile_.sysBusiType.getName(), sysBusiType));
            criteria.add(Restrictions.eq(SysFile_.showName.getName(), showName));
            List<SysFile> fileList = criteria.list();
            if (Validate.isList(fileList)) {
                StringBuffer deleteIds = new StringBuffer();
                for (int i = 0, l = fileList.size(); i < l; i++) {
                    SysFile sysFile = fileList.get(i);
                    try {
                        //删除ftp上的文件
                        Ftp f = sysFile.getFtp();
                        if (f != null && FtpUtil.connectFtp(f,false)) {
                            if (FtpUtil.removeFile(sysFile.getFileUrl())) {
                                //如果删除成功，则删除本地
                                if (deleteIds.length() > 0) {
                                    deleteIds.append(",");
                                }
                                deleteIds.append(sysFile.getSysFileId());
                            }
                        }
                    } catch (Exception e) {
                        logger.info("删除ftp附件【" + sysFile.getShowName() + "】异常：" + e.getMessage());
                    } finally {
                        FtpUtil.closeFtp();
                    }
                }
                if (deleteIds.length() > 0) {
                    deleteById(SysFile_.sysFileId.getName(), deleteIds.toString());
                }
            }
        }
    }

    /**
     * 根据附件ID删除附件
     *
     * @param sysFileId
     * @return
     */
    @Override
    public ResultMsg deleteByFileId(String sysFileId) {
        ResultMsg resultMsg ;
        if (Validate.isString(sysFileId)) {
            SysFile fl = findById(sysFileId);
            if (SessionUtil.getLoginName().equals(fl.getCreatedBy())) {
                try {
                    //删除ftp上的文件
                    Ftp f = fl.getFtp();
                    if (f == null || !FtpUtil.connectFtp(f,false) || !FtpUtil.removeFile(fl.getFileUrl())) {
                        resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"删除失败！无法连接文件服务器，请联系管理员查看！");
                    } else {
                        delete(fl);
                        resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(),"删除成功！");
                    }
                } catch (Exception e) {
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，错误信息已记录，请联系管理员查看！");
                    logger.info("删除ftp附件【" + fl.getShowName() + "】异常：" + e.getMessage());
                } finally {
                    FtpUtil.closeFtp();
                }
            }else{
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您无权删除此附件！");
            }
        }else{
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"没有删除的附件信息！");
        }
        return resultMsg;
    }
}
