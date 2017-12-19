/**
all
**/
(function($){
	//展示拓扑图
	$.fn.showTopology = function(ajaxurl,type){
		//获得远程数据
		var $desk = $(this);
		$.ajax({
			url: ajaxurl+"&r="+new Date(),
			type:"POST",
			dataType: 'html',
			timeout: 20000,
			data:null,
			error: function(){alert('载入异常，请检查数据完整性');},
			success: function(html){
				//alert(html);
				showData(html,$desk,type);
			}
		});
	}
	//显示节点数据
	function showData(data,$desk,type){
		if(data==null){
			return;
		}
		var obj = eval("("+data+")");
		var i,j,n,m;
		
//		//创建所有机柜
//		for(n=0;n<obj.nodeBoxList.length;n++){
//			var box = obj.nodeBoxList[n];
//			var box_id = box.tableName+'_'+box.nodeType+'_'+box.nodeId;
//			if(box.id){//添加到桌面显示
//				$desk.createIconNode(box_id,box,0);
//			}
//		}
		
		//创建系统和节点组
		for(i=0;i<obj.sysAndNgList.length;i++){
			var nl=obj.sysAndNgList[i];
			var node_id = nl.tableName+'_'+nl.nodeType+'_'+nl.nodeId;
			if(nl.id){//添加到桌面显示
				$desk.createIconNode(node_id,nl,1);
			}
		}
		//创建所有连线
		//alert("line num--:"+obj.relaList.length);
		for(j=0;j<obj.relaList.length;j++){
			var ll=obj.relaList[j];
			$desk.createLine(ll);
		}
		if(type!=1){	
			$(document).progressDialog.hideDialog();
		}
		
	}
	
	//编辑拓扑图
	$.fn.editTopology = function(ajaxurl,type){
		//获得远程数据
		var $desk = $(this);
		$.ajax({
			url: ajaxurl+"&r="+new Date(),
			type:"POST",
			dataType: 'html',
			timeout: 20000,
			data:null,
			error: function(){alert('载入异常，请检查数据完整性');},
			success: function(html){
				//alert(html);
		 		explainData(html,$desk,type);
			}
		});
	}
	//显示节点数据
	function explainData(data,$desk,type){
		if(data==null){
			return;
		}
		var obj = eval("("+data+")");
		var i,j,n,m;
		//------------------------------------------------begin---------------------------------------------------for:hckings
		
		//创建所有机柜
		for(n=0;n<obj.nodeBoxList.length;n++){
			var box = obj.nodeBoxList[n];
			var box_id = box.tableName+'_'+box.nodeType+'_'+box.nodeId;
			//添加到页面节点展示
			$desk.createIconNode(box_id,box,0);	
		}
		//------------------------------------------------end---------------------------------------------------
		
		//创建节点组和系统
		for(i=0;i<obj.sysAndNgList.length;i++){
			var nl=obj.sysAndNgList[i];
			var node_id = nl.tableName+'_'+nl.nodeType+'_'+nl.nodeId;
			if(nl.nodeType=="system"){//系统
				$desk.createIconNode(node_id,nl,0);
			}else{//节点组
				if(nl.id==0){//添加到右侧列表
					$("#rightMenuContent").createLiNode(node_id,nl);
				}else{//添加到页面节点展示
					$desk.createIconNode(node_id,nl,0);
				}
			}
			
		}
		
		//创建节点
		for(i=0;i<obj.nodeList.length;i++){
			var nl=obj.nodeList[i];
			var node_id = nl.tableName+'_'+nl.nodeType+'_'+nl.nodeId;
			$("#rightMenuContent").createLiNode(node_id,nl);
		}
		
		//创建所有连线
		for(j=0;j<obj.relaList.length;j++){
			var ll=obj.relaList[j];
			$desk.createLine(ll);
		}
		
		if(type!=1){	
			$(document).progressDialog.hideDialog();
		}
		
		
		//键盘事件处理
//		$desk.bind({
//			keyup :function(e){
//				//delete按键 Unicode 键值号 46
//				if(e.keyCode==46){//执行删除操作
//					var nodesSize = $("a[divType='node'][active='1']").size();
//					var lineSize = $("div[divType='line'][active='1'][id*='_x_']").size();
//					if(confirm("您确定要删除这"+nodesSize+"个节点、"+lineSize+"个关联线么？")){
//						deleteNodesInPage(0);
//						deleteLinesInPage(0);
//					}
//				}
//			}
//		});
		
	}
	function refreashLines($desk){
			setTimeout(function(){
				$.ajax({
				url: "${pageContext.request.contextPath }/nodePosition/nodePosition.do?action=getLastLineSate"+"&r="+new Date(),
				type:"POST",
				dataType: 'html',
				timeout: 20000,
				data:null,
				error: function(){alert('error');},
				success: function(html){
			 		getLastLineSate(html,$desk);
				}
				})
				},5*1000);
	}
	//Line更新返回函数
	function getLastLineSate(data,$desk){
		var obj = eval("("+data+")");
		//创建所有连线
		for(var j=0;j<obj.relaList.length;j++){
			var ll=obj.relaList[j];
			var $lines = $("div[divType='line'][id^='"+ll.childId+"'][id$='"+ll.fatherId+"']");
			if($lines.size()>0){
				$lines.attr("bgc",ll.state+"");
				$lines.each(function(){
					$(this)[0].style.backgroundColor=ll.state;
				})
			}
		}
		refreashLines($desk);
	}
	
	//------------------------------------------------begin---------------------------------------------------for:hckings
	//添加列表节点信息
	$.fn.createLiNodeBox = function(boxId,box){
		var $newLi = getIconModel(2);
		
		$newLi.attr('id',boxId).attr('title',box.ispn).find('img').attr('src',(!box.imageUrl ? '${pageContext.request.contextPath }/images/show/nm.png' : box.imageUrl))
		$newLi.append(function(index,html){
			$(this).html("&nbsp;&nbsp;&nbsp;&nbsp;"+$(this).find('img')[0].outerHTML+" "+(box.ispn.length>4 ? (box.ispn.substring(0,4)+"..") : box.ispn)+$(this).find('div')[0].outerHTML);
		});
		$(this).append($newLi);
		//data缓存中保存数据
		$newLi.data("data", box);
		$newLi.data("node_id", boxId);
	}
	
	//添加列表节点信息
	$.fn.showBox = function(node_id,nl){
		var $newLi=$("<li id='"+node_id+"' title='"+nl.nodeDesc+"' divType='node' class='lastout' onclick='javascript:qry("+nl.nodeId+");'>"+
					"&nbsp;&nbsp;&nbsp;&nbsp;<img src='"+(!nl.imageUrl ? '${pageContext.request.contextPath }/images/show/nm.png' : nl.imageUrl)+"' width='26' heigth='24'/>"+
					(nl.nodeDesc.length>4 ? (nl.nodeDesc.substring(0,4)+"..") : nl.nodeDesc)+"<div class='menu_line'></div></li>");
		
		$newLi.attr('style','color:#000000');
		$newLi.append(function(index,html){
			if(nl.isWrining=="1"){
				$(this).html("&nbsp;<img src='${pageContext.request.contextPath }/images/no.gif'/>&nbsp;"+$(this).find('img')[0].outerHTML+" "+(nl.nodeDesc.length>4 ? (nl.nodeDesc.substring(0,4)+"..") : nl.nodeDesc)+$(this).find('div')[0].outerHTML);
			}else{
				$(this).html("&nbsp;"+$(this).find('img')[0].outerHTML+" "+(nl.nodeDesc.length>4 ? (nl.nodeDesc.substring(0,4)+"..") : nl.nodeDesc)+$(this).find('div')[0].outerHTML);
			}
		});
		$(this).append($newLi);
		//data缓存中保存数据
		$newLi.data("data", nl);
		$newLi.data("node_id", node_id);
		
		//绑定图标选中事件
		$newLi.bind({
			'click' : function(e){
				var $obj =  $(e.srcElement);
				if($obj.data('data')==null){
					$obj = $($obj.parent("#"+node_id));
				}
				var nl = $obj.data('data');
				var boxName= nl.ispn;
				$("#iconBox").find('#boxTitle').text("编号："+boxName);
			}
		});
		
	}
	
	
	//------------------------------------------------end---------------------------------------------------
	
	//添加列表节点信息
	$.fn.createLiNode = function(node_id,nl){
		//var $newLi=$("<li id='"+node_id+"' title='"+nl.nodeName+"' divType='node' class='lastout'>"+"&nbsp;&nbsp;&nbsp;&nbsp;<img src='"+(!nl.imageUrl ? '/nmsweb/images/show/nm.gif' : nl.imageUrl)+"' width='26' heigth='24' />"+(nl.nodeName.length>4 ? (nl.nodeName.substring(0,4)+"..") : nl.nodeName)+"<div class='menu_line'></div></li>");
		var $newLi = getIconModel(2);
		var ip_name="";
		if(nl.nodeType=="nodeGroup"){
			ip_name = nl.nodeName;
		}else{
			ip_name = nl.nodeIp+"_"+nl.nodeName;	
		}
		$newLi.attr('id',node_id).attr('title',ip_name).find('img').attr('src',(!nl.imageUrl ? '${pageContext.request.contextPath }/images/show/node_pc.png' : nl.imageUrl));
		//$newLi.attr('style','color:#000000');
		
		$newLi.append(function(index,html){
			$(this).html("&nbsp;&nbsp;&nbsp;&nbsp;"+$(this).find('img')[0].outerHTML+" "+(ip_name.length>10 ? (ip_name.substring(0,10)+"..") : ip_name)+$(this).find('div')[0].outerHTML);
		});
		$(this).append($newLi);
		//data缓存中保存数据
		$newLi.data("data", nl);
		$newLi.data("node_id", node_id);
		
	}
	
	
		//添加列表节点信息
	$.fn.createSpanNode = function(node_id,nl){
		var $newSpan=getIconModel(1);
		$newSpan.attr('uType',nl.nodeUType);	
		var inId = $(this).attr('id');
		$newSpan.attr('id',node_id);
		
		//绑定鼠标移入移出事件
		$newSpan.bind({
			'mouseenter' : function() {//单引
				$("#spanIp").text(nl.nodeIp);
				$("#spanName").text(nl.nodeName);
				$("#spanUType").text(nl.nodeUType+"U");
				var iconBoxPosition = {
					top: $("#iconBox").css('top').replace("px","")*1,
					left: $("#iconBox").css('left').replace("px","")*1,
					width: $("#iconBox").css('width').replace("px","")*1,
					height: $("#iconBox").css('height').replace("px","")*1
				}
				
				var cPointY=0;
				var cPointX=0;
				
				var sumWidth = $("#board").css('width').replace("px","")*1;
				var sumheight = $("#board").css('height').replace("px","")*1;
				
				var iconBoxLeft = parseInt(iconBoxPosition.left);
				if(sumWidth-iconBoxLeft < parseInt(iconBoxPosition.width+200)){
					cPointY = parseInt(iconBoxPosition.left - iconBoxPosition.width);
				}else{
					cPointY = parseInt(iconBoxPosition.left + iconBoxPosition.width+10);
				}
				cPointX = parseInt(iconBoxPosition.top + parseInt(inId)*11+30);
				if(cPointX+100>sumheight){
					cPointX = sumheight-110;
				}
				
				$("#span_info1")
				.css('top',cPointX+'px')
				.css('left',cPointY+'px')
				.show();
			},
			'mouseleave' : function() {//单引
				$("#span_info1").hide();
				$("#spanIp").empty();
				$("#spanName").empty();
				$("#spanUType").empty();
			}
		});
		

		
		$(this).append($newSpan);
		
		//data缓存中保存数据
		$newSpan.data("data", nl);
		$newSpan.data("node_id", node_id);
	}
	
//	$.fn.createSpanInTD = function(node_id,nl){
//		var $newSpan=getIconModel(1);
//		$newSpan.attr('id',node_id).attr('title',nl.nodeName);
//		//var $newSpan = getIconModel(1);
//		//$newSpan
//		//alert($newSpan[0].outerHTML)
//		$(this).append($newSpan);
//		//data缓存中保存数据
//		$newSpan.data("data", nl);
//		$newSpan.data("node_id", node_id);
//	}
//	
	
	
	//添加展示节点信息
	$.fn.createIconNode = function(node_id,nl,type){
	    var $desktop = $(this);
	    var $icon;
	    //在画板上添加新的节点
	    if(nl.nodeType=="nodeBox"){
	    	$icon = $('<a id="'+node_id+'" class="abs icon"  divType="node" action="update" flagType="box" title="'+nl.nodeDesc+'"> <img src="' + 
		    		(!nl.imageUrl ? '${pageContext.request.contextPath }/images/show/nm.png' : nl.imageUrl) +'" width = "24" height = "24" /><span style="display:block;color:#000000;font-size:8px">'+nl.nodeDesc+ '</span></a>');
	    }else{
	    	$icon = $('<a id="'+node_id+'" class="abs icon"  divType="node" action="update" title="'+nl.nodeName+'"> <img src="' + 
	    		(!nl.imageUrl ? '${pageContext.request.contextPath }/images/show/topic.gif' : nl.imageUrl) +'" width = "24" height = "24" /><span style="display:block;color:#000000;font-size:8px">'+nl.nodeName+ '</span></a>');
		}
	    
//	    if(nl.nodeType=="system" && nl.id!=0){
//	    	$("#sysNpId").val(nl.id);
//	    }
		$desktop.append($icon);
		//初始化节点坐标
		var lastNodeOffset ={top: nl.positionY,left: nl.positionX};
//		if(!lastNodeOffset.top && !lastNodeOffset.left && $("a[divType='node']").size()>1){
//			var $lastNode = $("a[divType='node']:eq("+($("a[divType='node']").size()-2)+")");
//			var offset = $lastNode.offset();
//			lastNodeOffset.top = offset.top+parseInt($lastNode.css('height').replace('px','')*1);
//			lastNodeOffset.left = offset.left+parseInt($lastNode.css('width').replace('px','')*1);
//		}
		$icon.css('top',lastNodeOffset.top+'px').css('left',lastNodeOffset.left+'px');
		//data缓存中保存数据
		$icon.data("data", nl);
		$icon.data("node_id", node_id);
		//鼠标移上和移出绑定样式事件
		
		if(type=="1" && nl.nodeType=="nodeGroup"){
			var group = 'menu_1';
			if(nl.childNum ==0){
				$icon.mouseDelay(false, group).hover(function () {
					var nl1 = $(this).data('data');
					getWarningInfoForShow(nl1,{top: nl1.positionY,left: nl1.positionX});
				}, function () {
					$("#warningInfo").hide(function(){$("#nodeSumNum").empty();$("#trueNum").empty();$("#falseNum").empty();});
				});
				$("#warningInfo").mouseDelay(false, group).hover(null, function () {
					$("#warningInfo").hide(function(){$("#nodeSumNum").empty();$("#trueNum").empty();$("#falseNum").empty();});
				});
				
			}else{
				$icon.mouseDelay(false, group).hover(function () {
					var nl1 = $(this).data('data');
					getNodeNumForShow(nl1,{top: nl1.positionY,left: nl1.positionX});
				}, function () {
					$("#warningInfo").hide(function(){$("#nodeSumNum").empty();$("#trueNum").empty();$("#falseNum").empty();});
				});
				$("#warningInfo").mouseDelay(false, group).hover(null, function () {
					$("#warningInfo").hide(function(){$("#nodeSumNum").empty();$("#trueNum").empty();$("#falseNum").empty();});
				});
			}
		}else{
			$icon.bind({
				'mouseenter' : function() {//单引
					$icon.addClass("hover");
				},
				'mouseleave' : function() {//单引
					if($icon.attr('active')==1)return;
					$icon.removeClass("hover");
				}
			});
		}
		
		//节点UI的拖动
		$icon.draggable( {
			cursorAt : {cursor :"move"},
			revert:false,
			zIndex:1200,
			containment:"#board",
			stop : function(event,ui){
				//alert(event.srcElement.tagName);
				var nl0 = $icon.data("data");
				//alert("X:"+ $icon.css('left').replace("px","")*1+"\nY:"+ $icon.css('top').replace("px","")*1);
				nl0.positionX = $icon.css('left').replace("px","")*1;
				nl0.positionY = $icon.css('top').replace("px","")*1;
				$icon.data("data", nl0);
			}
		});
		//绑定图标选中事件
		$icon.bind({
			'click' : function(e){
				activeNode(e.srcElement,0);
			},
			'contextmenu' : function(e){
				activeNode(e.srcElement,1);
			},
			'mousedown' : function(){
				//alert("sfdsf");
				$icon.attr('action','update');
			}
		});
		/*
		2011-07-29 增加多级页面展示编辑
		root根节点   根节点标识 accessOperation
		****/
		if(nl.id == $("#pageId").val()){
			$icon.attr("accessOperation","root");
		}
	}
   	//创建所有节点的连接线
	$.fn.createLine = function(ll){
	   	var $objHtmlX,$objHtmlM,$objHtmlY;//三条线
	   	//线初始化
   		$objHtmlX=$("<div divType='line' action='normal' id=\""+ll.childId+"_x_"+ll.fatherId+"\" class=\"lineH\" bgc='"+ll.state+"' style=\"background-color:"+ll.state+";display:none;\"><img height=\"1px\" width=\"1px\"/></div>");
   		$objHtmlM=$("<div divType='line' action='normal' id=\""+ll.childId+"_m_"+ll.fatherId+"\" class=\"lineH\" bgc='"+ll.state+"' style=\"background-color:"+ll.state+";display:none;\"><img height=\"1px\" width=\"1px\"/></div>");
   		$objHtmlY=$("<div divType='line' action='normal' id=\""+ll.childId+"_y_"+ll.fatherId+"\" class=\"lineH\" bgc='"+ll.state+"' style=\"background-color:"+ll.state+";display:none;\"><img height=\"1px\" width=\"1px\"/></div>");
	   	$(this).append($objHtmlX);
		$(this).append($objHtmlM);
	 	$(this).append($objHtmlY);
	 	//绑定连线选中事件
		$("div[divType='line'][id^='"+ll.childId+"'][id$='"+ll.fatherId+"']").bind({
			'click' : function(e){
				activeLines(e.srcElement,0);
			},
			'contextmenu' : function(e){
				activeLines(e.srcElement,1);
			}
		});
	 	//为存在连线的首尾节点绑定图标移动，绑定重置线事件
		if(!$("#"+ll.childId).data("events") || !$("#"+ll.childId).data("events")['move']){//子节点绑定move事件绑定条件
			$("#"+ll.childId).bind('move',function(){
					$(this).attr('action','update');
					$(this).resetLine();
			});
		}
		if(!$("#"+ll.fatherId).data("events")|| !$("#"+ll.fatherId).data("events")['move']){//父节点绑定move事件绑定条件
			$("#"+ll.fatherId).bind('move',function(){
					$(this).attr('action','update');
					$(this).resetLine();
			});
		}
		//手动触发事件、作初始化
		$("#"+ll.childId).trigger('move');
	}
	//重置线
	$.fn.resetLine = function(){
		var nodeId = $(this).attr('id');
		//child（起点）的节点画线
		$("div[divType='line'][id^='"+nodeId+"_x'][action!='delete']").each(function(){
			var farther_id = $(this).attr('id').split('_x_')[1];
			$.drawLine(nodeId,farther_id);
		});
		//farther（终点）的节点画线
		$("div[divType='line'][id$='x_"+nodeId+"'][action!='delete']").each(function(){
			var child_id = $(this).attr('id').split('_x_')[0];
			$.drawLine(child_id,nodeId);
		});
	}
	//--连线展示重置
	$.drawLine = function(child_id,farther_id){
		try{
		//获取关联节点坐标及尺寸
		var $nodeObj_1=$("#"+child_id);
		var $nodeObj_2=$("#"+farther_id);
		var obj1Position = {
			top: $nodeObj_1.css('top').replace("px","")*1,
			left: $nodeObj_1.css('left').replace("px","")*1,
			width: $nodeObj_1.css('width').replace("px","")*1,
			height: $nodeObj_1.css('height').replace("px","")*1
		}
		var obj2Position = {
			top: $nodeObj_2.css('top').replace("px","")*1,
			left: $nodeObj_2.css('left').replace("px","")*1,
			width: $nodeObj_2.css('width').replace("px","")*1,
			height: $nodeObj_2.css('height').replace("px","")*1
		}
		//关联节点坐标纵向比
		var topFlag = (obj1Position.top)/(obj1Position.top+obj2Position.top);
		//关联节点坐标横向比
		var leftFlag =(obj1Position.left)/(obj1Position.left+obj2Position.left);
		
		//获取两点间连线（ x、m、y ）三线
		var $line_x=$("#"+child_id+"_x_"+farther_id);//起始线
		var $line_m=$("#"+child_id+"_m_"+farther_id);//中间线
		var $line_y=$("#"+child_id+"_y_"+farther_id);//结束线
		
		//根据横纵坐标比计算连线起始和结束坐标
		//child节点连线起始点坐标计算
		var cPointX = parseInt(obj1Position.left + obj1Position.width/4 + obj1Position.width/2*(1-leftFlag));
		var cPointY = parseInt(obj1Position.top + obj1Position.height/4  + obj1Position.height/2*(1-topFlag));
		//farther节点连线结束点坐标计算
		var fPointX = parseInt(obj2Position.left + obj2Position.width/4  + obj2Position.width/2*leftFlag);
		var fPointY = parseInt(obj2Position.top + obj2Position.height/4  + obj2Position.height/2*topFlag);
		
		//画线
		$line_x
		.css('top',cPointY+'px')
		.css('left',(cPointX-fPointX) < 0 ? cPointX : ( cPointX - Math.abs(parseInt((cPointX-fPointX)/2))) +'px')
		.css('width',Math.abs(parseInt((cPointX-fPointX)/2))+'px')
		.show();
		
		$line_m
		.css('top',(cPointY-fPointY) < 0 ? cPointY : fPointY +'px')
		.css('left',((cPointX+fPointX)/2)+'px')
		.css('height',Math.abs((cPointY-fPointY))+'px')
		.show();
		
		$line_y
		.css('top',fPointY +'px')
		.css('left',(fPointX-cPointX) < 0 ? fPointX : ( fPointX - Math.abs(parseInt((cPointX-fPointX)/2)))+'px')
		.css('width',Math.abs(parseInt((cPointX-fPointX)/2))+'px')
		.show();
		}catch(e){}
	}
})(jQuery)
	var param = {actionFlag : false};
jQuery(function(){
	$('#board').click(function(){
		if(param.actionFlag == true){
			param.actionFlag =false;
		}
	});
});
	
//hover延时器
(function ($, plugin) {
	var data = {}, id = 1, etid = plugin + 'ETID';
	
	// 延时构造器
	$.fn[plugin] = function (speed, group) {
		id ++;	
		group = group || this.data(etid) || id;
		speed = speed || 150;
		
		// 缓存分组名称到元素
		if (group === id) this.data(etid, group);
		
		// 暂存官方的hover方法
		this._hover = this.hover;
		
		// 伪装一个hover函数，并截获两个回调函数交给真正的hover函数处理
		this.hover = function (over, out) {
			over = over || $.noop;
			out = out || $.noop;
			this._hover(function (event) {
				var elem = this;
				clearTimeout(data[group]);
				data[group] = setTimeout(function () {
					over.call(elem, event);
				}, speed);
			}, function (event) {
				var elem = this;
				clearTimeout(data[group]);
				data[group] = setTimeout(function () {
					out.call(elem, event);
				}, speed);
			});
			
			return this;
		};
		
		return this;
	};
	
	// 冻结选定元素的延时器
	$.fn[plugin + 'Pause'] = function () {
		clearTimeout(this.data(etid));
		return this;
	};
	
	// 静态方法
	$[plugin] = {
		// 获取一个唯一分组名称
		get: function () {
			return id ++;
		},
		// 冻结指定分组的延时器
		pause: function (group) {
			clearTimeout(data[group]);
		}
	};
	
})(jQuery, 'mouseDelay');




	function getWarningInfoForShow(nl,lastNodeOffset){
		$("#nodeSumNum").empty();
		$("#trueNum").empty();
		$("#falseNum").empty();
		var i,j,k;
		var allIpList ="";
		var trueIpList="";
		var falseIpList="";

		var allSeqList ="";
		var trueSeqList="";
		var falseSeqList="";	
		
		var sum = nl.nwmt.nodeTableList.length;
		var trueNum= nl.nwmt.trueNodeList.length;
		var falseNum = nl.nwmt.falseNodeList.length;
		
		var $falseNode;
		if(sum>0){
			//所有节点
			for(i=0;i<sum;i++){
				var nlsub=nl.nwmt.nodeTableList[i];
				var nodeIp = nlsub.nodeIp;
				if(i!=sum-1){
					allIpList+=nodeIp+",";
				}else{
					allIpList+=nodeIp;
				}
			}
			
			
			//正常节点
			if(trueNum>0){
				for(j=0;j<trueNum;j++){
					var nlt=nl.nwmt.trueNodeList[j];
					var nodeIpt = nlt.nodeIp;
					if(j!=trueNum-1){
						trueIpList+=nodeIpt+",";
					}else{
						trueIpList+=nodeIpt;
					}
				}
				
			}else{
				trueNum = 0;
				
			}
			
			//非正常节点
			if(falseNum>0){
				for(k=0;k<falseNum;k++){
					var nlf=nl.nwmt.falseNodeList[k];
					var nodeIpf = nlf.nodeIp;
					var nodeSeq = nlf.seqId;
					if(k!=falseNum-1){
						falseIpList+=nodeIpf+",";
						falseSeqList+=nodeSeq+","
					}else{
						falseIpList+=nodeIpf;
						falseSeqList+=nodeSeq;
					}
				}
				$falseNode = $('<a id="falseNum" href="${pageContext.request.contextPath }/detection/detectionWarning.do?action=query&seqList='+falseSeqList+'" style= "display:inline; color:#FF0000" title="'+falseIpList+'">非正常节点个数：'+falseNum+'</a>');
			}else{
				falseNum = 0;
				$falseNode = $('<span style="display:inline;">'+falseNum+'</span>');
			}
					
			$("#nodeSumNum").text("节点总数："+sum).attr('title',allIpList);
			$("#trueNum").text("正常节点个数："+trueNum).attr('title',trueIpList);
			$("#falseNum").append($falseNode);
			
		}else{
			$("#nodeSumNum").text("节点组下还未维护节点");
		}
		
		$("#Title").text(nl.nodeName+"的告警信息");
		var cPointX = parseInt(lastNodeOffset.top+70);
		var cPointY = parseInt(lastNodeOffset.left+70);
			
		$("#warningInfo").css('top',cPointX+'px').css('left',cPointY+'px').show(1000);
	}
	
	function getNodeNumForShow(nl,lastNodeOffset){
		$("#nodeSumNum").empty();
		$("#trueNum").empty();
		$("#falseNum").empty();
		var sum = nl.nodeNum;
		var $all;
		
		if(sum>0){
			$("#nodeSumNum").text("存在子节点组，节点总数："+sum);
		}else{
			$("#nodeSumNum").text("存在子节点组，节点组下还未维护节点");
		}
		
		$("#Title").text(nl.nodeName+"的告警信息");
		var cPointX = parseInt(lastNodeOffset.top+70);
		var cPointY = parseInt(lastNodeOffset.left+70);
		$("#warningInfo").css('top',cPointX+'px').css('left',cPointY+'px').show(1000);
	}


	function getIconModel(type){
		var $icon;
		if(type == 1){	//方块元素SPAN
			$icon = $("<span divType='node' class='icon-span' />");
		}else if(type == 2){	//列表元素LI
			$icon = $("<li divType='node' class='lastout'>"+"<img src='' width='26' heigth='24' /><div class='menu_line'></div></li>");
		}else if(type == 3){	//图标元素A
			$icon = $('<a  divType="node" class="abs icon" action="normal"> <img src="" /><span style="display:block;"></span></a>');
		}
		
			//节点UI的拖动
			$icon.draggable({
				
				addClasses : 'true',
				cursorAt : {cursor :"move"},
				revert:false,
				zIndex : 1200,
				start : function(event,ui){
					if(type == 1){
						$("#rightNodeList").attr('class','liList1');
					}
				},
				stop : function(event,ui){
					var nl0 = $icon.data("data");
					//alert("X:"+ $icon.css('left').replace("px","")*1+"\nY:"+ $icon.css('top').replace("px","")*1);
					nl0.positionX = $icon.css('left').replace("px","")*1;
					nl0.positionY = $icon.css('top').replace("px","")*1;
					$icon.data("data", nl0);
				},
				containment:"#board"
			});
		
		return $icon;
	}
	
	//页面删除被选中的节点
	function deleteNodesInPage(confirmFlag){
		var nodesSize = $("a[divType='node'][active='1']").size();
		if(nodesSize>0){
			if(nodesSize>1 && confirmFlag==1 && !confirm("您确定要移除这"+nodesSize+"个节点么？\n被移除节点将被放入右侧菜单！\n被移除节点的关联关系将被删除！")){
				return;
			}
		}else{
			return;
		}
		//删除节点及节点连线
		var $content=$("#rightMenuContent");
		$("a[divType='node'][active='1']").each(function(){
			//创建列表节点并保存信息
			var $icon = $(this);
			//删除权限 根节点过滤
			if($icon.attr('accessOperation')=='root'){
				alert($icon.data('data').nodeName +" 节点是根节点，无法删除");
				return true;
			}
			var data=$icon.data("data");
			var node_id=$icon.data("node_id");
			if(data.tableName!="node_box_table"){
				$content.createLiNode(node_id,data);
			}
			
			//删除已存在的关联关系
			$('#board').find("div[divType='line'][id*='"+node_id+"'][action!='delete']").attr('action','delete').attr('active','0').hide();
			//删除桌面图标
			$icon.hide(500,function(){
				$icon.remove();
			});
			//将要删除的桌面元素id放入隐藏域，以备删除之需
			addDelNpIds(data.id,data.nodeId);
			//绑定移动事件
			$content.find("#"+node_id).draggable({
				cursorAt : {cursor :"move"},
				revert:false
			});
		});
	}
	//页面删除被选中的关联关系
	function deleteLinesInPage(confirmFlag){
		var lineSize = $("div[divType='line'][active='1'][id*='_x_']").size();
		if(lineSize>0){
			if(lineSize>1 && confirmFlag==1 && !confirm("您确定要删除这"+(lineSize)+"个关联关系么？")){
				return ;
			}
		}else{
			return;
		}
		//删除连线
		//$("div[divType='line'][active='1']").attr("action",'delete').attr('active','0').hide();
		//alert($("div[divType='line'][active='1']").size());
		$("div[divType='line'][active='1'][id*='_x_']").each(function(){
			var ll = $(this).attr("id").split('_x_');
			var nl1 = $("#"+ll[0]).data('data');
			var nl2 = $("#"+ll[1]).data('data');
			var flag = linkCheck(nl1,nl2,'delete');
			if(flag!=1){
				return ;
			}
			flag = linkCheck(nl2,nl1,'delete');
			if(flag!=1){
				return ;
				
			}
			if(flag){
				$("div[divType='line'][active='1'][id*='"+ll[0]+"'][id*='"+ll[0]+"']")
				.attr("action",'delete')
				.attr('active','0')
				.hide();
			}
		});
	}

	//将要删除的桌面元素id放入隐藏域 以备删除之需
	function addDelNpIds(npid,nodeId){
		if($("#del_Npids").val()==""){
			$("#del_Npids").val(npid);
		}else{
			$("#del_Npids").val($("#del_Npids").val()+","+npid);	
		}
		//alert($("#del_Npids").val());
		if($("#del_Nodeids").val()==""){
			$("#del_Nodeids").val(nodeId);
		}else{
			$("#del_Nodeids").val($("#del_Nodeids").val()+","+nodeId);	
		}
		
	}


	//图标选中事件
	function activeNode(obj,mouseFlag){
		var $obj =  $(obj);
		//判断当前事件节点是否为A Icon 不是则获取其父对象
		if($obj[0].tagName != 'A'){
			$obj = $obj.parent();
		}
		var nl = $obj.data('data');
		if(!nl){
			return;
		}
		//右键事件操作 取消当前时间之外的图标的激活状态 0：左键 1：右键
		if(mouseFlag==1){
			$("a[divType='node'][active='1']").removeClass("hover").removeClass("hover2").attr('active',0);
			$("div[divType='line'][active='1']").removeClass("lineH3").attr('active','0').each(function(){
				$(this).css("background-color",$(this).attr('bgc'));
			});
		}
		if($obj.attr('active')==1){
			$obj.removeClass("hover2").attr('active',0);
		}else{
			$obj.addClass("hover2").attr('active',1);
		}
	}
	//连线选中事件
	function activeLines(obj,mouseFlag){
		var lineId = $(obj).attr('id');
		var ll = null;
		if(lineId.indexOf('_x_')>-1)ll = lineId.split('_x_');
		if(lineId.indexOf('_m_')>-1)ll = lineId.split('_m_');
		if(lineId.indexOf('_y_')>-1)ll = lineId.split('_y_');
		//右键事件操作 取消当前时间之外的连线的激活状态 0：左键 1：右键
		//右键事件操作 取消当前时间之外的连线的激活状态 0：左键 1：右键
		if(mouseFlag==1){
			$("a[divType='node'][active='1']").removeClass("hover").removeClass("hover2").attr('active',0);
			$("div[divType='line'][active='1']").removeClass("lineH3").attr('active','0').each(function(){
				$(this).css("background-color",$(this).attr('bgc'));
			});
		}
		//active 激活状态标志 0：未激活 1：激活 
		if($(obj).attr('active')==1){
			$("div[divType='line'][id*='"+ll[0]+"_'][id*='_"+ll[1]+"']")
			.css("background-color",$("div[divType='line'][id*='"+ll[0]+"_'][id*='_"+ll[1]+"']").attr('bgc'))
			.removeClass("lineH3")
			.attr('active','0');
		}else{
			$("div[divType='line'][id*='"+ll[0]+"_'][id*='_"+ll[1]+"']")
			.css('background-color','')
			.addClass("lineH3")
			.attr('active','1');
		}
	}
	//选择点击事件
	function mouseclickF(){
		var imgObj = event.srcElement;
		if($(imgObj).attr('checkVal')==1)
		{$(imgObj).attr('checkVal','0');mouseoutF(imgObj)}
		else
		{
			$("#imgSelect img[checkVal='1']").each(function(){
				$(this).attr('checkVal','0');
				mouseoutF($(this)[0]);
			});
			$(imgObj).attr('checkVal','1');mouseoverF(imgObj);
		}
	}
	//鼠标移出图片事件
	function mouseoutF(imgObj){
		if($(imgObj).attr('checkVal')==0){
			imgObj.style.margin='1px';
			imgObj.style.borderWidth='0px';
		}
	}
	//鼠标移入图片事件
	function mouseoverF(imgObj){
			imgObj.style.margin='0px';
			imgObj.style.borderWidth='1px';
	}
	
	//在页面上添加关联线
	function addLinkInPage(){
		var $fromNode = $('#board').data('active_obj');
		var node_id1 = $fromNode.data('node_id');
		var nl1 = $fromNode.data('data');
		var $a_obj = $(event.srcElement);
		if($a_obj[0].tagName != 'A'){
			$a_obj = $a_obj.parent();
		}
		var nl2 = $a_obj.data('data');
		if(!nl2){
			alert("请选择图标进行关联操作！");
			return;
		}
		var node_id2 = $a_obj.data('node_id');
		//原关联操作过滤校验
		if(node_id1 == node_id2){alert("关联节点重复,请重新选择！");activeNode($a_obj[0],0);return;}
		if((nl1.tableName == nl2.tableName && nl1.nodeType == nl2.nodeType)){alert("同类节点不能相互关联,请重新选择！");activeNode($a_obj[0],0);return;}
		//新增关联过滤
		//A->B的关联判断
		var linkFlag1 = linkCheck(nl1,nl2,"update");
		//alert('linkFlag1 '+linkFlag1);
		if(linkFlag1 == 2){
			return;
		}
		//B->A的关联判断
		var linkFlag2 = linkCheck(nl2,nl1,"update");
		//alert('linkFlag2 '+linkFlag2);
		if(linkFlag2 == 2){
			return;
		}
		if(linkFlag1 == 0 && linkFlag2 == 0){
			alert('两类节点不存在关联关系，无法关联');
			return ;
		}
		//关联节点
		$("#action_info").html("<input type='button' value='取消' id='exitLink'><br/>关联节点1：<br/>"+ nl1.nodeName+'<br/>关联节点2：<br/>'+ nl2.nodeName);
		//关联关系已存在 再次启用
		if($("#"+node_id1+"_x_"+node_id2).size()>0){//再次启用
			$("div[divType='line'][id^='"+node_id1+"'][id$='"+node_id2+"']").attr('action','update').each(function(){
				activeLines($(this)[0],0);//手动左键激活
			});
		}else if($("#"+node_id2+"_x_"+node_id1).size()>0){//再次启用
			$("div[divType='line'][id^='"+node_id2+"'][id$='"+node_id1+"']").attr('action','update').each(function(){
				activeLines($(this)[0],0);//手动左键激活
			});
		}else{//创建新关联关系
			$('#board').createLine({childId:node_id1,fatherId:node_id2,state:'#00ff00'});
	 		$("div[divType='line'][id^='"+node_id1+"'][id$='"+node_id2+"']").attr("action",'update').each(function(){
	 			activeLines($(this)[0],0);//手动左键激活
	 		});
		}
		$('#'+node_id1).resetLine();
		$("#exitLink").trigger('click');
		param.actionFlag = true;
		return ;
	}
	//关联校验解析函数
	//nl1 节点1的data数据{"tableName@$@needFlag@$@linkSize"}
	//nl2 节点2的data数据
	//action 0:delete 1:update
	//return i值 0：不存在关联约束   1：可以关联  2：约束成立 
	function linkCheck(nl1,nl2,action){
		var i=0;
		var node_id1 = nl1.tableName+"_"+nl1.nodeType+"_"+nl1.nodeId;
		var splitArray = nl1.linkSplit.split(",");
		for( sa in splitArray ){//循环比较关联匹配式
			if(splitArray[sa].indexOf(nl2.tableName)>-1){//存在关联接点的匹配式进一步比较
				++i;
				if(action=='delete'){//delete操作
					var needFlag = splitArray[sa].split("@$@")[1]==""? 0 : splitArray[sa].split("@$@")[1]*1;
					if(needFlag==1){//必须 至少保留一条关联
						if($("div[id*='"+node_id1+"'][id*='"+nl2.tableName+"'][id*='_x_'][action!='delete']").size()==1){
							alert(nl1.nodeName+"->"+nl2.nodeName+"\n此关联为必要关联，不能删除！");
							++i;//值为2
						}else{
							//return true;//i值为1
						}
					}else{//非必须 可以任意删除关联
						//return true;//i值为1
					}
				}else if(action=='update'){//update操作
					var linkSize = splitArray[sa].split("@$@")[2]==""? 0 : splitArray[sa].split("@$@")[2]*1;
					if(linkSize==0){
						//return true;//i值为1
					}else if(linkSize>0){
						if($("div[id*='"+node_id1+"'][id*='"+nl2.tableName+"'][id*='_x_'][action!='delete']").size()<linkSize){
							//return true;//i值为1
						}else{
							if(confirm("节点已存在此类关联，是否删除其原有关联执行新的关联？")){//删除连线
								$("div[id*='"+node_id1+"'][id*='"+nl2.tableName+"'][action!='delete']").attr("action",'delete').attr('active','0').hide();
								//return true;//i值为1
							}else{
								++i;
							}
						}
					}
				}
			}
		}
		return i;
	}
	
	
	function getbox(url) 
	{
		$.post(url+"&r="+new Date(),{},resuGetBox,'html');
	}
	function resuGetBox(data){
		var obj = eval("("+data+")");
		var i;
		var p =$("#ispn").offset();
		
		if(obj.nodeBoxList.length>0){
			for(i=0;i<obj.nodeBoxList.length;i++){
				var box=obj.nodeBoxList[i];
				var box_id = box.tableName+'_'+box.nodeType+'_'+box.nodeId;
				$("#boxList").createLiNodeBox(box_id,box);
			}
			$("#suggest1").css('top',p.top-15+'px').css('left',p.left-10+'px');
			
			$("#suggest1").bind({
				'mouseenter' : function() {//单引
					$("#suggest1").show();
				},
				'mouseleave' : function() {//单引
					//2秒后自动关闭
					window.setTimeout(function(){$("#suggest1").hide(800)},2000);
				}
			});
			
			$("#suggest1").show();
		}
	}
	
	function getBoxByGroup(data,url){
		$.post(url+"&r="+new Date(),{},showBoxView,'html');
	}
	function showBoxView(data){
		if(data==null){
			alert("此节点组下没有维护机柜信息");
			return;
		}
		var obj = eval("("+data+")");
		
		var i;
		//创建所有节点
		if(obj.nodeBoxList.length>0){
			for(i=0;i<obj.nodeBoxList.length;i++){
				var nl=obj.nodeBoxList[i];
				var node_id = nl.tableName+'_'+nl.nodeType+'_'+nl.nodeId;
				$("#rightBoxContent").showBox(node_id,nl);
			}
			
		}
		
	}
	
	function openBoxqRequest(data,url){
		$.post(url+"&r="+new Date(),{},openBox,'html');
	}
	function openBox(data){
		if(data==null){
			return;
		}
		var obj = eval("("+data+")");
		var i;
		if(obj.nodeInBoxList.length>0){
			//创建所有节点
			for(i=0;i<obj.nodeInBoxList.length;i++){
				var nl=obj.nodeInBoxList[i];
				var node_id = nl.tableName+'_'+nl.nodeType+'_'+nl.nodeId;
				createPoint(node_id,nl);
			}
			
		}
	}
	function createPoint(node_id,nl){
			var $newSpan = getIconModel(1);
			if(nl.isDragg=="1"){//如果是1则禁用此元素的拖拽功能
				$newSpan.draggable('option','disabled',[true]);
			}
			if(nl.nodeUType>1){
				var u = nl.nodeUType; //U位
				var position = nl.latticePosition; //位置
				var j;
				for(j=1;j<u;j++){
					var thisTd = parseInt(position) + j;
					//$("#"+thisTd).parent().remove();
					//$("#spanTable").find('#'+thisTd).parent().remove();//删除占位置的tr
					$("#spanTable").find('#'+thisTd).parent().hide();//删除占位置的tr
				}
				//alert($("#spanTable")[0].outerHTML);	
			}
	
			$newSpan.attr('id',node_id);
			$newSpan.attr('uType',nl.nodeUType);
			//绑定鼠标移入移出事件
			$newSpan.bind({
				'mouseenter' : function() {//单引
					
					$("#spanIp").text(nl.nodeIp);
					$("#spanName").text(nl.nodeName);
					$("#spanUType").text(nl.nodeUType+"U");
					var iconBoxPosition = {
						top: $("#iconBox").css('top').replace("px","")*1,
						left: $("#iconBox").css('left').replace("px","")*1,
						width: $("#iconBox").css('width').replace("px","")*1,
						height: $("#iconBox").css('height').replace("px","")*1
					}
					
					var cPointY=0;
					var cPointX=0;
					
					var sumWidth = $("#board").css('width').replace("px","")*1;
					var sumheight = $("#board").css('height').replace("px","")*1;
					
					var iconBoxLeft = parseInt(iconBoxPosition.left);
					if(sumWidth-iconBoxLeft < parseInt(iconBoxPosition.width+200)){
						cPointY = parseInt(iconBoxPosition.left - iconBoxPosition.width);
					}else{
						cPointY = parseInt(iconBoxPosition.left + iconBoxPosition.width+10);
					}
					cPointX = parseInt(iconBoxPosition.top + parseInt(nl.latticePosition)*11+30);
					if(cPointX+100>sumheight){
						cPointX = sumheight-110;
					}
					
					$("#span_info1")
					.css('top',cPointX+'px')
					.css('left',cPointY+'px')
					.show();
				},
				'mouseleave' : function() {
					$("#span_info1").hide();
					$("#spanIp").empty();
					$("#spanName").empty();
					$("#spanUType").empty();
				}
			});
			$("#spanTable").find('#'+nl.latticePosition).append($newSpan);
			//data缓存中保存数据
			$newSpan.data("data", nl);
			$newSpan.data("node_id", node_id);	
	}
	
	//即刻更新节点信息
	function updateNode(url){
		var max=$("a[divType='node'][action='update']").size();
		var i=0;
		var sysPositionId;
		//alert(max);
		saveOne();
		function saveOne(){
			if(i<max){
				var $node = $("a[divType='node'][action='update']:eq("+i+")");
				var nl0 = $node.data('data');
				if(nl0==null){
					saveOver(1);
					return;
				}
				if(nl0.nodeType == 'nodeGroup'){//节点组
					nl0.viewType = 1;
					nl0.parent_id = sysPositionId;//set parentID
					$("#sysNpId").val(sysPositionId);
				}
				if(nl0.nodeType == 'nodeBox'){//机柜
					nl0.viewType = 2;
					nl0.parent_id = $("#groupNpId").val();//set parentID
				}
				nl0.positionX = $node.css('left').replace("px","")*1;
				nl0.positionY = $node.css('top').replace("px","")*1;
				nl0.imageUrl = $node.find('img').attr('src');
				$.post(url+"&r="+new Date(),
							{'nodeM.id':nl0.id,'nodeM.tableName':nl0.tableName
							,'nodeM.nodeType':nl0.nodeType,'nodeM.nodeId':nl0.nodeId
							,'nodeM.parent_id':nl0.parent_id,'nodeM.nodeDesc':nl0.nodeDesc
							,'nodeM.positionX':nl0.positionX,'nodeM.positionY':nl0.positionY
							,'nodeM.imageUrl':nl0.imageUrl,'nodeM.viewType':nl0.viewType
							}
							,saveOver,'html');
				
			}else{
				alert('保存成功');
				document.location.reload();
			}
		}
		function saveOver(datas){
			if(datas != null){
				if(datas== 'save1'){
					i++;
					saveOne();
				}else{
					sysPositionId = datas;
					i++;
					saveOne();
				}
				
			}else{
				var $node = $("a[divType='node'][action='update']:eq("+i+")");
				alert('Error');
			}
		}
	}

	//保存机柜联线
	function saveBoxLine(ngid,boxNodeId,url){
		//机柜下的节点
		var max=$("div[id='iconBoxContent']").find("span").size();
		//alert(max);
		var saveResu = false;
		var nIdSeqLatts="";
		var seqList="";
		if(max>0){
			var m;
			for(m=0;m<max;m++){
				var $nodeSpan = $($("div[id='iconBoxContent']").find("span").get(m));
			
				var node = $nodeSpan.data('data');
				var lid = $nodeSpan.parent().attr('id');
				
				var resu = $nodeSpan.draggable('option','disabled');
				if(!resu){
					if(m!=max-1){
						//节点Id@seq_位置
						nIdSeqLatts+=node.nodeId+"@"+node.seqId+"_"+lid+":";
						seqList +=node.seqId+",";
					}else{
						nIdSeqLatts+=node.nodeId+"@"+node.seqId+"_"+lid;
						seqList +=node.seqId;
					}	
				}
			}
			$.ajax( {
				url : url+"&r="+new Date(),
				type : "POST",
				dataType : "html",
				data:{'seqList':seqList,'nIdSeqLatts':nIdSeqLatts,'nodeGroupId':ngid,'boxId':boxNodeId},
				async:false,
				success : function(data) {
					if(data == '1'){
						saveResu = true;
					}
				}
			})
			//$.post(url+"&r="+new Date(),{'seqList':seqList,'nIdSeqLatts':nIdSeqLatts,'nodeGroupId':ngid,'boxId':boxData.nodeId},"",'html');
					
		}else{
			alert("请先维护节点再保存");
			return;
		}
		return saveResu;
	}
	//删除机柜上的节点
	function delLattNode(data,url){
		//alert("in");
		$.post(url+"&r="+new Date(),{'seqId':data.seqId},"",'html');
	}
	//保存机柜联线
	function deleteBoxLine(url){
		var lineNum=$("div[divType='line'][action='update'][id*='_x_']").size();//线条数
		var j = 0;
		deleteOne();
		function deleteOne(){
			if(lineNum >j){
				var $line = $("div[divType='line'][action='update'][id*='_x_']:eq("+j+")");
				var ll = $line.attr("id").split('_x_');
				
				var nl0 = $("#"+ll[0]).data('data');
				var nl1 = $("#"+ll[1]).data('data');
			
				if(nl0.nodeType=='nodeBox'){//机柜
					$.post(url+"&r="+new Date(),{'boxId':nl0.nodeId},deleteOver,'html');
				}else{
					$.post(url+"&r="+new Date(),{'boxId':nl1.nodeId},deleteOver,'html');
				}
				
			}else{
				
			}
		}
		function deleteOver(datas){
			if(datas == '1'){
				j++;
				deleteOne();
			}else{
				//var $node = $("a[divType='node'][action='update']:eq("+i+")");
				alert('Error');
			}
		}
	}
	//保存节点信息
	function saveNode(url,type){
		var max=$("a[divType='node'][action='update']").size();
		var i=0;
		var sysPositionId;
		//alert(max);
		saveOne();
		function saveOne(){
			if(i<max){
				var $node = $("a[divType='node'][action='update']:eq("+i+")");
				var nl0 = $node.data('data');
				if(nl0==null){
					saveOver(1);
					return;
				}
				if(type=='0'){//保存系统
					if(nl0.nodeType == 'nodeGroup'){//节点组
						nl0.viewType = 1;
						sysPositionId = nl0.parent_id;//set parentID
						$("#sysNpId").val(sysPositionId);
					}
				}
				
				if(nl0.nodeType == 'nodeBox'){//机柜
					nl0.viewType = 2;
					nl0.parent_id = $("#groupNpId").val();//set parentID
				}
				nl0.positionX = $node.css('left').replace("px","")*1;
				nl0.positionY = $node.css('top').replace("px","")*1;
				nl0.imageUrl = $node.find('img').attr('src');
				$.post(url+"&r="+new Date(),
							{'nodeM.id':nl0.id,'nodeM.tableName':nl0.tableName
							,'nodeM.nodeType':nl0.nodeType,'nodeM.nodeId':nl0.nodeId
							,'nodeM.parent_id':nl0.parent_id,'nodeM.nodeDesc':nl0.nodeDesc
							,'nodeM.positionX':nl0.positionX,'nodeM.positionY':nl0.positionY
							,'nodeM.imageUrl':nl0.imageUrl,'nodeM.viewType':nl0.viewType
							}
							,saveOver,'html');
				
			}else{
				alert('保存成功');
				document.location.reload();
			}
		}
		function saveOver(datas){
			if(datas != null){
				if(datas== 'save1'){
					i++;
					saveOne();
				}else{
					sysPositionId = datas;
					i++;
					saveOne();
				}
				
			}else{
				var $node = $("a[divType='node'][action='update']:eq("+i+")");
				alert('Error');
			}
		}
	}
	//删除节点信息
	function deleteNode(url,npids,nodeIds){
		var delResu="";
		delbegin();
		function delbegin(){
			$.ajax( {
				url : url+"&r="+new Date(),
				type : "POST",
				dataType : "html",
				data:{'npids':npids,'nodeids':nodeIds},
				async:false,
				success : function(data) {
					if(data == '1'){
						delResu = true;
					}
				}
			})
			
		}
		return delResu;
	}
	//保存关联节点信息
	function saveLine(url){
		var max=$("div[divType='line'][action='update'][id*='_x_']").size();
		var i=0;
		saveOne();
		function saveOne(){
			if(i<max){
				var $line = $("div[divType='line'][action='update'][id*='_x_']:eq("+i+")");
				var ll = $line.attr("id").split('_x_');
				//$line.css('background-color','#000000');
				var nl0 = $("#"+ll[0]).data('data');
				var nl1 = $("#"+ll[1]).data('data');
				//alert( $("#"+ll[0]).size()+" "+ $("#"+ll[1]).size());
				if(nl0==null || nl1==null  ){
					saveOver(1);
				}else{
					$.post(url+"&r="+new Date(),{'tableName1':nl0.tableName,'nodeId1':nl0.nodeId,'tableName2':nl1.tableName,'nodeId2':nl1.nodeId},saveOver,'html');
				}
			}else{
				//alert('保存成功');
				//document.location.reload();
			}
		}
		function saveOver(datas){
			if(datas == '1'){
				i++;
				saveOne();
			}else{
				var $node = $("div[divType='line'][action='update'][id*='_x_']:eq("+i+")");
				alert('Error');
			}
		}
	}
