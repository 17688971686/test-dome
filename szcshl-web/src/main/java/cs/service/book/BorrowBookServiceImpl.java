package cs.service.book;

import cs.common.utils.BeanCopierUtils;
import cs.domain.book.BorrowBookInfo;
import cs.model.PageModelDto;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.book.BorrowBookRepo;
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
public class BorrowBookServiceImpl implements BorrowBookService {

	@Autowired
	private BorrowBookRepo borrowBookRepo;


	@Override
	public PageModelDto<BookBorrowInfoDto> get(ODataObj odataObj) {
		PageModelDto<BookBorrowInfoDto> pageModelDto = new PageModelDto<BookBorrowInfoDto>();
		List<BorrowBookInfo> resultList = borrowBookRepo.findByOdata(odataObj);
		List<BookBorrowInfoDto> resultDtoList = new ArrayList<BookBorrowInfoDto>(resultList.size());

		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				BookBorrowInfoDto modelDto = new BookBorrowInfoDto();
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
	public void save(BookBorrowInfoDto record) {
		BorrowBookInfo domain = new BorrowBookInfo();
		BeanCopierUtils.copyProperties(record, domain);
		borrowBookRepo.save(domain);
	}

}