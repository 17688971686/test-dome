package cs.service.manager;

import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.project.SignDispaWork;
import cs.domain.topic.TopicInfo;
import cs.model.PageModelDto;
import cs.model.manager.ReviewFee;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 专家评审费发放超期（项目、课题）业务处理
 * Author: mcl
 * Date: 2017/11/4 13:12
 */
@Service
public class ReviewFeeServiceImpl implements  ReviewFeeService{

    @Autowired
    private ExpertReviewRepo expertReviewRepo;

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;

    @Autowired
    private TopicInfoRepo topicInfoRepo;

    /**
     * 查找评审费发放超期的（项目、课题）信息 (停用)
     * param businessType
     * @return
     */
    @Override
    public PageModelDto<ReviewFee> findOverTimeReviewFee( String businessType) {

        PageModelDto<ReviewFee> pageModelDto = new PageModelDto<>();
        List<ReviewFee> reviewFeeList = new ArrayList<>();
//        List<ExpertReview> expertReviewList = expertReviewRepo.findReviewOverTime();
        List<ExpertReview> expertReviewList = expertReviewRepo.findReviewOverTime(businessType);
        if(expertReviewList != null && expertReviewList.size()>0){
            for(ExpertReview expertReview : expertReviewList){
                ReviewFee reviewFee = new ReviewFee();
                if(businessType == null) {
                    //项目类型
                    if (expertReview != null
                            && Validate.isString(expertReview.getBusinessType())
                            && "SIGN".equals(expertReview.getBusinessType())
                            && Validate.isString(expertReview.getBusinessId())) {
                        SignDispaWork signDispaWork = signDispaWorkRepo.findSDPBySignId(expertReview.getBusinessId());
                        reviewFee.setBusinessId(expertReview.getBusinessId());
                        reviewFee.setBusinessType("SIGN");
                        reviewFee.setName(signDispaWork.getProjectname());
                    }
                    if (expertReview != null
                            && Validate.isString(expertReview.getBusinessType())
                            && "TOPIC".equals(expertReview.getBusinessType())
                            && Validate.isString(expertReview.getBusinessId())) {
                        TopicInfo topicInfo = topicInfoRepo.findTopByBusinessId(expertReview.getBusinessId());
                        reviewFee.setBusinessId(expertReview.getBusinessId());
                        reviewFee.setBusinessType("TOPIC");
                        reviewFee.setName(topicInfo.getTopicName());
                    }
                }else{
                    if("SIGN".equals(businessType) && "SIGN".equals(expertReview.getBusinessType())){
                        SignDispaWork signDispaWork = signDispaWorkRepo.findSDPBySignId(expertReview.getBusinessId());
                        reviewFee.setBusinessId(expertReview.getBusinessId());
                        reviewFee.setBusinessType("SIGN");
                        reviewFee.setName(signDispaWork.getProjectname());

                    }else if("TOPIC".equals(businessType) && "TOPIC".equals(expertReview.getBusinessType())){
                        TopicInfo topicInfo = topicInfoRepo.findTopByBusinessId(expertReview.getBusinessId());
                        reviewFee.setBusinessId(expertReview.getBusinessId());
                        reviewFee.setBusinessType("TOPIC");
                        reviewFee.setName(topicInfo.getTopicName());
                    }
                }
                if(Validate.isString(reviewFee)){
                    reviewFeeList.add(reviewFee);
                }
            }
        }

        pageModelDto.setCount(reviewFeeList.size());
        pageModelDto.setValue(reviewFeeList);


        return pageModelDto;
    }
}