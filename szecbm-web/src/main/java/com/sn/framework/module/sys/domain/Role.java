package com.sn.framework.module.sys.domain;

import com.sn.framework.core.DomainBase;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: 角色信息  数据映射实体
 * @Author: tzg
 * @Date: 2017/8/23 17:07
 */
@Entity
@Table(name = "cs_sys_role")
@Data
public class Role extends DomainBase {

    private static final long serialVersionUID = -6567523716293168913L;
    /**
     * 角色ID
     */
    @Id
    @Column(length = 64)
    private String roleId;
    /**
     * 角色名
     */
    @Column(unique = true, nullable = false, length = 64)
    private String roleName;
    /**
     * 角色显示名
     */
    @Column(nullable = false, length = 100)
    private String displayName;
    /**
     * 备注
     */
    @Column
    private String remark;
    /**
     * 角色状态：1、启用；0、禁用
     */
    @Column(length = 10, nullable = false)
    private String roleState;
    /**
     * 角色数据类型：0、为系统基础数据，不可删除；1、为用户添加数据，可删除
     */
    @Column(length = 1)
    private int roleDataType;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cs_sys_role_resources", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "res_id"))
    private Set<Resource> resources = new HashSet<>();

}