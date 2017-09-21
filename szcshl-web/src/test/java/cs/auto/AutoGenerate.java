package cs.auto;

import cs.auto.core.CRUDGenerate;
import cs.auto.core.config.CRUDGanConfig;
import cs.auto.core.config.FileConfig;
import cs.auto.core.config.FileConst;
import cs.domain.asserts.userAssertDetail.UserAssertDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 代码生成器
 * User: tzg
 * Date: 2017/5/6 16:41
 */
public class AutoGenerate {

    public static void main(String[] args) {

        CRUDGanConfig config = new CRUDGanConfig(UserAssertDetail.class, "用户资产明细");
        config.setAuthor("zsl");
        config.setOuputPath("C:\\Users\\Administrator\\Desktop\\userAssertDetail");
        config.setFileOverride(true);
        config.setOpen(true);
        config.setFileConfs(getFileConf());
        new CRUDGenerate(config);

    }

    /**
     * 创建文件配置信息
     * @return
     */
    public static List<FileConfig> getFileConf() {
       List<FileConfig> fileConfs = new ArrayList<FileConfig>();
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
        return fileConfs;
    }

}