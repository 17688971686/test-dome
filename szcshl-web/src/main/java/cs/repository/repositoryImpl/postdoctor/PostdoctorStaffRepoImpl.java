package cs.repository.repositoryImpl.postdoctor;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.domain.postdoctor.PostdoctoralStaff;
import cs.domain.postdoctor.PostdoctoralStaff_;
import cs.model.postdoctor.PostdoctoralStaffDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


/**
 * Description: 博士后基地 数据操作实现类
 * author: zsl
 * Date: 2018-10-23 15:04:55
 */
@Repository
public class PostdoctorStaffRepoImpl extends AbstractRepository<PostdoctoralStaff, String> implements PostdoctorStaffRepo {

    @Override
    public List<PostdoctoralStaffDto> findStationStaff() {

        List<PostdoctoralStaffDto> dtoList = new ArrayList<>();

        Criteria criteria = this.getExecutableCriteria();
        criteria.add(Restrictions.eq(PostdoctoralStaff_.status.getName() , Constant.EnumState.STOP.getValue()));
        List<PostdoctoralStaff> staffList = criteria.list();
        if(staffList != null && staffList.size() > 0 ){
            for(PostdoctoralStaff p : staffList){
                PostdoctoralStaffDto dto = new PostdoctoralStaffDto();
                BeanCopierUtils.copyPropertiesIgnoreProps(p , dto);
                dtoList.add(dto);
            }

        }
        return dtoList;
    }

    @Override
    public Boolean findByName() {

        Criteria criteria = this.getExecutableCriteria();
        criteria.add(Restrictions.like(PostdoctoralStaff_.name.getName() , SessionUtil.getLoginName()));
        criteria.add(Restrictions.eq(PostdoctoralStaff_.status.getName() , Constant.EnumState.STOP.getValue()));
        List<PostdoctoralStaff> psList = criteria.list();
        if(psList != null && psList.size() > 0 ){
            return true;
        }else{
            return false;
        }
    }
}