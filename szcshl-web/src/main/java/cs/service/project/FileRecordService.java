package cs.service.project;

import java.util.List;

import cs.domain.sys.SysFile;
import cs.model.project.FileRecordDto;

public interface FileRecordService{

	void save(FileRecordDto fileRecordDto) throws Exception;

	FileRecordDto initBySignId(String signid);

	List<SysFile> sysFileByIds(String signid);

}
