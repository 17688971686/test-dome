(function() {
	'use strict';

	angular.module('app').factory('flowSvc', flow);

	flow.$inject = ['$http', '$state', 'signFlowSvc'];

	function flow($http, $state, signFlowSvc) {
		var service = {
			initFlowData : initFlowData, // 初始化流程数据
			getFlowInfo : getFlowInfo, // 获取流程信息
			commit : commit, // 提交
			rollBackToLast : rollBackToLast, // 回退到上一环节
			rollBack : rollBack, // 回退到选定环节
			initBackNode : initBackNode, // 初始化回退环节信息
			initDealUerByAcitiviId : initDealUerByAcitiviId,
			suspendFlow : suspendFlow, // 流程挂起
			activeFlow : activeFlow, // 重启流程
			deleteFlow : deleteFlow, // 流程终止
			
			markGrid : markGrid, // 评分列表
			gotoExpertmark : gotoExpertmark, // 打开专家评分弹窗
			saveMark : saveMark, // 保存专家评分
			paymentGrid : paymentGrid, // 专家费用列表
			savePayment : savePayment, // 保存专家费用
			countTaxes : countTaxes, // 计算应纳税额
			gotopayment : gotopayment,
			getselectExpertById : getselectExpertById

		};
		return service;

		// S_初始化流程数据
		function initFlowData(vm) {
			var processInstanceId = vm.flow.processInstanceId;
			if (angular.isUndefined(vm.flow.hideFlowImg)
					|| vm.flow.hideFlowImg == false) {
				vm.picture = rootPath + "/flow/processInstance/img/"
						+ processInstanceId;
			}

			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath
						+ "/flow/processInstance/history/" + processInstanceId),
				schema : common.kendoGridConfig().schema({
							id : "id"
						}),
				rowNumber : true,
				headerCenter : true
			});

			var columns = [{
						field : "",
						title : "序号",
						template : "<span class='row-number'></span>",
						width : 30
					}, {
						field : "activityName",
						title : "环节名称",
						width : 120,
						filterable : false
					}, {
						field : "assignee",
						title : "处理人",
						width : 80,
						filterable : false
					}, {
						field : "startTime",
						title : "开始时间",
						width : 120,
						filterable : false,
						format : "{0: yyyy-MM-dd HH:mm:ss}"
					}, {
						field : "endTime",
						title : "结束时间",
						width : 120,
						filterable : false,
						format : "{0: yyyy-MM-dd HH:mm:ss}"
					}, {
						field : "duration",
						title : "处理时长",
						width : 120,
						filterable : false
					}, {
						field : "message",
						title : "处理信息",
						width : 300,
						filterable : false
					}];
			// End:column
			vm.historygrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true,
				dataBound : function() {
					var rows = this.items();
					$(rows).each(function(i) {
								if (i == rows.length - 1) {
									initBackNode(vm);
								}
								$(this).find(".row-number").html(i + 1);
							});
				}
			};
		}// E_初始化流程数据

		// S_getFlowInfo
		function getFlowInfo(vm) {
			var httpOptions = {
				method : 'get',
				url : rootPath + "/flow/processInstance/flowNodeInfo",
				params : {
					taskId : vm.flow.taskId,
					processInstanceId : vm.flow.processInstanceId
				}
			}

			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.flow = response.data;
						if (vm.flow.businessMap) {
							if (vm.flow.businessMap.viceDirectors) {
								vm.businessTr = true;
								vm.ZHB_SP_SW = true;
								vm.viceDirectors = vm.flow.businessMap.viceDirectors;
							}
							if (vm.flow.businessMap.orgs) {
								vm.businessTr = true;
								vm.FGLD_SP_SW = true;
								vm.orgs = vm.flow.businessMap.orgs;
							}
							if (vm.flow.businessMap.users) {
								vm.businessTr = true;
								vm.BM_FB = true;
								vm.users = vm.flow.businessMap.users;
							}
							// 协审分管领导审批
							if (vm.flow.businessMap.xsOrgs) {
								vm.businessTr = true;
								vm.XS_FGLD_SP_SW = true;
								vm.xsOrgs = vm.flow.businessMap.xsOrgs;
							}
						}
						signFlowSvc.initBusinessParams(vm);
					}

				})
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_getFlowInfo

		// S_提交下一步
		function commit(vm) {
			common.initJqValidation($("#flow_form"));
			var isValid = $("#flow_form").valid();
			if (isValid) {
				vm.isCommit = true;
				var httpOptions = {
					method : 'post',
					url : rootPath + "/flow/commit",
					data : vm.flow
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							common.alert({
										vm : vm,
										msg : response.data.reMsg,
										closeDialog : true,
										fn : function() {
											if (response.data.reCode == "error") {
												vm.isCommit = false;
											} else {
												$state.go('index');
											}
										}
									})
						}

					})
				}

				common.http({
							vm : vm,
							$http : $http,
							httpOptions : httpOptions,
							success : httpSuccess,
							onError : function(response) {
								vm.isCommit = false;
							}
						});
			}
		}// E_提交下一步

		// S_回退到上一步
		function rollBackToLast(vm) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/rollbacklast",
				data : vm.flow
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : response.data.reMsg
										})
							}

						})
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_回退到上一步

		// S_回退到指定环节
		function rollBack(vm) {
			if (vm.flow.back == null || vm.flow.back.activitiId == null
					|| vm.flow.back.activitiId == "") {
				common.alert({
							vm : vm,
							msg : "请先选择要会退的环节！"
						})
				return;
			}

			common.confirm({
						vm : vm,
						title : "",
						msg : "确认回退吗？",
						fn : function() {
							// 设置
							vm.flow.rollBackActiviti = vm.flow.back.activitiId;
							vm.flow.backNodeDealUser = vm.flow.back.assignee;

							var httpOptions = {
								method : 'post',
								url : rootPath + "/flow/rollback",
								data : vm.flow
							}
							var httpSuccess = function success(response) {
								common.requestSuccess({
											vm : vm,
											response : response,
											fn : function() {
												common.alert({
															vm : vm,
															msg : response.data.reMsg
														})
											}
										})
							}

							common.http({
										vm : vm,
										$http : $http,
										httpOptions : httpOptions,
										success : httpSuccess
									});
						}
					})
		}// E_回退到指定环节

		// S_初始化回退环节信息
		function initBackNode(vm) {
			vm.backNode = [];
			// 初始化可回退环节
			var datas = vm.historygrid.dataSource.data()
			var totalNumber = datas.length;
			for (var i = 0; i < totalNumber; i++) {
				if (datas[i].assignee && datas[i].endTime) {
					vm.backNode.push({
								"activitiId" : datas[i].activityId,
								"activitiName" : datas[i].activityName,
								"assignee" : datas[i].assignee
							});
				}
			}
		}// E_初始化回退环节信息

		// S_初始化下一环节处理人
		function initDealUerByAcitiviId(vm) {
			vm.nextDealUserList = vm.nextDealUserMap[vm.flow.nextNodeAcivitiId];
			if (vm.nextDealUserList) {
				vm.flow.nextDealUser = vm.nextDealUserList[0].loginName; // 默认选中
			}
		}// E_初始化下一环节处理人

		// S_流程挂起
		function suspendFlow(vm, businessKey) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/suspend/" + businessKey
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : "操作成功！"
										})
							}
						})
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_流程挂起

		// S_流程激活
		function activeFlow(vm, businessKey) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/active/" + businessKey
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : "操作成功！"
										})
							}
						})
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_流程激活

		// S_终止流程
		function deleteFlow(vm) {
			if (vm.flow.dealOption == null || vm.flow.dealOption == "") {
				common.alert({
							vm : vm,
							msg : "请填写处理信息！"
						})
				return;
			}
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/deleteFLow",
				data : vm.flow
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : "操作成功！"
										})
							}
						})
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_终止流程

		// begin#markGrid
		function markGrid(vm) {
			var signId = vm.model.signid;
			var dataSource = common.kendoGridDataSource(rootPath+ "/expertReview/html/getSelectExpert/" + signId);
			var dataBound = function() {
				var rows = this.items();
				$(rows).each(function(i) {
							if (i == rows.length - 1) {
								initBackNode(vm);
							}
							$(this).find(".row-number").html(i + 1);
						});
			}

			// End:column
			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getExpertColumns(),
				dataBound : dataBound,
				resizable : true
				// editable: "inline"
			};
		}// end fun grid

		function getExpertColumns() {
			var columns = [
					/*
					 * { template : function(item) { return kendo.format("<input
					 * type='checkbox' relId='{0}' name='checkbox'
					 * class='checkbox' />",item.expertID) }, filterable :
					 * false, width : 40, title : "<input id='checkboxAll'
					 * type='checkbox' class='checkbox' />" },
					 */
					{
				field : "",
				title : "序号",
				width : 50,
				template : "<span class='row-number'></span>"
			}, {
				field : "",
				title : "姓名",
				width : 100,
				filterable : true,
				template : function(item) {
					return item.name == null ? "" : item.name;
				}
			}, {
				field : "",
				title : "工作单位",
				width : 100,
				filterable : true,
				template : function(item) {
					return item.comPany == null ? "" : item.comPany;
				}
			}, {
				field : "",
				title : "职位/职称",
				width : 150,
				filterable : true,
				template : function(item) {
					if (!item.job) {
						item.job = "";
					}
					if (!item.post) {
						item.post = "";
					}
					return item.job + "/" + item.post;
				}
			}, {
				field : "",
				title : "专业",
				width : 100,
				filterable : true,
				template : function(item) {
					return item.majorStudy == null ? "" : item.majorStudy;
				}
			}, {
				field : "",
				title : "联系电话/办公电话",
				width : 150,
				filterable : true,
				template : function(item) {

					if (!item.phone) {
						item.phone = "";
					}
					if (!item.userPhone) {
						item.userPhone = "";
					}
					return item.userPhone + "/" + item.phone;
				}
			},

			{
				field : "",
				title : "备注",
				width : 100,
				filterable : true,
				template : function(item) {
					return item.remark == null ? "" : item.remark;
				}
			}, {
				field : "expertReviewDto.score",
				title : "评分",
				width : 200,
				template : function(item) {
					var str="";
					for(var i=0;i<item.expertReviewDto.score;i++){
						str+="<span style='color:gold;font-size:20px;' >☆</span>";
					}
					
					return str;
				}
			}, {
				field : "expertReviewDto.describes",
				title : "评级描述",
				width : 200
			}, {
				field : "",
				title : "操作",
				width : 100,
				template : function(item) {

					return common.format($('#columnBtns').html(),"vm.editSelectExpert('" + item.expertID + "','"+item.expertReviewDto.score+"')",item.expertID,item.expertReviewDto.score);
				}
			}
			];
			return columns;
		}

		// S_gotoExpertmark
		function gotoExpertmark(vm) {
			$("#star").raty({
						score : function() {
							$(this).attr("data-num",vm.expertReview.score);
							return $(this).attr("data-num");
						},
						starOn : '../contents/libs/raty/lib/images/star-on.png',
						starOff : '../contents/libs/raty/lib/images/star-off.png',
						starHalf : '../contents/libs/raty/lib/images/star-half.png',
						readOnly : false,
						halfShow : true,
						size : 34,
						click : function(score, evt) {
							vm.expertReview.score = score;
						}
					});
			vm.showExpertRemark = true;
			var WorkeWindow = $("#expertmark");
			WorkeWindow.kendoWindow({
						width : "1000px",
						height : "630px",
						title : "编辑-专家星级",
						visible : false,
						modal : true,
						closable : true,
						actions : ["Pin", "Minimize", "Maximize", "Close"]
					}).data("kendoWindow").center().open();
					getselectExpertById(vm);
		}// E_gotoExpertmark

		// S_saveMark
		function saveMark(vm) {
			common.initJqValidation($('#markform'));
			var isValid = $('#markform').valid();
			if (isValid) {
				var httpOptions = {
					method : 'post',
					url : rootPath + "/expertReview/html/expertMark",
					data : vm.expertReview
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
								vm : vm,
								response : response,
								fn : function() {
									window.parent.$("#expertmark")
											.data("kendoWindow").close();
									vm.gridOptions.dataSource.read();
									vm.isSubmit = false;
									common.alert({
												vm : vm,
												msg : "操作成功"
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
		}// E_saveMark

		// S_getpaymentColumns
		function getpaymentColumns() {
			var columns = [{
						field : "rowNumber",
						title : "序号",
						width : 50,
						template : "<span class='row-number'></span>"
					}, {
						field : "name",
						title : "姓名",
						width : 100,
						filterable : true
					}, {
						field : "idCard",
						title : "身份证号码",
						width : 100,
						filterable : true
					}, {
						field : "openingBank",
						title : "开户行",
						width : 100,
						filterable : true
					}, {
						field : "bankAccount",
						title : "银行账号",
						width : 100,
						filterable : true
					}, {
						field : "expertReviewDto.reviewCost",
						title : "评审费",
						width : 100,
						filterable : true
					},

					{
						field : "expertReviewDto.reviewTaxes",
						title : "应纳税额",
						width : 100,
						filterable : true
					}, {
						field : "expertReviewDto.totalCost",
						title : "合计（元）",
						width : 100
					}, {
						field : "",
						title : "操作",
						width : 100,
						template : function(item) {

							return common.format($('#columnBtn').html(),
									"vm.editpayment('" + item.expertID + "')",
									item.expertID);
						}
					}];
			return columns;
		}// E_getpaymentColumns

		// begin#remarkGrid
		function paymentGrid(vm) {
			var signId = vm.model.signid;
			var dataSource = common.kendoGridDataSource(rootPath
					+ "/expertReview/html/getSelectExpert/" + signId);
			var dataBound = function() {
				var rows = this.items();
				$(rows).each(function(i) {
							if (i == rows.length - 1) {
								initBackNode(vm);
							}
							$(this).find(".row-number").html(i + 1);
						});
			}

			// End:column
			vm.paymentgrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getpaymentColumns(),
				dataBound : dataBound,
				resizable : true
			};

		}// end fun grid

		// S_savePayment
		function savePayment(vm) {
			common.initJqValidation($('#payform'));
			var isValid = $('#payform').valid();
			if (isValid) {
				if (!validateNum(vm)) {
					window.parent.$("#payment").data("kendoWindow").close();
					common.alert({
								vm : vm,
								msg : "应纳税额计算错误,保存失败！",
								fn : function() {
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})
					return;
				}
				vm.isCommit = true;
				var httpOptions = {
					method : 'post',
					url : rootPath + "/expertReview/html/savePayment",
					data : vm.expertReview
				}

				var httpSuccess = function success(response) {
					vm.isCommit = false;
					window.parent.$("#payment").data("kendoWindow").close();
					vm.paymentgrid.dataSource.read();
					common.alert({
								vm : vm,
								msg : "操作成功"
							})
				}

				common.http({
							vm : vm,
							$http : $http,
							httpOptions : httpOptions,
							success : httpSuccess
						});
			}
		}// E_savePayment

		// S_countNum
		function countNum(vm) {
			var XSum = vm.expertReview.reviewCost;
			var Rate;
			var Balan;
			var TSum_7;
			if (XSum <= 1500) {
				Rate = 3;
				Balan = 0;
			}
			if ((1500 < XSum) && (XSum <= 4500)) {
				Rate = 10;
				Balan = 105;
			}
			if ((4500 < XSum) && (XSum <= 9000)) {
				Rate = 20;
				Balan = 555;
			}
			if ((9000 < XSum) && (XSum <= 35000)) {
				Rate = 25;
				Balan = 1005;
			}
			if ((35000 < XSum) && (XSum <= 55000)) {
				Rate = 30;
				Balan = 2755;
			}
			if ((55000 < XSum) && (XSum <= 80000)) {
				Rate = 35;
				Balan = 5505;
			}
			if (XSum > 80000) {
				Rate = 45;
				Balan = 13505;
			}
			TSum_7 = (XSum * Rate) / 100 - Balan
			if (TSum_7 < 0) {
				TSum_7 = 0
			}
			return TSum_7;
		}// E_countNum

		// S_countTaxes
		function countTaxes(vm) {
			var XSum = vm.expertReview.reviewCost;
			var TSum_7 = countNum(vm);
			vm.expertReview.reviewTaxes = TSum_7.toFixed(2);
			vm.expertReview.totalCost = XSum - TSum_7.toFixed(2);
			console.log(vm.expertReview.reviewTaxes);

		}// E_countTaxes

		// S_validateNum
		function validateNum(vm) {
			var is = true;
			var XSum = vm.expertReview.reviewCost;
			var TSum_7 = countNum(vm);
			var totalCost = XSum - TSum_7.toFixed(2);
			var reviewTaxes = TSum_7.toFixed(2);
			if (reviewTaxes != vm.expertReview.reviewTaxes
					&& totalCost != vm.expertReview.totalCost) {
				is = false;
			}

			return is;
		}// E_validateNum

		// S_gotopayment
		function gotopayment(vm) {
			vm.showExpertpayment = true;
			var WorkeWindow = $("#payment");
			WorkeWindow.kendoWindow({
						width : "1000px",
						height : "630px",
						title : "编辑-专家费用",
						visible : false,
						modal : true,
						closable : true,
						actions : ["Pin", "Minimize", "Maximize", "Close"]
					}).data("kendoWindow").center().open();
			getselectExpertById(vm);
		}// E_gotopayment

		// S_getselectExpertById
		function getselectExpertById(vm) {
			vm.isCommit = true;
			var httpOptions = {
				method : 'get',
				url : rootPath + "/expertReview/html/getSelectExpertById",
				params : {
					expertId : vm.expertReview.expertId
				}

			}

			var httpSuccess = function success(response) {
				vm.isCommit = false;
				vm.expertReview = response.data;
				vm.expertReview.expertId = vm.expertReview.expertDto.expertID;
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_getselectExpertById
	}
})();
