package cs.rtx.service;

import cs.common.Constant;
import cs.common.utils.PropertyUtil;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class SendRTXService {

	
	/**
	 * 定时器发送腾讯通消息
	 */
	public void timeForSendRTXMsg(){
		/*List<Map<String,Object>> urList = sendRTXDao.queryMsgForSend();
		if(urList != null && urList.size() > 0){
			try {				
				//注册IP地址和服务
				SetSvrIpPort setSvrIpPort = new SetSvrIpPort();
				if(!SetSvrIpPort.isSet()){
					//读取配置文件，获取IP地址和端口				
					URL fileUrl = this.getClass().getResource("/config.properties");
					Properties properties = new Properties();				
					properties.load(new FileInputStream(new File(fileUrl.getFile())));
					String SvrIP = properties.getProperty("RTX.IP");
					int port = Integer.parseInt(properties.getProperty("RTX.PORT"));
					setSvrIpPort.initIPPort(SvrIP, port);
					SetSvrIpPort.setSet(true);
				}
								
		    	RTXSvrApi  rtxsvrapiObj = new RTXSvrApi();    
		    	String msgTitle = "广西区地税绩效管理系统未读消息提示";
		    	if( rtxsvrapiObj.Init()){   
		    		for(Map<String,Object> m:urList){
						String recvName = m.get("NAME").toString();
						int num = Integer.parseInt(m.get("TOTOLUR").toString());
						int iRet= -1;
						if(num > 0){
							String msgInfo = "您有"+num+"条未读消息，[点击查看|http://151.12.72.50/dsjx-web/message/queryRecvMessage?status=0]";
							iRet = rtxsvrapiObj.sendNotify(recvName,msgTitle,msgInfo, "0","0");
							SendRTX slog = new SendRTX(recvName,msgTitle,msgInfo, "0","0");						
				    		if(iRet == 0){
				    			slog.setResult("1");			    			
				    		}else{
				    			slog.setResult("0");
				    		}
				    		sendRTXDao.insert(slog);
						}
					}		    		
		    	}
		    	rtxsvrapiObj.UnInit();		 */
				
				//linux下发送通知消息
				/*URL fileUrl = this.getClass().getResource("/config.properties");
				Properties properties = new Properties();				
				properties.load(new FileInputStream(new File(fileUrl.getFile())));
				String host = properties.getProperty("RTX.IP");
				int port = Integer.parseInt(properties.getProperty("RTX.PORT"));
				String dsjxHttp = properties.getProperty("RTX.DSJX.LOCAL");	//绩效网站访问地址
				
				String title = "广西区地税绩效管理系统未读消息提示";
				String sendImg = "/sendnotify.cgi?" ;  			//  RTX发送消息接口 
				StringBuffer sendMsgParams = new StringBuffer(sendImg);
				*/
	            //String host = "1.1.1.102" ;  					//  RTX服务器地址 
	            //int  port = 8012 ;  							//  RTX服务器监听端口 
	            //String[] receiverss  = {"test001"};  			//  接收人，RTX帐号 
	            //String content  = "[RTX培训|www.qq.com] " ;  		//  内容 
				//StringBuffer sendMsgParams = new StringBuffer(sendImg);
				//StringBuffer receiveUrlStr = new StringBuffer();
				//for(int i = 0;i < receiverss.length;i++) {
				//    if(receiveUrlStr.length() == 0 ) {
				//        receiveUrlStr.append(receiverss[i]);
				//    }else{
				//       receiveUrlStr.append( "," + receiverss[i]);
				//   }
				//}
				//sendMsgParams.append("&receiver=" + receiveUrlStr);
				//if(content  !=   null ){
				//   sendMsgParams.append("&msg=" +  java.net.URLEncoder.encode(content,"gbk"));
				//}
	            /*for(Map<String,Object> m:urList){
					String recvName = m.get("NAME").toString();
					sendMsgParams.append("&receiver=" +recvName);
					sendMsgParams.append("&title="+java.net.URLEncoder.encode(title,"gbk"));
					int num = Integer.parseInt(m.get("TOTOLUR").toString());
					if(num > 0){
						String msgInfo = "您有"+num+"条未读消息，[点击查看|http://"+dsjxHttp+"/dsjx-web/message/queryRecvMessage?status=0]";
						sendMsgParams.append("&msg=" +java.net.URLEncoder.encode(msgInfo,"gbk"));
						URL url = new URL("HTTP", host, port, sendMsgParams.toString());
			            HttpURLConnection httpconn  =  (HttpURLConnection) url.openConnection();
			            String ret  =  httpconn.getHeaderField( 3 );
			            
			            SendRTX slog = new SendRTX(recvName,title,msgInfo, "0","0");			
			    		if(ret != null && !"null".equals(ret)){
			    			slog.setResult("1");			    			
			    		}else{
			    			slog.setResult("0");
			    		}
			    		sendRTXDao.insert(slog);
					}
				}		           	           
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
	
	public String testSendRTXMsg(){
		try{
			PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
			String rtxHost = propertyUtil.readProperty("RTX.IP");
			int rtxPort = Integer.parseInt(propertyUtil.readProperty("RTX.PORT"));
			
			String title = "【深圳市政府投资项目评审中心项目管理信息系统】消息提醒";
			String sendImg = "/sendnotify.cgi?" ;  			//  RTX发送消息接口 
			StringBuffer sendMsgParams = new StringBuffer(sendImg);
			sendMsgParams.append("&receiver=test");
			sendMsgParams.append("&title="+java.net.URLEncoder.encode(title,"gbk"));
			String msgInfo = "腾讯通发送消息测试";
			sendMsgParams.append("&msg=" +java.net.URLEncoder.encode(msgInfo,"gbk"));
			URL url = new URL("HTTP", rtxHost, rtxPort, sendMsgParams.toString());
            HttpURLConnection httpconn  =  (HttpURLConnection) url.openConnection();
            return  httpconn.getHeaderField( 3 );
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
}
