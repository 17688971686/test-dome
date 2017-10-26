package cs.ahelper;

import cs.common.utils.Validate;
import cs.domain.sys.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by ldm on 2017/10/26.
 */
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private final int hashIterations = 2;

    public User encryptPassword(User user) {
        if(!Validate.isString(user.getUserSalt())){
            user.setUserSalt(randomNumberGenerator.nextBytes().toHex());
        }
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes(user.getUserSalt()), hashIterations).toHex();
        user.setPassword(newPassword);
        return user;
    }
}
