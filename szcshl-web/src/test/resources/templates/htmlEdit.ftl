<${info.well_}assign path=request.contextPath/>
<meta charset="UTF-8">

<div ng-include src="'${info.dollar_}{path}/contents/app/common/dialog-alert.tmpl.html'"></div>

<div class="well well-sm" style="background:white;">
    <ol class="breadcrumb">
        <li><a href="#/${info.beanName?uncap_first}List">${info.comment!''}</a></li>
        <li class="active">{{vm.title}}</li>
    </ol>
    <form id="form" name="form">
        <div class="toolbar">
            <a class="btn btn-sm btn-primary" href="#/${info.beanName?uncap_first}"><span class="glyphicon glyphicon-chevron-left"></span>返回</a>
            <button class="btn btn-sm btn-success" ng-click="vm.create()" ng-disabled="vm.isSubmit"
                    ng-hide="vm.isUpdate"><span class="glyphicon glyphicon-ok"></span>创建
            </button>
            <button class="btn btn-sm btn-success" ng-click="vm.update()" ng-disabled="vm.isSubmit"
                    ng-show="vm.isUpdate"><span class="glyphicon glyphicon-ok"></span>更新
            </button>
        </div>
        <table style="width:100%">
            <tr>
                <td class="formAlignRight" style="width:150px;"></td>
                <td style="width:400px;"><span class="errors">{{vm.message}}</span></td>
                <td></td>
            </tr>
<#list info.fields as f><#if f.isId=false>
            <tr>
                <td class="formAlignRight"><#if f.comment??>${f.comment}<#else>${f.name!''}</#if>：<#if f.required=true><span class="required">*</span></#if></td>
                <td>
                    <input type="text" maxlength="200" class="form-control input-sm " ng-model="vm.model.${f.name!''}"
                           id="${f.name!''}" name="${f.name!''}" <#if f.required=true>data-val="true" data-val-required="必填"</#if> />
                </td>
                <td><span data-valmsg-for="${f.name!''}" data-valmsg-replace="true" class="errors"></span></td>
            </tr>
</#if></#list>
        </table>
    </form>
</div>