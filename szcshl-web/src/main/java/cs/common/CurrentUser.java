package cs.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser implements ICurrentUser {
	private String loginName;
	private String displayName;

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
}
