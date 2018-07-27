package cs.common.constants;

import cs.domain.DomainBase_;
import cs.domain.project.Sign_;

/**
 * 对象复制是忽略的属性
 * Created by ldm on 2018/6/13 0013.
 */
public class IgnoreProps {

    /**
     * 委里推送数据是忽略的属性
     */
    public static final String[] PUSH_SIGN_IGNORE_PROPS = new String[]{
            Sign_.signSeq.getName(),Sign_.signdate.getName(),Sign_.sendusersign.getName(),
            Sign_.createdBy.getName(),Sign_.createdDate.getName(),Sign_.dealOrgType.getName(),
            Sign_.filenum.getName(),Sign_.issign.getName(),Sign_.surplusdays.getName(),
            Sign_.totalReviewdays.getName(),Sign_.reviewdays.getName()};

    /**
     * 基本的忽略属性
     */
    public static final String[] BASE_IGNORE_PROPS = new String[]{
            DomainBase_.createdBy.getName(),DomainBase_.createdDate.getName(),
            DomainBase_.modifiedBy.getName(),DomainBase_.modifiedDate.getName()
    };
}
