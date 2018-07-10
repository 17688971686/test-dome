<!-- Content Header (Page header) -->
<fieldset>
    <legend>添加组织机构</legend>
    <form id="form" name="form" class="form-horizontal">
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <a class="btn btn-sm btn-primary" href="#/organ">
                    <span class="glyphicon glyphicon-chevron-left"></span> 取消
                </a>
            <@shiro.hasPermission name="sys:organ:post">
                <button class="btn btn-sm btn-success" ng-click="saveOrgan()" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-ok"></span> 创建
                </button>
            </@shiro.hasPermission>
            </div>
        </div>
        <div class="form-group">
            <label for="organCode" class="col-sm-2 control-label">机构代码：<span class="text-red">*</span></label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="organCode" name="organCode" ng-model="model.organCode"
                       data-val="true" data-val-required="必填"/>
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
                        data-val-required="必填">
                    <option value="">--请选择--</option>
                    <option value="0">分类</option>
                    <option value="1">单位</option>
                    <option value="2">部门</option>
                </select>
                <span data-valmsg-for="organType" data-valmsg-replace="true" class="text-red"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="parentId" class="col-sm-2 control-label">父机构：</label>
            <div class="col-sm-4">
                <input type="hidden" class="form-control input-sm " ng-model="model.parentId"
                       id="parentId" name="parentId"/>
                <input ng-show="hasParent" type="text" class="form-control input-sm " disabled
                       ng-model="parentOrgan.organName"/>
                <ul ng-show="!hasParent" id="parentOrganTree" class="ztree"></ul>
            </div>
        </div>
        <div class="form-group">
            <label for="itemOrder" class="col-sm-2 control-label">所属区域：</label>
            <div class="col-sm-4">
                <select class="form-control" name="organRegion" id="organRegion" ng-model="model.organRegion"
                        ng-options="x.dictKey as x.dictName for x in DICT.ORGAN.dicts.ORGAN_REGION.dictList">
                    <option value="">--请选择--</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="itemOrder" class="col-sm-2 control-label">排序号：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="itemOrder" name="itemOrder" ng-model="model.itemOrder"/>
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