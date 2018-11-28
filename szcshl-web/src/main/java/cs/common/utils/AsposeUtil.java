package cs.common.utils;

import com.aspose.cells.License;
import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ldm on 2018/11/16 0016.
 */
public class AsposeUtil {

    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            File templateFile = ResourceUtils.getFile("classpath:license.xml");
            InputStream is =  new FileInputStream(templateFile);
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void doc2pdf(String wordPath, String pdfPath) {
        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            long old = System.currentTimeMillis();
            File file = new File(pdfPath);  //新建一个pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(wordPath);                    //Address是将要被转化的word文档
            doc.save(os, com.aspose.words.SaveFormat.PDF);            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            os.close();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param excelPath
     * @param pdfPath
     */
    public static void excel2pdf(String excelPath, String pdfPath) {
        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            long old = System.currentTimeMillis();
            Workbook wb = new Workbook(excelPath);// 原始excel路径
            FileOutputStream fileOS = new FileOutputStream(new File(pdfPath));
            wb.save(fileOS, com.aspose.cells.SaveFormat.PDF);
            fileOS.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ppt2pdf(String pptPath, String pdfPath){
        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            long old = System.currentTimeMillis();
            File file = new File(pdfPath);// 输出pdf路径
            Presentation pres = new Presentation(pptPath);//输入pdf路径
            FileOutputStream fileOS = new FileOutputStream(file);
            pres.save(fileOS, com.aspose.slides.SaveFormat.Pdf);
            fileOS.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断预览文件类型是否需要转成pdf
     * @param fileType
     * @return
     */
    public static boolean neddConvertToPdf(String fileType) {
        final Set<String> ALLOW_PATH_SET = new HashSet();
        ALLOW_PATH_SET.add(".doc");
        ALLOW_PATH_SET.add(".docx");
        ALLOW_PATH_SET.add(".xls");
        ALLOW_PATH_SET.add(".xlsx");
        ALLOW_PATH_SET.add(".ppt");
        ALLOW_PATH_SET.add(".pptx");
        return ALLOW_PATH_SET.contains(fileType);
    }

    /**
     * 支持DOC, DOCX, OOXML, RTF, HTML, OpenDocument, PDF, EPUB, XPS, SWF等相互转换<br>
     *
     * @param args
     */
    public static void main(String[] args) {
       /* // 验证License
        if (!getLicense()) {
            return;
        }

        try {
            long old = System.currentTimeMillis();
           *//* Workbook wb = new Workbook("D:\\myProjects.xls");// 原始excel路径
            File pdfFile = new File("D:\\myProjects.pdf");// 输出路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);*//*

           *//* Document doc = new Document("D:\\评审中心问题整理20181114.doc");
            File pdfFile2 = new File("D:\\评审中心问题整理20181114.pdf");
            FileOutputStream fileOS2 = new FileOutputStream(pdfFile2);
            doc.save(fileOS2, com.aspose.words.SaveFormat.PDF);
            fileOS2.flush();
            fileOS2.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");*//*

            //AsposeUtil.ppt2pdf("D:\\测试.pptx","D:\\test4.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
