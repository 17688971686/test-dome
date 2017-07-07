package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.PageModelDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;

@Service
public class AnnountmentServiceImpl implements AnnountmentService{

	@Autowired
	private AnnountmentRepo annountmentRepo;

	@Autowired
	private ICurrentUser currentUser;

	@Autowired
	private OrgRepo orgRepo;

	@Override
	public PageModelDto<AnnountmentDto> get(ODataObj odataobj) {
		List<Annountment> annList=annountmentRepo.findByOdata(odataobj);
		List<AnnountmentDto> annountmentDtoList=new ArrayList<>();
		PageModelDto<AnnountmentDto> pagemodelDto=new PageModelDto<>();
		for(Annountment annountment:annList){
			AnnountmentDto annDto=new AnnountmentDto();
			BeanCopierUtils.copyProperties(annountment, annDto);
			annountmentDtoList.add(annDto);
		}

		pagemodelDto.setValue(annountmentDtoList);
		pagemodelDto.setCount(odataobj.getCount());

		return pagemodelDto;
	}

	@Override
	@Transactional
	public void createAnnountment(AnnountmentDto annountmentDto) {
		Annountment annountment=new Annountment();
		BeanCopierUtils.copyProperties(annountmentDto, annountment);
		annountment.setAnId(UUID.randomUUID().toString());
		annountment.setCreatedBy(currentUser.getDisplayName());
		annountment.setCreatedDate(new Date());
		annountment.setModifiedBy(currentUser.getDisplayName());
		annountment.setModifiedDate(new Date());
		annountment.setAnDate(new Date());
		annountment.setAnUser(currentUser.getDisplayName());
		annountmentRepo.save(annountment);
		annountmentDto.setAnId(annountment.getAnId());

	}

	@Override
	@Transactional
	public String findAnOrg() {

		String loginName=currentUser.getLoginName();

		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("select o from " +Org.class.getSimpleName()+" o where o."+Org_.id.getName()+"=("   );
		hqlBuilder.append("select u."+User_.org.getName()+"."+Org_.id.getName()+" from "+User.class.getSimpleName()+" u where u."+User_.loginName.getName()+"=:loginName)");
		hqlBuilder.setParam("loginName", loginName);
		List<Org> orgList=orgRepo.findByHql(hqlBuilder);
		if(orgList!=null && orgList.size()>0){
			return orgList.get(0).getName();
		}else{
			return null;
		}

	}

	@Override
	@Transactional
	public AnnountmentDto findAnnountmentById(String anId) {
		Annountment annountment=annountmentRepo.findById(anId);
		AnnountmentDto annountmentDto=new AnnountmentDto();
		BeanCopierUtils.copyProperties(annountment, annountmentDto);
		return annountmentDto;
	}

	@Override
	@Transactional
	public void updateAnnountment(AnnountmentDto annountmentDto) {
		Annountment annountment=annountmentRepo.findById(annountmentDto.getAnId());
		if(annountment!=null){
			BeanCopierUtils.copyProperties(annountmentDto, annountment);
			annountment.setModifiedBy(currentUser.getDisplayName());
			annountment.setModifiedDate(new Date());
		}

		annountmentRepo.save(annountment);
	}

	@Override
	@Transactional
	public void deleteAnnountment(String id) {

		String[] ids=id.split(",");
		for(String anId: ids){
			Annountment ann=annountmentRepo.findById(anId);
			annountmentRepo.delete(ann);
		}
	}

	@Override
	@Transactional
	public List<AnnountmentDto> getAnnountment() {
		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("select a from "+ Annountment.class.getSimpleName() +" a order by "+Annountment_.isStick.getName() +" asc,"+Annountment_.anDate.getName()+" desc");
		List<Annountment> annList=annountmentRepo.findByHql(hqlBuilder);
		List<AnnountmentDto> annDtoList=new ArrayList<>();
		for(Annountment ann:annList){
			AnnountmentDto annDto=new AnnountmentDto();
			BeanCopierUtils.copyProperties(ann, annDto);
			annDtoList.add(annDto);
		}

		return annDtoList;
	}

	@Override
	@Transactional
	public AnnountmentDto postAritle(String id) {
		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick asc,andate desc)t from cs_annountment)");
		hqlBuilder.append(" select res.* from res where t=(");
		hqlBuilder.append("select res.t-1 from res where res.anid=:id)");
		hqlBuilder.setParam("id", id);
		List<Annountment> annList=annountmentRepo.findBySql(hqlBuilder);
		AnnountmentDto annDto=new AnnountmentDto();
		if(annList.size()>0){
			BeanCopierUtils.copyProperties(annList.get(0), annDto);
		}
		return annDto;
	}

	@Override
	@Transactional
	public AnnountmentDto nextArticle(String id) {
		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick asc,andate desc)t from cs_annountment)");
		hqlBuilder.append(" select res.* from res where t=(");
		hqlBuilder.append("select res.t+1 from res where res.anid=:id)");
		hqlBuilder.setParam("id", id);
		List<Annountment> annList=annountmentRepo.findBySql(hqlBuilder);
		AnnountmentDto annDto=new AnnountmentDto();
		if(annList.size()>0){
			BeanCopierUtils.copyProperties(annList.get(0), annDto);
		}
		return annDto;
	}

}
