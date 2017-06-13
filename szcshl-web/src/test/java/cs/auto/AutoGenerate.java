package cs.auto;

import cs.auto.core.CRUDGenerate;
import cs.auto.core.config.CRUDGanConfig;
import cs.auto.core.config.FileConfig;
import cs.auto.core.config.FileConst;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.project.AssistPlan;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnitUser;
import cs.domain.sys.SysConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 代码生成器
 * User: tzg
 * Date: 2017/5/6 16:41
 */
public class AutoGenerate {

    public static void main(String[] args) {

        CRUDGanConfig config = new CRUDGanConfig(SysConfig.class, "系统参数");
        config.setAuthor("ldm");
        config.setOuputPath("C:\\Users\\Administrator\\Desktop\\SysConfig");
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

       /* fileConfs.add(new FileConfig(FileConst.listHtml));
        fileConfs.add(new FileConfig(FileConst.listCtrlJs));
        fileConfs.add(new FileConfig(FileConst.listSvcJs));
        fileConfs.add(new FileConfig(FileConst.editHtml));
        fileConfs.add(new FileConfig(FileConst.editCtrJs));*/
        return fileConfs;
    }

}