
<!-- Content Header (Page header) -->
<#assign path=request.contextPath/>
<meta charset="UTF-8">

<section class="content-header">
    <h1>
        文档库
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">文档库</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar">
            <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit"><span class="glyphicon glyphicon-remove"></span>批量删除</button>
          <#--  <div class="form-inline">
                <a class="btn btn-sm btn-primary" href = "#/attachmentEdit/"><span class="glyphicon glyphicon-plus"></span>上传文档</a>
           </div>-->
        </div>
        <script type="text/template" id="columnBtns">
                <button class="btn btn-xs btn-danger" ng-click="vm.del(row.id)">
                    <span class="glyphicon glyphicon-remove"></span> 删除
                </button>
                <a class="btn btn-xs btn-primary" href="#/attachmentEdit/{{row.id}}">
                    <span class="glyphicon glyphicon-pencil"></span> 编辑
                </a>
        </script>

        <table bs-table-control="vm.bsTableControl" data-toolbar="#toolbar" id="editTable"></table>
    </div>
</section>