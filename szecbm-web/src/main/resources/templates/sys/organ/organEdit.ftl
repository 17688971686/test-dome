<fieldset>
    <legend>更新【{{model.organName}}】组织机构信息</legend>
    <form id="form" name="form" class="form-horizontal">
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" class="btn btn-sm btn-primary" ui-sref="organ" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-chevron-left"></span> 取消
                </button>
            <@shiro.hasPermission name="sys:organ:put">
                <button type="button" class="btn btn-sm btn-success" ng-click="saveOrgan()" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-ok"></span> 更新
                </button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:organ:authorization">
                <button type="button" class="btn btn-sm btn-success" ng-click="authorization()">
                    <span class="glyphicon glyphicon-bookmark"></span> 授权
                </button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:user:get">
                <button type="button" class="btn btn-sm btn-success" ui-sref="organ.user({id: organId})"
                        ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-user"></span> 机构人员
                </button>
            </@shiro.hasPermission>
            </div>
        </div>
        <div class="form-group">
            <label for="organCode" class="col-sm-2 control-label">机构代码：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="organCode" name="organCode" ng-model="model.organCode"
                       data-val="true" data-val-required="必填" disabled/>
                <span data-valmsg-for="organCode" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="organName" class="col-sm-2 control-label">机构名称：<span class="text-red">*</span></label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="organName" name="organName" ng-model="model.organName"
                       data-val="true" data-val-required="必填"/>
                <span data-valmsg-for="organName" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="shortName" class="col-sm-2 control-label">机构简称：<span class="text-red">*</span></label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="shortName" name="shortName" ng-model="model.shortName"
                       data-val="true" data-val-required="必填"/>
                <span data-valmsg-for="shortName" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="organType" class="col-sm-2 control-label">机构类型：<span class="text-red">*</span></label>
            <div class="col-sm-4">
                <select class="form-control" name="organType" id="organType" ng-model="model.organType" data-val="true"
                        data-val-required="必填"
                        ng-options="x.dictKey as x.dictName for x in DICT.ORGAN.dicts.ORGAN_TYPR.dictList">
                    <option value="">--请选择--</option>
                </select>
                <span data-valmsg-for="organType" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group" ng-show="model.organType == '1'">
            <label for="organName" class="col-sm-2 control-label">主要领导：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="organLead" name="organLead" ng-model="model.organLead"/>
            </div>
        </div>
        <div class="form-group" ng-show="model.organType == '1'">
            <label for="organName" class="col-sm-2 control-label">领导电话：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="leadPhone" name="leadPhone" ng-model="model.leadPhone"
                       data-val="true" placeholder="填写固话或手机号" data-val-regex="号码不合理！"
                       data-val-regex-pattern="^((0\d{2,3}-\d{7,8})|(\d{7,8})|(1[3|4|5|8|7][0-9]\d{4,8}))$"/>
                <span data-valmsg-for="leadPhone" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group" ng-show="model.organType == '1'">
            <label for="organName" class="col-sm-2 control-label">分管领导：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="organManage" name="organManage"
                       ng-model="model.organManage"/>
            </div>
        </div>
        <div class="form-group" ng-show="model.organType == '1'">
            <label for="organName" class="col-sm-2 control-label">分管领导电话：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="managePhone" name="managePhone" ng-model="model.managePhone"
                       data-val="true" placeholder="填写固话或手机号" data-val-regex="号码不合理！"
                       data-val-regex-pattern="^((0\d{2,3}-\d{7,8})|(\d{7,8})|(1[3|4|5|8|7][0-9]\d{4,8}))$"/>
                <span data-valmsg-for="managePhone" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="parentId" class="col-sm-2 control-label">父机构：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control input-sm " ng-model="parentOrgan.organName" disabled
                       id="parentId" name="parentId"/>
            </div>
        </div>
   <#--     <div class="form-group">
            <label for="itemOrder" class="col-sm-2 control-label">所属区域：</label>
            <div class="col-sm-4">
                <select class="form-control" name="organRegion" id="organRegion" ng-model="model.organRegion"
                        ng-options="x.dictKey as x.dictName for x in DICT.ORGAN.dicts.ORGAN_REGION.dictList">
                    <option value="">--请选择--</option>
                </select>
            </div>
        </div>-->
        <div class="form-group">
            <label for="itemOrder" class="col-sm-2 control-label">排序号：</label>
            <div class="col-sm-4">
                <input type="number" class="form-control" id="itemOrder" name="itemOrder" ng-model="model.itemOrder"/>
            </div>
        </div>
        <div class="form-group">
            <label for="itemOrder" class="col-sm-2 control-label">备注：</label>
            <div class="col-sm-4">
                <textarea class="form-control" maxlength="200" name="remark" id="remark" ng-model="model.remark"
                          style="height:120px;"></textarea>
            </div>
        </div>
    </form>
</fieldset>
<div id="organAuthorizationWin" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">【{{model.organName}}】机构菜单授权窗口</h4>
            </div>
            <div class="modal-body" style="max-height: 500px;overflow: auto;">
                <ul id="resourceTree" class="ztree"></ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" ng-click="toAuthorization()">
                    <span class="glyphicon glyphicon-ok"></span> 保存
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->