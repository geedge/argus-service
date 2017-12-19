$(function() {
	var rowIndex;
	resize(0);
	var menu = new BootstrapMenu(
			'#sample_4 tbody tr',
			{
				fetchElementData : function(row) { // fetchElementData获取table数据,row当前操作行
					// var data = $("#sample_1").rows(a).data(); //获取表格数据
					// var data=$("#sample_1")[0].rows[0];
					var checkValue = row[0].cells[0].children[0].children[0].children[0].children[0].value;// 获取当前右键行的id(checkbox的value)
					// rowIndex = row[0].rowIndex;
					// alert("行索引："+row[0].rowIndex);
					// var taskMainType =
					// row[0].cells[2].children[0].children[0].innerHTML;//
					// 获取任务的类型
					var updateSign = row[0].cells[0].children[0].children[1].value;
					var taskName = row[0].cells[1].children[0].children[0].innerHTML;
					$("#taskState").val(updateSign);
					$("#taskName").val(taskName);
					$("#taskMainType").val(updateSign);
					return checkValue;
				},
				actionsGroups : [ // 给右键菜单的选项加一个分组，分割线
				[ 'editDescription' ], //
				[ 'lookDescription' ],//
				[ 'taskLookDescription' ],//
				[ 'delDescription' ] //
				],
				actions : {
					addDescription : {
						name : '<font size=3  style="color:black;">新增</font>',
						iconClass : 'fa-plus',
						onClick : function(row) { // 添加右击事件
							// alert("行索引" + rowIndex);
							add();
							// 定义你自己的添加事件
						}
					},
					editDescription : {
						name : '<font size=3  style="color:black;">修改</font>',
						iconClass : 'fa-edit',
						onClick : function(row) { // 修改右击事件
							update(row);
							// 定义你自己的修改事件
						}
					},
					lookDescription : {
						name : '<font size=3  style="color:black;">查看</font>',
						iconClass : 'fa-list',
						onClick : function(id) { // 查看右击事件
							getTaskMain(id);
						}
					},
					taskLookDescription : {
						name : '<font size=3  style="color:black;">任务流量查看</font>',
						iconClass : 'fa-list',
						onClick : function(id) { // 查看右击事件
							taskLookDescription(id);
						}
					},
					delDescription : {
						name : '<font size=3  style="color:black;">注销</font>',
						iconClass : 'fa-trash',
						onClick : function(id) { // 注销右击事件
							delTaskMain(id);
						}
					}
				}
			});
	
	//当弹出框显示之后，重新加载Iframe高度
	$("#form_modalReplay").on("shown.bs.modal",function(){
		//界面高度
		var bodyModalHeight=window.document.documentElement.offsetHeight;
		//详情div高度
		var viewModalHeight=$(".modal-dialog").height();
		//详情div的magin距离
		var marginHeight=(parent.window.pageYOffset || parent.document.body.scrollTop ||parent.document.documentElement.scrollTop ||0)+100;
		//详情界面高度超出了整个界面高度，将多出部分设置到main的高度上
		if((viewModalHeight+marginHeight) > bodyModalHeight){
			resize(((viewModalHeight+marginHeight)-bodyModalHeight));
		}else{
			resize(0);
		}
	});
});

// 条件查询记录
function query() {
	document.form2.submit();
}
function add() {
	window.location.href = "/mesaweb/taskMainAction/toAddPage";
}

function taskLookDescription(id) {
	var taskName = $("#taskName").val();
	var taskState = $("#taskState").val();
	if (taskState == 0 || taskState == 1) {
		window.location.href = "/mesaweb/deviceDataGraphicsTaskFlow/taskFlowShow?taskId="
				+ id
				+ "&taskNum=0&taskName="
				+ taskName
				+ "&taskState="
				+ taskState;
	} else {
		alert("只有执行中,和已完成状态的任务可以查看流量");
	}

}

function update(id) {
	if (id == undefined) {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
		if (checkedNum == 1) {
			var checkedState = $('input[type="checkbox"][name="td_ck"]:checked')
					.parent().parent().next().val();
			if (checkedState == "-2") {
				var checkId = $('input[type="checkbox"][name="td_ck"]:checked')
						.val();
				window.location.href = '/mesaweb/taskMainAction/getTaskReplayRuleByIdForUpdate?id='
						+ checkId;//
			} else {
				alert("非暂存任务不可修改");
			}
		} else {
			if (checkedNum > 1) {
				alert("只能修改一行任务");
			} else {
				alert("请选择一行任务进行修改");
			}
		}
	} else {
		if ($("#taskMainType").val() == "-2") {
			window.location.href = '/mesaweb/taskMainAction/getTaskReplayRuleByIdForUpdate?id='
					+ id;//
		} else {
			alert("非暂存任务不可修改");
		}
	}

}

function delTaskMain(id) {
	if (id == undefined) {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
		if (checkedNum > 0) {
			var cancelState = 0;
			var id = "[";
			$('input[type="checkbox"][name="td_ck"]:checked').each(function() {
				if ($(this).parent().parent().next().val() == '5') {
					cancelState = cancelState + 1;
				}
				id = id + "," + $(this).val();
			})
			if (cancelState == 0) {
				if (window.confirm("确定注销吗？")) {
					id += ",]";
					delForAjax(id);
				}
			} else {
				alert("选择的任务中含有已注销的任务,请重新选择数据");
			}

		} else {
			alert("请选择一行任务进行注销");
		}
	} else {
		var state = $("#taskState").val();
		if (state != '5') {
			if (window.confirm("确定注销吗？")) {
				id = "[," + id + ",]";
				delForAjax(id);
			}
		} else {
			alert("选择的任务中含有已注销的任务,请重新选择数据");
		}

	}

}
function delForAjax(id) {
	$.ajax({
		type : 'post',
		url : '/mesaweb/taskMainAction/deleteTaskMainByid',
		data : {
			"id" : id
		},
		dataType : 'json',
		async : false,
		success : function(data, textStatus) {
			if (data.msg == "1") {
				alert("注销成功");
				window.location.reload(true);
			} else {
				alert("注销失败");
				// alert(data.id);
			}
		}
	})
}

function getTaskMain(id) {
	if (id == undefined) {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
		if (checkedNum == 1) {
			var checkId = $('input[type="checkbox"][name="td_ck"]:checked')
					.val();
			getForAjax(checkId, "look");
		} else {
			if (checkedNum > 1) {
				alert("只能查看一行任务");
			} else {
				alert("请选择一行任务进行查看");
			}
		}
	} else {
		getForAjax(id, "look");
	}
}

function getForAjax(id, look) {
	var height=(parent.window.pageYOffset || parent.document.body.scrollTop ||parent.document.documentElement.scrollTop ||0)+100;
	$(".modal-dialog").attr("style","margin-top:"+height+"px");
	$
			.ajax({
				type : 'post',
				url : '/mesaweb/taskMainAction/getTaskMainById',
				data : {
					"id" : id
				},
				dataType : 'json',
				async : false,
				success : function(data, textStatus) {
					if (data == null) {
						alert("查看详情失败");
					} else {
						$("#look_task_name").html(data.taskMain.task_name);
						$("#look_task_state").html(
								data.taskMain.task_state_show);
						$("#look_task_type").html(data.taskMain.task_type_show);

						if (data.taskMain.task_run_times == 0) {
							$("#look_task_run_times").html("");
						} else {
							$("#look_task_run_times").html(
									data.taskMain.task_run_times);
						}
						// $("#look_task_des").html(data.taskMain.task_des);
						$("#look_task_issued_time").html(
								data.taskMain.task_issued_time);
						$("#look_task_start_time").html(
								data.taskMain.task_start_time);
						if (data.taskMain.task_delay_time == 0) {
							$("#look_task_delay_time").html("");
						} else {
							$("#look_task_delay_time").html(
									data.taskMain.task_delay_time);
						}
						if (data.taskMain.task_duration_time == 0) {
							$("#look_task_duration_time").html("");
						} else {
							$("#look_task_duration_time").html(
									data.taskMain.task_duration_time);
						}

						$("#look_task_end_time").html(
								data.taskMain.task_end_time);
						if (data.taskMain.flow_speed == 0) {
							$("#look_flow_speed").html("");
						} else {
							$("#look_flow_speed")
									.html(data.taskMain.flow_speed);
						}
						$("#look_create_time").html(data.taskMain.create_time);
						$("#look_create_id").html(data.taskMain.create_name);
						$("#look_opt_time").html(data.taskMain.opt_time);
						$("#look_opt_id").html(data.taskMain.opt_name);

						if (data.taskReplayRule != undefined) {
							if (data.taskReplayRule.chagne_ip == '0') {
								$("#replay_chagne_ip").html("否");
							} else {
								$("#replay_chagne_ip").html("是");

							}
							if (data.taskReplayRule.flow_speed == 0) {
								$("#replay_flow_speed").html("");
							} else {
								$("#replay_flow_speed").html(
										data.taskReplayRule.flow_speed);
							}
							if (data.taskReplayRule.task_run_times == 0) {
								$("#replay_task_run_times").html("");
							} else {
								$("#replay_task_run_times").html(
										data.taskReplayRule.task_run_times);
							}

							if (data.taskReplayRule.replay_original == '0') {
								$("#replay_replay_original").html("否");
							} else {
								$("#replay_replay_original").html("是");

							}
							$("#replay_replay_type").html(
									data.taskReplayRule.replay_type_show);
							$("#replay_replay_file").html(
									data.taskReplayRule.replay_file);
						}
						if (data.taskSubList != undefined) {
							$("#subTitleTr").html("");
							$("#subInfoTr").html("");
							$("#subTitleTr").append("<th>子任务执行节点IP：</th>");
							$("#subInfoTr").append("<th>执行节点端口：</th>");
							for ( var index = 0; index < data.taskSubList.length; index++) {
								var node_ip = "<td>"
										+ data.taskSubList[index].node_ip
										+ "</td>"
								$("#subTitleTr").append(node_ip);
								var node_if = "<td>"
										+ data.taskSubList[index].node_if
										+ "</td>"
								$("#subInfoTr").append(node_if);
							}

							if (data.taskSubList.length == 0) {
								$("#subTitleTr").append("<td></td>");
								$("#subInfoTr").append("<td></td>");
							}
						}
						// if (data.taskSubList != undefined) {
						// $("#subTitleTr").html("");
						// $("#subInfoTr").html("");
						// for ( var index = 0; index < data.taskSubList.length;
						// index++) {
						// $("#subTitleTr").append("<th>子任务执行节点IP：</th><th>执行节点端口：</th>");
						// var
						// td="<td>"+data.taskSubList[index].node_ip+"</td><td>"+data.taskSubList[index].node_if+"</td>"
						// $("#subInfoTr").append(td);
						// }
						// }

						// if (data.taskSubList != undefined) {
						// $("#subTitleTr").html("");
						// $("#subInfoTr").html("");
						// $("#subInfoTr").append("<tr><th>子任务执行节点IP：</th><th>执行节点端口：</th></tr>");
						// for ( var index = 0; index < data.taskSubList.length;
						// index++) {
						// var
						// td="<tr><td>"+data.taskSubList[index].node_ip+"</td><td>"+data.taskSubList[index].node_if+"</td></tr>"
						// $("#subInfoTr").append(td);
						// }
						// }

						// 导入加载框显示
						$("#form_modalReplay").modal({
							backdrop : "static",
							keyboard : false,
							show : true
						});
					}
				}
			})
}

function cancleTHCheck(obj) {
	if ($(obj).attr("checked") == "checked") {
	} else {// 如果有一行checkbox没有被选中则上面序号处的checkbox就不被选中
		$("#th_ck").attr("checked", false);
		$("#th_ck").parent().attr("class", "");
	}
	var checkNum = 0;// 记录当前有多少行的checkbox被选中
	$("input[type='checkbox'][name='td_ck']").each(function(i) {
		if ($(this).attr("checked") == "checked") {
			checkNum = Number(checkNum) + Number("1");
		}
	});
	var tdCheckBoxNum = $('input[type="checkbox"][name="td_ck"]').length;
	if (Number(checkNum) == Number(tdCheckBoxNum)) {// 如果都被选中，则最上面的序号处的checkbox也被选中
		$("#th_ck").attr("checked", true);
		$("#th_ck").parent().attr("class", "checked")
	}
}

function selectAll(obj) {
	if ($(obj).attr("checked") == "checked") {// 全选
		$('input[type="checkbox"][name="td_ck"]').each(function() {// 由于bootstrap为checkbox添加了父标签span并且通过span的class="checked"设置checkbox是否被选中
			$(this).parent().attr('class', 'checked');
			$(this).attr("checked", true);
		})
	} else {// 取消全选
		$('input[type="checkbox"][name="td_ck"]').each(function() {
			$(this).parent().attr('class', '');
			$(this).attr("checked", false);
		})
	}

}
//子页面重新修改父页面iframe高度
function resize(height) {
	var realHeight = 0;
	if (navigator.userAgent.indexOf("Firefox") > 0 || navigator.userAgent.indexOf("Mozilla") > 0 || navigator.userAgent.indexOf("Safari") > 0 || navigator.userAgent.indexOf("Chrome") > 0) { // Mozilla,
		realHeight = window.document.documentElement.offsetHeight + 30+height; 
	} else if (navigator.userAgent.indexOf("MSIE") > 0) { // IE
	  var bodyScrollHeight = window.document.body.scrollHeight + 10+height; // 取得body的scrollHeight
	  var elementScrollHeight = window.document.documentElement.scrollHeight + 1+height; // 取得documentElement的scrollHeight
	  realHeight = Math.max(bodyScrollHeight, elementScrollHeight); // 取当中比较大的一个
	} else {// 其他浏览器
	      realHeight = window.document.body.scrollHeight + window.document.body.clientHeight + 1+height;
	}
	if (realHeight < 800) {
	      realHeight = 800;
	}
	if ($("#rightiFrame", window.parent.document).is("iframe")) {
		$("#rightiFrame", window.parent.document).height(realHeight);
	}
}