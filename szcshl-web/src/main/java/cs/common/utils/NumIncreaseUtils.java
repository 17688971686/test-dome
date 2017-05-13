package cs.common.utils;

import cs.common.Constant;

/**
 * 数字递增工具类
 * @author ldm
 *
 */
public class NumIncreaseUtils {
	
	private static String FILENO_YEAR = "fileno_year";
	private static String FILENO_KEY = "fileno_increase_no";
	private static String PREFIX="深投审";
	private static String FILE_RECORD_YEAR = "file_record_year";
	private static String FILE_RECORD_KEY = "file_record_no";
	private static String EXPERT_NO = "expert_no";					
	
	/**
	 * 获取文档编号
	 * @return
	 */
	public static synchronized  String getFileNo(){
		String curNo = "1";
		PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
		String year = propertyUtil.readProperty(FILENO_YEAR);
		int curYear = DateUtils.getCurYear();
		if(curYear == Integer.valueOf(year)){
			curNo = propertyUtil.readProperty(FILENO_KEY);
			propertyUtil.writeProperty(FILENO_KEY, (Integer.valueOf(curNo)+1)+"");
			return PREFIX +"["+curYear+"]"+curNo;
		}else{
			propertyUtil.writeProperty(FILENO_KEY, (Integer.valueOf(curNo)+1)+"");
			propertyUtil.writeProperty(FILENO_YEAR, curYear+"");
			return PREFIX +"["+curYear+"]"+curNo;
		}
	}
	
	/**
	 * 存档编号
	 * @param typeName （GD表示概率，PD表示评估）
	 * @return
	 */
	@SuppressWarnings("unused")
	public static synchronized String getFileRecordNo(String typeName){
		String curNo = "1";
		PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
		String year = propertyUtil.readProperty(FILE_RECORD_YEAR);
		int curYear = DateUtils.getCurYear();
		if(curYear == Integer.valueOf(year)){
			curNo = propertyUtil.readProperty(FILE_RECORD_KEY);			
		}else{			
			propertyUtil.writeProperty(FILE_RECORD_YEAR, curYear+"");
		}
		propertyUtil.writeProperty(FILE_RECORD_KEY, (Integer.valueOf(curNo)+1)+"");
		
		return curYear+typeName+curNo;
	}
	
	/**
	 * 获取专家编号（5位数）
	 * @return
	 */
	public static synchronized String getExpertNo(){
		PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
		String expertNo = propertyUtil.readProperty(EXPERT_NO);		
		propertyUtil.writeProperty(EXPERT_NO, (Integer.valueOf(expertNo)+1)+"");		
		return String.format("%05d", Integer.valueOf(expertNo));
	}
	
	
	public static void main(String[] args){
		System.out.println(getExpertNo());
	}
	
	
}
