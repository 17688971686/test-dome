package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.Header;
import cs.model.PageModelDto;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by MCL
 * 2017/9/12
 */
public interface HeaderService {

    ResultMsg createHeader(HeaderDto headerDto);

    List<HeaderDto> findHeaderList(String headerType,String headState);

    void updateSelectedHeader(String idStr);

    void updateCancelHeader(String idStr);
    /*List<HeaderDto> findHeaderListSelected(String headerType);*/

    PageModelDto<HeaderDto> get(ODataObj oDataObj);

    void deleteHeader(String id);
    HeaderDto getHeaderById(String id);
    ResultMsg updateHeader(HeaderDto headerDto);

    List<Header> findHeaderByType(String type);
    //设置表头顺序
    void setSelectHeadOrder(HeaderDto[] headerDtos);
}
