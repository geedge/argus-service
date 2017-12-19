optionSelect = "";
$(function() {
    $("span[name='proving']").css("display", "none");
}
)

function submitForm(obj) {
    if (obj == "issued") {
        if (valIssued()) {
            $("#issueOrTemp").val(obj);
            //alert("issued");
            formSubmit()
        }
    }
    
    if (obj == "cache") {
        if (valCache()) {
            $("#issueOrTemp").val(obj);
            //alert("cache");
            formSubmit()
        }
    }
    if (obj == "templet") {
        if (valTemplet()) {
            $("#issueOrTemp").val(obj);
            formSubmit()
            //alert("templet");
        }
    }

}

var valTaskName;
// 验证任务名称是否存在
function validateTaskName(obj) {
    $.ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/validateTaskName',
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
        url: '/mesaweb/taskMainAction/valTempletTaskName',
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

function formSubmit() {
    var formData = new FormData($("form[name='taskMainForm']")[0]);
    // $("form[name='taskMainForm']").serialize(),
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/saveTaskReplayRule',
        data: formData,
        async: true,
        cache: false,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function(result) {
            
            if (result.taskState == "-1") {
                alert("添加失败");
            }
            if (result.taskState == "0") {
                alert("添加成功");
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
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
                    window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
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
        	 window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
        },
        error: function() {
        	 window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
        }
    });
}
var reg = new RegExp("^[0-9]*$");
// 下发验证
function valIssued() {
    var num = 0;
    if (trim($("#task_name").val()).length == 0) {
        // 验证任务名称是否有值
        $("#val_task_name").css("display", "inline");
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
            $("#val_taskContinueTime").css("display", "none");
            $("#val_taskContinueTime").html("");
        }
    } else {
        // 验证持续时间
        if (task_run_times.length == 0 || (Number(task_run_times) == 0)) {
            // 任务次数为空或0时验证任务持续时间
            if (task_duration_time_h.length > 0 || task_duration_time_m.length > 0 || task_duration_time_s.length > 0) {
                if (!valTaskTime(task_duration_time_h, task_duration_time_m, task_duration_time_s, 'val_taskContinueTime')) {
                    num = num + 1;
                }
            } else {
                $("#val_task_run_times").css("display", "inline");
                $("#val_task_run_times").html("任务次数不能为空或0");
                $("#val_taskContinueTime").css("display", "inline");
                $("#val_taskContinueTime").html("持续时间不能为空或0");
                num = num + 1;
            }
        }
    }
    
    if ((task_run_times.length == 0 || task_run_times == "0") && (task_duration_time_h.length == 0 || task_duration_time_h == "0") 
    && (task_duration_time_m.length == 0 || task_duration_time_m == "0") && (task_duration_time_s.length == 0 || task_duration_time_s == "0")) {
        $("#val_task_run_times").css("display", "inline");
        $("#val_task_run_times").html("任务次数不能为空或0");
        $("#val_taskContinueTime").css("display", "inline");
        $("#val_taskContinueTime").html("持续时间不能为空或0");
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
    // 验证回放大小
    var flow_speed = trim($("#flow_speed").val());
    if (flow_speed.length > 0) {
        if (!valBlurFlowSpeed()) {
            num = num + 1;
        }
    } else {
        $("#val_flow_speed").css("display", "inline");
        $("#val_flow_speed").html("回放大小不能为空");
        num = num + 1;
    }
    
    
    var fileInfo = $("#fileInfo").val();
    if (fileInfo == "") {
        var replay_type = $("#replay_type").val();
        if (replay_type == "-1") {
            $("#val_replay_type").css("display", "inline");
            $("#val_replay_type").html("请选择回放文件类型");
            num = num + 1;
        } else {
            $("#val_replay_type").css("display", "none");
        }
        var replay_file = $("#replay_file").val();
        if (replay_file == null  || replay_file == "-1") {
            $("#val_replay_file").css("display", "inline");
            $("#val_replay_file").html("请选择或上传回放文件");
            num = num + 1;
        } else {
            $("#val_replay_file").css("display", "none");
        }
    } else {
        var replay_type = $("#replay_type").val();
        if (replay_type == "-1") {
            $("#val_replay_type").css("display", "inline");
            $("#val_replay_type").html("请选择回放文件类型");
            num = num + 1;
        } else {
            $("#val_replay_type").css("display", "none");
        }
        $("#val_replay_file").css("display", "none");
    }
    
    
    if(!valReplayFile()){
    	 num = num + 1;
    }
    
    if (num == 0) {
        return true;
    } else {
        return false;
    }

}
// 暂存验证
function valCache() {
    var num = 0;
    if (trim($("#task_name").val()).length == 0) {
        // 验证任务名称是否有值
        $("#val_task_name").css("display", "inline");
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
    var count = valFormat();
    if (count != 0) {
        num = num + 1;
    }
    
    var fileInfo = $("#fileInfo").val();
    if (fileInfo != "") {
        var replay_type = $("#replay_type").val();
        if (replay_type == "-1") {
            $("#val_replay_type").css("display", "inline");
            $("#val_replay_type").html("请选择回放文件类型");
            num = num + 1;
        } else {
            $("#val_replay_type").css("display", "none");
        }
    } else {
        $("#val_replay_type").css("display", "none");
        $("#val_replay_file").css("display", "none");
    }
    
    
    if(!valReplayFile()){
   	  num = num + 1;
    }
    if (num == 0) {
        return true;
    } else {
        return false
    }
}
// 保存模板验证
function valTemplet() {
    var num = 0;
    if (trim($("#task_name").val()).length == 0) {
        // 验证任务名称是否有值
        $("#val_task_name").css("display", "inline");
        num = num + 1;
    } else {
        valTempletTaskName(trim($("#task_name").val()))
        if (valTaskName == 1) {
            $("#val_task_name").css("display", "inline");
            $("#val_task_name").html("模板任务名称已存在");
            num = num + 1;
        } else {
            $("#val_task_name").css("display", "none");
        }
    }
    var count = valFormat();
    if (count != 0) {
        num = num + 1;
    }
    
    var fileInfo = $("#fileInfo").val();
    if (fileInfo != "") {
        var replay_type = $("#replay_type").val();
        if (replay_type == "-1") {
            $("#val_replay_type").css("display", "inline");
            $("#val_replay_type").html("请选择回放文件类型");
            num = num + 1;
        } else {
            $("#val_replay_type").css("display", "none");
        }
    } else {
        $("#val_replay_type").css("display", "none");
        $("#val_replay_file").css("display", "none");
    }
    
    if(!valReplayFile()){
   	  num = num + 1;
    }
    if (num == 0) {
        return true;
    } else {
        return false
    }
}
// 暂存和保存模板的公共验证信息
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
            $("#val_taskContinueTime").css("display", "none");
            $("#val_taskContinueTime").html("");
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
            if (!valTaskTime(task_duration_time_h, task_duration_time_m, task_duration_time_s, 'val_taskContinueTime')) {
                count = count + 1;
            }
        } else {
            $("#val_taskContinueTime").css("display", "none");
            $("#val_taskContinueTime").html("");
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
    // 验证流量大小
    var flow_speed = trim($("#flow_speed").val());
    if (flow_speed.length > 0) {
        if (!valBlurFlowSpeed()) {
            count = count + 1;
        }
    } else {
        $("#val_flow_speed").css("display", "none");
        $("#val_flow_speed").html("");
    }
    return count;
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
                    $("#" + valTime).css("display", "none");
                
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

// 任务名称失去焦点时验证数据格式
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
        disableTaskExcuteTime(taskRunTimes);
    } else {
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
        disableTaskExcuteTime(taskRunTimes);
    }
}


//验证延迟时间,持续时间失去鼠标焦点时,是否符合规范(并设置任务次数是否可输入)
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
            $("#" + valObj).html("");
            $("#" + valObj).css("display", "none");
            return true;
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
        //时分秒都没有值时设置回放次数可输入
        $("#" + valObj).css("display", "none");
        $("#" + valObj).html("");
        if (valObj == 'val_taskContinueTime') {
            disabletaskRunTimes();
        }
    }
}


// 回放大小失去焦点验证数据格式及设置是否原样回放
function valBlurFlowSpeed() {
    var objVal = trim($("#flow_speed").val());
    if (!reg.test(objVal)) {
        $("#val_flow_speed").css("display", "inline");
        $("#val_flow_speed").html("请输入正整数");
        return false;
    } else {
        $("#val_flow_speed").css("display", "none");
        $("#val_flow_speed").html("");
        if (!(Number(objVal) >= 0)) {
            $("#val_flow_speed").css("display", "inline");
            $("#val_flow_speed").html("请输入正整数");
            return false;
        } else {
            if ((Number(objVal) > 100000) ) {
                $("#val_flow_speed").css("display", "inline");
                $("#val_flow_speed").html("回放大小不能超过100000Mbps");
                return false;
            } else {
                $("#val_flow_speed").css("display", "none");
                flowSpeedZero(objVal);
                return true;
            }
        }
    }
}

//提交任务前判断选择的回放文件是否存在，有可能在选择了回放文件后，提交时回放文件被别人删除了
function valReplayFile(){
	var replayFile=$("#replay_file").val();
	if(replayFile!='-1'){
		if(!isExist(replayFile)){
			 $("#val_replay_file").css("display", "inline");
             $("#val_replay_file").html("回放文件不存在,请重新选择");
			return false;
		}
	}
	 $("#val_replay_file").css("display", "none");
     $("#val_replay_file").html("");
	return true;
}

// 根据持续时间设置任务次数是否可输入
function disabletaskRunTimes() {
    var task_duration_time_h = $("#task_duration_time_h").val();
    var task_duration_time_m = $("#task_duration_time_m").val();
    var task_duration_time_s = $("#task_duration_time_s").val();
    if (task_duration_time_h != 0 || task_duration_time_m != 0 
    || task_duration_time_s != 0) {
        $("#task_run_times").attr("disabled", true);
        $("#val_task_run_times").css("display", "none");
        $("#val_task_run_times").html("");
    } else {
        $("#task_run_times").attr("disabled", false);
    }
}
// 根据任务次数设置持续时间是否可输入
function disableTaskExcuteTime(inputLength) {
    if (inputLength.length > 0) {
        if (Number(inputLength) != 0) {
            $("#task_duration_time_h").attr("disabled", true);
            $("#task_duration_time_m").attr("disabled", true);
            $("#task_duration_time_s").attr("disabled", true);
            $("#val_taskContinueTime").css("display", "none");
            $("#val_taskContinueTime").html("");
        } else {
            $("#task_duration_time_h").attr("disabled", false);
            $("#task_duration_time_m").attr("disabled", false);
            $("#task_duration_time_s").attr("disabled", false);
        }
    } else {
        $("#task_duration_time_h").attr("disabled", false);
        $("#task_duration_time_m").attr("disabled", false);
        $("#task_duration_time_s").attr("disabled", false);
    }
}


function flowSpeedZero(objVal) {
    if (objVal != "") {
        if (Number(objVal) == 0) {
            $("#replay_originalYes").attr("checked", true);
            $("#replay_originalNo").attr("checked", false);
            /**
			 * 由于bootstrap自动为radio外面添加了一个span标签，上面仅仅设置了属性值但是界面形式不会变 ，所以添加下面两行的代码
			 */
            $("#replay_originalNo").parent().attr("class", "");
            $("#replay_originalYes").parent().attr("class", "checked");
            $("#replay_originalNo").prop("disabled", true);
            $("#val_flow_speed").css("display", "inline");
            $("#val_flow_speed").html("回放大小为0或空时,原样回放");
        } else {
            $("#replay_originalNo").prop("disabled", false);
            $("#val_flow_speed").css("display", "none");
            $("#val_flow_speed").html("");
        }
    } else {
        $("#replay_originalYes").attr("checked", true);
        $("#replay_originalNo").attr("checked", false);
        /**
		 * 由于bootstrap自动为radio外面添加了一个span标签，上面仅仅设置了属性值但是界面形式不会变 ，所以添加下面两行的代码
		 */
        $("#replay_originalNo").parent().attr("class", "");
        $("#replay_originalYes").parent().attr("class", "checked");
        $("#replay_originalNo").prop("disabled", true);
        $("#val_flow_speed").css("display", "inline");
        $("#val_flow_speed").html("回放大小为0或空时,原样回放");
    }
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
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
            } else {
                alert("任务暂存失败,任务已使用系统分配时间下发");
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
            }
        },
        error: function() {
            alert("任务暂存失败,任务已使用系统分配时间下发");
            window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
        }
    });
}

function deleteTaskMainAndRule(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/deleteTaskMainAndRule',
        data: {
            "taskMainId": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("已取消保存任务为暂存状态,任务已注销");
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
            } else {
                alert("取消保存任务为暂存状态失败，已暂存任务");
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
            }
        },
        error: function() {
            alert("取消保存任务为暂存状态失败，已暂存任务");
            window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
        }
    });
}

function resourceDel(obj) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/delResource',
        data: {
            "id": obj
        },
        async: true,
        cache: false,
        dataType: 'json',
        success: function(result) {
            if (result.msg == "1") {
                alert("取消使用系统分配时间,任务已删除");
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
            } else {
                alert("取消使用系统分配时间失败,任务已使用系统分配时间下发");
                window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
            }
        },
        error: function() {
            alert("取消使用系统分配时间失败,任务已使用系统分配时间下发");
            window.location.href = "/mesaweb/taskMainAction/queryTaskMainInfo";
        }
    });
}


function forbiddenReplayFile() {
    if ($("#replay_file").find("option:selected").val() == "-1") {
        var fileInfo = $("#fileInfo").val();
        if (fileInfo != '') {
            $("#replay_file").attr("disabled", true);
            $("#val_replay_file").css("display", "none");
            $("#val_replay_file").html("");
        } else {
            $("#replay_file").attr("disabled", false);
        }
    }
}

function forbiddenFileInfo() {
    if ($("#replay_file").find("option:selected").val() != "-1") {
        $("#uploadFile").attr("disabled", true);
        $("#fileInfo").attr("disabled", true);
        $("#val_replay_file").css("display", "none");
    } else {
        $("#uploadFile").attr("disabled", false);
        $("#fileInfo").attr("disabled", false);
    }
}




function reloadPage() {
    window.location.reload(true);
}
function getFileList(obj) {
    var optionVal = $(obj).val();
    if (optionVal != "-1") {
        $("#val_replay_type").css("display", "none");
    }
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/queryReplayRuleByFT',
        data: {
            "fileType": optionVal
        },
        dataType: 'json',
        async: false,
        success: function(data, textStatus) {
            $("#replay_file option").remove();
            $("#replay_file")
            .append(
            "<option  value='-1'>请选择</option>");
            if (data != null  && data.length > 0) {
                for (var obj = 0; obj < data.length; obj++) {
                    var option = "<option value='" 
                    + data[obj].filePath 
                    + "'>" 
                    + data[obj].fileName + "</option>";
                    $("#replay_file").append(option);
                }
            }
        }
    });

}

function fileList(optionVal) {
    $
    .ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/queryReplayRuleByFT',
        data: {
            "fileType": optionVal
        },
        dataType: 'json',
        async: false,
        success: function(data, textStatus) {
            $("#replay_file option").remove();
            $("#replay_file")
            .append(
            "<option  value='-1'>请选择</option>");
            if (data != null  && data.length > 0) {
                for (var obj = 0; obj < data.length; obj++) {
                    var option = "<option  ";
                    if (optionSelect == data[obj].fileName) {
                        option += "selected='selected' ";
                        $("#uploadFile").attr("disabled", true);
                        $("#fileInfo").attr("disabled", true);
                    }
                    option += "value='" + data[obj].filePath + "' id='" 
                    + data[obj].filePath + "'>" 
                    + data[obj].fileName + "</option>";
                    $("#replay_file").append(option);
                }
            
            } else {
                $("#uploadFile").attr("disabled", false);
                $("#fileInfo").attr("disabled", false);
            }
        }
    });
}

// 去除字符串左右空格
function trim(obj) {
    return obj.replace(/(^\s*)|(\s*$)/g, "")
}
// 获取模板列表
function getTempletTask() {
    var taskName = $("#taskTempletName").val();
    window.open("/mesaweb/trafficScene/selectTemp?task_type=1&type=1", "win", "width=850px,height=550px");
}
function setColumnValue(templetId) {
    $.ajax({
        type: 'post',
        url: '/mesaweb/taskMainAction/getTempletTask',
        data: {
            "id": templetId
        },
        dataType: 'json',
        async: false,
        success: function(data, textStatus) {
            if (data == null ) {
                $("#task_duration_time_h").attr("disabled", false);
                $("#task_duration_time_m").attr("disabled", false);
                $("#task_duration_time_s").attr("disabled", false);
                $("#task_run_times").attr("disabled", false);
                
                // $("#task_name").val("");
                $("#task_run_times").val("");
                $("#task_duration_time_h").val("");
                $("#task_duration_time_m").val("");
                $("#task_duration_time_s").val("");
                $("#task_delay_time_h").val("");
                $("#task_delay_time_m").val("");
                $("#task_delay_time_s").val("");
                $("#flow_speed").val("");
                $("#replay_originalYes").attr("checked", true);
                $("#replay_originalNo").attr("checked", false);
                /**
				 * 由于bootstrap自动为radio外面添加了一个span标签，上面仅仅设置了属性值但是界面形式不会变
				 * ，所以添加下面两行的代码
				 */
                $("#replay_originalNo").parent().attr("class", "");
                $("#replay_originalYes").parent().attr("class", "checked");
                $("#replay_type").val("-1");
                $("#replay_file").val("-1");
            
            } else {
                $("#task_duration_time_h").attr("disabled", false);
                $("#task_duration_time_m").attr("disabled", false);
                $("#task_duration_time_s").attr("disabled", false);
                $("#task_run_times").attr("disabled", false);
                
                // $("#task_name").val(data.taskMain.task_name);
                
                if (data.taskMain.task_run_times != 0) {
                    $("#task_run_times").val(data.taskMain.task_run_times);
                } else {
                    $("#task_run_times").val("");
                }
                if (data.taskMain.task_run_times != '0') {
                    $("#task_duration_time_h").attr("disabled", true);
                    $("#task_duration_time_m").attr("disabled", true);
                    $("#task_duration_time_s").attr("disabled", true);
                }
                
                
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
                if (data.taskMain.task_duration_time_h != 0 
                || data.taskMain.task_duration_time_m != 0 
                || data.taskMain.task_duration_time_s != 0) {
                    $("#task_run_times").attr("disabled", true);
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
                    $("#flow_speed").val(data.taskMain.flow_speed);
                } else {
                    $("#flow_speed").val("");
                }
                if (data.taskReplayRule.replay_original == '1') {
                    
                    $("#replay_originalYes").attr("checked", true);
                    $("#replay_originalNo").attr("checked", false);
                    /**
					 * 由于bootstrap自动为radio外面添加了一个span标签，上面仅仅设置了属性值但是界面形式不会变
					 * ，所以添加下面两行的代码
					 */
                    $("#replay_originalNo").parent().attr("class", "");
                    $("#replay_originalYes").parent().attr("class", "checked");
                } else {
                    $("#replay_originalYes").attr("checked", false);
                    $("#replay_originalNo").attr("checked", true);
                    /**
					 * 由于bootstrap自动为radio外面添加了一个span标签，上面仅仅设置了属性值但是界面形式不会变
					 * ，所以添加下面两行的代码
					 */
                    $("#replay_originalNo").parent().attr("class", "checked");
                    $("#replay_originalYes").parent().attr("class", "");
                }
                optionSelect = data.optionSelect;
                $("#replay_type").val(data.taskReplayRule.replay_type);
                fileList(data.taskReplayRule.replay_type);
            }
        
        }
    })
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