package cs.auto.core;

import cs.auto.core.config.FileConfig;
import cs.auto.core.config.GanConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * Description: 代码生成器基础配置
 * User: tzg
 * Date: 2017/5/6 16:43
 */
public class Generate extends AbstractGenerate {


    /**
     * 根据配置生成代码
     * @param conf
     */
    public Generate(GanConfig conf) {
        super(conf);
    }

    public void execute() {
        gconf.generateParams();
        logger.info("<<=====================开始生成代码===============================");
        List<FileConfig> fileConfs = gconf.getFileConfs();
        for (FileConfig fc : fileConfs) {
            createFile(fc.getTemplatePath(), fc.getOutputPath());
        }
        logger.info("<<=====================代码已成功生成===============================");

        if (gconf.isOpen()) {
            // 打开输出目录
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + gconf.getOuputPath());
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + gconf.getOuputPath());
                    } else {
                        logger.debug("文件输出目录:" + gconf.getOuputPath());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}