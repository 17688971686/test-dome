package cs.common.utils;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Excel操作工具类
 * @author liangdengming
 *
 */
public class ExcelTools {
	
	public ExcelTools() {
		super();
	}

	/*  
     * 列头单元格样式 
     */      
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {           
          // 设置字体  
          HSSFFont font = workbook.createFont();  
          //设置字体大小  
          font.setFontHeightInPoints((short)11);  
          //字体加粗  
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          HSSFCellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
            
          return style;  
            
    }  
      
    /*   
     * 列数据信息单元格样式 
     */    
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {  
          // 设置字体  
          HSSFFont font = workbook.createFont();  
          //设置字体大小  
          //font.setFontHeightInPoints((short)10);  
          //字体加粗  
          //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          HSSFCellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(true);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);   
          return style;  
      
    }  
	
	/**
	 * 创建字体
	 * @param wb
	 * @param fontName	字体类型名称
	 * @param pix	高度
	 * @param isBold	是否粗体
	 * @param isItalic 是否斜体
	 * @return
	 */
	public HSSFFont createFont(HSSFWorkbook wb, String fontName, int pix, boolean isBold, boolean isItalic){
		HSSFFont font = wb.createFont();
		if(fontName != null && !"".equals(fontName))
			font.setFontName(fontName);
		font.setFontHeightInPoints((short)pix);
		if(isBold)
			font.setBold(isBold);
		if(isItalic)
			font.setItalic(isItalic);
		return font;
	}
	
	//封装调用get属性方法
	private String getMethodString(String field){
		int x = field.charAt(0)-32;
		return "get"+(char)x+field.substring(1);
	}
	
	/**
	 * 创建一个Excel(没有合并)
	 * @param bookTitle 标题名称
	 * @param headerPair 表头标签（格式为：中文名=字段或属性名，如 单位名称=ORGANNAME）
	 * @param items	数据项列表
	 * @param itemType 数据项类型
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public HSSFWorkbook createExcelBook(String title, String[] headerPair,List<?> items, 
			Class<?> itemType) throws Exception{
		//1、创建HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook();		
		HSSFSheet sheet = wb.createSheet(title == null?"数据表格":title);
		
		 // 产生表格标题行  
        HSSFRow rowm = sheet.createRow(0);  
        HSSFCell cellTiltle = rowm.createCell(0);  
          
        //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(wb);//获取列头样式对象                
        //合并行
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (headerPair.length-1)));    
        cellTiltle.setCellStyle(columnTopStyle);  
        cellTiltle.setCellValue(title);  
        
		//数据不完整，则返回null
		if(headerPair == null || headerPair.length == 0 || items == null || items.size() == 0){
			return wb;
		}	
		//2、取表头标签名称和对应的字段值
		String[][] headDataInfo = parseHeadArr(headerPair);
		String[] headName = headDataInfo[0], headDataName = headDataInfo[1]; 	

		//3、创建表头
		sheet = createExcelHead(wb,sheet,headName,columnTopStyle);
		//4、判断数据对象类型，并填充数据
		HSSFCellStyle style = this.getStyle(wb);                  //单元格样式对象     
		fillExcelData(wb,sheet,headDataName,items,itemType,style);	
		
		return wb;
	}
	
	/**
	 * 解析表头
	 * @param headerPair
	 * @return
	 */
	private String[][] parseHeadArr(String[] headerPair) {
		int headLabelLen = headerPair.length;
		String[] headName = new String[headLabelLen],headDataName = new String[headLabelLen];
		for(int i=0; i<headLabelLen; i++){
			String[] splitArr = headerPair[i].split("=");
			if(splitArr.length != 2){
				headName[i] = headerPair[i];
				headDataName[i] = headerPair[i];
			}else{
				headName[i] = splitArr[0];
				headDataName[i] = splitArr[1];
			}	
		}		
		return new String[][]{headName,headDataName};
	}
	
	/**
	 * 创建表头
	 * @param wb
	 * @param sheet
	 * @param headName	表头数组
	 * @param titleStyle 表头样式
	 * @return
	 */
	private HSSFSheet createExcelHead(HSSFWorkbook wb, HSSFSheet sheet,String[] headName,HSSFCellStyle columnTopStyle) {
		HSSFRow row = sheet.createRow(2);				
		for(int i=0,l=headName.length;i<l; i++){
			HSSFCell cell = row.createCell(i);								//创建列头对应个数的单元格             
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);             		//设置列头单元格的数据类型  
            HSSFRichTextString text = new HSSFRichTextString(headName[i]);  
            cell.setCellValue(text);                                 		//设置列头单元格的值  
            cell.setCellStyle(columnTopStyle);                       		//设置列头单元格样式  				
		}
		return sheet;
	}
	
	/**
	 * 填充数据
	 * @param wb
	 * @param sheet
	 * @param dataLabel 取字段的数组
	 * @param items 数据列表
	 * @param itemType 数据类型
	 * @param startrow	 开始填充的数据行
	 * @param dataStyle 数据样式
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void fillExcelData(HSSFWorkbook wb,HSSFSheet sheet, String[] dataLabel,List<?> items, Class<?> itemType,HSSFCellStyle dataStyle) throws Exception{
		String className = itemType.getSimpleName();
		boolean isMap = "HashMap".equals(className);
		
		Object real = null,val = null;
		Map<String,Object> dataMap = null;
		
		if(dataStyle == null){
			dataStyle = getStyle(wb);
		}
		for(int l=0,itemLen = items.size();l<itemLen;l++){
			HSSFRow row = sheet.createRow(l + 3);
			if(isMap){
				 dataMap = (Map<String, Object>) items.get(l);	
			}else{
				real = items.get(l);				
			}					
			//遍历列
			for(int i =0;i<dataLabel.length;i++){
				if(isMap){
					val = dataMap.get(dataLabel[i].toUpperCase());
				}else{
					val = itemType.getMethod(getMethodString(dataLabel[i]), null).invoke(real, null);
				}
				HSSFCell cell = row.createCell(i);
				if(val != null){
					String ct = val.getClass().getSimpleName();
					if(ct != null && ("Double".equals(ct) || "Integer".equals(ct) || "Float".equals(ct) || "Long".equals(ct) || "Short".equals(ct) || "Byte".equals(ct))){
						cell.setCellValue(Double.parseDouble(String.valueOf(val)));
					}else{
						cell.setCellValue(String.valueOf(val));
					}					
				}	else{
					cell.setCellValue("");
				}	
				cell.setCellStyle(dataStyle);
			}					
		}
		//让列宽随着导出的列长自动适应  
        for (int colNum = 0; colNum < items.size(); colNum++) {  
            int columnWidth = sheet.getColumnWidth(colNum) / 256;  
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {  
                HSSFRow currentRow;  
                //当前行未被使用过  
                if (sheet.getRow(rowNum) == null) {  
                    currentRow = sheet.createRow(rowNum);  
                } else {  
                    currentRow = sheet.getRow(rowNum);  
                }  
                if (currentRow.getCell(colNum) != null) {  
                    HSSFCell currentCell = currentRow.getCell(colNum);  
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                        int length = currentCell.getStringCellValue().getBytes().length;  
                        if (columnWidth < length) {  
                            columnWidth = length;  
                        }  
                    }  
                }  
            }  
            /*if(colNum == 0){  
                sheet.setColumnWidth(colNum, (columnWidth-2)*256>255*256?255*256:(columnWidth-2)*256);  
            }else{  
                sheet.setColumnWidth(colNum, (columnWidth+4)*256>255*256?255*256:(columnWidth+4)*256);  
            }  */
            sheet.setColumnWidth(colNum, columnWidth>160?160*256:columnWidth*256); 
        }  
	}
		
}
