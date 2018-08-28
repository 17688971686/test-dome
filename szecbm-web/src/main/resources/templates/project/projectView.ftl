<!-- Content Header (Page header) -->
<section class="content-header">
    <h1>
        项目编辑
    </h1>
    <ol class="breadcrumb">
        <li class="active">小型项目管理</li>
        <li class="active">项目填报</li>
        <li class="active">项目编辑</li>
    </ol>
</section>


<!-- Main content -->
<section class="content">


    <div class="well well-sm" style="background:white;">
        <form id="form" name="form" ng-init="vm.model.fillDate = '${.now?string("yyyy-MM-dd")}'">
            <div class="toolbar">
                <button ng-if="vm.flag=='view'" class="btn btn-sm btn-primary" ui-sref="projectManage">
                    <span class="glyphicon glyphicon-chevron-left"></span> 返回
                </button>
                 <button ng-if="vm.flag=='cancelView'" class="btn btn-sm btn-primary" ui-sref="projectManageCancel">
                    <span class="glyphicon glyphicon-chevron-left"></span> 返回
                </button>
                <button class="btn btn-sm btn-primary" ng-click="vm.clickDetailBt();" id="detail_file_bt">查看附件</button>
            </div>
        <#include "projectEditInfo.ftl">
        <@projectEditInfo isEdit=false></@projectEditInfo>
        </form>
    </div>
</section>