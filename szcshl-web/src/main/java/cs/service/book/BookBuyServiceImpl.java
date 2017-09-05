package cs.service.book;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.book.BookBuy;
import cs.model.PageModelDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.book.BookBuyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:  图书模型业务操作实现类
 * author: zsl
 * Date: 2017-9-5 16:16:22
 */
@Service
public class BookBuyServiceImpl  implements BookBuyService {

	@Autowired
	private BookBuyRepo bookBuyRepo;
	
	@Override
	public PageModelDto<BookBuyDto> get(ODataObj odataObj) {
		PageModelDto<BookBuyDto> pageModelDto = new PageModelDto<BookBuyDto>();
		List<BookBuy> resultList = bookBuyRepo.findByOdata(odataObj);
		List<BookBuyDto> resultDtoList = new ArrayList<BookBuyDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				BookBuyDto modelDto = new BookBuyDto();
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
	public void save(BookBuyDto record) {
		BookBuy domain = new BookBuy(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		bookBuyRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(BookBuyDto record) {
		BookBuy domain = bookBuyRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		bookBuyRepo.save(domain);
	}

	@Override
	public BookBuyDto findById(String id) {		
		BookBuyDto modelDto = new BookBuyDto();
		if(Validate.isString(id)){
			BookBuy domain = bookBuyRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}