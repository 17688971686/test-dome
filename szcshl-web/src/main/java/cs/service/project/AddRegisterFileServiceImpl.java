package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    @Transactional
    public void update(AddRegisterFileDto[] addRegisterFileDtos) {
        if (addRegisterFileDtos != null && addRegisterFileDtos.length > 0) {
            for (AddRegisterFileDto addRegisterFileDto : addRegisterFileDtos) {
                AddRegisterFile addRegisterFile = addRegisterFileRepo.findById(addRegisterFileDto.getId());
                if (addRegisterFile != null) {
                    BeanCopierUtils.copyProperties(addRegisterFileDto, addRegisterFile);
                    Date now = new Date();
                    addRegisterFile.setCreatedDate(now);
                    addRegisterFile.setModifiedDate(now);
                    addRegisterFile.setCreatedBy(SessionUtil.getLoginName());
                    addRegisterFile.setModifiedBy(SessionUtil.getLoginName());
                    addRegisterFileRepo.save(addRegisterFile);
                }
            }
        }

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
    public Map<String, Object> initprint(String signid) {
        Date now = new Date();
        Map<String, Object> map = new HashMap<>();
        //根据signid获取所有数据登记补充资料
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.businessId.getName() + " =:signid");
        hqlBuilder.setParam("signid", signid);
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
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        SignDto signdto = new SignDto();
        signdto.setProjectname(sign.getProjectname());
        signdto.setFilecode(sign.getFilecode());
        signdto.setDesigncompanyName(sign.getDesigncompanyName());
        signdto.setProjectcode(sign.getProjectcode());
        signdto.setSendusersign(sign.getSendusersign());
        map.put("signdto", signdto);
        return map;

    }

    @Override
    public Map<String, Object> initRegisterFile(String signid) {
        Map<String, Object> map = new HashMap<String, Object>();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + AddRegisterFile.class.getSimpleName() + " where " + AddRegisterFile_.businessId.getName() + " =:signid");
        hqlBuilder.setParam("signid", signid);
        List<AddRegisterFile> financiallist = addRegisterFileRepo.findByHql(hqlBuilder);
        map.put("financiallist", financiallist);

        return map;
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