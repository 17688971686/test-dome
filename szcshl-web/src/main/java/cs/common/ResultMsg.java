package cs.common;

import cs.common.constants.Constant;

/**
 * 操作返回类
 * @author ldm
 * 
 */
public class ResultMsg implements java.io.Serializable {

	private boolean flag; // 成功标识
	private String reCode; // 返回代码
	private String idCode; // 结果ID
	private String reMsg; // 返回信息
	private Object reObj; // 返回实体

	public ResultMsg() {
		super();
	}

	public ResultMsg(boolean flag, String reCode, String idCode, String reMsg, Object reObj) {
		this.flag = flag;
		this.reCode = reCode;
		this.reMsg = reMsg;
		this.idCode = idCode;
		this.reObj = reObj;
	}

	public ResultMsg(boolean flag, String reCode, String reMsg) {
		this.flag = flag;
		this.reCode = reCode;
		this.reMsg = reMsg;
	}

	public ResultMsg(boolean flag, String reCode, String reMsg, Object reObj) {
		this.flag = flag;
		this.reCode = reCode;
		this.reMsg = reMsg;
		this.reObj = reObj;
	}

	public static ResultMsg ok(String msg){
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), null == msg?"成功":msg);
	}

	public static ResultMsg error(String msg){
		return new ResultMsg(false,  Constant.MsgCode.ERROR.getValue(), null == msg?"失败":msg);
	}
	public String getReCode() {
		return reCode;
	}

	public void setReCode(String reCode) {
		this.reCode = reCode;
	}

	public String getReMsg() {
		return reMsg;
	}

	public void setReMsg(String reMsg) {
		this.reMsg = reMsg;
	}

	public Object getReObj() {
		return reObj;
	}

	public void setReObj(Object reObj) {
            this.reObj = reObj;
	}
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

}
