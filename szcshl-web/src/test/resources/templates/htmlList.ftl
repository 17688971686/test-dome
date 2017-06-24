<${info.well_}assign path=request.contextPath/>
<meta charset="UTF-8">
<div ng-include src="'${info.dollar_}{path}/contents/app/common/dialog-alert.tmpl.html'"></div>
<div ng-include src="'${info.dollar_}{path}/contents/app/common/dialog-confirm.tmpl.html'"></div>

<div class="well well-sm" style="background:white;">
    <ol class="breadcrumb">
        <li><a href="#/${info.beanName?uncap_first}">${info.comment!''}</a></li>
        <li class="active">{{vm.title}}</li>
    </ol>
    <form id="form" name="form">
        <div class="toolbar">
        <${info.at_}shiro.hasPermission name="${info.beanName?uncap_first}##post">
            <a class="btn btn-sm btn-primary" href="#/${info.beanName?uncap_first}Edit/"><span class="glyphicon glyphicon-plus"></span>新增用户</a>
        </${info.at_}shiro.hasPermission>
        <${info.at_}shiro.hasPermission name="${info.beanName?uncap_first}##delete">
            <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit"><span class="glyphicon glyphicon-remove"></span>批量删除</button>
        </${info.at_}shiro.hasPermission>
        </div>
        <script type="text/template" id="columnBtns">
            <${info.at_}shiro.hasPermission name="${info.beanName?uncap_first}##delete">
            <button class="btn btn-xs btn-danger"  ng-click="{0}" ><span class="glyphicon glyphicon-remove"></span>删除</button>
            </${info.at_}shiro.hasPermission>
            <${info.at_}shiro.hasPermission name="${info.beanName?uncap_first}##put">
            <a class="btn btn-xs btn-primary" href="#/${info.beanName?uncap_first}Edit/{1}"><span class="glyphicon glyphicon-pencil"></span>编辑</a>
            </${info.at_}shiro.hasPermission>
        </script>

        <div class="grid" kendo-grid="grid" k-options="vm.gridOptions"></div>
    </form>
</div>

