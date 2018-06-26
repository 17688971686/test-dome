package cs.repository.repositoryImpl.party;

import cs.common.HqlBuilder;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.party.PartyManager;
import cs.domain.party.PartyManager_;
import cs.model.PageModelDto;
import cs.model.party.PartyManagerDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Author: mcl
 * Date: 2018/3/11 21:08
 */
@Repository
public class PartyManagerRepoImpl extends AbstractRepository<PartyManager, String> implements  PartyManageRepo {


    /**
     * 保存党员信息
     * @param partyManagerDto
     * @return
     */
    @Override
    public ResultMsg creteParty(PartyManagerDto partyManagerDto) {

        Date now = new Date();
        PartyManager partyManager = new PartyManager();
        BeanCopierUtils.copyPropertiesIgnoreNull(partyManagerDto , partyManager);
        if(! Validate.isString(partyManagerDto.getPmId())){
            partyManager.setPmId(UUID.randomUUID().toString());
            partyManager.setCreatedBy(SessionUtil.getDisplayName());
            partyManager.setCreatedDate(now);
        }
        partyManager.setModifiedBy(SessionUtil.getDisplayName());
        partyManager.setModifiedDate(now);

        save(partyManager);

        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "保存成功！" , partyManager);
    }

    /**
     * 通过OData查询数据列表
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<PartyManagerDto> findByOData(ODataObj oDataObj) {

        PageModelDto<PartyManagerDto> pageModelDto = new PageModelDto<>();

        Criteria criteria = getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);

        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (oDataObj.getSkip() > 0) {
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if (oDataObj.getTop() > 0) {
            criteria.setMaxResults(oDataObj.getTop());
        }

        //处理orderby
        if (Validate.isString(oDataObj.getOrderby())) {
            if (oDataObj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
            }
        }

        List<PartyManager> partyManagerList = criteria.list();
        List<PartyManagerDto> partyManagerDtoList = new ArrayList<>();
        if(partyManagerList != null &&partyManagerList.size() > 0 ){
            for(PartyManager partyManager : partyManagerList ){
                PartyManagerDto partyManagerDto = new PartyManagerDto();
                BeanCopierUtils.copyPropertiesIgnoreNull(partyManager , partyManagerDto);
                partyManagerDtoList.add(partyManagerDto);
            }
        }

        pageModelDto.setValue(partyManagerDtoList);

        return pageModelDto;
    }

    /**
     * 通过Id查询党员信息
     * @param pmId
     * @return
     */
    @Override
    public ResultMsg findPartyById(String pmId) {
       PartyManager partyManager = findById(PartyManager_.pmId.getName() , pmId);
       PartyManagerDto partyManagerDto = new PartyManagerDto();
       BeanCopierUtils.copyPropertiesIgnoreNull(partyManager , partyManagerDto);
        return new ResultMsg(true , Constant.MsgCode.OK.getValue()  , "查询成功" , partyManagerDto);
    }

    /**
     * 更新党员信息
     * @param partyManagerDto
     * @return
     */
    @Override
    public ResultMsg updateParty(PartyManagerDto partyManagerDto) {

        PartyManager partyManager = findById(PartyManager_.pmId.getName() , partyManagerDto.getPmId());
        BeanCopierUtils.copyPropertiesIgnoreNull(partyManagerDto , partyManager);
        partyManager.setModifiedDate(new Date());
        partyManager.setModifiedBy(SessionUtil.getDisplayName());

        save(partyManager);

        return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "更新成功" , null);
    }

    /**
     * 删除党员信息
     * @param pmId
     */
    @Override
    public void deleteParty(String pmId) {
        deleteById(PartyManager_.pmId.getName() , pmId);
    }
}