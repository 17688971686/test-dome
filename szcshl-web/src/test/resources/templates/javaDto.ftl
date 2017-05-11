<#if layer??>package ${layer};</#if>

import ${info.beanPackage}.${info.beanName};
import cs.model.BaseDto2;
<#list info.fields as f>
    <#if f.type='Date'>import java.util.Date;</#if>
</#list>


/**
 * Description: ${info.comment!''} 页面数据模型
 * author: ${info.author!''}
 * Date: ${.now}
 */
public class ${info.beanName}Dto extends BaseDto {

<#list info.fields as f>
    private ${f.type} ${f.name};
</#list>

    public ${info.beanName}Dto() {
    }
   
	<#list info.fields as f>
    public ${f.type} get${f.name?cap_first}() {
        return ${f.name};
    }

    public void set${f.name?cap_first}(${f.type} ${f.name}) {
        this.${f.name} = ${f.name};
    }
	</#list>

}