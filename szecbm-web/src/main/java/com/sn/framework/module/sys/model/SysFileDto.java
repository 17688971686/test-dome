package com.sn.framework.module.sys.model;

import com.sn.framework.module.sys.domain.Ftp;
import com.sn.framework.module.sys.domain.SysFile;

/**
 * Created by Administrator on 2018/7/11 0011.
 */
public class SysFileDto extends SysFile {

    public SysFileDto(){
        super();
    }

    public SysFileDto(String sysFileId, String businessId, String fileUrl, String showName, Long fileSize, String fileType, String mainId, String mainType, String sysfileType, String sysBusiType) {
        super(sysFileId, businessId, fileUrl, showName, fileSize, fileType, mainId, mainType, sysfileType, sysBusiType);
    }

    public SysFileDto(String sysFileId, String businessId, String fileUrl, String showName, Long fileSize, String fileType, String mainId, String mainType, String sysfileType, String sysBusiType, Ftp ftp) {
        super(sysFileId, businessId, fileUrl, showName, fileSize, fileType, mainId, mainType, sysfileType, sysBusiType, ftp);
    }
    /**
     * 文件大小，主要用于显示
     */
    private String fileSizeStr;

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }
}
