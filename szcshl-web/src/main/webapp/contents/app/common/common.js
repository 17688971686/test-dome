(function() {
	'use strict';

	var service = {
		initJqValidation : initJqValidation,// 重置form验证
		requestError : requestError,// 请求错误时执行
		requestSuccess : requestSuccess,// 请求成功时执行
		format : format,// string格式化
		blockNonNumber : blockNonNumber,// 只允许输入数字
		floatNumberInput : floatNumberInput,
		adminContentHeight : adminContentHeight,// 当前Content高度
		alert : alertDialog,// 显示alert窗口
		confirm : confirmDialog,// 显示Confirm窗口
		getQuerystring : getQuerystring,// 取得Url参数
		kendoGridConfig : kendoGridConfig,// kendo grid配置
		getKendoCheckId : getKendoCheckId,// 获得kendo grid的第一列checkId
		cookie : cookie,// cookie操作
		getToken : getToken,// 获得令牌
		appPath : "",// app路径
		http : http,// http请求
		gridDataSource : gridDataSource,// gridDataSource
		loginUrl : window.rootPath + '/home/login',
		buildOdataFilter : buildOdataFilter, // 创建多条件查询的filter
		initDictData : initDictData, // 初始化数字字典
		kendoGridDataSource : kendoGridDataSource, // 获取gridDataSource
		initUploadOption : initUploadOption, // 附件上传参数
		getTaskCount : getTaskCount, // 用户待办总数
		initIdeaData : initIdeaData, // 初始化选择意见窗口数据
		deleteCommonIdea : deleteCommonIdea, // 删除常用意见
		addCommonIdea : addCommonIdea, // 添加常用意见
		saveCommonIdea : saveCommonIdea, // 保存常用意见
		addCorrentIdea : addCorrentIdea, // 添加当前意见
		saveCurrentIdea : saveCurrentIdea, // 绑定当前意见
		initcommonUploadWin : initcommonUploadWin, // 初始化上传附件窗口
		initcommonQueryWin : initcommonQueryWin, // 初始化附件列表窗口
		initUpload : initUpload, // 初始化上传附件控件
		commonDelSysFile : commonDelSysFile, // 删除系统文件
		commonSysFilelist : commonSysFilelist, // 查看系统附件列表
		commonDownloadFile : commonDownloadFile,// 系统文件下载

	};
	window.common = service;

	// 系统文件下载
	function commonDownloadFile(vm, id) {
		var sysfileId = id;
		window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
	}

	// S 删除系统文件
	function commonDelSysFile(vm, id, $http) {
		var httpOptions = {
			method : 'delete',
			url : rootPath + "/file/deleteSysFile",
			data : id
		}
		var httpSuccess = function success(response) {
			common.requestSuccess({
				vm : vm,
				response : response,
				fn : function() {
					window.parent.$("#commonQueryWindow").data("kendoWindow")
							.close();
					common.alert({
						vm : vm,
						msg : "删除成功",
						fn : function() {
							$('.alertDialog').modal('hide');
							$('.modal-backdrop').remove();
						}
					})
				}

			});

		}
		common.http({
			vm : vm,
			$http : $http,
			httpOptions : httpOptions,
			success : httpSuccess
		});
	}
	// E 删除系统文件

	// S 初始化上传附件窗口
	function initcommonUploadWin(options) {
		$("#commonuploadWindow").kendoWindow({
			width : "660px",
			height : "400px",
			title : "附件上传",
			visible : false,
			modal : true,
			closable : true,
			actions : [ "Pin", "Minimize", "Maximize", "Close" ]
		}).data("kendoWindow").center().open();
		initUpload(options);
	}
	// E 初始化上传附件窗口


	// S 初始化上传附件控件
	function initUpload(options) {
		var businessId = options.businessId;
		
		var projectfileoptions = {
			language : 'zh',
			allowedPreviewTypes : [ 'image' ],
			allowedFileExtensions : [ 'jpg', 'png', 'gif', "xlsx", "docx",
					"doc", "xls", "pdf" ],
			maxFileSize : 2000,
			showRemove : false,
			uploadUrl : rootPath + "/file/fileUpload",
			uploadExtraData : {
				businessId : businessId
			}
		};
		$("#commonphotofile").fileinput(projectfileoptions).on(
				"filebatchselected", function(event, files) {

				}).on("fileuploaded", function(event, data) {

		});
	}
	// E 初始化上传附件控件

	// S 查看系统附件列表
	function commonSysFilelist(vm, $http) {
		var sysfileId = vm.sysSignId;
		var httpOptions = {
			method : 'get',
			url : rootPath + "/file/findBySysFileSignId",
			params : {
				signid : sysfileId
			}
		}
		var httpSuccess = function success(response) {
			vm.sysFilelists = response.data;
			
		}
		common.http({
			vm : vm,
			$http : $http,
			httpOptions : httpOptions,
			success : httpSuccess
		});
	}
	// E 查看系统附件列表

	// S 初始化附件上传列表
	function initcommonQueryWin(vm) {
		
		$("#commonQueryWindow").kendoWindow({
			width : "800px",
			height : "400px",
			title : "附件上传列表",
			visible : false,
			modal : true,
			closable : true,
			actions : [ "Pin", "Minimize", "Maximize", "Close" ]
		}).data("kendoWindow").center().open();
//		 commonSysFilelist(vm,$http);
		

	}
	// E 初始化附件上传列表

	function initJqValidation(formObj) {
		if (formObj) {
			formObj.removeData("validator");
			formObj.removeData("unobtrusiveValidation");
			$.validator.unobtrusive.parse(formObj);
		} else {
			$("form").removeData("validator");
			$("form").removeData("unobtrusiveValidation");
			$.validator.unobtrusive.parse("form");
		}
	}
	function requestError(options) {
		var message = '发生错误,系统已记录,我们会尽快处理！';
		if (options.response != undefined) {
			if (options.response.status == 401) {
				location.href = service.loginUrl;
			}
			message = options.response.data.message || message;
		}
		service.alert({
			vm : options.vm,
			msg : message,
			fn : function() {
				options.vm.isSubmit = false;
				options.vm.disabledButton = false;
				$('.alertDialog').modal('hide');
			}
		});
	}
	function requestSuccess(options) {
		var showError = function(msg) {
			service.alert({
				vm : options.vm,
				msg : msg,
				fn : function() {
					options.vm.isSubmit = false;
					$('.alertDialog').modal('hide');
				}
			});
		};
		if (options.response.status > 400) {
			showError("发生错误！");

		} else {
			var data = options.response.data;
			if (data && data.status == 555) {
				showError(data.message);
			} else if (options.fn) {
				options.fn(data);
			}
		}
	}
	function format() {
		var theString = arguments[0];

		// start with the second argument (i = 1)
		for (var i = 1; i < arguments.length; i++) {
			// "gm" = RegEx options for Global search (more than one instance)
			// and for Multiline search
			var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
			theString = theString.replace(regEx, arguments[i]);
		}
		return theString;
	}
	function blockNonNumber(val) {
		var str = val.toString().replace(/[^0-9]/g, '');
		return parseInt(str, 10);
	}
	function floatNumberInput(val) {
		return isNaN(parseFloat(val, 10)) ? 0 : parseFloat(val, 10);
	}
	function adminContentHeight() {
		return $(window).height() - 180;
	}
	function alertDialog(options) {

		// $('.alertDialog').modal('hide');//bug:backdrop:static会失效
		options.vm.alertDialogMessage = options.msg;
		options.vm.alertDialogFn = function() {
			if (options.closeDialog && options.closeDialog == true) {
				$('.alertDialog').modal('hide');
				$('.modal-backdrop').remove();
			}
			if (options.fn) {
				options.fn();
			} else {
				$('.alertDialog').modal('hide');
			}
		};
		$('.alertDialog').modal({
			backdrop : 'static',
			keyboard : false
		});
	}
	function confirmDialog(options) {
		options.vm.dialogConfirmTitle = options.title;
		options.vm.dialogConfirmMessage = options.msg;
		$('.confirmDialog').modal({
			backdrop : 'static'
		});
		options.vm.dialogConfirmSubmit = options.fn;
		if (options.cancel) {
			options.vm.dialogConfirmCancel = options.cancel;
		} else {
			options.vm.dialogConfirmCancel = function() {
				$('.confirmDialog').modal('hide');
			}
		}

	}
	function getQuerystring(key, default_) {
		if (default_ == null)
			default_ = "";
		key = key.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
		var regex = new RegExp("[\\?&]" + key + "=([^&#]*)");
		var qs = regex.exec(window.location.href);
		if (qs == null)
			return default_;
		else
			return qs[1];
	}

	function kendoGridDataSource(url, searchForm) {
		var dataSource = new kendo.data.DataSource({
			type : 'odata',
			transport : kendoGridConfig().transport(url, searchForm),
			schema : kendoGridConfig().schema({
				id : "id",
				fields : {
					createdDate : {
						type : "date"
					}
				}
			}),
			serverPaging : true,
			serverSorting : true,
			serverFiltering : true,
			pageSize : 10,
			sort : {
				field : "createdDate",
				dir : "desc"
			}
		});
		return dataSource;
	}
	function kendoGridConfig() {
		return {
			filterable : {
				extra : false,
				// mode: "row", 将过滤条件假如title下,如果不要直接与title并排
				operators : {
					string : {
						"contains" : "包含",
						"eq" : "等于"
					// "neq": "不等于",
					// "doesnotcontain": "不包含"
					},
					number : {
						"eq" : "等于",
						"neq" : "不等于",
						gt : "大于",
						lt : "小于"
					},
					date : {
						gt : "大于",
						lt : "小于"
					}
				}
			},
			pageable : {
				pageSize : 10,
				previousNext : true,
				buttonCount : 5,
				refresh : true,
				pageSizes : true
			},
			schema : function(model) {
				return {
					data : "value",
					total : function(data) {
						return data['count'];
					},
					model : model
				};
			},
			transport : function(url, form, paramObj) {
				return {
					read : {
						url : url,
						dataType : "json",
						type : "post",
						beforeSend : function(req) {
							req.setRequestHeader('Token', service.getToken());
						},
						data : function() {
							if (form) {
								var filterParam = common.buildOdataFilter(form);
								if (filterParam) {
									if (paramObj && paramObj.filter) {
										return {
											"$filter" : filterParam + " and "
													+ paramObj.filter
										};
									} else {
										return {
											"$filter" : filterParam
										};
									}
								} else {
									if (paramObj && paramObj.filter) {
										return {
											"$filter" : paramObj.filter
										};
									} else {
										return {};
									}
								}
							} else {
								return {};
							}
						}
					}
				}
			},
			noRecordMessage : {
				template : '暂时没有数据.'
			}
		}
	}

	function getKendoCheckId($id) {
		var checkbox = $($id).find('tr td:nth-child(1)').find('input:checked')
		var data = [];
		checkbox.each(function() {
			var id = $(this).attr('relId');
			data.push({
				name : 'id',
				value : id
			});
		});
		return data;
	}

	function http(options) {
		options.headers = {
			Token : service.getToken()
		};
		options.$http(options.httpOptions).then(options.success,
				function(response) {
					if (options.onError) {
						options.onError(response);
					}
					common.requestError({
						vm : options.vm,
						response : response
					});
				});
	}

	// begin:cookie
	function cookie() {
		var cookieUtil = {
			get : function(name, subName) {
				var subCookies = this.getAll(name);
				if (subCookies) {
					return subCookies[subName];
				} else {
					return null;
				}
			},
			getAll : function(name) {
				var cookieName = encodeURIComponent(name) + "=", cookieStart = document.cookie
						.indexOf(cookieName), cookieValue = null, result = {};
				if (cookieStart > -1) {
					var cookieEnd = document.cookie.indexOf(";", cookieStart)
					if (cookieEnd == -1) {
						cookieEnd = document.cookie.length;
					}
					cookieValue = document.cookie.substring(cookieStart
							+ cookieName.length, cookieEnd);
					if (cookieValue.length > 0) {
						var subCookies = cookieValue.split("&");
						for (var i = 0, len = subCookies.length; i < len; i++) {
							var parts = subCookies[i].split("=");
							result[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
						}
						return result;
					}
				}
				return null;
			},
			set : function(name, subName, value, expires, path, domain, secure) {
				var subcookies = this.getAll(name) || {};
				subcookies[subName] = value;
				this.setAll(name, subcookies, expires, path, domain, secure);
			},
			setAll : function(name, subcookies, expires, path, domain, secure) {
				var cookieText = encodeURIComponent(name) + "=";
				var subcookieParts = new Array();
				for ( var subName in subcookies) {
					if (subName.length > 0
							&& subcookies.hasOwnProperty(subName)) {
						subcookieParts.push(encodeURIComponent(subName) + "="
								+ encodeURIComponent(subcookies[subName]));
					}
				}
				if (subcookieParts.length > 0) {

					cookieText += subcookieParts.join("&");
					if (expires instanceof Date) {

						cookieText += ";expires=" + expires.toGMTString();
					}
					if (path) {
						cookieText += ";path=" + path;
					}
					if (domain) {
						cookieText += ";domain=" + domain;
					}
					if (secure) {
						cookieText += ";secure";
					}
				} else {

					cookieText += ";expires=" + (new Date(0)).toGMTString();
				}
				document.cookie = cookieText;
			},
			unset : function(name, subName, path, domain, secure) {
				var subcookies = this.getAll(name);
				if (subcookies) {
					delete subcookies[subName];
					this.setAll(name, subcookies, null, path, domain, secure);
				}
			},
			unsetAll : function(name, path, domain, secure) {
				this.setAll(name, null, new Date(0), path, domain, secure);
			}
		};
		return cookieUtil;
	}
	// end:cookie

	function getToken() {
		var data = cookie().getAll("data");
		return data != null ? data.token : "";
	}

	function gridDataSource(dataSource) {
		dataSource.error = function(e) {
			if (e.status == 401) {
				location.href = service.loginUrl;
			} else {

			}
		};
		return dataSource;
	}

	// S_封装filer的参数
	function buildOdataFilter(from) {
		/*
		 * var t = new Array(); var arrIndex = 0;
		 * $(from).find('input,radio,select,textarea').each(function(index,obj){
		 * if(obj.name && obj.value){ var param = {};
		 * if($(this).attr('operator')){ param.operator =
		 * $(this).attr('operator'); }else{ param.operator = 'eq'; } param.name =
		 * $.trim(obj.name); param.value = $.trim(obj.value); t[arrIndex] =
		 * param; arrIndex++; } });
		 * 
		 * var i = 0; var filterStr = ""; $.each(t, function() { if(this.value){
		 * if(i > 0){ filterStr += " and "; } filterStr += this.name + " " +
		 * this.operator + " '"+ this.value +"'"; i++; } }); return filterStr;
		 */

		var manipulation_rcheckableType = /^(?:checkbox|radio)$/i, rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i, rsubmittable = /^(?:input|select|textarea|keygen)/i;

		return $(from).map(function() {
			var elements = jQuery.prop(this, "elements");
			return elements ? jQuery.makeArray(elements) : this;
		}).filter(
				function() {
					var type = this.type;
					return this.value
							&& this.name
							&& !$(this).is(":disabled")
							&& rsubmittable.test(this.nodeName)
							&& !rsubmitterTypes.test(type)
							&& (this.checked || !manipulation_rcheckableType
									.test(type));
				}).map(
				function(i, elem) {
					var $me = $(this), val = $me.val();
					if (!val)
						return false;
					val = "'" + val + "'";
					var operator = $me.attr("operator") || "eq", dataRole = $me
							.attr("data-role")
							|| ""; // data-role="datepicker"
					if (dataRole == "datepicker") {
						val = "date" + val;
					} else if (dataRole == "datetimepicker") {
						val = "datetime" + val;
					}

					return operator == "like" ? ("substringof(" + val + ", "
							+ elem.name + ")") : (elem.name + " " + operator
							+ " " + val);
				}).get().join(" and ");
	}// E_封装filer的参数

	function initDictData(options) {
		options.$http({
			method : 'get',
			url : rootPath + '/dict/dictItems'
		}).then(function(response) {
			options.scope.dictMetaData = response.data;
			var dictsObj = {};
			reduceDict(dictsObj, response.data);
			options.scope.DICT = dictsObj;
		}, function(response) {
			alert('初始化数据字典失败');
		});
	}

	function reduceDict(dictsObj, dicts, parentId) {
		if (!dicts || dicts.length == 0) {
			return;
		}
		if (!parentId) {
			// find the top dict
			for (var i = 0; i < dicts.length; i++) {
				var dict = dicts[i];

				if (!dict.parentId) {
					dictsObj[dict.dictCode] = {};
					dictsObj[dict.dictCode].dictId = dict.dictId;
					dictsObj[dict.dictCode].dictCode = dict.dictCode;
					dictsObj[dict.dictCode].dictName = dict.dictName;
					dictsObj[dict.dictCode].dictKey = dict.dictKey;
					dictsObj[dict.dictCode].dictSort = dict.dictSort;

					reduceDict(dictsObj[dict.dictCode], dicts, dict.dictId);
				}
			}
		} else {
			// find sub dicts
			for (var i = 0; i < dicts.length; i++) {
				var dict = dicts[i];
				if (dict.parentId && dict.parentId == parentId) {
					if (!dictsObj.dicts) {
						dictsObj.dicts = new Array();
					}
					var subDict = {};
					subDict.dictId = dict.dictId;
					subDict.dictName = dict.dictName;
					subDict.dictCode = dict.dictCode;
					subDict.dictKey = dict.dictKey;
					subDict.dictSort = dict.dictSort;
					dictsObj.dicts.push(subDict);
					// recruce
					reduceDict(subDict, dicts, dict.dictId);
				}
			}
		}
	}

	// S_附件上传参数初始化
	function initUploadOption(options) {
		return {
			async : {
				saveUrl : rootPath + "/file",
				removeUrl : rootPath + "/file/delete",
				autoUpload : false
			},
			select : function(e) {
				if (options.onSelect) {
					options.onSelect(e)
				} else {
					$.each(e.files, function(index, value) {
						console.log("Name: " + value.name + "Size: "
								+ value.size + " bytes" + "Extension: "
								+ value.extension);
					});
				}
			},
			upload : function(e) {
				if (options.onUpload) {
					options.onUpload(e)
				} else {
					var files = e.files;
					console.log(e.response)
				}
			},
			success : function(e) {
				if (options.onSuccess) {
					options.onSuccess(e)
				} else {
					var files = e.files;
					if (e.operation == "upload") {
						files[0].sysFileId = e.response.sysFileId;
					}
				}
			},
			remove : function(e) {
				if (options.onRemove) {
					options.onRemove(e)
				} else {
					var files = e.files;
					e.data = {
						'sysFileId' : files[0].sysFileId
					};
				}
			}
		}
	}// E_附件上传参数初始化

	// S_获取待办总数
	function getTaskCount(options) {
		options.$http({
			method : 'get',
			url : rootPath + '/flow/html/tasksCount'
		}).then(function(response) {
			$('#GtasksCount').html(response.data);
		});
	}// E_获取待办总数

	// init
	init();
	function init() {
		// begin#grid 处理
		// 全选
		$(document)
				.on(
						'click',
						'#checkboxAll',
						function() {
							var isSelected = $(this).is(':checked');
							$('.grid').find('tr td:nth-child(1)').find(
									'input:checkbox').prop('checked',
									isSelected);
						});
		// 点击行，改变背景
		$('body').on('click', '.grid tr', function(e) {
			$(this).parent().find('tr').removeClass('selected');
			$(this).addClass('selected');
			// $(this).find('td:nth-child(1)').find('input').prop('checked',
			// true);
			// $(this).find('td:nth-child(2)').find('input').prop('checked',
			// true);
		})
		// end#grid 处理
	}

	// 初始化常用意见
	function initIdeaData(vm, $http, options) {
		vm.ideaContent = '';// 初始化当前意见
		vm.$http = $http;
		vm.i = 1;

		var ideaEditWindow = $("#ideaWindow");
		ideaEditWindow.kendoWindow({
			width : "50%",
			height : "80%",
			title : "意见选择",
			visible : false,
			modal : true,
			closable : true,
			actions : [ "Pin", "Minimize", "Maximize", "close" ]
		}).data("kendoWindow").center().open();

		vm.$http({
			method : 'get',
			url : rootPath + "/idea"
		}).then(function(response) {
			vm.commonIdeas = response.data;

			vm.deleteCommonIdea = function() {// 删除常用意见
				deleteCommonIdea(vm);
			};

			vm.addCorrentIdea = function(ideaContent) {// 添加当前意见
				addCorrentIdea(vm, ideaContent);
			};

			vm.addCommonIdea = function() {// 添加常用意见
				addCommonIdea(vm);
			}

			vm.saveCommonIdea = function() {// 保存常用意见

				saveCommonIdea(vm);
			}

			vm.saveCurrentIdea = function() {
				saveCurrentIdea(vm, options);
			}
		});

	}

	function deleteCommonIdea(options) {
		var isCheck = $("#commonIdeaTable input[name='ideaCheck']:checked");
		if (isCheck.length < 1) {
			alert("请选择要删除的意见！");
		} else {
			var ids = [];
			for (var i = 0; i < isCheck.length; i++) {
				options.commonIdeas.forEach(function(c, number) {
					if (isCheck[i].value == c.ideaID || c.ideaID == undefined) {
						options.commonIdeas.splice(number, 1);
					}
					ids.push(isCheck[i].value);
				});
			}
			var idsStr = ids.join(",");

			options.$http({
				method : 'delete',
				url : rootPath + '/idea',
				params : {
					ideas : idsStr
				}
			})

		}
	}// end

	function addCorrentIdea(options, ideaContent) {

		options.ideaContent = options.ideaContent + ideaContent;
	}// end

	function addCommonIdea(options) {
		options.commonIdea = {};
		options.commonIdea.ideaType = "个人常用意见";
		options.commonIdeas.push(options.commonIdea);
		options.i++;
	}// end

	function saveCommonIdea(options) {

		options.$http({
			method : 'post',
			url : rootPath + "/idea",
			headers : {
				"contentType" : "application/json;charset=utf-8" // 设置请求头信息
			},
			dataType : "json",
			data : angular.toJson(options.commonIdeas)
		}).then(function(response) {
			alert("保存成功！");
		});
	}// end

	function saveCurrentIdea(vm, options) {
		var targetObj = $("#" + options.targetId);
		targetObj.val(targetObj.val() + vm.ideaContent);
		window.parent.$("#ideaWindow").data("kendoWindow").close();
	}// end

})();
