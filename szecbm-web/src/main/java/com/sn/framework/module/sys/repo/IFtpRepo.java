package com.sn.framework.module.sys.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.Ftp;

/**
 * Created by Administrator on 2018/7/11 0011.
 */
public interface IFtpRepo extends IRepository<Ftp,String> {
    /**
     * 根据IP查询
     * @param enabledIP
     * @return
     */
    Ftp getByIP(String enabledIP);
}
