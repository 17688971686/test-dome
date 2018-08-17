package cs.service.expert;

import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.*;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.*;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 专家抽取条件 业务操作实现类
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
@Service
public class ExpertSelConditionServiceImpl implements ExpertSelConditionService {
    private static Logger log = Logger.getLogger(ExpertSelConditionServiceImpl.class);
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;

    @Override
    public PageModelDto<ExpertSelConditionDto> get(ODataObj odataObj) {
        PageModelDto<ExpertSelConditionDto> pageModelDto = new PageModelDto<ExpertSelConditionDto>();
        List<ExpertSelCondition> resultList = expertSelConditionRepo.findByOdata(odataObj);
        List<ExpertSelConditionDto> resultDtoList = new ArrayList<ExpertSelConditionDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                ExpertSelConditionDto modelDto = new ExpertSelConditionDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    @Transactional
    public void save(ExpertSelConditionDto record) {
        ExpertSelCondition domain = new ExpertSelCondition();
        BeanCopierUtils.copyProperties(record, domain);
        expertSelConditionRepo.save(domain);
    }

    @Override
    @Transactional
    public void update(ExpertSelConditionDto record) {
        ExpertSelCondition domain = expertSelConditionRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        expertSelConditionRepo.save(domain);
    }

    @Override
    public ExpertSelConditionDto findById(String id) {
        ExpertSelConditionDto modelDto = new ExpertSelConditionDto();
        if (Validate.isString(id)) {
            ExpertSelCondition domain = expertSelConditionRepo.findById(ExpertSelCondition_.id.getName(),id);
            if(Validate.isObject(domain) && Validate.isString(domain.getId())){
                BeanCopierUtils.copyProperties(domain, modelDto);
            }
        }
        return modelDto;
    }

    /**
     * 删除专家抽取条件(同时删除已经抽取的专家)
     *
     * @param ids
     */
    @Override
    @Transactional
    public ResultMsg delete(String reviewId,String ids) {
        try{
            ExpertReview expertReview = expertReviewRepo.findById(reviewId);
            if(Validate.isObject(expertReview)){
                int cSize = Validate.isList(expertReview.getExpertSelConditionList())?expertReview.getExpertSelConditionList().size():0;
                int delLength = StringUtil.getSplit(ids,",").size();
                //如果是清空抽取条件，则可以重新进行抽取
                if(cSize > 0 && cSize == delLength){
                    expertReview.setSelectIndex(null);
                    expertReview.setExtractInfo(null);
                    expertReview.setFinishExtract(null);
                    expertReview.setState(null);
                    expertReviewRepo.save(expertReview);
                }
                expertSelConditionRepo.deleteById(ExpertSelCondition_.id.getName(), ids);
                expertSelectedRepo.deleteById(ExpertSelected_.conditionId.getName(),ids);
            }
            return ResultMsg.ok("删除成功！");
        }catch (Exception e){
            log.error("删除专家抽取条件异常："+e.getMessage());
            return ResultMsg.error("删除异常，错误问题已记录，请联系系统管理员处理！");
        }
    }

    /**
     * 保存专家抽取条件信息
     *
     * @param recordList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ResultMsg saveConditionList(String businessId, String minBusinessId,String businessType, String reviewId, ExpertSelConditionDto[] recordList) {
        if (recordList != null && recordList.length > 0) {
            ExpertReview reviewObj = new ExpertReview();
            if (Validate.isString(reviewId)) {
                reviewObj = expertReviewRepo.findById(ExpertReview_.id.getName(), reviewId);
                if( null != reviewObj.getPayDate() && null != reviewObj.getReviewDate() && (new Date()).after(reviewObj.getReviewDate())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"已发送专家评审费，不能对方案再修改！");
                }
            } else {
                Date now = new Date();
                reviewObj.setBusinessId(businessId);    //设置业务ID
                reviewObj.setCreatedBy(SessionUtil.getLoginName());
                reviewObj.setModifiedBy(SessionUtil.getLoginName());
                reviewObj.setCreatedDate(now);
                reviewObj.setModifiedDate(now);
                reviewObj.setBusinessType(businessType);
                //评审会标题
                expertReviewRepo.initReviewTitle(reviewObj,businessId,businessType);
                //获取评审会日期
                if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
                    WorkProgram wp = workProgramRepo.findById(WorkProgram_.id.getName(),minBusinessId);
                    //专家函评
                    if(Constant.MergeType.REVIEW_LEETER.getValue().equals(wp.getReviewType())){
                        reviewObj.setReviewDate(wp.getLetterDate());
                    }
                }
                if(reviewObj.getReviewDate() == null){
                    reviewObj.setReviewDate(roomBookingRepo.getMeetingDateByBusinessId(minBusinessId));
                }
                expertReviewRepo.save(reviewObj);
            }
            //先删除之前的，再保存
            expertSelConditionRepo.deleteById("expertReviewId", reviewObj.getId());
            List<ExpertSelConditionDto> resultList = new ArrayList<>(recordList.length);
            List<ExpertSelCondition> saveList = new ArrayList<>(recordList.length);
            for (int i = 0, l = recordList.length; i < l; i++) {
                ExpertSelConditionDto rl = recordList[i];
                ExpertSelCondition domain = new ExpertSelCondition();
                BeanCopierUtils.copyProperties(rl, domain);
                domain.setExpertReview(reviewObj);
                domain.setBusinessId(minBusinessId);        //设置业务ID
                domain.setSelectIndex(domain.getSelectIndex() == null ? 0 : domain.getSelectIndex());
                saveList.add(domain);
                expertSelConditionRepo.save(domain);
            }
            expertSelConditionRepo.bathUpdate(saveList);

            //设置返回值
            for (ExpertSelCondition sl : saveList) {
                ExpertSelConditionDto dto = new ExpertSelConditionDto();
                BeanCopierUtils.copyProperties(sl, dto);
                dto.setExpertReviewId(reviewObj.getId());
                resultList.add(dto);
            }
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", resultList);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败，获取不到抽取条件信息！");
        }

    }

    /**
     * 统计专家抽取设定人数
     * @param minBusinessId
     * @return
     */
    @Override
    public int getExtractEPCount(String minBusinessId) {
        return expertSelConditionRepo.getExtractEPCount(minBusinessId);
    }

}