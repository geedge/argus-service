$(function() {
	resize(0);
	var rowIndex;
	var menu = new BootstrapMenu(
			'#sample_4 tbody tr',
			{
				fetchElementData : function(row) { // fetchElementData获取table数据,row当前操作行
					// var data = $("#sample_1").rows(a).data(); //获取表格数据
					// var data=$("#sample_1")[0].rows[0];
					var checkValue = row[0].cells[0].children[0].children[0].children[0].children[0].value;// 获取当前右键行的id(checkbox的value)
					// rowIndex = row[0].rowIndex;
					// alert("行索引："+row[0].rowIndex);
					// var taskType =
					// row[0].cells[0].children[0].children[2].value;//
					// 获取任务的类型显示的值
					$("#taskType").val(taskType);
					// var taskType =
					// row[0].cells[3].children[0].children[0].innerHTML;//
					// 获取任务的状态(显示的值)
					 var taskType =row[0].cells[0].children[0].children[2].value;// 获取任务的类型
					// 获取任务的状态实际的值
					var updateSign = row[0].cells[0].children[0].children[1].value;
					$("#taskMainState").val(updateSign);
					$("#taskType").val(taskType);
					return checkValue;
				},
				actionsGroups : [ [ 'editDescription' ], [ 'delDescription' ] ],// 给右键菜单的选项加一个分组，分割线
				actions : {
					lookDescription : {
						name : '<font size=3 style="color:black;">查看</font>',
						iconClass : 'fa-list',
						onClick : function(id) { // 查看右击事件
							getTaskMain(id);
						}
					},
					editDescription : {
						name : '<font size=3 style="color:black;">修改</font>',
						iconClass : 'fa-edit',
						onClick : function(id) { // 修改右击事件
							update(id);
						}
					},
					delDescription : {
						name : '<font size=3  style="color:black;">删除</font>',
						iconClass : 'fa-trash',
						onClick : function(id) { // 删除右击事件
							delTaskMain(id);
						}
					}
				}
			});
	

	// 当弹出框显示之后，重新加载Iframe高度
	$("#form_modalDetail").on("shown.bs.modal",function(){
		// 界面高度
		var bodyModalHeight=window.document.documentElement.offsetHeight;
		// 详情div高度
		var viewModalHeight=$(".modal-dialog").height();
		// 详情div的magin距离
		var marginHeight=(parent.window.pageYOffset || parent.document.body.scrollTop ||parent.document.documentElement.scrollTop ||0)+100;
		// 详情界面高度超出了整个界面高度，将多出部分设置到main的高度上
		if((viewModalHeight+marginHeight) > bodyModalHeight){
			resize(((viewModalHeight+marginHeight)-bodyModalHeight));
		}else{
			resize(0);
		}
	});
});
// 条件查询记录
function query(){
	document.form2.submit();	
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
				url : '/mesaweb/taskMainAction/getTaskTempletById',
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
			
						if(data.taskMain.task_run_times==0){
							$("#look_task_run_times").html("");
						}else{
							$("#look_task_run_times").html(data.taskMain.task_run_times);
						}
						// $("#look_task_des").html(data.taskMain.task_des);
						$("#look_task_issued_time").html(
								data.taskMain.task_issued_time);
						$("#look_task_start_time").html(
								data.taskMain.task_start_time);
						if(data.taskMain.task_delay_time==0){
							$("#look_task_delay_time").html("");
						}else{
							$("#look_task_delay_time").html(data.taskMain.task_delay_time);
						}
						if(data.taskMain.task_duration_time==0){
							$("#look_task_duration_time").html("");
						}else{
							$("#look_task_duration_time").html(data.taskMain.task_duration_time);
						}
					
						$("#look_task_end_time").html(
								data.taskMain.task_end_time);
						if(data.taskMain.flow_speed==0){
							$("#look_flow_speed").html("");
						}else{
							$("#look_flow_speed").html(data.taskMain.flow_speed);
						}
						$("#look_create_time").html(data.taskMain.create_time);
						$("#look_create_id").html(data.taskMain.create_name);
						$("#look_opt_time").html(data.taskMain.opt_time);
						$("#look_opt_id").html(data.taskMain.opt_name);

						if (data.taskSubList != undefined) {
							$("#subTitleTr").html("");
							$("#subInfoTr").html("");
							$("#subTitleTr").append("<th>子任务执行节点IP：</th>");
							$("#subInfoTr").append("<th>执行节点端口：</th>");
							for ( var index = 0; index < data.taskSubList.length; index++) {
								var node_ip="<td>"+data.taskSubList[index].node_ip+"</td>"
								$("#subTitleTr").append(node_ip);
								var node_if="<td>"+data.taskSubList[index].node_if+"</td>"
								$("#subInfoTr").append(node_if);
							}
							if(data.taskSubList.length==0){
								$("#subTitleTr").append("<td></td>");
								$("#subInfoTr").append("<td></td>");
							}
						}
						if (data.taskType == 1) {
							$("#taskReplayDiv").show();
							$("#taskAttackDiv").hide();
							$("#taskImitateDiv").hide();
							$("#taskSceneDiv").hide();
							$("#taskMainDiv").show();
							$("#taskTitle").html("流量回放任务详情");
							if (data.taskReplayRule != undefined) {
								if (data.taskReplayRule.chagne_ip == '0') {
									$("#replay_chagne_ip").html("否");
								} else {
									$("#replay_chagne_ip").html("是");

								}

								if(data.taskReplayRule.flow_speed==0){
									$("#replay_flow_speed").html("");
								}else{
									$("#replay_flow_speed").html(data.taskReplayRule.flow_speed);
								}
								
								if(data.taskReplayRule.task_run_times==0){
									$("#replay_task_run_times").html("");
								}else{
									$("#replay_task_run_times").html(data.taskReplayRule.task_run_times);
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

						}
						if (data.taskType == 2) {
							$("#taskSceneDiv").hide();
							$("#taskReplayDiv").hide();
							$("#taskAttackDiv").show();
							$("#taskImitateDiv").hide();
							$("#taskMainDiv").show();
							$("#taskTitle").html("攻击流量任务详情");
							if (data.taskAttackRule != undefined) {
								if(data.taskAttackRule.flow_speed!=0){
									$("#attack_flow_speed").html(data.taskAttackRule.flow_speed);
								}
								$("#attack_attack_type").html(data.taskAttackRule.attack_type_show);
							
								if(data.taskAttackRule.src_ip!=-1){
									$("#attack_src_ip").html(data.taskAttackRule.src_ip);
								}
								if(data.taskAttackRule.src_ip_mask!=-1){
									$("#attack_src_ip_mask").html(data.taskAttackRule.src_ip_mask);
								}
								if(data.taskAttackRule.dst_ip!=-1){
									$("#attack_dst_ip").html(data.taskAttackRule.dst_ip);
								}
								if(data.taskAttackRule.dst_ip_mask!=-1){
									$("#attack_dst_ip_mask").html(data.taskAttackRule.dst_ip_mask);
								}
								if(data.taskAttackRule.dst_ip_port!=-1){
									$("#attack_dst_ip_port").html(data.taskAttackRule.dst_ip_port);
								}
								if(data.taskAttackRule.dst_mac!=-1){
									$("#attack_dst_mac").html(data.taskAttackRule.dst_mac);
								}
								var tcp_payload=data.taskAttackRule.tcp_payload_length.replace(null,"0").replace(null,"0");
								$("#attack_tcp_payload_length").html(tcp_payload);
							}
						}
						if (data.taskType == 3) {
							
							$("#taskSceneDiv").hide();
							$("#taskReplayDiv").hide();
							$("#taskAttackDiv").hide();
							$("#taskImitateDiv").show();
							$("#taskMainDiv").show();
							$("#taskTitle").html("模拟任务详情");
							if (data.taskImitateRule != undefined) {
								$("#imitate_flow_speed").html(
										data.taskImitateRule.flow_speed);
								if (data.taskImitateRule.flow_speed > 0) {
									$("#imitate_src_ip").html(
											data.taskImitateRule.src_ip);
									$("#imitate_src_ip_mask").html(
											data.taskImitateRule.src_ip_mask);
									$("#imitate_dst_ip").html(
											data.taskImitateRule.dst_ip);
									$("#imitate_dst_ip_mask").html(
											data.taskImitateRule.dst_ip_mask);
									if (data.taskImitateRule.src_ip_port_type == '0') {
										$("#imitate_src_ip_port_type").html("随机");
									} else {
										$("#imitate_src_ip_port_type").html("常数");
										$("#imitate_src_ip_port")
												.html(
														data.taskImitateRule.src_ip_port);
									}
									$("#imitate_max_nflow_second")
											.html(
													data.taskImitateRule.max_nflow_second);
									$("#imitate_max_cflow_second")
											.html(
													data.taskImitateRule.max_cflow_second);
									$("#imitate_flow_acontinue")
											.html(
													data.taskImitateRule.flow_acontinue);

								}

								$("#imitate_imitate_type").html(
										data.taskImitateRule.imitate_type_show);
								if (data.taskImitateRule.imitate_net_env == "0") {
									$("#imitate_imitate_net_env").html("否");
								} else {
									$("#imitate_imitate_net_env").html("是");
									$("#imitate_fragment_per").html(
											data.taskImitateRule.fragment_per);
									$("#imitate_replay_per").html(
											data.taskImitateRule.replay_per);
									$("#imitate_unorder_per").html(
											data.taskImitateRule.unorder_per);
									$("#imitate_damage_per").html(
											data.taskImitateRule.damage_per);
									$("#imitate_message_delay").html(
											data.taskImitateRule.message_delay);
								}

							}
							if (data.taskImitateHttpRule != undefined) {
								$("#ftpDetail").hide();
								$("#mailDetail").hide();
								$("#httpDetail").show();
								$("#http_request_method")
										.html(
												data.taskImitateHttpRule.request_method);
								$("#http_request_version")
										.html(
												data.taskImitateHttpRule.request_version);
								$("#http_uset_agent").html(
										data.taskImitateHttpRule.uset_agent);
								$("#http_cookie").html(
										data.taskImitateHttpRule.cookie);
								$("#http_content_type").html(
										data.taskImitateHttpRule.content_type);
								$("#http_keyword").html(
										data.taskImitateHttpRule.keyword);
								$("#http_request_url").html(
										data.taskImitateHttpRule.request_url);
							}
							if (data.taskImitateRuleFtp != undefined) {
								$("#httpDetail").hide();
								$("#mailDetail").hide();
								$("#ftpDetail").show();
								$("#ftp_url").html(data.taskImitateRuleFtp.url);
								$("#ftp_download_file_name")
										.html(
												data.taskImitateRuleFtp.download_file_name);
								$("#ftp_download_file_name_keyword")
										.html(
												data.taskImitateRuleFtp.download_file_name_keyword);
								$("#ftp_uploaded_file_name")
										.html(
												data.taskImitateRuleFtp.uploaded_file_name);
								$("#ftp_uploaded_file_keyword")
										.html(
												data.taskImitateRuleFtp.uploaded_file_keyword);
							}
							if (data.taskImitateRuleMail != undefined) {
								$("#httpDetail").hide();
								$("#ftpDetail").hide();
								$("#mailDetail").show();
								$("#mail_from_mail").html(
										data.taskImitateRuleMail.from_mail);
								$("#mail_to_mail").html(
										data.taskImitateRuleMail.to_mail);
								$("#mail_subject").html(
										data.taskImitateRuleMail.subject);
								$("#mail_content").html(
										data.taskImitateRuleMail.content);
								$("#mail_attachment_filename")
										.html(
												data.taskImitateRuleMail.attachment_filename);
								$("#mail_attachment_keyword")
										.html(
												data.taskImitateRuleMail.attachment_keyword);
							}

						}
						if (data.taskType == 4) {
							var datas=eval("("+data.taskInfoList+")");
							data=datas.taskInfoList;
							$("#taskReplayDiv").hide();
							$("#taskAttackDiv").hide();
							$("#taskImitateDiv").hide();
							$("#taskMainDiv").hide();
							$("#taskSceneDiv").show();
							$("#taskTitle").html("混合任务详情");

							$("#replaySub").hide();
							$("#attackSub").hide();
							$("#imitateSub").hide();
							$("#replaySub").find("table").eq(0).find("tr").eq(0).html("<td>IP</td>");
							$("#replaySub").find("table").eq(0).find("tr").eq(1).html("<td>端口</td>");
							$("#attackSub").find("table").eq(0).find("tr").eq(0).html("<td>IP</td>");
							$("#attackSub").find("table").eq(0).find("tr").eq(1).html("<td>端口</td>");
							$("#imitateSub").find("table").eq(0).find("tr").eq(0).html("<td>IP</td>");
							$("#imitateSub").find("table").eq(0).find("tr").eq(1).html("<td>端口</td>");
							$("#replayDetail").hide();
							$("#attackDetail").hide();
							$("#imitateDetail").hide();
							$("#replayDetail").find("table").eq(0).find("tbody").html("");
							$("#attackDetail").find("table").eq(0).find("tbody").html("");
							$("#imitateDetail").find("table").eq(0).find("tbody").html("");
							$.each(data,function(index,item){
								var html="";
								if(data[index].taskTypeMain == 4){
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(0).find("td").eq(0).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(0).find("td").eq(0).html(data[index].taskNameMain);
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(0).find("td").eq(1).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(0).find("td").eq(1).html(data[index].taskStateStrMain);
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(0).find("td").eq(2).html("场景任务");
									
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(1).find("td").eq(0).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(1).find("td").eq(1).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(1).find("td").eq(2).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(1).find("td").eq(0).html(getNewData(data[index].taskRunTimesMain,"0"));
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(1).find("td").eq(1).html(getNewData(data[index].taskIssuedTimeStrMain,"0"));
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(1).find("td").eq(2).html(getNewData(data[index].taskStartTimeStrMain,"0"));
									
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(2).find("td").eq(0).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(2).find("td").eq(1).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(2).find("td").eq(2).html("");
									
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(2).find("td").eq(0).html(getNewData(data[index].taskDelayTimeMain,"0"));
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(2).find("td").eq(1).html(getNewData(data[index].taskDurationTimeMain,"0"));
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(2).find("td").eq(2).html(getNewData(data[index].taskEndTimeStrMain,"0"));
									
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(3).find("td").eq(0).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(3).find("td").eq(1).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(3).find("td").eq(2).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(3).find("td").eq(0).html(getNewData(data[index].flowSpeedMain,"-1"));
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(3).find("td").eq(1).html(data[index].createTimeStrMain);
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(3).find("td").eq(2).html(data[index].createNameMain);
									
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(4).find("td").eq(0).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(4).find("td").eq(1).html("");
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(4).find("td").eq(0).html(data[index].optTimeStrMain);
									$("#taskMainDetail").find("table").eq(0).find("tr").eq(4).find("td").eq(1).html(data[index].optNameMain);
								}
								if(data[index].taskTypeMain == 1){
									html+=$("#replayDetail").find("table").eq(0).find("tbody").html();
									html+="<tr>";
									html+="<td>"+getNewData(data[index].taskDelayTimeMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskDurationTimeMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskIssuedTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskStartTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskEndTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskRunTimesMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].flowSpeedMain,"-1")+"</td>";
									if(data[index].chagneIpReplay == 0){
										html+="<td>否</td>";
									}else{
										html+="<td>是</td>";
									}
									if(data[index].replayOriginalReplay == 0){
										html+="<td>否</td>";
									}else{
										html+="<td>是</td>";
									}
									if(data[index].replayTypeReplay != -1){
										html+="<td>"+data[index].replayTypeStrReplay+"</td>";
										if(data[index].replayFileReplay!='-1'){
											html+="<td>"+data[index].replayFileReplay+"</td>";
										}else{
											html+="<td></td>";
										}
									}else{
										html+="<td></td>";
										html+="<td></td>";
									}
									html+="</tr>";
									$("#replayDetail").find("table").eq(0).find("tbody").html(html);
									$("#replayDetail").show();
									$.ajax({
										type : 'post',
										url : '/mesaweb/trafficScene/getTaskInfo',
										data : {
											"taskId" : "'"+data[index].taskMainIdReplay+"'","type":0,"isTemplate":0
										},
										dataType : 'json',
										async : false,
										success : function(data1, textStatus) {
											if(data1.length > 0){
												var htmlTR1=$("#replaySub").find("table").eq(0).find("tr").eq(0).html();
												var htmlTR2=$("#replaySub").find("table").eq(0).find("tr").eq(1).html();
												$.each(data1,function(index,item){
													htmlTR1+="<td>"+data1[index].nodeIpSub+"</td>";
													htmlTR2+="<td>"+data1[index].nodeIfSub+"</td>";
												});
												$("#replaySub").find("table").eq(0).find("tr").eq(0).html(htmlTR1);
												$("#replaySub").find("table").eq(0).find("tr").eq(1).html(htmlTR2);
												$("#replaySub").show();
											}
											}
										});
								}
								if(data[index].taskTypeMain == 2){
									html=$("#attackDetail").find("table").eq(0).find("tbody").html();
									html+="<tr>";
									html+="<td>"+getNewData(data[index].taskDelayTimeMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskDurationTimeMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskIssuedTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskStartTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskEndTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].flowSpeedMain,"-1")+"</td>";
									if(data[index].attackTypeAttack >= 1){
										html+="<td>"+data[index].attackTypeStrAttack+"</td>";
									}else{
										html+="<td></td>";
									}
									html+="<td>"+getNewData(data[index].srcIpAttack,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].srcIpMaskAttack,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].dstIpAttack,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].dstIpMaskAttack,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].dstIpPortAttack,"-1")+"</td>";
									html+="<td>"+data[index].dstMacAttack+"</td>";
									html+="<td>"+data[index].tcpPayloadLengthAttack+"</td>";
									html+="</tr>";
									$("#attackDetail").find("table").eq(0).find("tbody").html(html);
									$("#attackDetail").show();
									$.ajax({
										type : 'post',
										url : '/mesaweb/trafficScene/getTaskInfo',
										data : {
											"taskId" : "'"+data[index].taskMainIdAttack+"'","type":0,"isTemplate":0
										},
										dataType : 'json',
										async : false,
										success : function(data2, textStatus) {
											if(data2.length > 0){
												var htmlTR1=$("#attackSub").find("table").eq(0).find("tr").eq(0).html();
												var htmlTR2=$("#attackSub").find("table").eq(0).find("tr").eq(1).html();
												$.each(data2,function(index,item){
													htmlTR1+="<td>"+data2[index].nodeIpSub+"</td>";
													htmlTR2+="<td>"+data2[index].nodeIfSub+"</td>";
												});
												$("#attackSub").find("table").eq(0).find("tr").eq(0).html(htmlTR1);
												$("#attackSub").find("table").eq(0).find("tr").eq(1).html(htmlTR2);
												$("#attackSub").show();
											}
											}
										});
								}
								if(data[index].taskTypeMain == 3){
									html=$("#imitateDetail").find("table").eq(0).find("tbody").html();
									html+="<tr>";
									html+="<td>"+getNewData(data[index].taskDelayTimeMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskDurationTimeMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskIssuedTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskStartTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskEndTimeStrMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].taskRunTimesMain,"0")+"</td>";
									html+="<td>"+getNewData(data[index].flowSpeedMain,"-1")+"</td>";
									if(data[index].attackTypeAttack >= 0){
										html+="<td>"+data[index].imitateTypeStrImitate+"</td>";
									}else{
										html+="<td></td>";
									}
									html+="<td>"+getNewData(data[index].srcIpImitate,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].srcIpMaskImitate,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].dstIpImitate,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].dstIpMaskImitate,"-1")+"</td>";
									if(data[index].srcIpPortTypeImitate == 0){
										html+="<td>随机</td>";
									}else if(data[index].srcIpPortTypeImitate == 1){
										html+="<td>常数</td>";
									}else{
										html+="<td></td>";
									}
									html+="<td>"+getNewData(data[index].srcIpPortImitate,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].maxNflowSecondImitate,"-1")+"</td>";
									html+="<td>"+getNewData(data[index].flowAcontinueImitate,"-1")+"</td>";
									if(data[index].imitateNetEnvImitate == 0){
										html+="<td>否</td>";
									}else if(data[index].imitateNetEnvImitate == 1){
										html+="<td>是</td>";
									}else{
										html+="<td>否</td>";
									}
									html+="<td>"+data[index].fragmentPerImitate+"</td>";
									html+="<td>"+data[index].replayPerImitate+"</td>";
									html+="<td>"+data[index].unorderPerImitate+"</td>";
									html+="<td>"+data[index].damagePerImitate+"</td>";
									html+="<td>"+data[index].messageDelayImitate+"</td>";
									if(data[index].imitateTypeImitate == 1){
										html+="<td>"+data[index].requestMethodHttp+"</td>";
										html+="<td>"+data[index].requestVersionHttp+"</td>";
										html+="<td>"+data[index].usetAgentHttp+"</td>";
										html+="<td>"+data[index].cookieHttp+"</td>";
										html+="<td>"+data[index].contentTypeHttp+"</td>";
										html+="<td>"+data[index].keywordHttp+"</td>";
										html+="<td>"+data[index].requestUrlHttp+"</td>";
										
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
									}
									if(data[index].imitateTypeImitate == 2){
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										
										html+="<td>"+data[index].fromMailMail+"</td>";
										html+="<td>"+data[index].toMailMail+"</td>";
										html+="<td>"+data[index].subjectMail+"</td>";
										html+="<td>"+data[index].contentMail+"</td>";
										html+="<td>"+data[index].attachmentGilenameMail+"</td>";
										html+="<td>"+data[index].attachmentKeywordMail+"</td>";
										
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
									}
									if(data[index].imitateTypeImitate == 3){
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										html+="<td></td>";
										
										html+="<td>"+data[index].urlFtp+"</td>";
										html+="<td>"+data[index].downloadFileNameFtp+"</td>";
										html+="<td>"+data[index].downloadFileNameKeywordFtp+"</td>";
										html+="<td>"+data[index].uploadedFileNameFtp+"</td>";
										html+="<td>"+data[index].keywordHttp+"</td>";
										
									}
									html+="</tr>";
									$("#imitateDetail").find("table").eq(0).find("tbody").html(html);
									$("#imitateDetail").show();
									$.ajax({
										type : 'post',
										url : '/mesaweb/trafficScene/getTaskInfo',
										data : {
											"taskId" : "'"+data[index].taskMainIdImitate+"'","type":0,"isTemplate":0
										},
										dataType : 'json',
										async : false,
										success : function(data3, textStatus) {
											if(data3.length > 0){
												var htmlTR1=$("#imitateSub").find("table").eq(0).find("tr").eq(0).html();
												var htmlTR2=$("#imitateSub").find("table").eq(0).find("tr").eq(1).html();
												$.each(data3,function(index,item){
													htmlTR1+="<td>"+data3[index].nodeIpSub+"</td>";
													htmlTR2+="<td>"+data3[index].nodeIfSub+"</td>";
												});
												$("#imitateSub").find("table").eq(0).find("tr").eq(0).html(htmlTR1);
												$("#imitateSub").find("table").eq(0).find("tr").eq(1).html(htmlTR2);
												$("#imitateSub").show();
											}
											}
										});
								}
								// 加载框显示
							});
						}
						// 导入加载框显示
						$("#form_modalDetail").modal({
							backdrop : "static",
							keyboard : false,
							show : true
						});
					}
				}
			})
}



// 默认值转换
function getNewData(value,defaultVal){
		if(defaultVal != null && defaultVal != ""  &&  value == defaultVal){
			return "";
		}else{
			return value;
		}
	}
function delForAjax(id) {
	$.ajax({
		type : 'post',
		url : '/mesaweb/taskMainAction/delTaskTemplet',
		data : {
			"id" : id
		},
		dataType : 'json',
		async : false,
		success : function(data, textStatus) {
			if (data.msg == "1") {
				alert("删除成功");
				window.location.href = '/mesaweb/taskMainAction/queryTaskMainTemplateInfo';
			}
			if (data.msg == "0") {
				alert("删除失败");
				// window.location.href =
				// '/mesaweb/taskMainAction/queryTaskMainTemplateInfo';
			}
			// if (data.msg == "2") {// 数据正常情况下不会出现该种状态，出现该种状态时说明模拟任务的数据出现异常
				// alert("删除模拟任务失败,其他任务删除成功,请检查数据是否正常!");
				// window.location.href =
				// '/mesaweb/taskMainAction/queryTaskMainTemplateInfo';
			// }
		}
	})
}

function delTaskMain(id) {
	if (id == undefined) {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
		if (checkedNum > 0) {
			if (window.confirm("确定删除吗？")) {
				var id = "[";
				$('input[type="checkbox"][name="td_ck"]:checked').each(
						function() {
							id = id + "," + $(this).val();
						})
				id += ",]";
				delForAjax(id);
			}
		} else {
			alert("请选择一行任务进行删除");
		}
	} else {
		if (window.confirm("确定删除吗？")) {
			id = "[," + id + ",]";
			delForAjax(id);
		}
	}

}

function update(id) {
	// var taskType = $("#taskType").val();
	if (id == undefined) {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
		if (checkedNum == 1) {
			var checkedState = $('input[type="checkbox"][name="td_ck"]:checked')
					.parent().parent().next().val();
	
			if (checkedState == "-2") {
				var checkId = $('input[type="checkbox"][name="td_ck"]:checked')
						.val();
				var taskType = $('input[type="checkbox"][name="td_ck"]:checked').parent().parent().next().next().val();
				if(taskType=='4'){
					window.location.href='/mesaweb/page/trafficScene/addTrafficSceneInfo.jsp?isTemplet=1&id='+checkId;
					return;
				}
				var href = 'id=' + checkId;
				window.location.href = '/mesaweb/taskMainAction/getTaskTempletForUpdate?'
						+ href//
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
		if ($("#taskMainState").val() == "-2") {
			if($("#taskType").val()=='4'){
				window.location.href='/mesaweb/page/trafficScene/addTrafficSceneInfo.jsp?isTemplet=1&id='+id;
				return;
			}
			var href = 'id=' + id;
			window.location.href = '/mesaweb/taskMainAction/getTaskTempletForUpdate?'
					+ href;//
		} else {
			alert("非暂存任务不可修改");
		}
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

// 子页面重新修改父页面iframe高度
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


