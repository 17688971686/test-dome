package cs.service.project;

import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.UnitScore;
import cs.domain.sys.Company;
import cs.repository.repositoryImpl.project.UnitScoreRepo;
import cs.repository.repositoryImpl.sys.CompanyRepo;
import cs.service.sys.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static cs.common.constants.Constant.DEFAULT_DESIGN_COMPNAME;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * Created by Administrator on 2018/3/27.
 */
@Service
public class UnitScoreServiceImpl implements UnitScoreService {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private UnitScoreRepo unitScoreRepo;

    @Override
    public void decide(String designcompanyName, String singid,boolean isSignUser) {
        //查找单位
        Company company = companyRepo.findCompany(designcompanyName);
        if (company == null) {
            //没有时进行添加单位
            companyService.createSignCompany(designcompanyName, DEFAULT_DESIGN_COMPNAME,isSignUser);
        }
        //查找单位评分表数据
        UnitScore unitScore = unitScoreRepo.findUnitScore(singid);
        if (unitScore != null) {
            unitScore.setCompany(company);
            unitScoreRepo.save(unitScore);
        } else {
            //没有就添加
            UnitScore unitScores = new UnitScore();
            unitScores.setSignid(singid);
            unitScores.setCompany(company);
            unitScores.setId(UUID.randomUUID().toString());
            unitScores.setCreatedBy(isSignUser?SessionUtil.getUserId():SUPER_ACCOUNT);
            unitScores.setCreatedDate(new Date());
            unitScores.setModifiedBy(isSignUser?SessionUtil.getDisplayName():SUPER_ACCOUNT);
            unitScores.setCreatedDate(new Date());
            unitScoreRepo.save(unitScores);
        }

    }
}
