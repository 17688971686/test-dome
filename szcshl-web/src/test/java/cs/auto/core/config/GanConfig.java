package cs.auto.core.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器配置类
 *
 * @author tzg
 * @date 2017/5/7 14:10
 */
public class GanConfig extends AbstractGanConfig {


    protected Class cls;

    /**
     * 实例化代码生成器的配置类
     *
     * @param cls 数据映射实体类
     */
    public GanConfig(Class cls) {
        this.cls = cls;
    }

    /**
     * 实例化代码生成器的配置类
     *
     * @param cls     数据映射实体类
     * @param comment 注解
     */
    public GanConfig(Class cls, String comment) {
        this.cls = cls;
        this.comment = comment;
    }

    /**
     * 实例化代码生成器的配置类
     *
     * @param cls       数据映射实体类
     * @param comment   注解
     * @param ouputPath 输出路径
     */
    public GanConfig(Class cls, String comment, String ouputPath) {
        this.cls = cls;
        this.comment = comment;
        this.ouputPath = ouputPath;
    }

    @Override
    public void autoGenerate() {
        fileConfs.add(new FileConfig(FileConst.dtoCls));
        fileConfs.add(new FileConfig(FileConst.repoCls));
        fileConfs.add(new FileConfig(FileConst.repoImplCls));
        fileConfs.add(new FileConfig(FileConst.serviceCls));
        fileConfs.add(new FileConfig(FileConst.serviceImplCls));
        fileConfs.add(new FileConfig(FileConst.controllerCls));

        fileConfs.add(new FileConfig(FileConst.listHtml));
        fileConfs.add(new FileConfig(FileConst.listCtrlJs));
        fileConfs.add(new FileConfig(FileConst.listSvcJs));
        fileConfs.add(new FileConfig(FileConst.editHtml));
        fileConfs.add(new FileConfig(FileConst.editCtrJs));

        generateParams();
    }

    protected String formatStr = "cs.%s";
    protected String module = null;

    /**
     * 生成代码生成器的配置信息
     */
    public void generateParams() {
        logger.info("<<=====================开始生成代码生成器的配置信息==============================");
        paramMap.put("well_", "#");
        paramMap.put("dollar_", "$");
        paramMap.put("at_", "@");

        String beanPackage = cls.getPackage().getName();
        beanName = cls.getSimpleName();
        paramMap.put("beanPackage", beanPackage);
        paramMap.put("beanName", beanName);

        List<FieldConfig> clsFields = new ArrayList<FieldConfig>();
        FieldConfig cf;
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            cf = new FieldConfig();
            cf.setName(f.getName());
            cf.setType(f.getType().getSimpleName());
            if(f.isAnnotationPresent(Id.class)) {
                cf.setIsId(true);
            }
            clsFields.add(cf);
        }
        paramMap.put("fields", clsFields);

        paramMap.put("comment", comment);
        paramMap.put("author", author);

        if (beanPackage.indexOf(PackageConst.PACKAGE_DOMAIN) > -1) {
            paramMap.put("module", beanPackage.substring(beanPackage.indexOf(PackageConst.PACKAGE_DOMAIN) + 1));
            formatStr = beanPackage.replace(PackageConst.PACKAGE_DOMAIN, "%s");
        }

        for (FileConfig fc : fileConfs) {
            fc.setFileName(String.format(fc.getFileName(), beanName));
            paramMap.put(fc.getSuffix(), fc.getFileName());
            if (StringUtils.isNoneBlank(fc.getLayer())) {
                fc.setLayer(String.format(formatStr, fc.getLayer()));
                paramMap.put(fc.getSuffix().concat("Layer"), fc.getLayer());
            }
            fc.setOutputPath(getOutputPath(fc.getLayer(), fc.getFileName(), fc.getFileType()));
            fc.setInfo(paramMap);
        }

        logger.debug(JSON.toJSONString(paramMap));

        logger.info("<<=====================结束生成代码生成器的配置信息==============================");
    }


    /**
     * 获取输出文件路径
     *
     * @param layer    层名/包名
     * @param fileName 文件名
     * @param suffix   文件后缀
     * @return
     */
    protected String getOutputPath(String layer, String fileName, String suffix) {
        String path = ouputPath.concat(File.separator);
        if (StringUtils.isNoneBlank(layer)) {
            path = path.concat(layer.replace(".", File.separator)).concat(File.separator);
        } else if (StringUtils.isNoneBlank(module)) {
            path = path.concat(module).concat(File.separator);
        }
        return path.concat(fileName).concat(suffix);
    }

    protected String comment = "";
    /**
     * 作者名
     */
    protected String author = System.getProperty("user.name");


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
