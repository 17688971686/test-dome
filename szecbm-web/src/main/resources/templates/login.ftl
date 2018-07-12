<!DOCTYPE html>
<html lang="zh-CN" xmlns:ng="http://angularjs.org">
<head>
    <#assign path=request.contextPath/>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>用户登录</title>
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${path}/libs/bootstrap/customize/css/bootstrap.min.css">
    <link rel="stylesheet" href="${path}/css/login.css">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <!--IE兼容性处理-->
    <script type="text/javascript" src="${path}/libs/bootstrap/customize/js/respond.min.js"></script>
    <script type="text/javascript" src="${path}/libs/bootstrap/customize/js/html5.js"></script>
    <!--[if lte IE 7]>
    <script src="${path}/libs/json2.js"></script>
    <![endif]-->

</head>
<body style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${path}/imgs/loginpage-bg.jpg', sizingMethod=scale);">
<div class="login-container">
    <div class="logotitle" style="text-align: center;">
        <img src="${path}/imgs/loginpage-title.png" alt="">
    </div>
    <img class="logo" src="${path}/imgs/logo.png" alt="LOGO">
    <div class="loginform" id="ng-app" ng-app="loginApp" ng-controller="loginCtrl as vm">
        <form method="post" id="form" name="form" ng-keypress="vm.keyEnter($event)" ng-keypress="vm.keyEnter($event)"
              action="${path}/login">
            <h3>用户登录</h3>
            <div class="input-container" style="margin-bottom:10px;">
                <h4>账号</h4>
                <input type="text" autofocus="autofocus" class="form-control" maxlength="100" name="username"
                       id="username" ng-model="vm.model.username" data-val="true" data-val-required="用户名必填">
                <p data-valmsg-for="username" data-valmsg-replace="true" class="text-red"></p>
            </div>
            <div class="input-container" style="margin-bottom:10px;">
                <h4>密码</h4>
                <input type="password" class="form-control" maxlength="100" name="password"
                       id="password" ng-model="vm.model.password" data-val="true" data-val-required="密码必填">
                <p data-valmsg-for="password" data-valmsg-replace="true" class="errors"></p>
            </div>
        </form>
        <div class="loginform-footer" style="">
            <label class="remember">
                <input type="checkbox">
                <span>记住登陆状态</span>
            </label>
            <button ng-click="vm.submit()" ng-disabled="vm.isSubmit" class="btn btn-success" type="submit" value="登录">
                登录
            </button>
        </div>
        <span ng-show="vm.message" class="errors" style="color:#f00" ng-bind="vm.message">${msg!''}</span>
    </div>
</div>
<div class="footer"> 深圳市政府投资项目评审中心版权所有</div>

<script src="${path}/libs/jsencrypt/jsencrypt.min.js"></script>
<script src="${path}/libs/jquery/jquery.min.js"></script>
<script src="${path}/libs/bootstrap/customize/js/bootstrap.min.js"></script>
<script src="${path}/libs/angular/angular.min.js"></script>
<script src="${path}/libs/jquery-validation/jquery.validate.min.js"></script>
<script src="${path}/libs/jquery-validation/jquery.validate.unobtrusive.min.js"></script>
<script src="${path}/app-dist/app.common.all.js?ts=${.now?string("yyyyMMhhhhmmsss")}"></script>
<script>
    (function () {
        'use strict';
        angular.module('loginApp', ["sn.common"]).controller('loginCtrl', function ($http) {
            var vm = this;
            vm.isSubmit = false;
            vm.message = "${msg!''}";
            vm.model = {};
            var encrypt = new JSEncrypt();
            vm.submit = function () {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;

                    // 去掉空格
                    vm.model.username = vm.model.username.replace(/\s+/g, "");
                    vm.model.password = vm.model.password.replace(/\s+/g, "");

                    $http.get("${path}/rsaKey").success(function (data) {
                        encrypt.setPublicKey(data || "");
                        vm.model.password = encrypt.encrypt(vm.model.password);

                        $http({
                            url: "${path}/login",
                            method: 'POST',
                            data: $.param(vm.model),
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).then(function () {
                            vm.isSubmit = false;
                            window.location.href = "${path}/index";
                        }, function () {
                            vm.isSubmit = false;
                            vm.model.password = "";
                            vm.reloadVerify && vm.reloadVerify();
                        })
                    })
                }
            }
            vm.keyEnter = function (e) {
                if (e.which === 13) {
                    vm.submit()
                }
            }
        })
    })();
</script>

</body>
</html>