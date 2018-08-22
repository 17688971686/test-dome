package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.IdWorker;
import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.SQLUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.DomainBase;
import com.sn.framework.core.repo.ICommonRepo;
import com.sn.framework.module.sys.domain.OrgDept;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.model.SysVariableDto;
import com.sn.framework.module.sys.model.UserDto;
import com.sn.framework.module.sys.repo.IOrgDeptRepo;
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
public class OrgDeptServiceImpl implements IOrgDeptService {
    private final static Logger logger = LoggerFactory.getLogger(OrgDeptServiceImpl.class);

    @Autowired
    private IOrgDeptRepo orgDeptRepo;

    @Override
    public List<OrgDept> findAll() {
        return orgDeptRepo.findAll();
    }
}
