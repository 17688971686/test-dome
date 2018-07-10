<meta charset="UTF-8">

<section class="content-header">
    <h1>
        欢迎使用
        <small>欢迎您：<b>${currentUser.username!''}</b></small>
    </h1>
    <ol class="breadcrumb">
        <li><i class="fa fa-dashboard"></i> 系统主页</li>
        <li class="active">主页</li>
    </ol>
</section>
<!-- Main content -->
<section class="content" id="welcome">
    <div class="row">
        <div class="col-md-6">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">您最近的操作</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse">
                            <i class="fa fa-minus"></i>
                        </button>
                        <button type="button" class="btn btn-box-tool" ng-click="vm.getOperatorLogList()">
                            <i class="glyphicon glyphicon-refresh icon-refresh"></i>
                        </button>
                    </div><!-- /.box-tools -->
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <div class="table-responsive">
                        <table class="table no-margin" ng-init="vm.getOperatorLogList()">
                            <thead>
                            <tr>
                                <th width="40">#</th>
                                <th>操作人</th>
                                <th>操作名</th>
                                <th></th>
                                <th width="160">时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="x in vm.operatorLogList.value">
                                <td>{{$index + 1}}.</td>
                                <td>{{x.createdBy}}</td>
                                <td>{{x.operateName}}</td>
                                <td><span ng-show='x.operateType==1' class='bg-green'>成功</span><span
                                        ng-show='x.operateType!=1' class='bg-red'>失败</span></td>
                                <td>
                                    <span class="label label-info">{{x.createdDate}}</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <!-- /.table-responsive -->
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </div>
    </div>
</section>
