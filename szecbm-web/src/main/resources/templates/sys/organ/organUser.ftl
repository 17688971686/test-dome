<!-- Content Header (Page header) -->
<fieldset>
    <legend>【{{organ.organName}}】 - 机构人员管理</legend>

    <div id="userToolbar">
        <div class="form-inline" role="form">
            <div class="form-group">
                <button type="button" class="btn btn-sm btn-primary" class="btn btn-primary"
                        ui-sref="organ.edit({id: organId})" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-chevron-left"></span> 返回
                </button>
            <@shiro.hasPermission name="sys:user:post">
                <button class="btn btn-sm btn-primary" ng-click="openUserEditWin()" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-plus"></span> 添加用户
                </button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:user:delete">
                <button class="btn btn-sm btn-danger" ng-click="delUsers()" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-remove"></span> 批量删除
                </button>
            </@shiro.hasPermission>
            </div>
            <div class="form-group">
                <span>用户名: </span>
                <input name="filter_like_username|displayName" class="form-control w70" type="text">
            </div>
            <div class="form-group">
                <span>姓名: </span>
                <input name="filter_eq_displayName" class="form-control w70" type="text">
            </div>
            <button type="button" class="btn btn-default" name="refresh" aria-label="refresh" title="搜索">
                搜索
            </button>
        </div>
    </div>

    <script type="text/template" id="columnBtns">
        <@shiro.hasPermission name="sys:user:delete">
        <button class="btn btn-xs btn-danger" ng-click="vm.del(row.userId)" ng-disabled="vm.isSubmit">
            <span class="glyphicon glyphicon-remove"></span> 删除
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
        <@shiro.hasPermission name="sys:user:isEnable">
        <button class="btn btn-xs btn-success" ng-click="vm.start(row)" ng-show="row.useState==0"
                ng-disabled="vm.isSubmit">
            启用
        </button>
        <button class="btn btn-xs btn-danger" ng-click="vm.stop(row)" ng-show="row.useState==1"
                ng-disabled="vm.isSubmit">
            禁用
        </button>
        </@shiro.hasPermission>
    </script>

    <table bs-table-control="bsTableControl" data-toolbar="#userToolbar" id="editTable"></table>
</fieldset>

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
                            <input type="text" class="form-control" id="organName" name="organName"
                                   ng-model="organ.organName"
                                   disabled/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="username" class="col-sm-3 control-label">登录名：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="username" name="username"
                                   ng-model="model.username"
                                   data-val="true" data-val-required="必填"/>
                            <span data-valmsg-for="username" data-valmsg-replace="true" class="errors"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="displayName" class="col-sm-3 control-label">用户姓名：<span
                                class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="displayName" name="displayName"
                                   ng-model="model.displayName"
                                   data-val="true" data-val-required="必填"/>
                            <span data-valmsg-for="displayName" data-valmsg-replace="true" class="errors"></span>
                        </div>
                    </div>
                    <div class="form-group" ng-hide="isUpdate">
                        <label for="password" class="col-sm-3 control-label">密码：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="password" name="password"
                                   ng-model="model.password"
                                   data-val="true" data-val-required="必填"/>
                            <span data-valmsg-for="password" data-valmsg-replace="true" class="errors"></span>
                        </div>
                    </div>
                    <div class="form-group" ng-hide="isUpdate">
                        <label for="verifyPassword" class="col-sm-3 control-label">确认密码：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control" id="verifyPassword" name="verifyPassword"
                                   ng-model="model.verifyPassword"
                                   data-val="true" data-val-required="必填"/>
                            <span data-valmsg-for="verifyPassword" data-valmsg-replace="true" class="errors"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="verifyPassword" class="col-sm-3 control-label">用户状态：<span class="text-red">*</span></label>
                        <div class="col-sm-9">
                            <label> <input type="radio" value="1" name="useState" id="useState1"
                                           ng-model="model.useState" ng-checked="model.useState==1"> 启用</label>
                            <label><input type="radio" value="0" name="useState" id="useState0"
                                          ng-model="model.useState" ng-checked="model.useState==0">禁用</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="itemOrder" class="col-sm-3 control-label">序号：</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="itemOrder" name="itemOrder"
                                   ng-model="model.itemOrder"/>
                            <span data-valmsg-for="itemOrder" data-valmsg-replace="true" class="errors"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="verifyPassword" class="col-sm-3 control-label">备注：</label>
                        <div class="col-sm-9">
                            <textarea class="form-control" maxlength="200" name="remark" id="remark"
                                      ng-model="model.remark"
                                      style="height:120px;"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" ng-disabled="isSubmit" ng-click="saveUser();">
                    保存
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>