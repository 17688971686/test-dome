package cs.freemarker;

import cs.common.utils.Validate;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RowspanDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        int rowspan = 1;
        try {
            SimpleSequence items;
            String keyName;
            String preKeyName = null;
            int index = 0;
            if(params.get("items") != null) {
                items = (SimpleSequence) params.get("items");
            } else {
                throw new TemplateModelException("items参数为空!");
            }
            if(params.get("keyName") != null) {
                keyName = ((SimpleScalar) params.get("keyName")).getAsString();
            } else {
                throw new TemplateModelException("keyName参数为空!");
            }
            if(params.get("index") != null) {
                index = ((SimpleNumber) params.get("index")).getAsNumber().intValue();
            } else {
                throw new TemplateModelException("index参数为空!");
            }
            if(params.get("preKeyName") != null) {
                preKeyName = ((SimpleScalar) params.get("preKeyName")).getAsString();
            }
            if(items != null) {
                TemplateModel cur = items.get(index);
                String cur_k = FMUtils.getAsString(cur, keyName), pre_k;
                if(Validate.isString(cur_k)) {
                    TemplateModel pre = index > 0 ? items.get(index - 1) : null;
                    pre_k = FMUtils.getAsString(pre, keyName);
                    if(Validate.isEmpty(pre_k) || !pre_k.equals(cur_k)) {
                        String cs = FMUtils.getAsString(cur, preKeyName), it_s;
                        TemplateModel it;
                        for(int i = index + 1; i < items.size(); i++) {
                            it = items.get(i);
                            if(Validate.isString(preKeyName)) {
                                String ss1 = FMUtils.getAsString(it, preKeyName);
                                if(Validate.isString(ss1) && Validate.isString(cs) && !ss1.equals(cs)) {
                                    break;
                                } else if(Validate.isEmpty(ss1) && Validate.isString(cs)) {
                                    break;
                                }
                            }
                            it_s = FMUtils.getAsString(it, keyName);
                            if(Validate.isString(it_s) && it_s.equals(cur_k)) {
                                rowspan++;
                            } else {
                                break;
                            }
                        }
                    } else {
                        if(Validate.isString(preKeyName)) {
                            String cs = FMUtils.getAsString(cur, preKeyName);
                            String ps = FMUtils.getAsString(pre, preKeyName);
                            if(Validate.isString(ps) && Validate.isString(cs) && ps.equals(cs)) {
                                return;
                            } else if(Validate.isEmpty(ps) && Validate.isEmpty(cs) && cur_k.equals(pre_k)) {
                                return;
                            } else if(Validate.isEmpty(ps) && Validate.isString(cs)) {
                                return;
                            } else {
                                TemplateModel it;
                                String ss1, it_s;
                                for(int i = index + 1; i < items.size(); i++) {
                                    it = items.get(i);
                                    ss1 = FMUtils.getAsString(it, preKeyName);
                                    if(Validate.isString(ss1) && Validate.isString(cs) && !ss1.equals(cs)) {
                                        break;
                                    } else if(Validate.isEmpty(ss1) && Validate.isString(cs)) {
                                        break;
                                    }
                                    it_s = FMUtils.getAsString(it, keyName);
                                    if(Validate.isString(it_s) && it_s.equals(cur_k)) {
                                        rowspan++;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(loopVars.length > 0) {
            loopVars[0] = new SimpleNumber(rowspan);
        } else {
            throw new TemplateModelException("缺少rowspan变量!");
        }
        body.render(env.getOut());
    }
}
