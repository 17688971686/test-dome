package com.sn.framework.core.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sn.framework.common.SnRuntimeException;
import com.sn.framework.common.StringUtil;
import com.sn.framework.core.shiro.IllegalCaptchaException;
import com.sn.framework.core.web.ClientType;
import com.sn.framework.jxls.JxlsUtils;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.model.UserDto;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;

import static com.sn.framework.common.StringUtil.*;
import static com.sn.framework.core.Constants.USER_SESSION_KEY;

/**
 * Description: 基础工具类
 *
 * @author: tzg
 * @date: 2017/12/21 9:38
 */
public class SNKit {

    private final static Logger logger = LoggerFactory.getLogger(SNKit.class);

//    public static Subject shiroLogin(ServletRequest request, ServletResponse response, String name) {
//        PrincipalCollection principals = new SimplePrincipalCollection(name, "WeChatRealm");
//        WebSubject.Builder builder = new WebSubject.Builder(request, response);
//        builder.principals(principals);
//        builder.authenticated(true);
//        Subject subject = builder.buildWebSubject();
//        ThreadContext.bind(subject);
//        return subject;
//    }

    /**
     * 用户登录成功后，添加Session信息
     *
     * @param subject
     * @param user
     */
    public static void addSessionAttribute(Subject subject, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("把用户信息设置为session属性值");
        }
        Session session = subject.getSession();
        UserDto userDto = user.convert(UserDto.class, "password", "userSalt", "organ");
        if (null != user.getOrgan()) {
            userDto.setOrgan(user.getOrgan().convert(Organ.class, "users", "resources"));
        }

        // 在session中设置用户信息
        session.setAttribute(USER_SESSION_KEY, userDto);
    }

    /**
     * 抽取登录失败的错误消息
     *
     * @param exception
     * @return
     */
    public static String extractErrorMsg(String exception) {
        String msg = "";
        if (StringUtil.isNotBlank(exception)) {
            logger.debug("登录异常: {}", exception);
            if (IllegalCaptchaException.class.getName().equals(exception)) {
                logger.info("验证码错误");
                msg = "验证码错误";
            } else if (UnknownAccountException.class.getName().equals(exception)) {
                logger.info("账号不存在");
                msg = "账号或密码不正确";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                logger.info("密码不正确");
                msg = "账号或密码不正确";
            } else if (ExcessiveAttemptsException.class.getName().equals(exception)) {
                logger.info("登录失败次数过多,请明天再试！");
                msg = "登录失败次数过多,请明天再试！";
            } else if (DisabledAccountException.class.getName().equals(exception)) {
                logger.info("账号已停用");
                msg = "账号已停用";
            } else if (AuthenticationException.class.getName().equals(exception)) {
                logger.info("账号已停用");
                msg = "用户登录失败";
            } else {
                logger.info(exception);
                msg = exception;
            }
        }
        return msg;
    }

    /**
     * 用户密码加密
     *
     * @param user
     * @param isEncryption
     */
    public static void decodePwd(User user, boolean isEncryption) {
        if (isEncryption) {
            //组装MD5密码,形式（ MD5（MD5(PASSWORD+SALT2))））
            String salt1 = new SecureRandomNumberGenerator().nextBytes().toHex();
            String password = decodePwd(user.getUsername(), user.getPassword(), salt1);
            user.setPassword(password);
            user.setUserSalt(salt1);
        }
    }

    /**
     * 密码加密
     *
     * @param username
     * @param password
     * @param passwordSalt
     * @return
     */
    public static String decodePwd(String username, String password, String passwordSalt) {
        if (logger.isDebugEnabled()) {
            logger.debug("对用户密码进行加密");
        }
        //组装MD5密码,形式（ MD5（MD5(PASSWORD+SALT2))））
        String salt2 = Cryptography.md5(passwordSalt, username);
        password = Cryptography.md5(password, password + salt2, 2);
        return password;
    }

    /**
     * 必须是由数字和字母组成的 8 到 16 位字符
     */
    public final static Pattern PASSWORD_PATTERN = Pattern.compile(
            //"^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$"
            "^(?![^a-zA-Z]+$)(?!\\D+$)"
    );


    /**
     * 检查密码复杂度
     *
     * @param password
     * @return
     */
    public static boolean checkPwd(String password) {
        if (StringUtil.isBlank(password)) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).find();
    }

    /**
     * 判断请求是否ajax
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        String x = request.getHeader("X-Requested-With");
        return StringUtil.isNotBlank(x) && "XMLHttpRequest".equalsIgnoreCase(x);
    }

    /**
     * 判断是否json的响应
     *
     * @param request
     * @return
     */
    public static boolean isJsonContent(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (StringUtil.isNotBlank(accept)) {
            return accept.contains(MediaType.APPLICATION_JSON_VALUE);
        }
        String x = request.getContentType();
        return StringUtil.isNotBlank(x) && x.contains(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 返回JSON数据格式的信息
     *
     * @param response
     * @param httpStatus
     * @param result
     * @throws IOException
     */
    public static void printJsonMsg(HttpServletResponse response, HttpStatus httpStatus, Object result) {
        if (logger.isDebugEnabled()) {
            logger.debug("返回json格式的消息");
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setHeader("Charset", UTF_8.name());
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(httpStatus.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            if (result instanceof CharSequence) {
                writer.print(result);
            } else {
                writer.print(JacksonUtils.objectToString(result));
            }
            writer.flush();
        } catch (JsonProcessingException e) {
            logger.error("响应消息转换失败", e);
        } catch (IOException e) {
            logger.error("响应处理失败", e);
        } finally {
            if (null != writer) {
                writer.close();
                try {
                    response.flushBuffer();
                } catch (IOException e) {
                    logger.error("响应异常", e);
                }
            }
        }
    }

    /**
     * 文件写入（文件上传）
     *
     * @param multipartFile
     * @param rootPath
     * @param outputPatch
     */
    public static void fileInput(MultipartFile multipartFile, String rootPath, String outputPatch) {
        Assert.hasText(rootPath, "缺少附件主目录的系统配置");

        File outFile = new File(rootPath, outputPatch);

        if (!outFile.getParentFile().exists()) { //检测目录是否存在，不存在则创建
            // 如果文件所在的目录不存在，则创建目录
            if (!outFile.getParentFile().mkdir()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("创建文件所在的目录失败!");
                }
                throw new SnRuntimeException("文件写入失败");
            }
        }
        try {
            multipartFile.transferTo(outFile);
        } catch (IOException e) {
//            e.printStackTrace();
            throw new SnRuntimeException("文件写入失败");
        }
    }

    /**
     * 文件写入
     *
     * @param input
     * @param rootPath
     * @param outputPatch
     */
    public static void fileInput(InputStream input, String rootPath, String outputPatch) {
        Assert.hasText(rootPath, "缺少附件主目录的系统配置");

        File rootFile = new File(rootPath);
        // 确保根目录存在
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        File outFile = new File(rootPath, outputPatch);

        //检测目录是否存在，不存在则创建
        if (!outFile.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!outFile.getParentFile().mkdir()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("创建文件所在的目录失败!");
                }
                throw new SnRuntimeException("文件写入失败");
            }
        }

        try (OutputStream outputStream = new FileOutputStream(outFile)) {
            IOUtils.copy(input, outputStream);
        } catch (IOException e) {
//            e.printStackTrace();
            throw new SnRuntimeException("文件写入失败");
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("文件流关闭失败  {}", e);
                }
            }
        }
    }

    /**
     * 文件下载
     *
     * @param response
     * @param outFile
     */
    public static void fileDownload(HttpServletResponse response, File outFile) {
        Assert.notNull(outFile, "缺少参数");
        if (!outFile.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("硬盘上不存在文件 {}", outFile.getName());
            }
            throw new SnRuntimeException(String.format("找不到【%s】文件", outFile.getName()));
        }
        //返回头 文件大小
        response.addHeader("Content-Length", "" + outFile.length());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(outFile);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在 {}", e);
        }
        fileDownload(response, inputStream);
    }

    /**
     * 文件下载
     *
     * @param response
     * @param inputStream
     */
    public static void fileDownload(HttpServletResponse response, InputStream inputStream) {
        try (OutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
//            e.printStackTrace();
            throw new SnRuntimeException("文件下载失败");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("文件流关闭失败  {}", e);
                }
            }
        }
    }

    /**
     * 文件下载
     *
     * @param response     请求响应对象
     * @param outFile      下载的文件对象
     * @param originalName 文件原始名称
     * @throws IOException
     */
    public static void fileDownload(HttpServletRequest request, HttpServletResponse response, File outFile, String originalName) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String filename = originalName;
        String userAgent = request.getHeader("user-agent");
        // 判断是否 ie 浏览器
        if (userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1 || userAgent.indexOf("Edge") != -1) {
            filename = new String(filename.getBytes(GBK), ISO_8859_1);
        } else {
            filename = new String(filename.getBytes(UTF_8), ISO_8859_1);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        fileDownload(response, outFile);
    }

    /**
     * 获取系统基础路径
     *
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        if (port != 80) {
            path = ":" + port + path;
        }
        return scheme + "://" + serverName + path;
    }

    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtil.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    private static String unknown = "unknown";

    /**
     * 判断 是否是微信浏览器
     *
     * @param request
     * @return
     */
    public static boolean isWechat(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return StringUtil.isNotBlank(userAgent) && userAgent.toLowerCase().indexOf("micromessenger") > -1;
    }

    /**
     * 获取客户端类型
     *
     * @param userAgent 从 request.getHeader("user-agent")  中获取
     * @return
     */
    public static ClientType getClientType(String userAgent) {
        if (StringUtil.isNotBlank(userAgent)) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("msie")) {
                return ClientType.IE;
            } else if (userAgent.contains("micromessenger")) {
                return ClientType.WECHAT;
            } else if (userAgent.contains("chrome")) {
                return ClientType.CHROME;
            } else if (userAgent.contains("safari")) {
                return ClientType.SAFARI;
            } else if (userAgent.contains("firefox")) {
                return ClientType.FIREFOX;
            } else {
                return ClientType.OTHER;
            }
        } else {
            return ClientType.UNKNOWN;
        }
    }

    /**
     * 导出excel
     *
     * @param filename      指定导出的文件名
     * @param templateName  模板路径
     * @param request
     * @param response
     * @param params        模板的参数集
     * @param funcs         模板的函数集
     * @throws IOException
     */
    public static void exportExcel(String filename, String templateName, HttpServletRequest request,
                                   HttpServletResponse response, Map<String, Object> params,
                                   Map<String, Object> funcs) throws IOException {
        String userAgent = request.getHeader("user-agent");
        // 判断是否 ie 浏览器
        if (userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1 || userAgent.indexOf("Edge") != -1) {
            filename = new String(filename.getBytes(GBK), ISO_8859_1);
        } else {
            filename = new String(filename.getBytes(UTF_8), ISO_8859_1);
        }
        response.setCharacterEncoding(UTF_8.name());
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        JxlsUtils.exportExcel(templateName, response.getOutputStream(), params, funcs);
    }

    /**
     * 导出excel
     *
     * @param filename      指定导出的文件名
     * @param templateName  模板路径
     * @param request
     * @param response
     * @param params        模板的参数集
     * @throws IOException
     */
    public static void exportExcel(String filename, String templateName, HttpServletRequest request,
                                   HttpServletResponse response, Map<String, Object> params) throws IOException {
        exportExcel(filename, templateName, request, response, params, null);
    }

    public static void main(String[] args){
        String password = decodePwd("admin","admin","1023ca3b7d3adaa82a8eca715bc4a4ca");
        System.out.println(password);
    }
}