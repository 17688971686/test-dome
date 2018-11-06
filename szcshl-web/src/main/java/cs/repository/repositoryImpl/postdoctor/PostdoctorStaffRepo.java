package cs.repository.repositoryImpl.postdoctor;

import cs.domain.postdoctor.PostdoctoralBase;
import cs.domain.postdoctor.PostdoctoralStaff;
import cs.model.postdoctor.PostdoctoralStaffDto;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 博士后基地 数据操作实现接口
 * author: zsl
 * Date: 2018-10-23 15:04:55
 */
public interface PostdoctorStaffRepo extends IRepository<PostdoctoralStaff, String> {

    /**
     * 查询在站人员-审核通过后
     * @return
     */
     List<PostdoctoralStaffDto> findStationStaff();

    /**
     * 通过名称获取在站人员信息
     * @return
     */
     Boolean findByName();




}
