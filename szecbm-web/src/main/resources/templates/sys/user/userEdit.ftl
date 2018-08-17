<!-- Content Header (Page header) -->
<#--<script src="libs/zTree/js/jquery.ztree.all.min.js"></script>-->
<section class="content-header">
    <h1>
        更新用户
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统主页</a></li>
        <li class="active">更新用户</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <form id="form" name="form" class="form-horizontal"  style="max-width: 600px;">
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <a class="btn btn-sm btn-primary" href="#/user">
                        <span class="glyphicon glyphicon-chevron-left"></span> 返回
                    </a>
                    <button class="btn btn-sm btn-success" ng-click="vm.save()" ng-disabled="vm.isSubmit">
                        <span class="glyphicon glyphicon-ok"></span> 保存
                    </button>
                </div>
            </div>
            <div class="form-group">
                <label for="username" class="col-sm-3 control-label">登录名：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control" ng-disabled="vm.isUpdate"
                           ng-model="vm.model.username" id="username" name="username" data-val="true"
                           data-val-required="必填">
                    <span data-valmsg-for="username" data-valmsg-replace="true" class="errors"></span>
                    <span class="errors" ng-show="vm.isUserExist">用户已存在</span>
                </div>
            </div>
            <div class="form-group">
                <label for="displayName" class="col-sm-3 control-label">用户姓名：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control" ng-disabled="vm.isUpdate"
                           ng-model="vm.model.displayName" id="displayName" name="displayName" data-val="true"
                           data-val-required="必填">
                    <span data-valmsg-for="displayName" data-valmsg-replace="true" class="errors"></span>
                </div>
            </div>
            <div class="form-group" ng-if="!vm.isUpdate">
                <label for="password" class="col-sm-3 control-label">密码：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="password" maxlength="200" class="form-control"
                           ng-model="vm.model.password" id="password" name="password" data-val="true"
                           data-val-required="必填">
                    <span data-valmsg-for="password" data-valmsg-replace="true" class="errors"></span>
                </div>
            </div>
            <div class="form-group" ng-if="!vm.isUpdate">
                <label for="verifyPassword" class="col-sm-3 control-label">确认密码：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="password" maxlength="200" class="form-control" ng-model="vm.model.verifyPassword"
                           id="verifyPassword" name="verifyPassword" data-val="true" data-val-required="必填"
                           data-val-equalto="两次密码不一致" data-val-equalto-other="password"/>
                    <span data-valmsg-for="verifyPassword" data-valmsg-replace="true" class="errors"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="useState1" class="col-sm-3 control-label">用户状态：</label>
                <div class="col-sm-9">
                    <input type="radio" value="1" name="useState" id="useState1"
                           ng-model="vm.model.useState" ng-checked="vm.model.useState==1"><label>启用</label>
                    <input type="radio" value="0" name="useState" id="useState0"
                           ng-model="vm.model.useState" ng-checked="vm.model.useState==0"><label>禁用</label>
                </div>
            </div>
            <div class="form-group">
                <label for="remark" class="col-sm-3 control-label">备注：</label>
                <div class="col-sm-9">
                <textarea class="form-control" maxlength="200" name="remark" id="remark" ng-model="vm.model.remark"
                          style="height:120px;"></textarea>
                </div>
            </div>
        </form>
    </div>
</section>