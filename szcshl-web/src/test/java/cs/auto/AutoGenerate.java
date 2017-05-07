package cs.auto;

import cs.auto.core.Generate;
import cs.auto.core.config.GanerateConfig;
import cs.domain.demo.MyTest;

/**
 * Description: 代码生成器
 * User: tzg
 * Date: 2017/5/6 16:41
 */
public class AutoGenerate {

    public static void main(String[] args) {

        GanerateConfig config = new GanerateConfig(MyTest.class, "My test");
        config.setAuthor("tzg");
        config.setOuputPath("C:\\Users\\Administrator\\Desktop\\test");
        config.setFileOverride(true);
        config.setOpen(false);
        new Generate(config);

    }

}