package cs.common.utils;

/**
 *
 * <p>Title: Tools.java</p>
 * <p>Description: 通用工具,主要为类型转换</p>
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.log4j.Logger;

public class Tools {

	private static final Logger log = Logger.getLogger(Tools.class);

	/**
	 * 十六代码
	 * 
	 * @return string
	 */
	private static String[] HexCode = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	public Tools() {
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null)
			return null;
		int len = hexString.length() / 2;
		byte b[] = new byte[len];
		int p = 0;
		for (int i = 0; i < len; i++) {
			b[i] = (byte) Integer.parseInt(hexString.substring(p, p + 2), 16);
			p += 2;
		}

		return b;
	}

	/**
	 * 将byte 字节转换为十六进制串
	 * 
	 * @param b
	 *            byte
	 * @return string
	 */
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HexCode[d1] + HexCode[d2];
	}

	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + byteToHexString(b[i]);
		}
		return result;
	}

	public static int byteToInt(byte[] b, int offset) {
		return (b[offset] & 0xff) | ((b[offset + 1] & 0xff) << 8) | ((b[offset + 2] & 0xff) << 16)
				| ((b[offset + 3] & 0xff) << 24);
	}

	public static int byteToInt(byte[] b) {
		return (b[0] & 0xff) | ((b[1] & 0xff) << 8) | ((b[2] & 0xff) << 16) | ((b[3] & 0xff) << 24);
	}

	public static long byteToLong(byte[] b) {
		return ((long) b[7] & (long) 255) | (((long) b[6] & (long) 255) << 8) | (((long) b[5] & (long) 255) << 16)
				| (((long) b[4] & (long) 255) << 24) | (((long) b[3] & (long) 255) << 32) | (((long) b[2] & (long) 255) << 40)
				| (((long) b[1] & (long) 255) << 48) | ((long) b[0] << 56);
	}

	public static long byteToLong(byte[] b, int offset) {
		return ((long) b[offset + 7] & (long) 255) | (((long) b[offset + 6] & (long) 255) << 8)
				| (((long) b[offset + 5] & (long) 255) << 16) | (((long) b[offset + 4] & (long) 255) << 24)
				| (((long) b[offset + 3] & (long) 255) << 32) | (((long) b[offset + 2] & (long) 255) << 40)
				| (((long) b[offset + 1] & (long) 255) << 48) | ((long) b[offset] << 56);
	}

	/**
	 * n 为4个字节
	 * 
	 * @param n
	 *            int
	 * @return byte
	 */
	public static byte[] intToByte(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n >> 24);
		b[2] = (byte) (n >> 16);
		b[1] = (byte) (n >> 8);
		b[0] = (byte) n;
		return b;
	}

	/**
	 * n 为待转数据（4个字节），buf[]为转换后的数据，offset为buf[]中转换的起始点 转换后数据从低到高位
	 * 
	 * @param n
	 *            int
	 * @param buf
	 *            byte
	 * @param offset
	 *            offset
	 */
	public static void intToByte(int n, byte[] buf, int offset) {
		buf[offset + 3] = (byte) (n >> 24);
		buf[offset + 2] = (byte) (n >> 16);
		buf[offset + 1] = (byte) (n >> 8);
		buf[offset] = (byte) n;
	}

	public static byte[] shortToByte(int n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	public static void shortToByte(int n, byte[] buf, int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	public static byte[] longToByte(long n) {
		byte[] b = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}

	public static void longToByte(long n, byte[] buf, int offset) {
		buf[offset] = (byte) (int) (n >> 56);
		buf[offset + 1] = (byte) (int) (n >> 48);
		buf[offset + 2] = (byte) (int) (n >> 40);
		buf[offset + 3] = (byte) (int) (n >> 32);
		buf[offset + 4] = (byte) (int) (n >> 24);
		buf[offset + 5] = (byte) (int) (n >> 16);
		buf[offset + 6] = (byte) (int) (n >> 8);
		buf[offset + 7] = (byte) (int) n;
	}

	/**
	 * 大概检查手机号码是否是11位数字，并是否以13开头
	 * 
	 * @param sMobile
	 *            String 传入的手机号码
	 * @return boolean true符合规范
	 */
	public static boolean checkMobile(String sMobile) {
		if (sMobile == null) {
			return false;
		}
		if (sMobile.length() != 11) {
			return false;
		}
		return sMobile.substring(0, 2).equals("13");
	}

	/**
	 * 字符串替换函数
	 * 
	 * @param sAll
	 *            String 原来的字符串
	 * @param sOld
	 *            String 要替换掉的字符串
	 * @param sNew
	 *            String 新的字符串
	 * @return String 替换后的结果
	 */
	public static synchronized String strReplace(String sAll, String sOld, String sNew) {
		int iT = 0;
		String sF = null;
		String sH = null;
		/* 如果新串中包括旧串,不让替多只让替少 */
		if (sNew.indexOf(sOld) != -1) {
			return sAll;
		}
		if ((sAll == null) || (sOld == null) || (sNew == null)) {
			return sAll;
		}
		iT = sAll.indexOf(sOld);
		while (iT != -1) {
			sF = sAll.substring(0, iT);
			sH = sAll.substring(iT + sOld.length());
			sAll = sF + sNew + sH;
			iT = sAll.indexOf(sOld);
		}
		return sAll;
	}

	/**
	 * 过滤接收字符{MO}
	 * 
	 * @param sMo
	 *            String 转换前字符
	 * @return boolean 转换后字符
	 * @说明
	 */
	public static synchronized String convertMoString(String sMo) {
		String sReturn = sMo;
		if (sReturn == null) {
			return sReturn;
		}
		try {
			sReturn = sReturn.toUpperCase();
			sReturn = sReturn.replace('，', ',');
			sReturn = sReturn.replace('。', '.');
			sReturn = sReturn.replace('；', ';');
			sReturn = sReturn.replace('！', '!');
			sReturn = sReturn.replace('？', '?');
			sReturn = sReturn.replace('：', ':');
			sReturn = sReturn.replace('"', '＂');
			sReturn = sReturn.replace('“', '＂');
			sReturn = sReturn.replace('”', '＂');
			// sReturn = sReturn.replace('-', ' ');
			// sReturn = sReturn.replace('_', ' ');
			sReturn = sReturn.replace('，', ',');
			sReturn = sReturn.replace('０', '0');
			sReturn = sReturn.replace('１', '1');
			sReturn = sReturn.replace('２', '2');
			sReturn = sReturn.replace('３', '3');
			sReturn = sReturn.replace('４', '4');
			sReturn = sReturn.replace('５', '5');
			sReturn = sReturn.replace('６', '6');
			sReturn = sReturn.replace('７', '7');
			sReturn = sReturn.replace('８', '8');
			sReturn = sReturn.replace('９', '9');
			sReturn = sReturn.replace('Ａ', 'A');
			sReturn = sReturn.replace('Ｂ', 'B');
			sReturn = sReturn.replace('Ｃ', 'C');
			sReturn = sReturn.replace('Ｄ', 'D');
			sReturn = sReturn.replace('Ｅ', 'E');
			sReturn = sReturn.replace('Ｆ', 'F');
			sReturn = sReturn.replace('Ｇ', 'G');
			sReturn = sReturn.replace('Ｈ', 'H');
			sReturn = sReturn.replace('Ｉ', 'I');
			sReturn = sReturn.replace('Ｊ', 'J');
			sReturn = sReturn.replace('Ｋ', 'K');
			sReturn = sReturn.replace('Ｌ', 'L');
			sReturn = sReturn.replace('Ｍ', 'M');
			sReturn = sReturn.replace('Ｎ', 'N');
			sReturn = sReturn.replace('Ｏ', 'O');
			sReturn = sReturn.replace('Ｐ', 'P');
			sReturn = sReturn.replace('Ｑ', 'Q');
			sReturn = sReturn.replace('Ｒ', 'R');
			sReturn = sReturn.replace('Ｓ', 'S');
			sReturn = sReturn.replace('Ｔ', 'T');
			sReturn = sReturn.replace('Ｕ', 'U');
			sReturn = sReturn.replace('Ｖ', 'V');
			sReturn = sReturn.replace('Ｗ', 'W');
			sReturn = sReturn.replace('Ｘ', 'X');
			sReturn = sReturn.replace('Ｙ', 'Y');
			sReturn = sReturn.replace('Ｚ', 'Z');
			sReturn = strReplace(sReturn, "‘", "'");
		} catch (Exception ex) {
			return sMo;
		}
		return sReturn;
	}

	/**
	 * 过滤接收字符{MT}
	 * 
	 * @param sMt
	 *            String 转换前字符
	 * @return string 转换后字符
	 * @说明
	 */
	public static synchronized String convertMtString(String sMt) {
		String sReturn = sMt;
		if (sReturn == null) {
			return sReturn;
		}
		try {
			sReturn = strReplace(sReturn, "‘", "'");
			sReturn = sReturn.replace('，', ',');
			sReturn = sReturn.replace('。', '.');
			sReturn = sReturn.replace('；', ';');
			sReturn = sReturn.replace('！', '!');
			sReturn = sReturn.replace('？', '?');
			sReturn = sReturn.replace('：', ':');
			sReturn = sReturn.replace('"', '＂');
			sReturn = sReturn.replace('\'', '＇');
			sReturn = sReturn.replace('“', '＂');
			sReturn = sReturn.replace('”', '＂');
		} catch (Exception ex) {
			return sMt;
		}
		return sReturn;
	}

	/**
	 * 过滤字符,以适应JavaScript输出要求
	 * 
	 * @param sMt
	 *            String 转换前字符
	 * @return string 转换后字符
	 * @说明
	 */
	public static synchronized String convert2JsString(String sMt) {
		String sReturn = sMt;
		if (sReturn == null) {
			return sReturn;
		}
		try {
			sReturn = strReplace(sReturn, "'", "‘");
			sReturn = sReturn.replace('"', '＂');
		} catch (Exception ex) {
			return sMt;
		}
		return sReturn;
	}

	/**
	 * is numeric
	 * 
	 * @param msg
	 *            msg body string
	 * @return boolean
	 */
	public static boolean isNumeric(String msg) {
		try {
			Integer.parseInt(msg, 10);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/***
	 * 转换为整数
	 * 
	 * @param msg
	 * @return
	 */
	public static int convert2Int(String msg) {
		int result = -1;
		try {
			result = Integer.parseInt(msg, 10);
		} catch (Exception ex) {
			return result;
		}
		return result;
	}

	/**
	 * 把byte数组转换为字符串
	 * 
	 * @param b
	 *            要转换的数组
	 * @return　转换后的字符串
	 */
	public static String byteArrayToString(byte[] b) {
		byte[] bb = new byte[b.length + 1];
		System.arraycopy(b, 0, bb, 0, b.length);

		return new String(bb).trim();
	}

	/**
	 * 取得手机所属的运营商
	 * 
	 * @param mobileNum
	 *            电话号码
	 * @return String (0移动,1联通,2模拟,3电信)
	 */
	public static String getMobileType(String mobileNum) {
		if (mobileNum == null || mobileNum.length() == 0) {
			return null;
		}

		if (isMoblie(mobileNum))
			return "0";
		else if (isUnicom(mobileNum))
			return "1";
		else if (isTelcom(mobileNum))
			return "3";
		else if (mobileNum.substring(0, 3).equals("999"))
			return "2";
		else
			return null;
	}

	/**
	 * 判断是否为移动的手机号码
	 * 
	 * @param num
	 *            电话号码
	 * @return boolean
	 */
	public static boolean isMoblie(String num) {
		if (num == null)
			return false;

		if (num.substring(0, 3).equals("134") || num.substring(0, 3).equals("135") || num.substring(0, 3).equals("136")
				|| num.substring(0, 3).equals("137") || num.substring(0, 3).equals("138") || num.substring(0, 3).equals("139")) {
			if (num.length() == 11)
				return true;
			else
				return false;
		} else
			return false;
	}

	/**
	 * 判断是否为联通的手机号码
	 * 
	 * @param num
	 *            电话号码
	 * @return boolean
	 */
	public static boolean isUnicom(String num) {
		if (num == null)
			return false;

		if (num.substring(0, 3).equals("130") || num.substring(0, 3).equals("131") || num.substring(0, 3).equals("132")
				|| num.substring(0, 3).equals("133")) {
			if (num.length() == 11)
				return true;
			else
				return false;
		} else
			return false;
	}

	/**
	 * 判断电话号码是否为电信的号码
	 * 
	 * @param num
	 *            电话号码
	 * @return boolean
	 */
	public static boolean isTelcom(String num) {
		if (num == null)
			return false;
		if (num.length() > 7 && num.length() <= 12)
			return true;
		else
			return false;
	}

	/**
	 * 以GZIP格式压缩数据
	 * 
	 * @param data
	 *            String
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] zip(String data) throws Exception {
		return zip(data.getBytes());
	}

	/**
	 * 以GZIP格式压缩数据
	 * 
	 * @param buffer
	 *            byte[]
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] zip(byte[] buffer) throws Exception {
		// 建立字节数组输出流
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		// 建立gzip压缩输出流
		GZIPOutputStream gzout = new GZIPOutputStream(o);
		gzout.write(buffer);
		gzout.finish();
		gzout.close();

		// 返回压缩字节流
		byte[] data_ = o.toByteArray();
		o.close();
		return data_;
	}

	/**
	 * 解压由GZIP压缩的数据
	 * 
	 * @param hexStr
	 *            String
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] unZip(String hexStr) throws Exception {
		return unZip(Tools.hexStringToBytes(hexStr));
	}

	/**
	 * 解压由GZIP压缩的数据
	 * 
	 * @param buffer
	 *            byte[]
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] unZip(byte[] buffer) throws Exception {
		byte[] buf = new byte[4096 * 2];
		// 建立字节数组输入流
		ByteArrayInputStream i = new ByteArrayInputStream(buffer);
		// 建立gzip解压输入流
		GZIPInputStream gzin = new GZIPInputStream(i);
		int size = gzin.read(buf);
		i.close();
		gzin.close();
		byte b[] = new byte[size];
		System.arraycopy(buf, 0, b, 0, size);

		return b;
	}

	/**
	 * 由Vector转换成二维数组 参数 Vector,数组列数
	 */
	public static String[][] vector2Ary(Vector<String[]> v, int clos) {
		int int_loop = 0;
		String tmpAry[][] = {};
		if (v.size() == 0)
			return tmpAry;
		String str_return[][] = new String[v.size()][clos];
		while (true) {
			try {
				str_return[int_loop] = (String[]) v.elementAt(int_loop);
			} catch (ArrayIndexOutOfBoundsException e) {
				break;
			}
			int_loop++;
		}
		return str_return;
	}

	/**
	 * 把字符串数组转成JS数据数组 参数:数据数组,标题名数组,元素名
	 */
	public static String ary2JsStr(String[][] dataAry, String[] titleAry, String itemname) {
		String titleAryName = itemname + "TitleAry";
		String dataAryName = itemname + "DataAry";
		String tmpStr = "";
		StringBuffer rtnStr = new StringBuffer("var " + titleAryName + " = new Array();\r\n" + "var " + dataAryName
				+ "  = new Array();\r\n");
		int i = 0, j = 0;
		if (titleAry != null && titleAry.length > 0) {
			rtnStr.append(titleAryName + "= new Array(");
			tmpStr = "";
			for (i = 0; i < titleAry.length; i++) {
				tmpStr += "\"" + titleAry[i] + "\",";
			}
			if (tmpStr != null && tmpStr.length() > 0) {
				rtnStr.append(tmpStr.substring(0, tmpStr.length() - 1) + ");\r\n");
			}
		} else {
			// 没有元素也要返回一个空数组
			rtnStr.append(titleAryName + "[0]= new Array();\r\n");
		}

		if (dataAry != null && dataAry.length > 0) {
			for (i = 0; i < dataAry.length; i++) {
				rtnStr.append(dataAryName + "[" + i + "]= new Array(");
				tmpStr = "";
				if (dataAry[i] != null && dataAry[i].length > 0) {
					for (j = 0; j < dataAry[i].length; j++) {
						tmpStr += "\"" + dataAry[i][j] + "\",";
					}
					if (tmpStr != null && tmpStr.length() > 0) {
						rtnStr.append(tmpStr.substring(0, tmpStr.length() - 1) + ");\r\n");
					}
				}
			}
		} else {
			// 没有数据也要返回一个空数组
			rtnStr.append(dataAryName + "[0]= new Array();\r\n");
		}
		return rtnStr.toString();
	}

	public static String formatStr4SplitByDollarChar(String str) {
		try {
			String tmpStr = str;
			tmpStr = tmpStr.replaceAll("\\$\\$", "\\$_\\$");
			if ("$".equals(tmpStr.substring(tmpStr.length() - 1, tmpStr.length()))) {
				// the end of char is "$"
				tmpStr += "_";
			}
			return tmpStr;
		} catch (Exception e) {
			log.debug("Format dollar char for Split-出错" + e.toString());
			return str;
		}
	}

	// 字符集转换
	public static String gb2Iso(String gbStr) {
		String str = null;
		try {
			str = new String(gbStr.getBytes("GBK"), "8859_1");
		} catch (Exception e) {
		}
		return str;
	}

	public static String iso2Gb(String Str) {
		String str = "";
		try {
			str = new String(Str.getBytes("8859_1"), "GBK");
		} catch (Exception e) {
		}
		return str;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param src
	 *            /
	 * @param dst
	 */
	public void copyFile(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), 16 * 1024);
				out = new BufferedOutputStream(new FileOutputStream(dst), 16 * 1024);
				byte[] buffer = new byte[16 * 1024];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	// 文件删除处理
	public static void deleteFile(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						deleteFile(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
			deleteFile(filepath);// 递归调用
		} else {
			f.delete();
		}
	}

	public static void deleteFile(File f) throws IOException {
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						deleteFile(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
			deleteFile(f);// 递归调用
		} else {
			f.delete();
		}
	}

	/*
	 * 生成随机文件名
	 */
	public static synchronized String generateRandomFilename() {
		String RandomFilename = "";
		Random rand = new Random();// 生成随机数
		int random = rand.nextInt();
		Calendar calCurrent = Calendar.getInstance();
		int intHour = calCurrent.get(Calendar.HOUR_OF_DAY);
		int intDay = calCurrent.get(Calendar.DATE);
		int intMonth = calCurrent.get(Calendar.MONTH) + 1;
		int intYear = calCurrent.get(Calendar.YEAR);
		String now = String.valueOf(intYear) + "_" + String.valueOf(intMonth) + "_" + String.valueOf(intDay) + "_"
				+ String.valueOf(intHour) + "_";
		RandomFilename = now + String.valueOf(random > 0 ? random : (-1) * random);
		return RandomFilename;
	}

	public String addStrTo2000byte(String str) {
		if (str.length() > 1000 && str.length() < 2001) {
			StringBuffer sbf = new StringBuffer();
			for (int i = 0; i < ((2001 - str.length()) / 100); i++) {
				sbf
						.append("                                                                                                    ");
			}
			sbf.append("                                                                                                    ");
			str += sbf.toString();
		} else if (str.length() <= 0) {
			str = "&nbsp;";
		}
		return str;
	}

	/**
	 * 获取浏览器版本信息
	 * @Title: getBrowserName
	 * @data:2015-1-12下午05:08:49
	 * @author:wolf
	 *
	 * @param agent
	 * @return
	 */
	public static String getBrowserName(String agent) {
		if(agent.indexOf("msie 7")>0){
			return "ie7";
		}else if(agent.indexOf("msie 8")>0){
			return "ie8";
		}else if(agent.indexOf("msie 9")>0){
			return "ie9";
		}else if(agent.indexOf("msie 10")>0){
			return "ie10";
		}else if(agent.indexOf("msie")>0){
			return "ie";
		}else if(agent.indexOf("opera")>0){
			return "opera";
		}else if(agent.indexOf("opera")>0){
			return "opera";
		}else if(agent.indexOf("firefox")>0){
			return "firefox";
		}else if(agent.indexOf("webkit")>0){
			return "webkit";
		}else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
			return "ie11";
		}else{
			return "Others";
		}
	}
}
