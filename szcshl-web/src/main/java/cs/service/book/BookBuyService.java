package cs.service.book;

import cs.model.PageModelDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 图书模型业务操作接口
 * author: zsl
 * Date: 2017-9-5 16:16:22
 */
public interface BookBuyService {
    
    PageModelDto<BookBuyDto> get(ODataObj odataObj);

	void save(BookBuyDto record);

	void update(BookBuyDto record);

	BookBuyDto findById(String deptId);

	void delete(String id);

}
