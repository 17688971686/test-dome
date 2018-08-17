package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.SQLUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.DomainBase;
import com.sn.framework.core.repo.ICommonRepo;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.model.SysVariableDto;
import com.sn.framework.module.sys.model.UserDto;
import com.sn.framework.module.sys.repo.IRoleRepo;
import com.sn.framework.module.sys.service.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.Id;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import static com.sn.framework.core.Constants.*;

/**
 * 系统初始化 业务操作类
 *
 * @author tzg
 */
@Service
public class SysServiceImpl implements ISysService {
    private final static Logger logger = LoggerFactory.getLogger(SysServiceImpl.class);

    @Autowired
    private IRoleRepo roleRepo;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISysVariableService sysVariableService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private ICommonRepo commonRepo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sysInit() {
        SysVariableDto sysVariable = sysVariableService.findByCode(SYS_VAR_CODE_IS_INIT);
        // 已经被初始化
        if (null != sysVariable) {
            logger.warn("已经存在初始化数据，此次操作无效");
            return false;
        } else {// 未被初始化
            // 初始化角色
            Role role = new Role();
            role.setRoleId(IdWorker.getUUID());
            role.setRoleName(ROLE_KEY_ADMIN);
            role.setDisplayName("超级管理员");
            role.setRemark("系统初始化创建,不可删除");
            role.setRoleState("1");
            role.setResources(new HashSet<>(resourceService.findAll()));
            role.setCreatedBy("root");
            role.setModifiedBy("root");
            roleRepo.save(role);

            // 初始化用户
            UserDto user = new UserDto();
            user.setUsername(USER_KEY_ADMIN);
            user.setPassword(USER_KEY_ADMIN);
            user.setVerifyPassword(USER_KEY_ADMIN);
            user.setRemark("系统初始化创建,不可删除");
            user.setDisplayName("超级管理员");
            user.setLoginFailCount(0);
            user.setUseState("1");
            user.getRoles().add(role);
            userService.create(user, "createdDate", "modifiedDate", "password");

            // 更新sysConfig
            sysVariable = new SysVariableDto();
            sysVariable.setVarCategory("sys");
            sysVariable.setVarCode(SYS_VAR_CODE_IS_INIT);
            sysVariable.setVarName("系统是否初始化");
            sysVariable.setVarValue(Boolean.TRUE);
            sysVariable.setCreatedBy(USER_KEY_ADMIN);
            sysVariable.setModifiedBy(USER_KEY_ADMIN);
            sysVariableService.create(sysVariable);

            try {
                dictService.initDictData();
            } catch (Exception e) {
                logger.error("数据字典初始化失败!", e);
            }

            logger.info("系统初始化成功!");
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initData(String fileName) {
        Assert.hasText(fileName, "缺少文件名称参数");
        String filePath = "/initData/".concat(fileName);
        InputStream dataIS = SysServiceImpl.class.getResourceAsStream(filePath);
        Assert.notNull(dataIS, "未找到数据文件");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(dataIS);
            // 获取根元素
            Element root = document.getRootElement();
            String clsName = root.attributeValue("class");
            String strategy = root.attributeValue("strategy");
            String relate = root.attributeValue("relate");
            String order = root.attributeValue("order");
            String relationChain = root.attributeValue("relationChain");
            logger.debug("===>> Root: {}  {}", root.getName(), clsName);
            Class<? extends DomainBase> cls = (Class<? extends DomainBase>) Class.forName(clsName);
            if (StringUtil.isBlank(strategy) || "all".equals(strategy.toLowerCase())) {
                logger.debug("===>> 全量更新策略 <<===");
                commonRepo.deleteAll(cls);
            }
            logger.debug("===>> 开始解析xml，生成对应实体，并插入数据 <<===");
            resolveElements(cls, null, root.elements(), strategy, relate, null, order, relationChain, null);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != dataIS) {
                try {
                    dataIS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析xml，生成对应实体，并插入数据
     *
     * @param cls
     * @param fields
     * @param els
     * @param strategy
     * @param parenIdField
     * @param parentId
     * @param orderField
     * @param relationChainField
     * @param <T>
     * @throws Exception
     */
    protected <T extends DomainBase> void resolveElements(Class<T> cls, Field[] fields, List<Element> els,
                                                          String strategy, String parenIdField, String parentId,
                                                          String orderField, String relationChainField,
                                                          String relationChain) {
        if (!ObjectUtils.isAnyEmpty(cls, els)) {
            if (ObjectUtils.isEmpty(fields)) {
                fields = cls.getDeclaredFields();
            }
            Attribute attr;
            DomainBase obj;
            Object attrVal;
            int i = 0;
            for (Element el : els) {
                obj = BeanUtils.instantiate(cls);
                String idVal = null, rcVal = null, fieldName;
                for (Field f : fields) {
                    fieldName = f.getName();
                    attr = el.attribute(fieldName);
                    if (null != attr) {
                        attrVal = ObjectUtils.getAttrValue(f, attr.getValue());
                        ObjectUtils.setFieldValue(obj, fieldName, attrVal);
                        if (f.isAnnotationPresent(Id.class)) {
                            idVal = (String) attrVal;
                        }
                    } else if (f.isAnnotationPresent(Id.class)) {
                        idVal = IdWorker.get32UUID();
                        ObjectUtils.setFieldValue(obj, fieldName, idVal);
                    }
                }

                if (StringUtil.isNoneBlank(parentId, parenIdField)) {
                    // 目前不能根据类的字段判断
                    ObjectUtils.setFieldValue(obj, parenIdField, parentId);
                }

                if (StringUtil.isNotBlank(orderField)) {
                    ObjectUtils.setFieldValue(obj, orderField, i);
                } else {
                    obj.setItemOrder(i);
                }
                i++;

                if (StringUtil.isNoneBlank(idVal, relationChainField)) {
                    rcVal = (StringUtil.isBlank(relationChain) ? ORGAN_REL_SEPARATE : relationChain).concat(idVal).concat(ORGAN_REL_SEPARATE);
                    ObjectUtils.setFieldValue(obj, relationChainField, rcVal);
                }

                obj.setCreatedBy("root");
                obj.setModifiedBy("root");
//				if (StringUtil.isNotBlank(strategy) && "update".equalsIgnoreCase(strategy)) {
//					commonRepo.delete(obj);  // 还存在bug，如主键不同，其他字段数据相同的情况
//				}
                commonRepo.saveOrUpdate(obj);
                resolveElements(cls, fields, el.elements(), strategy, parenIdField, idVal, orderField, relationChainField, rcVal);
            }
        }
    }

    @Override
    public void createView() throws IOException {
        // 生成视图
        String[] sqls = SQLUtils.getSql(
                "classpath:sql/1.workflow-view.sql",
                "classpath:sql/2.project-view.sql",
                "classpath:sql/3.leadProject-view.sql",
                "classpath:sql/4.questionSummary_view.sql",
                "classpath:sql/5.schedule_view.sql",
                "classpath:sql/6.schedule_lead_view.sql"
        );
        logger.debug("createView: {}", sqls);
        jdbcTemplate.batchUpdate(sqls);
    }
}
