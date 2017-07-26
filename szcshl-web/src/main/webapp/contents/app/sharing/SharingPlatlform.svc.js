(function () {
    'use strict';

    angular.module('app').factory('sharingPlatlformSvc', sharingPlatlform);

    sharingPlatlform.$inject = ['$http'];

    function sharingPlatlform($http) {
        var url_sharingPlatlform = rootPath + "/sharingPlatlform", url_back = '#/sharingPlatlform';
        var url_user=rootPath +'/user';
        var  url_org = rootPath + "/org/fingByOData";
       
        var service = {
            grid: grid,
            getSharingPlatlformById: getSharingPlatlformById,
            createSharingPlatlform: createSharingPlatlform,
            deleteSharingPlatlform: deleteSharingPlatlform,
            updateSharingPlatlform: updateSharingPlatlform,
            initFileOption:initFileOption,					//初始化上传控件
            findAllOrglist:findAllOrglist,					//所有部门
            findAllUsers:findAllUsers,						//所有用户
            findFileList:findFileList,					//系统附件列表
            getSharingDetailById:getSharingDetailById,	//获取详情页
            downloadSysfile:downloadSysfile,		//下载
            postArticle:postArticle,			//上一篇
            IntroHTML:IntroHTML,				//截取字符串
            initZtreeClient:initZtreeClient,	//初始化树
            findByIdOrgUsers:findByIdOrgUsers,	//根据id查找部门下所以用户
            getOrg: getOrg,
            
        };

        return service;
        
        function  getOrg(vm){
        	 var httpOptions = {
 	                method: "post",
 	                url: url_sharingPlatlform + "/getOrg",
 	               
 	            }

 	            var httpSuccess = function success(response) {
 	                vm.orgUsers = response.data.userDtolist;
 	                vm.orgPgyb = response.data.userDtopgyb;
 	                console.log(vm.orgPgyb);
 	                console.log(vm.orgUsers);
 	            }
 	            common.http({
 	                vm: vm,
 	                $http: $http,
 	                httpOptions: httpOptions,
 	                success: httpSuccess
 	            });
        }
        function findByIdOrgUsers(vm,id){
	        var httpOptions = {
	                method: "get",
	                url: url_user + "/findUsersByOrgId",
	                params: {
	                    orgId: id
	                }
	            }
	            var httpSuccess = function success(response) {
	                vm.modelPost = response.data;
	            }
	            common.http({
	                vm: vm,
	                $http: $http,
	                httpOptions: httpOptions,
	                success: httpSuccess
	            });
        }
        
        // begin common fun
        function getZtreeChecked() {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var nodes = treeObj.getCheckedNodes(true);
            return nodes;
        }
        
        
        function updateZtree(vm) {
        	
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var checkedNodes = $linq(vm.model.roles).select(function (x) {
                return x.roleName;
            }).toArray();
            var allNodes = treeObj.getNodesByParam("level", 1, null);

            var nodes = $linq(allNodes).where(function (x) {
                return $linq(checkedNodes).contains(x.name);
            }).toArray();

            for (var i = 0, l = nodes.length; i < l; i++) {
                treeObj.checkNode(nodes[i], true, true);
            }
        }
        // begin#initZtreeClient
        function initZtreeClient(vm) {
            var httpOptions = {
                method: 'post',
                url: url_org
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        var zTreeObj;
                        var setting = {
                            check: {
                                chkboxType: {
                                    "Y": "ps",
                                    "N": "ps"
                                },
                                enable: true
                            }
                        };
                        var zNodes = $linq(response.data.value).select(
                            function (x) {
                                return {
                                    id: x.id,
                                    name: x.name
                                };
                            }).toArray();
                        var rootNode = {
                            id: '',
                            name: '共享部门',
                            children: zNodes
                        };
                        zTreeObj = $.fn.zTree.init($("#zTree"), setting, rootNode);
                      
                        if (vm.isUpdate) {
                            updateZtree(vm);
                       }
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        /*Introduction截取content前240个文本字符*/
        function IntroHTML(str) {
            if(str) {
                str = str.replace(/<\/?[^>]*>/g, '');
                str = str.replace(/[ | ]*\n/g, '\n');
                str = str.replace(/\n[\s| | ]*\r/g, '\n');
                str = str.replace(/&nbsp;/ig, '');
                str = str.replace(/\s/g, '');
                if (str.length >= 240) {
                    str = str.substring(0, 240) + "......";
                }
            }
            return str;
        }
        //begin postArticle
        function postArticle(vm, id) {
        
            var httpOptions = {
                method: "get",
                url: rootPath + "/sharingPlatlform/postSharingAritle",
                params: {
                    id: id
                }
            }

            var httpSuccess = function success(response) {
                vm.modelPost = response.data;
                vm.modelPost.inTro=IntroHTML(vm.modelPost.content);
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end postArticle
        
        //begin nextArticle
        function nextArticle(vm, id) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/sharingPlatlform/nextSharingArticle",
                params: {
                    id: id
                }
            }

            var httpSuccess = function success(response) {
                vm.modelNext = response.data;
                vm.modelNext.inTro=IntroHTML(modelNext.content);
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end nextArticle
        
        //下载
        function downloadSysfile(id){
        	if(id){
        		window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
        	}
        }
        
        //S 详情页面
        function getSharingDetailById (vm,id){
        	
        	var httpOptions = {
                    method: 'get',
                    url: url_sharingPlatlform + "/html/sharingDeatilById",
                    params:{id:id}
                };
                var httpSuccess = function success(response) {
                    vm.model = response.data;
                    postArticle(vm, id);
                    nextArticle(vm, id);
                
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });  
        }
        //E 详情页面
        
        
        //S_findFileList
        function findFileList(vm) {
        
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {
                    businessId: vm.model.sharId
                }
            }
            var httpSuccess = function success(response) {
                vm.sysFilelists = response.data;
               
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_findFileList
        
        //S 所有用户
        function findAllUsers(vm){
        	
        	var httpOptions = {
					method: 'post',
					url: url_user + "/fingByOData"
			}
			var httpSuccess = function success(response) {
				vm.userlist = {};
				vm.userlist = response.data.value;
				//console.log(vm.userlist);
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
        }
        //E 所有用户
        
      //S_查询部门列表
		function findAllOrglist(vm){
			
			var httpOptions = {
					method: 'get',
					url: common.format(url_user + "/getOrg")
			}
			var httpSuccess = function success(response) {
				vm.orglist = {};
				vm.orglist = response.data;
			
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
		}
		//E_查询部门列表
        
        //S_initFileOption
        function initFileOption(option) {
        	
            if (option.uploadBt) {
                $("#" + option.uploadBt).click(function () {
                  if (!option.businessId) {
                        common.alert({
                            vm: option.vm,
                            msg: "请先保存数据！",
                            closeDialog: true
                        })
                    } else {
                        $("#commonuploadWindow").kendoWindow({
                            width: 700,
                            height: 400,
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    }
                });
          
               if (option.businessId) {
	                //附件下载方法
	                option.vm.downloadSysFile = function (id) {
	                    window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
                }
              }
                //附件删除方法
              option.vm.delSysFile = function (id) {
            	
                    var httpOptions = {
                        method: 'delete',
                        url: rootPath + "/file/deleteSysFile",
                        params: {
                            id: id
                        }
                    }
                    var httpSuccess = function success(response) {
                        common.requestSuccess({
                            vm: option.vm,
                            response: response,
                            fn: function () {
                                findFileList(option.vm);
                                common.alert({
                                    vm: option.vm,
                                    msg: "删除成功",
                                    closeDialog: true
                                })
                            }
                        });
                    }
                    common.http({
                        vm: option.vm,
                        $http: $http,
                        httpOptions: httpOptions,
                        success: httpSuccess
                    });
                }
             
                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf", "ppt", "zip", "rar"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: {
                        businessId: option.businessId,
                        sysfileType: angular.isUndefined(option.sysfileType) ? "共享平台" : option.sysfileType,
                    }
                };
               
                var filesCount = 0;
                $("#sysfileinput").fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {
                       filesCount = files.length;
                    }).on("fileuploaded", function (event, data, previewId, index) {
                    if (filesCount == (index + 1)) {
                        findFileList(option.vm);
                    }
                });
              option.vm.businessFlag.isInitFileOption = true;
            }

        }//E_initFileOption
    
        // begin#updateSharingPlatlform
        function updateSharingPlatlform(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_sharingPlatlform,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    location.href = url_back;
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteSharingPlatlform
        function deleteSharingPlatlform(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sharingPlatlform+"/sharingDelete",
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createSharingPlatlform
        function createSharingPlatlform(vm) {
   
        	var dept = document.getElementsByTagName('input');
        	var val = [];
        	for(var i=0; i<dept.length; i++){
        		if(dept[i].name =="menuModule" && dept[i].checked == true){
        			val.push(dept[i].value);
        		}
        	}
        	var strVal = val.join(",");
        	vm.model.postResume = strVal;
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
             
              
                var httpOptions = {
                    method: 'post',
                    url: url_sharingPlatlform +"/addSharing",
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn:function() {							
                        	 vm.isSubmit = false;
                             common.alert({
                                 vm: vm,
                                 msg: "操作成功",
                                 closeDialog: true
                             })
						}
                    });
                   vm.model.sharId = response.data.sharId;
                   //初始化附件上传
                       initFileOption({
                            businessId: vm.model.sharId,
                            sysfileType: "共享平台",
                            uploadBt: "upload_file_bt",
                            vm: vm
                       });
                   
                    
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        // begin#getSharingPlatlformById
        function getSharingPlatlformById(vm) {
        	
        	var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/findById",
                params:{id:vm.model.sharId}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
                initFileOption({
                    businessId: vm.model.sharId,
                    sysfileType: "共享平台",
                    uploadBt: "upload_file_bt",
                    vm: vm
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }

        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_sharingPlatlform + "/findByOData",$("#formSharing")),
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
            };
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "theme",
                    title: "共享主题",
                    width: 100,
                    filterable: true
                },
                {
                    field: "pubDept",
                    title: "共享部门",
                    width: 100,
                    filterable: true
                },
                {
                    field: "postResume",
                    title: "共享用户",
                    width: 100,
                    filterable: true
                },
             
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: 80,
                    filterable: true
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 100,
                    filterable: true
                },
                {
                    field: "remark",
                    title: "备注",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                          item.sharId ,  "vm.del('" + item.sharId + "')", item.sharId);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid

    }
})();