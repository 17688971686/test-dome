package cs.service.project;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.SignWork;
import cs.domain.project.SignWork_;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
import cs.model.project.SignWorkDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignWorkRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SignWorkServiceImpl implements SignWorkService {

    @Autowired
    private SignWorkRepo signWorkRepo;


    /**
     * 查询在办项目的工作方案专家抽取信息
     *
     * @param odataObj
     * @return
     */
    @Override
    public List<SignWorkDto> fingSignWorkList(ODataObj odataObj) {
        List<SignWork> allList = signWorkRepo.fingSignWorkList(odataObj);
        List<SignWorkDto> resultList = new ArrayList<>();
        SignWorkDto signWorkDto = null;
        List<SignWorkDto> signWorkDtoList = null;
        if (Validate.isList(allList)) {
            String oldSignId = "";
            for (int i = 0, l = allList.size(); i < l; i++) {
                boolean isSame = false;
                SignWork signWork = allList.get(i);
                //判断oldSignId是否是一个字符串，或者当前遍历业务id不等于oldSignId
                if (!Validate.isString(oldSignId) || !oldSignId.equals(signWork.getSignId())) {
                    //如果是个集合，则给对象添加集合数据
                    if (Validate.isList(signWorkDtoList)) {
                        signWorkDto.setSignWorkDtoList(signWorkDtoList);
                    }
                    //每次遍历先将对象存入结果集
                    resultList.add(signWorkDto);
                    //再将业务ID赋值给oldSignId ， 并重新定义对象以及集合
                    oldSignId = signWork.getSignId();
                    signWorkDto = new SignWorkDto();
                    signWorkDtoList = new ArrayList<>();
                } else {
                    isSame = true;
                }

                //如果oldSignId是字符串 或者业务id等于oldSignId, 则将遍历的对象之间赋值给Dto，并添加到结果集中
                if (isSame) {
                    SignWorkDto cSignWorkDto = new SignWorkDto();
                    BeanCopierUtils.copyProperties(signWork, cSignWorkDto);
                    signWorkDtoList.add(cSignWorkDto);
                } else {
                    BeanCopierUtils.copyProperties(signWork, signWorkDto);
                }

                //最后一个元素
                if (i == l - 1) {
                    if (Validate.isList(signWorkDtoList)) {
                        signWorkDto.setSignWorkDtoList(signWorkDtoList);
                    }
                    resultList.add(signWorkDto);
                }
            }
        }
        return resultList;
    }

    @Override
    public SignWorkDto fingSignWorkById(String signId) {
        Criteria criteria = signWorkRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SignWork_.signId.getName(), signId));
        List<SignWork> allList = criteria.list();
        SignWorkDto result = new SignWorkDto();
        List<SignWorkDto> signWorkDtoList = new ArrayList<>();
        if (Validate.isList(allList)) {
            for (int i = 0, l = allList.size(); i < l; i++) {
                SignWork signWork = allList.get(i);
                if (i == 0) {
                    BeanCopierUtils.copyProperties(signWork, result);
                }else{
                    SignWorkDto cSignWorkDto = new SignWorkDto();
                    BeanCopierUtils.copyProperties(signWork, cSignWorkDto);
                    signWorkDtoList.add(cSignWorkDto);
                }

                //最后一个元素
                if (i == l - 1) {
                    if (Validate.isList(signWorkDtoList)) {
                        result.setSignWorkDtoList(signWorkDtoList);
                    }
                }
            }
        }
        return result;
    }

}