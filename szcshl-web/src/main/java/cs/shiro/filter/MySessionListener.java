package cs.shiro.filter;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.Date;

/**
 * Created by shenning on 2017/10/26.
 */
public class MySessionListener implements SessionListener {

    //会话创建时触发
    @Override
    public void onStart(Session session) {
        System.out.println("会话创建：" + session.getId()+"》》时间："+session.getLastAccessTime());
    }
    //会话过期时触发
    @Override
    public void onStop(Session session) {
        System.out.println("会话过期：" + session.getId()+"》》时间："+new Date());
    }
    //退出/会话过期时触发
    @Override
    public void onExpiration(Session session) {
        System.out.println("会话停止：" + session.getId()+"》》时间："+session.getLastAccessTime());
    }
}
