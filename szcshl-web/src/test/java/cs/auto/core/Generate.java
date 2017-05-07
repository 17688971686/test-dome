package cs.auto.core;

import cs.auto.core.config.GanerateConfig;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

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
    public Generate(GanerateConfig conf) {
        super(conf);
    }

    @Override
    public void execute() {
        // 生成配置信息
        gconf.generateParams();
        logger.info("<<=====================开始生成代码===============================");
        // 创建DTO类
        createFile("Dto.ftl", getOutputPath(gconf.getDtoPackage(), gconf.getDtoClsName()));
        // 创建repo的接口和实现类
        createFile("Repository.ftl", getOutputPath(gconf.getRepoPackage(), gconf.getRepoClsName()));
        createFile("RepositoryImpl.ftl", getOutputPath(gconf.getRepoImplPackage(), gconf.getRepoImplClsName()));
        // 创建service的接口和实现类
        createFile("IService.ftl", getOutputPath(gconf.getServicePackage(), gconf.getServiceClsName()));
        createFile("ServiceImpl.ftl", getOutputPath(gconf.getServiceImplPackage(), gconf.getServiceImplClsName()));
        // 创建controller类
        createFile("Controller.ftl", getOutputPath(gconf.getControllerPackage(), gconf.getControllerClsName()));

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

    protected String getOutputPath(String pck, String clsName) {
        return gconf.getOuputPath().concat(File.separator).concat(pck.replace(".", File.separator)).concat(File.separator).concat(String.format(clsName, gconf.getBeanName()));
    }


}