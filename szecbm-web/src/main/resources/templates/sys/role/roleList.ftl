<!-- Content Header (Page header) -->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<link href="${path}/libs/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
<script src="${path}/libs/zTree/js/jquery.ztree.all.min.js" type="text/javascript"></script>

<section class="content-header">
    <h1>
        角色管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">角色信息管理</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <div id="toolbar">
            <button class="btn btn-sm btn-primary" ui-sref="roleEdit">
                <span class="glyphicon glyphicon-plus"></span> 新增角色
            </button>
            <button class="btn btn-sm btn-danger" ng-click="vm.dels()" ng-disabled="vm.isSubmit"><span
                    class="glyphicon glyphicon-remove"></span> 批量删除
            </button>
        </div>
        <script type="text/template" id="columnBtns">
            <button class="btn btn-xs btn-danger" ng-click="vm.del(row.roleId)">
                <span class="glyphicon glyphicon-remove"></span> 删除
            </button>
            <a class="btn btn-xs btn-primary" href="#/roleEdit/{{row.roleId}}">
                <span class="glyphicon glyphicon-pencil"></span> 编辑
            </a>
            <button class="btn btn-xs btn-primary" ng-click="vm.authorization(row)">
                <span class="glyphicon glyphicon-bookmark"></span> 授权
            </button>
            <#--<button class="btn btn-xs btn-primary" ng-click="vm.setUserData(row)">
                <span class="glyphicon glyphicon-user"></span> 设置用户
            </button>-->
            <button class="btn btn-xs btn-primary" ng-click="vm.start(row.roleId)" ng-show="row.roleState==0">启用
            </button>
            <button class="btn btn-xs btn-danger" ng-click="vm.stop(row.roleId)" ng-show="row.roleState==1">禁用</button>
        </script>

        <table bs-table-control="vm.bsTableControl" data-toolbar="#toolbar" id="editTable"></table>
    </div>

    <div id="roleAuthorizationWin" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">【{{vm.displayName}}】角色菜单授权窗口</h4>
                </div>
                <div class="modal-body" style="max-height: 500px;overflow: auto;">
                    <ul id="resourceTree" class="ztree"></ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" ng-click="vm.toAuthorization()">
                        <span class="glyphicon glyphicon-ok"></span> 保存
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</section>