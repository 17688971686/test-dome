package cs.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import cs.domain.sys.User;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser implements ICurrentUser {
	private String loginName;
	private String displayName;	
	private User user;


	/*
	 * (non-Javadoc)
	 * 
	 * @see cs.common.ICurrentUser#getLoginName()
	 */
	@Override
	public String getLoginName() {
		if (loginName == null) {
			return "";
		}else{
		return loginName;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs.common.ICurrentUser#setLoginName(java.lang.String)
	 */
	@Override
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs.common.ICurrentUser#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs.common.ICurrentUser#setDisplayName(java.lang.String)
	 */
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void setLoginUser(User user) {
		// TODO Auto-generated method stub
		this.user = user;
	}

	@Override
	public User getLoginUser() {
		// TODO Auto-generated method stub
		return this.user;
	}

	
}
