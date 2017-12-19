$(function() {
    $("span[name='proving']").css("display", "none");
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
            // alert("templet");
            formSubmit();
        }
    }

}


function formSubmit() {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskAttackRuleAction/saveTaskAttackRule',
        data: $("form[name='taskMainForm']").serialize(),
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            
            if (result.taskState == "-1") {
                alert("添加失败");
            }
            if (result.taskState == "0") {
                alert("添加成功");
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            }
            if (result.taskState == "3") {
                alert("保存模板成功");
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
                    window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
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
            window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
        },
        error: function() {
            window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
        }
    });
}




var valTaskName;
// 验证任务名称是否存在
function validateTaskName(obj) {
    $.ajax({
        type: 'post',
        url: '/mesaweb/taskAttackRuleAction/validateTaskName',
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
        url: '/mesaweb/taskAttackRuleAction/valTempletTaskName',
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
    $.ajax({
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
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            } else {
                alert("任务暂存失败,任务已使用系统分配时间下发");
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            }
        },
        error: function() {
            alert("任务暂存失败,任务已使用系统分配时间下发");
            window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
        }
    });
}

function deleteTaskMainAndRule(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskAttackRuleAction/deleteTaskMainAndRule',
        data: {
            "taskMainId": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("已取消保存任务为暂存状态,任务已注销");
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            } else {
                alert("取消保存任务为暂存状态失败，已暂存任务");
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            }
        },
        error: function() {
            alert("取消保存任务为暂存状态失败，已暂存任务");
            window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
        }
    });
}

function resourceDel(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskAttackRuleAction/delResource',
        data: {
            "id": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("取消使用系统分配时间,任务已删除");
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            } else {
                alert("取消使用系统分配时间失败,任务已使用系统分配时间下发");
                window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
            }
        },
        error: function() {
            alert("取消使用系统分配时间失败,任务已使用系统分配时间下发");
            window.location.href = "/mesaweb/taskAttackRuleAction/queryTaskAttackRule";
        }
    });
}

var reg = new RegExp("^[0-9]*$");
// 去除字符串左右空格
function trim(obj) {
    return obj.replace(/(^\s*)|(\s*$)/g, "")
}

// 保存模板时验证
function valTemplet() {
    // 验证模板任务名称
    if (!valTaskTempletName()) {
        return false;
    }
    var count = valFormat()
    if (count != 0) {
        return false;
    }
    return true;
}
// 暂存时验证
function valCache() {
    // 验证任务名称
    if (!valTaskNameFun()) {
        return false;
    }
    var count = valFormat()
    if (count != 0) {
        return false;
    }
    return true;
}
// 下发时验证
function valIssued() {
    var count = 0;
    // 验证任务名称
    if (!valTaskNameFun()) {
        count = count + 1;
    }
    
    
    // 验证任务持续时间
    if (!valTaskDurationTime()) {
        count = count + 1;
    }
    
    // 验证任务延迟执行时间
    if (!valTaskDelayTime()) {
        count = count + 1;
    }
    
    // 验证发送速度
    var flow_speed = trim($("#flow_speed").val());
    if (flow_speed.length > 0) {
        if (!valFlowSpeed()) {
            count = count + 1;
        }
    } else {
        $("#val_flow_speed").html("发送速度不能为空");
        $("#val_flow_speed").css("display", "inline");
        count = count + 1;
    }
    
    
    
    // 验证源ip
    var src_ip = trim($("#src_ip").val());
    if (src_ip.length > 0) {
        if (!valSrcIp()) {
            count = count + 1;
        }
    } else {
        $("#val_src_ip").html("源ip不能为空");
        $("#val_src_ip").css("display", "inline");
        count = count + 1;
    }
    
    // 验证源ip掩码
    var src_ip_mask = trim($("#src_ip_mask").val());
    if (src_ip_mask.length > 0) {
        if (!valSrcIpMask()) {
            count = count + 1;
        }
    } else {
        $("#val_src_ip_mask").html("源ip掩码不能为空");
        $("#val_src_ip_mask").css("display", "inline");
        count = count + 1;
    }
    
    
    // 验证目的ip
    var dst_ip = trim($("#dst_ip").val());
    if (dst_ip.length > 0) {
        if (!valDstIp()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_ip").html("目的ip不能为空");
        $("#val_dst_ip").css("display", "inline");
        count = count + 1;
    }
    // 验证目的ip掩码
    var dst_ip_mask = trim($("#dst_ip_mask").val());
    if (dst_ip_mask.length > 0) {
        if (!valDstIpMask()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_ip_mask").html("目的ip掩码不能为空");
        $("#val_dst_ip_mask").css("display", "inline");
        count = count + 1;
    }
    
    // 如果输入目的端口，必须符合格式
    var dst_ip_port = trim($("#dst_ip_port").val());
    if (dst_ip_port.length > 0) {
        if (!valDstIpPort()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_ip_port").html("目的MAC不能为空");
        $("#val_dst_ip_port").css("display", "inline");
        count = count + 1;
    }
    // 验证目的MAC
    var dst_mac = trim($("#dst_mac").val());
    if (dst_mac.length > 0) {
        if (!valDstMac()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_mac").html("目的MAC不能为空");
        $("#val_dst_mac").css("display", "inline");
        count = count + 1;
    }
    
    // TCP负载长度
    var attack_type = $("#attack_type").val();
    if (attack_type == 2) {
        var min = trim($("#tcp_payload_length_min").val());
        var max = trim($("#tcp_payload_length_max").val());
        if (min.length > 0 && max.length > 0) {
            // TCP负载长度
            if (!valTcpPayLoadLength()) {
                count = count + 1;
            }
        } else {
            $("#val_tcp_payload_length").html("最大值或最小值不能为空");
            $("#val_tcp_payload_length").css("display", "inline");
            count = count + 1;
        
        }
    } else {
        $("#val_tcp_payload_length").css("display", "none");
    }
    
    if (count == 0) {
        return true;
    } else {
        return false;
    }
}


function valFormat() {
    var count = 0;
    // 验证任务持续时间
    var task_duration_time_h = trim($("#task_duration_time_h").val());
    var task_duration_time_m = trim($("#task_duration_time_m").val());
    var task_duration_time_s = trim($("#task_duration_time_s").val());
    if (task_duration_time_h.length > 0 || task_duration_time_m.length > 0 
    || task_duration_time_s.length > 0) {
        var show = 0;
        if (!reg.test(task_duration_time_h)) {
            $("#val_task_duration_time").css("display", "inline");
            $("#val_task_duration_time").html("小时数必须大于等于0");
            count = count + 1;
            show = 1;
        } else {
            if (!(Number(task_duration_time_h) >= 0)) {
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("小时数必须大于等于0");
                count = count + 1;
                show = 1;
            } else {
                if (show == 0) {
                    $("#val_task_duration_time").css("display", "none");
                    $("#val_task_duration_time").html("");
                }
            }
        }
        
        if (!reg.test(task_duration_time_m)) {
            $("#val_task_duration_time").css("display", "inline");
            $("#val_task_duration_time").html("分钟数必须大于等于0且小于等于59");
            count = count + 1;
            show = 1;
        } else {
            if (!(Number(task_duration_time_m) >= 0 && Number(task_duration_time_m) <= 59)) {
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("分钟数必须大于等于0且小于等于59");
                count = count + 1;
                show = 1;
            } else {
                if (show == 0) {
                    $("#val_task_duration_time").css("display", "none");
                    $("#val_task_duration_time").html("");
                }
            }
        }
        if (!reg.test(task_duration_time_s)) {
            $("#val_task_duration_time").css("display", "inline");
            $("#val_task_duration_time").html("秒数必须大于等于0且小于等于59");
            count = count + 1;
            show = 1;
        } else {
            if (!(Number(task_duration_time_s) >= 0 && Number(task_duration_time_s) <= 59)) {
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("秒数必须大于等于0且小于等于59");
                count = count + 1;
                show = 1;
            } else {
                if (show == 0) {
                    $("#val_task_duration_time").css("display", "none");
                    $("#val_task_duration_time").html("");
                }
            }
        }
    } else {
        $("#val_task_duration_time").css("display", "none");
        $("#val_task_duration_time").html("");
    }
    
    // 验证任务延迟执行时间
    var task_delay_time_h = trim($("#task_delay_time_h").val());
    var task_delay_time_m = trim($("#task_delay_time_m").val());
    var task_delay_time_s = trim($("#task_delay_time_s").val());
    
    if (task_delay_time_h.length > 0 || task_delay_time_m.length > 0 
    || task_delay_time_s.length > 0) {
        var show = 0;
        if (task_delay_time_h.length == 0 && task_delay_time_m.length == 0) {
            if (!reg.test(task_delay_time_s)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("最小延迟时间30秒");
                count = count + 1;
                show = 1;
            } else {
                if ((Number(task_delay_time_s) >= 0 && Number(task_delay_time_s) <= 29) ) {
                    $("#val_task_delay_time").css("display", "inline");
                    $("#val_task_delay_time").html("最小延迟时间30秒");
                    count = count + 1;
                    show = 1;
                } else {
                    if (show == 0) {
                        $("#val_task_delay_time").css("display", "none");
                        $("#val_task_delay_time").html("");
                    }
                }
            }
        } else {
            if (!reg.test(task_delay_time_h)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("小时数必须大于等于0");
                count = count + 1;
                show = 1;
            } else {
                if (!(Number(task_delay_time_h) >= 0)) {
                    $("#val_task_delay_time").css("display", "inline");
                    $("#val_task_delay_time").html("小时数必须大于等于0");
                    count = count + 1;
                    show = 1;
                } else {
                    if (show == 0) {
                        $("#val_task_delay_time").css("display", "none");
                        $("#val_task_delay_time").html("");
                    }
                }
            }
            if (!reg.test(task_delay_time_m)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("分钟数必须大于等于0且小于等于59");
                count = count + 1;
                show = 1;
            } else {
                if (!(Number(task_delay_time_m) >= 0 && Number(task_delay_time_m) <= 59)) {
                    $("#val_task_delay_time").css("display", "inline");
                    $("#val_task_delay_time").html("分钟数必须大于等于0且小于等于59");
                    count = count + 1;
                    show = 1;
                } else {
                    if (show == 0) {
                        $("#val_task_delay_time").css("display", "none");
                        $("#val_task_delay_time").html("");
                    }
                }
            }
            if (!reg.test(task_delay_time_s)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("秒数必须大于等于0且小于等于59");
                count = count + 1;
                show = 1;
            } else {
                if (!(Number(task_delay_time_s) >= 0 && Number(task_delay_time_s) <= 59)) {
                    $("#val_task_delay_time").css("display", "inline");
                    $("#val_task_delay_time").html("秒数必须大于等于0且小于等于59");
                    count = count + 1;
                    show = 1;
                } else {
                    if (show == 0) {
                        $("#val_task_delay_time").css("display", "none");
                        $("#val_task_delay_time").html("");
                    }
                }
            }
        }
    
    } else {
        $("#val_task_delay_time").css("display", "none");
        $("#val_task_delay_time").html("");
    }
    
    // 验证发送速度
    var flow_speed = trim($("#flow_speed").val());
    if (flow_speed.length > 0) {
        if (!valFlowSpeed()) {
            count = count + 1;
        }
    } else {
        $("#val_flow_speed").css("display", "none");
    }
    
    // 验证源ip
    var src_ip = trim($("#src_ip").val());
    if (src_ip.length > 0) {
        if (!valSrcIp()) {
            count = count + 1;
        }
    } else {
        $("#val_src_ip").css("display", "none");
    }
    
    // 验证源ip掩码
    var src_ip_mask = trim($("#src_ip_mask").val());
    if (src_ip_mask.length > 0) {
        if (!valSrcIpMask()) {
            count = count + 1;
        }
    } else {
        $("#val_src_ip_mask").css("display", "none");
    }
    // 验证目的ip
    var dst_ip = trim($("#dst_ip").val());
    if (dst_ip.length > 0) {
        if (!valDstIp()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_ip").css("display", "none");
    }
    // 验证目的ip掩码
    var dst_ip_mask = trim($("#dst_ip_mask").val());
    if (dst_ip_mask.length > 0) {
        if (!valDstIpMask()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_ip_mask").css("display", "none");
    }
    
    // 如果输入目的端口，必须符合格式
    var dst_ip_port = trim($("#dst_ip_port").val());
    if (dst_ip_port.length > 0) {
        if (!valDstIpPort()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_ip_port").css("display", "none");
    }
    // 验证目的MAC
    var dst_mac = trim($("#dst_mac").val());
    if (dst_mac.length > 0) {
        if (!valDstMac()) {
            count = count + 1;
        }
    } else {
        $("#val_dst_mac").css("display", "none");
    }
    
    // TCP负载长度
    var attack_type = $("#attack_type").val();
    if (attack_type == 2) {
        var min = trim($("#tcp_payload_length_min").val());
        var max = trim($("#tcp_payload_length_max").val());
        if (!reg.test(min) || !reg.test(max)) {
            $("#val_tcp_payload_length").html("请输入正整数");
            $("#val_tcp_payload_length").css("display", "inline");
            count = count + 1;
        }
    } else {
        $("#val_tcp_payload_length").css("display", "none");
    }
    
    return count;
}

// 验证模板任务名称
function valTaskTempletName() {
    var taskName = trim($("#task_name").val());
    if (taskName.length == 0) {
        // 验证任务名称是否有值
        $("#val_task_name").css("display", "inline");
        $("#val_task_name").html("请输入任务名称");
        return false;
    } else {
        valTempletTaskName(trim($("#task_name").val()))
        if (valTaskName == 1) {
            $("#val_task_name").css("display", "inline");
            $("#val_task_name").html("模板名称已存在");
            return false;
        } else {
            $("#val_task_name").css("display", "none");
            $("#val_task_name").html("");
            return true;
        }
    }
}

// 验证任务名称
function valTaskNameFun() {
    // 验证任务名称不为空
    if (trim($("#task_name").val()).length == 0) {
        $("#val_task_name").css("display", "inline");
        $("#val_task_name").html("请输入任务名称");
        return false;
    } else {
        validateTaskName(trim($("#task_name").val()))
        if (valTaskName == 1) {
            $("#val_task_name").css("display", "inline");
            $("#val_task_name").html("任务名称已存在");
            return false;
        } else {
            $("#val_task_name").css("display", "none");
            $("#val_task_name").html("");
        }
    }
    return true;
}


// 验证任务持续时间
function valTaskDurationTime() {
    var task_duration_time_h = trim($("#task_duration_time_h").val());
    var task_duration_time_m = trim($("#task_duration_time_m").val());
    var task_duration_time_s = trim($("#task_duration_time_s").val());
    if ((task_duration_time_h.length == 0 || task_duration_time_h == "0") 
    && (task_duration_time_m.length == 0 || task_duration_time_m == "0") && (task_duration_time_s.length == 0 || task_duration_time_s == "0")) {
        $("#val_task_duration_time").css("display", "inline");
        $("#val_task_duration_time").html("持续时间不能为空或0");
        return false;
    }
    if (task_duration_time_h.length > 0 || task_duration_time_m.length > 0 
    || task_duration_time_s.length > 0) {
        if (!reg.test(task_duration_time_h)) {
            $("#val_task_duration_time").css("display", "inline");
            $("#val_task_duration_time").html("小时数必须大于等于0");
            return false;
        } else {
            if (!(Number(task_duration_time_h) >= 0)) {
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("小时数必须大于等于0");
                return false;
            } else {
                $("#val_task_duration_time").css("display", "none");
                $("#val_task_duration_time").html("");
            }
        }
        
        if (!reg.test(task_duration_time_m)) {
            $("#val_task_duration_time").css("display", "inline");
            $("#val_task_duration_time").html("分钟数必须大于等于0且小于等于59");
            return false;
        } else {
            if (!(Number(task_duration_time_m) >= 0 && Number(task_duration_time_m) <= 59)) {
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("分钟数必须大于等于0且小于等于59");
                return false;
            } else {
                $("#val_task_duration_time").css("display", "none");
                $("#val_task_duration_time").html("");
            }
        }
        if (!reg.test(task_duration_time_s)) {
            $("#val_task_duration_time").css("display", "inline");
            $("#val_task_duration_time").html("秒数必须大于等于0且小于等于59");
            return false;
        } else {
            if (!(Number(task_duration_time_s) >= 0 && Number(task_duration_time_s) <= 59)) {
                $("#val_task_duration_time").css("display", "inline");
                $("#val_task_duration_time").html("秒数必须大于等于0且小于等于59");
                return false;
            } else {
                $("#val_task_duration_time").css("display", "none");
                $("#val_task_duration_time").html("");
            }
        }
    } else {
        $("#val_task_duration_time").css("display", "inline");
        $("#val_task_duration_time").html("请输入持续时间");
        return false;
    }
    return true;
}



// 验证任务延迟执行时间
function valTaskDelayTime() {
    var task_delay_time_h = trim($("#task_delay_time_h").val());
    var task_delay_time_m = trim($("#task_delay_time_m").val());
    var task_delay_time_s = trim($("#task_delay_time_s").val());
    if (task_delay_time_h.length == 0 && task_delay_time_m.length == 0) {
        if (!reg.test(task_delay_time_s)) {
            $("#val_task_delay_time").css("display", "inline");
            $("#val_task_delay_time").html("最小延迟时间30秒");
            return false;
        } else {
            if ((Number(task_delay_time_s) >= 0 && Number(task_delay_time_s) <= 29) ) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("最小延迟时间30秒");
                return false;
            } else {
                $("#val_task_delay_time").css("display", "none");
                $("#val_task_delay_time").html("");
            }
        }
    } else {
        if (!reg.test(task_delay_time_h)) {
            $("#val_task_delay_time").css("display", "inline");
            $("#val_task_delay_time").html("小时数必须大于等于0");
            return false;
        } else {
            if (!(Number(task_delay_time_h) >= 0)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("小时数必须大于等于0");
                return false;
            } else {
                $("#val_task_delay_time").css("display", "none");
                $("#val_task_delay_time").html("");
            }
        }
        if (!reg.test(task_delay_time_m)) {
            $("#val_task_delay_time").css("display", "inline");
            $("#val_task_delay_time").html("分钟数必须大于等于0且小于等于59");
            return false;
        } else {
            if (!(Number(task_delay_time_m) >= 0 && Number(task_delay_time_m) <= 59)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("分钟数必须大于等于0且小于等于59");
                return false;
            } else {
                $("#val_task_delay_time").css("display", "none");
                $("#val_task_delay_time").html("");
            }
        }
        if (!reg.test(task_delay_time_s)) {
            $("#val_task_delay_time").css("display", "inline");
            $("#val_task_delay_time").html("秒数必须大于等于0且小于等于59");
            return false;
        } else {
            if (!(Number(task_delay_time_s) >= 0 && Number(task_delay_time_s) <= 59)) {
                $("#val_task_delay_time").css("display", "inline");
                $("#val_task_delay_time").html("秒数必须大于等于0且小于等于59");
                return false;
            } else {
                $("#val_task_delay_time").css("display", "none");
                $("#val_task_delay_time").html("");
            }
        }
    }
    return true;
}


// 验证发送速度
function valFlowSpeed() {
    
    var flow_speed = trim($("#flow_speed").val());
    if (!reg.test(flow_speed)) {
        $("#val_flow_speed").html("请输入正整数");
        $("#val_flow_speed").css("display", "inline");
        return false;
    } else {
        if (!(Number(flow_speed) > 0)) {
            $("#val_flow_speed").html("请输入正整数");
            $("#val_flow_speed").css("display", "inline");
            return false;
        } else {
            if ((Number(flow_speed) > 100000) ) {
                $("#val_flow_speed").css("display", "inline");
                $("#val_flow_speed").html("发送速度不能超过100000Mbps");
                return false;
            } else {
                $("#val_flow_speed").css("display", "none");
                $("#val_flow_speed").html("");
            }
        }
    }
    return true;
}

// 验证源ip
function valSrcIp() {
    var src_ip = trim($("#src_ip").val());
    if (ipReg(src_ip)) {
        $("#val_src_ip").css("display", "none");
        $("#val_src_ip").html("");
    } else {
        $("#val_src_ip").css("display", "inline");
        $("#val_src_ip").html("源ip格式不正确");
        return false;
    }
    return true;
}


// 验证源ip掩码
function valSrcIpMask() {
    var src_ip_mask = trim($("#src_ip_mask").val());
    if (ipMarkReg(src_ip_mask)) {
        $("#val_src_ip_mask").css("display", "none");
        $("#val_src_ip_mask").html("");
    } else {
        $("#val_src_ip_mask").css("display", "inline");
        $("#val_src_ip_mask").html("源ip掩码格式不正确");
        return false;
    }
    return true;
}


// 验证目的ip
function valDstIp() {
    var dst_ip = trim($("#dst_ip").val());
    if (ipReg(dst_ip)) {
        $("#val_dst_ip").css("display", "none");
        $("#val_dst_ip").html("");
    } else {
        $("#val_dst_ip").css("display", "inline");
        $("#val_dst_ip").html("目的ip格式不正确");
        return false;
    }
    return true;
}
// 验证目的ip掩码
function valDstIpMask() {
    var dst_ip_mask = trim($("#dst_ip_mask").val());
    if (ipMarkReg(dst_ip_mask)) {
        $("#val_dst_ip_mask").css("display", "none");
        $("#val_dst_ip_mask").html("");
    } else {
        $("#val_dst_ip_mask").css("display", "inline");
        $("#val_dst_ip_mask").html("目的ip掩码格式不正确");
        return false;
    }
    return true;
}
// 验证目的端口
function valDstIpPort() {
    var dst_ip_port = trim($("#dst_ip_port").val());
    if (!reg.test(dst_ip_port)) {
        $("#val_dst_ip_port").html("请输入正整数");
        $("#val_dst_ip_port").css("display", "inline");
        return false;
    } else {
        if (!(Number(dst_ip_port) > 0)) {
            $("#val_dst_ip_port").html("请输入正整数");
            $("#val_dst_ip_port").css("display", "inline");
            return false;
        } else {
            $("#val_dst_ip_port").css("display", "none");
            $("#val_dst_ip_port").html("");
        }
        if ((Number(dst_ip_port) > 65535) ) {
            $("#val_dst_ip_port").html("目的端口不能超过65535");
            $("#val_dst_ip_port").css("display", "inline");
            return false;
        } else {
            $("#val_dst_ip_port").css("display", "none");
            $("#val_dst_ip_port").html("");
        }
    }
    return true;
}
// 验证目的MAC
function valDstMac() {
    var dst_mac = trim($("#dst_mac").val());
    if (dst_macReg(dst_mac)) {
        $("#val_dst_mac").css("display", "none");
        $("#val_dst_mac").html("");
    } else {
        $("#val_dst_mac").css("display", "inline");
        $("#val_dst_mac").html("目的MAC格式不正确");
        return false;
    }
    return true;
}


// 验证攻击类型
function valAttackType() {
    var attack_type = $("#attack_type").val();
    if (attack_type == '') {
        $("#val_attack_type").css("display", "inline");
        return false;
    } else {
        $("#val_attack_type").css("display", "none");
    }
    return true;
}


// TCP负载长度
function valTcpPayLoadLength() {
    var min = trim($("#tcp_payload_length_min").val());
    var max = trim($("#tcp_payload_length_max").val());
    if (!reg.test(min) || !reg.test(max)) {
        $("#val_tcp_payload_length").html("请输入正整数");
        $("#val_tcp_payload_length").css("display", "inline");
        return false;
    } else {
        if (Number(min) < 10) {
            $("#val_tcp_payload_length").html("最小值介于(10-1460)");
            $("#val_tcp_payload_length").css("display", "inline");
            return false;
        } else {
            if (Number(min) > Number(max)) {
                $("#val_tcp_payload_length").html("最小值不能大于最大值");
                $("#val_tcp_payload_length").css("display", "inline");
                return false;
            } else {
                if (Number(max) > 1460) {
                    $("#val_tcp_payload_length").html("最大值介于(10-1460)");
                    $("#val_tcp_payload_length").css("display", "inline");
                    return false;
                } else {
                    $("#val_tcp_payload_length").css("display", "none");
                    $("#val_tcp_payload_length").html("");
                    return true;
                }
            }
        }
    }
    return true;
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

// 验证延迟时间,持续时间失去鼠标焦点时,是否符合规范
function valBlurTime(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (obj == 'task_delay_time_h' || obj == 'task_duration_time_h') {
        if (!(Number(objValue) >= 0)) {
            $("#" + valObj).html("请输入正整数");
            $("#" + valObj).css("display", "inline");
            return false;
        } else {
            $("#" + valObj).html("");
            $("#" + valObj).css("display", "none");
            return true;
        }
    }
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
        if (obj == 'task_duration_time_m') {
            // 持续时间分钟
            if (judge == 1) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 2) {
                $("#" + valObj).html("分钟数必须大于等于0且小于等于59");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 0) {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                return true;
            }
        
        }
        if (obj == 'task_duration_time_s') {
            // 持续时间秒数
            if (judge == 1) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 2) {
                $("#" + valObj).html("秒数必须大于等于0且小于等于59");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 0) {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                return true;
            }
        }
        if (obj == 'task_delay_time_m') {
            // 延迟时间分钟
            if (judge == 1) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 2) {
                $("#" + valObj).html("分钟数必须大于等于0且小于等于59");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 0) {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                return true;
            }
        }
        if (obj == 'task_delay_time_s') {
            // 延迟时间秒数
            if (judge == 1) {
                $("#" + valObj).html("请输入正整数");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 2) {
                $("#" + valObj).html("秒数必须大于等于0且小于等于59");
                $("#" + valObj).css("display", "inline");
                return false;
            }
            if (judge == 0) {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
                return true;
            }
        }
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
    }
}

// 验证发送速度失去焦点时数据，是否符合规范
function valBlurFlowSpeed(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (objValue.length > 0) {
        if (!(Number(objValue) > 0)) {
            $("#" + valObj).css("display", "inline");
            $("#" + valObj).html("请输入正整数");
            return false;
        } else {
            if ((Number(objValue) > 100000) ) {
                $("#" + valObj).css("display", "inline");
                $("#" + valObj).html("发送速度不能超过100000Mbps");
                return false;
            } else {
                $("#" + valObj).html("");
                $("#" + valObj).css("display", "none");
                return true;
            }
        }
    } else {
        $("#" + valObj).html("");
        $("#" + valObj).css("display", "none");
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
            } else {
                $("#" + valObj).html("目的ip格式不正确");
            }
            return false;
        }
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
    }
}


// 验证ip掩码失去焦点时数据，是否符合规范
function valBlurIpMask(obj, valObj) {
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

// 验证目的端口失去焦点时数据，是否符合规范
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
                $("#" + valObj).html("目的端口不能超过65535");
                $("#" + valObj).css("display", "inline");
                return false;
            } else {
                $("#" + valObj).css("display", "none");
                $("#" + valObj).html("");
            }
        }
        return true;
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
    }
}
// 验证mac失去焦点时数据，是否符合规范
function valBlurMac(obj, valObj) {
    var objValue = trim($("#" + obj).val());
    if (objValue.length > 0) {
        if (dst_macReg(objValue)) {
            $("#" + valObj).css("display", "none");
            $("#" + valObj).html("");
            return true;
        } else {
            $("#" + valObj).css("display", "inline");
            $("#" + valObj).html("目的MAC格式不正确");
            return false;
        }
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
    }
}

// TCP负载长度失去焦点时数据，是否符合规范
function valBlurTcpPayLoad(valObj) {
    var min = trim($("#tcp_payload_length_min").val());
    var max = trim($("#tcp_payload_length_max").val());
    if (!reg.test(min) || !reg.test(max)) {
        $("#val_tcp_payload_length").html("请输入正整数");
        $("#val_tcp_payload_length").css("display", "inline");
        return false;
    } else {
        if (Number(min) < 10) {
            $("#val_tcp_payload_length").html("最小值介于(10-1460)");
            $("#val_tcp_payload_length").css("display", "inline");
            return false;
        } else {
            if (Number(min) > Number(max)) {
                $("#val_tcp_payload_length").html("最小值不能大于最大值");
                $("#val_tcp_payload_length").css("display", "inline");
                return false;
            } else {
                if (Number(max) > 1460) {
                    $("#val_tcp_payload_length").html("最大值介于(10-1460)");
                    $("#val_tcp_payload_length").css("display", "inline");
                    return false;
                } else {
                    $("#val_tcp_payload_length").css("display", "none");
                    $("#val_tcp_payload_length").html("");
                    return true;
                }
            }
        }
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
// 验证mac
function dst_macReg(dst_mac) {
    var dst_macReg = /^(([A-Fa-f0-9]{2}\:){5}([A-Fa-f0-9]{2}))$/;
    return dst_macReg.test(dst_mac);
}


function reloadPage() {
    window.location.reload(true);
}

function setTcpPayloadLength(obj) {
    if ($(obj).val() == "2") {
        $("#tcp_payload_length_max").attr("readonly", false);
        $("#tcp_payload_length_min").attr("readonly", false);
        if ($("#tcp_payload_length_min").val() == "" 
        && $("#tcp_payload_length_max").val() == "") {
            $("#tcp_payload_length_min").val("10");
            $("#tcp_payload_length_max").val("10");
        }
    } else {
        if ($("#tcp_payload_length_min").val() != "" 
        && $("#tcp_payload_length_max").val() != "") {
            $("#tcp_payload_length_min").val("");
            $("#tcp_payload_length_max").val("");
        }
        $("#tcp_payload_length_max").attr("readonly", true);
        $("#tcp_payload_length_min").attr("readonly", true);
    }

}

// 获取模板列表
function getTempletTask() {
    var taskName = $("#taskTempletName").val();
    window.open("/mesaweb/trafficScene/selectTemp?task_type=2&type=2", "win", "width=850px,height=550px");
}
function setColumnValue(templetId) {
    $.ajax({
        type: 'post',
        url: '/mesaweb/taskAttackRuleAction/getTempletTask',
        data: {
            "id": templetId
        },
        dataType: 'json',
        async: false,
        success: function(data, textStatus) {
            if (data == null ) {
                // $("#task_name").val("");
                $("#task_duration_time_h").val("");
                $("#task_duration_time_m").val("");
                $("#task_duration_time_s").val("");
                $("#task_delay_time_h").val("");
                $("#task_delay_time_m").val("");
                $("#task_delay_time_s").val("");
                $("#flow_speed").val("");
                if (data.taskAttackRule.src_ip == "-1") {
                    $("#src_ip").val("");
                }
                if (data.taskAttackRule.src_ip_mask == "-1") {
                    $("#src_ip_mask").val("");
                }
                if (data.taskAttackRule.dst_ip == "-1") {
                    $("#dst_ip").val("");
                }
                if (data.taskAttackRule.dst_ip_mask == "-1") {
                    $("#dst_ip_mask").val("");
                }
                if (data.taskAttackRule.dst_ip_port == "-1") {
                    $("#dst_ip_port").val("");
                }
                if (data.taskAttackRule.dst_mac == "-1") {
                    $("#dst_mac").val("");
                }
                if (data.taskAttackRule.attack_type == "-1") {
                    
                    if (data.taskAttackRule.attack_type == "1") {
                        $("#attack_type").val(1);
                        $("#tcp_payload_length_min").val("");
                        $("#tcp_payload_length_max").val("");
                        $("#tcp_payload_length_max").attr("readonly", true);
                        $("#tcp_payload_length_min").attr("readonly", true);
                    } else {
                        $("#attack_type").val(2);
                        $("#tcp_payload_length_max").attr("readonly", false);
                        $("#tcp_payload_length_min").attr("readonly", false);
                        $("#tcp_payload_length_max").val(10);
                        $("#tcp_payload_length_min").val(10);
                    }
                }
            } else {
                // $("#task_name").val(data.taskMain.task_name);
                if (data.taskMain.task_duration_time_h != 0) {
                    $("#task_duration_time_h").val(data.taskMain.task_duration_time_h);
                } else {
                    $("#task_duration_time_h").val("");
                }
                if (data.taskMain.task_duration_time_m != 0) {
                    $("#task_duration_time_m").val(data.taskMain.task_duration_time_m);
                } else {
                    $("#task_duration_time_m").val("");
                }
                if (data.taskMain.task_duration_time_s != 0) {
                    $("#task_duration_time_s").val(data.taskMain.task_duration_time_s);
                } else {
                    $("#task_duration_time_s").val("");
                }
                if (data.taskMain.task_delay_time_h != 0) {
                    $("#task_delay_time_h").val(data.taskMain.task_delay_time_h);
                } else {
                    $("#task_delay_time_h").val("");
                }
                if (data.taskMain.task_delay_time_m != 0) {
                    $("#task_delay_time_m").val(data.taskMain.task_delay_time_m);
                } else {
                    $("#task_delay_time_m").val("");
                }
                if (data.taskMain.task_delay_time_s != 0) {
                    $("#task_delay_time_s").val(data.taskMain.task_delay_time_s);
                } else {
                    $("#task_delay_time_s").val("");
                }
                if (data.taskMain.flow_speed != 0) {
                    $("#flow_speed").val(data.taskAttackRule.flow_speed);
                } else {
                    $("#flow_speed").val("");
                }
                if (data.taskAttackRule.src_ip != "-1") {
                    $("#src_ip").val(data.taskAttackRule.src_ip);
                } else {
                    $("#src_ip").val("");
                }
                if (data.taskAttackRule.src_ip_mask != "-1") {
                    $("#src_ip_mask").val(data.taskAttackRule.src_ip_mask);
                } else {
                    $("#src_ip_mask").val("");
                }
                if (data.taskAttackRule.dst_ip != "-1") {
                    $("#dst_ip").val(data.taskAttackRule.dst_ip);
                } else {
                    $("#dst_ip").val("");
                }
                if (data.taskAttackRule.dst_ip_mask != "-1") {
                    $("#dst_ip_mask").val(data.taskAttackRule.dst_ip_mask);
                } else {
                    $("#dst_ip_mask").val("");
                }
                if (data.taskAttackRule.dst_ip_port != "-1") {
                    $("#dst_ip_port").val(data.taskAttackRule.dst_ip_port);
                } else {
                    $("#dst_ip_port").val("");
                }
                if (data.taskAttackRule.dst_mac != "-1") {
                    $("#dst_mac").val(data.taskAttackRule.dst_mac);
                } else {
                    $("#dst_mac").val("");
                }
                if (data.taskAttackRule.attack_type != "-1") {
                    $("#attack_type").val(data.taskAttackRule.attack_type);
                    if (data.taskAttackRule.attack_type == "1") {
                        $("#tcp_payload_length_min").val("");
                        $("#tcp_payload_length_max").val("");
                        $("#tcp_payload_length_max").attr("readonly", true);
                        $("#tcp_payload_length_min").attr("readonly", true);
                    } else {
                        $("#tcp_payload_length_max").attr("readonly", false);
                        $("#tcp_payload_length_min").attr("readonly", false);
                        $("#tcp_payload_length_max").val(
                        data.taskAttackRule.tcp_payload_length_max);
                        $("#tcp_payload_length_min").val(
                        data.taskAttackRule.tcp_payload_length_min);
                    }
                }
            }
        
        }
    })
}
