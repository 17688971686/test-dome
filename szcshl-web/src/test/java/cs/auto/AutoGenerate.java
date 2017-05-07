package cs.auto;

import cs.auto.core.CRUDGenerate;
import cs.auto.core.config.CRUDGanConfig;
import cs.domain.demo.MyTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 代码生成器
 * User: tzg
 * Date: 2017/5/6 16:41
 */
public class AutoGenerate {

    public static void main(String[] args) {

        CRUDGanConfig config = new CRUDGanConfig(MyTest.class, "My test");
        config.setAuthor("tzg");
        config.setOuputPath("C:\\Users\\Administrator\\Desktop\\test");
        config.setFileOverride(true);
        config.setOpen(false);
        new CRUDGenerate(config);

//        String test = "varchar(255) not NULL comment   '测试名' varchar(255) not NULLa comment   ' 测试名 '  comment   '' 测试名 '' not  NULL";
//        Pattern pattern = Pattern.compile("\\s+comment+\\s+\\'*(\\d|\\b|[\\u0391-\\uFFE5]||\\s)+\\'"),
//                notNullPattern = Pattern.compile("\\s+(?i)not+\\s+(?i)null(?!\\S)");
//        Matcher matcher = pattern.matcher(test);
//        while(matcher.find()) {
//            System.out.println("1     " + matcher.group());
//        }
//        matcher = notNullPattern.matcher(test);
//        while(matcher.find()) {
//            System.out.println("2     " + matcher.group());
//        }
    }

}