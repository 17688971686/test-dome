<meta charset="UTF-8">

<section class="content-header">
    <h1>
        系统变量编辑表单
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li><a href="#/sysVariable"> 系统变量管理</a></li>
        <li class="active"> 系统变量编辑表单</li>
    </ol>
</section>

<section class="content">
    <div class="well well-sm" style="background:white;">
        <form id="form" name="form" class="form-horizontal" style="max-width: 600px;">
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-10" style="margin-bottom: 10px;">
                    <button class="btn btn-sm btn-primary" ng-click="backPrevPage('sysVariable')">
                        <span class="glyphicon glyphicon-chevron-left"></span> 返回
                    </button>
                    <button class="btn btn-sm btn-success" ng-click="vm.save()" ng-disabled="vm.isSubmit">
                        <span class="glyphicon glyphicon-ok"></span> 保存
                    </button>
                </div>
            </div>
            <div class="form-group">
                <label for="varCode" class="col-sm-3 control-label">系统变量编码：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control input-sm" ng-model="vm.model.varCode"
                           ng-disabled="vm.varId"
                           id="varCode" name="varCode" data-val="true" data-val-required="必填">
                    <span data-valmsg-for="varCode" data-valmsg-replace="true" class="text-red"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="varName" class="col-sm-3 control-label">系统变量名称：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control input-sm" ng-model="vm.model.varName"
                           id="varName" name="varName" data-val="true" data-val-required="必填">
                    <span data-valmsg-for="varName" data-valmsg-replace="true" class="text-red"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="varType" class="col-sm-3 control-label">系统变量类型：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <select type="text" maxlength="200" class="form-control input-sm" ng-model="vm.model.varType"
                            id="varType" name="varType" data-val="true" data-val-required="必填">
                    <#list varTypes as t>
                        <option value="${t.name()}">${t.getTypeName()}</option>
                    </#list>
                    </select>
                    <span data-valmsg-for="displayName" data-valmsg-replace="true" class="text-red"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="varValue" class="col-sm-3 control-label">系统变量值：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control input-sm" ng-model="vm.model.varValue"
                           id="varValue" name="varValue" data-val="true" data-val-required="必填">
                    <span data-valmsg-for="varValue" data-valmsg-replace="true" class="text-red"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="remark" class="col-sm-3 control-label">备注：</label>
                <div class="col-sm-9">
                    <textarea class="form-control" maxlength="200" name="remark" id="remark"
                              ng-model="vm.model.remark" style="height:120px;"></textarea>
                </div>
            </div>
        </form>
    </div>
</section>