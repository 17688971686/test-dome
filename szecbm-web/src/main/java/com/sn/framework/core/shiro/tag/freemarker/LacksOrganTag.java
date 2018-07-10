package com.sn.framework.core.shiro.tag.freemarker;

import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.common.StringUtil;
import com.sn.framework.module.sys.domain.Organ;

/**
 * Description: 判断不是某个机构的用户
 *
 * @author: tzg
 * @date: 2017/10/21 18:58
 */
public class LacksOrganTag extends OrganTag {

    @Override
    protected boolean showTagBody(String organId) {
        if (StringUtil.isBlank(organId)) {
            return true;
        }
        Organ organ = SessionUtil.getOrgan();
        return organ == null || !organ.getOrganId().equals(organId);
    }

}