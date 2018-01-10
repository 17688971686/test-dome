package cs.service.book;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.domain.book.*;
import cs.model.PageModelDto;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.book.BookBuyRepo;
import cs.repository.repositoryImpl.book.BorrowBookHisRepo;
import cs.repository.repositoryImpl.book.BorrowBookRepo;
import cs.repository.repositoryImpl.book.ReturnBookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

import static cs.common.Constant.FTP_IP1;

/**
 * Description: 图书信息 业务操作实现类
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
@Service
public class BorrowBookServiceImpl implements BorrowBookService {

	@Autowired
	private BorrowBookRepo borrowBookRepo;

	@Autowired
	private BorrowBookHisRepo borrowBookHisRepo;

	@Autowired
	private BookBuyRepo bookBuyRepo;

	@Autowired
	private ReturnBookRepo returnBookRepo;

	@Autowired
	private BookBuyService bookBuyService;


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
		BorrowBookInfo borrowBookInfo = new BorrowBookInfo();
		BeanCopierUtils.copyProperties(record, borrowBookInfo);
		borrowBookInfo.setId(UUID.randomUUID().toString());
		borrowBookInfo.setBookBorrower(SessionUtil.getDisplayName());
		borrowBookRepo.save(borrowBookInfo);
		//保存借书历史
		BorrowBookHis borrowBookHis = new BorrowBookHis();
		BeanCopierUtils.copyProperties(borrowBookInfo, borrowBookHis);
		borrowBookHis.setId(UUID.randomUUID().toString());
		borrowBookHisRepo.save(borrowBookHis);
	}

	/**
	 * 保存借书详细信息
	 * @param bookBorrowInfoDto
	 * @return
	 */
	@Transactional
	public ResultMsg saveBooksDetail (BookBorrowInfoDto bookBorrowInfoDto){
		try{
			save(bookBorrowInfoDto);
			bookBuyService.updateBookInfo(bookBorrowInfoDto.getId(),bookBorrowInfoDto.getBorrowNum());
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功", "");
		}catch (Exception e){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败");
		}
	}

	/**
	 * 保存还书详细信息
	 * @param bookBorrowInfoDto
	 * @return
	 */
	@Transactional
	public ResultMsg saveReturnDetail (BookBorrowInfoDto bookBorrowInfoDto){
		try{
		    BorrowBookInfo borrowBookInfo =  borrowBookRepo.findById(BorrowBookInfo_.id.getName(),bookBorrowInfoDto.getId());
			BookBuy bookBuy = bookBuyRepo.findById(BookBuy_.bookNo.getName(),bookBorrowInfoDto.getBookNo());
			if(null != bookBorrowInfoDto){
		/*		//保存借书历史
				BorrowBookHis borrowBookHis = new BorrowBookHis();
				BeanCopierUtils.copyProperties(borrowBookInfo, borrowBookHis);
				borrowBookHis.setId(UUID.randomUUID().toString());
				borrowBookHisRepo.save(borrowBookHis);*/
				//保存还书信息
				ReturnBookInfo returnBookInfo = new ReturnBookInfo();
				BeanCopierUtils.copyProperties(bookBorrowInfoDto,returnBookInfo);
				returnBookInfo.setReturnBorrower(borrowBookInfo.getBookBorrower());
				returnBookInfo.setId(UUID.randomUUID().toString());
				returnBookInfo.setCreatedBy(SessionUtil.getDisplayName());
				returnBookInfo.setCreatedDate(new Date());
				returnBookInfo.setModifiedBy(SessionUtil.getDisplayName());
				returnBookInfo.setModifiedDate(new Date());
				returnBookRepo.save(returnBookInfo);
				//更新借阅信息
				Integer temp = borrowBookInfo.getBorrowNum()-bookBorrowInfoDto.getReturnNum();
				if (temp == 0){
					borrowBookRepo.delete(borrowBookInfo);
				}else{
					borrowBookInfo.setBorrowNum(borrowBookInfo.getBorrowNum()-bookBorrowInfoDto.getReturnNum());
					borrowBookRepo.save(borrowBookInfo);
				}

				//更新库存信息
				bookBuy.setStoreConfirm(bookBuy.getStoreConfirm()+bookBorrowInfoDto.getReturnNum());
				bookBuyRepo.save(bookBuy);
			}
			return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功", "");
		}catch (Exception e){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败");
		}
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