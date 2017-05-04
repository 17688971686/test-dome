package cs.common;

/**
 * 系统常亮
 * @author ldm
 *
 */
public class Constant {
	public static final String ERROR_MSG = "操作异常，错误信息已记录，请联系相关人员处理！";
	/**
	 * 定义一个业务状态
	 * "0" 表示否
	 * "1" 表示进行中
	 * "2" 表示暂停
	 * "5" 表示正常
	 * "7" 表示已删除
	 * "8" 表示强制结束
	 * "9" 表示是,或者已完成
	 * @author ldm
	 *
	 */
	public static enum EnumState{
	    NO("0"),PROCESS("1"),STOP("2"),NORMAL("5"),
	    DELETE("7"),FORCE("8"),YES("9");

	    private String value;

	    EnumState(String value){
	        this.value = value;
	    }

	    public String getValue(){
	        return value;
	    }

	}
	
	/**
	 * 业务流程名称
	 * @author ldm
	 *
	 */
	public static enum EnumFlow{
	    SIGN("signflow");

	    private String value;

	    EnumFlow(String value){
	        this.value = value;
	    }

	    public String getValue(){
	        return value;
	    }

	}
	
	/**
	 * 流程环节 处理组名称
	 * @author ldm
	 *
	 */
	public static enum EnumFlowNodeGroupName{
		COMM_DEPT_DIRECTOR("综合部部长"),
		CENTRAL_LEADER("中心领导"),
		DEPT_PRINCIPAL("部门负责人"),
		DIRECTOR("主任"),
		FILER("归档员"),
		DEPT_LEADER("部门领导");
		
		private String value;

		EnumFlowNodeGroupName(String value){
	        this.value = value;
	    }

	    public String getValue(){
	        return value;
	    }
	}
	
	/**
	 * 消息返回码
	 * @author ldm
	 *
	 */
	public static enum MsgCode{
		OK("ok"),ERROR("error");
		
		private String value;

		MsgCode(String value){
	        this.value = value;
	    }

	    public String getValue(){
	        return value;
	    }
	}
			
}
