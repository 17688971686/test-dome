package cs.common.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cs.model.expert.ExpertCostCountDto;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Excel操作工具类
 *
 * @author liangdengming
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
        font.setFontHeightInPoints((short) 12);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

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
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;

    }

    /**
     * 创建字体
     *
     * @param wb
     * @param fontName 字体类型名称
     * @param pix      高度
     * @param isBold   是否粗体
     * @param isItalic 是否斜体
     * @return
     */
    public HSSFFont createFont(HSSFWorkbook wb, String fontName, int pix, boolean isBold, boolean isItalic) {
        HSSFFont font = wb.createFont();
        if (fontName != null && !"".equals(fontName)) {
            font.setFontName(fontName);
        }
        font.setFontHeightInPoints((short) pix);
        if (isBold) {
            font.setBold(isBold);
        }
        if (isItalic) {
            font.setItalic(isItalic);
        }
        return font;
    }

    //封装调用get属性方法
    private String getMethodString(String field) {
        int x = field.charAt(0) - 32;
        return "get" + (char) x + field.substring(1);
    }


    /**
     * 创建一个Excel(没有合并)
     *
     * @param title      标题名称
     * @param headerPair 表头标签（格式为：中文名=字段或属性名，如 单位名称=ORGANNAME）
     * @param items      数据项列表
     * @param itemType   数据项类型
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public HSSFWorkbook createExcelBook(String title, String[] headerPair, List<?> items,
                                        Class<?> itemType) throws Exception {
        //1、创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title == null ? "数据表格" : title);

        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);

        //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(wb);//获取列头样式对象
        //合并行
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (headerPair.length - 1)));
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);

        //数据不完整，则返回null
        if (Validate.isEmpty(headerPair) || !Validate.isList(items)) {
            return wb;
        }
        //2、取表头标签名称和对应的字段值
        String[][] headDataInfo = parseHeadArr(headerPair);
        String[] headName = headDataInfo[0], headDataName = headDataInfo[1];

        //3、创建表头
        sheet = createExcelHead(wb, sheet, headName, columnTopStyle);
        //4、判断数据对象类型，并填充数据
        HSSFCellStyle style = this.getStyle(wb);                  //单元格样式对象
        fillExcelData(wb, sheet, headDataName, items, itemType, style);

        return wb;
    }

    /**
     * 创建专家缴税导出表头
     *
     * @param title
     * @return
     */
    public HSSFWorkbook createExpertTaxes(String title, List<ExpertCostCountDto> expertCostCountDtoList) throws Exception {
        //1、创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("专家缴税统计");
        //1.2头标题样式
        HSSFCellStyle headStyle = createCellStyle(wb, (short) 16);
        //1.1创建合并单元格对象
        CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, 8);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(callRangeAddress);
        // 总标题行
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cellTiltle = row1.createCell(0);
        cellTiltle.setCellStyle(headStyle);
        cellTiltle.setCellValue(title);

        //字段标题
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(wb);//获取列头样式对象

        HSSFRow row2 = sheet.createRow(1);
        HSSFCell cell2_0 = row2.createCell(0);
        //cell2_0.setCellStyle(columnTopStyle);
        cell2_0.setCellValue("序号");
        CellRangeAddress xhRangeAddress = new CellRangeAddress(1, 2, 0, 0);//起始行,结束行,起始列,结束列

        for (int i = xhRangeAddress.getFirstRow(); i <= xhRangeAddress.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = xhRangeAddress.getFirstColumn(); j <= xhRangeAddress.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(columnTopStyle);
            }
        }
        sheet.addMergedRegion(xhRangeAddress);

        HSSFCell cell2_1 = row2.createCell(1);
        cell2_1.setCellStyle(columnTopStyle);
        cell2_1.setCellValue("姓名");
        CellRangeAddress xmRangeAddress = new CellRangeAddress(1, 2, 1, 1);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(xmRangeAddress);

        HSSFCell cell2_2 = row2.createCell(2);
        cell2_2.setCellStyle(columnTopStyle);
        cell2_2.setCellValue("身份证号");
        CellRangeAddress sfzRangeAddress = new CellRangeAddress(1, 2, 2, 2);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(sfzRangeAddress);

        HSSFCell cell2_3 = row2.createCell(3);
        cell2_3.setCellStyle(columnTopStyle);
        cell2_3.setCellValue("手机号码");
        CellRangeAddress sjRangeAddress = new CellRangeAddress(1, 2, 3, 3);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(sjRangeAddress);

        HSSFCell cell2_4 = row2.createCell(4);
        cell2_4.setCellStyle(columnTopStyle);
        cell2_4.setCellValue("本月合计");
        CellRangeAddress monthRangeAddress = new CellRangeAddress(1, 1, 4, 5);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(monthRangeAddress);

        HSSFCell cell2_5 = row2.createCell(6);
        cell2_5.setCellStyle(columnTopStyle);
        cell2_5.setCellValue("本年合计");
        CellRangeAddress yearRangeAddress = new CellRangeAddress(1, 1, 6, 7);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(yearRangeAddress);

        HSSFRow row3 = sheet.createRow(2);
        HSSFCell cell3_0 = row3.createCell(4);
        cell3_0.setCellStyle(columnTopStyle);
        cell3_0.setCellValue("应缴所得税额");

        HSSFCell cell3_1 = row3.createCell(5);
        cell3_1.setCellStyle(columnTopStyle);
        cell3_1.setCellValue("应缴税额");

        HSSFCell cell3_2 = row3.createCell(6);
        cell3_2.setCellStyle(columnTopStyle);
        cell3_2.setCellValue("应缴所得税额");

        HSSFCell cell3_3 = row3.createCell(7);
        cell3_3.setCellStyle(columnTopStyle);
        cell3_3.setCellValue("应缴税额");

        //4、判断数据对象类型，并填充数据
        HSSFCellStyle style = this.getStyle(wb);
        //填充数据
        int totalLength = expertCostCountDtoList.size();
        BigDecimal monthCost = BigDecimal.ZERO,monthTaxes = BigDecimal.ZERO,yearCost = BigDecimal.ZERO,yearTaxes =  BigDecimal.ZERO;
        for (int i = 0; i < totalLength; i++) {
            ExpertCostCountDto expertCostCountDto = expertCostCountDtoList.get(i);
            HSSFRow row = sheet.createRow(3+i);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellStyle(style);
            cell0.setCellValue(i+1);

            HSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(style);
            cell1.setCellValue(expertCostCountDto.getName());

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style);
            cell2.setCellValue(expertCostCountDto.getIdCard());

            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(style);
            cell3.setCellValue(expertCostCountDto.getUserPhone());

            monthCost = Arith.safeAdd(monthCost,expertCostCountDto.getReviewcost());
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(style);
            cell4.setCellValue(expertCostCountDto.getReviewcost().doubleValue());

            monthTaxes = Arith.safeAdd(monthTaxes,expertCostCountDto.getReviewtaxes());
            HSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(style);
            cell5.setCellValue(expertCostCountDto.getReviewtaxes().doubleValue());

            yearCost = Arith.safeAdd(yearCost,expertCostCountDto.getYreviewcost());
            HSSFCell cell6 = row.createCell(6);
            cell6.setCellStyle(style);
            cell6.setCellValue(expertCostCountDto.getYreviewcost().doubleValue());

            yearTaxes = Arith.safeAdd(yearTaxes,expertCostCountDto.getYreviewtaxes());
            HSSFCell cell7 = row.createCell(7);
            cell7.setCellStyle(style);
            cell7.setCellValue(expertCostCountDto.getYreviewtaxes().doubleValue());
        }
        int maxLength = 3+totalLength ;
        HSSFRow row = sheet.createRow(3+totalLength);
        CellRangeAddress totalRangeAddress = new CellRangeAddress(maxLength, maxLength, 0, 3);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(totalRangeAddress);
        Cell totalCell = row.createCell(0);
        totalCell.setCellStyle(style);
        totalCell.setCellValue("总计");

        Cell totalCell_1 = row.createCell(4);
        totalCell_1.setCellStyle(style);
        totalCell_1.setCellValue(monthCost.doubleValue());

        Cell totalCell_2 = row.createCell(5);
        totalCell_2.setCellStyle(style);
        totalCell_2.setCellValue(monthTaxes.doubleValue());

        Cell totalCell_3 = row.createCell(6);
        totalCell_3.setCellStyle(style);
        totalCell_3.setCellValue(yearCost.doubleValue());

        Cell totalCell_4 = row.createCell(7);
        totalCell_4.setCellStyle(style);
        totalCell_4.setCellValue(yearTaxes.doubleValue());

        //让列宽随着导出的列长自动适应
        sheet.setColumnWidth(0, 5* 256);
        sheet.setColumnWidth(1, 13* 256);
        sheet.setColumnWidth(2, 32* 256);
        sheet.setColumnWidth(3, 27* 256);
        sheet.setColumnWidth(4, 25* 256);
        sheet.setColumnWidth(5, 25* 256);
        sheet.setColumnWidth(6, 25* 256);
        sheet.setColumnWidth(7, 25* 256);
        return wb;
    }

    /**
     * @param wb
     * @param sheet
     * @param items
     * @param itemType
     * @param style
     * @param start
     */
    private void fillExcelData(HSSFWorkbook wb, HSSFSheet sheet, String[] headName, List<?> items, Class<?> itemType, HSSFCellStyle style, int start) throws Exception {
        String className = itemType.getSimpleName();
        boolean isMap = "HashMap".equals(className);

        Object real = null, val = null;
        Map<String, Object> dataMap = null;

        if (style == null) {
            style = getStyle(wb);
        }
        for (int l = 0, itemLen = items.size(); l < itemLen; l++) {
            HSSFRow row = sheet.createRow(l + start);
            if (isMap) {
                dataMap = (Map<String, Object>) items.get(l);
            } else {
                real = items.get(l);
            }
            //序号
            HSSFCell xcell = row.createCell(0);
            xcell.setCellType(l + 1);
            xcell.setCellStyle(style);
            //遍历列
            for (int i = 1; i <= headName.length; i++) {
                if (isMap) {
                    val = dataMap.get(headName[i].toUpperCase());
                } else {
                    val = itemType.getMethod(getMethodString(headName[i]), null).invoke(real, null);
                }
                HSSFCell cell = row.createCell(i);
                if (val != null) {
                    String ct = val.getClass().getSimpleName();
                    if (ct != null && ("Double".equals(ct) || "Integer".equals(ct) || "Float".equals(ct) || "Long".equals(ct) || "Short".equals(ct) || "Byte".equals(ct))) {
                        cell.setCellValue(Double.parseDouble(String.valueOf(val)));
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(style);
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
            sheet.setColumnWidth(colNum, columnWidth > 160 ? 160 * 256 : columnWidth * 256);
        }

    }

    /**
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
        // TODO Auto-generated method stub
        HSSFCellStyle style = workbook.createCellStyle();
        //水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        return style;
    }

    /**
     * 解析表头
     *
     * @param headerPair
     * @return
     */
    private String[][] parseHeadArr(String[] headerPair) {
        int headLabelLen = headerPair.length;
        String[] headName = new String[headLabelLen], headDataName = new String[headLabelLen];
        for (int i = 0; i < headLabelLen; i++) {
            String[] splitArr = headerPair[i].split("=");
            if (splitArr.length != 2) {
                headName[i] = headerPair[i];
                headDataName[i] = headerPair[i];
            } else {
                headName[i] = splitArr[0];
                headDataName[i] = splitArr[1];
            }
        }
        return new String[][]{headName, headDataName};
    }

    /**
     * 创建表头
     *
     * @param wb
     * @param sheet
     * @param headName       表头数组
     * @param columnTopStyle 表头样式
     * @return
     */
    private HSSFSheet createExcelHead(HSSFWorkbook wb, HSSFSheet sheet, String[] headName, HSSFCellStyle columnTopStyle) {
        HSSFRow row = sheet.createRow(2);
        for (int i = 0, l = headName.length; i < l; i++) {
            HSSFCell cell = row.createCell(i);                                //创建列头对应个数的单元格
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);                    //设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(headName[i]);
            cell.setCellValue(text);                                        //设置列头单元格的值
            cell.setCellStyle(columnTopStyle);                            //设置列头单元格样式
        }
        return sheet;
    }

    /**
     * 填充数据
     *
     * @param wb
     * @param sheet
     * @param dataLabel 取字段的数组
     * @param items     数据列表
     * @param itemType  数据类型
     * @param dataStyle 数据样式
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void fillExcelData(HSSFWorkbook wb, HSSFSheet sheet, String[] dataLabel, List<?> items, Class<?> itemType, HSSFCellStyle dataStyle) throws Exception {
        String className = itemType.getSimpleName();
        boolean isMap = "HashMap".equals(className);

        Object real = null, val = null;
        Map<String, Object> dataMap = null;

        if (dataStyle == null) {
            dataStyle = getStyle(wb);
        }
        for (int l = 0, itemLen = items.size(); l < itemLen; l++) {
            HSSFRow row = sheet.createRow(l + 3);
            if (isMap) {
                dataMap = (Map<String, Object>) items.get(l);
            } else {
                real = items.get(l);
            }
            //遍历列
            for (int i = 0; i < dataLabel.length; i++) {
                if (isMap) {
                    val = dataMap.get(dataLabel[i].toUpperCase());
                } else {
                    val = itemType.getMethod(getMethodString(dataLabel[i]), null).invoke(real, null);
                }
                HSSFCell cell = row.createCell(i);
                if (val != null) {
                    String ct = val.getClass().getSimpleName();
                    if (ct != null && ("Double".equals(ct) || "Integer".equals(ct) || "Float".equals(ct) || "Long".equals(ct) || "Short".equals(ct) || "Byte".equals(ct))) {
                        cell.setCellValue(Double.parseDouble(String.valueOf(val)));
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                } else {
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
            sheet.setColumnWidth(colNum, columnWidth > 160 ? 160 * 256 : columnWidth * 256);
        }
    }

}
