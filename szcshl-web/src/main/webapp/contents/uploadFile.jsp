<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="java.io.*" %>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload"/>
<%
    try {
        // ��ʼ���ϴ����
        mySmartUpload.initialize(pageContext);
        mySmartUpload.upload();
        com.jspsmart.upload.File myFile = null;
        myFile = mySmartUpload.getFiles().getFile(0);
        //String filePath = request.getParameter("file");
        String localFilePath = request.getParameter("localFilePath");
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
