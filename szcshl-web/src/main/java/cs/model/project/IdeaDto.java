package cs.model.project;


import cs.model.BaseDto;

public class IdeaDto extends BaseDto {
	
	private String ideaID;  //审批意见id
	
	private String ideaContent;  //意见内容
	
	private String ideaType;  //意见类型

	public String getIdeaID() {
		return ideaID;
	}

	public void setIdeaID(String ideaID) {
		this.ideaID = ideaID;
	}

	public String getIdeaContent() {
		return ideaContent;
	}

	public void setIdeaContent(String ideaContent) {
		this.ideaContent = ideaContent;
	}

	public String getIdeaType() {
		return ideaType;
	}

	public void setIdeaType(String ideaType) {
		this.ideaType = ideaType;
	}

	
	
}
