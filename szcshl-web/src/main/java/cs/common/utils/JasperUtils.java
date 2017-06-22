package cs.common.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description: Jasper报表引擎工具类
 * Author: tzg
 * Date: 2017/6/5 18:05
 */
public class JasperUtils {

    private static final Logger logger = Logger.getLogger(JasperUtils.class);

    /**
     * 获取JasperPrint
     * @param jasperPath
     * @param parameters
     * @param beanCollection
     * @return
     * @throws JRException
     */
    public static JasperPrint fillReport(String jasperPath, Map<String, Object> parameters, Collection<?> beanCollection) throws JRException {
        return JasperFillManager.fillReport(JasperUtils.class.getResourceAsStream(jasperPath), parameters, new JRBeanCollectionDataSource(beanCollection));
    }

    /**
     * 报表输出格式
     */
    public enum JasperFormat {
        HTML,
        CSV,
        XML,
        XLS,
        XLSX,
        RTF,
        DOCX,
        PDF;
    }

    /**
     * 根据设置的格式输出报表（默认为HTML格式）
     * @param print         报表JasperPrint实体
     * @param format        输出格式
     * @param response      http相应对象
     * @param exportName    输出文件名（不包含后缀）
     * @return
     * @throws JRException
     * @throws IOException
     */
    public static JRExporter reportToExport(JasperPrint print, String format, HttpServletResponse response, String exportName) throws JRException, IOException {
        List<JasperPrint> printList = new ArrayList<JasperPrint>(1);
        printList.add(print);
        return reportToExport(printList, format, response, exportName);
    }

    /**
     * 根据设置的格式输出报表（默认为HTML格式）
     * @param printList     报表JasperPrint实体
     * @param format        输出格式
     * @param response      http相应对象
     * @param exportName    输出文件名（不包含后缀）
     * @return
     * @throws JRException
     */
    public static JRExporter reportToExport(List<JasperPrint> printList, String format, HttpServletResponse response, String exportName) throws JRException, IOException {
        JRExporter exporter;
        boolean html = false;
        if (StringUtil.isBlank(format)) {       // 设置默认值，避免空字符串
            format = "HTML";
        }
        if (StringUtil.isBlank(exportName)) {   // 设置默认值，避免空字符串
            exportName = "Report File";
        }
        exportName = new String(exportName.getBytes(), "ISO-8859-1"); // 下载文件时，避免中文的文件名无法显示

        final OutputStream outputStream = response.getOutputStream();

        if (JasperFormat.CSV.toString().equalsIgnoreCase(format)) {
            exporter = new JRCsvExporter();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".csv");
        } else if (JasperFormat.XML.toString().equalsIgnoreCase(format)) {
            exporter = new JRXmlExporter();
        } else if (JasperFormat.XLS.toString().equalsIgnoreCase(format)) {
            exporter = new JRXlsExporter();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".xls");
        } else if (JasperFormat.XLSX.toString().equalsIgnoreCase(format)) {
            exporter = new JRXlsxExporter();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".xlsx");
        } else if (JasperFormat.RTF.toString().equalsIgnoreCase(format)) {
            exporter = new JRRtfExporter();
            response.setContentType("application/rtf");
            response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".rtf");
        } else if (JasperFormat.DOCX.toString().equalsIgnoreCase(format)) {
            exporter = new JRDocxExporter();
            response.setContentType("application/msword");
            response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".docx");
        } else if (JasperFormat.PDF.toString().equalsIgnoreCase(format)) {
            exporter = new JRPdfExporter();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=" + exportName + ".pdf");
        } else {
            exporter = new HtmlExporter();
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
            html = true;
        }
        System.out.println(response.getHeader("Content-disposition"));

        if (!html) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        }

        exporter.setExporterInput(SimpleExporterInput.getInstance(printList));
        exporter.exportReport();
        return exporter;
    }

    /**
     * 根据设置的格式输出报表（默认为HTML格式）
     * @param jasperPath        报表模板路径
     * @param parameters        报表参数
     * @param beanCollection    报表数据源
     * @param format            输出格式
     * @param response          HTTP相应对象
     * @param exportName        输出文件名（不包含后缀）
     * @return
     * @throws JRException
     * @throws IOException
     */
    public static JRExporter fillReportToExport(String jasperPath, Map<String, Object> parameters, Collection<?> beanCollection, String format, HttpServletResponse response, String exportName) throws JRException, IOException {
        return reportToExport(fillReport(jasperPath, parameters, beanCollection), format, response, exportName);
    }

    /**
     * 根据设置的格式输出报表
     * @param print         报表JasperPrint实体
     * @param format        JasperFormat格式对象
     * @param outputStream  输出流对象
     * @return
     * @throws JRException
     */
    public static JRExporter reportToExport(JasperPrint print, JasperFormat format, OutputStream outputStream) throws JRException {
        JRExporter exporter;
        boolean html = false;

        switch (format) {
            case HTML:
                exporter = new HtmlExporter();
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                html = true;
                break;

            case CSV:
                exporter = new JRCsvExporter();
                break;

            case XML:
                exporter = new JRXmlExporter();
                break;

            case XLS:
                exporter = new JRXlsExporter();
                break;

            case XLSX:
                exporter = new JRXlsxExporter();
                break;

            case RTF:
                exporter = new JRDocxExporter();
                break;

            case DOCX:
                exporter = new JRDocxExporter();
                break;

            case PDF:
                exporter = new JRPdfExporter();
                break;

            default:
                logger.error("Unknown reportToExport format: " + format.toString());
                throw new JRException("Unknown reportToExport format: " + format.toString());
        }

        if (!html) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        }

        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.exportReport();
        return exporter;
    }

    /**
     * 指定的报表模板输出html
     * @param jasperPath
     * @param parameters
     * @param beanCollection
     * @param outputStream
     */
    public static void reportToHtml(String jasperPath, Map<String,Object> parameters, Collection<?> beanCollection, OutputStream outputStream) throws JRException {
        reportToExport(fillReport(jasperPath, parameters, beanCollection), JasperFormat.HTML, outputStream);
    }

    /**
     * 指定的报表模板输出Excel
     * @param jasperPath
     * @param parameters
     * @param beanCollection
     * @param outputStream
     * @throws JRException
     */
    public static void reportToExcel(String jasperPath, Map<String,Object> parameters, Collection<?> beanCollection, OutputStream outputStream) throws JRException {
        reportToExport(fillReport(jasperPath, parameters, beanCollection), JasperFormat.XLSX, outputStream);
    }

    /**
     * 指定的报表模板输出PDF
     * @param jasperPath
     * @param parameters
     * @param beanCollection
     * @param outputStream
     * @throws JRException
     */
    public static void reportToPdf(String jasperPath, Map<String,Object> parameters, Collection<?> beanCollection, OutputStream outputStream) throws JRException {
        reportToExport(fillReport(jasperPath, parameters, beanCollection), JasperFormat.PDF, outputStream);
    }

    /**
     * 指定的报表模板输出WORD
     * @param jasperPath
     * @param parameters
     * @param beanCollection
     * @param outputStream
     * @throws JRException
     */
    public static void reportToWord(String jasperPath, Map<String,Object> parameters, Collection<?> beanCollection, OutputStream outputStream) throws JRException {
        reportToExport(fillReport(jasperPath, parameters, beanCollection), JasperFormat.DOCX, outputStream);
    }

}