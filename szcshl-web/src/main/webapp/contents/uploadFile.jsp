<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="java.io.*" %>
<%@ page import="cs.common.utils.SysFileUtil" %>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload"/>
<%
    try {
        // ��ʼ���ϴ����
        mySmartUpload.initialize(pageContext);
        mySmartUpload.upload();
        com.jspsmart.upload.File myFile = null;
        myFile = mySmartUpload.getFiles().getFile(0);
        String fileName = request.getParameter("fileName");
        fileName = java.net.URLDecoder.decode(java.net.URLDecoder.decode(fileName,"UTF-8"),"UTF-8");
        String filePath = SysFileUtil.getUploadPath();
        filePath = filePath.replaceAll("\\\\", "/");
        String localFilePath =  filePath + File.separator + fileName;
        if (!myFile.isMissing()) {
            myFile.saveAs(localFilePath, mySmartUpload.SAVE_PHYSICAL);    // �����ϴ��ļ����ڴ�
            out.clear();
            out.write("����ɹ�");//���ؿؼ�HttpPost()����ֵ��
            out.flush();
        }
    } catch (Exception e) {
        out.clear();
        out.write("failed");//���ؿؼ�HttpPost()����ֵ��
        out.flush();
    }%>
