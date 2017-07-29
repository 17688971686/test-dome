package cs.service.sharing;

import cs.common.HqlBuilder;
import cs.repository.repositoryImpl.sharing.SharingPrivilegeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 共享平台 业务操作实现类
 * author: sjy
 * Date: 2017-7-20 18:23:08
 */
@Service
public class SharingPrivilegeServiceImpl implements SharingPrivilegeService {

    @Autowired
    private SharingPrivilegeRepo sharingPrivilegeRepo;

    /**
     * 根据共享ID删除权限信息
     *
     * @param shareId
     * @return
     */
    @Override
    public boolean deleteByShareId(String shareId) {
        HqlBuilder sqlHuilder = HqlBuilder.create();
        sqlHuilder.append(" delete from cs_sharing_privilege where sharid =:sharid ");
        sqlHuilder.setParam("sharid", shareId);
        return sharingPrivilegeRepo.executeSql(sqlHuilder) < 0 ? false : true;
    }

    @Override
    public boolean bathDeleteByShareId(String shareIds) {
        HqlBuilder sqlHuilder = HqlBuilder.create();
        sqlHuilder.append(" delete from cs_sharing_privilege ");
        sqlHuilder.bulidIdString("where","sharid",shareIds);
        return sharingPrivilegeRepo.executeSql(sqlHuilder) < 0 ? false : true;
    }
}