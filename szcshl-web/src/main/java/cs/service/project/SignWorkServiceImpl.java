package cs.service.project;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.SignWork;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
import cs.model.project.SignWorkDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignWorkRepo;
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
     * @param odataObj
     * @return
     */
    @Override
    public List<SignWorkDto> fingSignWorkList(ODataObj odataObj) {
        List<SignWork> allList = signWorkRepo.fingSignWorkList(odataObj);
        List<SignWorkDto> resultList = new ArrayList<>();
        SignWorkDto signWorkDto = null;
        List<SignWorkDto> signWorkDtoList = null;
        if(Validate.isList(allList)){
            String oldSignId = "";
            for(int i=0,l=allList.size();i<l;i++){
                boolean isSame = false;
                SignWork signWork = allList.get(i);
                if(!Validate.isString(oldSignId) || !oldSignId.equals(signWork.getSignId())){
                    if(Validate.isList(signWorkDtoList)){
                        signWorkDto.setSignWorkDtoList(signWorkDtoList);
                        resultList.add(signWorkDto);
                    }
                    oldSignId = signWork.getSignId();
                    signWorkDto = new SignWorkDto();
                    signWorkDtoList = new ArrayList<>();
                }else{
                    isSame = true;
                }

                if(isSame){
                    SignWorkDto cSignWorkDto = new SignWorkDto();
                    BeanCopierUtils.copyProperties(signWork,cSignWorkDto);
                    signWorkDtoList.add(cSignWorkDto);
                }else{
                    BeanCopierUtils.copyProperties(signWork,signWorkDto);
                }

                //最后一个元素
                if( i == l-1){
                    if(Validate.isList(signWorkDtoList)){
                        signWorkDto.setSignWorkDtoList(signWorkDtoList);
                    }
                    resultList.add(signWorkDto);
                }
            }
        }
        return resultList;
    }
}