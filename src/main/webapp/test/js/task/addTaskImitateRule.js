$(function() {
	resize(0);
    $("#templetName").attr("disabled", true);
    // 禁用模板任务
    $("span[name='proving']").css("display", "none");
    $("#flow_speed").keyup(function() {
        // 根据流量大小的值，判断是否显示必输项
        var reg = new RegExp("^[0-9]*$");
        var flowSpeed = trim($("#flow_speed").val());
        if (reg.test(flowSpeed)) {
            if (Number(flowSpeed) > 0) {
                // 当流量大小的值大于0时显示必输项
                $("#mustField").css("display", "inline");
                // 显示必输项时,清空必输项上一次失去焦点时提示的不能为空信息
                $("#val_src_ip").css("display", "none");
                $("#val_src_ip_mask").css("display", "none");
                $("#val_dst_ip").css("display", "none");
                $("#val_dst_ip_mask").css("display", "none");
                $("#val_src_ip_port").css("display", "none");
                $("#val_max_nflow_second").css("display", "none");
                $("#val_max_cflow_second").css("display", "none");
                $("#val_flow_acontinue").css("display", "none");
            } else {
                $("#mustField").css("display", "none");
            }
        } else {
            $("#mustField").css("display", "none");
        }
    }
    )

}
)

function submitForm(obj) {
    if (obj == "issued") {
        if (valIssued()) {
            $("#issueOrTemp").val(obj);
            formSubmit();
            // alert("issued");
        }
    }
    if (obj == "cache") {
        if (valCache()) {
            $("#issueOrTemp").val(obj);
            formSubmit();
            // alert("cache");
        }
    }
    if (obj == "templet") {
        if (valTemplet()) {
            $("#issueOrTemp").val(obj);
            formSubmit();
            // alert("templet");
        }
    }

}
function formSubmit() {
	window.parent.divShow();
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskImitateRuleAction/saveImitateInfo',
        data: $("form[name='taskMainForm']").serialize(),
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
        	window.parent.divHide();
            if (result.taskState == "-1") {
                alert("添加失败");
            }
            if (result.taskState == "0") {
                alert("添加成功");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            }
            if (result.taskState == "3") {
                alert("保存模板成功");
            }
            if (result.taskState == "5") {
            	if(result.fileExist=="1"){
            		alert("上传文件失败,远程服务器存在"+result.sameFileName+"同名文件,请修改文件名称后重新下发任务");
                	// window.location.href =
					// "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            	}else{
            		alert("上传文件失败,任务已暂存！(文件上传服务器连接可能出现了问题)");
                	window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            	}
            	
            }
            if (result.taskState == "1") {
                if (window.confirm("没有合适时间资源，将使用系统分配时间？")) {
                	setTaskMainStartAndEndTime(result.taskMainId);
                } else {
                    if (window.confirm("是否暂存任务")) {
                        updateIssuedTaskToCache(result.taskMainId);
                    } else {
                        resourceDel(result.taskMainId);
                    }
                }
            }
            if (result.taskState == "2") {
                if (window.confirm("没有可用资源，是否暂存任务")) {
                    alert("暂存任务成功");
                    window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
                } else {
                    deleteTaskMainAndRule(result.taskMainId)
                }
            }
        },
        error: function() {
            alert("添加失败");
        }
    });
}



function setTaskMainStartAndEndTime(taskId){
	$.ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/setTaskMainStartAndEndTime',
        data: {
            "id": taskId
        },
        async: false,
        cache: false,
        dataType: 'json',
        success: function(result) {
        	 window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
        },
        error: function() {
        	 window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
        }
    });
}
var valTaskName;
// 验证任务名称是否存在
function validateTaskName(obj) {
    $.ajax({
        type: 'post',
        url: '/mesaweb/taskImitateRuleAction/validateTaskName',
        data: {
            "taskName": obj
        },
        async: false,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                valTaskName = 1;
            } else {
                valTaskName = 0;
            }
        },
        error: function() {
            valTaskName = 0;
        }
    });
}
// 验证模板任务名称是否存在
function valTempletTaskName(obj) {
    $.ajax({
        type: 'post',
        url: '/mesaweb/taskImitateRuleAction/valTempletTaskName',
        data: {
            "taskName": obj
        },
        async: false,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                valTaskName = 1;
            } else {
                valTaskName = 0;
            }
        },
        error: function() {
            valTaskName = 0;
        }
    });
}

function updateIssuedTaskToCache(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/updateIssuedTaskToCache',
        data: {
            "taskMainId": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("暂存任务成功");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            } else {
                alert("任务暂存失败,任务已使用系统分配时间下发");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            }
        },
        error: function() {
            alert("任务暂存失败,任务已使用系统分配时间下发");
            window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
        }
    });
}

function deleteTaskMainAndRule(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskImitateRuleAction/deleteTaskMainAndRule',
        data: {
            "taskMainId": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("已取消保存任务为暂存状态,任务已注销");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            } else {
                alert("取消保存任务为暂存状态失败，已暂存任务");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            }
        },
        error: function() {
            alert("取消保存任务为暂存状态失败，已暂存任务");
            window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
        }
    });
}

function resourceDel(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskImitateRuleAction/delResource',
        data: {
            "id": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("取消使用系统分配时间,任务已删除");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            } else {
                alert("取消使用系统分配时间失败,任务已使用系统分配时间下发");
                window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
            }
        },
        error: function() {
            alert("取消使用系统分配时间失败,任务已使用系统分配时间下发");
            window.location.href = "/mesaweb/taskImitateRuleAction/queryTaskMainInfo";
        }
    });
}
var reg = new RegExp("^[0-9]*$");

function valIssued() {
    var num = 0;
    // 验证任务名称不为空
    if (trim($("#task_name").val()).length == 0) {
        $("#val_task_name").css("display", "inline");
        $("#val_task_name").html("任务名称不能为空");
        num = num + 1;
    } else {
        validateTaskName(trim($("#task_name").val()))
        if (valTaskName == 1) {
            $("#val_task_name").css("display", "inline");
            $("#val_task_name").html("任务名称已存在");
            num = num + 1;
        } else {
            $("#val_task_name").css("display", "none");
        }
    }
    // 验证任务次数
    var task_run_times = trim($("#task_run_times").val());
    var task_duration_time_h = trim($("#task_duration_time_h").val());
    var task_duration_time_m = trim($("#task_duration_time_m").val());
    var task_duration_time_s = trim($("#task_duration_time_s").val());
    if (task_run_times.length > 0) {
        if (!reg.test(task_run_times)) {
            $("#val_task_run_times").css("display", "inline");
            $("#val_task_run_times").html("请输入正整数");
            num = num + 1;
        } else {
            $("#val_task_run_times").css("display", "none");
            $("#val_task_run_times").html("");
            $("#val_task_duration_time").css("display", "none");
            $("#val_task_duration_time").html("");
        }
    } else {
        // 验证持续时间
        if (task_run_times.length == 0 || (Number(task_run_times) == 0)) {
            // 任务次数为空或0时验证任务持续时间
            if (task_duration_time_h.length > 0 || task_duration_time_m.length > 0 || task_duration_time_s.length > 0) {
                if (!valTaskTime(task_duration_time_h, task_duration_time_m, task_duration_time_s, 'val_task_duration_time')) {
                    num = num + 1;
                }
            } else {
                $("#val_task_run_times").css("display", "inline");
                $("#val_task_run_times").html("任务次数不能为空或0");
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("持续时间不能为空或0");
                num = num + 1;
            }
        }
    }
    
    if ((task_run_times.length == 0 || task_run_times == "0") && (task_duration_time_h.length == 0 || task_duration_time_h == "0") 
    && (task_duration_time_m.length == 0 || task_duration_time_m == "0") && (task_duration_time_s.length == 0 || task_duration_time_s == "0")) {
        $("#val_task_run_times").css("display", "inline");
        $("#val_task_run_times").html("任务次数不能为空或0");
        $("#val_task_duration_time").css("display", "inline");
        $("#val_task_duration_time").html("持续时间不能为空或0");
        num = num + 1;
    }
    
    
    
    // 验证延迟时间
    var task_delay_time_h = trim($("#task_delay_time_h").val());
    var task_delay_time_m = trim($("#task_delay_time_m").val());
    var task_delay_time_s = trim($("#task_delay_time_s").val());
    if (task_delay_time_h.length > 0 || task_delay_time_m.length > 0 || task_delay_time_s.length > 0) {
        if (!valTaskTime(task_delay_time_h, task_delay_time_m, task_delay_time_s, 'val_task_delay_time')) {
            num = num + 1;
        }
    } else {
        $("#val_task_delay_time").css("display", "inline");
        $("#val_task_delay_time").html("延迟时间不能为空");
        num = num + 1;
    }
    
    
    if ((task_delay_time_h.length == 0 || task_delay_time_h == "0") 
    && (task_delay_time_m.length == 0 || task_delay_time_m == "0") && (task_delay_time_s.length == 0 || task_delay_time_s == "0")) {
        $("#val_task_delay_time").css("display", "inline");
        $("#val_task_delay_time").html("最小延迟时间30秒");
        num = num + 1;
    }
    
    // 验证流量大小
    var flow_speed = trim($("#flow_speed").val());
    if (flow_speed.length > 0) {
        if (!valBlurFlowSpeed()) {
            num = num + 1;
        }
        if(Number(flow_speed)>0){
        	if (!validateMustField()) {
                num = num + 1;
            }
        }
        
    } else {
        $("#val_flow_speed").css("display", "inline");
        $("#val_flow_speed").html("回放大小不能为空");
        num = num + 1;
    }
    
    
    // 验证比例
    var fragment_per = $("#fragment_per").val();
    // 报文碎片化比例
    if (fragment_per.length > 0) {
        if (!validateRatio(fragment_per, 'val_fragment_per')) {
            num = num + 1;
        }
    } else {
        $("#val_fragment_per").css("display", "inline");
        $("#val_fragment_per").html("报文碎片化比例不能为空");
    }
    var replay_per = $("#replay_per").val();
    // 报文重放比例
    if (replay_per.length > 0) {
        if (!validateRatio(replay_per, 'val_replay_per')) {
            num = num + 1;
        }
    } else {
        $("#val_replay_per").css("display", "inline");
        $("#val_replay_per").html("报文重放比例不能为空");
        num = num + 1;
    }
    var unorder_per = $("#unorder_per").val();
    // 报文乱序比例
    if (unorder_per.length > 0) {
        if (!validateRatio(unorder_per, 'val_unorder_per')) {
            num = num + 1;
        }
    } else {
        $("#val_unorder_per").css("display", "inline");
        $("#val_unorder_per").html("报文乱序比例不能为空");
        num = num + 1;
    }
    var damage_per = $("#damage_per").val();
    // 报文损坏比例
    if (damage_per.length > 0) {
        if (!validateRatio(damage_per, 'val_damage_per')) {
            num = num + 1;
        }
    } else {
        $("#val_damage_per").css("display", "inline");
        $("#val_damage_per").html("报文损坏比例不能为空");
        num = num + 1;
    }
    var message_delay = $("#message_delay").val();
    // 报文延迟
    if (message_delay.length > 0) {
        if (!validateRatio(message_delay, 'val_message_delay')) {
            num = num + 1;
        }
    } else {
        $("#val_message_delay").css("display", "inline");
        $("#val_message_delay").html("报文延迟不能为空");
        num = num + 1;
    }
    if (num == 0) {
        return true;
    } else {
        return false;
    }
}
function valTemplet() {
    var count = 0;
    // 验证模板任务名称不为空
    if (trim($("#task_name").val()).length == 0) {
        $("#val_task_name").css("display", "inline");
        $("#val_task_name").html("模板任务名称不能为空");
        count = count + 1;
    } else {
        valTempletTaskName(trim($("#task_name").val()))
        if (valTaskName == 1) {
            $("#val_task_name").css("display", "inline");
            $("#val_task_name").html("模板任务名称已存在");
            count = count + 1;
        } else {
            $("#val_task_name").css("display", "none");
        }
    }
    if (!valFormat()) {
        count = count + 1;
    }
    if (count == 0) {
        return true;
    } else {
        return false
    }
}
function valCache() {
    var count = 0;
    // 验证任务名称不为空
    if (trim($("#task_name").val()).length == 0) {
        $("#val_task_name").css("display", "inline");
        $("#val_task_name").html("任务名称不能为空");
        count = count + 1;
    } else {
        validateTaskName(trim($("#task_name").val()))
        if (valTaskName == 1) {
            $("#val_task_name").css("display", "inline");
            $("#val_task_name").html("任务名称已存在");
            count = count + 1;
        } else {
            $("#val_task_name").css("display", "none");
        }
    }
    if (!valFormat()) {
        count = count + 1;
    }
    if (count == 0) {
        return true;
    } else {
        return false
    }
}
function valFormat() {
    var count = 0;
    // 验证任务次数
    var task_run_times = trim($("#task_run_times").val());
    if (task_run_times.length > 0) {
        if (!reg.test(task_run_times)) {
            $("#val_task_run_times").css("display", "inline");
            $("#val_task_run_times").html("请输入正整数");
            count = count + 1;
        } else {
            $("#val_task_run_times").css("display", "none");
            $("#val_task_run_times").html("");
            $("#val_task_duration_time").css("display", "none");
            $("#val_task_duration_time").html("");
        }
    } else {
        $("#val_task_run_times").css("display", "none");
        $("#val_task_run_times").html("");
    }
    
    // 验证持续时间
    var task_duration_time_h = trim($("#task_duration_time_h").val());
    var task_duration_time_m = trim($("#task_duration_time_m").val());
    var task_duration_time_s = trim($("#task_duration_time_s").val());
    var task_run_times = trim($("#task_run_times").val());
    if (task_run_times.length == 0 || (Number(task_run_times) == 0)) {
        // 任务次数为空或0时验证任务持续时间
        if (task_duration_time_h.length > 0 || task_duration_time_m.length > 0 || task_duration_time_s.length > 0) {
            if (!valTaskTime(task_duration_time_h, task_duration_time_m, task_duration_time_s, 'val_task_duration_time')) {
                count = count + 1;
            }
        } else {
            $("#val_task_duration_time").css("display", "none");
            $("#val_task_duration_time").html("");
        }
    }
    // 验证延迟时间
    var task_delay_time_h = trim($("#task_delay_time_h").val());
    var task_delay_time_m = trim($("#task_delay_time_m").val());
    var task_delay_time_s = trim($("#task_delay_time_s").val());
    if (task_delay_time_h.length > 0 || task_delay_time_m.length > 0 || task_delay_time_s.length > 0) {
        if (!valTaskTime(task_delay_time_h, task_delay_time_m, task_delay_time_s, 'val_task_delay_time')) {
            count = count + 1;
        }
    } else {
        $("#val_task_delay_time").css("display", "none");
        $("#val_task_delay_time").html("");
    }
    
    // 验证流量大小及必输项
    var flow_speed = trim($("#flow_speed").val());
    if (flow_speed.length > 0) {
        if (!valBlurFlowSpeed()) {
            count = count + 1;
        }
        if(Number(flow_speed)>0){
	        if (!validateMustField()) {
	            count = count + 1;
	        }
        }
    } else {
        $("#val_flow_speed").css("display", "none");
        $("#val_flow_speed").html("");
    }
    
    // 验证比例
    var fragment_per = $("#fragment_per").val();
    // 报文碎片化比例
    if (fragment_per.length > 0) {
        if (!validateRatio(fragment_per, 'val_fragment_per')) {
            count = count + 1;
        }
    } else {
        $("#val_fragment_per").css("display", "none");
    }
    var replay_per = $("#replay_per").val();
    // 报文重放比例
    if (replay_per.length > 0) {
        if (!validateRatio(replay_per, 'val_replay_per')) {
            count = count + 1;
        }
    } else {
        $("#val_replay_per").css("display", "none");
    }
    var unorder_per = $("#unorder_per").val();
    // 报文乱序比例
    if (unorder_per.length > 0) {
        if (!validateRatio(unorder_per, 'val_unorder_per')) {
            count = count + 1;
        }
    } else {
        $("#val_unorder_per").css("display", "none");
    }
    var damage_per = $("#damage_per").val();
    // 报文损坏比例
    if (damage_per.length > 0) {
        if (!validateRatio(damage_per, 'val_damage_per')) {
            count = count + 1;
        }
    } else {
        $("#val_damage_per").css("display", "none");
    }
    var message_delay = $("#message_delay").val();
    // 报文延迟
    if (message_delay.length > 0) {
        if (!validateRatio(message_delay, 'val_message_delay')) {
            count = count + 1;
        }
    } else {
        $("#val_message_delay").css("display", "none");
    }
    if (count == 0) {
        return true;
    } else {
        return false;
    }
}

// 验证比例
function validateRatio(objValue, valObj) {
    if (!reg.test(objValue)) {
        $("#" + valObj).css("display", "inline");
        $("#" + valObj).html("请输入正整数");
        return false;
    } else {
        if (!(Number(objValue) > 0)) {
            $("#" + valObj).css("display", "inline");
            $("#" + valObj).html("请输入正整数");
            return false;
        } else {
            $("#" + valObj).css("display", "none");
            return true;
        }
    }
}

// 验证必输项
function validateMustField() {
    var count = 0;
    // 验证源ip
    var src_ip = trim($("#src_ip").val());
    if (src_ip.length > 0) {
        if (ipReg(src_ip)) {
            $("#val_src_ip").css("display", "none");
            $("#val_src_ip").html("");
        } else {
            $("#val_src_ip").css("display", "inline");
            $("#val_src_ip").html("源ip格式不正确");
            count = count + 1;
        }
    } else {
        $("#val_src_ip").css("display", "inline");
        $("#val_src_ip").html("请输入源ip");
        count = count + 1;
    }
    
    // 验证源ip掩码
    var src_ip_mask = trim($("#src_ip_mask").val());
    if (src_ip_mask.length > 0) {
        if (ipReg(src_ip_mask)) {
            $("#val_src_ip_mask").css("display", "none");
            $("#val_src_ip_mask").html("");
        } else {
            $("#val_src_ip_mask").css("display", "inline");
            $("#val_src_ip_mask").html("源ip掩码格式不正确");
            count = count + 1;
        }
    } else {
        $("#val_src_ip_mask").css("display", "inline");
        $("#val_src_ip_mask").html("请输入源ip掩码");
        count = count + 1;
    }
    // 验证目的ip
    var dst_ip = trim($("#dst_ip").val());
    if (dst_ip.length > 0) {
        if (ipReg(dst_ip)) {
            $("#val_dst_ip").css("display", "none");
            $("#val_dst_ip").html("");
        } else {
            $("#val_dst_ip").css("display", "inline");
            $("#val_dst_ip").html("目的ip格式不正确");
            count = count + 1;
        }
    } else {
        $("#val_dst_ip").css("display", "inline");
        $("#val_dst_ip").html("请输入目的ip");
        count = count + 1;
    }
    // 验证目的ip掩码
    var dst_ip_mask = trim($("#dst_ip_mask").val());
    if (dst_ip_mask.length > 0) {
        if (ipReg(dst_ip_mask)) {
            $("#val_dst_ip_mask").css("display", "none");
            $("#val_dst_ip_mask").html("");
        } else {
            $("#val_dst_ip_mask").css("display", "inline");
            $("#val_dst_ip_mask").html("目的ip掩码格式不正确");
            count = count + 1;
        }
    } else {
        $("#val_dst_ip_mask").css("display", "inline");
        $("#val_dst_ip_mask").html("请输入目的ip掩码");
        count = count + 1;
    }
    
    // 验证源端口
    if ($("#src_ip_port_type").val() == "1") {
        // 当源端口类型为常数时验证源端口为必输项
        var src_ip_port = trim($("#src_ip_port").val());
        if (src_ip_port.length > 0) {
            if (!reg.test(src_ip_port)) {
                $("#val_src_ip_port").css("display", "inline");
                $("#val_src_ip_port").html("源端口格式不正确");
                count = count + 1;
            } else {
                var num = 0;
                if (!(Number(src_ip_port) > 0)) {
                    $("#val_src_ip_port").css("display", "inline");
                    $("#val_src_ip_port").html("请输入正整数");
                    count = count + 1;
                    num = 1;
                } else {
                    $("#val_src_ip_port").css("display", "none");
                    $("#val_src_ip_port").html("");
                }
                if ((Number(src_ip_port) > 65535) ) {
                    $("#val_src_ip_port").css("display", "inline");
                    $("#val_src_ip_port").html("源端口不能超过65535");
                    count = count + 1;
                } else {
                    if (num == 0) {
                        $("#val_src_ip_port").css("display", "none");
                        $("#val_src_ip_port").html("");
                    }
                }
            }
        } else {
            $("#val_src_ip_port").css("display", "inline");
            $("#val_src_ip_port").html("请输入源端口");
            count = count + 1;
        }
    
    }
    
    // 验证每秒最大新增流数
    var max_nflow_second = trim($("#max_nflow_second").val());
    if (!reg.test(max_nflow_second)) {
        $("#val_max_nflow_second").css("display", "inline");
        $("#val_max_nflow_second").html("请输入正整数");
        count = count + 1;
    } else {
        if (!(Number(max_nflow_second) > 0)) {
            $("#val_max_nflow_second").css("display", "inline");
            $("#val_max_nflow_second").html("请输入正整数");
            count = count + 1;
        } else {
            $("#val_max_nflow_second").css("display", "none");
        }
    }
    // 验证每秒最大并发流数
    var max_cflow_second = trim($("#max_cflow_second").val());
    if (!reg.test(max_cflow_second)) {
        $("#val_max_cflow_second").css("display", "inline");
        $("#val_max_cflow_second").html("请输入正整数");
        count = count + 1;
    } else {
        if (!(Number(max_cflow_second) > 0)) {
            $("#val_max_cflow_second").css("display", "inline");
            $("#val_max_cflow_second").html("请输入正整数");
            count = count + 1;
        } else {
            $("#val_max_cflow_second").css("display", "none");
        }
    }
    
    // 验证流平均持续时间
    var flow_acontinue_h = trim($("#flow_acontinue_h").val());
    var flow_acontinue_m = trim($("#flow_acontinue_m").val());
    var flow_acontinue_s = trim($("#flow_acontinue_s").val());
    if (flow_acontinue_h.length > 0 || flow_acontinue_m.length > 0 || flow_acontinue_s.length > 0) {
        if (!valTaskTime(flow_acontinue_h, flow_acontinue_m, flow_acontinue_s, 'val_flow_acontinue')) {
            count = count + 1;
        }
    } else {
        $("#val_flow_acontinue").css("display", "inline");
        $("#val_flow_acontinue").html("请输入正整数");
        count = count + 1;
    }
    
    if ((flow_acontinue_h == "" || flow_acontinue_h == "0") && (flow_acontinue_m == "" || flow_acontinue_m == "0") && (flow_acontinue_s == "" || flow_acontinue_s == "0")) {
        $("#val_flow_acontinue").css("display", "inline");
        $("#val_flow_acontinue").html("请输入正整数");
        count = count + 1;
    }
    
    if (count == 0) {
        return true;
    } else {
        return false;
    }
}



// 验证持续时间和延迟时间
function valTaskTime(hour, minute, second, valTime) {
    var count = 0;
    if (hour.length == 0 && minute.length == 0) {
        if (valTime == 'val_task_delay_time') {
            if (!reg.test(second)) {
                $("#" + valTime).css("display", "inline");
                $("#" + valTime).html("");
                $("#" + valTime).html("最小延迟时间30秒");
                count = count + 1;
            } else {
                if ((Number(second) >= 0 && Number(second) <= 29) ) {
                    $("#" + valTime).css("display", "inline");
                    $("#" + valTime).html("");
                    $("#" + valTime).html("最小延迟时间30秒");
                    count = count + 1;
                } else {
                    if (!(Number(second) >= 0 && Number(second) <= 59)) {
                        $("#" + valTime).css("display", "inline");
                        $("#" + valTime).html("");
                        $("#" + valTime).html("秒数必须大于等于0且小于等于59");
                        count = count + 1;
                    } else {
                        if (count == 0) {
                            $("#" + valTime).css("display", "none");
                        }
                    }
                
                }
            }
        } else {
            if (!reg.test(second)) {
                $("#" + valTime).css("display", "inline");
                $("#" + valTime).html("");
                $("#" + valTime).html("请输入持续时间");
                count = count + 1;
            } else {
                $("#" + valTime).css("display", "none");
            }
        }
    
    } else {
        if (!reg.test(hour)) {
            $("#" + valTime).css("display", "inline");
            $("#" + valTime).html("");
            $("#" + valTime).html("小时数必须大于等于0");
            count = count + 1;
        } else {
            if (!(Number(hour) >= 0)) {
                $("#" + valTime).css("display", "inline");
                $("#" + valTime).html("");
                $("#" + valTime).html("小时数必须大于等于0");
                count = count + 1;
            } else {
                $("#" + valTime).css("display", "none");
            }
        }
        if (!reg.test(minute)) {
            $("#" + valTime).css("display", "inline");
            $("#" + valTime).html("");
            $("#" + valTime).html("分钟数必须大于等于0且小于等于59");
            count = count + 1;
        } else {
            if (!(Number(minute) >= 0 && Number(minute) <= 59)) {
                $("#" + valTime).css("display", "inline");
                $("#" + valTime).html("");
                $("#" + valTime).html("分钟数必须大于等于0且小于等于59");
                count = count + 1;
            } else {
                if (count == 0) {
                    $("#" + valTime).css("display", "none");
                }
            }
        }
        if (!reg.test(second)) {
            $("#" + valTime).css("display", "inline");
            $("#" + valTime).html("");
            $("#" + valTime).html("秒数必须大于等于0且小于等于59");
            count = count + 1;
        } else {
            if (!(Number(second) >= 0 && Number(second) <= 59)) {
                $("#" + valTime).css("display", "inline");
                $("#" + valTime).html("");
                $("#" + valTime).html("秒数必须大于等于0且小于等于59");
                count = count + 1;
            } else {
                if (count == 0) {
                    $("#" + valTime).css("display", "none");
                }
            }
        }
    }
    
    if (count == 0) {
        return true;
    } else {
        return false;
    }
}
// 失去焦点时验证任务名称
function valBlurTaskName() {
    var taskName = trim($("#task_name").val());
    if (taskName.length == 0) {
        // 验证任务名称是否有值
        $("#val_task_name").html("请输入任务名称");
        $("#val_task_name").css("display", "inline");
    } else {
        $("#val_task_name").html("");
        $("#val_task_name").css("display", "none");
    }
}
// 回放次数失去焦点时验证数据格式,并根据值设置持续时间是否可输入
function valBlurRunTimes(obj, valObj) {
    var taskRunTimes = trim($("#" + obj).val());
    if (!reg.test(taskRunTimes)) {
        $("#" + valObj).css("display", "inline");
        $("#" + valObj).html("请输入正整数");
        $("#val_task_duration_time").css("display", "none");
        $("#val_task_duration_time").html("");
        disableTaskExcuteTime(taskRunTimes);
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
        $("#val_task_duration_time").css("display", "none");
        $("#val_task_duration_time").html("");
        disableTaskExcuteTime(taskRunTimes);
    }
}

// 验证延迟时间,持续时间失去鼠标焦点时,是否符合规范(并设置任务次数是否可输入)
function valBlurTime(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (obj.indexOf("_h") != -1) {
        if (obj == 'task_duration_time_h') {
            disabletaskRunTimes();
        }
        if (!(Number(objValue) >= 0)) {
            $("#" + valObj).html("请输入正整数");
            $("#" + valObj).css("display", "inline");
            return false;
        } else {
            if (obj == 'flow_acontinue_h') {
                if ($("#flow_acontinue_m").val().length == 0 && $("#flow_acontinue_s").val().length == 0) {
                    $("#" + valObj).html("流平均持续时间不能为空");
                    $("#" + valObj).css("display", "inline");
                    return false;
                } else {
                    $("#" + valObj).html("");
                    $("#" + valObj).css("display", "none");
                    return true;
                }
            } else {
                $("#" + valObj).html("");
                $("#" + valObj).css("display", "none");
                return true;
            }
        }
    }
    // 验证分钟和秒是否符合格式
    var judge = 0;
    if (objValue.length > 0) {
        if (!reg.test(objValue)) {
            judge = 1;
            // 不是整数
        } else {
            if (!(Number(objValue) >= 0 && Number(objValue) <= 59)) {
                judge = 2;
                // 不符规范
            } else {
                judge = 0;
            }
        }
        if (obj.indexOf("_m") != -1) {
            // 分钟
            if (judge == 1) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                if (obj == 'task_duration_time_m') {
                    disabletaskRunTimes();
                }
                return false;
            }
            if (judge == 2) {
                $("#" + valObj).html("分钟数必须大于等于0且小于等于59");
                $("#" + valObj).css("display", "inline");
                if (obj == 'task_duration_time_m') {
                    disabletaskRunTimes();
                }
                return false;
            }
            if (judge == 0) {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                if (obj == 'task_duration_time_m') {
                    disabletaskRunTimes();
                }
                return true;
            }
        }
        if (obj.indexOf("_s") != -1) {
            // 秒数
            if (judge == 1) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                if (obj == 'task_duration_time_s') {
                    disabletaskRunTimes();
                }
                return false;
            }
            if (judge == 2) {
                $("#" + valObj).html("秒数必须大于等于0且小于等于59");
                $("#" + valObj).css("display", "inline");
                if (obj == 'task_duration_time_s') {
                    disabletaskRunTimes();
                }
                return false;
            }
            if (judge == 0) {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                if (obj == 'task_duration_time_s') {
                    disabletaskRunTimes();
                }
                return true;
            }
        }
    
    } else {
        if (obj == 'flow_acontinue_m') {
            if ($("#flow_acontinue_h").val().length == 0 && $("#flow_acontinue_s").val().length == 0) {
                $("#" + valObj).html("流平均持续时间不能为空");
                $("#" + valObj).css("display", "inline");
                return false;
            } else {
                $("#" + valObj).html("");
                $("#" + valObj).css("display", "none");
                return true;
            }
        } else {
            if (obj == 'flow_acontinue_s') {
                if ($("#flow_acontinue_m").val().length == 0 && $("#flow_acontinue_h").val().length == 0) {
                    $("#" + valObj).html("流平均持续时间不能为空");
                    $("#" + valObj).css("display", "inline");
                    return false;
                } else {
                    $("#" + valObj).html("");
                    $("#" + valObj).css("display", "none");
                    return true;
                }
            } else {
                // 时分秒都没有值时设置任务次数可输入
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                if (valObj == 'val_task_duration_time') {
                    disabletaskRunTimes();
                }
            }
        }
    
    }
}

// 流量大小失去焦点验证数据格式
function valBlurFlowSpeed() {
    var objVal = trim($("#flow_speed").val());
    if (!reg.test(objVal)) {
        $("#val_flow_speed").css("display", "inline");
        $("#val_flow_speed").html("请输入正整数");
        return false;
    } else {
        if (!(Number(objVal) >= 0)) {
            $("#val_flow_speed").css("display", "inline");
            $("#val_flow_speed").html("请输入正整数");
            return false;
        } else {
            if ((Number(objVal) > 100000) ) {
                $("#val_flow_speed").css("display", "inline");
                $("#val_flow_speed").html("流量大小不能超过100000Mbps");
                return false;
            } else {
                $("#val_flow_speed").css("display", "none");
                return true;
            }
        }
    }
}

// 验证ip失去焦点时数据，是否符合规范
function valBlurIp(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (objValue.length > 0) {
        if (ipReg(objValue)) {
            $("#" + valObj).css("display", "none");
            $("#" + valObj).html("");
            return true;
        } else {
            $("#" + valObj).css("display", "inline");
            if (valObj == 'val_src_ip') {
                $("#" + valObj).html("源ip格式不正确");
            }
            if (valObj == 'val_src_ip_mask') {
                $("#" + valObj).html("源ip掩码格式不正确");
            }
            if (valObj == 'val_dst_ip') {
                $("#" + valObj).html("目的ip格式不正确");
            }
            if (valObj == 'val_dst_ip_mask') {
                $("#" + valObj).html("目的ip掩码格式不正确");
            }
            return false;
        }
    } else {
        $("#" + valObj).css("display", "inline");
        if (valObj == 'val_src_ip') {
            $("#" + valObj).html("源ip不能为空");
        }
        if (valObj == 'val_src_ip_mask') {
            $("#" + valObj).html("源ip掩码不能为空");
        }
        if (valObj == 'val_dst_ip') {
            $("#" + valObj).html("目的ip不能为空");
        }
        if (valObj == 'val_dst_ip_mask') {
            $("#" + valObj).html("目的ip掩码不能为空");
        }
        return false;
    }
}

// 验证ip掩码失去焦点时数据，是否符合规范
function valBlurIpMask(obj, valObj) {
    // 需求改为不要求验证掩码以255开头，和ip验证一样，所以掩码的失去焦点事件改为valBlurIp
    var objValue = trim($("#" + obj).val());
    if (objValue.length > 0) {
        if (ipMarkReg(objValue)) {
            $("#" + valObj).css("display", "none");
            $("#" + valObj).html("");
            return true;
        } else {
            $("#" + valObj).css("display", "inline");
            if (valObj == 'val_src_ip_mask') {
                $("#" + valObj).html("源ip掩码格式不正确");
            } else {
                $("#" + valObj).html("目的ip掩码格式不正确");
            }
            return false;
        }
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
    }
}

// 验证源端口失去焦点时数据，是否符合规范
function valBlurIpPort(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (objValue.length > 0) {
        if (!reg.test(objValue)) {
            $("#" + valObj).html("请输入正整数");
            $("#" + valObj).css("display", "inline");
            return false;
        } else {
            if (!(Number(objValue) > 0)) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                return false;
            } else {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
            }
            if ((Number(objValue) > 65535) ) {
                $("#" + valObj).html("源端口不能超过65535");
                $("#" + valObj).css("display", "inline");
                return false;
            } else {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
            }
        }
        return true;
    } else {
        $("#" + valObj).css("display", "inline");
        $("#" + valObj).html("源端口不能为空");
        return false;
    }
}

// 验证：每秒最大新增流数，每秒最大并发流数，报文碎片化比例，报文重放比例，报文乱序比例，报文损坏比例，报文延迟等数据格式
function valNumber(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (objValue.length > 0) {
        if (!reg.test(objValue)) {
            $("#" + valObj).css("display", "inline");
            $("#" + valObj).html("请输入正整数");
            return false;
        } else {
            if (!(Number(objValue) > 0)) {
                $("#" + valObj).css("display", "inline");
                $("#" + valObj).html("请输入正整数");
                return false;
            } else {
                $("#" + valObj).css("display", "none");
                return true;
            }
        }
    } else {
        if (valObj == 'val_max_nflow_second') {
            $("#" + valObj).css("display", "inline");
            $("#" + valObj).html("每秒最大新增流数不能为空");
            return false;
        } else {
            if (valObj == 'val_max_cflow_second') {
                $("#" + valObj).css("display", "inline");
                $("#" + valObj).html("每秒最大并发流数不能为空");
                return false;
            } else {
                $("#" + valObj).css("display", "none");
                return true;
            }
        }
    }

}

// 输入任务次数时禁用持续时间
function disableTaskExcuteTime(inputLength) {
    var task_duration_time_h = $("#task_duration_time_h");
    var task_duration_time_m = $("#task_duration_time_m");
    var task_duration_time_s = $("#task_duration_time_s");
    if (inputLength.length > 0) {
        if (Number(inputLength) != 0) {
            task_duration_time_h.attr("disabled", true);
            task_duration_time_m.attr("disabled", true);
            task_duration_time_s.attr("disabled", true);
        } else {
            task_duration_time_h.attr("disabled", false);
            task_duration_time_m.attr("disabled", false);
            task_duration_time_s.attr("disabled", false);
        }
    } else {
        task_duration_time_h.attr("disabled", false);
        task_duration_time_m.attr("disabled", false);
        task_duration_time_s.attr("disabled", false);
    }
}

// 输入持续时间时禁用任务次数
function disabletaskRunTimes() {
    var task_duration_time_h = trim($("#task_duration_time_h").val());
    var task_duration_time_m = trim($("#task_duration_time_m").val());
    var task_duration_time_s = trim($("#task_duration_time_s").val());
    if (task_duration_time_h.length > 0 || task_duration_time_m.length > 0 
    || task_duration_time_s.length > 0) {
        $("#task_run_times").attr("disabled", true);
    } else {
        $("#task_run_times").attr("disabled", false);
    }
    if (task_duration_time_h == "") {
        task_duration_time_h = 0;
    }
    if (task_duration_time_m == "") {
        task_duration_time_m = 0;
    }
    if (task_duration_time_s == "") {
        task_duration_time_s = 0;
    }
    if (task_duration_time_h == 0 && task_duration_time_m == 0 
    && task_duration_time_s == 0) {
        $("#task_run_times").attr("disabled", false);
    } else {
        $("#task_run_times").attr("disabled", true);
    }
}
// ip验证
function ipReg(ipAddr) {
    var regIps = /^(((25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|[0-9])\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|[0-9]))$/;
    return regIps.test(ipAddr);
}

// ip掩码验证
function ipMarkReg(ipMark) {
    var ipMarkReg = /^(255.(((25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|[0-9])\.){2})(25[0-5]|2[0-4]\d|1\d{2}|[1-9]\d|[0-9]))$/;
    return ipMarkReg.test(ipMark);
}
// 刷新页面
function reloadPage() {
    window.location.reload(true);
}
// 根据下拉框的值显示http或ftp或mail的子参数
function changeService(obj) {
    var changeType = $(obj).val()
    if (changeType == '1') {
        $("#httpArea").css("display", "inline");
        $("#mailArea").css("display", "none");
        $("#ftpArea").css("display", "none");
    }
    if (changeType == '2') {
        $("#httpArea").css("display", "none");
        $("#mailArea").css("display", "inline");
        $("#ftpArea").css("display", "none");
    }
    if (changeType == '3') {
        $("#httpArea").css("display", "none");
        $("#mailArea").css("display", "none");
        $("#ftpArea").css("display", "inline");
    }

}

// 去除字符串左右空格
function trim(obj) {
    return obj.replace(/(^\s*)|(\s*$)/g, "")
}
// 当源端口类型选择随机时，禁用源端口
function disabledIpPort(obj) {
    if ($(obj).val() == 0) {
        // 随机
        $("#src_ip_port").attr("disabled", true);
        $("#src_ip_port").val("");
        $("#val_src_ip_port").html("");
        $("#val_src_ip_port").css("display", "none");
    
    } else {
        $("#src_ip_port").attr("disabled", false);
    }
}

// 模拟网络环境时可以输入报文比例等信息，不模拟时禁用报文比例等信息
function disabelNetArea(obj) {
    if (obj == 1) {
        $("#fragment_per").attr("disabled", false);
        // 报文碎片化
        $("#replay_per").attr("disabled", false);
        // 报文重放
        $("#unorder_per").attr("disabled", false);
        // 报文乱序
        $("#damage_per").attr("disabled", false);
        // 报文损坏
        $("#message_delay").attr("disabled", false);
        // 报文延迟
        $("#percentageArea1").show();
        $("#percentageArea2").show();
    } else {
        $("#fragment_per").val("");
        // 报文碎片化
        $("#replay_per").val("");
        // 报文重放
        $("#unorder_per").val("");
        // 报文乱序
        $("#damage_per").val("");
        // 报文损坏
        $("#message_delay").val("");
        // 报文延迟
        
        $("#fragment_per").attr("disabled", true);
        // 报文碎片化
        $("#replay_per").attr("disabled", true);
        // 报文重放
        $("#unorder_per").attr("disabled", true);
        // 报文乱序
        $("#damage_per").attr("disabled", true);
        // 报文损坏
        $("#message_delay").attr("disabled", true);
        // 报文延迟
        
        $("#percentageArea1").hide();
        $("#percentageArea2").hide();
    
    }
}

// 获取模板列表
function getTempletTask() {
    var taskName = $("#taskTempletName").val();
    window.open("/mesaweb/trafficScene/selectTemp?task_type=3&type=3", "win", "width=850px,height=550px");
}
function setColumnValue(templetId) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskImitateRuleAction/getTempletTask',
        data: {
            "id": templetId
        },
        dataType: 'json',
        async: false,
        success: function(data, textStatus) {
            if (data == null ) {
                alert("获取模板信息失败");
            } else {
                // $("#task_name").val(data.taskMain.task_name);
                
                $("#task_run_times").val("");
                $("#task_duration_time_h").val("");
                $("#task_duration_time_m").val("");
                $("#task_duration_time_s").val("");
                
                if (data.taskMain.task_delay_time_h != 0) {
                    $("#task_delay_time_h").val(data.taskMain.task_delay_time_h);
                } else {
                    $("#task_delay_time_h").val("");
                }
                if (data.taskMain.task_delay_time_m != 0) {
                    $("#task_delay_time_m").val(
                    data.taskMain.task_delay_time_m);
                } else {
                    $("#task_delay_time_m").val("");
                }
                if (data.taskMain.task_delay_time_s != 0) {
                    $("#task_delay_time_s").val(
                    data.taskMain.task_delay_time_s);
                } else {
                    $("#task_delay_time_s").val("");
                }
                
                if (data.taskMain.task_run_times != "" 
                && data.taskMain.task_run_times != 0) {
                    $("#task_run_times").val(
                    data.taskMain.task_run_times);
                    $("#task_run_times").attr("disabled", false);
                    $("#task_duration_time_h").attr("disabled", true);
                    $("#task_duration_time_m").attr("disabled", true);
                    $("#task_duration_time_s").attr("disabled", true);
                } else {
                    if ((data.taskMain.task_duration_time_h != "" && data.taskMain.task_duration_time_h != 0) 
                    || (data.taskMain.task_duration_time_m != "" && data.taskMain.task_duration_time_m != 0) 
                    || (data.taskMain.task_duration_time_s != "" && data.taskMain.task_duration_time_s != 0)) {
                        $("#task_run_times").attr("disabled", true);
                    } else {
                        $("#task_run_times").attr("disabled", false);
                    }
                    if (data.taskMain.task_run_times != 0) {
                        $("#task_run_times").val(
                        data.taskMain.task_run_times);
                    } else {
                        $("#task_run_times").val("");
                    }
                    $("#task_duration_time_h").attr("disabled", false);
                    $("#task_duration_time_m").attr("disabled", false);
                    $("#task_duration_time_s").attr("disabled", false);
                    if (data.taskMain.task_duration_time_h != 0) {
                        $("#task_duration_time_h").val(
                        data.taskMain.task_duration_time_h);
                    } else {
                        $("#task_duration_time_h").val("");
                    }
                    if (data.taskMain.task_duration_time_m != 0) {
                        $("#task_duration_time_m").val(
                        data.taskMain.task_duration_time_m);
                    } else {
                        $("#task_duration_time_m").val("");
                    }
                    if (data.taskMain.task_duration_time_s != 0) {
                        $("#task_duration_time_s").val(
                        data.taskMain.task_duration_time_s);
                    } else {
                        $("#task_duration_time_s").val("");
                    }
                
                }
                
                if (data.taskMain.flow_speed != 0) {
                    $("#flow_speed").val(data.taskMain.flow_speed);
                } else {
                    $("#flow_speed").val("");
                }
                if (data.taskMain.flow_speed != "" 
                && data.taskMain.flow_speed != 0) {
                    $("#mustField").css("display", "inline");
                    $("#src_ip").val(data.taskImitateRule.src_ip);
                    $("#src_ip_mask").val(
                    data.taskImitateRule.src_ip_mask);
                    $("#dst_ip").val(data.taskImitateRule.dst_ip);
                    $("#dst_ip_mask").val(
                    data.taskImitateRule.dst_ip_mask);
                    $("#src_ip_port_type").val(
                    data.taskImitateRule.src_ip_port_type);
                    if (data.taskImitateRule.src_ip_port_type == 0) {
                        $("#src_ip_port").attr("disabled", true);
                        $("#src_ip_port").val("");
                    } else {
                        $("#src_ip_port").attr("disabled", false);
                        $("#src_ip_port").val(
                        data.taskImitateRule.src_ip_port);
                    }
                    
                    $("#max_nflow_second").val(
                    data.taskImitateRule.max_nflow_second);
                    $("#max_cflow_second").val(
                    data.taskImitateRule.max_cflow_second);
                    $("#flow_acontinue_h").val(
                    data.taskImitateRule.flow_acontinue_h);
                    $("#flow_acontinue_m").val(
                    data.taskImitateRule.flow_acontinue_m);
                    $("#flow_acontinue_s").val(
                    data.taskImitateRule.flow_acontinue_s);
                } else {
                    $("#mustField").css("display", "none");
                }
                if (data.taskImitateRule.imitate_net_env == 0) {
                    // 是否模拟网络环境否
                    $("#imitate_net_envNo").attr("checked", true);
                    $("#imitate_net_envYes").attr("checked", false);
                    $("#percentageArea1").hide();
                    $("#percentageArea2").hide();
                    $("#imitate_net_envNo").parent().attr("class", 
                    "checked");
                    $("#imitate_net_envYes").parent().attr("class", "");
                    
                    $("#fragment_per").val("");
                    // 报文碎片化
                    $("#replay_per").val("");
                    // 报文重放
                    $("#unorder_per").val("");
                    // 报文乱序
                    $("#damage_per").val("");
                    // 报文损坏
                    $("#message_delay").val("");
                    // 报文延迟
                    
                    $("#fragment_per").attr("disabled", true);
                    // 报文碎片化
                    $("#replay_per").attr("disabled", true);
                    // 报文重放
                    $("#unorder_per").attr("disabled", true);
                    // 报文乱序
                    $("#damage_per").attr("disabled", true);
                    // 报文损坏
                    $("#message_delay").attr("disabled", true);
                    // 报文延迟
                } else {
                    // 是否模拟网络环境是
                    $("#imitate_net_envNo").attr("checked", false);
                    $("#imitate_net_envYes").attr("checked", true);
                    $("#percentageArea1").show();
                    $("#percentageArea2").show();
                    $("#imitate_net_envNo").parent().attr("class", "");
                    $("#imitate_net_envYes").parent().attr("class", 
                    "checked");
                    $("#fragment_per").attr("disabled", false);
                    // 报文碎片化
                    $("#replay_per").attr("disabled", false);
                    // 报文重放
                    $("#unorder_per").attr("disabled", false);
                    // 报文乱序
                    $("#damage_per").attr("disabled", false);
                    // 报文损坏
                    $("#message_delay").attr("disabled", false);
                    // 报文延迟
                    
                    if (data.taskImitateRule.fragment_per != "-1" 
                    && data.taskImitateRule.fragment_per != "0") {
                        $("#fragment_per").val(
                        data.taskImitateRule.fragment_per);
                    } else {
                        $("#fragment_per").val("");
                    }
                    if (data.taskImitateRule.replay_per != "-1" 
                    && data.taskImitateRule.replay_per != "0") {
                        $("#replay_per").val(
                        data.taskImitateRule.replay_per);
                    } else {
                        $("#replay_per").val("");
                    }
                    if (data.taskImitateRule.unorder_per != "-1" 
                    && data.taskImitateRule.unorder_per != "0") {
                        $("#unorder_per").val(
                        data.taskImitateRule.unorder_per);
                    } else {
                        $("#unorder_per").val("");
                    }
                    if (data.taskImitateRule.damage_per != "-1" 
                    && data.taskImitateRule.damage_per != "0") {
                        $("#damage_per").val(
                        data.taskImitateRule.damage_per);
                    } else {
                        $("#damage_per").val("");
                    }
                    if (data.taskImitateRule.message_delay != "-1" 
                    && data.taskImitateRule.message_delay != "0") {
                        $("#message_delay").val(
                        data.taskImitateRule.message_delay);
                    } else {
                        $("#message_delay").val("");
                    }
                }
                
                addOrUpdate = data.taskImitateRule.imitate_type;
                $("#imitate_type").val(
                data.taskImitateRule.imitate_type);
                if (data.taskImitateRule.imitate_type == '1') {
                    $("#httpArea").css("display", "inline");
                    $("#mailArea").css("display", "none");
                    $("#ftpArea").css("display", "none");
                    $("#request_method").val(
                    data.taskImitateHttpRule.request_method);
                    $("#request_version").val(
                    data.taskImitateHttpRule.request_version);
                    $("#uset_agent").val(
                    data.taskImitateHttpRule.uset_agent);
                    $("#cookie").val(data.taskImitateHttpRule.cookie);
                    $("#content_type").val(
                    data.taskImitateHttpRule.content_type);
                    $("#keyword").val(data.taskImitateHttpRule.keyword);
                    $("#request_url").val(
                    data.taskImitateHttpRule.request_url);
                }
                if (data.taskImitateRule.imitate_type == '2') {
                    $("#httpArea").css("display", "none");
                    $("#mailArea").css("display", "inline");
                    $("#ftpArea").css("display", "none");
                    $("#from_mail").val(
                    data.taskImitateRuleMail.from_mail);
                    $("#to_mail").val(data.taskImitateRuleMail.to_mail);
                    $("#subject").val(data.taskImitateRuleMail.subject);
                    $("#content").val(data.taskImitateRuleMail.content);
                    $("#attachment_filename")
                    .val(
                    data.taskImitateRuleMail.attachment_filename);
                    $("#attachment_keyword")
                    .val(
                    data.taskImitateRuleMail.attachment_keyword);
                }
                if (data.taskImitateRule.imitate_type == '3') {
                    $("#httpArea").css("display", "none");
                    $("#mailArea").css("display", "none");
                    $("#ftpArea").css("display", "inline");
                    $("#url").val(data.taskImitateRuleFtp.url);
                    $("#download_file_name").val(
                    data.taskImitateRuleFtp.download_file_name);
                    $("#download_file_name_keyword")
                    .val(
                    data.taskImitateRuleFtp.download_file_name_keyword);
                    $("#uploaded_file_name").val(
                    data.taskImitateRuleFtp.uploaded_file_name);
                    $("#uploaded_file_keyword")
                    .val(
                    data.taskImitateRuleFtp.uploaded_file_keyword);
                }
            }
        }
    })
}

//子页面重新修改父页面iframe高度
function resize(height) {
	var realHeight = 0;
	if (navigator.userAgent.indexOf("Firefox") > 0 || navigator.userAgent.indexOf("Mozilla") > 0 || navigator.userAgent.indexOf("Safari") > 0 || navigator.userAgent.indexOf("Chrome") > 0) { // Mozilla,
		realHeight = window.document.documentElement.offsetHeight + 40+height; 
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
