package cs.service.book;

import cs.model.PageModelDto;
import cs.model.book.BookBuyDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 图书信息 业务操作接口
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
public interface BookBuyService {
    
    PageModelDto<BookBuyDto> get(ODataObj odataObj);

	void save(BookBuyDto record);

	void update(BookBuyDto record);

	BookBuyDto findById(String deptId);

	void delete(String id);

	/**
	 * 更新图书信息
	 * @param id,num
	 */
	void updateBookInfo(String id,Integer num);
}
