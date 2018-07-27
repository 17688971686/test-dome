<!DOCTYPE html>
<html lang="zh-CN" xmlns:ng="http://angularjs.org">
<head>
<#assign path=request.contextPath/>
    <meta charset="UTF-8">
    <title>用户登录</title>
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${path}/libs/bootstrap/customize/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${path}/libs/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${path}/libs/ionicons/iconfont.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${path}/libs/adminLTE/css/AdminLTE.min.css">
    <!--<link rel="stylesheet" href="/contents/libs/adminLTE/dist/css/skins/skin-blue.min.css">-->
    <link href="${path}/libs/adminLTE/css/skins/skin-green.min.css" rel="stylesheet"/>

    <link rel="shortcut icon" href="${path}/imgs/favicon.ico">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <!--IE兼容性处理-->
    <script type="text/javascript" src="${path}/libs/bootstrap/customize/js/respond.min.js"></script>
    <script type="text/javascript" src="${path}/libs/bootstrap/customize/js/html5.js"></script>
    <script type="text/javascript">
        document.createElement('ng-include');
        document.createElement('ng-pluralize');
        document.createElement('ng-view');

        // Optionally these for CSS
        document.createElement('ng:include');
        document.createElement('ng:pluralize');
        document.createElement('ng:view');

        if (!Function.prototype.bind) {
            Function.prototype.bind = function (oThis) {
                if (typeof this !== "function") {
                    throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
                }
                var aArgs = Array.prototype.slice.call(arguments, 1),
                        fToBind = this,
                        fNOP = function () {
                        },
                        fBound = function () {
                            return fToBind.apply(this instanceof fNOP && oThis
                                            ? this
                                            : oThis,
                                    aArgs.concat(Array.prototype.slice.call(arguments)));
                        };
                fNOP.prototype = this.prototype;
                fBound.prototype = new fNOP();
                return fBound;
            };
        }
    </script>
    <![endif]-->
    <!--[if lte IE 7]>
    <script src="${path}/libs/json2.js"></script>
    <![endif]-->

    <link href="${path}/css/share.css" rel="stylesheet"/>

    <!--End:自定义独立login页面css -->
    <link href="${path}/login/css/login-regist.css" rel="stylesheet"/>

    <!--Begin:自定义public css-->
    <link href="${path}/css/Transm.css" rel="stylesheet"/>
    <link href="${path}/css/hover-min.css" rel="stylesheet"/>
    <style>
        .login-form {
            margin-top: 20px;
            height: 155px;
            background: url(${path}/login/image/login-form-bg.jpg) no-repeat center top/100%;
        }

        .login-form .box-wrap {
            padding: 5% 0 0 8%;
            text-align: center;
        }

        .login-form label {
            position: relative;
            margin-right: 20px;
            width: 20%;
        }

        .login-form label > span {
            position: absolute;
            right: 10px;
            top: -20px;
            text-align: left;
        }

        .login-form label > span:first-child {
            left: 10px;
            top: 10px;
            width: 60px;
        }

        .login-form input {
            display: inline-block;
            padding-left: 68px;
            width: 100%;
            height: 40px;
            background: #e9f2fb;
            border: 1px solid #fff;
            border-radius: 10px;
            box-shadow: 0 0 12px #d4dce4 inset;
            outline: none
        }

        .login-form label:hover {
            cursor: text;
        }

        .login-form input[type='password'] {
            padding-left: 55px;
        }

        .login-form input:focus {
            box-shadow: 0 0 5px #333;
        }

        .adminlogin-loginbtn {
            width: 120px;
            height: 40px;
            border: none;
            background: url(${path}/login/image/adminlogin-loginbtn-bg.png) repeat-x;
            color: #fff;
            margin-right: 10px;
        }
    </style>
</head>
<body class="login-body ">
<div style="text-align: center;margin-top: 30px;padding:0 50px;">
    <a href="">
        <img src="${path}/login/image/szlogo.png" alt="">
    </a>
    <a href="">
        <img src="${path}/login/image/loginpage-title.png" alt=""/>
    </a>
    <div class="pull-right" style="padding-top:20px;color:#fff">
        <span>我还没有业主账号 | </span><a class="t-z-white" href="${path}/owner/register">注册 <img
            src="${path}/login/image/gosrc.png"/> </a>
    </div>
</div>

<div class="login-form col-md-8 col-md-offset-2" id="ng-app" ng-app="loginApp" ng-controller="loginCtrl as vm">
    <form method="post" id="form" name="form" ng-keypress="vm.keyEnter($event)" action="${path}/login">
        <div class="box-wrap">
            <label for="username">
                <span>用户名：</span>
                <input type="text" autofocus="autofocus" class="form-control" maxlength="100" name="username"
                       id="username" ng-model="vm.model.username" data-val="true" data-val-required="用户名必填">
                <span data-valmsg-for="username" data-valmsg-replace="true" class="text-red"></span>
            </label>
            <label for="password">
                <span>密码：</span>
                <input type="password" class="form-control" maxlength="100" name="password"
                       id="password" ng-model="vm.model.password" data-val="true" data-val-required="密码必填">
                <span data-valmsg-for="password" data-valmsg-replace="true" class="text-red"></span>
            </label>
        <#if loginCaptcha?? && loginCaptcha=true>
            <label for="captcha" style="width: 30%;text-align: left;">
                <input style="width: 40%;margin-right: 10px;padding-left:10px;" type="text" class="form-control"
                       placeholder="验证码" maxlength="10" name="captcha"
                       id="captcha" ng-model="vm.model.captcha" data-val="true" data-val-required="验证码必填"/>
                <img style="pull-right" class="cursor-hand" id="verify" width="40%" height="40"
                     ng-click="vm.reloadVerify()"
                     ng-src="{{vm.captchaImagePath}}"/>
                <span style="left: 30px;" data-valmsg-for="captcha" data-valmsg-replace="true" id="captchaError"
                      class="text-red"></span>
            </label>
        </#if>
            <label for="" style="text-align: left;">
                <button class="adminlogin-loginbtn" type="button" ng-click="vm.submit()" ng-disabled="vm.isSubmit">登录
                </button>
            </label>
            <span ng-show="vm.message" class="errors text-red" ng-bind="vm.message"></span>
        </div>
    <#-- <#if loginCaptcha?? && loginCaptcha=true>
         <div>
             <span data-valmsg-for="captcha" data-valmsg-replace="true" class="text-red"></span>
         </div>
         <div class="form-group has-feedback">
             <input type="text" class="form-control" placeholder="验证码" maxlength="10" name="captcha"
                    id="captcha" ng-model="vm.model.captcha" data-val="true" data-val-required="验证码必填"/>

         </div>
         <div class="m5">
             <label class="code_word" ng-click="vm.reloadVerify()">看不清，换一张</label>
             <img class="cursor-hand" id="verify" width="100" height="50" ng-click="vm.reloadVerify()"
                  ng-src="{{vm.captchaImagePath}}"/>
         </div>
     </#if>
-->
    </form>
</div>
<#--<div class="top m">
    <div class="col-xs-4"><img src="${path}/login/image/logo.png"/></div>
    <div class="col-xs-8 t-right t-z-white">
        <span>我还没有业主账号 | </span><a class="t-z-white" href="${path}/owner/register">注册 <img
            src="${path}/login/image/gosrc.png"/> </a>
    </div>
</div>-->

<#--<div class="login-box shadow box-hide round-3" id="ng-app" ng-app="loginApp" ng-controller="LoginCtrl as vm">
    <div class="login-logo">管理平台</div>
    <!-- /.login-logo &ndash;&gt;
    <div class="login-box-body">

        <form method="post" id="form" name="form" ng-keypress="vm.keyEnter($event)" action="${path}/login">
            <div>
                <span ng-show="vm.message" class="text-red" ng-bind="vm.message"></span>
            </div>

            <div>
                <span data-valmsg-for="username" data-valmsg-replace="true" class="text-red"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" class="form-control" placeholder="用户名" maxlength="100" name="username"
                       id="username" ng-model="vm.model.username" data-val="true" data-val-required="用户名必填">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>

            <div>
                <span data-valmsg-for="password" data-valmsg-replace="true" class="text-red"></span>
            </div>
            <div class="form-group has-feedback">

                <input type="password" class="form-control" placeholder="密码" maxlength="100" name="password"
                       id="password" ng-model="vm.model.password" data-val="true" data-val-required="密码必填">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>

        <#if loginCaptcha?? && loginCaptcha=true>
            <div>
                <span data-valmsg-for="captcha" data-valmsg-replace="true" class="text-red"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" class="form-control" placeholder="验证码" maxlength="10" name="captcha"
                       id="captcha" ng-model="vm.model.captcha" data-val="true" data-val-required="验证码必填"/>

            </div>
            <div class="m5">
                <label class="code_word" ng-click="vm.reloadVerify()">看不清，换一张</label>
                <img class="cursor-hand" id="verify" width="100" height="50" ng-click="vm.reloadVerify()"
                     ng-src="{{vm.captchaImagePath}}"/>
            </div>
        </#if>

            <div class="row">

                <!-- /.col &ndash;&gt;
                <div class="pull-right col-sm-12">
                    <button type="button" ng-click="vm.submit()" ng-disabled="vm.isSubmit"
                            class="btn btn-primary btn-block btn-flat p10 b-yellowGreen butt-shadow">登 录
                    </button>
                    <br>
                </div>
                <!-- /.col &ndash;&gt;
            </div>
        </form>
    </div>
    <!-- /.login-box-body &ndash;&gt;
</div>-->
<!-- /.login-box -->
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
            /* jshint validthis:true */
            var vm = this;
            vm.isSubmit = false;
            vm.message = "${msg!''}";
            vm.model = {};

            var encrypt = new JSEncrypt();
            vm.submit = function () {
                util.initJqValidation();
                var isValid = $('form').valid();
            <#if loginCaptcha?? && loginCaptcha=true>
                if (!(vm.model.captcha || vm.model.captcha.length == 4)) {
                    $("#captchaError").html("验证码错误");
                    return false;
                }
            </#if>
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
        <#if loginCaptcha?? && loginCaptcha=true>
            vm.captchaImagePath = "${path}/captchaImage";
            vm.reloadVerify = function () {
                vm.model.captcha = "";
                vm.captchaImagePath = "${path}/captchaImage?" + Math.random();
            }
        </#if>
        })
    })();

</script>


</body>
</html>