<!--S_小型项目信息-->
<meta charset="UTF-8">
<#assign path=request.contextPath/>
<#macro projectEditInfo isEdit=true modelKey="model">
<table  class="table table-bordered table-striped">
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
                   ng-model="vm.${modelKey}.projectName" >
            <span data-valmsg-for="projectName" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>
    <tr>
        <td class="text-right">评审阶段：<span class="text-red">(*)</span></td>
        <td>
            <select class="form-control input-sm" style="width:200px;" ng-model="vm.${modelKey}.reviewStage"
                    <#if !isEdit>disabled</#if>
                    name="reviewStage" id="reviewStage" data-val="true" data-val-required="必填">
                <option value="">---请选择---</option>
                <option value="1">---项目建议书---</option>
                <option value="2">---可行性研究报告---</option>
                <option value="3">---资金申请报告---</option>
            </select>
            <span data-valmsg-for="reviewStage" data-valmsg-replace="true" class="text-red"></span>
        </td>
        <td class="text-right">项目单位：<span class="text-red">(*)</span></td>
        <td>
            <input style="width:200px;" type="text" maxlength="300" class="form-control input-sm"
                   <#if !isEdit>disabled</#if>
                   id="proUnit" name="proUnit" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.proUnit" >
            <span data-valmsg-for="proUnit" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>

    <tr>
        <td class="text-right">评审部门：<span class="text-red">(*)</span></td>
        <td>
            <select   class="form-control input-sm" style="width:200px;" ng-model="vm.${modelKey}.reviewDept"
                    <#if !isEdit>disabled</#if> id="reviewDept" name="reviewDept" data-val="true" data-val-required="必填">
                <option value="">---请选择---</option>
<#--                <option ng-repeat="x in vm.deptAllArr" ng-selected="vm.${modelKey}.implementDept == x.organName" value="{{x.organName}}" >{{x.organName}}</option>-->
                <option value="">---请选择---</option>
                <option value="1">---评估一部---</option>
                <option value="2">---评估二部---</option>
                <option value="3">---概算一部---</option>
            </select>
            <span data-valmsg-for="reviewDept" data-valmsg-replace="true" class="text-red"></span>
        </td>
        <td class="text-right">项目负责人：<span class="text-red">(*)</span></td>
        <td>
            <div class="input-group" style="width: 200px;">
                <input class="form-control input-sm" <#if !isEdit>disabled<#else>type="text"</#if>
                       id="mUserId" name="mUserId" data-val="true" data-val-required="必填"
                       ng-model="vm.${modelKey}.mUserId">
            </div>
            <span data-valmsg-for="mUserId" data-valmsg-replace="true" class="text-red"></span>
        </td>
    </tr>

    <tr>
        <td class="text-right">发文日期：<span class="text-red">(*)</span></td>
        <td>
            <#if !isEdit>
                <input class="form-control" disabled type="text" id="dispatchDate" name="dispatchDate" style="width: 200px;"
                       ng-model="vm.${modelKey}.dispatchDate">
            <#else>
                <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
                    <input class="form-control" size="16" type="text" id="dispatchDate" name="dispatchDate"
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
                   id="fileCode" name="fileCode" style="width: 200px;" data-val="true" data-val-required="必填"
                   ng-model="vm.${modelKey}.fileCode">
            <span data-valmsg-for="fileCode" data-valmsg-replace="true" class="text-red"></span>
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
                    <input class="form-control" size="16" type="text" id="fileDate" name="fileDate"
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
</#macro>