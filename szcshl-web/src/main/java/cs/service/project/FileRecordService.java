package cs.service.project;

import cs.common.ResultMsg;
import cs.model.project.FileRecordDto;

import java.util.Date;

public interface FileRecordService{

	ResultMsg save(FileRecordDto fileRecordDto);

	/**
	 * 根据项目信息，初始化归档信息
	 * @param signid
	 * @return
	 */
	FileRecordDto initBySignId(String signid);
}
