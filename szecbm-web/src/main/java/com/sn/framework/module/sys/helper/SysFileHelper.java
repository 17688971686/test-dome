package com.sn.framework.module.sys.helper;

import com.google.common.collect.Lists;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.util.BeanCopierUtils;
import com.sn.framework.core.util.SysFileUtil;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.model.SysFileDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/11 0011.
 */
public class SysFileHelper {

    private SysFile sysFile;

    private List<SysFile> sysFileList;

    protected SysFileHelper(SysFile sysFile) {
        this.sysFile = sysFile;
    }
    protected SysFileHelper(List<SysFile> sysFileList) {
        this.sysFileList = sysFileList;
    }

    public static SysFileHelper create(SysFile sysFile) {
        return new SysFileHelper(sysFile);
    }

    public static SysFileHelper create(List<SysFile> sysFileList) {
        return new SysFileHelper(sysFileList);
    }

    public List<SysFileDto> listTransToDto(){
        List<SysFileDto> sysFileDtoList = Lists.newArrayList();
        if (Validate.isList(sysFileList)) {
            sysFileDtoList = sysFileList.stream().map(item->{
                SysFileDto sysFileDto = new SysFileDto();
                sysFileDto.setFileSizeStr(SysFileUtil.getFileSize(item.getFileSize()));
                BeanCopierUtils.copyPropertiesIgnoreProps(item,sysFileDto);
                return sysFileDto;
            }).collect(Collectors.toList());
        }
        return sysFileDtoList;
    }
}
