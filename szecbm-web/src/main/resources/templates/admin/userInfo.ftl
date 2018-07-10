<!-- Content Header (Page header) -->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<section class="content-header">
    <h1>
        【${currentUser.username}】个人信息
    </h1>
    <ol class="breadcrumb">
        <li><a href="#/"><i class="fa fa-home"></i> 系统主页</a></li>
        <li class="active">个人信息</li>
    </ol>
</section>

<section class="content">
    <div class="well well-sm" style="background:white;">
        <form id="userInfoForm" class="form-horizontal" style="max-width: 600px;"
              ng-init="vm.model = {userId: '${currentUser.userId!''}', username: '${currentUser.username!''}', displayName: '${currentUser.displayName!''}', mobileNumber: '${currentUser.mobileNumber!''}'}">
            <div class="form-group">
                <label for="username" class="col-sm-3 control-label">用户名：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="username" name="username" ng-model="vm.model.username" disabled>
                </div>
            </div>
            <div class="form-group">
                <label for="displayName" class="col-sm-3 control-label">用户姓名：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="displayName" name="displayName" ng-model="vm.model.displayName" data-val="true" data-val-required="必填">
                    <span data-valmsg-for="username" data-valmsg-replace="true" class="errors"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="oldPassword" class="col-sm-3 control-label">原密码：</label>
                <div class="col-sm-9">
                    <input type="password" class="form-control" id="oldPassword" name="oldPassword" ng-model="vm.model.oldPassword">
                </div>
            </div>
            <div class="form-group">
                <label for="newPassword" class="col-sm-3 control-label">新密码：</label>
                <div class="col-sm-9">
                    <input type="password" class="form-control" id="newPassword" name="newPassword" ng-model="vm.model.newPassword">
                </div>
            </div>
            <div class="form-group">
                <label for="verifyPassword" class="col-sm-3 control-label">新密码确认：</label>
                <div class="col-sm-9">
                    <input type="password" class="form-control" id="verifyPassword" name="verifyPassword" ng-model="vm.model.verifyPassword">
                </div>
            </div>
            <div class="form-group">
                <label for="mobileNumber" class="col-sm-3 control-label">手机号码：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="mobileNumber" name="mobileNumber" ng-model="vm.model.mobileNumber">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <button type="button" class="btn btn-default" ng-click="vm.toUpdate()" ng-disabled="vm.isSubmit">修改</button>
                </div>
            </div>
        </form>
    </div>
</section>