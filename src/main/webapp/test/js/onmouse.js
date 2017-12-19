var  highlightcolor='#8D94A5';
//此处clickcolor只能用win系统颜色代码才能成功,如果用#xxxxxx的代码就不行,
var  clickcolor='#8D94A5';
function changeto(){
	//source=event.srcElement;
	//if (source.tagName=="TR"||source.tagName=="TABLE") return;
	//while(source.tagName!="TD")
	//	source=source.parentElement;
	//source=source.parentElement;
	//cs  =  source.children;
	//alert(cs.length);
	//if  (cs[1].style.backgroundColor!=highlightcolor&&source.id!="nc"&&cs[1].style.backgroundColor!=clickcolor)
	//for(i=0;i<cs.length;i++){
	//	cs[i].style.backgroundColor=highlightcolor;
	//}
}

function  changeback(){
//if  (event.fromElement.contains(event.toElement)||source.contains(event.toElement)||source.id=="nc")
//return
//if  (event.toElement!=source&&cs[1].style.backgroundColor!=clickcolor)
//source.style.backgroundColor=originalcolor
//for(i=0;i<cs.length;i++){
//	cs[i].style.backgroundColor="";
//}
}
function checkAll(c)
{

	var checks = document.getElementsByTagName("input");
	for(i=0;i<checks.length;i++)
	{
 
	if(checks[i].type=='checkbox'&&checks[i].disabled!=true)
 	{
		if(c.checked){
			checks[i].checked='checked';
			//$(".checker").find("span").addClass("checked");
			$(checks[i]).parent("span").addClass("checked");
		}else{
			checks[i].checked='';
			//$(".checker").find("span").removeClass("checked");
			$(checks[i]).parent("span").removeClass("checked");
		}
 	}
	}
}
function countChecked(checkName){
	var selectedCount = 0;
	var checks = document.getElementsByTagName("input");
	
	for(i = 0 ; i <= checks.length ; i++ ){
		if(checks[i]){
			if(checks[i].name == checkName){
				if(checks[i].checked)
				{
					selectedCount++;
				}
			}
		}
	}
	return selectedCount;
}

//返回第一个选中的复选框的值
function getOnlyValue(checkName) {
    var selectedValue = "";
	var checks = document.getElementsByTagName("input");
	
	for(i = 0 ; i <= checks.length ; i++ ){
		if(checks[i]){
			if(checks[i].name == checkName){
				if(checks[i].checked)
				{
					selectedValue = checks[i].value;
					break;
				}
			}
		}
	}
	return selectedValue;
}

function isRigthCheck(checkName,how){
	var selectedCount = countChecked(checkName);
	
	if(selectedCount > 0){
		if(how == "edit")
			return selectedCount > 1 ? false : true;
		else if(how == 'remove')
			return true;
		else if(how == 'exportXls')
			return true;
	}else{
		return false;
	}
}
//判断是否进行提交操作
function isOperation(checkName,flagValue){
	var yxbzValue ;
	var yxbz;
	var flag = false;
	var checks = document.getElementsByTagName("input");	
	for(i = 0 ; i <= checks.length ; i++ ){
		if(checks[i]){
			if(checks[i].name == checkName){
				if(checks[i].checked)
				{
					yxbz='yxbz'+checks[i].value;
					yxbzValue = document.getElementById(yxbz).value;
					if(yxbzValue==flagValue){
						flag=true;
					}
				}
			}
		}
	}
	return flag;
}

//判断是否进行提交操作
function isGroup(checkName){
	var yxbzValue ;
	var yxbz;
	var flag = false;
	var checks = document.getElementsByTagName("input");	
	for(i = 0 ; i <= checks.length ; i++ ){
		if(checks[i]){
			if(checks[i].name == checkName){
				if(checks[i].checked)
				{
					yxbz='group'+checks[i].value;
					yxbzValue = document.getElementById(yxbz).value;
					//alert(yxbzValue);
					if(yxbzValue!=null&&yxbzValue!=''){
						flag = true;
					}
				}
			}
		}
	}
	//alert(flag);
	return flag;
}


function $id(tid,type,show){// id 类型  alter的消息
    //2013-1-30  hyx add  ---start
    if(document.getElementById(tid)==null) {
    	return true;
    }
    //2013-1-30  hyx add  ---end
	if(type != 'ip1'){
		$("#"+tid).next().html('*');
	}else{
		$("#"+tid).next().html('');
	}
	
	var name=document.getElementById(tid).value;
	if($.trim(name)==''&& type=="xz"){
		$("#"+tid).next().html('请选择'+show);
		return false;
	}
	
	if($.trim(name)==''&& type!="remark"){
	 	$("#"+tid).next().html('请输入'+show);
	 	return false;
	}
	 
	if(type=='sz'){
		if(name.replace(/[\d+]/ig,"").length>0){
		 	//alert(show+'请输入数字')
		 	$("#"+tid).next().html(show+'请输入数字');
	        return false;
        }
	}

	if(type=='flsz'){
		if(name.replace(/^[1-9]\d*$/,"").length>0){
		 	//alert(show+'请输入数字')
		 	$("#"+tid).next().html(show+'请输入非零数字');
	        return false;
        }
	}
	

	  if(type=='port'){
		if(name.replace(/[\d+]/ig,"").length>0){
		 	//alert(show+'请输入数字')
			$("#"+tid).next().html(show+'请输入数字');
	        return false;
        }
         if(name<0){
        	//alert(show+'不能小于零')
        	$("#"+tid).next().html(show+'不能小于零');
	        return false;
        }
         if(name>65535){
        	//alert(show+'不能大于65535')
        	$("#"+tid).next().html(show+'不能大于65535');
	        return false;
        }
	 }
	  
	 if(type=='ip'|| type=='ip1'){
	 	var arr=name.match(/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/);   
		 if(arr==null){
		 	//alert("输入"+show+"不符合要求");
		 	$("#"+tid).next().html('输入'+show+'不符合要求');
		 	return false;  
		 } 
		 for(i=1;i<arr.length;i++){
		     if(String(Number(arr[i]))!=arr[i]||Number(arr[i])>255){
		        //alert("输入"+show+"不符合要求");
		        $("#"+tid).next().html('输入'+show+'不符合要求');
		     	return false; 
		     }
		 }
	  }   
	  
	  if(type=='remark'){
         if(name.length>500){
        	//alert(show+'不能大于五百个字符')
        	$("#"+tid).next().html(show+'不能大于五百个字符');
	        return false;
        }
	 }

//	if(name.length>0){
//		$("#"+tid).val(name);
//	}
	 return true;
}
    function fixTableHeader(gv,scrollHeight)
    {
        //var gvn=$(gv).clone(true).removeAttr("id");
        var gvn = $(gv).
        $(gvn).find("tr:not(:first)").remove();
        $(gv).before(gvn);
        $(gv).find("tr:first").remove();
        $(gv).wrap("<div style='height:"+ scrollHeight +"px; overflow-y: scroll;overflow-x:hidden; overflow: auto; padding: 0;margin: 0;'></div>");
    }
    
    (function($){
	    //内部变量
		var tableId,windowId;
		var iWindowObjs = new Array();		//iframe的窗口数组
		var iWinOffset = new Array();		//窗口的
		//属性
		var defaults ={
			'lockFlag': true,						// 是否实现表头浮动标识 true 实现 ；flase 不实现   默认 实现
			'resize' : true,						// 窗口大小变化时 同步表头浮动   默认实现
			'minRows' : 1,							// 表头行数   默认 1行
			'headerDiv' : 'clone_header_div',		// 存放表头的div
			'cloneHeader' : 'clone_header'			// 克隆表头
		};
		//初始化参数
		$.fn.initHeader = function(winId,lockFlag,resize,minRows,headerDiv,cloneHeader){
			tableId = $(this).attr("id");	//列表ID
			windowId = winId;				//窗口ID
			defaults.lockFlag = ( lockFlag != null && lockFlag != '' ? lockFlag : defaults.lockFlag );
			defaults.resize = ( resize != null && resize != '' ? resize : defaults.resize );
			defaults.minRows = ( minRows != null && minRows != '' ? minRows : defaults.minRows );
			defaults.headerDiv = ( headerDiv != null && headerDiv != '' ? headerDiv : defaults.headerDiv );
			defaults.cloneHeader = ( cloneHeader != null && cloneHeader != '' ? cloneHeader : defaults.cloneHeader );
			return $(this);
		}
    
    	$.fn.floatHeaderDiv = function(){
			var $tableOrg = $(this);
			var $cloneHeader = $(this).clone(true);	//克隆表
			
			$cloneHeader.width($(this).width());		//宽同步
			$cloneHeader.attr("boder",0);
			$cloneHeader.attr("id",defaults.cloneHeader);
			//行列两层嵌套循环，对应设定克隆表列宽
	    	//计算表头行数defaults.minRows
	    	$cloneHeader.find("tr").each(function(row,domEle){		// 克隆表   行循环
	    		if(row<defaults.minRows){
		    		$colDataOrg = $tableOrg.find("tr").eq(row).children();		// 原数据表 取第row行数据 
		    		$(domEle).children().each(function(col){		// 克隆表   列循环
		    			$cellOrg = $colDataOrg.eq(col);
			    		$(this).width($cellOrg.width());		//宽同步
			    		$(this).height($cellOrg.height());		//高同步
			    		//Cell内checkbox事件同步
			    		$(this).find("input[type='checkbox']").each(function (){
			    		 	var $checkbox0 = $(this);
			    		 	var $checkbox1 = $cellOrg.find("input[id='"+$checkbox0.attr('id')+"']");
			    			//将$checkbox0 的事件同步到$checkbox1 上
							$checkbox0.click(function(){
								$checkbox1.attr('checked',$checkbox0.attr('checked'));
							});
							//将$checkbox1 的事件同步到$checkbox0 上
							$checkbox1.click(function(){
								$checkbox0.attr('checked',$checkbox1.attr('checked'));
							});
			    		});
			    		//校正 浮动表头 行数
			    		if($cellOrg.attr('rowspan')>defaults.minRows-row){
			    			defaults.minRows = defaults.minRows + $cellOrg.attr('rowspan')-1;
			    		}
		    		});
	    		}else{
	    			$(domEle).remove();		//删除克隆表的无意义数据
	    		}
	    	});
	    	
	    	//检查 表头是否已存在 删除已存在的 以备添加新表头
			if($("#"+defaults.headerDiv).length > 0)$("#"+defaults.headerDiv).remove();
			//创建表头div
		    var tableOrgPosition = $(this).position();
		    var $headerDiv = $("<div style='position:absolute;z-index:20;display:none;'></div>");
		    $headerDiv.css("top",tableOrgPosition.top+'px').css("left", tableOrgPosition.left+'px')		//显示的X轴到左边框距离
			.css("width", $cloneHeader.outerWidth(true)+'px').css("height", $cloneHeader.outerHeight(true)+'px')		//高度
		    .attr("id", defaults.headerDiv);  						// 为div设置Id 为 headerId
		    $headerDiv.append($cloneHeader);						// 将克隆的表头添加到div中
		    $headerDiv.insertBefore($('body table',window.document).first());		// 将div加入页面中
			
			//同步列表宽度
			$(this).parent().scroll(function(){
				var $divArray = $(this).closest("div");
				if($divArray.size()>0){
					var divO = $divArray[0];
					if(tableOrgPosition.top < divO.scrollTop){
						$headerDiv.css('top',$(divO).position().top+'px');
						$headerDiv.css('display','inline');
					}else{
						$headerDiv.css('display','none');
					}
				}
				
		    	
			});
			
			//$(this).resize(function(){
			//	if(defaults.resize){
			//		if($headerDiv.width() != $(this).width()){
			//		    var $tableOrg = $(this);
			//			var $cloneHeader = $("#"+defaults.cloneHeader);	//表头
			//	    	$cloneHeader.find("tr").each(function(row,domEle){				// 克隆表   行循环
			//	    		$colDataOrg = $tableOrg.find("tr").eq(row).children();		// 原数据表 取第row行数据 
			//	    		//alert($(domEle).children().size()+" " +$colDataOrg.size());
			//	    		$(domEle).children().each(function(col){					// 克隆表   列循环
			//	    			$cellOrg = $colDataOrg.eq(col);
			//		    		$(this).width($cellOrg.width());			//设置对应列宽
			//		    		$(this).height($cellOrg.height());
			//	    		});
			//			});
			//		    //获取当前 iframe 在全窗口位置
			//			$headerDiv.css("left", $tableOrg.position().left+'px');		//显示的X轴到左边框距离
			//			$headerDiv.css("width", $tableOrg.width()+'px');			//宽度
			//		    
			//		    $("#"+defaults.cloneHeader).css("left", $tableOrg.position().left+'px');		//显示的X轴到左边框距离
			//			$("#"+defaults.cloneHeader).css("width", $tableOrg.width()+'px');			//宽度
			//			//判断表头是否需要显示
			//			//var headerTop = $(this).position().top;
			//		}
			//	}
			//});
			return $headerDiv;
		}
    })(jQuery)

    
function checkEmail(str){
		var reg=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/ ;
		return reg.test(str);
	}

//检查是否包含特殊字符
function containSpecial( s )       
{       
	var containSpecial = RegExp(/[(\s)(、)(\ )(\~)(\!)(\@)(\#)(\$)(￥)(\%)(；)(：)(“)(”)(\^)(\&)(\*)(\()(\))(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(。)(\/)(\<)(\>)(\?)(\)]+/);       
	return ( containSpecial.test(s) );       
}

//检查是否包含特殊字符
function checkStrNoDian( s )       
{       
	var containSpecial = RegExp(/[(\s)(、)(\ )(\~)(\!)(\@)(\#)(\$)(￥)(\%)(；)(：)(“)(”)(\^)(\&)(\*)(\()(\))(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(。)(\/)(\<)(\>)(\?)(\)]+/);       
	return ( containSpecial.test(s) );       
}

//检查是否包含特殊字符 可以包含中间空格 . _
function checkSpecial( s )       
{   
	var containSpecial = RegExp(/[(、)(\~)(\!)(\@)(\#)(\$)(￥)(\%)(；)(：)(“)(”)(\^)(\&)(\*)(\()(\))(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(。)(\/)(\<)(\>)(\?)(\)]+/);       
	return ( containSpecial.test($.trim(s)));       
}


//层覆盖
function coverDiv(){
	var msgw,msgh,bordercolor;
	msgw=400;//提示窗口的宽度
	msgh=100;//提示窗口的高度
	titleheight=25 //提示窗口标题高度
	bordercolor="#336699";//提示窗口的边框颜色
	titlecolor="#99CCFF";//提示窗口的标题颜色
	
	var sWidth,sHeight;
	sWidth=document.body.offsetWidth;//浏览器工作区域内页面宽度
	
	if(document.body.offsetHeight<screen.height)
		sHeight=screen.height;//屏幕高度（垂直分辨率）
	else
		sHeight=document.body.offsetHeight;//浏览器工作区域内页面高度
	
	//背景层（大小与窗口有效区域相同，即当弹出对话框时，背景显示为放射状透明灰色）
	var bgObj=document.createElement("div");//创建一个div对象（背景层）
	//定义div属性，即相当于
	bgObj.setAttribute('id','bgDiv');
	bgObj.style.position="absolute";
	bgObj.style.top="0";
	bgObj.style.background="#777";
	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity="0.6";
	bgObj.style.left="0";
	bgObj.style.width=sWidth + "px";
	bgObj.style.height=sHeight + "px";
	bgObj.style.zIndex = "10000";
	
	document.body.appendChild(bgObj);//在body内添加该div对象
	
	var title=document.createElement("h4");//创建一个h4对象（提示框标题栏）
	//定义h4的属性，即相当于
	title.setAttribute("id","msgTitle");
	title.setAttribute("align","center");
	title.style.position = "absolute";
	title.style.left = "50%";
	title.style.top = "50%";
	title.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.marginLeft = "-225px" ;
	title.style.marginTop = -75+document.documentElement.scrollTop+"px";
	title.style.width = msgw + "px";
	title.style.height =msgh + "px";
	title.style.textAlign = "center";
	title.style.lineHeight ="25px";
	title.style.zIndex = "10001";
	title.innerHTML="后台正在处理数据……";
	function removeObj(){//点击标题栏触发的事件
	    document.body.removeChild(bgObj);//删除背景层Div
	}
	document.getElementById("bgDiv").appendChild(title);//在提示框div中添加标题栏对象title

}

var interval;
//层覆盖,针对ajax请求，5秒钟取消遮盖层
function coverAjaxDiv(){
	var msgw,msgh,bordercolor;
	msgw=400;//提示窗口的宽度
	msgh=100;//提示窗口的高度
	titleheight=25 //提示窗口标题高度
	bordercolor="#336699";//提示窗口的边框颜色
	titlecolor="#99CCFF";//提示窗口的标题颜色
	
	var sWidth,sHeight;
	sWidth=document.body.offsetWidth;//浏览器工作区域内页面宽度
	
	if(document.body.offsetHeight<screen.height)
		sHeight=screen.height;//屏幕高度（垂直分辨率）
	else
		sHeight=document.body.offsetHeight;//浏览器工作区域内页面高度
	
	//背景层（大小与窗口有效区域相同，即当弹出对话框时，背景显示为放射状透明灰色）
	var bgObj=document.createElement("div");//创建一个div对象（背景层）
	//定义div属性，即相当于
	bgObj.setAttribute('id','bgDiv');
	bgObj.style.position="absolute";
	bgObj.style.top="0";
	bgObj.style.background="#777";
	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity="0.6";
	bgObj.style.left="0";
	bgObj.style.width=sWidth + "px";
	bgObj.style.height=sHeight + "px";
	bgObj.style.zIndex = "10000";
	
	document.body.appendChild(bgObj);//在body内添加该div对象
	
	var title=document.createElement("h4");//创建一个h4对象（提示框标题栏）
	//定义h4的属性，即相当于
	title.setAttribute("id","msgTitle");
	title.setAttribute("align","center");
	title.style.position = "absolute";
	title.style.left = "50%";
	title.style.top = "50%";
	title.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.marginLeft = "-225px" ;
	title.style.marginTop = -75+document.documentElement.scrollTop+"px";
	title.style.width = msgw + "px";
	title.style.height =msgh + "px";
	title.style.textAlign = "center";
	title.style.lineHeight ="25px";
	title.style.zIndex = "10001";
	title.innerHTML="后台正在处理数据……";
	title.click(function(){//点击标题栏触发的事件
		document.body.removeChild(bgObj);//删除背景层Div
	})
	document.getElementById("bgDiv").appendChild(title);//在提示框div中添加标题栏对象title
	interval = setInterval('removeObj()',1000);
}

function removeObj(){//点击标题栏触发的事件
	clearInterval(interval);
    document.body.removeChild(document.getElementById("bgDiv"));//删除背景层Div
}

	
	function showTitle(showContentSpanId,obj) {
		
		//当前鼠标的坐标          
   	 	var scrollLeft = document.documentElement.scrollLeft;
   	 	var scrollTop = document.documentElement.scrollTop;
   	 	var startX = Number(window.event.clientX)+Number(scrollLeft);
   	 	var startY = Number(window.event.clientY)+Number(scrollTop);
		
		//span的坐标及宽度
		var instanceDiv = document.getElementById(showContentSpanId);
		var spanToWinRightWidth = 350;//span的左边距离窗口右边的距离
        var spanX = document.body.offsetWidth-spanToWinRightWidth;//span的x坐标
        var spanY = startY+10;//span的y坐标
        var spanRealWidth = spanToWinRightWidth-50;//span实际宽度：250
        
        //先取span的x坐标为：obj的x坐标+1/2的obj的宽度，如果窗口最右边到此坐标的距离小于spanLeftX，则取：document.body.offsetWidth-spanLeftX
        var objX = Number(obj.clientX)+Number(obj.scrollLeft);
        var objWidth = obj.offsetWidth;
        var tmpLeftVal = spanX;
        if(objX!='NaN' && objWidth!='NaN') {      
             tmpLeftVal = Number(objX)+Number(0.5*objWidth);//span的x坐标
        }
        
        if(document.body.offsetWidth-tmpLeftVal>spanToWinRightWidth) {
        	spanX = tmpLeftVal;
        }     
       
        if(instanceDiv!=null && instanceDiv!='undefined') {
	        instanceDiv.style.left = spanX;
	        instanceDiv.style.top = spanY;
	        instanceDiv.style.width = spanRealWidth;//宽度为当前鼠标的位置到最右边窗口的距离  
	        instanceDiv.style.zIndex = 1000;        
        }        
      
		//$("#infoTipSpan").html(showContent);
		//$("#infoTipSpan").show();
		$("#"+showContentSpanId).show();
	}
	
	function hideTitle(showContentSpanId) {
		//$("#infoTipSpan").text('');
		//$("#infoTipSpan").hide();
		$("#"+showContentSpanId).hide();
	}
	