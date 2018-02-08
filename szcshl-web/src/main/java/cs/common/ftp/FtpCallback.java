package cs.common.ftp;

public interface FtpCallback<K,T> {
	T doCall(K k) throws Exception;
}
