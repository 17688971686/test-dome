package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.UnitScore;
import cs.domain.project.UnitScore_;
import cs.domain.sys.Company_;
import cs.model.project.UnitScoreDto;
import cs.repository.repositoryImpl.project.UnitScoreRepo;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.utils.BeanCopierUtils;
import cs.domain.sys.Company;
import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.CompanyRepo;

@Service
public class CompanyServiceImpl implements CompanyService {
    private static Logger logger = Logger.getLogger(CompanyServiceImpl.class);
    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private UnitScoreRepo unitScoreRepo;

    @Override
    public PageModelDto<CompanyDto> get(ODataObj odataObj) {
        List<Company> comList = companyRepo.findByOdata(odataObj);
        List<CompanyDto> comDtoList = new ArrayList<>();

        for (Company item : comList) {

            CompanyDto comDto = new CompanyDto();

            comDto.setId(item.getId());
            comDto.setCoAddress(item.getCoAddress());
            comDto.setCoDept(item.getCoDept());
            comDto.setCoDeptName(item.getCoDeptName());
            comDto.setCoFax(item.getCoFax());
            comDto.setCoName(item.getCoName());
            comDto.setCoPC(item.getCoPC());
            comDto.setCoPhone(item.getCoPhone());
            comDto.setCoSite(item.getCoSite());
            comDto.setCoSynopsis(item.getCoSynopsis());
            comDto.setCoType(item.getCoType());
            comDtoList.add(comDto);

        }
        PageModelDto<CompanyDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(comDtoList);
        return pageModelDto;
    }

    @Override
    @Transactional
    public void createCompany(CompanyDto companyDto) {
        //判断单位名称是否添加
        Criteria criteria = companyRepo.getSession().createCriteria(Company.class);
        criteria.add(Restrictions.eq("coName", companyDto.getCoName()));
        List<Company> com = criteria.list();
        if (com.isEmpty()) {

            Company c = new Company();
            c.setId(UUID.randomUUID().toString());
            c.setCoAddress(companyDto.getCoAddress());
            c.setCoDept(companyDto.getCoDept());
            c.setCoDeptName(companyDto.getCoDeptName());
            c.setCoFax(companyDto.getCoFax());
            c.setCoName(companyDto.getCoName());
            c.setCoPC(companyDto.getCoPC());
            c.setCoPhone(companyDto.getCoPhone());
            c.setCoSite(companyDto.getCoSite());
            c.setCoType(companyDto.getCoType());
            c.setCoSynopsis(companyDto.getCoSynopsis());
            c.setCreatedBy(SessionUtil.getLoginName());
            c.setModifiedBy(SessionUtil.getLoginName());

            companyRepo.save(c);
            logger.info(String.format("创建单位，单位名:%s", companyDto.getCoName()));
        } else {
            throw new IllegalArgumentException(String.format("该单位已存在：%s 已经存在，请重新输入", companyDto.getCoName()));

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSignCompany(String name,String comType) {
        //判断单位名称是否添加
        Criteria criteria = companyRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Company_.coName.getName(), name));
        List<Company> com = criteria.list();
        if (com.isEmpty()) {
            Company c = new Company();
            c.setId(UUID.randomUUID().toString());
            c.setCoName(name);
            c.setCoType(comType);
            c.setCreatedBy(SessionUtil.getUserId());
            c.setModifiedBy(SessionUtil.getLoginName());
            c.setCreatedDate(new Date());
            c.setModifiedDate(new Date());
            companyRepo.save(c);
        }


    }

    @Override
    @Transactional
    public void deleteCompany(String id) {

        Company com = companyRepo.findById(id);
        if (com != null) {

            companyRepo.delete(com);
            //	this.deleteCompany(id);
            logger.info(String.format("删除单位，单位名coName:%s", com.getCoName()));
        }

    }

    @Override
    @Transactional
    public void deleteCompanys(String[] ids) {

        for (String id : ids) {

            this.deleteCompany(id);
            //companyRepo.delete(entity);
        }
        logger.info("批量删除单位");

    }

    @Override
    @Transactional
    public void updateCompany(CompanyDto companyDto) {

        Company c = companyRepo.findById(companyDto.getId());

        c.setCoAddress(companyDto.getCoAddress());
        c.setCoDept(companyDto.getCoDept());
        c.setCoDeptName(companyDto.getCoDeptName());
        c.setCoFax(companyDto.getCoFax());
        c.setCoName(companyDto.getCoName());
        c.setCoPC(companyDto.getCoPC());
        c.setCoPhone(companyDto.getCoPhone());
        c.setCoSite(companyDto.getCoSite());
        c.setCoType(companyDto.getCoType());
        c.setCoSynopsis(companyDto.getCoSynopsis());
        c.setCreatedBy(SessionUtil.getLoginName());
        c.setModifiedBy(SessionUtil.getLoginName());

        companyRepo.save(c);
        logger.info(String.format("编辑单位，单位名:%s", companyDto.getCoName()));
    }

    @Override
    public CompanyDto findByIdCompany(String id) {
        Company com = companyRepo.findById(id);
        CompanyDto comDto = new CompanyDto();
        BeanCopierUtils.copyProperties(com, comDto);
        return comDto;
    }

    @Override
    public List<CompanyDto> findCompanys() {
        List<Company> companyList = companyRepo.findAll();
        List<CompanyDto> comDtoList = new ArrayList<>();
        if (companyList != null && companyList.size() > 0) {
            companyList.forEach(x -> {
                CompanyDto comDto = new CompanyDto();
                BeanCopierUtils.copyProperties(x, comDto);
                comDto.setCreatedDate(x.getCreatedDate());
                comDto.setModifiedDate(x.getModifiedDate());
                comDtoList.add(comDto);
            });
        }
        return comDtoList;
    }

    @Override
    @Transactional
    public ResultMsg updateUnitScore(UnitScoreDto unitScoreDto) {
        boolean isUpdateScore = false;
        if (!Validate.isObject(unitScoreDto.getScore()) || unitScoreDto.getScore() <= 0) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你还没对专家进行评分！");
        }
        UnitScore unitScore = unitScoreRepo.findById(UnitScore_.id.getName(), unitScoreDto.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(unitScoreDto, unitScore);
        unitScoreRepo.save(unitScore);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

}
