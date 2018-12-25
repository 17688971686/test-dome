(function (){
    'ust strict';
    angular.module('app').factory('postdoctorSubjectSvc' , postdoctorSubject);
    postdoctorSubject.$inject = ['$http'];
    function postdoctorSubject($http){
        var service = {
            subjectGrid : subjectGrid , //课题列表
            findBySubjectId : findBySubjectId , //通过ID获取课题信息
            createSubject : createSubject , //创建课题
            findStationStaff : findStationStaff , //查询在站人员
            isPermission : isPermission , //判断是否有权限查看
            deleteSubject : deleteSubject , //删除课题

        };
        return service;

        //begin deleteSubject
        function deleteSubject(vm , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/postdoctorSubject/deleteSubject",
                params : {id : vm.id}
            }
            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end deleteSubject

        //begin isPermission
        function isPermission(callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/postdoctorSubject/isPermission"
            }
            var httpSuccess = function success(response){
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end isPermission

        //begin createSubject
        function createSubject(vm , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/postdoctorSubject/createdSubject",
                data : vm.subject
            }
            var httpSuccess = function success(response){
                if(callBack != undefined && typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end createSubject

        //begin   findBySubjectId
        function findBySubjectId(vm , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/postdoctorSubject/findBySubjectId",
                params : {id : vm.id}
            }
            var httpSuccess = function success(response){

                if(callBack != undefined && typeof callBack == "function"){
                    callBack(response.data);
                }
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end  findBySubjectId


        function subjectGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/postdoctorSubject/findByAll"  , $('#doctorSubjectForm')),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
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
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.mId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox' />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 40,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "subjectName",
                    title: "课题名称",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        return '<a ng-click="vm.details(' + "'" + item.id + "'" + ')" >'+item.subjectName+'</a>'
                    }
                },
                {
                    field: "pricipalName",
                    title: "课题负责人",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "subjectCreatedDate",
                    title: "创建时间",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 120,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),   item.id , item.pricipalName);
                    }
                }
            ];
            // End:column

            vm.subjectGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };
        }

        //begin findStationStaff
        function findStationStaff(callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/postdoctorSubject/findStationStaff"
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findStationStaff
    }
})();