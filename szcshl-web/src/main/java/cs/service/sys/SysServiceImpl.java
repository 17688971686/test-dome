package cs.service.sys;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.sysResource.ClassFinder;
import cs.common.sysResource.SysResourceDto;
import cs.common.utils.Validate;
import cs.domain.sys.Resource;
import cs.domain.sys.Role;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.sys.RoleRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cs.common.Constant.SUPER_USER;

@Service
public class SysServiceImpl implements SysService {
    private static Logger logger = Logger.getLogger(SysServiceImpl.class);

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public List<SysResourceDto> get() {

        List<SysResourceDto> resources = new ArrayList<SysResourceDto>();
        List<Class<?>> classes = ClassFinder.find("cs.controller");
        for (Class<?> obj : classes) {
            if (obj.isAnnotationPresent(RequestMapping.class)) {
                //如果是忽略模块，则返回
                Annotation ignoreAnnotation = obj.getAnnotation(IgnoreAnnotation.class);
                if(Validate.isObject(ignoreAnnotation)){
                    continue;
                }
                //先找到模块名称
                Annotation moduleAnnotation = obj.getAnnotation(MudoleAnnotation.class);
                MudoleAnnotation mudoleAnnotation = (MudoleAnnotation) moduleAnnotation;
                //构建模块
                SysResourceDto moduleResourceDto = buildSysResource(resources, mudoleAnnotation);

                //具体某个子模块
                Annotation classAnnotation = obj.getAnnotation(RequestMapping.class);
                RequestMapping classAnnotationInfo = (RequestMapping) classAnnotation;
                SysResourceDto resource = new SysResourceDto();
                resource.setName(classAnnotationInfo.name());
                resource.setPath(classAnnotationInfo.path()[0]);

                List<SysResourceDto> operations = new ArrayList<SysResourceDto>();

                for (Method method : obj.getDeclaredMethods()) {
                    //过滤不是页面的方法
                    if (method.isAnnotationPresent(RequestMapping.class) && method.isAnnotationPresent(RequiresPermissions.class)) {
                        SysResourceDto operation = new SysResourceDto();
                        Annotation methodAnnotation = method.getAnnotation(RequestMapping.class);
                        RequestMapping methodAnnotationInfo = (RequestMapping) methodAnnotation;

                        String httpMethod = methodAnnotationInfo.method().length == 0 ? "GET": methodAnnotationInfo.method()[0].name();
                        operation.setPath(String.format("%s#%s#%s", resource.getPath(), Validate.isArray(methodAnnotationInfo.path())?
                                methodAnnotationInfo.path()[0].replace("{", "").replace("}", ""):"", httpMethod));
                        operation.setName(String.format("%s(%s)", methodAnnotationInfo.name(), operation.getPath()));
                        operation.setMethod(httpMethod);
                        operations.add(operation);

                    }
                }

                if(moduleResourceDto != null){
                    moduleResourceDto.getChildren().addAll(operations);
                }else{
                    resource.setChildren(operations);
                    resources.add(resource);
                }
            }

        }
        return resources;
    }

    /**
     * 创建资源文件
     * @param resources
     * @param moduleAnnotation
     * @return
     */
    private SysResourceDto buildSysResource(List<SysResourceDto> resources, MudoleAnnotation moduleAnnotation) {
        if(null == moduleAnnotation){
            return null;
        }
        String moduleName = moduleAnnotation.name();
        String modulePath = moduleAnnotation.value();
        boolean isExists = false;
        SysResourceDto resourceDto = null;
        if(Validate.isList(resources)){
            for(SysResourceDto rd:resources){
                if(rd.getName().equals(moduleName)){
                    resourceDto = rd;
                    isExists = true;
                    break;
                }
            }
        }
        if(!isExists){
            resourceDto = new SysResourceDto();
            resourceDto.setName(moduleName);
            resourceDto.setPath(modulePath);
            resources.add(resourceDto);
        }
        return resourceDto;
    }

    /**
     * 系统初始化，这个要修改
     *
     * @return
     */
    @Override
    @Transactional
    public ResultMsg SysInit() {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(Constant.EnumConfigKey.SYSINIT.getValue());
        // 更新sysConfig
        if (sysConfigDto != null && (Constant.EnumState.YES.getValue()).equals(sysConfigDto.getConfigValue())) {// 已经被初始化
            logger.warn("已经存在初始化数据，此次操作无效");
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"已经存在初始化数据，此次操作无效");
        } else {
            //初始化角色
            Role role = new Role();
            role.setRoleName(Constant.SUPER_ROLE);
            role.setId(UUID.randomUUID().toString());
            role.setRemark("系统初始化创建,不可删除");
            role.setCreatedBy("root");
            role.setModifiedBy("root");

            List<SysResourceDto> resourceDtos = this.get();
            List<Resource> resources = new ArrayList<>();
            resourceDtos.forEach(x -> {
                x.getChildren().forEach(y -> {
                    Resource resource = new Resource();
                    resource.setMethod(y.getMethod());
                    resource.setName(y.getName());
                    resource.setPath(y.getPath());
                    resources.add(resource);
                });
            });
            role.setResources(resources);

            roleRepo.save(role);

            // 初始化用户
            User user = new User();
            user.setLoginName(SUPER_USER);
            user.setId(UUID.randomUUID().toString());
            user.setPassword(SUPER_USER);
            user.setRemark("系统初始化创建,不可删除");
            user.setDisplayName(Constant.SUPER_ROLE);
            user.getRoles().add(role);
            user.setCreatedBy("root");
            user.setModifiedBy("root");
            userRepo.save(user);

            // 更新sysConfig
            sysConfigDto = new SysConfigDto();
            sysConfigDto.setConfigName("系统初始化");
            sysConfigDto.setConfigKey(Constant.EnumConfigKey.SYSINIT.getValue());
            sysConfigDto.setConfigValue(Constant.EnumState.YES.getValue());
            sysConfigDto.setIsShow(Constant.EnumState.NO.getValue());
            sysConfigService.save(sysConfigDto);

            logger.info("系统初始化成功!");
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"初始化成功");


        }

    }
}
