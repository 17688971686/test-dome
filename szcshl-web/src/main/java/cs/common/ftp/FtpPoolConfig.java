package cs.common.ftp;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

public class FtpPoolConfig extends GenericKeyedObjectPoolConfig{
	public FtpPoolConfig() {
		setTestWhileIdle(true);
		setTimeBetweenEvictionRunsMillis(60000);
		setMinEvictableIdleTimeMillis(1800000L);
		setTestOnBorrow(true);
		setMaxTotal(1000);
		setMaxTotalPerKey(10);
		setMaxIdlePerKey(3);
		setMinIdlePerKey(2);
		setMaxWaitMillis(600000);
		setTestWhileIdle(true);
	}
}
