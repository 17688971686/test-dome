package com.sn.framework.module.common.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.common.domain.Comment;
import com.sn.framework.module.common.domain.Comment_;
import com.sn.framework.module.common.repo.ICommentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12 0012.
 */
@Component
public class CommentRepoImpl extends AbstractRepository<Comment, String> implements ICommentRepo {

    private final Logger logger = LoggerFactory.getLogger(CommentRepoImpl.class);

    @Override
    public List<Comment> findByBusinessKey(String businessKey) {
        CriteriaQuery<Comment> query = createCriteriaQuery();
        Root<Comment> root = query.from(Comment.class);
        Path busiKeyPath = root.get(Comment_.businessKey),
             sortPath = root.get(Comment_.createdDate);
        Predicate pp = getBuilder().equal(busiKeyPath, businessKey);
        query.where(getBuilder().and(pp));
        query.orderBy(getBuilder().desc(sortPath));

        return entityManager.createQuery(query.select(root)).getResultList();

    }
}
