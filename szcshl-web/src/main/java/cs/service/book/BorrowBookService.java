package cs.service.book;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 图书信息 业务操作接口
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
public interface BorrowBookService {
    
    PageModelDto<BookBorrowInfoDto> get(ODataObj odataObj);

	void save(BookBorrowInfoDto record);

    /**
     * 借书列表
     * @return
     */
	ResultMsg getBookBorrowList(BookBorrowInfoDto bookBorrowInfoDto);

    /**
     * 获取个人借书总数
     * @return
     */
    BookBorrowInfoDto  getBookBorrowSum(BookBorrowInfoDto bookBorrowInfoDto);

/*	void update(BorrowBookInfo record);*/

}
