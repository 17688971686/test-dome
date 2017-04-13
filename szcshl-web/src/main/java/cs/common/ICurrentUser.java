package cs.common;

public interface ICurrentUser {

	String getLoginName();

	void setLoginName(String loginName);

	String getDisplayName();

	void setDisplayName(String displayName);

}