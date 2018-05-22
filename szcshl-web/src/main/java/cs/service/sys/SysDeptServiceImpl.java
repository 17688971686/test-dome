package cs.service.sys;


import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sys.SysDept;
import cs.domain.sys.SysDept_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.SysDeptDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.SysDeptRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysDeptServiceImpl implements SysDeptService {
    private static Logger logger = Logger.getLogger(SysDeptServiceImpl.class);

    @Autowired
    private SysDeptRepo sysDeptRepo;

    /**
     * 分页查询
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SysDeptDto> get(ODataObj odataObj) {
        List<SysDept> list = sysDeptRepo.findByOdata(odataObj);
        List<SysDeptDto> dtoList = new ArrayList<>(list == null?0:list.size());
        list.forEach( sd ->{
            SysDeptDto sysDeptDto = new SysDeptDto();
            BeanCopierUtils.copyProperties(sd,sysDeptDto);
            dtoList.add(sysDeptDto);
        });
        PageModelDto<SysDeptDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(dtoList);
        return pageModelDto;
    }

    /**
     * 保存组（内部部门信息）
     * @param sysDeptDto
     * @return
     */
    @Override
    public ResultMsg save(SysDeptDto sysDeptDto) {
        SysDept sysDept = new SysDept();
        if(Validate.isString(sysDeptDto.getId())){
            sysDept = sysDeptRepo.findById(SysDept_.id.getName(), sysDeptDto.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(sysDeptDto, sysDept);
            //清除已有的用户
            sysDept.getUserList().clear();
        }else{
            BeanCopierUtils.copyProperties(sysDeptDto, sysDept);
        }

        //添加部门用户
        for (UserDto userDto : sysDeptDto.getUserDtoList()) {
            User user = new User();
            user.setId(userDto.getId());
            user.setDisplayName(userDto.getDisplayName());
            sysDept.getUserList().add(user);
        }
        sysDeptRepo.save(sysDept);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"添加成功！");
    }

    /**
     * 根据ID查询部门信息
     * @param id
     * @return
     */
    @Override
    public SysDeptDto findById(String id) {
        SysDeptDto sysDeptDto = new SysDeptDto();
        if(Validate.isString(id)){
            SysDept sysDept = sysDeptRepo.findById(SysDept_.id.getName(),id);
            BeanCopierUtils.copyProperties(sysDept,sysDeptDto);

            if(Validate.isList(sysDept.getUserList())){
                List<UserDto> userDtoList = new ArrayList<>();
                sysDept.getUserList().forEach(ul->{
                    UserDto userDto = new UserDto();
                    BeanCopierUtils.copyProperties(ul,userDto);
                    userDtoList.add(userDto);
                });
                sysDeptDto.setUserDtoList(userDtoList);
            }
        }
        return sysDeptDto;
    }

    /**
     * 删除部门信息
     * @param id
     * @return
     */
    @Override
    public ResultMsg deleteSysDept(String id) {
        sysDeptRepo.deleteById(SysDept_.id.getName(),id);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
    }

    /**
     * 获取已经分配的部门用户
     * @param id
     * @return
     */
    @Override
    public PageModelDto<UserDto> getSysDeptUsers(String id) {
        return null;
    }

    /**
     * 获取非该部门的用户信息
     * @param id
     * @return
     */
    @Override
    public PageModelDto<UserDto> getUserNotInSysDept(String id) {
        return null;
    }

    @Override
    public void removeSysDeptUser(String id, String userId) {

    }

    @Override
    public void addUserToSysDept(String id, String userId) {

    }


}
