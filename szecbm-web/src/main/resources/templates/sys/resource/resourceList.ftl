<!-- Content Header (Page header) -->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<link href="${path}/libs/zTree/css/metroStyle/metroStyle.diy.css" rel="stylesheet"/>
<script src="${path}/libs/zTree/js/jquery.ztree.all.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/jquery.ztree.exhide.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/fuzzysearch.min.js" type="text/javascript"></script>

<section class="content-header">
    <h1>
        系统资源管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i>系统管理</a></li>
        <li class="active">系统资源管理</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm container-fluid" style="background:white;">
        <div class="row">
            <div class="col-md-12" role="button" style="margin-bottom: 10px;">
                <button class="btn btn-primary" ng-click="createResource()" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-plus"></span> 添加
                </button>
                <button class="btn btn-danger" ng-click="resetResource()" ng-disabled="isSubmit">
                    <span class="glyphicon glyphicon-share-alt"></span> 重置
                </button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-3" style="overflow-x:auto;min-height: 600px;">
                <fieldset>
                    <legend>系统资源树</legend>
                    <div class="form-group has-success has-feedback">
                        <input type="text" id="resourceKey" value="" class="form-control input-sm" placeholder="资源检索"/>
                        <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <div style="overflow-x:auto;min-height: 600px;">
                        <ul id="resourceTree" class="ztree"></ul>
                    </div>
                </fieldset>
            </div>
            <div class="col-md-9">
                <fieldset>
                    <legend>{{editTitle}}</legend>
                    <form class="form-horizontal" style="max-width: 600px;">
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="button" class="btn btn-primary" ng-click="createSave()" ng-if="!isUpdate">
                                    <span class="glyphicon glyphicon-ok"></span> 新增
                                </button>
                                <button type="button" class="btn btn-primary" ng-click="updateSave()" ng-if="isUpdate">
                                    <span class="glyphicon glyphicon-ok"></span> 更新
                                </button>
                                <button type="button" class="btn btn-danger" ng-click="createResource()">取消</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="resName" class="col-sm-3 control-label">资源名称：<span
                                    class="text-red">*</span></label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="resName" name="resName"
                                       ng-model="model.resName" data-val="true" data-val-required="必填"/>
                                <span data-valmsg-for="resName" data-valmsg-replace="true" class="text-red"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="resType" class="col-sm-3 control-label">资源类型：<span
                                    class="text-red">*</span></label>
                            <div class="col-sm-9">
                                <select class="form-control" name="resType" id="resType" ng-model="model.resType"
                                        data-val="true" data-val-required="必填">
                                    <option value="">--请选择--</option>
                                    <option ng-repeat="x in DICT.RESOURCE.dicts.RESOURCE_TYPR.dictList"
                                            value="{{x.dictKey}}">
                                        {{x.dictName}}
                                    </option>
                                </select>
                                <span data-valmsg-for="resType" data-valmsg-replace="true" class="text-red"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="resUri" class="col-sm-3 control-label">资源链接：</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="resUri" name="resUri"
                                       ng-model="model.resUri"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="parentId" class="col-sm-3 control-label">父资源：</label>
                            <div class="col-sm-9">
                                <input type="hidden" class="form-control" id="parentId" name="parentId"
                                       ng-model="model.parentId"/>
                                <ul id="parentResourceTree" class="ztree"></ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="resIcon" class="col-sm-3 control-label">图标：</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="resIcon" name="resIcon"
                                       ng-model="model.resIcon"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="target" class="col-sm-3 control-label">打开方式：</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="target" name="target"
                                       ng-model="model.target"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">资源状态：<span class="text-red">*</span></label>
                            <div class="col-sm-9">
                                <label class="radio-inline">
                                    <input type="radio" id="resStatus1" name="status" value="1" ng-model="model.status"
                                           ng-checked="model.status==1"> 可用
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" id="resStatus0" name="status" value="0" ng-model="model.status"
                                           ng-checked="model.status==0"> 禁用
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="permCode" class="col-sm-3 control-label">权限码：</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="permCode" name="permCode"
                                       ng-model="model.permCode"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="itemOrder" class="col-sm-3 control-label">序号：</label>
                            <div class="col-sm-9">
                                <input type="number" maxlength="200" class="form-control input-sm "
                                       ng-model="model.itemOrder"
                                       id="itemOrder" name="itemOrder"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="remark" class="col-sm-3 control-label">备注：</label>
                            <div class="col-sm-9">
                                <textarea class="form-control" id="remark" name="remark" ng-model="model.remark"
                                          cols="30" rows="3"></textarea>
                            </div>
                        </div>
                    </form>
                </fieldset>
            </div>
        </div>
    </div>
</section>