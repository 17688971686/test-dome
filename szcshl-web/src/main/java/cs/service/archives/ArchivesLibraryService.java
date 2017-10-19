package cs.service.archives;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.archives.ArchivesLibraryDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 档案借阅管理 业务操作接口
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
public interface ArchivesLibraryService {
    
    PageModelDto<ArchivesLibraryDto> get(ODataObj odataObj);

	ResultMsg save(ArchivesLibraryDto record);

	void update(ArchivesLibraryDto record);

	ArchivesLibraryDto findById(String deptId);

	void delete(String id);

	PageModelDto<ArchivesLibraryDto> findByProjectList(ODataObj odataObj);

	void updateArchivesLibrary(ArchivesLibraryDto record);

	PageModelDto<ArchivesLibraryDto> findByCenterData(ODataObj odataObj);

	PageModelDto<ArchivesLibraryDto> findByCityList(ODataObj odataObj);

	ResultMsg saveCity(ArchivesLibraryDto record);

}
