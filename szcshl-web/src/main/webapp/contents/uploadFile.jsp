<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="java.io.*" %>
<%@ page import="cs.common.utils.SysFileUtil" %>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload"/>
<%
    try {
        // 初始化上传组件
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
            myFile.saveAs(localFilePath, mySmartUpload.SAVE_PHYSICAL);    // 保存上传文件到内存
            out.clear();
            out.write("保存成功");//返回控件HttpPost()方法值。
            out.flush();
        }
    } catch (Exception e) {
        out.clear();
        out.write("failed");//返回控件HttpPost()方法值。
        out.flush();
    }%>
