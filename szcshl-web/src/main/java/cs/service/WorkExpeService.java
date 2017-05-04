﻿package cs.service;

import java.util.List;

import cs.domain.WorkExpe;
import cs.model.WorkExpeDto;
import cs.repository.odata.ODataObj;

public interface WorkExpeService {
	 List<WorkExpeDto> getWork(ODataObj odataObj);
	 void createWork(WorkExpeDto workExpeDto);
	 void deleteWork(String id);
	 void deleteWork(String[] ids);
	 void updateWork(WorkExpeDto workExpeDto);
	 void createWork(WorkExpe workExpe);
}