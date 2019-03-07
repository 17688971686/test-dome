package cs.common.ftp;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * @author ldm
 */
public class FtpPoolConfig extends GenericKeyedObjectPoolConfig{
	public FtpPoolConfig() {
		/**
		 * 则设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。
		 * 不能通过有效性检查的对象也将被回收。
		 */
		setTestWhileIdle(true);
		/**
		 * 设定间隔每过多少毫秒进行一次后台对象清理的行动。
		 * 如果这个值不是正数，则实际上不会进行后台对象清理。
		 */
		setTimeBetweenEvictionRunsMillis(60000L);
		/**
		 * 设定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。过期的对象将被回收。
		 * 如果这个值不是正数，那么对休眠时间没有特别的约束
		 */
		setMinEvictableIdleTimeMillis(GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
		/**
		 * 设定在借出对象时是否进行有效性检查
		 */
		setTestOnBorrow(true);
		/**
		 * 设定在进行后台对象清理时，每次检查对象数
		 */
		setNumTestsPerEvictionRun(GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN);

        /**
         * 整个池最大值
         */
		setMaxTotal(1000);
		/**
		 * 对象池每个key最大实例化对象数
		 */
		setMaxTotalPerKey(30);
		/**
		 * 对象池每个key最大的闲置对象数
		 */
		setMaxIdlePerKey(GenericObjectPool.DEFAULT_MAX_IDLE);
		/**
		 * 对象池每个key最小的闲置对象数
		 */
		setMinIdlePerKey(2);

		/**
		 * 被空闲对象回收器回收前在池中保持空闲状态的最小时间毫秒数
		 */
		setSoftMinEvictableIdleTimeMillis(GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
		/**
		 * 允许最大等待时间毫秒数
         * 设置为负数表示获取不到永远等待
		 */
		setMaxWaitMillis(120000L);
	}
}
