package cs.model.expert;


public class ExpertTypeDto {

	private String id;
	private  Integer seqNum;
	private String expertType;//专家类型
	private String maJorBig;//突出专业（大类）
	private String maJorSmall;//突出专业（小类）
	private ExpertDto expert;
	
	private String expertID;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExpertType() {
		return expertType;
	}
	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}
	public String getMaJorBig() {
		return maJorBig;
	}
	public void setMaJorBig(String maJorBig) {
		this.maJorBig = maJorBig;
	}
	public String getMaJorSmall() {
		return maJorSmall;
	}
	public void setMaJorSmall(String maJorSmall) {
		this.maJorSmall = maJorSmall;
	}
	public ExpertDto getExpert() {
		return expert;
	}
	public void setExpert(ExpertDto expert) {
		this.expert = expert;
	}
	public String getExpertID() {
		return expertID;
	}
	public void setExpertID(String expertID) {
		this.expertID = expertID;
	}

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
