<!DOCTYPE html>
<html lang="zh-CN" xmlns:ng="http://angularjs.org" id="ng-app" ng-app="myApp" ng-controller="indexCtrl">
<head>
<#assign path=request.contextPath/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>深圳市政府投资项目评审中心项目评审管理系统</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"/>
    <link rel="shortcut icon" href="${path}/imgs/favicon.ico">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${path}/libs/bootstrap/customize/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${path}/libs/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${path}/libs/ionicons/iconfont.css"/>
    <!-- Theme style -->
    <link rel="stylesheet" href="${path}/libs/adminLTE/css/AdminLTE.min.css"/>
    <!--<link rel="stylesheet" href="/contents/libs/adminLTE/dist/css/skins/skin-blue.min.css">-->
    <link rel="stylesheet" href="${path}/libs/adminLTE/css/skins/skin-green.min.css" rel="stylesheet"/>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
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

    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${path}/libs/adminLTE/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="${path}/libs/bootstrap-table/bootstrap-table.min.css">
    <link rel="stylesheet" href="${path}/libs/bootstrap-table/extensions/filter-odata/bootstrap-table-filter-odata.css">
    <link rel="stylesheet" href="${path}/libs/angular-loading-bar/loading-bar.min.css"/>
    <link rel="stylesheet" href="${path}/libs/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <!--uploadify-->
    <link rel="stylesheet" href="${path}/libs/uploadify/uploadify.css" rel="stylesheet"/>

    <!--Begin:自定义public css-->
    <link href="${path}/css/Transm.css" rel="stylesheet"/>
    <link href="${path}/css/hover-min.css" rel="stylesheet"/>
    <link href="${path}/css/share.css" rel="stylesheet"/>
    <link href="${path}/css/welcome.css" rel="stylesheet">

    <script type="text/javascript">
        //应用路径
        var contextPath = '${path}';
        var loginCaptcha = ${loginCaptcha?string("true","false")};
        //时间戳
        var ts = '${.now?string("yyyyMMddHHmmsss")}';
    </script>
</head>
<body class="hold-transition skin-blue-light fixed sidebar-mini" mouse-Scroll style="min-width: 1024px;">
<div id="loading-bar-container"></div>
<div class="wrapper">
    <div class="header">
        <header class="main-header">
            <!-- Logo -->
            <a class="logo">
                <img src="${path}/imgs/szlogo.png" alt="LOGO">
            </a>
            <!-- Header Navbar: style can be found in header.less -->
            <nav class="navbar navbar-static-top" role="navigation">
                <div class="navbar-custom-menu">
                    <a href="javascript:void(0);" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                        <span class="sr-only">收缩导航</span>
                    </a>

                    <ul class="nav navbar-nav">
                        <!-- User Account: style can be found in dropdown.less -->
                        <li class="dropdown user user-menu">
                            <!-- Menu Toggle Button -->
                            <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
                                <!-- The user image in the navbar-->
                                <img src="${path}/libs/adminLTE/img/avatar5.png" class="user-image"
                                     alt="User Image">
                                <!-- hidden-xs hides the username on small devices so only the image appears. -->
                                <span class="hidden-xs">${currentUser.username!''}</span>
                            </a>
                            <a class="logout" href="${path}/logout"
                               style="border-left:1px solid #eee;padding-right: 30px">
                                <span class="glyphicon glyphicon-log-out"></span>
                                <span>退出</span>
                            </a>
                            <ul class="dropdown-menu">
                                <!-- The user image in the menu -->
                                <li class="user-header">
                                    <img src="${path}/libs/adminLTE/img/avatar5.png" class="img-circle"
                                         alt="User Image">
                                    <p>
                                    ${currentUser.username!''}
                                        <small>${.now}</small>
                                    </p>
                                </li>
                                <!-- Menu Body -->
                                <li class="user-body">
                                    <div class="row">

                                    </div>
                                    <!-- /.row -->
                                </li>
                                <!-- Menu Footer-->
                                <li class="user-footer">
                                    <div class="pull-left">
                                        <a href="#/userInfo" class="btn btn-default btn-flat">个人信息</a>
                                    </div>
                                    <div class="pull-right">
                                        <a href="${path}/logout" class="btn btn-default btn-flat">退出</a>
                                    </div>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
    </div>
    <div style="height: 50px"></div>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar" id="leftMenuBox">
            <!-- sidebar menu: : style can be found in sidebar.less -->
        <#--<ul class="sidebar-menu" ng-repeat="x in MAIN_MENU" ng-show="menuBoxTitle==x.resName">
            <li class="active treeview">
                <a href="#/">
                    <i class="fa fa-home"></i>
                    <span>主页</span>
                </a>
            </li>

            <li class="active treeview">
                <a class="t-z-blue-dep" href="javascript:(0)">
                    <i class="{{x.resIcon||'fa fa-bars'}}"></i>
                    <span> &nbsp; {{x.resName}}</span>
                </a>
            </li>

            <li class="treeview active" ng-repeat="y in x.children" ng-if="!!x.children">
                <a class="" ng-href="{{y.resUri||'javascript:;'}}" target="{{y.target}}">
                    <i class="{{y.resIcon||'fa fa-bars'}}"></i>
                    <span>{{y.resName}}</span>
                    <span class="pull-right-container" ng-if="!!y.children">
                            <i class="fa fa-angle-left pull-right"></i>
                            </span>
                </a>
                <ul class="treeview-menu" ng-if="!!y.children">
                    <li ng-repeat="z in y.children">
                        <a ng-href="{{z.resUri||'javascript:;'}}" target="{{z.target}}">
                            <i class="{{z.resIcon||'fa fa-circle-o'}}"></i> {{z.resName}}
                        </a>
                    </li>
                </ul>
            </li>
        </ul>-->
        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper content-wrapper-all" ui-view style="padding-top: 5px;min-height:895px;"></div>
    <!-- /.content-wrapper
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>版本</b> 1.0.0
        </div>
        <strong>广西深宁科技有限公司 版权所有</strong>
    </footer>
    -->
</div>
<!-- ./wrapper -->

<!--angular1.2.30-->
<script src="${path}/libs/jsencrypt/jsencrypt.min.js"></script>
<script src="${path}/libs/jquery/jquery.min.js"></script>
<script src="${path}/libs/bootstrap/customize/js/bootstrap.min.js"></script>
<script src="${path}/libs/angular/angular.js"></script>
<script src="${path}/libs/angular-loading-bar/loading-bar.min.js"></script>
<script src="${path}/libs/angular/angular-ui-router.min.js"></script>
<script src="${path}/libs/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${path}/libs/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="${path}/libs/bootstrap-table/extensions/filter-odata/bootstrap-table-filter-odata.min.js"></script>
<script src="${path}/libs/bootstrap-table/extensions/angular/bootstrap-table-angular.min.js"></script>
<script src="${path}/libs/jquery-validation/jquery.validate.min.js"></script>
<script src="${path}/libs/jquery-validation/jquery.validate.unobtrusive.min.js"></script>
<script src="${path}/libs/slimScroll/jquery.slimscroll.min.js"></script>
<!--uploadify-->
<script src="${path}/libs/uploadify/jquery.uploadify.js"></script>
<!--uploadify-->

<!--datetimepicker-->
<script src="${path}/libs/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="${path}/libs/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<!--datetimepicker-->

<!-- <script>
  $.widget.bridge('uibutton', $.ui.button);
</script> -->
<!-- adminLTE app -->
<script src="${path}/libs/adminLTE/js/app.min.js"></script>
<!--App -->
<script src="${path}/app-dist/app.common.all.js?ts=${.now?string("yyyyMMhhhhmmsss")}"></script>
<script src="${path}/app-dist/app.all.js?ts=${.now?string("yyyyMMhhhhmmsss")}"></script>


</body>
</html>
