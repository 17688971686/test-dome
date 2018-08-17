<#assign path=request.contextPath/>
<meta charset="UTF-8">

<section class="content-header">
    <h1>
        文档库
    </h1>
    <ol class="breadcrumb">
        <li><a href="admin/index"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">文档库</li>
    </ol>
</section>

<section class="content">
<div class="well well-sm" style="background:white;">
    <form id="form" name="form" class="form-inline">
        <div id="toolbar">
        <button type="button" class="btn btn-sm btn-primary" ng-click="backPrevPage('attachment')">
            <span class="glyphicon glyphicon-chevron-left"></span> 返回
        </button>
        <button type="button" class="btn btn-sm btn-success" ng-click="vm.save()">
            <span class="glyphicon glyphicon-floppy-saved"></span> 保存
        </button>
    </div>
        <table class="table table-bordered table-striped table-condensed">
          <#--  <tr>
                <td class="formAlignRight" style="width:150px;"></td>
                <td style="width:400px;"><span class="errors">{{vm.message}}</span></td>
                <td></td>
            </tr>-->

            <tr >
                <td style="width: 200px" class="text-right">选择文档： <span class="required" style="color: red;">*</span></td>
                <td class="text-left">
                    <input name="files" type="file" id="orginalFiles">
                    <span class="p1" style="float: left;margin-top: -40px;margin-left: 155px;color: red;">此类型附件仅支持(*.pdf;*.txt;*.png;*.doc;*png;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.ceb)</span>
                </td>
            </tr>
            <tr>
                <td style="width: 200px" class="text-right">文档名称： <span class="required" style="color: red;">*</span></td>
                <td class="text-left">
                    <input  type="text" maxlength="200" class="form-control input-sm " style="width: 400px;"
                            ng-model="vm.attachment.originalName " id="originalName" name="originalName"
                            data-val="true" data-val-required="必填" >
                    <span data-valmsg-for="originalName" data-valmsg-replace="true" class="text-red"
                          style="color: red"></span>
                </td>
            </tr>
            <tr>
                <td style="width: 200px" class="text-right">文档分类： <span class="required" style="color: red;">*</span> </td>
                <td class="text-left">
                    <select ng-model = "vm.attachment.docCategory" class="form-control input-sm "  name = "docCategory" data-val="true" data-val-required="必填">
                        <option value="">--请选择--</option>
                        <option ng-repeat="x in DICT.ATTACHMENT.dicts.CATEGORY.dictList"
                                value="{{x.dictKey}}">
                            {{x.dictName}}
                        </option>
                    </select>
                    <span data-valmsg-for="docCategory" data-valmsg-replace="true" class="text-red"
                          style="color: red"></span>
                </td>
            </tr>
              <tr>
                  <td class="text-right" style="width:150px;">是否公开给业主：</td>
                  <td class="text-left" colspan="2">
                      <label>
                          <input type="radio" ng-model="vm.attachment.publicAtt" name="publicAtt" id="publicAtt1" ng-value="true"> 是
                      </label>
                      <label>
                          <input type="radio" ng-model="vm.attachment.publicAtt" name="publicAtt"  id="publicAtt2" ng-value="false">否
                      </label>
                  </td>
              </tr>
            <tr>
                <td style="width: 200px" class="text-right">排序： </td>
                <td class="text-left">
                    <input  type="number" maxlength="200" class="form-control input-sm " style="width: 400px;"
                            ng-model="vm.attachment.itemOrder" id="itemOrder" name="itemOrder">
                </td>
            </tr>
        </table>
    </form>
</div>
</section>