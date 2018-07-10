<!-- Content Header (Page header) -->
<#assign path=request.contextPath/>
<link href="${path}/libs/zTree/css/metroStyle/metroStyle.diy.css" rel="stylesheet"/>
<script src="${path}/libs/zTree/js/jquery.ztree.all.min.js"></script>
<script src="${path}/libs/zTree/js/jquery.ztree.exhide.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/fuzzysearch.min.js" type="text/javascript"></script>

<meta charset="UTF-8">
<section class="content-header">
    <h1>
        数据字典管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i>系统管理</a></li>
        <li class="active">数据字典管理</li>
    </ol>
</section>

<section class="content">
    <div id="editTable" class="well well-sm container-fluid" style="background:white;">
        <div class="row">
            <div class="col-lg-12" role="button" style="margin-bottom: 10px;">
                <a class="btn btn-sm btn-primary" ui-sref="dict.edit({dictId:null})">
                    <span class="glyphicon glyphicon-plus"></span> 新增
                </a>
                <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit">
                    <span class="glyphicon glyphicon-remove"></span> 删除
                </button>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4">
                <fieldset>
                    <legend>数据字典树</legend>
                    <div class="form-group has-success has-feedback">
                        <input type="text" id="dictTreeKey" value="" class="form-control input-sm" placeholder="字典检索"/>
                        <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <div style="overflow-x:auto;min-height: 600px;">
                        <ul id="zTree" class="ztree"></ul>
                    </div>
                </fieldset>
            </div>
            <div class="col-md-8">
                <fieldset>
                    <legend>数据字典编辑表单</legend>
                    <div ui-view></div>
                </fieldset>
            </div>
        </div>
    </div>
</section>

