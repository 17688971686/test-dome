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
        <script type="text/template" id="columnBtns">
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/view" >
                <span class="glyphicon glyphicon-pencil"></span> 查看
            </a>
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/normal" >
                <span class="glyphicon glyphicon-pencil"></span> 恢复
            </a>
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/delete" >
                <span class="glyphicon glyphicon-pencil"></span> 删除
            </a>
        </script>

        <table bs-table-control="vm.bsTableCancelManagement" data-toolbar="#toolbar" id="listTable"></table>
    </div>

</section>