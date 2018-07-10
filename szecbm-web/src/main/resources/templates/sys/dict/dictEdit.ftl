<!-- Content Header (Page header) -->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<#--<link href="${path}/libs/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
<script src="${path}/libs/zTree/js/jquery.ztree.all.min.js"></script>-->

<form id="form" name="form" class="form-horizontal" style="max-width: 600px;">
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-10" style="margin-bottom: 10px;">
            <button class="btn btn-sm btn-success" ng-click="vm.create()" ng-disabled="vm.isSubmit"
                    ng-hide="vm.isUpdate">
                <span class="glyphicon glyphicon-ok"></span>创建
            </button>
            <button class="btn btn-sm btn-success" ng-click="vm.update()" ng-disabled="vm.isSubmit"
                    ng-show="vm.isUpdate">
                <span class="glyphicon glyphicon-ok"></span>更新
            </button>
        </div>
    </div>

    <div class="form-group">
        <label for="dictId" class="col-sm-3 control-label">字典编码：<span class="text-red">*</span></label>
        <div class="col-sm-9">
            <input type="text" maxlength="200" class="form-control input-sm "
                   ng-disabled="vm.isUpdate" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
                   ng-model="vm.dict.dictId" id="dictId" name="dictId" data-val="true"
                   data-val-required="必填">
            <span data-valmsg-for="dictId" data-valmsg-replace="true" class="text-red">
                    <span class="errors" ng-show="vm.isDictCodeExist">字典编码已存在</span>
        </div>
    </div>
    <div class="form-group">
        <label for="dictName" class="col-sm-3 control-label">字典名称：<span class="text-red">*</span></label>
        <div class="col-sm-9">
            <input type="text" maxlength="200" class="form-control input-sm " ng-model="vm.dict.dictName"
                   id="dictName" name="dictName" data-val="true" data-val-required="必填">
            <span data-valmsg-for="dictName" data-valmsg-replace="true" class="text-red">
        </div>
    </div>
    <div class="form-group">
        <label for="dictKey" class="col-sm-3 control-label">字典值：<span class="text-red">*</span></label>
        <div class="col-sm-9">
            <input type="text" maxlength="200" class="form-control input-sm" ng-disabled="vm.dict.dictType=='0'"
                   ng-model="vm.dict.dictKey" id="dictKey" name="dictKey" data-val="true" data-val-range-min="0"
                   data-val-range-max="" data-val-range="不可输入负数！"
                   data-val-required="必填">
            <span data-valmsg-for="dictKey" data-valmsg-replace="true" class="text-red"></span>
        </div>
    </div>
    <div class="form-group" ng-init="vm.dict.dictState = '1'">
        <label for="dictState1" class="col-sm-3 control-label">是否启用：<span class="text-red">*</span></label>
        <div class="col-sm-9">
            <label class="radio-inline">
                <input type="radio" ng-model="vm.dict.dictState" id="dictState1" name="dictState" value="1"> 是
            </label>
            <label class="radio-inline">
                <input type="radio" ng-model="vm.dict.dictState" id="dictState0" name="dictState" value="0"> 否
            </label>
        </div>
    </div>
    <div class="form-group">
        <label for="dictKey" class="col-sm-3 control-label">父字典：<span class="text-red">*</span></label>
        <div class="col-sm-9">
            <ul id="pzTree" class="ztree" ng-if="!vm.isUpdate"></ul>
            <input type="text" class="form-control input-sm " disabled ng-if="vm.isUpdate"
                   ng-model="vm.dict.parentId">
        </div>
    </div>
    <div class="form-group">
        <label for="remark" class="col-sm-3 control-label">备注：</label>
        <div class="col-sm-9">
                    <textarea class="form-control" id="remark" name="remark" ng-model="vm.dict.remark" cols="30"
                              rows="3"></textarea>
        </div>
    </div>
    <div class="form-group">
        <label for="itemOrder" class="col-sm-3 control-label">序号：</label>
        <div class="col-sm-9">
            <input type="number" maxlength="200" class="form-control input-sm " ng-model="vm.dict.itemOrder"
                   id="itemOrder" name="itemOrder"/>
        </div>
    </div>

</form>