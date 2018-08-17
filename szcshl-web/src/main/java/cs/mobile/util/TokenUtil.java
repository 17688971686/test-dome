package cs.mobile.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * taoken 工具类
 * Created by ldm on 2018/5/20.
 */
public class TokenUtil {

    private static final String JWT_KEY = "jwtKey";

    /**
     * 系统用户token名称
     */
    public static final String SYS_TOKEN_KEY = "sysToken";

    /**
     * 不用拦截的URL
     */
    public static final ArrayList<String> PASS_URL = new ArrayList<String>(Arrays.asList("/api/login/signin"));
    /**
     * 生成用户token
     * @param account
     * @return
     */
    public static String getUserToken(String account) {
        return Jwts.builder()
                .setSubject(account)
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .compact();
    }

}
