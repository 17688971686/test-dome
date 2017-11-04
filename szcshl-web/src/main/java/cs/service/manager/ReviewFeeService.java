package cs.service.manager;

import cs.model.PageModelDto;
import cs.model.manager.ReviewFee;

import java.util.List;

/**
 * Created by MCL
 * 2017/11/4
 */
public interface ReviewFeeService {

    /**
     * 查找评审费发放超期的（项目、课题）信息
     * param businessType
     * @return
     */
    PageModelDto<ReviewFee>  findOverTimeReviewFee(String businessType);
}
