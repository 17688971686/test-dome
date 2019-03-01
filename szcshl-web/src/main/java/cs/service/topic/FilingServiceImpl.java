package cs.service.topic;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.ProjectConstant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.domain.topic.Filing;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.topic.FilingDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AddRegisterFileRepo;
import cs.repository.repositoryImpl.topic.FilingRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: ldm
 * Date: 2017-9-4 15:40:48
 * Description: 课题归档 业务操作实现类
 */
@Service
public class FilingServiceImpl implements FilingService {

    @Autowired
    private FilingRepo filingRepo;
    @Autowired
    private TopicInfoRepo topicInfoRepo;
    @Autowired
    private AddRegisterFileRepo addRegisterFileRepo;

    @Override
    public PageModelDto<FilingDto> get(ODataObj odataObj) {
        PageModelDto<FilingDto> pageModelDto = new PageModelDto<FilingDto>();
        List<FilingDto> resultDtoList = new ArrayList<>();
        List<Filing> resultList = filingRepo.findByOdata(odataObj);
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                FilingDto modelDto = new FilingDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg save(FilingDto record) {
        if (!Validate.isString(record.getTopicId())) {
            return ResultMsg.error( "操作失败，请重新处理！");
        }
        Filing domain = new Filing();
        Date now = new Date();
        if (!Validate.isString(record.getId())) {
            BeanCopierUtils.copyProperties(record, domain);
            //设置关联关系
            TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), record.getTopicId());
            if (!Validate.isString(topicInfo.getIsFinishFiling())
                    || Constant.EnumState.NO.getValue().equals(topicInfo.getIsFinishFiling())) {
                topicInfo.setIsFinishFiling(Constant.EnumState.YES.getValue());
                topicInfoRepo.save(topicInfo);
            }
            domain.setTopicInfo(topicInfo);
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setCreatedDate(now);
        } else {
            domain = filingRepo.findById(record.getId());
            BeanCopierUtils.copyProperties(record, domain);
            //先删除拟补充资料函
            addRegisterFileRepo.deleteById(AddRegisterFile_.businessId.getName(), record.getId());
        }
        //2、设置归档编号
        if (!Validate.isString(domain.getFileNo())) {
            String year;
            if(null != domain.getFilingDate()){
                 year = DateUtils.converToString(domain.getFilingDate(), DateUtils.DATE_YEAR);
            }else{
                 year = DateUtils.converToString(new Date(), DateUtils.DATE_YEAR);
            }
            int maxSeq = filingRepo.findCurMaxSeq(year)+1;
            //序号改为单个数字的形式，去掉之前保留3个有效数字的格式
            /*if (maxSeq < 1000) {
                fileNumValue = String.format("%03d", Integer.valueOf(maxSeq));
            } else {
                fileNumValue = maxSeq + "";
            }*/
            if(maxSeq < 1){
                maxSeq = 1;
            }
            //课题代码(课题代码2017KT001，归档编号2016KD17001)
            StringBuilder fileNumValue = new StringBuilder();
            fileNumValue.append(domain.getFilingCode().substring(0, 4));
            fileNumValue.append(ProjectConstant.FILE_RECORD_KEY.KD.toString());
            fileNumValue.append(year.substring(2, 4));
            fileNumValue.append(maxSeq);
            domain.setFileNo(fileNumValue.toString());
        }

        //4、设置其他一些属性
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setModifiedDate(now);
        if(!Validate.isBlank(record.getIsGdy()) && "1".equals(record.getIsGdy())){
            if(Validate.isBlank(record.getFilingUser())){
                domain.setFilingUser(SessionUtil.getDisplayName());
            }
            if(record.getFilingDate()== null){
                domain.setFilingDate(new Date());
            }
        }
        //5、项目第一负责人签名
        if(Validate.isBlank(record.getPrincipal())){
            domain.setPrincipal(SessionUtil.getDisplayName());
        }

        filingRepo.save(domain);
        BeanCopierUtils.copyProperties(domain, record);
        //5、添加拟补充资料函
        if (Validate.isList(record.getRegisterFileDto())) {
            List<AddRegisterFile> registerFileList = new ArrayList<>(record.getRegisterFileDto().size());
            for (AddRegisterFileDto fdto : record.getRegisterFileDto()) {
                AddRegisterFile addRegisterFile = new AddRegisterFile();
                BeanCopierUtils.copyProperties(fdto, addRegisterFile);
                addRegisterFile.setBusinessId(domain.getId());
                addRegisterFile.setCreatedBy(SessionUtil.getUserId());
                addRegisterFile.setCreatedDate(now);
                addRegisterFile.setModifiedBy(SessionUtil.getUserId());
                addRegisterFile.setModifiedDate(now);
                registerFileList.add(addRegisterFile);
                BeanCopierUtils.copyProperties(addRegisterFile, fdto);
            }
            addRegisterFileRepo.bathUpdate(registerFileList);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", record.getTopicId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FilingDto record) {
        Filing domain = filingRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());

        filingRepo.save(domain);
    }

    @Override
    public FilingDto findById(String id) {
        FilingDto modelDto = new FilingDto();
        if (Validate.isString(id)) {
            Filing domain = filingRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {

    }

    /**
     * 根据课题ID初始化
     *
     * @param topicId
     * @return
     */
    @Override
    public FilingDto initByTopicId(String topicId) {
        FilingDto result = new FilingDto();
        Filing filing = filingRepo.findById("topId", topicId);
        if (filing == null || !Validate.isString(filing.getId())) {
            filing = new Filing();
            TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), topicId);
            filing.setTopicName(topicInfo.getTopicName());
            filing.setCooperator(topicInfo.getCooperator());
            filing.setFilingCode(topicInfo.getTopicCode());
        } else {
            //查询补充资料函信息
            List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(), filing.getId(), null);
            if (Validate.isList(registerFileList)) {
                List<AddRegisterFileDto> dtoList = new ArrayList<>(registerFileList.size());
                registerFileList.forEach(rgf -> {
                    AddRegisterFileDto dto = new AddRegisterFileDto();
                    BeanCopierUtils.copyProperties(rgf, dto);
                    dtoList.add(dto);
                });
                result.setRegisterFileDto(dtoList);
            }
        }
        BeanCopierUtils.copyProperties(filing, result);
        return result;
    }
}