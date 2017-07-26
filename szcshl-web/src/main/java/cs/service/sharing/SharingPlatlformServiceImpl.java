package cs.service.sharing;

import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sharing.SharingPlatlform;
import cs.domain.sharing.SharingPrivilege;
import cs.domain.sys.Org;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sharing.SharingPrivilegeDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sharing.SharingPlatlformRepo;
import cs.repository.repositoryImpl.sharing.SharingPrivilegeRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Description: 共享平台 业务操作实现类
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
@Service
public class SharingPlatlformServiceImpl implements SharingPlatlformService {

    private static Logger logger = Logger.getLogger(SharingPlatlformServiceImpl.class);
    @Autowired
    private SharingPlatlformRepo sharingPlatlformRepo;
    @Autowired
    private ICurrentUser currentUser;
    @Autowired
    private SharingPrivilegeService sharingPrivilegeService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrgRepo orgRepo;

    @Override
    public PageModelDto<SharingPlatlformDto> get(ODataObj odataObj) {
        PageModelDto<SharingPlatlformDto> pageModelDto = new PageModelDto<SharingPlatlformDto>();
        List<SharingPlatlform> resultList = sharingPlatlformRepo.findByOdata(odataObj);
        List<SharingPlatlformDto> resultDtoList = new ArrayList<SharingPlatlformDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                SharingPlatlformDto modelDto = new SharingPlatlformDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                modelDto.setCreatedDate(x.getCreatedDate());
                modelDto.setModifiedDate(x.getModifiedDate());

                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 保存共享资料信息
     *
     * @param record
     */
    @Override
    @Transactional
    public void save(SharingPlatlformDto record) {
        SharingPlatlform domain = new SharingPlatlform();
        Date now = new Date();
        BeanCopierUtils.copyProperties(record, domain);
        //1、如果有ID，则要先删除之前的权限
        if(!Validate.isString(domain.getSharId())){
            domain.setSharId(UUID.randomUUID().toString());
        }else{
            sharingPrivilegeService.deleteByShareId(domain.getSharId());
        }
        //如果不是发布给所有人看，则要添加相应的权限
        if(!EnumState.YES.getValue().equals(domain.getIsNoPermission())){
            List<SharingPrivilegeDto> privilegeDtoList = record.getPrivilegeDtoList();
            List<SharingPrivilege> privilegeList = new ArrayList<>();
            if(privilegeDtoList != null && privilegeDtoList.size() > 0){
                privilegeDtoList.forEach(plDto ->{
                    SharingPrivilege spl = new SharingPrivilege();
                    BeanCopierUtils.copyProperties(plDto,spl);
                    spl.setSharingPlatlform(domain);
                    privilegeList.add(spl);
                });
                domain.setPrivilegeList(privilegeList);
            }
        }
        domain.setIsPublish(record.getIsPublish());
        domain.setCreatedBy(currentUser.getLoginName());
        domain.setModifiedBy(currentUser.getLoginName());
        domain.setPublishDate(now);
        domain.setPublishUsername(currentUser.getLoginName());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        sharingPlatlformRepo.save(domain);
        record.setSharId(domain.getSharId());

    }

    @Override
    @Transactional
    public void update(SharingPlatlformDto record) {
        SharingPlatlform domain = sharingPlatlformRepo.findById(record.getSharId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        //1、如果有ID，则要先删除之前的权限
        if(!Validate.isString(domain.getSharId())){
            domain.setSharId(UUID.randomUUID().toString());
        }else{
            sharingPrivilegeService.deleteByShareId(domain.getSharId());
        }
        //如果不是发布给所有人看，则要添加相应的权限
        if(!EnumState.YES.getValue().equals(domain.getIsNoPermission())){
            List<SharingPrivilegeDto> privilegeDtoList = record.getPrivilegeDtoList();
            List<SharingPrivilege> privilegeList = new ArrayList<>();
            if(privilegeDtoList != null && privilegeDtoList.size() > 0){
                privilegeDtoList.forEach(plDto ->{
                    SharingPrivilege spl = new SharingPrivilege();
                    BeanCopierUtils.copyProperties(plDto,spl);
                    privilegeList.add(spl);
                });
                domain.setPrivilegeList(privilegeList);
            }
        }
        Date now = new Date();
        domain.setModifiedBy(currentUser.getLoginName());
        domain.setPublishDate(now);
        domain.setPublishUsername(currentUser.getLoginName());
        domain.setModifiedDate(now);
        sharingPlatlformRepo.save(domain);
    }

    /**
     * 根据ID查询，包括权限
     * @param id
     * @return
     */
    @Override
    public SharingPlatlformDto findById(String id) {
        SharingPlatlformDto modelDto = new SharingPlatlformDto();
        if (Validate.isString(id)) {
            List<SharingPrivilegeDto> privilegeDtoList = new ArrayList<>();
            SharingPlatlform domain = sharingPlatlformRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
            if(domain.getPrivilegeList() != null && domain.getPrivilegeList().size() > 0){
                domain.getPrivilegeList().forEach(pl->{
                    SharingPrivilegeDto spDto = new SharingPrivilegeDto();
                    BeanCopierUtils.copyProperties(pl,spDto);
                    privilegeDtoList.add(spDto);
                });
                modelDto.setPrivilegeDtoList(privilegeDtoList);
            }
        }
        return modelDto;
    }

    @Override
    @Transactional
    public void delete(String id) {
        SharingPlatlform sharing = sharingPlatlformRepo.findById(id);
        if (sharing != null) {
            if (sharing.getCreatedBy().equals(currentUser.getLoginName())) {
                sharingPlatlformRepo.delete(sharing);
                logger.info(String.format("删除发现信息，发布主题:%s", sharing.getTheme()));
            } else if (currentUser.getLoginName().equals("admin")) {
                sharingPlatlformRepo.delete(sharing);
                logger.info(String.format("删除发现信息，发布主题:%s", sharing.getTheme()));
            } else {
                throw new IllegalArgumentException("您不是发布人员，不能对其进行删除操作！");
            }
        }

    }

    @Override
    public void deletes(String[] ids) {
        for (String id : ids) {
            this.delete(id);
        }
        logger.info("批量删除发布信息");
    }


    @Override
    @Transactional
    public void updatePublishStatus(SharingPlatlformDto sharingDto) {
        SharingPlatlform domain = sharingPlatlformRepo.findById(sharingDto.getSharId());
        if (domain != null) {

            if (domain.getIsPublish().equals("0") && domain.getPublishUsername().equals(currentUser.getLoginName())) {
                domain.setIsPublish(EnumState.YES.getValue());
                BeanCopierUtils.copyPropertiesIgnoreNull(sharingDto, domain);
                Date now = new Date();
                domain.setCreatedBy(currentUser.getLoginName());
                domain.setModifiedBy(currentUser.getLoginName());
                domain.setPublishDate(now);
                domain.setPublishUsername(currentUser.getLoginName());
                domain.setCreatedDate(now);
                domain.setModifiedDate(now);
            } else if (domain.getIsPublish().equals("9") && domain.getPublishUsername().equals(currentUser.getLoginName())) {
                domain.setIsPublish(EnumState.NO.getValue());
                BeanCopierUtils.copyPropertiesIgnoreNull(sharingDto, domain);
                Date now = new Date();
                domain.setCreatedBy(currentUser.getLoginName());
                domain.setModifiedBy(currentUser.getLoginName());
                domain.setPublishDate(now);
                domain.setPublishUsername(currentUser.getLoginName());
                domain.setCreatedDate(now);
                domain.setModifiedDate(now);
            } else {
                throw new IllegalArgumentException("您不是发布人员，不能对该记录操作！");
            }
        }

        sharingPlatlformRepo.save(domain);
    }

    @Override
    public UserDto findUser(String loginName) {
        User user = userRepo.findUserByName(loginName);
        UserDto userDto = new UserDto();
        if (user.getLoginName().equals(currentUser.getLoginName())) {
            BeanCopierUtils.copyProperties(user, userDto);
            return userDto;
        } else {
            throw new IllegalArgumentException("只能选择登录本人！");
        }

    }

    /**
     * 获取部门和用户，用户初始化数据
     * @return
     */
    @Override
    public Map<String, Object> initOrgAndUser() {
        Map<String, Object> resultMap = new HashMap<>();

        //1、查询用户和部门
        Criteria criteria = orgRepo.getExecutableCriteria();
        List<Org> orgList = criteria.list();
        List<OrgDto> orgDtoList = new ArrayList<>(orgList == null?0:orgList.size());
        orgList.forEach(ol -> {
            OrgDto orgDto = new OrgDto();
            List<UserDto> userDtoList = new ArrayList<>();
            BeanCopierUtils.copyProperties(ol,orgDto);
            if(ol.getUsers() != null && ol.getUsers().size() > 0){
                ol.getUsers().forEach( ul ->{
                    if("t".equals(ul.getJobState())){
                        UserDto userDto = new UserDto();
                        userDto.setDisplayName(ul.getDisplayName());
                        userDto.setId(ul.getId());
                        userDto.setLoginName(ul.getLoginName());
                        userDtoList.add(userDto);
                    }

                });
            }
            orgDto.setUserDtos(userDtoList);
            orgDtoList.add(orgDto);
        });
        resultMap.put("orgDtoList",orgDtoList);

        //2、查询没有部门的用户（在职，部门ID为空的用户）
        HqlBuilder userSql = HqlBuilder.create();
        userSql.append(" select * from cs_user where "+ User_.jobState.getName()+" = 't' and (orgid is null or orgid = '') ");
        List<User> noOrgUserList = userRepo.findBySql(userSql);
        List<UserDto> userDtoList2 = new ArrayList<>();
        if(noOrgUserList != null && noOrgUserList.size() > 0){
            noOrgUserList.forEach( ul ->{
                UserDto userDto = new UserDto();
                userDto.setDisplayName(ul.getDisplayName());
                userDto.setId(ul.getId());
                userDto.setLoginName(ul.getLoginName());
                userDtoList2.add(userDto);
            });
        }
        resultMap.put("noOrgUserList",userDtoList2);

        return resultMap;
    }

}