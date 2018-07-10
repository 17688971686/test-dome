<!-- Content Header (Page header) -->
<#assign path=request.contextPath/>
<meta charset="UTF-8">
<section class="content-header">
    <h1>
        系统操作日志
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">系统操作日志</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar">
            <div class="btn-group">
                <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                        aria-expanded="false">删除<span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#/operatorLog" ng-click="vm.delLog('1')">一天前</a></li>
                    <li><a href="#/operatorLog" ng-click="vm.delLog('3')">三天前</a></li>
                    <li><a href="#/operatorLog" ng-click="vm.delLog('7')">一周前</a></li>
                    <li><a href="#/operatorLog" ng-click="vm.delLog('30')">一个月前</a></li>
                    <li><a href="#/operatorLog" ng-click="vm.delLog('90')">三个月前</a></li>
                </ul>
            </div>
        <#--<div class="form-inline" role="button">
            <button class="btn btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit">
                <span class="glyphicon glyphicon-remove"></span> 批量删除
            </button>
        </div>-->
        </div>

    <#--<script type="text/template" id="columnBtns">
        <button class="btn btn-xs btn-danger" ng-click="vm.del(row.id)" >
            <span class="glyphicon glyphicon-remove"></span> 删除
        </button>
    </script>-->

        <table bs-table-control="bsTableControl" data-toolbar="#toolbar" id="editTable"></table>
    </div>
</section>