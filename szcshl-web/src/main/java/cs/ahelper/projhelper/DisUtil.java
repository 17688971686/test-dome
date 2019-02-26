package cs.ahelper.projhelper;

import cs.common.constants.Constant;
import cs.common.utils.Validate;
import cs.domain.project.DispatchDoc;

/**
 * @author ldm on 2018/5/6 0006.
 * 发文工具类
 */
public class DisUtil {

    private DispatchDoc dispatchDoc;

    protected DisUtil() {

    }
    protected DisUtil(DispatchDoc dispatchDoc) {
        this.dispatchDoc = dispatchDoc;
    }

    public static DisUtil create() {
        return new DisUtil();
    }
    public static DisUtil create(DispatchDoc dispatchDoc) {
        return new DisUtil(dispatchDoc);
    }

    /**
     * 重置第一负责人意见
     * @return
     */
    public DisUtil resetMainUserOption(){
        dispatchDoc.setMianChargeSuggest("");
        return this;
    }

    /**
     * 重置第二负责人意见
     * @return
     */
    public DisUtil resetSecondUserOption(){
        dispatchDoc.setSecondChargeSuggest("");
        return this;
    }

    /**
     * 重置主办部长意见
     * @return
     */
    public DisUtil resetMinisterOption(){
        dispatchDoc.setMinisterSuggesttion("");
        dispatchDoc.setMinisterDate(null);
        dispatchDoc.setMinisterName("");
        return this;
    }

    /**
     * 重置分管主任意见
     * @return
     */
    public DisUtil resetViceDirectorOption(){
        dispatchDoc.setViceDirectorSuggesttion("");
        dispatchDoc.setViceDirectorDate(null);
        dispatchDoc.setViceDirectorName("");
        return this;
    }

    /**
     * 重置主任意见
     * @return
     */
    public DisUtil resetDirectorOption(){
        dispatchDoc.setDirectorSuggesttion("");
        dispatchDoc.setDirectorDate(null);
        dispatchDoc.setDirectorName("");
        return this;
    }

    /**
     * 是否是合并发文
     * @return
     */
    public boolean isMergeDis(){
        String disWay = dispatchDoc.getDispatchWay();
        if (Validate.isString(disWay) && Constant.MergeType.DIS_MERGE.getValue().equals(disWay)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是主项目
     * @return
     */
    public boolean isMainProj(){
        String isMainProj = dispatchDoc.getIsMainProject();
        if (Validate.isString(isMainProj) && Constant.EnumState.YES.getValue().equals(isMainProj)) {
            return true;
        }
        return false;
    }

    /**
     * 重置发文的审批意见
     * 清空部长或者分管领导意见，避免回退的时候，意见重叠
     * @param dp
     */
    public void resetDisReviewOption(DispatchDoc dp) {
        dp.setSecondChargeSuggest("");
        dp.setMinisterSuggesttion("");
        dp.setMinisterDate(null);
        dp.setMinisterName("");
        dp.setViceDirectorSuggesttion("");
        dp.setViceDirectorDate(null);
        dp.setViceDirectorName("");
        dp.setDirectorSuggesttion("");
        dp.setDirectorDate(null);
        dp.setDirectorName("");
    }

    public DispatchDoc getDispatchDoc() {
        return dispatchDoc;
    }

    public void setDispatchDoc(DispatchDoc dispatchDoc) {
        this.dispatchDoc = dispatchDoc;
    }
}
