package cs.service.project;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cs.domain.project.Sign_;
import cs.domain.sys.User;
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
	
	@Autowired
	private SignPrincipalService signPrincipalService;
	
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
	/**
	 * 保存补充资料函
	 */
	@Override
	@Transactional
	public  ResultMsg addSupp(AddSuppLetterDto addSuppLetterDto,Boolean isaddSuppLettr) {
		if(Validate.isString(addSuppLetterDto.getSignid())){
			AddSuppLetter addSuppLetter = null;
			Date now =new Date();
			if(Validate.isString(addSuppLetterDto.getId())){
				addSuppLetter= addSuppLetterRepo.findById(addSuppLetterDto.getId());
				BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetterDto, addSuppLetter);
			}else{
				addSuppLetter = new AddSuppLetter();
				BeanCopierUtils.copyProperties(addSuppLetterDto,addSuppLetter);
				addSuppLetter.setId(UUID.randomUUID().toString());
				addSuppLetter.setCreatedBy(SessionUtil.getUserInfo().getId());
				addSuppLetter.setModifiedBy(SessionUtil.getUserInfo().getId());
				//addSuppLetter.setUserId(SessionUtil.getUserInfo().getId());
			}
			addSuppLetter.setModifiedDate(now);
			addSuppLetter.setCreatedDate(now);
		
			addSuppLetterRepo.save(addSuppLetter);
			//addSuppLetterDto.setId(addSuppLetter.getId());
			return new ResultMsg(true,Constant.MsgCode.OK.getValue(),"操作成功！",addSuppLetterDto);
		}else{
			 return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");
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


	/**
	 * 初始化补充资料函
	 */
	@Override
	public AddSuppLetterDto initSuppLetter(String signid,String id) {
		AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
		HqlBuilder hql = HqlBuilder.create();
		hql.append(" from "+AddSuppLetter.class.getSimpleName() +" where "+ AddSuppLetter_.signid.getName() +" = :signid ");
		hql.setParam("signid", signid);
		List<AddSuppLetter> suppletterlist = addSuppLetterRepo.findByHql(hql);
		if(suppletterlist !=null && suppletterlist.size() > 0){
			AddSuppLetter suppletter =suppletterlist.get(0);
			BeanCopierUtils.copyProperties(suppletter, suppletterDto);
		}else{
			Sign sign = signRepo.findById(signid);
		    User mainUser = signPrincipalService.getMainPriUser(signid);
		    //suppletterDto.setUserName(mainUser.getDisplayName());
		    suppletterDto.setUserName(SessionUtil.getLoginName());
		    suppletterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
		    suppletterDto.setSignid(sign.getSignid());
		    suppletterDto.setTitle("《"+sign.getProjectname()+sign.getReviewstage()+"》");
		}
		return suppletterDto;
	}

	/**
	 * 生成文件字号
	 */
	@Override
	@Transactional
	public void fileNum(String id) {
		AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(id);
		//获取拟稿最大编号
		int curYearMaxSeq = findCurMaxSeq(addSuppLetter.getDisapDate());
		String filenum = Constant.DISPATCH_PREFIX+"["+ DateUtils.converToString(addSuppLetter.getDisapDate(),"yyyy")+"]"+(curYearMaxSeq + 1);
		addSuppLetter.setFilenum(filenum);
		addSuppLetter.setFileSeq((curYearMaxSeq + 1));
		addSuppLetterRepo.save(addSuppLetter);
	}
	@Override
	public AddSuppLetterDto findByIdSuppLetter(String id) {
		AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
		if(Validate.isString(id)){
			AddSuppLetter addSuppletter = addSuppLetterRepo.findById(id);
			BeanCopierUtils.copyProperties(addSuppletter, suppletterDto);
		}
		return suppletterDto;
	}




}