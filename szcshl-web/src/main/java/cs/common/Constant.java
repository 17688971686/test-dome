package cs.common;

/**
 * 系统常亮
 * @author ldm
 *
 */
public class Constant {

	/**
	 * 定义一个业务状态
	 * "0" 表示否
	 * "1" 表示是
	 * @author ldm
	 *
	 */
	public static enum EnumState{
	    NO("0"),YES("9");

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
	 * @author Administrator
	 *
	 */
	public static enum EnumFlowNodeGroupName{
		COMM_DEPT_DIRECTOR("综合部部长"),
		CENTRAL_LEADER("中心领导"),
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
}
