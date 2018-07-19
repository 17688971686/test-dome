package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.common.ObjectUtils;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.*;
import com.sn.framework.module.sys.repo.IResourceRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: 系统资源  数据操作实现类
 * @Author: tzg
 * @Date: 2017/9/14 9:43
 */
@Repository
public class ResourceRepoImpl extends AbstractRepository<Resource, String> implements IResourceRepo {

    @Override
    public Set<String> findUserPermission(User user) {
        Set<String> permissions = new HashSet<>();
        if( null !=user.getSuperUser() && (1 == user.getSuperUser())){
            permissions.add("*");
        }else{
            Set<Role> roles = user.getRoles();
            Organ organ = user.getOrgan();
            if (ObjectUtils.isEmpty(roles) && null == organ) {
                return new HashSet<>();
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<String> query = builder.createQuery(String.class);
            Root<Resource> root = query.from(Resource.class);
            List<Predicate> predicateList = createPredicates(roles, organ, root);
            Path permCode = root.get(Resource_.permCode);
            query.where(builder.and(
                    builder.or(predicateList.toArray(new Predicate[predicateList.size()])),
                    permCode.isNotNull(),
                    builder.notEqual(permCode, "")
            ));
            List<String> permCodes = entityManager.createQuery(query.select(root.get(Resource_.permCode)).distinct(true)).getResultList();
            permissions = new HashSet<>(permCodes);
        }
        return permissions;
    }

    private List<Predicate> createPredicates(Set<Role> roles, Organ organ, Root<Resource> root) {
        List<Predicate> predicateList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(roles)) {
            Join<Resource, Role> rolePath = root.join(Resource_.roles, JoinType.LEFT);
            int i = 0, len = roles.size();
            String[] roleIds = new String[len];
            for (Role r : roles) {
                roleIds[i] = r.getRoleId();
                i++;
            }
            predicateList.add(rolePath.get(Role_.roleId).in(roleIds));
        }
        if (organ != null && StringUtil.isNotBlank(organ.getOrganRel())) {
            Join<Resource, Organ> organJoin = root.join(Resource_.organs, JoinType.LEFT);
            String organRel = organ.getOrganRel();
            String[] relChain = StringUtil.split(organRel.substring(1, organRel.length() - 1), "|");
            predicateList.add(organJoin.get(Organ_.organId).in(relChain));
        }
        return predicateList;
    }

    @Override
    public List<Resource> findMenus(User user, String status) {
        Set<Role> roles = user.getRoles();
        Organ organ = user.getOrgan();
        if (ObjectUtils.isEmpty(roles) && null == organ) {
            return new ArrayList<>();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resource> query = builder.createQuery(Resource.class);
        Root<Resource> root = query.from(Resource.class);
        List<Predicate> predicateList = createPredicates(roles, organ, root);
        query.where(builder.and(
                builder.or(predicateList.toArray(new Predicate[predicateList.size()])),
                builder.le(root.get(Resource_.resType), 2),
                builder.equal(root.get(Resource_.status), status)
        )).orderBy(builder.asc(root.get(Resource_.itemOrder)));
        return entityManager.createQuery(query.select(root).distinct(true)).getResultList();
    }

}