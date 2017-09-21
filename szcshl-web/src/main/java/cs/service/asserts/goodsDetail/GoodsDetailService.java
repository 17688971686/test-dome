package cs.service.asserts.goodsDetail;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.asserts.goodsDetail.GoodsDetailDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 物品明细 业务操作接口
 * author: zsl
 * Date: 2017-9-15 14:15:55
 */
public interface GoodsDetailService {
    
    PageModelDto<GoodsDetailDto> get(ODataObj odataObj);

	void save(GoodsDetailDto record);

	void update(GoodsDetailDto record);

	GoodsDetailDto findById(String deptId);

	void delete(String id);

	ResultMsg getStoreAssertData();

}
