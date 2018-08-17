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
                <button class="btn btn-sm btn-primary" ui-sref="projectManageEdit">
                    <span class="glyphicon glyphicon-plus"></span> 添加项目
                </button>
                <button class="btn btn-sm btn-success" ng-click="vm.expProinfo()">导出</button>
           <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit">
                    <span class="glyphicon glyphicon-remove"></span> 批量删除
                </button>

                <table style="margin-top: 10px;" >
                    <tr>
                        <td valign="middle">收文编号：</td>
                        <td>
                            <input class="form-control input-sm" name="filter_like_fileCode" placeholder="包含"
                                   type="text">
                        </td>
                        <td valign="middle">项目名称：</td>
                        <td>
                            <input class="form-control input-sm" name="filter_like_projectName" placeholder="包含"
                                   type="text">
                        </td>
                        <td valign="middle">评审部门：</td>
                        <td>
                            <select class="form-control input-sm" style="width:100px;"
                                    ng-model="vm.model.reviewDept"
                                    name="filter_eq_reviewDept"
                                    ng-options="x.dictKey as x.dictName for x in DICT.DEPT.dicts.TRANSACT_DEPARTMENT.dictList">
                                <option value="">---请选择---</option>
                            </select>
                        </td>
                        <td>
                            发文日期：
                            <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                                <input class="form-control" size="16" type="text"  data-val="true"
                                       name="filter_gte_dispatchDate" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                <span class="input-group-addon"><span
                                        class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                            -
                            <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                                <input class="form-control" size="16" type="text"  data-val="true"
                                       name="filter_lte_dispatchDate" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                <span class="input-group-addon"><span
                                        class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>

                        <td valign="middle">发文号：</td>
                        <td>
                            <input class="form-control input-sm" name="filter_like_fileNum" placeholder="包含"
                                   type="text">
                        </td>
                        <td>
                            <button type="button" class="btn btn-default" name="refresh" aria-label="refresh" title="搜索">
                                搜索
                            </button>
                        </td>

                    </tr>

                </table>
        </div>

<#--        <div id="toolbar" class="form-inline" role="form">
                <div class="form-group">
                    <span>项目名称: </span>
                    <input name="filter_like_projectName" class="form-control w70" type="text">
                </div>

                <button type="button" class="btn btn-default" name="refresh" aria-label="refresh" title="搜索">
                    搜索
                </button>
            </div>
        </div>-->

        <script type="text/template" id="columnBtns">
            <a class="btn btn-xs btn-primary" href="#/projectManageView/{{row.id}}/view" >
                <span class="glyphicon glyphicon-pencil"></span> 查看
            </a>
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/edit" >
                <span class="glyphicon glyphicon-pencil"></span> 编辑
            </a>
     <#--       <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/cancel" >
                <span class="glyphicon glyphicon-pencil"></span> 作废
            </a>-->
            <button class="btn btn-xs btn-primary" ng-click="vm.cancel(row)">
                <span class="glyphicon glyphicon-pencil"></span> 作废
            </button>
        </script>
        <table bs-table-control="vm.rsTableControl" data-toolbar="#toolbar" id="listTable"></table>
    </div>

</section>