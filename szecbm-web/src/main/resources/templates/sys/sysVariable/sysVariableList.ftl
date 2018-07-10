<!-- Content Header (Page header) -->
<meta charset="UTF-8">
<#assign path=request.contextPath/>

<section class="content-header">
    <h1>
        系统变量管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">系统变量管理</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar" class="form-inline">
            <button class="btn btn-sm btn-primary" ui-sref="sysVariableEdit">
                <span class="glyphicon glyphicon-plus"></span> 新增系统变量
            </button>
            <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit">
                <span class="glyphicon glyphicon-remove"></span> 批量删除
            </button>
        </div>
        <script type="text/template" id="columnBtns">
            <button class="btn btn-xs btn-danger" ng-click="vm.del(row.varId)">
                <span class="glyphicon glyphicon-remove"></span> 删除
            </button>
            <a class="btn btn-xs btn-primary" href="#/sysVariableEdit/{{row.varId}}">
                <span class="glyphicon glyphicon-pencil"></span> 编辑
            </a>
        </script>

        <table bs-table-control="vm.bsTableControl" data-toolbar="#toolbar" id="editTable"></table>
    </div>

</section>