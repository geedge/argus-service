$(function() {
		resize(0);
		var taskState;
		var taskId;
		var taskName;
		var rowIndex;
		var menu = new BootstrapMenu(
				'#sample_4 tbody tr',
				{
					fetchElementData : function(row) { // fetchElementData获取table数据,row当前操作行
						taskId = row[0].cells[0].children[0].children[0].children[0].children[0].value.split("+")[0];
						taskState = row[0].cells[0].children[0].children[0].children[0].children[0].value.split("+")[1];
						taskName = row[0].cells[0].children[0].children[0].children[0].children[0].value.split("+")[2];
						return taskId;
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
							onClick : function(row) {
								add();
							}
						},
						editDescription : {
							name : '<font size=3  style="color:black;">修改</font>',
							iconClass : 'fa-edit',
							onClick : function(row) { // 修改右击事件
								getTaskInfoById(taskId,taskState);
							}
						},
						lookDescription : {
							name : '<font size=3  style="color:black;">查看</font>',
							iconClass : 'fa-list',
							onClick : function(id) { 
								viewTrafficScene(taskId,taskState,taskName);
							}
						},
						taskLookDescription : {
							name : '<font size=3  style="color:black;">任务流量查看</font>',
							iconClass : 'fa-list',
							onClick : function(id) { // 查看右击事件
								viewTaskFlow(taskId,taskState,taskName);
							}
						},
						delDescription : {
							name : '<font size=3  style="color:black;">注销</font>',
							iconClass : 'fa-trash',
							onClick : function(id) { // 注销右击事件
								deleteTask(taskId,taskState);
							}
						}
					}
				});
		//当弹出框显示之后，重新加载Iframe高度
		$("#form_modal").on("shown.bs.modal",function(){
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
//子页面重新修改父页面iframe高度
function resize(height) {
    
	var realHeight = 0;
    if (navigator.userAgent.indexOf("Firefox") > 0 || navigator.userAgent.indexOf("Mozilla") > 0 || navigator.userAgent.indexOf("Safari") > 0 || navigator.userAgent.indexOf("Chrome") > 0) { // Mozilla, Safari,Chrome, ...  
        realHeight = window.document.documentElement.offsetHeight + 30+height; 
    } else if (navigator.userAgent.indexOf("MSIE") > 0) { // IE  
        var bodyScrollHeight = window.document.body.scrollHeight + 10+height; //取得body的scrollHeight  
        var elementScrollHeight = window.document.documentElement.scrollHeight + 1+height; //取得documentElement的scrollHeight  
        realHeight = Math.max(bodyScrollHeight, elementScrollHeight); //取当中比较大的一个  
    } else {//其他浏览器  
        realHeight = window.document.body.scrollHeight + window.document.body.clientHeight + 1+height;
    }
    if (realHeight < 800) {
        realHeight = 800;
    }
    if ($("#rightiFrame", window.parent.document).is("iframe")) {
        $("#rightiFrame", window.parent.document).height(realHeight);
    }
}
//条件查询记录
function query(){
	document.form2.submit();	
}
//添加
function add(){
	window.location.href="/mesaweb/page/trafficScene/addTrafficSceneInfo.jsp";
}

//修改记录
function getTaskInfoById(id,taskState){
 	id=getIdStr("update",id,taskState);
 	if(id != null && id != ""){
	    window.location.href="/mesaweb/page/trafficScene/addTrafficSceneInfo.jsp?taskId="+id;
 	}
}
//查看任务流量
function viewTaskFlow(taskId,taskState,taskName){
 	id=getIdStr("viewFlow",taskId,taskState);
 	if(id != null && id != ""){
 		window.location.href="/mesaweb/deviceDataGraphicsTaskFlow/taskFlowShow?taskId="+id+"&taskNum=0&taskName="+taskName+"&taskState="+taskState;
	}
 	
}
 
//注销
function deleteTask(id,taskState){
	id=getIdStr("delete",id,taskState);
	if(id != null && id != ""){
		$.ajax({
				type : 'post',
				url : '/mesaweb/trafficScene/deleteTask',
				data : {
					"taskId" : id
				},
				async : true,
				cache : false,
				dataType : 'text',
				success : function(data,textStatus) {
					alert(data);
					window.location.href="/mesaweb/trafficScene/queryTaskMainList";
				}
			});
	}
}
//默认值转换
function getNewData(value,defaultVal){
		if(defaultVal != null && defaultVal != ""  &&  value == defaultVal){
			return "";
		}else{
			return value;
		}
	}
//获取任务id
function getIdStr(type,taskId,taskState){
		var ids=taskId;
		var flag=true;
		if(ids == null || ids ==""){
			var len=$("input[name=id][checked=checked]").length;
			if(type=="update"){
					if(len > 1){
						alert("只能选择一条记录进行操作");
						ids=""
						return ids;
					}else{
						ids=$("input[name=id][checked=checked]").val().split("+")[0];
						taskState=$("input[name=id][checked=checked]").val().split("+")[1];
						if(taskState == -2){
							ids="'"+ids+"'";
							return ids;
						}else{
							alert("此操作值针对暂存任务");
							ids=""
							return ids;
						}
					}
			}else if(type=="viewDetail"){
					if(len > 1){
						alert("只能选择一条记录进行操作");
						ids=""
						return ids;
					}else{
						ids=$("input[name=id][checked=checked]").val().split("+")[0];
						taskState=$("input[name=id][checked=checked]").val().split("+")[1];
						//if(taskState != 5){
							ids="'"+ids+"'";
							return ids;
						//}else{
						//	alert("注销任务无法查看");
						//	ids=""
						//	return ids;
						//}
					}
			}else if(type=="viewFlow"){
					if(len > 1){
						alert("只能选择一条记录进行操作");
						ids=""
						return ids;
					}else{
						ids=$("input[name=id][checked=checked]").val().split("+")[0];
						taskState=$("input[name=id][checked=checked]").val().split("+")[1];
						if(taskState == 0 || taskState == 1){
							return ids;
						}else{
							alert("此操作只针对执行中和已完成任务");
							ids=""
							return ids;
						}
					}
			}else{
					ids="";
					$("input[name=id][checked=checked]").each(function(){
						value=$(this).val().split("+")[0];
						taskState=$(this).val().split("+")[1];
						ids+=",'"+value+"'";
						if(taskState == 5 && flag){
							flag=false;
						}
						
					});
					if(flag){
						if(ids.length > 0){
							ids=ids.substring(1);
						}
						return ids;
					}else{
						alert("注销任务不允许再次注销");
						ids=""
						return ids;
					}
			}
		}else{
			if(type=="update"){
				if(taskState == -2){
					ids="'"+ids+"'";
					return ids;
				}else{
					alert("此操作值针对暂存任务");
					ids=""
					return ids;
				}
			}else if(type=="viewDetail"){
				//if(taskState != 5){
					ids="'"+ids+"'";
					return ids;
				//}else{
				//	alert("注销任务无法查看");
				//	ids=""
				//	return ids;
				//}
			}else if(type=="viewFlow"){
				if(taskState == 0 || taskState == 1){
					return ids;
				}else{
					alert("此操作只针对执行中和已完成任务");
					ids=""
					return ids;
				}
			}else{
				if(taskState != 5){
					ids="'"+ids+"'";
					return ids;
				}else{
					alert("注销任务不允许再次注销");
					ids=""
					return ids;
				}
			}
		}
		return ids;
	}
//查看详情
function viewTrafficScene(id,taskState){
	var height=(parent.window.pageYOffset || parent.document.body.scrollTop ||parent.document.documentElement.scrollTop ||0)+100;
	$(".modal-dialog").attr("style","margin-top:"+height+"px");
 	id=getIdStr("viewDetail",id,taskState);
	if(id != null && id != ""){
	$.ajax({
			type : 'post',
			url : '/mesaweb/trafficScene/getTaskInfo',
			data : {
				"taskId" : id,"type":4,"isTemplate":0
			},
			dataType : 'json',
			async : false,
			success : function(data, textStatus) {
				if(data.length > 0){
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
							html+="<td>"+getNewData(data[index].maxCflowSecondImitate,"-1")+"</td>";
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
								html+="<td>"+data[index].uploadedFileKeywordFtp+"</td>";
								
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
						//加载框显示
					});
				 	$("#form_modal").modal({
						backdrop:"static",
							keyboard:false,
							show:true
						});
					}else{
						alert("没有详细信息");
					}
				}
			});
		}
}
	