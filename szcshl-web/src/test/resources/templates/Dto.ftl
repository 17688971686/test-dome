<#if dtoPackage??>package ${dtoPackage};</#if>

import ${beanPackage}.${beanName};
import cs.model.BaseDto2;

/**
* Description: 页面数据模型
* User: tzg
* Date: 2017/5/4 17:54
*/
public class ${beanName}Dto extends BaseDto2<${beanName}> {

    private String id;
    private String testName;
    private String test01;
    private String test02;
<#list fields as f>
    private ${f.type} ${f.name};
</#list>

    public ${beanName}Dto() {
    }

    public ${beanName}Dto(${beanName} source) {
        super(source);
    }

    @Override
    protected Class<${beanName}> getCls() {
        return ${beanName}.class;
    }

<#list fields as f>
    public ${f.type} get${f.name?cap_first}() {
        return ${f.name};
    }

    public void set${f.name?cap_first}(${f.type} ${f.name}) {
        this.${f.name} = ${f.name};
    }
</#list>

}