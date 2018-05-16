package cs.service.sharing;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.SysFileUtil;
import cs.common.utils.Validate;
import cs.domain.sharing.SharingPlatlform;
import cs.domain.sharing.SharingPlatlform_;
import cs.domain.sharing.SharingPrivilege;
import cs.domain.sys.*;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sharing.SharingPrivilegeDto;
import cs.model.sys.OrgDto;
import cs.model.sys.SysDeptDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sharing.SharingPlatlformRepo;
import cs.repository.repositoryImpl.sys.*;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
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
    private SharingPrivilegeService sharingPrivilegeService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SysFileRepoImpl sysFileRepoImpl;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private SysDeptRepo sysDeptRepo;

    @Override
    public PageModelDto<SharingPlatlformDto> get(ODataObj odataObj) {
        PageModelDto<SharingPlatlformDto> pageModelDto = new PageModelDto<SharingPlatlformDto>();
        List<SharingPlatlform> resultList = sharingPlatlformRepo.findByOdata(odataObj);
        List<SharingPlatlformDto> resultDtoList = new ArrayList<SharingPlatlformDto>(resultList.size());

        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                SharingPlatlformDto modelDto = new SharingPlatlformDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 查询个人发布
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SharingPlatlformDto> findByCurUser(ODataObj odataObj) {
        PageModelDto<SharingPlatlformDto> pageModelDto = new PageModelDto<SharingPlatlformDto>();
        Criteria criteria = sharingPlatlformRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        //创建人为当前用户
        criteria.add(Restrictions.eq(SharingPlatlform_.createdBy.getName(), SessionUtil.getUserId()));
        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        // 处理分页
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        // 处理orderby
        if (Validate.isString(odataObj.getOrderby())) {
            if (odataObj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(odataObj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(odataObj.getOrderby()).asc());
            }
        }
        List<SharingPlatlform> resultList = criteria.list();
        List<SharingPlatlformDto> resultDtoList = new ArrayList<SharingPlatlformDto>(resultList==null?0:resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                SharingPlatlformDto modelDto = new SharingPlatlformDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setValue(resultDtoList);

        return pageModelDto;
    }


    /**
     * 个人接收到的共享数据
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SharingPlatlformDto> findByReception(ODataObj odataObj) {
        PageModelDto<SharingPlatlformDto> pageModelDto = new PageModelDto<SharingPlatlformDto>();
        Criteria criteria = sharingPlatlformRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        //查询个人接收到的记录信息（全局、部分和个人）
        String userId = SessionUtil.getUserId();
        Object[] objects = null;
        Type[] types = null;
        boolean isOrgUser = Validate.isObject(SessionUtil.getUserInfo().getOrg()) && Validate.isString(SessionUtil.getUserInfo().getOrg().getId());
        criteria.add(Restrictions.eq(SharingPlatlform_.isPublish.getName(),Constant.EnumState.YES.getValue()));
        StringBuilder linkSql = new StringBuilder("(isnopermission = ? or sharid in ");
        linkSql.append(" ( select sharid from cs_sharing_privilege where (businesstype = ? and  businessId =?) ");
        if(isOrgUser){
            linkSql.append(" or (businesstype =? and  businessId =?)" );
            linkSql.append(" or (businesstype =? and  businessId in (select SYSDEPTLIST_ID from CS_DEPT_CS_USER where USERLIST_ID = ?))" );
        }
        linkSql.append(" ) )");
        if(isOrgUser){
            objects = new Object[]{EnumState.YES.getValue(),"2",userId,"1",SessionUtil.getUserInfo().getOrg().getId(),"3",userId};
            types = new Type[]{StringType.INSTANCE,StringType.INSTANCE,StringType.INSTANCE,StringType.INSTANCE,StringType.INSTANCE,StringType.INSTANCE,StringType.INSTANCE};
        }else {
            objects = new Object[]{EnumState.YES.getValue(),"2",userId};
            types = new Type[]{StringType.INSTANCE,StringType.INSTANCE,StringType.INSTANCE};
        }

        criteria.add(Restrictions.sqlRestriction(linkSql.toString(),objects,types));

        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        // 处理分页
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        // 处理orderby
        if (Validate.isString(odataObj.getOrderby())) {
            if (odataObj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(odataObj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(odataObj.getOrderby()).asc());
            }
        }
        List<SharingPlatlform> resultList = criteria.list();
        List<SharingPlatlformDto> resultDtoList = new ArrayList<SharingPlatlformDto>(resultList==null?0:resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                SharingPlatlformDto modelDto = new SharingPlatlformDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
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
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setCreatedDate(now);
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
        //立即发布
        if(Validate.isString(domain.getIsPublish()) && EnumState.YES.getValue().equals(domain.getIsPublish())){
            domain.setPublishDate(now);
            domain.setPublishUsername(SessionUtil.getDisplayName());
        }
        domain.setIsPublish(record.getIsPublish());
        domain.setModifiedBy(SessionUtil.getLoginName());

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
        domain.setModifiedBy(SessionUtil.getLoginName());
        domain.setPublishDate(now);
        domain.setPublishUsername(SessionUtil.getLoginName());
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
        //1、删除对应关系
        sharingPrivilegeService.bathDeleteByShareId(id);
        //2、删除记录信息
        sharingPlatlformRepo.deleteById(SharingPlatlform_.sharId.getName(),id);
        //3、删除附件
        List<SysFile> fileList = sysFileRepoImpl.findByIds(SysFile_.businessId.getName(),id,null);
        if(Validate.isList(fileList)){
            fileList.forEach(f->{
                sysFileRepoImpl.deleteByFileId(f.getSysFileId());
                //SysFileUtil.deleteFile(SysFileUtil.getUploadPath() + f.getFileUrl());
            });
        }
    }

    /**
     * 更改发布状态
     * @param ids
     * @param status
     */
    @Override
    @Transactional
    public void updatePublishStatus(String ids,String status) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+SharingPlatlform.class.getSimpleName()+" set "+ SharingPlatlform_.isPublish.getName()+" =:status " );
        hqlBuilder.setParam("status",status);
        if(Constant.EnumState.YES.getValue().equals(status)){
            hqlBuilder.append(","+SharingPlatlform_.publishDate.getName()+"=sysdate ");
            hqlBuilder.append(","+SharingPlatlform_.publishUsername.getName()+"= :issueUser ").setParam("issueUser",SessionUtil.getLoginName());
        }
        hqlBuilder.bulidPropotyString("where",SharingPlatlform_.sharId.getName(),ids);
        sharingPlatlformRepo.executeHql(hqlBuilder);
    }

    @Override
    public UserDto findUser(String loginName) {
        User user = userRepo.findUserByName(loginName);
        UserDto userDto = new UserDto();
        if (user.getLoginName().equals(SessionUtil.getLoginName())) {
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
    public Map<String, Object> initOrgAndUser(boolean isInCludeGroup) {
        Map<String, Object> resultMap = new HashMap<>();
        //1、查询用户和部门
        Criteria criteria = orgRepo.getExecutableCriteria();
        criteria.addOrder(Order.asc(Org_.sort.getName()));
        List<Org> orgList = criteria.list();
        List<OrgDto> orgDtoList = new ArrayList<>(Validate.isList(orgList)?orgList.size():0);
        orgList.forEach(ol -> {
            OrgDto orgDto = new OrgDto();
            List<UserDto> userDtoList = new ArrayList<>();
            BeanCopierUtils.copyProperties(ol,orgDto);
            if(Validate.isList(ol.getUsers())){
                ol.getUsers().forEach( ul ->{
                    if("t".equals(ul.getJobState()) && !Constant.SUPER_USER.equals(ul.getLoginName())){
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
                if(!Constant.SUPER_USER.equals(ul.getLoginName())){
                    UserDto userDto = new UserDto();
                    userDto.setDisplayName(ul.getDisplayName());
                    userDto.setId(ul.getId());
                    userDto.setLoginName(ul.getLoginName());
                    userDtoList2.add(userDto);
                }
            });
        }
        resultMap.put("noOrgUserList",userDtoList2);


        //判断是否包含组别
        if(isInCludeGroup){
            criteria = sysDeptRepo.getExecutableCriteria();
            List<SysDept> deptList = criteria.list();
            if(Validate.isList(deptList)){
                List<SysDeptDto> deptDtoList = new ArrayList<>();
                deptList.forEach(dl -> {
                    SysDeptDto sysDeptDto = new SysDeptDto();
                    List<UserDto> userDtoList = new ArrayList<>();
                    BeanCopierUtils.copyProperties(dl,sysDeptDto);
                    if(Validate.isList(dl.getUserList())){
                        dl.getUserList().forEach( ul ->{
                            if("t".equals(ul.getJobState()) && !Constant.SUPER_USER.equals(ul.getLoginName())){
                                UserDto userDto = new UserDto();
                                userDto.setDisplayName(ul.getDisplayName());
                                userDto.setId(ul.getId());
                                userDto.setLoginName(ul.getLoginName());
                                userDtoList.add(userDto);
                            }
                        });
                    }
                    sysDeptDto.setUserDtoList(userDtoList);
                    deptDtoList.add(sysDeptDto);

                });
                resultMap.put("deptDtoList",deptDtoList);
            }
        }
        return resultMap;
    }

}