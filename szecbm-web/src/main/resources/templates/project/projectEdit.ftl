<!-- Content Header (Page header) -->
<section class="content-header">
    <h1>
        项目编辑
    </h1>
    <ol class="breadcrumb">
        <li class="active">项目管理</li>
        <li class="active">项目填报</li>
        <li class="active">项目编辑</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="well well-sm" style="background:white;">
        <form id="form" name="form" ng-init="vm.model.fillDate = '${.now?string("yyyy-MM-dd")}'">
            <div class="toolbar" >
                <button class="btn btn-sm btn-primary" ui-sref="projectManage">
                    <span class="glyphicon glyphicon-chevron-left"></span> 返回
                </button>
                <button class="btn btn-sm btn-success" ng-click="vm.save()" >
                    <span class="glyphicon glyphicon-ok"></span> 保存
                </button>
                <button class="btn btn-sm btn-success" ng-click="vm.save()" ng-show="vm.isSubmit">
                    <span class="glyphicon glyphicon-ok"></span> 更新
                </button>
                <button class="btn btn-sm btn-primary" ng-click="vm.clickUploadBt();" id="upload_file_bt">上传附件</button>
                <button class="btn btn-sm btn-primary" ng-click="vm.clickDetailBt();" id="detail_file_bt">查看附件</button>
            </div>

        <#include "projectEditInfo.ftl">
        <@projectEditInfo isEdit=true></@projectEditInfo>
        </form>
    </div>
</section>