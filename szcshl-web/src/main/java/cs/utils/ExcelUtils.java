package cs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.IEEE754rUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cs.domain.RoomBooking;


public class ExcelUtils {

	//导出本周评审会议安排
	public static void fillExcelData(List<RoomBooking> foom,Workbook wb,String[] headers)throws Exception{
		int rowIndex=0;
		//创建sheet页面
		Sheet sheet=wb.createSheet();
		 //创建行
		Row row=sheet.createRow(rowIndex++);
		//遍历头信息
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
	
		//遍历集合所有值
		
		for (RoomBooking rb :foom) {
			row=sheet.createRow(rowIndex++);
//			int i=0;
					//创建行并且给行添加数据
					row.createCell(0).setCellValue(rb.getRbName().toString());
					row.createCell(1).setCellValue(rb.getRbDay().toString());
					row.createCell(2).setCellValue(rb.getBeginTime().toString());
					row.createCell(3).setCellValue(rb.getEndTime().toString());
					row.createCell(4).setCellValue(rb.getDueToPeople().toString());
					row.createCell(5).setCellValue(rb.getHost().toString());
					//row.createCell(6).setCellValue(rb.getAddressName().toString());
//					i++;
			}
	}
	//导出本周全部会议安排
	public static void findWeek(List<RoomBooking> room,Workbook wb,String[] headers){
		int rowIndex=0;
		//创建sheet页面
		Sheet sheet=wb.createSheet();
		 //创建行
		Row row=sheet.createRow(rowIndex++);
		//遍历头信息
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
	
		//遍历集合所有值
		
		for (RoomBooking rb : room) {
			row=sheet.createRow(rowIndex++);
//			int i=0;
					//创建行并且给行添加数据
					row.createCell(0).setCellValue(rb.getRbName().toString());
					row.createCell(1).setCellValue(rb.getRbDay().toString());
					row.createCell(2).setCellValue(rb.getBeginTime().toString());
					row.createCell(3).setCellValue(rb.getEndTime().toString());
					row.createCell(4).setCellValue(rb.getDueToPeople().toString());
					row.createCell(5).setCellValue(rb.getHost().toString());
					//row.createCell(6).setCellValue(rb.getAddressName().toString());
//					i++;
			}
	}
	//导出下周全部会议安排
	public static void excelNextWeek(List<RoomBooking> room,Workbook wb,String[] headers) throws Exception{
		int rowIndex =0;
		Sheet sheet =wb.createSheet();
		Row row =sheet.createRow(rowIndex++);
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
		for(RoomBooking rb : room){
			row = sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(rb.getRbName().toString());
			row.createCell(1).setCellValue(rb.getRbDay().toString());
			row.createCell(2).setCellValue(rb.getBeginTime().toString());
			row.createCell(3).setCellValue(rb.getEndTime().toString());
			row.createCell(4).setCellValue(rb.getDueToPeople().toString());
			row.createCell(5).setCellValue(rb.getHost().toString());
			//row.createCell(6).setCellValue(rb.getAddressName().toString());
		}
	}
	//导出下周评审会议安排
	public static void exportStageNextWeek(List<RoomBooking> room ,Workbook wb , String [] headers) throws Exception{
		
		int rowIndex = 0;
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(rowIndex++);
		
		for(int i=0;i<headers.length;i++){
			
			row.createCell(i).setCellValue(headers[i]);
			
		}
		for(RoomBooking rb : room){
			row = sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(rb.getRbName().toString());
			row.createCell(1).setCellValue(rb.getRbDay().toString());
			row.createCell(2).setCellValue(rb.getBeginTime().toString());
			row.createCell(3).setCellValue(rb.getEndTime().toString());
			row.createCell(4).setCellValue(rb.getDueToPeople().toString());
			row.createCell(5).setCellValue(rb.getHost().toString());
		}
	}
	
	//会议导出
	public static  void exports(HttpServletResponse response,Workbook wb,String fileName)throws Exception{
		response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"iso8859-1"));
		response.setContentType("application/ynd.ms-excel;charset=UTF-8");
		OutputStream out=response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
	}
	
	
}
