package cs.model.sharing;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cs.domain.sharing.SharingPlatlform;
import cs.domain.sys.Org;
import cs.model.BaseDto;
import cs.model.BaseDto2;
    
    

/**
 * Description: 共享平台 页面数据模型
 * author: sjy
 * Date: 2017-7-11 10:31:02
 */
public class SharingPlatlformDto extends BaseDto {

	private String sharId;

	/**
	 * 主题
	 */
	private String theme;

	/**
	 * 是否全员可看（9：是，0：否）
	 */
	private String isNoPermission;

	/**
	 * 是否正式发布（ 0:否 ,9:是）
	 */
	private String isPublish;

	/**
	 * 发布人
	 */
	private String publishUsername;

	/**
	 * 发布时间
	 */
	private Date publishDate;

	/**
	 * 内容
	 */
	private String content;


    /**
     * 权限配置
     */
	private List<SharingPrivilegeDto> privilegeDtoList;


	public String getSharId() {
		return sharId;
	}

	public void setSharId(String sharId) {
		this.sharId = sharId;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getIsNoPermission() {
		return isNoPermission;
	}

	public void setIsNoPermission(String isNoPermission) {
		this.isNoPermission = isNoPermission;
	}

	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public String getPublishUsername() {
		return publishUsername;
	}

	public void setPublishUsername(String publishUsername) {
		this.publishUsername = publishUsername;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public List<SharingPrivilegeDto> getPrivilegeDtoList() {
        return privilegeDtoList;
    }

    public void setPrivilegeDtoList(List<SharingPrivilegeDto> privilegeDtoList) {
        this.privilegeDtoList = privilegeDtoList;
    }
}