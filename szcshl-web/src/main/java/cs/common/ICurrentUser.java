package cs.common;

import cs.domain.sys.User;

public interface ICurrentUser {

	String getLoginName();

	void setLoginName(String loginName);

	String getDisplayName();

	void setDisplayName(String displayName);
	
	void setLoginUser(User user);
	
	User getLoginUser();

}