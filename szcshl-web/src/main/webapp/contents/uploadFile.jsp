<%@ page import="java.io.*" %>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload"/>
<%
    try {
        // 初始化上传组件
        mySmartUpload.initialize(pageContext);
        mySmartUpload.upload();
        com.jspsmart.upload.File myFile = null;
        myFile = mySmartUpload.getFiles().getFile(0);
        //String filePath = request.getParameter("file");
        String localFilePath = request.getParameter("localFilePath");
        if (!myFile.isMissing()) {
            myFile.saveAs(localFilePath, mySmartUpload.SAVE_PHYSICAL);    // 保存上传文件到内存
            out.clear();
            out.write("success");//返回控件HttpPost()方法值。
            out.flush();
        }
    } catch (Exception e) {
        out.clear();
        out.write("failed");//返回控件HttpPost()方法值。
        out.flush();
    }%>