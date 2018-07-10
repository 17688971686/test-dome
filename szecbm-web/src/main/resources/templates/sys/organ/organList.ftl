<!-- Content Header (Page header) -->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<link href="${path}/libs/zTree/css/metroStyle/metroStyle.diy.css" rel="stylesheet"/>
<script src="${path}/libs/zTree/js/jquery.ztree.all.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/jquery.ztree.exhide.min.js" type="text/javascript"></script>
<script src="${path}/libs/zTree/js/fuzzysearch.min.js" type="text/javascript"></script>

<section class="content-header">
    <h1> 组织机构管理 </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">组织机构管理</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar" class="row">
            <div class="col-md-12 form-inline" role="button" style="margin-bottom: 10px;">
            <@shiro.hasPermission name="sys:organ:post">
                <button class="btn btn-sm btn-primary" ui-sref="organ.add">
                    <span class="glyphicon glyphicon-plus"></span> 添加机构
                </button>
            </@shiro.hasPermission>
                <button class="btn btn-sm btn-success" ng-click="initOrganTree()">
                    <span class="glyphicon glyphicon-refresh"></span> 刷新机构
                </button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-3" style="padding-bottom: 10px;padding-top: 15px;">
                <fieldset>
                    <legend>组织机构树</legend>
                    <div class="form-group has-success has-feedback">
                        <input type="text" id="organKey" value="" class="form-control input-sm" placeholder="机构检索" />
                        <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <div style="overflow-x:auto;min-height: 600px;">
                        <ul id="organTree" class="ztree" <@shiro.hasPermission name="sys:organ:delete">delete="true"</@shiro.hasPermission>></ul>
                    </div>
                </fieldset>
            </div>
            <div class="col-md-9" ui-view=""></div>
        </div>

    </div>
</section>
<@shiro.hasPermission name="sys:organ:post">
<script id="treeAddBtn" type="text/template">
    <span class='button add' id='addBtn_{0}' title='添加机构' onfocus='this.blur();'></span>
</script>
</@shiro.hasPermission>