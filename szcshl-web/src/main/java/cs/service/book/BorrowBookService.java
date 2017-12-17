package cs.service.book;

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

/*	void update(BorrowBookInfo record);*/

}
