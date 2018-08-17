<meta charset="UTF-8">

<section class="content-header">
    <h1>
        添加角色
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统主页</a></li>
        <li class="active">添加角色</li>
    </ol>
</section>

<section class="content">
    <div class="well well-sm" style="background:white;">
        <form id="form" name="form" class="form-horizontal" style="max-width: 600px;">
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-10" style="margin-bottom: 10px;">
                    <button class="btn btn-sm btn-primary" ui-sref="role">
                        <span class="glyphicon glyphicon-chevron-left"></span> 返回
                    </button>
                    <button class="btn btn-sm btn-success" ng-click="vm.save()" ng-disabled="vm.isSubmit">
                        <span class="glyphicon glyphicon-ok"></span> 保存
                    </button>
                </div>
            </div>
            <div class="form-group">
                <label for="dictId" class="col-sm-3 control-label">角色名：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control input-sm " ng-model="vm.role.roleName "
                           id="roleName" name="roleName" data-val="true" data-val-required="必填">
                    <span data-valmsg-for="roleName" data-valmsg-replace="true" class="text-red"></span>
                    <span class="errors" ng-show="vm.isRoleExist">角色已存在</span>
                </div>
            </div>
            <div class="form-group">
                <label for="displayName" class="col-sm-3 control-label">显示名：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <input type="text" maxlength="200" class="form-control input-sm "
                           ng-model="vm.role.displayName " id="displayName" name="displayName"
                           data-val="true" data-val-required="必填">
                    <span data-valmsg-for="displayName" data-valmsg-replace="true" class="text-red"></span>
                </div>
            </div>
            <div class="form-group">
                <label for="roleState1" class="col-sm-3 control-label">是否启用：<span class="text-red">*</span></label>
                <div class="col-sm-9">
                    <label class="radio-inline"><input type="radio" value="1" name="roleState" id="roleState1"
                                  ng-model="vm.role.roleState"> 启用</label>
                    <label class="radio-inline"><input type="radio" value="0" name="roleState" id="roleState0"
                                  ng-model="vm.role.roleState"> 禁用</label>
                </div>
            </div>
            <div class="form-group">
                <label for="remark" class="col-sm-3 control-label">备注：</label>
                <div class="col-sm-9">
                    <textarea class="form-control" maxlength="200" name="remark" id="remark"
                              ng-model="vm.role.remark" style="height:120px;"></textarea>
                </div>
            </div>
        </form>
    </div>
</section>