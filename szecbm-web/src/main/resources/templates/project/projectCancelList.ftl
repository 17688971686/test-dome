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
            <button class="btn btn-sm btn-success" ng-click="vm.expProinfo()">导出</button>
            <table style="margin-top: 10px;" >
                <tr>
                    <td valign="middle">收文编号：</td>
                    <td>
                        <input class="form-control input-sm" name="filter_like_fileCode"
                               type="text">
                    </td>
                    <td valign="middle">项目名称：</td>
                    <td>
                        <input class="form-control input-sm" name="filter_like_projectName"
                               type="text">
                    </td>
                    <td valign="middle">评审部门：</td>
                    <td>
                        <select class="form-control input-sm" style="width:100px;"
                                name="filter_eq_reviewDept">
                            <option value="">---请选择---</option>
                            <option ng-repeat="x in DICT.DEPT.dicts.TRANSACT_DEPARTMENT.dictList"
                                    value="{{x.dictKey}}">
                                {{x.dictName}}
                            </option>
                        </select>
                    </td>
                    <td valign="middle">  发文日期：</td>
                    <td>

                        <div class="input-group date"  sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                            <input class="form-control date-input" size="16" type="text"  data-val="true"
                                   name="filter_gte_dispatchDate" readonly>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                            <span class="input-group-addon"><span
                                    class="glyphicon glyphicon-calendar"></span></span>
                        </div>
                        -
                        <div class="input-group date"  sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                            <input class="form-control date-input" size="16" type="text"  data-val="true"
                                   name="filter_lte_dispatchDate" readonly>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                            <span class="input-group-addon"><span
                                    class="glyphicon glyphicon-calendar"></span></span>
                        </div>
                    </td>

                    <td valign="middle">发文号：</td>
                    <td>
                        <input class="form-control input-sm" name="filter_like_fileNum" type="text">
                    </td>
                    <td>
                        <button type="button" class="btn btn-default" name="refresh" aria-label="refresh" title="搜索">
                            搜索
                        </button>
                    </td>

                </tr>

            </table>
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