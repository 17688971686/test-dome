package cs.common.utils;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: excel读取工具类
 * Author: mcl
 * Date: 2018/6/25 10:49
 */
public class ExcelReader {

    private Workbook wb;
    private Sheet sheet;
    private Row row;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *
     * @param filepath Excel文件路径
     * @throws Exception
     */
    public ExcelReader(String filepath) throws Exception {
        InputStream is = new FileInputStream(filepath);
        wb = WorkbookFactory.create(is);
        sheet = wb.getSheetAt(0);
    }

    public ExcelReader()  throws Exception {
    }

    public ExcelReader(InputStream is)  throws Exception {
        wb = WorkbookFactory.create(is);
        sheet = wb.getSheetAt(0);
    }

    public ExcelReader(String filepath,int sheetNo) throws Exception {
        InputStream is = new FileInputStream(filepath);
        wb = WorkbookFactory.create(is);
        sheet = wb.getSheetAt(sheetNo);
    }

    public ExcelReader(InputStream is,int sheetNo)  throws Exception {
        wb = WorkbookFactory.create(is);
        sheet = wb.getSheetAt(sheetNo);
    }

    /**
     * 读取Excel数据内容
     */
    public void readExcelContent(RowHandler rowHandler) throws Exception {
        readExcelContent(rowHandler,0);
    }

    /**
     * 读取Excel数据内容
     */
    public void readExcelContent(RowHandler rowHandler,int startRow) throws Exception {
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        if(startRow > rowNum){
            return;
        }
        for (int i = startRow ; i <= rowNum; i++) {
            row = sheet.getRow(i);
            if(row == null){
                continue;
            }
            int colNum = row.getPhysicalNumberOfCells();
            List<Object> cellValueList = new ArrayList<Object>(colNum);
            for (int j = 0; j <= colNum; j++) {
                cellValueList.add(getCellFormatValue(row.getCell(j)));
            }
            rowHandler.handle(cellValueList);
        }
    }


    /**
     * 根据XSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellvalue = sdf.format(date);
                    } else {
                        // 如果是纯数字取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRING
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = "";
            }
        }
        return cellvalue;
    }


    public interface RowHandler {
        void handle(final List<Object> row) throws Exception;
    }

    /**
     *
     * 读取Excel数据内容
     * @param is 文件流
     * @param beginnum 开始行 (包含本行)
     * @param endnum 结束行 ( 包含本行，如为 0 表示最终行)
     * @return List 包含单元格数据内容的Map对象
     */
    public List<Map<Integer, String>> readExcelContent(InputStream is,int beginnum,int endnum) {
        List<Map<Integer, String>>  list = new ArrayList<Map<Integer, String>>();
        //文件流重用
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        try {
            //.xlsx文件处理
            wb = new XSSFWorkbook( new ByteArrayInputStream(baos.toByteArray()));
        } catch (Exception e) {
//                e.printStackTrace();
            //.xls文件处理
            try {
                wb = new HSSFWorkbook( new ByteArrayInputStream(baos.toByteArray()));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        int colNum = 0;
        if(rowNum >0 ){
            row = sheet.getRow(3);
            colNum = row.getPhysicalNumberOfCells();
        }
        //判断内容起始行
        if(beginnum < 1 ){
            beginnum = 1;
        }
        if(endnum == 0 ||endnum > (rowNum+1)){
            endnum = rowNum+1;
        }
        for (int i = beginnum-1; i < endnum; i++) {
            Map<Integer, String> content = new HashMap<Integer, String>();
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                String str = getCellFormatValue(row.getCell((short) j)).trim();
                content.put(j, str);
                j++;
            }
            list.add(content);
        }
        return list;
    }
}