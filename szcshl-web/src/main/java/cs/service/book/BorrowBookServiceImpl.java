package cs.service.book;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.domain.book.BorrowBookInfo;
import cs.model.PageModelDto;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.book.BorrowBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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

	/**
	 * 保存借书详细信息
	 * @param record
	 */
	@Override
	@Transactional
	public void save(BookBorrowInfoDto record) {
		BorrowBookInfo domain = new BorrowBookInfo();
		BeanCopierUtils.copyProperties(record, domain);
		domain.setId(UUID.randomUUID().toString());
		borrowBookRepo.save(domain);
	}

	/**
	 *获取借书列表
	 * @return
	 */
	@Override
	public ResultMsg getBookBorrowList(BookBorrowInfoDto bookBorrowInfoDto) {
		try{
			List<BookBorrowInfoDto> bookBorrowDetailList = borrowBookRepo.getBookBorrowList(bookBorrowInfoDto);
			List<BookBorrowInfoDto> bookBorrowSumDtoList = borrowBookRepo.getBookBorrowSum(bookBorrowInfoDto);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("bookBorrowDetailList",bookBorrowDetailList);
			resultMap.put("bookBorrowSumDtoList",bookBorrowSumDtoList);
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
		}catch (Exception e){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "查询失败");
		}
	}

	/**
	 * 获取个人借书总数
	 * @param bookBorrowInfoDto
	 * @return
	 */
	@Override
	public BookBorrowInfoDto getBookBorrowSum(BookBorrowInfoDto bookBorrowInfoDto) {
		return null;
	}

}