<!-- Content Header (Page header) -->
<#assign path=request.contextPath/>
<meta charset="UTF-8">
<section class="content-header">
    <h1>
        项目管理
    </h1>
    <ol class="breadcrumb">
        <li class="active">项目管理</li>
    </ol>
</section>


<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar" class="form-inline" role="form">
            <div class="form-inline" role="button">
                <button class="btn btn-sm btn-primary" ui-sref="projectManageEdit">
                    <span class="glyphicon glyphicon-plus"></span> 添加项目
                </button>
            <#--    <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit">
                    <span class="glyphicon glyphicon-remove"></span> 批量删除
                </button>-->
            </div>
        </div>

        <script type="text/template" id="columnBtns">
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/view" >
                <span class="glyphicon glyphicon-pencil"></span> 查看
            </a>
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/edit" >
                <span class="glyphicon glyphicon-pencil"></span> 编辑
            </a>
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/cancel" >
                <span class="glyphicon glyphicon-pencil"></span> 作废
            </a>
        </script>

        <table bs-table-control="vm.bsTableControlForManagement" data-toolbar="#toolbar" id="listTable"></table>
    </div>

</section>