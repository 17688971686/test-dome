<!-- Content Header (Page header) -->
<#assign path=request.contextPath/>
<meta charset="UTF-8">
<section class="content-header">
    <h1>
        项目管理
    </h1>
    <ol class="breadcrumb">
        <li class="active">作废项目管理</li>
    </ol>
</section>


<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar" class="form-inline" role="form">
            <div class="form-inline" role="button">
                <button class="btn btn-sm btn-success" ng-click="vm.expProinfo()">导出</button>
            </div>
        </div>
        <script type="text/template" id="columnBtns">
            <a class="btn btn-xs btn-primary" href="#/projectManageView/{{row.id}}/cancelView" >
                <span class="glyphicon glyphicon-pencil"></span> 查看
            </a>
            <button class="btn btn-xs btn-primary" ng-click="vm.restore(row)">
                <span class="glyphicon glyphicon-pencil"></span> 恢复
            </button>
            <button class="btn btn-xs btn-primary" ng-click="vm.delete(row.id)">
                <span class="glyphicon glyphicon-pencil"></span> 删除
            </button>
        </script>

        <table bs-table-control="vm.bsTableCancelManagement" data-toolbar="#toolbar" id="listTable"></table>
    </div>

</section>