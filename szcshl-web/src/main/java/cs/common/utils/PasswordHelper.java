package cs.common.utils;

import cs.domain.sys.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by ldm on 2018/1/20.
 */
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private final int hashIterations = 2;

    public void encryptPassword(User user) {
        // User对象包含最基本的字段Username和Password
        user.setUserSalt(randomNumberGenerator.nextBytes().toHex());
        // 将用户的注册密码经过散列算法替换成一个不可逆的新密码保存进数据，散列过程使用了盐
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes(user.getUserSalt()), hashIterations).toHex();
        user.setPassword(newPassword);
    }

    public static void main(String[] args){
        PasswordHelper passwordHelper = new PasswordHelper();
        String  salt = passwordHelper.randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(passwordHelper.algorithmName, "1",
                ByteSource.Util.bytes(salt), passwordHelper.hashIterations).toHex();
        System.out.print(newPassword);
    }
}
