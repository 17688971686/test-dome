package cs.service.book;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.book.BookBuy;
import cs.domain.book.BookBuyBusiness;
import cs.model.PageModelDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.book.BookBuyBusinessRepo;
import cs.repository.repositoryImpl.book.BookBuyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 图书采购申请业务信息 业务操作实现类
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
@Service
public class BookBuyBusinessServiceImpl  implements BookBuyBusinessService {

	@Autowired
	private BookBuyBusinessRepo bookBuyBusinessRepo;
	@Autowired
	private BookBuyRepo bookBuyRepoRepo;
	@Override
	public PageModelDto<BookBuyBusinessDto> get(ODataObj odataObj) {
		PageModelDto<BookBuyBusinessDto> pageModelDto = new PageModelDto<BookBuyBusinessDto>();
		List<BookBuyBusiness> resultList = bookBuyBusinessRepo.findByOdata(odataObj);
		List<BookBuyBusinessDto> resultDtoList = new ArrayList<BookBuyBusinessDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				BookBuyBusinessDto modelDto = new BookBuyBusinessDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(BookBuyBusinessDto record) {
		BookBuyBusiness domain = new BookBuyBusiness(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		bookBuyBusinessRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(BookBuyBusinessDto record) {
		BookBuyBusiness domain = bookBuyBusinessRepo.findById(record.getBusinessId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		bookBuyBusinessRepo.save(domain);
	}

	@Override
	public BookBuyBusinessDto findById(String id) {		
		BookBuyBusinessDto modelDto = new BookBuyBusinessDto();
		if(Validate.isString(id)){
			BookBuyBusiness domain = bookBuyBusinessRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}

	@Override
	public ResultMsg saveBooksDetailList(BookBuyDto[] bookList) {
		if(bookList != null && bookList.length > 0){
			Date now = new Date();
			BookBuyBusiness bookBuyBusiness = new BookBuyBusiness();
			bookBuyBusiness.setBusinessId(UUID.randomUUID().toString());
			//bookBuyBusiness.setBookBuyList(bookBuyList);
			bookBuyBusiness.setCreatedBy(SessionUtil.getDisplayName());
			bookBuyBusiness.setModifiedBy(SessionUtil.getDisplayName());
			bookBuyBusiness.setCreatedDate(now);
			bookBuyBusiness.setModifiedDate(now);
			bookBuyBusinessRepo.save(bookBuyBusiness);
			List<BookBuy> bookBuyList = new ArrayList<BookBuy>();
			for(int i=0,l=bookList.length;i<l;i++){
				BookBuy bookBuy = new BookBuy();
				BookBuyDto bookBuyDto = bookList[i];
				BeanCopierUtils.copyProperties(bookBuyDto, bookBuy);
				bookBuy.setBookBuyBusiness(bookBuyBusiness);
				bookBuy.setId(UUID.randomUUID().toString());
				bookBuy.setCreatedBy(SessionUtil.getDisplayName());
				bookBuy.setModifiedBy(SessionUtil.getDisplayName());
				bookBuy.setCreatedDate(now);
				bookBuy.setModifiedDate(now);
				bookBuyList.add(bookBuy);
			}
			if (bookBuyList.size()>0)
			bookBuyRepoRepo.bathUpdate(bookBuyList);
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"保存成功！");
		}else{
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败");
		}
	}

}