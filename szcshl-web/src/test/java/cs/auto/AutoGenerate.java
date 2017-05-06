package cs.auto;

import cs.auto.core.Generate;
import cs.domain.demo.MyTest;

/**
 * Description: 代码生成器
 * User: tzg
 * Date: 2017/5/6 16:41
 */
public class AutoGenerate {

    public static void main(String[] args) {

        new Generate(MyTest.class, "C:\\Users\\Administrator\\Desktop\\test");

    }

}