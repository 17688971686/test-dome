package cs.service.book;

import cs.common.utils.BeanCopierUtils;
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
import java.util.List;

/**
 * Description: 图书信息 业务操作实现类
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
@Service
public class BookBuyServiceImpl  implements BookBuyService {

	@Autowired
	private BookBuyRepo bookBuyRepo;
	
	@Override
	public PageModelDto<BookBuyDto> get(ODataObj odataObj) {
		PageModelDto<BookBuyDto> pageModelDto = new PageModelDto<BookBuyDto>();
		List<BookBuyDto> resultDtoList = new ArrayList<>();
		List<BookBuy> resultList = bookBuyRepo.findByOdata(odataObj);
		if(Validate.isList(resultList)){
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

    /**保存图书信息
     *
     * @param record
     */
	@Override
	@Transactional
	public void save(BookBuyDto record) {
		BookBuy domain = new BookBuy(); 
		BeanCopierUtils.copyProperties(record, domain);
		bookBuyRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(BookBuyDto record) {
		BookBuy domain = bookBuyRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
	/*	domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());*/
		
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

	/**
	 * 结束后更新库存信息
	 * @param id,num
	 * @param num
	 */
	@Override
	@Transactional
	public void updateBookInfo(String id,Integer num) {
		BookBuy domain = bookBuyRepo.findById(id);
		domain.setStoreConfirm(domain.getStoreConfirm() - num);
		bookBuyRepo.save(domain);
	}

}