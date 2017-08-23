package cs.service.project;

import cs.common.ResultMsg;
import cs.model.project.FileRecordDto;

import java.util.Date;

public interface FileRecordService{

	ResultMsg save(FileRecordDto fileRecordDto);

	FileRecordDto initBySignId(String signid);

}
