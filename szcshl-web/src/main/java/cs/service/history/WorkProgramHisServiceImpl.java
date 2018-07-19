package cs.service.history;

import cs.common.constants.SysConstants;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.*;
import cs.domain.history.*;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.model.history.WorkProgramHisDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.history.*;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.sql.WorkSql;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkProgramHisServiceImpl implements WorkProgramHisService {
    private static Logger log = Logger.getLogger(WorkProgramHisServiceImpl.class);

    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private WorkProgramHisRepo workProgramHisRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private RoomBookingHisRepo roomBookingHisRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private ExpertReviewHisRepo expertReviewHisRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private ExpertSelectedHisRepo expertSelectedHisRepo;
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private ExpertSelConditionHisRepo expertSelConditionHisRepo;

    @Override
    public boolean copyWorkProgram(WorkProgram workProgram,String signId) throws RuntimeException {
        //1、工作方案留痕
        WorkProgramHis workProgramHis = new WorkProgramHis();
        BeanCopierUtils.copyProperties(workProgram,workProgramHis);
        workProgramHis.setSignId(signId);
        workProgramHisRepo.save(workProgramHis);
        //用hql删除
        workProgramRepo.deleteById(WorkProgram_.id.getName(),workProgram.getId());

        //2、会议预定留痕
        List<String> idList = new ArrayList<>();
        List<RoomBooking> roomBookList = roomBookingRepo.findByBusinessId(workProgram.getId());
        if(Validate.isList(roomBookList)){
            List<RoomBookingHis> roomBookingHisList = roomBookList.stream().map(item->{
                RoomBookingHis roomBookingHis = new RoomBookingHis();
                BeanCopierUtils.copyProperties(item,roomBookingHis);
                idList.add(item.getId());
                return roomBookingHis;
            }).collect(Collectors.toList());
            roomBookingHisRepo.bathUpdate(roomBookingHisList);
        }
        if(Validate.isList(idList)){
            roomBookingRepo.deleteById(RoomBooking_.id.getName(),StringUtils.join(idList.toArray(), SysConstants.SEPARATE_COMMA));
        }

        boolean isHaveExpertReview = false;
        ExpertReviewHis expertReviewHis = null;
        //3、专家抽取方案
        ExpertReview expertReview = expertReviewRepo.findByBusinessId(signId);
        if(Validate.isObject(expertReview)){
            isHaveExpertReview = true;
            expertReviewHis = expertReviewHisRepo.findByBusinessId(signId);
            if(!Validate.isObject(expertReviewHis) || !Validate.isString(expertReviewHis.getId())){
                expertReviewHis = new ExpertReviewHis();
                BeanCopierUtils.copyProperties(expertReview,expertReviewHis);
                expertReviewHis.setId(null);
                expertReviewHisRepo.save(expertReviewHis);
            }
        }
        if(isHaveExpertReview){
            //直接通过语句复制
            expertSelectedHisRepo.executeSql(WorkSql.copyExpertSelected(workProgram.getId()));
            expertSelectedRepo.deleteByBusinessId(workProgram.getId());

            //4、选择的专家留痕
            /*List<ExpertSelected> expertSelectedList = expertSelectedRepo.findAllByBusinessId(workProgram.getId());
            if(Validate.isList(expertSelectedList)){
                for(ExpertSelected ep : expertSelectedList){
                    ExpertSelectedHis expertSelectedHis = new ExpertSelectedHis();
                    BeanCopierUtils.copyPropertiesIgnoreProps(ep,expertSelectedHis,ExpertSelected_.expert.getName());
                    if(Validate.isObject(ep.getExpert()) || Validate.isString(ep.getExpert().getExpertID())){
                        Expert expert = expertRepo.findById(Expert_.expertID.getName(), ep.getExpert().getExpertID());
                        expertSelectedHis.setExpert(expert);
                    }
                    expertSelectedHis.setExpertReviewId(expertReview.getId());
                    expertSelectedHis.setId(null);
                    expertSelectedHisRepo.save(expertSelectedHis);
                }
                expertSelectedRepo.deleteByBusinessId(workProgram.getId());
            }*/

            expertSelConditionHisRepo.executeSql(WorkSql.copyExpertCondition(workProgram.getId()));
            expertSelConditionRepo.deleteByBusinessId(workProgram.getId());

            //5、抽取条件留痕
           /* List<ExpertSelCondition> expertSelConditionList = expertSelConditionRepo.findAllByBusinessId(workProgram.getId());
            if(Validate.isList(expertSelConditionList)){
                for(ExpertSelCondition esc : expertSelConditionList){
                    ExpertSelConditionHis expertSelConditionHis = new ExpertSelConditionHis();
                    BeanCopierUtils.copyProperties(esc,expertSelConditionHis);
                    expertSelConditionHis.setExpertReviewId(expertReview.getId());
                    expertSelConditionHis.setId(null);
                    expertSelConditionHisRepo.save(expertSelConditionHis);
                }
                expertSelConditionRepo.deleteByBusinessId(workProgram.getId());
            }*/
        }

        return true;
    }

    /**
     * 工作方案历史信息
     * @param signid
     * @param branchId
     * @return
     */
    @Override
    public List<WorkProgramHisDto> findBySignAndBranch(String signid, String branchId) {
        List<WorkProgramHis> workProgramHisList = workProgramHisRepo.findBySignAndBranch(signid,branchId);
        if(Validate.isList(workProgramHisList)){
            int countSize = workProgramHisList.size();
            List<WorkProgramHisDto> resultList = new ArrayList<>(countSize);
            for(int i=0;i<countSize;i++){
                WorkProgramHis workProgramHis = workProgramHisList.get(i);
                WorkProgramHisDto workProgramHisDto = new WorkProgramHisDto();
                BeanCopierUtils.copyProperties(workProgramHis,workProgramHisDto);
                workProgramHisRepo.initWPMeetingExp(workProgramHisDto, workProgramHis);
                resultList.add(workProgramHisDto);
            }
            return resultList;
        }
        return null;
    }
}
