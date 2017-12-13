package cs.service.sys;

import cs.common.ResultMsg;
import cs.common.sysResource.SysResourceDto;

import java.util.List;

public interface SysService {

    List<SysResourceDto> get();

    ResultMsg SysInit();
}