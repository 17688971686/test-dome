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
        <form id="filterForm" class="form-horizontal">
            <div class="panel panel-default">
                <div class="panel-heading">查询条件</div>
                <div style="margin-bottom: 10px;">
                    <form id="filterForm" class="form-horizontal">
                        <div class="row">
                            <div class="form-group" style="margin-top: 15px">
                                <label class="control-label col-sm-1" for="filter_like_fileCode">收文编号：</label>
                                <div class="col-sm-2">
                                    <input type="text" class="form-control text-input"
                                           id="filter_like_fileCode" name="filter_like_fileCode">
                                </div>
                                <label class="control-label col-sm-1"
                                       for="filter_like_projectName">项目名称：</label>
                                <div class="col-sm-2">
                                    <input type="text" class="form-control text-input"
                                           id="filter_like_projectName" name="filter_like_projectName">
                                </div>
                                <label class="control-label col-sm-1"
                                       for="txt_search_priUser">评审部门：</label>
                                <div class="col-sm-2">
                                    <select class="form-control input-sm" style="width:100px;"
                                            name="filter_eq_reviewDept">
                                        <option value="">---请选择---</option>
                                        <option ng-repeat="x in DICT.DEPT.dicts.TRANSACT_DEPARTMENT.dictList"
                                                value="{{x.dictKey}}">
                                            {{x.dictName}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-1">发文日期：</label>
                                <div class="col-sm-5 form-inline" >
                                <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd"  style="width: 40%">
                                    <input class="form-control date-input" size="16" type="text" data-val="true"
                                           name="filter_gte_dispatchDate" readonly>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                                <label> - </label>
                                <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 40%">
                                    <input class="form-control date-input" size="16" type="text" data-val="true"
                                           name="filter_lte_dispatchDate" readonly>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                                </div>
                                <label class="control-label col-sm-1"
                                       for="filter_like_fileNum">发文号：</label>
                                <div class="col-sm-2">
                                    <input class="form-control input-sm" name="filter_like_fileNum" type="text">
                                </div>
                                <div class="col-sm-1">
                                    <button type="button" class="btn btn-primary btn-sm"
                                            id="searchBtn" name="refresh" ng-click="vm.filterSearch()">
                                        <span class="glyphicon glyphicon-search"></span>&nbsp;查询
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </form>
        <div id="toolbar">
            <button class="btn btn-sm btn-primary" ui-sref="projectManageEdit">
                <span class="glyphicon glyphicon-plus"></span> 添加项目
            </button>
            <button class="btn btn-sm btn-success" ng-click="vm.expProinfo()">导出</button>
        </div>
        <table bs-table-control="vm.rsTableControl" data-toolbar="#toolbar" id="listTable"></table>
        <script type="text/template" id="columnBtns">
            <a class="btn btn-xs btn-primary" href="#/projectManageView/{{row.id}}/view">
                <span class="glyphicon glyphicon-search"></span> 查看
            </a>
            <a class="btn btn-xs btn-primary" href="#/projectManageEdit/{{row.id}}/edit">
                <span class="glyphicon glyphicon-pencil"></span> 编辑
            </a>
            <button class="btn btn-xs btn-danger" ng-click="vm.cancel(row)">
                <span class="glyphicon glyphicon-remove"></span> 作废
            </button>
        </script>
    </div>
</section>