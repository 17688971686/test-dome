package com.sn.framework.core.ftp;

public interface FtpCallback<K,T> {
	T doCall(K k) throws Exception;
}
