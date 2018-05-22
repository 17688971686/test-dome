package cs.ahelper;

import cs.common.constants.Constant;
import cs.common.utils.PropertyUtil;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * 用openoffice把word转pdf
 */
public class OpenOfficePdfConvert {

    /**
     * @param args
     */
    private static OfficeManager officeManager;
    private static String OPENOFFICEPATH = "OPENOFFICEPATH";
    private static int port[] = { 8100 };

    public static void convert2PDF(String inputFile, String outputFile) throws FileNotFoundException {

        startService();
        System.out.println("进行文档转换转换:" + inputFile + " --> " + outputFile);

        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(new File(inputFile), new File(outputFile));

        stopService();
        System.out.println();

    }

    public static void convert2PDF(File inputFile, File outputFile) throws FileNotFoundException {

        startService();
        System.out.println("进行文档转换转换:" + inputFile + " --> " + outputFile);

        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(inputFile, outputFile);

        stopService();
        System.out.println();

    }

    // 打开服务器
    public static void startService() {
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            String OFFICE_HOME = propertyUtil.readProperty(OPENOFFICEPATH);
            System.out.println("准备启动服务....");
            configuration.setOfficeHome(OFFICE_HOME);// 设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); // 设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时

            officeManager = configuration.buildOfficeManager();
            officeManager.start(); // 启动服务
            System.out.println("office转换服务启动成功!");
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }

    // 关闭服务器
    public static void stopService() {
        System.out.println("关闭office转换服务....");
        if (officeManager != null) {
            officeManager.stop();
        }
        System.out.println("关闭office转换成功!");
    }

    public static void main(String[] args) throws Exception {
        /*int wdFormatPDF = 17;// word转PDF 格式
        String path = "D:/szec_uploadfile/2017_12_18_12_377764303.doc";
        String path2 = "D:/szec_uploadfile/2017_12_18_12_377764303_2.pdf";
        ActiveXComponent _app = new ActiveXComponent("Word.Application");
        _app.setProperty("Visible", Variant.VT_FALSE);

        Dispatch documents = _app.getProperty("Documents").toDispatch();
        File file = new File(path);
        // 打开FreeMarker生成的Word文档
        Dispatch doc = Dispatch.call(documents, "Open", file.getAbsolutePath(), Variant.VT_FALSE, Variant.VT_TRUE).toDispatch();
        // 另存为新的Word文档
        Dispatch.call(doc, "SaveAs", path2, wdFormatPDF);

        Dispatch.call(doc, "Close", Variant.VT_FALSE);
        _app.invoke("Quit", new Variant[] {});
        ComThread.Release();*/
       /* File file2 = new File(path2);
        String filePath = path.substring(0, path.lastIndexOf(".")) + ".pdf";
        File printFile = new File(filePath);
        OpenOfficePdfConvert.convert2PDF(file2, printFile);*/
    }

}
