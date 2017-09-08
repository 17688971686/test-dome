package cs.service.book;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.book.BookBuyBusinessDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 图书采购申请业务信息 业务操作接口
 * author: zsl
 * Date: 2017-9-8 10:26:20
 */
public interface BookBuyBusinessService {
    
    PageModelDto<BookBuyBusinessDto> get(ODataObj odataObj);

	void save(BookBuyBusinessDto record);

	void update(BookBuyBusinessDto record);

	BookBuyBusinessDto findById(String deptId);

	void delete(String id);

	ResultMsg saveBooksDetailList( BookBuyDto[] bookList);

}
