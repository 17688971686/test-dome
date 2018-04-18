package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.ObjectUtils;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertNewInfo_;
import cs.domain.expert.ExpertSelected_;
import cs.domain.expert.Expert_;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.project.SignAssistCostDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExpertRepoImpl extends AbstractRepository<Expert, String> implements ExpertRepo {

    /**
     * 判断专家身份证号 是否已经录入
     * @param idCard
     * @param expertId
     * @return
     */
    @Override
    public boolean checkIsHaveIdCard(String idCard,String expertId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count("+Expert_.expertID.getName()+") from cs_expert ");
        sqlBuilder.append(" where "+Expert_.idCard.getName()+" =:idCard ").setParam("idCard",idCard);
        if(Validate.isString(expertId)){
            sqlBuilder.append(" and "+Expert_.expertID.getName()+" <> :expertId ").setParam("expertId",expertId);
        }
        int count = returnIntBySql(sqlBuilder);
        return (count > 0)?true:false;
    }

    @Override
    public List<Expert> findExpertByIdCard(String idCard) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(Expert_.idCard.getName(), idCard));
        return criteria.list();
    }

    /**
     * 查询所有重名的专家
     * @return
     */
    @Override
    public List<Expert> findAllRepeat() {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" name IN (SELECT name FROM CS_EXPERT GROUP BY  name  HAVING COUNT (name) > 1)"));
        criteria.addOrder(Order.desc(Expert_.name.getName()));

        return criteria.list();
    }

    /**
     * 根据业务ID获取选取的专家信息(确认并参加会议的专家)
     * @param businessId
     * @return
     */
    @Override
    public List<Expert> findByBusinessId(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select e.* from cs_expert e where e.expertID in (select expertID from cs_expert_selected ");
        sqlBuilder.append("  where "+ExpertSelected_.businessId.getName()+" = :businessId ");
        sqlBuilder.setParam("businessId",businessId);
        sqlBuilder.append(" and "+ ExpertSelected_.isConfrim.getName()+" = :isConfirm ");
        sqlBuilder.setParam("isConfirm",Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ ExpertSelected_.isJoin.getName()+" = :isJoin )");
        sqlBuilder.setParam("isJoin",Constant.EnumState.YES.getValue());
        return findBySql(sqlBuilder);
    }

    @Override
    public int countByBusinessId(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(e.expertID) from cs_expert e where e.expertID in (select expertID from cs_expert_selected ");
        sqlBuilder.append("  where "+ExpertSelected_.businessId.getName()+" = :businessId ");
        sqlBuilder.setParam("businessId",businessId);
        sqlBuilder.append(" and "+ ExpertSelected_.isConfrim.getName()+" = :isConfirm ");
        sqlBuilder.setParam("isConfirm",Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ ExpertSelected_.isJoin.getName()+" = :isJoin )");
        sqlBuilder.setParam("isJoin",Constant.EnumState.YES.getValue());
        return returnIntBySql(sqlBuilder);
    }

    @Override
    public List<Expert> get(ODataObj odataObj) {
        String maJorBigParam = "",maJorSamllParam="",expertTypeParam="";
        //Criteria 查询
        Criteria criteria = getExecutableCriteria();
        if (Validate.isList(odataObj.getFilter())) {
            Object value;
            for (ODataFilterItem item : odataObj.getFilter()) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                if("maJorBigParam".equals(item.getField())){
                    maJorBigParam = item.getValue().toString();
                    continue;
                }
                if("maJorSamllParam".equals(item.getField())){
                    maJorSamllParam = item.getValue().toString();
                    continue;
                }
                if("expertTypeParam".equals(item.getField())){
                    expertTypeParam = item.getValue().toString();
                    continue;
                }
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(),value));
            }
        }
        //关联专家大类、小类和专业类型查询
        if(Validate.isString(maJorBigParam) || Validate.isString(maJorSamllParam) || Validate.isString(expertTypeParam)){
            StringBuffer sqlSB = new StringBuffer();
            sqlSB.append(" (select count(ept.ID) from CS_EXPERT_TYPE ept where EPT.EXPERTID = "+criteria.getAlias()+"_.EXPERTID ");
            //突出专业，大类
            if (Validate.isString(maJorBigParam)) {
                sqlSB.append(" and ept.maJorBig = '"+maJorBigParam+"' ");
            }
            //突出专业，小类
            if (Validate.isString(maJorSamllParam)) {
                sqlSB.append(" and ept.maJorSmall = '"+maJorSamllParam+"' ");
            }
            //专家类型
            if (Validate.isString(expertTypeParam)) {
                sqlSB.append(" and ept.expertType = '"+expertTypeParam+"' ");
            }
            sqlSB.append(" ) > 0 ");
            criteria.add(Restrictions.sqlRestriction(sqlSB.toString()));
        }
        if(odataObj.isCount()){
            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            criteria.setProjection(null);
            odataObj.setCount(totalResult);
        }

        // 处理分页
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        return  criteria.list();
    }

    @Override
    public List<Expert> exportData(String filters) {
        int flag=0;

        String[] filterArr = filters.split(",");
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select * from cs_expert  ");
        if(filterArr.length>0 && !"".equals(filterArr[0]) ){
            Boolean b = false;
            hqlBuilder.append(" where ");
            for(int i=0 ; i<filterArr.length ; i++){
                String filter = filterArr[i];
                String[] params = filter.split(":");
                if("expertType".equals( params[0].substring(1,params[0].length()-1))
                        || "maJorBig".equals( params[0].substring(1,params[0].length()-1))
                        || "maJorSmall".equals( params[0].substring(1,params[0].length()-1))){
                    if(flag == 0){

                        hqlBuilder.append(" expertID in (select expertID from cs_expert_type where ");
                    }
                    if( flag > 0){
                        hqlBuilder.append(" and ");
                    }
                    flag ++;
                    hqlBuilder.append( params[0].substring(1,params[0].length()-1) + "=:");
                    hqlBuilder.append( params[0].substring(1,params[0].length()-1));
                    hqlBuilder.setParam(params[0].substring(1,params[0].length()-1) , params[1].substring(1,params[1].length()-1));
                }else{
                    if(flag >0){
                        b =true;
                        hqlBuilder.append(" )");
                    }
                    hqlBuilder.append( params[0].substring(1,params[0].length()-1) + "=:" + params[0].substring(1,params[0].length()-1) );
                    hqlBuilder.setParam(params[0].substring(1,params[0].length()-1) , params[1].substring(1,params[1].length()-1));
                    if(i<filterArr.length-1){
                        hqlBuilder.append(" and ");
                    }
                }
            }
            if(!b){
                hqlBuilder.append(" )");
            }
        }

        List<Expert> expertList = this.findBySql(hqlBuilder);
        return expertList;
    }

    /**
     * 更新
     * @param expertID
     */
    @Override
    public void updateExpertCompositeScore(String expertID) {
        /**
         * UPDATE CS_EXPERT
         SET compositeScore =
         ROUND ( (SELECT SUM (SCORE) / COUNT (id)
         FROM CS_EXPERT_SELECTED
         WHERE EXPERTID = '3FD4FCCAA9BB4EC2A5CB54D8410040C3' AND SCORE is not null AND SCORE > 0),
         2)
         WHERE expertID = '3FD4FCCAA9BB4EC2A5CB54D8410040C3'
         */
        if(Validate.isString(expertID)){
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append("update CS_EXPERT set "+Expert_.compositeScore.getName()+" = ROUND( " );
            sqlBuilder.append(" (SELECT SUM (SCORE) / COUNT (id) FROM CS_EXPERT_SELECTED WHERE EXPERTID =:sExpertID ");
            sqlBuilder.setParam("sExpertID",expertID);
            sqlBuilder.append(" AND ISCONFRIM = :isconfrim AND ISJOIN = :isjoin ");
            sqlBuilder.setParam("isconfrim", Constant.EnumState.YES.getValue()).setParam("isjoin", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" AND SCORE is not null AND SCORE > 0),2)");
            sqlBuilder.append(" where "+Expert_.expertID.getName()+" =:expertID");
            sqlBuilder.setParam("expertID",expertID);
            executeSql(sqlBuilder);
        }

    }

    /**
     * 根据抽取条件，获取抽取的专家
     * @param minBusinessId
     * @param reviewId
     * @param epSelCondition
     * @return
     */
    @Override
    public List<ExpertDto> fingDrafExpert(String minBusinessId, String reviewId, ExpertSelConditionDto epSelCondition) {
        List<ExpertDto> resultList = new ArrayList<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT distinct ep.EXPERTID, ep.NAME, ep.COMPANY, ep.JOB, ep.POST, ep.EXPERTSORT ");
        sqlBuilder.append(" , ep.USERPHONE, ep.MAJORSTUDY, ep.MAJORWORK,ep.REMARK,ep.COMPOSITESCORE,edp.expertType,edp.maJorSmall,edp.maJorBig");
        sqlBuilder.append(" FROM CS_EXPERT ep LEFT JOIN ( ");
        sqlBuilder.append(" SELECT WP.ID ID, WP.BUILDCOMPANY bcp, WP.DESIGNCOMPANY dcp FROM CS_WORK_PROGRAM wp ");
        sqlBuilder.append(" WHERE WP.ID = :minBusinessId) lwp").setParam("minBusinessId",minBusinessId);
        sqlBuilder.append(" ON (lwp.bcp = ep.COMPANY OR lwp.dcp = ep.COMPANY ) ");
        sqlBuilder.append(" LEFT JOIN CS_EXPERT_SELECTED cursel ON CURSEL.EXPERTID = EP.EXPERTID ");
        sqlBuilder.append(" AND CURSEL.EXPERTREVIEWID = :reviewId ").setParam("reviewId",reviewId);
        //是否有抽取条件
        boolean isHaveCondition = Validate.isObject(epSelCondition) && (Validate.isString(epSelCondition.getMaJorBig())
                || Validate.isString(epSelCondition.getMaJorSmall()) || Validate.isString(epSelCondition.getExpeRttype()));
        if(isHaveCondition){
            sqlBuilder.append(" left JOIN V_EXPERT_DRAFPOOL edp on edp.EXPERTID = EP.EXPERTID ");
            if(Validate.isString(epSelCondition.getMaJorBig())){
                sqlBuilder.append(" AND edp.maJorBig = :maJorBig ").setParam("maJorBig",epSelCondition.getMaJorBig());
            }
            if(Validate.isString(epSelCondition.getMaJorSmall())){
                sqlBuilder.append(" AND edp.maJorSmall = :maJorSmall ").setParam("maJorSmall",epSelCondition.getMaJorSmall());
            }
            if(Validate.isString(epSelCondition.getExpeRttype())){
                sqlBuilder.append(" AND edp.expertType = :expertType ").setParam("expertType",epSelCondition.getExpeRttype());
            }
        }
        sqlBuilder.append(" WHERE ep.STATE = '2' AND lwp.ID IS NULL AND CURSEL.ID IS NULL ");
        if(isHaveCondition){
            sqlBuilder.append(" AND edp.EXPERTID IS NOT NULL");
        }
        boolean isHaveObj = Validate.isObject(epSelCondition);
        if(isHaveObj){
            if(epSelCondition.getCompositeScore() != null && epSelCondition.getCompositeScore() > -1){
                sqlBuilder.append(" and  ep.compositeScore >= :compositeScore");
                sqlBuilder.setParam("compositeScore", epSelCondition.getCompositeScore());
            }
            if(epSelCondition.getCompositeScoreEnd() != null && epSelCondition.getCompositeScoreEnd() > -1){
                sqlBuilder.append(" and  ep.compositeScore <= :compositeScoreEnd");
                sqlBuilder.setParam("compositeScoreEnd", epSelCondition.getCompositeScoreEnd());
            }
        }
        List<Object[]> expertList = getObjectArray(sqlBuilder);
        if(Validate.isList(expertList)){
            for (int i = 0, l = expertList.size(); i < l; i++) {
                ExpertDto expertDto = new ExpertDto();
                Object[] obj = expertList.get(i);
                String expertId = obj[0] == null ? "" : obj[0].toString();
                String expertName = obj[1] == null ? "" : obj[1].toString();
                String comPany = obj[2] == null ? "" : obj[2].toString();
                String job = obj[3] == null ? "" : obj[3].toString();
                String post = obj[4] == null ? "" : obj[4].toString();
                String expertSort = obj[11] == null ? "" : obj[11].toString();
                String userPhone = obj[6] == null ? "" : obj[6].toString();
                String majorStudy = obj[13] == null ? "" : obj[13].toString(); //专业大类
                String majorWork = obj[12] == null ? "" : obj[12].toString(); //专业小类
                String remark = obj[9] == null ? "" : obj[9].toString();
                Double compositeScore = obj[10] == null ? null : Double.valueOf(obj[10].toString());

                expertDto.setExpertID(expertId);
                expertDto.setName(expertName);
                expertDto.setComPany(comPany);
                expertDto.setJob(job);
                expertDto.setPost(post);
                expertDto.setExpertSort(expertSort);
                expertDto.setUserPhone(userPhone);
                expertDto.setMajorStudy(majorStudy);
                expertDto.setMajorWork(majorWork);
                expertDto.setRemark(remark);
                expertDto.setCompositeScore(compositeScore);

                resultList.add(expertDto);
            }
        }
        return resultList;
    }

    /**
     * 通过业务ID获取抽取的专家，并参与评审中的组长名称--主要用于会前准备材料的主持人手稿
     * @param businessId
     * @return
     */
    @Override
    public String findExpertGlByBusiness(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select e.name from cs_expert e where e.expertID in (select expertID from cs_expert_selected  ");
        sqlBuilder.append("  where "+ExpertSelected_.businessId.getName()+" = :businessId ");
        sqlBuilder.setParam("businessId",businessId);
        sqlBuilder.append(" and " + ExpertSelected_.expeRttype.getName() + " like '%组长%' ");
        sqlBuilder.append(" and "+ ExpertSelected_.isConfrim.getName()+" = :isConfirm ");
        sqlBuilder.setParam("isConfirm",Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ ExpertSelected_.isJoin.getName()+" = :isJoin )");
        sqlBuilder.setParam("isJoin",Constant.EnumState.YES.getValue());

//        sqlBuilder.append("select expertID , name  from cs_expert_new_info where " + ExpertNewInfo_.businessId.getName() + "=:businessId");
//        sqlBuilder.append(" and " + ExpertNewInfo_.expeRttype.getName() + " like '%组长%' ");
        sqlBuilder.setParam("businessId" , businessId);
        List<Object[]> objList = this.getObjectArray(sqlBuilder);
        String expertGl = "";
        if(objList != null && objList.size() > 0){
            Object[] object =  objList.get(0);
            if(object.length > 0){

                expertGl = objList.get(0)[1].toString();
            }
        }
        return expertGl;
    }
}
