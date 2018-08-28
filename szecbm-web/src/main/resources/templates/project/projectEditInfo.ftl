<!--S_小型项目信息-->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<#macro projectEditInfo isEdit=true modelKey="model" >
<table class="table table-bordered table-striped">
    <tr>
        <td class="text-right" width="200">收文编号：<span class="text-red">(*)</span></td>
        <td>
            <input style="width:200px;" type="text" maxlength="300" class="form-control input-sm"
                   <#if !isEdit>disabled</#if>
                   id="fileCode" name="fileCode" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.fileCode">
            <span data-valmsg-for="fileCode" data-valmsg-replace="true" class="text-red"></span>
        </td>

        <td class="text-right" width="200">项目名称：<span class="text-red">(*)</span></td>
        <td>
            <input style="width:200px;" type="text" maxlength="300" class="form-control input-sm"
                   <#if !isEdit>disabled</#if>
                   id="projectName" name="projectName" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.projectName">
            <span data-valmsg-for="projectName" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>
    <tr>
        <td class="text-right">评审阶段：<span class="text-red">(*)</span></td>
        <td>
            <select class="form-control input-sm" style="width:200px;" ng-model="vm.${modelKey}.reviewStage"
                    <#if !isEdit>disabled</#if>
                    name="reviewStage" id="reviewStage" data-val="true" data-val-required="必填" >
            <option value="">---请选择---</option>
            <option value="项目建议书" ng-selected="'项目建议书' == vm.model.reviewStage">项目建议书</option>
            <option value="可行性研究报告" ng-selected="'可行性研究报告' == vm.model.reviewStage">可行性研究报告</option>
            <option value="项目概算" ng-selected="'项目概算' == vm.model.reviewStage">项目概算</option>
            <option value="资金申请报告" ng-selected="'资金申请报告' == vm.model.reviewStage">资金申请报告</option>
            <option value="进口设备" ng-selected="'进口设备' == vm.model.reviewStage">进口设备</option>
            <option value="设备清单（国产）" ng-selected="'设备清单（国产）' == vm.model.reviewStage">设备清单（国产）</option>
            <option value="设备清单（进口）" ng-selected="'设备清单（进口）' == vm.model.reviewStage">设备清单（进口）</option>
            <option value="其它" ng-selected="'其它' == vm.model.reviewStage">其它</option>
            </select>
            <span ng-show="vm.${modelKey}.reviewStage=='项目建议书'||vm.${modelKey}.reviewStage=='可行性研究报告'||vm.${modelKey}.reviewStage=='项目概算'||vm.${modelKey}.reviewStage=='资金申请报告'">
                    	<input class="text-right" type="checkbox" style="width:20px;"
                               ng-model="vm.${modelKey}.isAdvanced" name="isAdvanced" ng-true-value="9"
                               ng-false-value="0"> 提前介入
             </span>

            <span ng-show="vm.${modelKey}.reviewStage=='项目概算'" style="margin-left: 20px;">
                        <input class="text-right" type="checkbox" style="width:20px;"
                               ng-model="vm.${modelKey}.ischangeEstimate" name="ischangeEstimate" ng-true-value="9"
                               ng-false-value="0"> 是否调概
            </span>
            <span data-valmsg-for="reviewStage" data-valmsg-replace="true" class="text-red"></span>
        </td>
        <td class="text-right">项目单位：<span class="text-red">(*)</span></td>
        <td>
            <input style="width:200px;" type="text" maxlength="300" class="form-control input-sm"
                   <#if !isEdit>disabled</#if>
                   id="proUnit" name="proUnit" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.proUnit">
            <span data-valmsg-for="proUnit" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>

    <tr>
        <td class="text-right">评审部门：<span class="text-red">(*)</span></td>
        <td>
            <select class="form-control input-sm" style="width:200px;" ng-model="vm.${modelKey}.reviewDept"
                    <#if !isEdit>disabled</#if> id="reviewDept" name="reviewDept" data-val="true"
                    data-val-required="必填">
                <option value="">---请选择---</option>
                <option ng-repeat="x in vm.orgDeptList"
                        value="{{x.name}}" ng-selected="x.name == vm.model.reviewDept">
                    {{x.name}}
                </option>
            </select>
            <span data-valmsg-for="reviewDept" data-valmsg-replace="true" class="text-red"></span>
        </td>
        <td class="text-right">第一负责人：<span class="text-red">(*)</span></td>
        <td>
            <select class="form-control input-sm" style="width:200px;" ng-model="vm.${modelKey}.mainUser"
                    ng-change="vm.checkPrincipal();"
                    <#if !isEdit>disabled</#if> id="mainUser" name="mainUser" data-val="true" data-val-required="必填">
                <option value="">---请选择---</option>
                <option ng-repeat="x in vm.principalUsers" ng-if="x.username!= 'admin'"
                        ng-selected="x.userId == vm.model.mainUser" value="{{x.userId}}">
                    {{x.displayName}}
                </option>
            </select>
            <span data-valmsg-for="mainUser" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>

    <tr>
        <td class="text-right">其他负责人：</td>
        <td colspan="3">
            <ul id="principalUser_ul">
                <li ng-repeat="u in vm.principalUsers" id="principalUser" style="float: left;width: 80px;">
                    <input ng-if="u.username != 'admin'" type="checkbox" selectType="assistUser" tit="{{u.displayName}}"
                           value="{{u.userId}}"
                           ng-checked="vm.model.assistUser && (vm.model.assistUser).indexOf(u.userId)>-1"
                           ng-disabled="vm.model.mainUser == u.userId"/><span ng-if="u.username != 'admin'">{{u.displayName}}</span>
                </li>
            </ul>
        </td>
    </tr>

    <tr>
        <td class="text-right">发文日期：<span class="text-red">(*)</span></td>
        <td>
            <#if !isEdit>
                <input class="form-control" disabled type="text" id="dispatchDate" name="dispatchDate"
                       style="width: 200px;"
                       ng-model="vm.${modelKey}.dispatchDate">
            <#else>
                <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                    <input class="form-control date-input" size="16" type="text" id="dispatchDate" name="dispatchDate"
                           readonly ng-model="vm.${modelKey}.dispatchDate" data-val="true" data-val-required="必填">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
                <span data-valmsg-for="dispatchDate" data-valmsg-replace="true" class="text-red"></span>
            </#if>
        </td>

        <td class="text-right">发文号：<span class="text-red">(*)</span></td>
        <td>
            <input style="width:200px;" type="text" maxlength="150" class="form-control input-sm"
                   <#if !isEdit>disabled</#if>
                   id="fileNum" name="fileNum" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.fileNum">
            <span data-valmsg-for="fileNum" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>
    <tr>
        <td class="text-right">存档日期：<span class="text-red">(*)</span></td>
        <td>
            <#if !isEdit>
                <input class="form-control" disabled type="text" id="fileDate" name="fileDate" style="width: 200px;"
                       ng-model="vm.${modelKey}.fileDate">
            <#else>
                <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                    <input class="form-control date-input" size="16" type="text" id="fileDate" name="fileDate"
                           readonly ng-model="vm.${modelKey}.fileDate" data-val="true" data-val-required="必填">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
                <span data-valmsg-for="fileDate" data-valmsg-replace="true" class="text-red"></span>
            </#if>
        </td>

        <td class="text-right">存档号：<span class="text-red">(*)</span></td>
        <td>
            <input style="width:200px;" type="text" maxlength="150" class="form-control input-sm"
                   <#if !isEdit>disabled</#if>
                   id="fileNo" name="fileNo" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.fileNo">
            <span data-valmsg-for="fileNo" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>

    <tr>
        <td class="text-right">备注：</td>
        <td colspan="3">
            <textarea class="form-control" maxlength="500" style="height:121px;width: 800px;"
                      placeholder="注：备注（500字符以内）" ng-model="vm.${modelKey}.remark"
                      <#if !isEdit>disabled<#else>ng-change="vm.checkLength(vm.${modelKey}.remark,500,'remarkTips')"</#if>
            ></textarea>
            <#if isEdit>
                <div class="tipfont">您还可以输入<span id="remarkTips"><font size="5">500</font></span>个字符!</div>
            </#if>
        </td>
    </tr>

</table>


<!--S 上传附件弹窗 -->
<div id="commonUploadWindow" style="margin:0px 20px 0px 10px;background:white;display:none;">
    <span style="margin-top: 20px;"></span>
    <input type="file" id="sysfileinput" name="file" multiple="multiple" class="file-loading"/>
</div>
<!--E 上传附件弹窗 -->


<!-- S 附件上传列表 -->
<div id="commonQueryWindow" class="well well-sm" style="background:white;display:none;">
    <p>
    </p>
    <table class="table table-bordered">
        <tr>
            <td style="width: 250px;" align="center" bgcolor="#eeeeee">文件名称</td>
            <td style="width: 80px;" align="center" bgcolor="#eeeeee">文件大小</td>
          <#--  <td style="width: 120px;" align="center" bgcolor="#eeeeee">附件类型</td>-->
            <td style="width: 100px;" align="center" bgcolor="#eeeeee">上传人</td>
            <td style="width: 150px;" align="center" bgcolor="#eeeeee">时间</td>
            <td style="width: 100px;" align="center" bgcolor="#eeeeee">操作</td>
        </tr>
        <tr ng-repeat="x in vm.sysFilelists ">
            <td >
                <i class="fa fa-file-image-o" aria-hidden="true" ng-show="x.fileType =='.png' || x.fileType =='.jpg' || x.fileType =='.gif'"></i>
                <i class="fa fa-file-word-o" aria-hidden="true" ng-show="x.fileType =='.docx' || x.fileType =='.doc'"></i>
                <i class="fa fa-file-excel-o" aria-hidden="true" ng-show="x.fileType =='.xlsx' || x.fileType =='.xls'"></i>
                <i class="fa fa-file-pdf-o" aria-hidden="true" ng-show="x.fileType =='.pdf'"></i>
                <a ng-click="vm.commonDownloadSysFile(x.sysFileId)">{{ x.showName }}</a>
            </td>
            <td style="text-align: center;">{{ x.fileSizeStr }}</td>
        <#--    <td style="text-align: center;">{{ x.sysBusiType }}</td>-->
            <td style="text-align: center;">{{ x.createdBy }}</td>
            <td style="text-align: center;">{{ x.createdDate }}</td>
            <td style="text-align: center;">
                <button ng-click="vm.downloadSysFile(x.sysFileId)" id="linksbtn" class="btn btn-xs btn-primary"><i class="fa fa-cloud-download" aria-hidden="true"></i>下载</button>
                <button   <#if !isEdit>style="display: none" </#if> class="btn btn-xs btn-danger" ng-click="vm.delSysFile(x.sysFileId)"><i class="fa fa-times" aria-hidden="true"></i>删除</button>
            </td>
        </tr>
    </table>
</div>
<!-- E 附件上传列表 -->
</#macro>