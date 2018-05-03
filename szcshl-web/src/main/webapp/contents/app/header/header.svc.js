(function(){
    'use strict';
    angular.module('app').factory('headerSvc' , header);
    header.$inject=['$http','bsWin'];

    function header($http , bsWin){

        var service={
            headerGrid : headerGrid,
            createHeader : createHeader ,//创建表头
            findHeaderListNoSelected : findHeaderListNoSelected ,//获取表头列表
            updateSelectedHeader : updateSelectedHeader ,//改变表头状态（改为选中）
            updateCancelHeader : updateCancelHeader , //改变表头状态（改为未选中）
            findHeaderListSelected : findHeaderListSelected,//查询已选的表头
            deleteHeader : deleteHeader , //删除表头
            getHeaderById : getHeaderById ,//通过id获取表头信息
            updateHeader : updateHeader ,//更新表头信息
            selectHeaderWindow : selectHeaderWindow,//自定义筛选表字段

        }

        return service;

        //begin updateHeader
        function updateHeader(vm){
            common.initJqValidation();
            var isValid = $('form').valid();
            if ( vm.header.headerKey!=undefined && vm.header.headerName!=undefined && vm.header.headerType!=undefined) {
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/header/updateHeader",
                    data: vm.header
                }

                var httpSuccess = function success(response) {
                    if (response.data.flag || response.data.reCode == 'ok') {
                        bsWin.success("修改成功!",function () {
                            window.parent.$("#addHeaderWindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                        });
                    } else {
                        bsWin.error(response.data.reMsg);
                    }

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end updateHeader

        //begin deleteHeader
        function deleteHeader(vm, id){
            var httpOptions ={
                method : 'delete',
                url : rootPath + "/header",
                params : {id : id}
            }
            var httpSuccess = function success(response){
                bsWin.success("删除成功！");
                vm.gridOptions.dataSource.read();
            }

            common.http({
                vm : vm,
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end deleteHeader

        //begin getHeaderById
        function getHeaderById(vm,id){
            var httpOptions ={
                method : 'get',
                url : rootPath + "/header/getHeaderById",
                params : {id : id}
            }
            var httpSuccess = function success(response){
                vm.header = response.data;
            }

            common.http({
                vm : vm,
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end getHeaderById

        //begin updateHeader
        function updateSelectedHeader(vm,idStr){
            var httpOptions ={
                method : 'put',
                url : rootPath + '/header/updateSelectedHeader',
                params : {idStr : idStr}
            }
            var httpSuccess = function success(response){
                // if (callBack != undefined && typeof callBack == 'function') {
                //     callBack();
                // }
            }

            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end updateHeader


        //begin updateCancelHeader
        function updateCancelHeader(vm , idStr){
            var httpOptions ={
                method : 'put',
                url : rootPath + '/header/updateCancelHeader',
                params : {idStr : idStr}
            }
            var httpSuccess = function success(response){
            }

            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end updateCancelHeader

        //begin createHeader
        function createHeader(vm){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (vm.header.headerKey!=undefined && vm.header.headerName!=undefined && vm.header.headerType!=undefined) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + '/header/createHeader',
                    data: vm.header
                }
                var httpSuccess = function success(response) {
                    if(response.data.flag || response.data.reCode == 'ok'){
                        bsWin.success("操作成功！",function(){
                            window.parent.$("#addHeaderWindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                        });
                    }else{
                        bsWin.error(response.data.reMsg);
                    }

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess

                });
            }

        }//end createHeader


        //begin getHeaderList
        function findHeaderListNoSelected(vm){
            vm.header = {};
            vm.header.headerType = vm.headerType;
            var httpOptions ={
                method : 'post',
                url : rootPath + '/header/findHeaderListNoSelected',
                data : vm.header
               // params : {headerType : encodeURIComponent(vm.headerType)},
            }
            var httpSuccess = function success(response){
                vm.allHeaderList = response.data;
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end getHeaderList

        //begin findHeaderListByState
        function findHeaderListSelected(vm , callBack){
            vm.header = {};
            vm.header.headerType = vm.headerType;
            var httpOptions={
                method : 'post',
                url : rootPath + "/header/findHeaderListSelected",
                data : vm.header
                //params : {headerType : encodeURIComponent(vm.headerType)},      //中文会乱码
            }
            var httpSuccess = function success(response){
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                vm : vm,
                $http :$http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findHeaderListByState

        //begin headerGrid
        function headerGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/header/findAllHeader", $("#headerForm")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource

            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo
                            .format(
                                "<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                                item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "headerName",
                    title: "列名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "headerKey",
                    title: "key值",
                    width: 100,
                    filterable: false
                },
                {
                    field: "headerType",
                    title: "类型",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", "vm.create('" + item.id +"')");

                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };
        }
        //end headerGrid

        /**
         * 查询
         * @param vm
         * @param headerType
         */
        function selectHeaderWindow(vm,headerType){
            findHeaderListNoSelected(vm);
            findHeaderListSelected(vm , function(data){
                vm.selectedHeaderList = data;
                vm.header = true;

                console.log(vm.selectedHeaderList);
            });

            $("#selectHeaderWindow").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自定义报表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();

            vm.headerType= headerType;
            vm.allChecked =function(){
                var tab = $('#selectHeaderForm').find('input');
                $.each(tab , function(i , obj){
                    obj.checked = true;
                });
            }

            //全取消
            vm.allCancel = function(){
                var tab = $('#selectHeaderForm').find('input');
                $.each(tab , function(i , obj) {
                    obj.checked = false;
                    // $('input:checkbox').attr('checked', false);
                });
            }

            /**
             * 左添加
             */
            vm.addHeader = function(){
                var ids =[];
                for(var i=0 ; i<vm.allHeaderList.length ; i++){
                    var s = {};
                    if(vm.allHeaderList[0].checkbox){
                        i=0;
                    }
                    s = vm.allHeaderList[i];
                    if(s.checkbox){
                        ids.push(s.id);
                        vm.selectedHeaderList.push(s);
                        vm.allHeaderList.splice(i,1);
                        i=0;
                        s.checkbox = false;
                    }
                }

                var idStr = ids.join(",");
                updateSelectedHeader(vm,idStr);

            }

            /**
             * 右取消
             */
            vm.cancelHeader = function (){
                var ids =[];
                for(var i=0 ; i< vm.selectedHeaderList.length ; i++){
                    var s = {};
                    if(vm.selectedHeaderList[0].checkbox){
                        i=0;
                    }
                     s = vm.selectedHeaderList[i];
                    if(s.checkbox){
                        ids.push(s.id);
                        vm.allHeaderList.push(s);
                        vm.selectedHeaderList.splice(i,1);
                        i=0;
                        s.checkbox = false;
                    }
                }

                var idStr = ids.join(",");
                updateCancelHeader(vm,idStr);
            }


            vm.changeType = function(){
               findHeaderListNoSelected(vm);
                vm.selectedHeaderList=[];
            }
        }
    }
})();