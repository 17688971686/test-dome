package com.sn.framework.core.common;


import com.sn.framework.core.Constants;
import lombok.Data;

/**
 * 操作返回类
 * 
 * @author ldm
 * 
 */
@Data
public class ResultMsg implements java.io.Serializable {

	private boolean flag; // 成功标识
	private String reCode; // 返回代码
	private String idCode; // 结果ID
	private String reMsg; // 返回信息
	private Object reObj; // 返回实体

	public ResultMsg() {

	}

	public ResultMsg(boolean flag, String reCode, String idCode, String reMsg, Object reObj) {
		this.flag = flag;
		this.reCode = reCode;
		this.idCode = idCode;
		this.reMsg = reMsg;
		this.reObj = reObj;
	}

	public ResultMsg(boolean flag, String reCode, String reMsg) {
		this.flag = flag;
		this.reCode = reCode;
		this.reMsg = reMsg;
	}

	public ResultMsg(boolean flag, String[] message) {
		this.flag = flag;
		this.reCode = message[0];
		this.reMsg = message[1];
	}

	public ResultMsg(boolean flag, String reCode, String reMsg, Object reObj) {
		this.flag = flag;
		this.reCode = reCode;
		this.reMsg = reMsg;
		this.reObj = reObj;
	}

	public static ResultMsg ok(String msg){
		return new ResultMsg(true, Constants.ReCode.OK.name(), null == msg?"成功":msg);
	}

	public static ResultMsg error(String msg){
		return new ResultMsg(false, Constants.ReCode.ERROR.name(), null == msg?"失败":msg);
	}

	public static ResultMsg error(int httpCode,String msg){
		return new ResultMsg(false, String.valueOf(httpCode), null == msg?"失败":msg);
	}
}
