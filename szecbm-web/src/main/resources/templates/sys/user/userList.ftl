<!-- Content Header (Page header) -->
<#assign path=request.contextPath/>
<meta charset="UTF-8">
<link href="${path}/libs/zTree/css/metroStyle/metroStyle.diy.css" rel="stylesheet"/>
<script src="${path}/libs/zTree/js/jquery.ztree.all.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/jquery.ztree.exhide.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/fuzzysearch.min.js" type="text/javascript"></script>

<section class="content-header">
    <h1>
        用户管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">用户信息管理</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div class="row">
            <div class="col-md-3">
                <fieldset style="padding-bottom: 10px;padding-top: 15px;">
                    <legend>组织机构树</legend>
                    <div class="form-group has-success has-feedback">
                        <input type="text" id="organKey" value="" class="form-control input-sm" placeholder="机构检索"/>
                        <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <div style="overflow-x:auto;min-height: 500px;">
                        <ul id="organTree" class="ztree"></ul>
                    </div>
                </fieldset>
            </div>
            <div class="col-md-9" ui-view="">
                <fieldset>
                    <legend>【{{vm.organName}}】 - 机构人员管理</legend>
                    <div id="toolbar">
                    <@shiro.hasPermission name="sys:user:post">
                        <button class="btn btn-sm btn-primary" ng-click="vm.openUserEditWin()"
                                ng-disabled="vm.isSubmit">
                            <span class="glyphicon glyphicon-plus"></span> 添加
                        </button>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="sys:user:delete">
                        <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit">
                            <span class="glyphicon glyphicon-remove"></span> 批量删除
                        </button>
                    </@shiro.hasPermission>
                    </div>
                    <table bs-table-control="vm.bsTableControl" data-toolbar="#toolbar" id="editTable"></table>
                </fieldset>
            </div>
        </div>
    </div>
</section>

<script type="text/template" id="userColumnBtns">
    <@shiro.hasPermission name="sys:user:setRoles">
    <button class="btn btn-xs btn-primary" ng-click="vm.setRoles(row)" ng-disabled="vm.isSubmit">
        <span class="glyphicon glyphicon-pencil"></span> 设置角色
    </button>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="sys:user:put">
    <button class="btn btn-xs btn-primary" ng-click="vm.openUserEditWin(row.userId)" ng-disabled="vm.isSubmit">
        <span class="glyphicon glyphicon-pencil"></span> 编辑
    </button>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="sys:user:resetPwd">
    <button class="btn btn-xs btn-primary" ng-click="vm.resetPwd(row.userId)" ng-disabled="vm.isSubmit">
        <span class="glyphicon glyphicon-pencil"></span> 重置密码
    </button>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="sys:user:delete">
    <button class="btn btn-xs btn-danger" ng-click="vm.del(row.userId)" ng-disabled="vm.isSubmit">
        <span class="glyphicon glyphicon-remove"></span> 删除
    </button>
    </@shiro.hasPermission>

</script>

<div id="organUserEditModel" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">机构人员编辑窗口</h4>
            </div>
            <div class="modal-body">
                <form id="organUserForm" class="form-horizontal">
                    <div class="form-group">
                        <label for="organName" class="col-sm-3 control-label">所属机构：</label>
                        <div class="col-sm-9">
                            <select class="form-control" id="organName" name="organName" ng-model="vm.model.organ.organId">
                                <option value="">请选择</option>
                                <option ng-repeat="x in vm.orgList" value="{{x.organId}}"
                                        ng-selected="vm.model.organ && (vm.model.organ.organId == x.organId)">{{x.organName}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="username" class="col-sm-3 control-label">登录名：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="username" name="username"
                                   ng-model="vm.model.username" data-val="true" data-val-required="必填"/>
                            <span data-valmsg-for="username" data-valmsg-replace="true" class="text-red"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="displayName" class="col-sm-3 control-label">用户姓名：<span
                                class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="displayName" name="displayName"
                                   ng-model="vm.model.displayName" data-val="true" data-val-required="必填"/>
                            <span data-valmsg-for="displayName" data-valmsg-replace="true" class="text-red"></span>
                        </div>
                    </div>
                    <div class="form-group" ng-if="!vm.isUpdate">
                        <label for="password" class="col-sm-3 control-label">密码：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="password" name="password"
                                   ng-model="vm.model.password" data-val="true" data-val-required="必填"
                                   data-val-length-min="6" data-val-length-max="18" data-val-length="6～18个字符"/>
                            <span data-valmsg-for="password" data-valmsg-replace="true" class="text-red"></span>
                        </div>
                    </div>
                    <div class="form-group" ng-if="!vm.isUpdate">
                        <label for="verifyPassword" class="col-sm-3 control-label">确认密码：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="verifyPassword" name="verifyPassword"
                                   ng-model="vm.model.verifyPassword" data-val="true" data-val-required="必填"
                                   data-val-equalto-other="password" data-val-equalto="两次密码不一致"/>
                            <span data-valmsg-for="verifyPassword" data-valmsg-replace="true" class="text-red"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="username" class="col-sm-3 control-label">电话号码：<span
                                class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"
                                   ng-model="vm.model.phoneNumber" data-val="true" data-val-required="必填"
                                   data-val-regex-pattern="^((0\d{2,3}-\d{7,8})|(\d{7,8})|(1[3|4|5|8|7][0-9]\d{4,8}))$"
                                   data-val-regex="号码不合理！"/>
                            <span data-valmsg-for="phoneNumber" data-valmsg-replace="true" class="text-red"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="username" class="col-sm-3 control-label">手机号码：<span
                                class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="mobileNumber" name="mobileNumber"
                                   ng-model="vm.model.mobileNumber" data-val="true" data-val-required="必填"
                                   data-val-regex-pattern="^1(3|4|5|7|8)\d{9}$" data-val-regex="号码不合理！"/>
                            <span data-valmsg-for="mobileNumber" data-valmsg-replace="true" class="text-red"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="verifyPassword" class="col-sm-3 control-label">用户状态：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <label><input type="radio" value="1" name="useState" id="useState1"
                                          ng-model="vm.model.useState" ng-checked="model.useState==1"> 启用</label>
                            <label><input type="radio" value="0" name="useState" id="useState0"
                                          ng-model="vm.model.useState" ng-checked="model.useState==0">禁用</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="itemOrder" class="col-sm-3 control-label">序号：</label>
                        <div class="col-sm-9">
                            <input type="number" class="form-control" id="itemOrder" name="itemOrder"
                                   ng-model="vm.model.itemOrder"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="verifyPassword" class="col-sm-3 control-label">备注：</label>
                        <div class="col-sm-9">
                            <textarea class="form-control" maxlength="200" name="remark" id="remark"
                                      ng-model="vm.model.remark"
                                      style="height:120px;"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" ng-disabled="isSubmit" ng-click="vm.saveUser();">
                    保存
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div id="userRolesModel" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">人员角色设置窗口</h4>
            </div>
            <div class="modal-body" style="height: 300px;overflow: hidden;">
                <form class="form-horizontal">
                    <ul>
                        <li ng-repeat="x in vm.roles">
                            <label>
                                <input type="checkbox" name="roleId" ng-value="x.roleId" ng-checked="x.checked">
                                {{x.displayName || x.roleName}}
                            </label>
                        </li>
                    </ul>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" ng-disabled="vm.isSubmit" ng-click="vm.toSetRoles();">
                    保存
                </button>
                <button type="button" class="btn btn-default" ng-disabled="vm.isSubmit" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>