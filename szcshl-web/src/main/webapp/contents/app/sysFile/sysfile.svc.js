(function () {
    'use strict';

    angular.module('app').factory('sysfileSvc', sysfile);

    sysfile.$inject = ['$http', 'bsWin','$state'];
    function sysfile($http, bsWin,$state) {
        var service = {
            initUploadOptions: initUploadOptions,       // 初始化上传附件控件
            delSysFile: delSysFile,                     // 删除系统文件
            downloadFile: downloadFile,                 // 系统文件下载
            queryPluginfile: queryPluginfile,           // 查询系统安装包
            findByMianId: findByMianId,                 // 根据主业务ID获取所有的附件信息
            findByBusinessId: findByBusinessId,         // 根据业务ID 获取上传附件
            mainTypeValue: mainTypeValue,               // 各大模块附件根目录
            previewFile: previewFile,                   // pdf 预览
            initZtreeClient: initZtreeClient,
            queryFile:queryFile

        };
        return service;

        function previewFile() {
            //alert(12);
        }

        // 各大模块附件根目录(跟后台Constant.SysFileMainType 同步)
        function mainTypeValue() {
            return {
                SIGN: "项目附件",
                FILLSIGN: "审批登记",
                HUMAN: "人事附件",
                BOOKS: "图书附件",
                NOTICE: "通知公告",
                SHARE: "资料共享",
                MEETTINGROOM: "会议室预定",
                WORKPROGRAM: "工作方案",
                DISPATCH: "发文",
                DOFILE: "归档",
                MEETING: "会前准备材料",
                SUPPLEMENT: "补充函",
                STAGEMEETING: "评审会会议",
                FILELIBRARY: "质量管理文件库",
                POLICYLIBRARY: "政策标准库",
                TOPIC: "课题附件",
                TOPIC_PLAN: "课题计划书",
                TOPIC_WORKPLAN: "课题工作方案",
                TOPIC_FILING: "课题归档",
                MONTH_FILE: "月报简报",
                AADSUPP_FILE: "拟补充资料函"
            }
        }

        // 系统文件下载
        function downloadFile(id) {
            window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
        }

        //根据主业务获取所有的附件信息
        function findByBusinessId(businessId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/file/findByBusinessId",
                params: {
                    businessId: businessId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //根据主业务获取所有的附件信息
        function findByMianId(mainId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/file/findByMainId",
                params: {
                    mainId: mainId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //根据点击的文件查询
        function queryFile(mainId,type, callBack) {
            var httpOptions ={
                method : 'post',
                url : rootPath+"/file/queryFile",
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                data: $.param({mainId: mainId, sysBusiType:type}),
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        // S 删除系统文件,自己实现回调方法
        function delSysFile(sysFileId, callBack) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/file/deleteSysFile",
                params: {
                    id: sysFileId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // E 删除系统文件

        // S 初始化上传附件控件
        /**
         * options 属性 options.vm.sysFile 一定要有，这个是附件对象
         *  uploadBt : 上传按钮
         *  detailBt : 查看按钮
         *  inputId : "sysfileinput",
         *  mainType : 主要业务模块，业务的根目录
         * @param options
         */
        function initUploadOptions(options) {
            options.vm.initUploadOptionSuccess = false;
            //options.vm.sysFile 为定义好的附件对象
            var sysFileDefaults = {
                width: "70%",
                height: "460px",
                uploadBt: "upload_file_bt",
                detailBt: "detail_file_bt",
                inputId: "sysfileinput",
                mainType: "没有归类附件",
                sysBusiType: "",
                showBusiType: true,
            };
            if (!options.vm.sysFile) {
                bsWin.alert("初始化附件控件失败，请先定义附件对象！");
                return;
            }
            if (options.sysBusiType) {
                sysFileDefaults.sysBusiType = options.sysBusiType;
            }
            if (options.width) {
                sysFileDefaults.width = options.width;
            }
            if (options.height) {
                sysFileDefaults.height = options.height;
            }

            //是否显示业务下来框
            if (angular.isUndefined(options.vm.sysFile.showBusiType)) {
                options.vm.sysFile.showBusiType = sysFileDefaults.showBusiType;
            }

            //附件下载方法
            options.vm.downloadSysFile = function (id) {
                downloadFile(id);
            }
            //附件删除方法
            options.vm.delSysFile = function (id) {
                delSysFile(id, function () {
                    bsWin.alert("删除成功！");
                    $.each(options.vm.sysFilelists, function (i, sf) {
                        if (sf.sysFileId == id) {
                            options.vm.sysFilelists.splice(i, 1);
                        }
                    })
                });
            }
            options.vm.clickUploadBt = function () {
                if (!options.vm.sysFile.businessId) {
                    bsWin.alert("请先保存业务数据！");
                } else {
                    $("#commonUploadWindow").kendoWindow({
                        width: sysFileDefaults.width,
                        height: sysFileDefaults.height,
                        title: "附件上传",
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "Close"]
                    }).data("kendoWindow").center().open();
                }
            }

            options.vm.clickDetailBt = function () {
                if (!options.vm.sysFile.businessId) {
                    bsWin.alert("请先保存业务数据！");
                    return;
                } else {
                    findByBusinessId(options.vm.sysFile.businessId, function (data) {
                        options.vm.sysFilelists = [];
                        options.vm.sysFilelists = data;
                        $("#commonQueryWindow").kendoWindow({
                            width: "800px",
                            height: "500px",
                            title: "附件上传列表",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    });
                }
            }
            //有业务数据才能初始化
            if (options.vm.sysFile.businessId) {
                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['sql', 'exe','lnk'],//修改过，改为了不支持了。比如不支持.sql的
                    maxFileSize: 5000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",// 默认上传ftp服务器 /file/fileUploadLocal 为上传到本地服务
                    uploadExtraData: function (previewId, index) {
                        var result = {};
                        result.businessId = options.vm.sysFile.businessId;
                        result.mainId = options.vm.sysFile.mainId;
                        result.mainType = options.vm.sysFile.mainType || sysFileDefaults.mainType;
                        result.sysfileType = options.vm.sysFile.sysfileType;
                        result.sysBusiType = options.vm.sysFile.sysBusiType || sysFileDefaults.sysBusiType;
                        return result;
                    }
                };

                var filesCount = 0;
                $("#" + options.inputId || sysFileDefaults.inputId).fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {
                        filesCount = files.length;
                    }).on("fileuploaded", function (event, data, previewId, index) {
                    projectfileoptions.sysBusiType = options.vm.sysFile.sysBusiType;
                    if (filesCount == (index + 1)) {
                        if (options.uploadSuccess != undefined && typeof options.uploadSuccess == 'function') {
                            options.uploadSuccess(event, data, previewId, index);
                        }
                    }
                });
                //表示初始化控件成功
                options.vm.initUploadOptionSuccess = true;
            }
        }

        // E 初始化上传附件控件

        // S 系统安装包管理
        function queryPluginfile(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/file/getPluginFile"),
                schema: common.kendoGridConfig().schema(),
                serverPaging: false,
                serverSorting: true,
                serverFiltering: true,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "fileName",
                    title: "名称",
                    filterable: false
                },
                {
                    field: "fileLength",
                    title: "大小",
                    width: 160,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), rootPath + "/" + item.relativePath);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
        }// E queryPluginfile

        function initZtreeClient(vm,$scope) {
            var zTreeObj;
            var setting = {
                check: {
                    chkboxType: {"Y": "ps", "N": "ps"},
                    enable: false
                },
                callback: {
                    onClick: zTreeOnClick
                }
            };

            var array = vm.sysFileList;
            vm.zNodes = [];
            //循环数据取出父类和相对应的子类
            for (var j = 0; j < array.length; j++) {
                if(array[j].sysBusiType==undefined ||array[j].sysBusiType==""){
                    var name="其他文件";
                }else{
                    var name = array[j].sysBusiType;
                }
                var nodes = new Object();//定义父类的对象
                nodes.id=array[j].mainId;
                nodes.name = name;
                var ss = [];//定义子类对象数组
                for (var i = 0; i < array.length;) {
                    if (array[i].sysBusiType == name) {//判断是否是属于父类
                        var s = new Object();
                        s.name = array[i].showName;
                        s.id = array[i].mainId;
                        s.sysFileId=array[i].sysFileId;
                        ss.push(s);
                        array.splice(i, 1);
                        j = -1;//让索引都是从0重新开始
                    } else {
                        i++;
                    }
                }
                nodes.children = ss;
                vm.zNodes.push(nodes);

            }
            $scope.$watch("vm.zNodes",function (newValue, oldValue) {
                setTimeout(function() {//页面的ID可能没有加载完成
                    zTreeObj = $.fn.zTree.init($("#zTree"), setting, vm.zNodes);
                },500)
            },true);
        }// end fun initZtreeClient
         //点击跳转
        function zTreeOnClick(event, treeId, treeNode) {
            if(treeNode.check_Child_State==0){//点击文件夹时展开列表
                var test = window.location.hash;//获取到地址栏的参数从#开始获取
                var arr=[];
                arr= test.split("/");//截取字符串是数组的
                if(treeNode.check_Child_State==0){//点击文件夹时展开列表
                    $state.go(arr[1]+'.fileList', { id: treeNode.id,type:treeNode.name});//获第二个参数
                }
            }
        };

        // end common fun

    }
})();