$(function(){
	getParmData("filetype","playBackDemo_taskReplayRules.replay_type");// 文件类型数据字典
	getParmData("attacktype","attackDemo_taskAttackRules.attack_type");// 攻击类型数据字典
	getParmData("imitatetype","imitateDemo_taskImitateRules.imitate_type");// 模拟业务类型数据字典
	
	//隐藏三大任务的模板信息
	$("#playBackTemp").hide();
	$("#attackTemp").hide();
	$("#imitateTemp").hide();
	var demoHtml;
	//每次点击tab时，对Iframe重新设置高度
	$(".nav-tabs > li > a").live("click",function(){
		resize();
	});
	$("#ok").click(function(){
			var taskId =$("[name=td_ck][checked=checked]").val();
			var type = $("#taskTempType").val();
			if(taskId != null && taskId != "" && taskId != "null"){
				loadTaskInfo("'"+taskId+"'",1,type);
			}
			resize();
		});
	$("#playBackBtnAdd").click(function(){
			demoHtml=$("#playBackTemp").clone().show();
			demoHtml=demoHtml.attr("id","");
			demoHtml.find("[name^=playBackDemo_]").each(function(){
				$(this).attr("name",$(this).attr("name").replace("playBackDemo_","").replace(".","["+getTaskMainMaxIndex()+"]."));
				if($(this).attr("name").indexOf("fileInfos") > -1){
					$(this).attr("name",$(this).attr("name").replace(".",""));
				}
			});
			$("#playBackTemp").after(demoHtml);
			resize();
		});
	$("#attackBtnAdd").click(function(){
			demoHtml=$("#attackTemp").clone().show();
			demoHtml=demoHtml.attr("id","");
			demoHtml.find("[name^=attackDemo]").each(function(){
				$(this).attr("name",$(this).attr("name").replace("attackDemo_","").replace(".","["+getTaskMainMaxIndex()+"]."));
			});
			$("#attackTemp").after(demoHtml);
			resize();
		});
	$("#imitateBtnAdd").click(function(){
			demoHtml=$("#imitateTemp").clone().show();
			demoHtml=demoHtml.attr("id","");
			demoHtml.find("[name^=imitateDemo]").each(function(){
				$(this).attr("name",$(this).attr("name").replace("imitateDemo_","").replace(".","["+getTaskMainMaxIndex()+"]."));
			});
			demoHtml.find("div[id=httpArea]").attr("id","httpArea["+getTaskMainMaxIndex()+"]");
			demoHtml.find("div[id=ftpArea]").attr("id","ftpArea["+getTaskMainMaxIndex()+"]");
			demoHtml.find("div[id=mailArea]").attr("id","mailArea["+getTaskMainMaxIndex()+"]");
			demoHtml.find("div[id=mustField]").attr("id","mustField["+getTaskMainMaxIndex()+"]");
			$("#imitateTemp").after(demoHtml);
			resize();
		});
	
	var taskId = $("input[name='taskMains[0].id']").val();
	if(taskId != null && taskId != "" && taskId != "null"){
		loadTaskInfo(taskId,0,4);
	}
	resize();
});
//子页面重新修改父页面iframe高度
function resize() {
    var realHeight = 0;
    if (navigator.userAgent.indexOf("Firefox") > 0 || navigator.userAgent.indexOf("Mozilla") > 0 || navigator.userAgent.indexOf("Safari") > 0 || navigator.userAgent.indexOf("Chrome") > 0) { // Mozilla, Safari,Chrome, ...  
        realHeight = window.document.documentElement.offsetHeight + 35; 
    } else if (navigator.userAgent.indexOf("MSIE") > 0) { // IE  
        var bodyScrollHeight = window.document.body.scrollHeight + 21; //取得body的scrollHeight  
        var elementScrollHeight = window.document.documentElement.scrollHeight + 1; //取得documentElement的scrollHeight  
        realHeight = Math.max(bodyScrollHeight, elementScrollHeight); //取当中比较大的一个  
    } else {//其他浏览器  
        realHeight = window.document.body.scrollHeight + window.document.body.clientHeight + 1;
    }
    if (realHeight < 800) {
        realHeight = 800;
    }
    if ($("#rightiFrame", window.parent.document).is("iframe")) {
        $("#rightiFrame", window.parent.document).height(realHeight);
    }
}
//获取字典信息
function getParmData(type,name){
	$.ajax({
			type : 'post',
			url : '/mesaweb/trafficScene/getParmData',
			data : {
				"type" : type
			},
			dataType : 'json',
			async : false,
			success : function(data, textStatus) {
				if(data.length > 1){
					var html="";
					if(type == "filetype"){
							html=$("[name='"+name+"']").html();
						}
					$.each(data,function(index,item){
							html+="<option value=\""+data[index].parm_code+"\">"+data[index].parm_name+"</option>"
					});
					$("[name='"+name+"']").html(html);
					$("[name='"+name+"']").trigger("change");
				}
			}
		});
}
//修改
function loadTaskInfo(taskId,isTemplate,taskType){
	$.ajax({
			type : 'post',
			url : '/mesaweb/trafficScene/getTaskInfo',
			data : {
				"taskId" : taskId,"isTemplate":isTemplate,"type":taskType
			},
			dataType : 'json',
			async : false,
			success : function(data, textStatus) {
				if(data.length > 0){
					if(isTemplate == 1 && taskType==4){
						$("#playBackTemp").parent().find("div[class='portlet light'][style='display: block;']").remove();
						$("#attackTemp").parent().find("div[class='portlet light'][style='display: block;']").remove();
						$("#imitateTemp").parent().find("div[class='portlet light'][style='display: block;']").remove();
					}
					$.each(data,function(index,item){
						var maxIndex=getTaskMainMaxIndex();
						if(data[index].taskTypeMain == 1){
							$("#playBackBtnAdd").trigger("click");
						}
						if(data[index].taskTypeMain == 2){
							$("#attackBtnAdd").trigger("click");
						}
						if(data[index].taskTypeMain == 3){
							$("#imitateBtnAdd").trigger("click");
						}
						//场景任务
						if((data[index].taskTypeMain == 4 && isTemplate == 0)||(data[index].taskTypeMain == 4 && isTemplate == 2)){
							$("[name='taskMains[0].task_name']").val(data[index].taskNameMain);
							$("[name='taskMains[0].id']").val(data[index].taskMainIdMain);
							$("[name='taskMains[0].task_des']").val(data[index].taskDesMain);
							$("[name='taskMains[0].create_time']").val(data[index].createTimeMain);
							$("[name='taskMains[0].create_id']").val(data[index].createIdMain);
						}else if(data[index].taskTypeMain != 4){
							$("[name='taskMains["+maxIndex+"].task_name']").val(data[index].taskNameMain);
							if(data[index].taskTypeMain != 2){
								$("[name='taskMains["+maxIndex+"].task_run_times']").val(getNewData("",data[index].taskRunTimesMain,"0"));
								$("[name='taskMains["+maxIndex+"].task_run_times']").trigger("change");
								$("[name='taskMains["+maxIndex+"].task_duration_time_h']").val(getNewData("h",data[index].taskDurationTimeMain,"0"));
								$("[name='taskMains["+maxIndex+"].task_duration_time_m']").val(getNewData("m",data[index].taskDurationTimeMain,"0"));
								$("[name='taskMains["+maxIndex+"].task_duration_time_s']").val(getNewData("s",data[index].taskDurationTimeMain,"0"));
								$("[name='taskMains["+maxIndex+"].task_duration_time_h']").trigger("change");
								$("[name='taskMains["+maxIndex+"].task_duration_time_m']").trigger("change");
								$("[name='taskMains["+maxIndex+"].task_duration_time_s']").trigger("change");
							}else{
								$("[name='taskMains["+maxIndex+"].task_duration_time_h']").val(getNewData("h",data[index].taskDurationTimeMain,"0"));
								$("[name='taskMains["+maxIndex+"].task_duration_time_m']").val(getNewData("m",data[index].taskDurationTimeMain,"0"));
								$("[name='taskMains["+maxIndex+"].task_duration_time_s']").val(getNewData("s",data[index].taskDurationTimeMain,"0"));
							}
							$("[name='taskMains["+maxIndex+"].task_delay_time_h']").val(getNewData("h",data[index].taskDelayTimeMain,"0"));
							$("[name='taskMains["+maxIndex+"].task_delay_time_m']").val(getNewData("m",data[index].taskDelayTimeMain,"0"));
							$("[name='taskMains["+maxIndex+"].task_delay_time_s']").val(getNewData("s",data[index].taskDelayTimeMain,"0"));
							if(data[index].taskTypeMain == 3){
							$("[name='taskMains["+maxIndex+"].flow_speed']").val(getNewData("",data[index].flowSpeedMain,"-1"));
								$("[name='taskMains["+maxIndex+"].flow_speed']").trigger("change");
							}else{
								$("[name='taskMains["+maxIndex+"].flow_speed']").val(getNewData("",data[index].flowSpeedMain,"-1"));
							}
							$("[name='taskMains["+maxIndex+"].task_des']").val(data[index].taskDesMain);
						}
						//回放任务信息
						if(data[index].taskTypeMain == 1){
							if(data[index].replayOriginalReplay == 1){
								$("[name='taskReplayRules["+maxIndex+"].replay_original']").eq(0).attr("checked","checked");
								$("[name='taskReplayRules["+maxIndex+"].replay_original']").eq(0).parent().attr("class","checked");
								$("[name='taskReplayRules["+maxIndex+"].replay_original']").eq(0).trigger("click");
							}else{
								$("[name='taskReplayRules["+maxIndex+"].replay_original']").eq(1).attr("checked","checked");
								$("[name='taskReplayRules["+maxIndex+"].replay_original']").eq(1).parent().attr("class","checked");
								$("[name='taskReplayRules["+maxIndex+"].replay_original']").eq(1).trigger("click");
							}
							if(data[index].replayTypeReplay > -1){
								$("[name='taskReplayRules["+maxIndex+"].replay_type']").find("option[value='"+data[index].replayTypeReplay+"']").attr("selected",true);
								$("[name='taskReplayRules["+maxIndex+"].replay_type']").trigger("change");
							}
							if(data[index].replayFileReplay != "" && data[index].replayFileReplay != null && data[index].replayFileReplay != "-1"){
								$("[name='taskReplayRules["+maxIndex+"].replay_file']").val(data[index].replayFileReplay);
								$("[name='taskReplayRules["+maxIndex+"].replay_file']").trigger("change");
							}
						}
						//攻击信息
						if(data[index].taskTypeMain == 2){
							$("[name='taskAttackRules["+maxIndex+"].src_ip']").val(getNewData("",data[index].srcIpAttack,"-1"));
							$("[name='taskAttackRules["+maxIndex+"].src_ip_mask']").val(getNewData("",data[index].srcIpMaskAttack,"-1"));
							$("[name='taskAttackRules["+maxIndex+"].dst_ip']").val(getNewData("",data[index].dstIpAttack,"-1"));
							$("[name='taskAttackRules["+maxIndex+"].dst_ip_mask']").val(getNewData("",data[index].dstIpMaskAttack,"-1"));
							$("[name='taskAttackRules["+maxIndex+"].dst_ip_port']").val(getNewData("",data[index].dstIpPortAttack,"-1"));
							$("[name='taskAttackRules["+maxIndex+"].dst_mac']").val(data[index].dstMacAttack);
							$("[name='taskAttackRules["+maxIndex+"].attack_type']").val(data[index].attackTypeAttack);
							$("[name='taskAttackRules["+maxIndex+"].attack_type']").trigger("change");
							$("[name='taskAttackRules["+maxIndex+"].tcp_payload_length']").eq(0).val(getNewData("payload_length",data[index].tcpPayloadLengthAttack,"").split(",")[0]);
							$("[name='taskAttackRules["+maxIndex+"].tcp_payload_length']").eq(1).val(getNewData("payload_length",data[index].tcpPayloadLengthAttack,"").split(",")[1]);
						}
						//模拟信息
						if(data[index].taskTypeMain == 3){
							$("[name='taskImitateRules["+maxIndex+"].src_ip']").val(getNewData("",data[index].srcIpImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].src_ip_mask']").val(getNewData("",data[index].srcIpMaskImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].dst_ip']").val(getNewData("",data[index].dstIpImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].dst_ip_mask']").val(getNewData("",data[index].dstIpMaskImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].src_ip_port_type']").val(getNewData("",data[index].srcIpPortTypeImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].src_ip_port_type']").trigger("change");
							$("[name='taskImitateRules["+maxIndex+"].src_ip_port']").val(getNewData("",data[index].srcIpPortImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].max_nflow_second']").val(getNewData("",data[index].maxNflowSecondImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].max_cflow_second']").val(getNewData("",data[index].maxCflowSecondImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].flow_acontinue_h']").val(getNewData("h",data[index].flowAcontinueImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].flow_acontinue_m']").val(getNewData("m",data[index].flowAcontinueImitate,"-1"));
							$("[name='taskImitateRules["+maxIndex+"].flow_acontinue_s']").val(getNewData("s",data[index].flowAcontinueImitate,"-1"));
							if(data[index].imitateNetEnvImitate == 1 ){
								$("[name='taskImitateRules["+maxIndex+"].imitate_net_env']").eq(0).attr("checked","checked");
								$("[name='taskImitateRules["+maxIndex+"].imitate_net_env']").eq(0).parent().attr("class","checked");
								$("[name='taskImitateRules["+maxIndex+"].imitate_net_env']").eq(0).trigger("click");
							}else{
								$("[name='taskImitateRules["+maxIndex+"].imitate_net_env']").eq(1).attr("checked","checked");
								$("[name='taskImitateRules["+maxIndex+"].imitate_net_env']").eq(1).parent().attr("class","checked");
								$("[name='taskImitateRules["+maxIndex+"].imitate_net_env']").eq(1).trigger("click");
							}
							$("[name='taskImitateRules["+maxIndex+"].fragment_per']").val(getNewData("",data[index].fragmentPerImitate,"0"));
							$("[name='taskImitateRules["+maxIndex+"].replay_per']").val(getNewData("",data[index].replayPerImitate,"0"));
							$("[name='taskImitateRules["+maxIndex+"].unorder_per']").val(getNewData("",data[index].unorderPerImitate,"0"));
							$("[name='taskImitateRules["+maxIndex+"].damage_per']").val(getNewData("",data[index].damagePerImitate,"0"));
							$("[name='taskImitateRules["+maxIndex+"].message_delay']").val(getNewData("",data[index].messageDelayImitate,"0"));
							$("[name='taskImitateRules["+maxIndex+"].message_delay']").trigger("change");
							$("[name='taskImitateRules["+maxIndex+"].imitate_type']").val(data[index].imitateTypeImitate);
							$("[name='taskImitateRules["+maxIndex+"].imitate_type']").trigger("change");
							if(data[index].imitateTypeImitate == 1){
								$("[name='taskImitateHttpRules["+maxIndex+"].request_method']").val(data[index].requestMethodHttp);
								$("[name='taskImitateHttpRules["+maxIndex+"].request_version']").val(data[index].requestVersionHttp);
								$("[name='taskImitateHttpRules["+maxIndex+"].uset_agent']").val(data[index].usetAgentHttp);
								$("[name='taskImitateHttpRules["+maxIndex+"].cookie']").val(data[index].cookieHttp);
								$("[name='taskImitateHttpRules["+maxIndex+"].content_type']").val(data[index].contentTypeHttp);
								$("[name='taskImitateHttpRules["+maxIndex+"].keyword']").val(data[index].keywordHttp);
								$("[name='taskImitateHttpRules["+maxIndex+"].request_url']").val(data[index].requestUrlHttp);
							}
							if(data[index].imitateTypeImitate == 2){
								$("[name='taskImitateRuleMails["+maxIndex+"].from_mail']").val(data[index].fromMailMail);
								$("[name='taskImitateRuleMails["+maxIndex+"].to_mail']").val(data[index].toMailMail);
								$("[name='taskImitateRuleMails["+maxIndex+"].subject']").val(data[index].subjectMail);
								$("[name='taskImitateRuleMails["+maxIndex+"].content']").val(data[index].contentMail);
								$("[name='taskImitateRuleMails["+maxIndex+"].attachment_filename']").val(data[index].attachmentGilenameMail);
								$("[name='taskImitateRuleMails["+maxIndex+"].attachment_keyword']").val(data[index].attachmentKeywordMail);
							}
							if(data[index].imitateTypeImitate == 3){
								$("[name='taskImitateRuleFtps["+maxIndex+"].url']").val(data[index].urlFtp);
								$("[name='taskImitateRuleFtps["+maxIndex+"].download_file_name']").val(data[index].downloadFileNameFtp);
								$("[name='taskImitateRuleFtps["+maxIndex+"].download_file_name_keyword']").val(data[index].downloadFileNameKeywordFtp);
								$("[name='taskImitateRuleFtps["+maxIndex+"].uploaded_file_name']").val(data[index].uploadedFileNameFtp);
								$("[name='taskImitateRuleFtps["+maxIndex+"].uploaded_file_keyword']").val(data[index].uploadedFileKeywordFtp);
							}
						}
					});
				}
			}
		});
}
function getNewData(type,value,defaultVal){
	var valNew="";
	if(type=="h"){
		if(value > 0){
			value=parseInt(parseInt(value)/3600);
			if( value>= 1){
				return value;
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	if(type=="m"){
		if(value > 0){
			value=parseInt((parseInt(value)%3600)/60);
			if( value>= 1){
				return value;
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	if(type=="s"){
		if(value > 0 ){
			value=parseInt(parseInt(value)%60);
			if( value>= 1){
				return value;
			}else{
				return "";
			}
		}else{
			return "";
		}
		
	}
	if(type=="payload_length"){
		if( value !=null && value != ""){
			return value;
		}else{
		 return ",";
		}
	}
	if(defaultVal != null && defaultVal != ""  &&  value == defaultVal){
		return valNew;
	}else{
		return value;
	}
	return valNew;
}
//获取taskMain对象最大的index
function getTaskMainMaxIndex(){
	var maxIndex=-1;
	$("[name^='taskMains[']").each(function(){
			var index=$(this).attr("name").substring($(this).attr("name").indexOf("[")+1,$(this).attr("name").indexOf("]."));
			if(index > maxIndex){
				maxIndex=index;
			}
		});
	return (parseInt(maxIndex)+1);
}
//根据回放文件类型获取回放文件
function getFileList(obj,name) {
	name=$(obj).attr("name").split(".")[0]+"."+name;
	var optionVal = $(obj).val();
	if (optionVal != null && optionVal != '') {
		$.ajax({
					type : 'post',
					url : '/mesaweb/taskMainAction/queryReplayRuleByFT',
					data : {
						"fileType" : optionVal
					},
					dataType : 'json',
					async : false,
					success : function(data, textStatus) {
						$("[name^='"+name+"'] option").remove();
						$("[name^='"+name+"']").append("<option value=''>请选择</option>");
						if (data!=null && data.length > 0) {
							for ( var obj = 0; obj < data.length; obj++) {
								var option = "<option value='"+ data[obj].filePath
										+ "'>"
										+ data[obj].fileName + "</option>";
								$("[name^='"+name+"']").append(option);
							}
						}
					}
				});
	}
}
	
//禁用操作
function forbid(obj,type,min,max,name){
	var value;
	if(name == 'fileInfos'){
		value=$(obj).val();
		name=name+$(obj).attr("name").substring($(obj).attr("name").indexOf("["),$(obj).attr("name").indexOf("]")+1)
		if(value != null && value != ''){
			$("[name^='"+name+"']").attr("disabled",true);
			$("[name^='"+name+"']").next("input[type='file']").attr("disabled",true);
		}else{
			$("[name^='"+name+"']").attr("disabled",false);
			$("[name^='"+name+"']").next("input[type='file']").attr("disabled",false);
		}
	}else if(name == 'replay_file'){
		value = $(obj).val();
		if (value != '') {
			$(obj).parent().parent().parent().prev("div").find("[name*='replay_file']").attr("disabled", true);
		} else {
			$(obj).parent().parent().parent().prev("div").find("[name*='replay_file']").attr("disabled", false);
		}
	}else if(name == 'imitate_net_env'){
		var flag=true;
		var reg = new RegExp("^[0-9]*$");
		value = $("[name='"+$(obj).attr("name").split(".")[0]+".fragment_per']").val();
		if(reg.test(value) && flag && value >0){
 			flag=false;
		}
		value = $("[name='"+$(obj).attr("name").split(".")[0]+".replay_per']").val();
		if(reg.test(value) && flag && value >0){
 			flag=false;
		}
		value = $("[name='"+$(obj).attr("name").split(".")[0]+".unorder_per']").val();
		if(reg.test(value) && flag && value >0){
 			flag=false;
		}
		value = $("[name='"+$(obj).attr("name").split(".")[0]+".damage_per']").val();
		if(reg.test(value) && flag && value >0){
 			flag=false;
		}
		value = $("[name='"+$(obj).attr("name").split(".")[0]+".message_delay']").val();
		if(reg.test(value) && flag && value >0){
 			flag=false;
		}
		//是否模拟网络环境 0,否;1,是。默认0以下属性任何一个值不为0时该属性值为1
		if(!flag){
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(0).attr("checked","checked");
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(0).parent().attr("class","checked");
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(1).attr("checked","");
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(1).parent().attr("class","");
		}else{
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(1).attr("checked","checked");
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(1).parent().attr("class","checked");
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(0).attr("checked","");
			$("[name='"+$(obj).attr("name").split(".")[0]+".imitate_net_env']").eq(0).parent().attr("class","");
		}
		
	}else if(name == 'remove'){
		value=$(obj).parent().find("[name^='fileInfos']").val();
		if (value != '') {
			$(obj).parent().parent().prev("div").find("[name*='replay_file']").attr("disabled", true);
		} else {
			$(obj).parent().parent().prev("div").find("[name*='replay_file']").attr("disabled", false);
		}
	}else{
		value=$(obj).val();
		if("mustField" != name ){
			name=$(obj).attr("name").split(".")[0]+"."+name;
		}
		if(type=='number'){
			if(min < 0){
				if(value > max){
					if(name=="mustField"){
						$(obj).parent().parent().parent().find("div[id^='mustField']").attr("style","display:inline");
						resize();
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='src_ip']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='src_ip_mask']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='dst_ip']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='dst_ip_mask']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='max_nflow_second']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='max_cflow_second']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='flow_acontinue_h']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='flow_acontinue_m']").val('');
						$(obj).parent().parent().parent().find("div[id^='mustField']").find("[specialval='flow_acontinue_s']").val('');
					}else{
						$("[name^='"+name+"']").attr("disabled",true);
					}
				}else{
					//如果是回放次数，还需验证
					if(name == ($(obj).attr("name").split(".")[0]+".task_run_times")){
						var flagDis=false;
						$("[name^='"+$(obj).attr("name").split(".")[0]+".task_duration_time"+"']").each(function(){
							value=$(this).val();
							if(value > 0 && !flagDis){
								flagDis=true; 
							}
						})     
						$("[name^='"+name+"']").attr("disabled",flagDis);
						if(flagDis){
							$("[name^='"+name+"']").eq(0).parent().find(".help-block").remove();
						}
					}else if(name=="mustField"){
						$(obj).parent().parent().parent().find("div[id^='mustField']").attr("style","display:none");
					}else{
						$("[name^='"+name+"']").attr("disabled",false);
					}
				}
			}else{
				if(value < min){
					$("[name^='"+name+"']").attr("disabled",true);
					$("[name^='"+name+"']").eq(0).attr("placeholder","最小值").val('');
					$("[name^='"+name+"']").eq(1).attr("placeholder","最大值").val('');
					$("[name^='"+name+"']").eq(0).parent().find(".help-block").remove();
				}else{
					$("[name^='"+name+"']").attr("disabled",false);
					$("[name^='"+name+"']").attr("value","10");
				}
			}
		}else if(type=='varchar'){
			
			if( value != null && value.trim() !=""){
					if(name.indexOf("src_ip_port") == -1){
						$("[name^='"+name+"']").attr("disabled",true);
						$("[name^='"+name+"']").parent().find(".help-block").remove();
					}else if(value == '0'){
						$("[name='"+name+"']").attr("disabled",true);
						$("[name='"+name+"']").parent().find(".help-block").remove();
						$("[name='"+name+"']").val('');
					}else if(value == '1'){
						$("[name='"+name+"']").attr("disabled",false);
					}
			}else{
				$("[name^='"+name+"']").attr("disabled",false);
			}
		}
	}
}
	
//校验
function validateForm(obj,type){
	var specialVal;
	var valueType;
	var value;
	var minVal;
	var maxVal;
	var flag=true;
	var len;
	if(obj != null ){
		specialVal=$(obj).attr("specialVal");
		valueType=$(obj).attr("valueType");
		value=$(obj).attr("value");
		minVal=$(obj).attr("minVal");
		maxVal=$(obj).attr("maxVal");
		len=value.length;
		var reg = new RegExp("^[0-9]*$");
		
		//清空校验提示信息
		if(specialVal.indexOf("task_duration_time") > -1){
			$(obj).parent().parent().find("input[name*='task_duration_time']").eq(0).parent().find(".help-block").remove();
		}else if(specialVal.indexOf("task_delay_time") > -1 ){
			$(obj).parent().parent().find("input[name*='task_delay_time']").eq(0).parent().find(".help-block").remove();
		}else if(specialVal.indexOf("flow_acontinue") > -1 ){
			$(obj).parent().parent().find("input[name*='flow_acontinue']").eq(0).parent().find(".help-block").remove();
		}else if(specialVal.indexOf("tcp_payload_length") > -1 ){
			$(obj).parent().parent().find("input").eq(0).parent().find(".help-block").remove();
		}else{
			$(obj).parent().find(".help-block").remove();
		}
		
		//得到任务持续时间的总长度
		if(specialVal.indexOf("task_duration_time") > -1){
			$(obj).parent().parent().find("input[name*='task_duration_time']").each(function(){
				if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
					if(len <= 0){
						len=$(this).val().length;
					}
				}
			});
		}
		//获取流平均持续时间的总长度
		if(specialVal.indexOf("flow_acontinue") > -1){
			$(obj).parent().parent().find("input[name*='flow_acontinue']").each(function(){
				if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
					if(len <= 0){
						len=$(this).val().length;
					}
				}
			});
		}
		//获取任务延迟总长度
		if(specialVal.indexOf("task_delay_time") > -1){
			$(obj).parent().parent().find("input[name*='task_delay_time']").each(function(){
				if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
					if(len <= 0){
						len=$(this).val().length;
					}
				}
			});
		}
		
		//非空继续校验
		if(flag){
			//只要输入了值就进行校验
			if(len > 0){
				if(valueType == 'number'){
					//判断是否为数字
					if(!reg.test(value) && flag){
						flag=false;
						//任务持续时间
						if(specialVal.indexOf("task_duration_time") > -1){
							$(obj).parent().parent().find("input[name*='task_duration_time']").eq(0).parent().append("<span class='help-block'>必须为正整数</span>");
						}else if(specialVal.indexOf("task_delay_time") > -1 ){
						//任务延迟时间
							$(obj).parent().parent().find("input[name*='task_delay_time']").eq(0).parent().append("<span class='help-block'>必须为正整数</span>");
						}else if(specialVal.indexOf("flow_acontinue") > -1 ){
							$(obj).parent().parent().find("input[name*='flow_acontinue']").eq(0).parent().find(".help-block").remove();
							$(obj).parent().parent().find("input[name*='flow_acontinue']").eq(0).parent().append("<span class='help-block'>必须为正整数</span>");
						}else if(specialVal.indexOf("tcp_payload_length") > -1 ){
							$(obj).parent().parent().find("input").eq(0).parent().append("<span class='help-block'>必须为正整数</span>");
						}else{
							$(obj).parent().append("<span class='help-block'>必须为正整数</span>");
						}
					}
					value=parseInt(value);
					//得到任务持续时间的总时间
					if(specialVal.indexOf("task_duration_time") > -1){
						value=0;
						$(obj).parent().parent().find("input[name*='task_duration_time']").each(function(){
							if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
								value=parseInt($(this).val())+value;
							}
						});
					}
					//获取流平均持续时间的总时间
					if(specialVal.indexOf("flow_acontinue") > -1){
						value=0;
						$(obj).parent().parent().find("input[name*='flow_acontinue']").each(function(){
							if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
								value=parseInt($(this).val())+value;
							}
						});
					}
					//获取任务延迟总时间
					if(specialVal.indexOf("task_delay_time") > -1){
						value=0;
						$(obj).parent().parent().find("input[name*='task_delay_time']").each(function(){
							if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
								if($(this).attr("name").indexOf("task_delay_time_h") > -1){
									value=(parseInt($(this).val())*3600)+value;
								}
								if($(this).attr("name").indexOf("task_delay_time_m") > -1){
									value=(parseInt($(this).val())*60)+value;
								}
								if($(this).attr("name").indexOf("task_delay_time_s") > -1){
									value=parseInt($(this).val())+value;
								}
							}
						});
					}
					
					//判断值的大小
					if(value < minVal && flag){
						flag=false;
						if(specialVal.indexOf("task_duration_time") > -1){
							$(obj).parent().parent().find("input[name*='task_duration_time']").eq(0).parent().append("<span class='help-block'>总时间最小为"+minVal+"秒</span>");
						}else if(specialVal.indexOf("task_delay_time") > -1 ){
							$(obj).parent().parent().find("input[name*='task_delay_time']").eq(0).parent().append("<span class='help-block'>总时间最小为"+minVal+"秒</span>");
						}else if(specialVal.indexOf("flow_acontinue") > -1 ){
							$(obj).parent().parent().find("input[name*='flow_acontinue']").eq(0).parent().append("<span class='help-block'>总时间最小为"+minVal+"秒</span>");
						}else if(specialVal.indexOf("tcp_payload_length") > -1 ){
							$(obj).parent().parent().find("input").eq(0).parent().append("<span class='help-block'>最小为"+minVal+"</span>");
						}else{
							$(obj).parent().append("<span class='help-block'>最小为"+minVal+"</span>");
						}
					}
					//得到任务持续时间的总时间
					if(specialVal.indexOf("task_duration_time") > -1){
						value=$(obj).val();
					}
					//获取流平均持续时间的总时间
					if(specialVal.indexOf("flow_acontinue") > -1){
						value=$(obj).val();
					}
					//获取任务延迟总时间
					if(specialVal.indexOf("task_delay_time") > -1){
						value=$(obj).val();
					}
					if(value > maxVal && flag && maxVal != 0){
						flag=false;
						if(specialVal =="task_duration_time_m"){
							$(obj).parent().parent().find("input[name*='task_duration_time']").eq(0).parent().append("<span class='help-block'>分最大为"+maxVal+"</span>");
						}else if(specialVal =="task_duration_time_s"){
							$(obj).parent().parent().find("input[name*='task_duration_time']").eq(0).parent().append("<span class='help-block'>秒最大为"+maxVal+"</span>");
						}else if(specialVal =="task_delay_time_m"){
							$(obj).parent().parent().find("input[name*='task_delay_time']").eq(0).parent().append("<span class='help-block'>分最大为"+maxVal+"</span>");
						}else if(specialVal =="task_delay_time_s"){
							$(obj).parent().parent().find("input[name*='task_delay_time']").eq(0).parent().append("<span class='help-block'>秒最大为"+maxVal+"</span>");
						}else if(specialVal =="flow_acontinue_m"){
							$(obj).parent().parent().find("input[name*='flow_acontinue']").eq(0).parent().append("<span class='help-block'>分最大为"+maxVal+"</span>");
						}else if(specialVal =="flow_acontinue_s"){
							$(obj).parent().parent().find("input[name*='flow_acontinue']").eq(0).parent().append("<span class='help-block'>秒最大为"+maxVal+"</span>");
						}else if(specialVal.indexOf("tcp_payload_length") > -1 ){
							$(obj).parent().parent().find("input").eq(0).parent().append("<span class='help-block'>最大为"+maxVal+"</span>");
						}else{
							$(obj).parent().append("<span class='help-block'>最大为"+maxVal+"</span>");
						}
					}
					if(flag && specialVal.indexOf("tcp_payload_length") > -1){
						var minPayload=$(obj).parent().parent().find("input").eq(0).val();
						var maxPayload=$(obj).parent().parent().find("input").eq(1).val();
						if(minPayload != null && minPayload != '' && reg.test(minPayload) && 
							maxPayload != null && maxPayload != '' && reg.test(maxPayload) &&
							minPayload > maxPayload){
							flag=false;
							$(obj).parent().parent().find("input").eq(0).parent().append("<span class='help-block'>最小值不能大于最大值</span>");
						}
					}
					if(flag && specialVal.indexOf("flow_speed") > -1){
							var sumFlowSpeed=0;
							$("[name*='flow_speed']").each(function(){
								value=$(this).val();
								if(value != null && value !='' && reg.test(value)){
									sumFlowSpeed=sumFlowSpeed+parseInt($(this).val());
								}
							});
							if(sumFlowSpeed > 100000){
								$(obj).val('');
								flag=false;
								$(obj).parent().append("<span class='help-block'>总任务流量不允许大于100000</span>");
							}
					}
				}else{
					if(specialVal == "src_ip" || specialVal == "dst_ip"){
						if(!ipReg(trim(value))){
							flag=false;
							$(obj).parent().append("<span class='help-block'>IP输入有误</span>");
						}
					}
					if((specialVal == "src_ip_mask" || specialVal == "dst_ip_mask") && $(obj).attr("name").indexOf("taskImitateRules")>-1){
						if(!ipReg(trim(value))){
							flag=false;
							$(obj).parent().append("<span class='help-block'>掩码输入有误</span>");
						}
					}
					//攻击任务掩码需要单独校验
					if((specialVal == "src_ip_mask" || specialVal == "dst_ip_mask") && $(obj).attr("name").indexOf("taskAttackRules")>-1){
						if(!ipMarkReg(trim(value))){
							flag=false;
							$(obj).parent().append("<span class='help-block'>掩码输入有误</span>");
						}
					}
					if(specialVal == "dst_mac"){
						if(!dst_macReg(trim(value))){
							flag=false;
							$(obj).parent().append("<span class='help-block'>目的MAC例：AA:BB:CC:DD:EE:FF</span>");
						}
					}
					if(flag && len > maxVal && maxVal > 0){
						flag=false;
						$(obj).parent().append("<span class='help-block'>输入长度必须小于"+maxVal+"</span>");
					}
					if(specialVal == "task_name" && flag){
						flag=checkTaskName(type,trim(value));
						if(!flag){
							$(obj).parent().append("<span class='help-block'>任务名称已存在</span>");
						}
					}
					if( specialVal == "replay_file" && flag && value!= null && value !=""){
						//判断此回放文件是否存在
						if(flag && !isExist(value)){
							flag=false;
							$(obj).parent().append("<span class='help-block'>文件不存在</span>");
						}
						
						var replayType=$(obj).parent().parent().prev("div").find("select[specialval=replay_type]").val();
						if(flag && replayType==null || replayType==""){
							flag=false;
							$(obj).parent().parent().prev("div").find("select[specialval=replay_type]").parent().append("<span class='help-block'>任务类型不能为空</span>");
						}					
					}else if(specialVal == "fileInfos" && flag && value!= null && value !=""){
						var replayType=$(obj).parent().parent().parent().parent().parent().find("select[specialval=replay_type]").val();
						if(replayType==null || replayType==""){
							flag=false;
							$(obj).parent().parent().parent().parent().parent().find("select[specialval=replay_type]").parent().append("<span class='help-block'>任务类型不能为空</span>");
						}						
					}
				}
			}else if((type == 3 || specialVal=="task_name") && flag && (minVal != 0 || specialVal === "src_ip_port" || specialVal === "flow_speed")){
				if(specialVal.indexOf("task_delay_time") > -1){
					flag=false;
					$(obj).parent().parent().find("input[name*=task_delay_time]").eq(0).parent().append("<span class='help-block'>不允许为空</span>");
				}else if(specialVal.indexOf("replay_file") > -1 ){
					//获取文件
					var fileName=$(obj).parent().next("div").find("input[type=file]").val();
					if(fileName.length <= 0){
						flag=false;
						$(obj).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}else if(specialVal.indexOf("fileInfos") > -1 ){
					//获取文件
					var fileName=$(obj).parent().parent().parent().prev("div").find("select").val();
					if(fileName.length <= 0){
						flag=false;
						$(obj).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}else if(specialVal.indexOf("task_duration_time") > -1 ){
					//获取任务次数
					var task_run_times_val=0;
					if($(obj).attr("name").indexOf("attack") < 0){
						task_run_times_val=$("input[name='"+$(obj).attr("name").split(".")[0]+".task_run_times']").val();
					}
					if(task_run_times_val <= 0){
						flag=false;
						$(obj).parent().parent().find("input[name*=task_duration_time]").eq(0).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}else if(specialVal == "task_run_times" ){
					//获取任务持续时间
					value=0;
					$(obj).parent().parent().parent().find("input[name*='task_duration_time']").each(function(){
						if($(this).val() != null && $(this).val() != '' && reg.test($(this).val())){
								value=parseInt($(this).val())+value;
						}
					});
					//当任务持续时间为空时，任务次数不能为空
					if(value <= 0){
						flag=false;
						$(obj).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}else if(specialVal.indexOf("tcp_payload_length") > -1 ){
					//获取攻击类型
					var attack_type_val=$("input[name='"+$(obj).attr("name").split(".")[0]+".attack_type']").val();
					//攻击类型为tcp_payload_flood时，tcp长度不能为空
					if(attack_type_val == 2){
						flag=false;
						$(obj).parent().parent().find("input[name*=tcp_payload_length]").eq(0).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}else if(specialVal == "src_ip_port" ){
					//获取源端口类型
					var src_ip_port_type_val=$("select[name='"+$(obj).attr("name").split(".")[0]+".src_ip_port_type']").val();
					//源端口类型为1，下发时源端口不能为空
					if(src_ip_port_type_val == 1){
						flag=false;
						$(obj).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}else{
					//模拟任务以下情况flow_speed <=0 不进行非空校验
					var idIndex=$(obj).attr("name").substring($(obj).attr("name").indexOf("["),$(obj).attr("name").indexOf("]")+1);
					var flowSpeed=$("[name='taskMains"+idIndex+".flow_speed']").val();
					if( $(obj).attr("name").indexOf("taskImitateRules") > -1 && flowSpeed <= 0 && 
							(specialVal=='src_ip' || specialVal=='src_ip_mask'
							|| specialVal=='dst_ip' || specialVal=='dst_ip_mask'
							|| specialVal=='max_nflow_second' || specialVal=='max_cflow_second'
							|| specialVal=='flow_acontinue_h' ||specialVal=='flow_acontinue_m'
							||specialVal=='flow_acontinue_s') ){
						flag=true;
					}else if(specialVal.indexOf("flow_acontinue") > -1 ){
						flag=false;
						$(obj).parent().parent().find("input[name*=flow_acontinue]").eq(0).parent().append("<span class='help-block'>不允许为空</span>");
					}else{
						flag=false;
						$(obj).parent().append("<span class='help-block'>不允许为空</span>");
					}
				}
				
			}
		}
	}
	
	return flag;
}
//判断文件是否存在
function isExist(filePath){
	var flag=true;
	$.ajax({
			type:'post',
			url:'/mesaweb/ruleManager/checkFileName',
			data:{"filePath":filePath,"fileName":""},
			dataType:'text',
			async:false,
			success:function (data,textStatus){	
				//存在
				if(data == "false"){
					flag=true;
				}else{
					//不存在
					flag=false;
				}
			}
	});
	return flag;
}
//提交整体校验如果是下发，需要校验是否为空
function checkAll(type){
	var flag=true;
	var taskNums=$("[name*='.task_type']").length;
	if(taskNums > 7){
		$("[onblur^='validateForm']").each(function(){
			if( $(this).attr("name").indexOf("[") > -1 || $(this).attr("name").indexOf("task_name") > -1){
				if(flag){
					flag=validateForm(this,type);
				}else{
					validateForm(this,type);
				}
			}
		});
		if(!flag){
			alert("存在错误信息，请检查表单");
			resize();
		}
	}else{
		flag=false;
		alert("至少添加一个子任务");
	}
	return flag;
}
//校验任务名称
function checkTaskName(type,value){
	var flag=true;
	var taskId=$("input[name='taskMains[0].id']").val()
	$.ajax({
		type : 'post',
		url : '/mesaweb/trafficScene/checkTaskName',
		data : {
			"taskName":value,"type":type,"taskId":taskId
		},
		async : false,
		cache : false,
		dataType : 'text',
		success : function(data,result) {
			//存在
			if (data == "true") {
				flag=false;
			} else {
			//不存在
				flag=true;
			}
		}
	});
	return flag;
}
//根据下拉框的值显示http或ftp或mail的子参数
function changeService(obj) {
	var changeType = $(obj).val();
	var idIndex=$(obj).attr("name").substring($(obj).attr("name").indexOf("["),$(obj).attr("name").indexOf("]")+1);
	if (changeType == '1') {
		$("[id^='httpArea"+idIndex+"']").css("display", "inline");
		$("[id^='mailArea"+idIndex+"']").css("display", "none");
		$("[id^='ftpArea"+idIndex+"']").css("display", "none");
	}
	if (changeType == '2') {
		$("[id^='httpArea"+idIndex+"']").css("display", "none");
		$("[id^='mailArea"+idIndex+"']").css("display", "inline");
		$("[id^='ftpArea"+idIndex+"']").css("display", "none");
	}
	if (changeType == '3') {
		$("[id^='httpArea"+idIndex+"']").css("display", "none");
		$("[id^='mailArea"+idIndex+"']").css("display", "none");
		$("[id^='ftpArea"+idIndex+"']").css("display", "inline");
	}

}
//删除新增的div
function removeDiv(obj){
	$(obj).parent().parent().parent().remove();
}
function setChexkBoxCancle(obj) {
	$("input[type='checkbox'][name='td_ck']").each(function(i) {
		$(this).parent().attr('class', '');
		$(this).attr("checked", false);
	});
	$(obj).attr("checked", true);
}
//复选框/单选按钮选中和取消事件
function changeChecked(obj){
	var checked=$(obj).is(":checked");
	var type=$(obj).attr("type");
	if(checked){
		if(type=="radio"){
			$(obj).parent().parent().parent().parent().find("span").attr("class","");
			$(obj).parent().parent().parent().parent().find("span").find("input").attr("checked");
		}else{
			$("#sample_4").find("input[value!='"+$(obj).val()+"']").attr("class","").attr("checked","");
		}
		$(obj).parent().attr("class","checked");
		$(obj).attr("checked","checked");
	}else{
		$(obj).parent().attr("class","");
		$(obj).attr("checked","");
	}
}
//模板界面公共方法调用
function selectTemp(taskType,type){
	window.open("/mesaweb/trafficScene/selectTemp?task_type="+taskType+"&type="+type,"win","width=850px,height=550px,top=150px,left=250px,toolbar=no,menubar=no,scrollbars=no,location=no,resizable=no");
}
//返回
function reBack(){
	window.location.href="/mesaweb/trafficScene/queryTaskMainList";
}
//设置文件标志
function setFile(){
	$("[name^='fileInfos[']").each(function(){
		if($(this).val() != null && $(this).val() != ""){
			$(this).next("input").val($(this).val());
			$(this).attr("name",$(this).attr("name").substring(0,$(this).attr("name").indexOf("[")));
			
		}
	});
}
//保存所有任务信息
function submitForm(type) {
	//type：1保存为模板，2暂存，3下发,4修改模板
	$("#type").val(type);
	if(type == 1){
		$("input[name='taskMains[0].id']").val("");
	}
	if(checkAll(type)){
		//设置文件名称
		setFile();
		var formData = new FormData($("form[name='trafficSceneForm']")[0]);
		$.ajax({
					type : 'post',
					url : '/mesaweb/trafficScene/saveTaskInfo',
					data : formData,
					async : true,
					cache : false,
					processData:false,
					contentType:false,
					dataType : 'json',
					success : function(data,textStatus) {
						if(data == null || data == ""){
							alert("保存失败");
						}else if(data.resultType == -1){
							if(type!='4'){
								alert("保存成功");
							}else{
								alert("修改模板成功");
				                window.location.href = "/mesaweb/taskMainAction/queryTaskMainTemplateInfo";
							}
						}else if(data.resultType == 0 ){
							alert("任务下发成功");
							reBack();
						}else if(data.resultType == 2){
							//上传文件成功
							if(data.imitateFileState == 'true'){
								if (window.confirm("没有可用资源，是否暂存任务")) {
									alert("暂存任务成功");
									reBack();
								} else {
									//注销所有任务
									deleteTask(data.Pid);
								}
							}else{
								alert("模拟任务文件上传失败,任务已暂存！(文件上传服务器连接可能出现了问题)");
								reBack();
							}
						}
					},
					error : function() {
						alert("保存失败");
					}
				});
			
		}
}
//没有资源 注销任务
function deleteTask(taskId) {
$.ajax({
			type : 'post',
			url : '/mesaweb/trafficScene/deleteTask',
			data : {
				"taskId" : "'"+taskId+"'"
			},
			async : true,
			cache : false,
			dataType : 'text',
			success : function(data,textStatus) {
				alert(data);
				reBack();
			}
		});
}