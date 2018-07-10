package com.sn.framework.module.sys.service;

import java.io.IOException;

/**
 * Description: 
 * @author: tzg
 * @date: 2017/5/17 19:22
 */
public interface ISysService {

    /**
     * 系统初始化
     * @return
     */
    boolean sysInit();

    /**
     * 根据数据文件参数化数据
     * @param fileName
     */
    void initData(String fileName);

    /**
     * 创建视图
     * @throws IOException
     */
    void createView() throws IOException;

}