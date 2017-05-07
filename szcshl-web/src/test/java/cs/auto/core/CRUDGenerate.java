package cs.auto.core;

import cs.auto.core.config.FileConfig;
import cs.auto.core.config.CRUDGanConfig;

import java.io.*;
import java.util.List;

/**
 * Description: 代码生成器基础配置(基本增删改查)
 * User: tzg
 * Date: 2017/5/6 16:43
 */
public class CRUDGenerate extends AbstractGenerate {


    /**
     * 根据配置生成代码
     * @param conf
     */
    public CRUDGenerate(CRUDGanConfig conf) {
        super(conf);
    }

    public void execute() {
        gconf.autoGenerate();

        logger.info("<<=====================开始生成代码===============================");
        List<FileConfig> fileConfs = gconf.getFileConfs();
        for (FileConfig fc : fileConfs) {
            createFile(fc);
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