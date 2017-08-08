package cs.service.project;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.domain.project.Sign;
import cs.model.project.AddSuppLetterDto;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.project.SignRepo;


/**
 * Description: 项目资料补充函 业务操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Service
public class AddSuppLetterServiceImpl  implements AddSuppLetterService {

	@Autowired
	private AddSuppLetterRepo addSuppLetterRepo;
	
	@Autowired
	private SignRepo signRepo;
	
	@Override
	public AddSuppLetterDto initSupp(String signid,String id) {
		AddSuppLetterDto addSuppLetterDto=new AddSuppLetterDto();
		if(Validate.isString(id)){
			
			AddSuppLetter addSuppLetter = addSuppLetterRepo.getById(id);
			BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetter, addSuppLetterDto);
		}else{
			
			Date now = new Date();
			addSuppLetterDto.setUserName(SessionUtil.getLoginName());
			addSuppLetterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
			addSuppLetterDto.setSuppLetterTime(now);
			addSuppLetterDto.setSignid(signid);
		}
		return addSuppLetterDto;
	}
	
	@Override
	@Transactional
	public void addSupp(AddSuppLetterDto addSuppLetterDto) {
		if(addSuppLetterDto==null){
			 throw new IllegalArgumentException(String.format("请填写数据！"));
		}else{
			if(Validate.isString(addSuppLetterDto.getId())){
				return;
			}
			AddSuppLetter addSuppLetter = new AddSuppLetter();
			BeanCopierUtils.copyProperties(addSuppLetterDto,addSuppLetter);
			addSuppLetter.setId(UUID.randomUUID().toString());
			addSuppLetter.setCreatedBy(SessionUtil.getLoginName());
			addSuppLetter.setModifiedBy(SessionUtil.getLoginName());
			Date now =new Date();
			addSuppLetter.setModifiedDate(now);
			addSuppLetter.setCreatedDate(now);
			addSuppLetterRepo.save(addSuppLetter);
			
			if(Validate.isString(addSuppLetter.getSignid())){//把关联id保存到收文中
			    	Sign sign = signRepo.getById(addSuppLetter.getSignid());
			    		sign.setSuppletterid(addSuppLetter.getId());
			    		signRepo.save(sign);
			}
		}
		
	}
	@Override
	@Transactional
	public void updateSupp(AddSuppLetterDto addSuppLetterDto) {
		if(addSuppLetterDto==null){
			throw new IllegalArgumentException(String.format("操作失败，无法获取拟稿信息！"));
		}else{
			if(!Validate.isString(addSuppLetterDto.getId())){
				return;
			}
			AddSuppLetter addSuppLetter = new AddSuppLetter();
			BeanCopierUtils.copyProperties(addSuppLetterDto,addSuppLetter);
			addSuppLetter.setCreatedBy(SessionUtil.getLoginName());
			addSuppLetter.setModifiedBy(SessionUtil.getLoginName());
			Date now =new Date();
			addSuppLetter.setModifiedDate(now);
			addSuppLetter.setCreatedDate(now);
			addSuppLetterRepo.save(addSuppLetter);
		}
		
	}
	
	@Override
	public AddSuppLetterDto getbyId(String id) {
		AddSuppLetter addSuppLetter = addSuppLetterRepo.getById(id);
		AddSuppLetterDto addSuppLetterDto=new AddSuppLetterDto();
		BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetter, addSuppLetterDto);
			return addSuppLetterDto;
		}
	
	
	 // 生成文件字号
    @Override
    @Transactional
    public ResultMsg fileNum(String id) {
    	AddSuppLetter addSuppLetter = addSuppLetterRepo.getById(id);
        //获取拟稿最大编号
        int curYearMaxSeq = findCurMaxSeq(addSuppLetter.getDisapDate());
        String filenum = Constant.DISPATCH_PREFIX+"["+ DateUtils.converToString(addSuppLetter.getDisapDate(),"yyyy")+"]"+(curYearMaxSeq + 1);
        addSuppLetter.setFilenum(filenum);
        addSuppLetter.setFileSeq((curYearMaxSeq + 1));
        addSuppLetterRepo.save(addSuppLetter);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), filenum);

    }
    
    /**
     * 获取最大拟稿编号
     * @param dispatchDate
     * @return
     */
    private int findCurMaxSeq(Date dispaDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+AddSuppLetter_.fileSeq.getName()+") from cs_add_suppLetter where TO_CHAR("+AddSuppLetter_.disapDate.getName()+",'yyyy') = :dispDate ");
        sqlBuilder.setParam("dispDate",DateUtils.converToString(dispaDate,"yyyy"));
        return addSuppLetterRepo.returnIntBySql(sqlBuilder);
    }

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}


}