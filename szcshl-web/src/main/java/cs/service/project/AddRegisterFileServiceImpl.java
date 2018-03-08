package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.project.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.project.FileRecordDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AddRegisterFileRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;

/**
 * Description: 登记补充资料 业务操作实现类
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
@Service
public class AddRegisterFileServiceImpl implements AddRegisterFileService {

    @Autowired
    private AddRegisterFileRepo addRegisterFileRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private FileRecordRepo fileRecordRepo;

    @Override
    public PageModelDto<AddRegisterFileDto> get(ODataObj odataObj) {
        PageModelDto<AddRegisterFileDto> pageModelDto = new PageModelDto<AddRegisterFileDto>();
        List<AddRegisterFile> resultList = addRegisterFileRepo.findByOdata(odataObj);
        List<AddRegisterFileDto> listDto = new ArrayList<>();
        if (resultList.size() > 0) {
            for (AddRegisterFile addRegisterFile : resultList) {
                AddRegisterFileDto addRegisterFileDto = new AddRegisterFileDto();
                BeanCopierUtils.copyProperties(addRegisterFile, addRegisterFileDto);
                listDto.add(addRegisterFileDto);
            }
        }
        List<AddRegisterFileDto> resultDtoList = new ArrayList<AddRegisterFileDto>(resultList.size());
        pageModelDto.setValue(listDto);
        pageModelDto.setCount(odataObj.getCount());
        return pageModelDto;
    }

    @Override
    @Transactional
    public void save(AddRegisterFileDto addRegisterFileDtos) {
        AddRegisterFile registerFile = new AddRegisterFile();
        BeanCopierUtils.copyProperties(addRegisterFileDtos, registerFile);
        Date now = new Date();
        if (addRegisterFileDtos.getId() == null) {
            registerFile.setId(UUID.randomUUID().toString());
        }
        registerFile.setCreatedBy(SessionUtil.getUserInfo().getId());
        registerFile.setModifiedBy(SessionUtil.getUserInfo().getId());
        registerFile.setCreatedDate(now);
        registerFile.setModifiedDate(now);
        addRegisterFileRepo.save(registerFile);
    }

    /**
     * 批量保存
     *
     * @param addRegisterFileDtos
     * @return
     */
    @Override
    @Transactional
    public ResultMsg bathSave(AddRegisterFileDto[] addRegisterFileDtos) {
        if (addRegisterFileDtos != null && addRegisterFileDtos.length > 0) {

            //更改收文状态
            Sign sign = signRepo.findById(Sign_.signid.getName(), addRegisterFileDtos[0].getBusinessId());
            if (sign != null && (!Validate.isString(sign.getIsSupplementary()) || Constant.EnumState.NO.getValue().equals(sign.getIsSupplementary()))) {
                sign.setIsSupplementary(Constant.EnumState.YES.getValue());
                signRepo.save(sign);
            }
            //更新归档拟补充资料信息
            FileRecord fileRecord = fileRecordRepo.findById(Sign_.signid.getName(), addRegisterFileDtos[0].getBusinessId());
            if (fileRecord != null && (!Validate.isString(fileRecord.getIsSupplementary()) ||
                    Constant.EnumState.NO.getValue().equals(fileRecord.getIsSupplementary()))) {
                fileRecord.setIsSupplementary(Constant.EnumState.YES.getValue());
                fileRecordRepo.save(fileRecord);
            }

            List<AddRegisterFileDto> resultList = new ArrayList<>(addRegisterFileDtos.length);
            List<AddRegisterFile> saveList = new ArrayList<>(addRegisterFileDtos.length);
            addRegisterFileRepo.deleteByBusIdAndBusType(addRegisterFileDtos[0].getBusinessId() , addRegisterFileDtos[0].getBusinessType());
//            addRegisterFileRepo.deleteById(AddRegisterFile_.businessId.getName(), addRegisterFileDtos[0].getBusinessId());
            Date now = new Date();
            for (AddRegisterFileDto addRegisterFileDto : addRegisterFileDtos) {
                AddRegisterFile addRegisterFile = new AddRegisterFile();
                BeanCopierUtils.copyProperties(addRegisterFileDto, addRegisterFile);

                addRegisterFile.setCreatedDate(now);
                addRegisterFile.setCreatedBy(SessionUtil.getDisplayName());
                addRegisterFile.setModifiedDate(now);
                addRegisterFile.setModifiedBy(SessionUtil.getDisplayName());

                if(addRegisterFileDto.getBusinessType()==null){
                    addRegisterFile.setBusinessType(Constant.AddRegisterFileType.BCZL.getValue());
                }
                saveList.add(addRegisterFile);
            }
            addRegisterFileRepo.bathUpdate(saveList);
            saveList.forEach(sl -> {
                AddRegisterFileDto dto = new AddRegisterFileDto();
                BeanCopierUtils.copyProperties(sl, dto);
                resultList.add(dto);
            });

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", resultList);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先填写补充资料！");
        }
    }


    /**
     * 通过业务id 和 业务类型查询
     * @param businessId
     * @param businessType
     * @return
     */
    @Override
    public List<AddRegisterFile> findByBusIdAndBusType(String businessId, Integer businessType) {
        return addRegisterFileRepo.findByBusIdAndBusType(businessId , businessType);
    }

    @Override
    @Transactional
    public AddRegisterFileDto findById(String id) {
        AddRegisterFileDto modelDto = new AddRegisterFileDto();
        return modelDto;
    }

    /**
     * 根据ID删除
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteRegisterFile(String id) {
        addRegisterFileRepo.deleteById(AddRegisterFile_.id.getName(), id);
    }

    @Override
    public List<AddRegisterFileDto> findbySuppdate(String suppDate) {
        List<AddRegisterFileDto> dtoList = new ArrayList<>();
        HqlBuilder hqlBuilder1 = HqlBuilder.create();
        hqlBuilder1.append("from " + AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.suppleDate.getName() + " =:suppleDate");
        hqlBuilder1.setParam("suppleDate", suppDate);
        List<AddRegisterFile> addRegisterFilelist = addRegisterFileRepo.findByHql(hqlBuilder1);
        if (!addRegisterFilelist.isEmpty()) {
            for (AddRegisterFile addRegisterFile : addRegisterFilelist) {
                AddRegisterFileDto addRegisterFileDto = new AddRegisterFileDto();
                BeanCopierUtils.copyProperties(addRegisterFile, addRegisterFileDto);
                dtoList.add(addRegisterFileDto);
            }
        }
        return dtoList;
    }

    @Override
    public Map<String, Object> initprint(String businessId) {
        Date now = new Date();
        Map<String, Object> map = new HashMap<>();
        //根据signid获取所有数据登记补充资料
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.businessId.getName() + " =:businessId");
        hqlBuilder.setParam("businessId", businessId);
        List<AddRegisterFile> addRegisterFileList = addRegisterFileRepo.findByHql(hqlBuilder);
        List<AddRegisterFileDto> AddRegisterFileDtoList = new ArrayList<>();
        List<Date> suppDate = new ArrayList<>();
        for (AddRegisterFile addRegisterFile : addRegisterFileList) {
            AddRegisterFileDto addRegisterFileDto = new AddRegisterFileDto();
            BeanCopierUtils.copyProperties(addRegisterFile, addRegisterFileDto);
            AddRegisterFileDtoList.add(addRegisterFileDto);
            suppDate.add(addRegisterFileDto.getSuppleDate());
        }
        map.put("AddRegisterFileDtoList", AddRegisterFileDtoList);
        map.put("printDate", now);

        //根据signid获取收文相关内容
        Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
        SignDto signdto = new SignDto();
        signdto.setProjectname(sign.getProjectname());
        signdto.setFilecode(sign.getFilecode());
        signdto.setDesigncompanyName(sign.getDesigncompanyName());
        signdto.setProjectcode(sign.getProjectcode());
        signdto.setSendusersign(sign.getSendusersign());
        map.put("signdto", signdto);
        return map;

    }

    /**
     * @param businessId
     * @return
     */
    @Override
    public List<AddRegisterFileDto> findByBusinessId(String businessId) {
        List<AddRegisterFileDto> resultList = new ArrayList<>();
        List<AddRegisterFile> fileList = addRegisterFileRepo.findByBusinessId(businessId);
        if (Validate.isList(fileList)) {
            fileList.forEach(fl -> {
                AddRegisterFileDto rfDto = new AddRegisterFileDto();
                BeanCopierUtils.copyProperties(fl, rfDto);
                resultList.add(rfDto);
            });
        }
        return resultList;
    }

    @Override
    public List<AddRegisterFileDto> initRegisterFileData(ODataObj odataObj) {
        List<AddRegisterFile> registerFilelist = addRegisterFileRepo.findByOdata(odataObj);
        List<AddRegisterFileDto> fileDtoList = new ArrayList<AddRegisterFileDto>();
        for (AddRegisterFile file : registerFilelist) {
            if (file.getCreatedBy().equals(SessionUtil.getUserInfo().getId())) {
                AddRegisterFileDto registerDto = new AddRegisterFileDto();
                BeanCopierUtils.copyProperties(file, registerDto);
                fileDtoList.add(registerDto);
            }
        }
        return fileDtoList;
    }

}