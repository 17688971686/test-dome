package cs.service.project;

import cs.model.project.FileRecordDto;

public interface FileRecordService{

	void save(FileRecordDto fileRecordDto) throws Exception;

	FileRecordDto initBySignId(String signid);

}
