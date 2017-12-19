    var headerFloating = true;						// 是否实现表头浮动标识 true 实现 ；flase 不实现   默认 实现
    var headerMinRows = 1;							// 表头航标   默认 1行
    var headerOnResize = true;						// 窗口大小变化时 同步表头浮动   默认实现
	var floatingDivId = 'tableHeaderDiv';			// 表头divId
	var defaultDataListId = null;					// 
	var defaultIframeWindowId = null;
	
    /* 是否实现表头浮动
    **/
    function initHeaderFloating(flag){
    	headerFloating = flag;
    }
    
    /* 浮动表头行数
    **/
    function initHeaderMinRows(num){
    	headerMinRows = num;
    }
    
    /* 浮动表头 同步窗口大小的变动
    **/
    function initHeaderOnResize(flag){
    	headerOnResize = flag;
    }
    
    /* 获取指定iframeId的window dom对象和层数
    **/
    function getIframeWindow(iframeId){
    	var maxSize = 5;							// 循环最大次数限制 防止死循环
    	var findFlag = false;						// 查找标识
    	
    	// 获取 I3 window dom对象
    	var windowObj = window; 					// I3的window dom对象 
		var parentSize = 1;							// iframe 所在的层数 当前window层为I3的 1 为子层 2 为子子层，3 为子子子层
    	
    	// iframeId 无效 查找失败 数据重置  windowObj 为 当前window  i 为0层 即无iframe
		if(iframeId == '' || iframeId == null){
			windowObj = window;
			parentSize = 0;
			return {windowObj:windowObj,parentSize:parentSize};
		}
		
		
		//循环获取父窗口的 window dom对象
		//如果获取到I3 即能 I3window 及 层次(i)
		for(parentSize;parentSize<maxSize;parentSize++){  							// 最大循环次数为5  防止死循环
			if(windowObj.parent.document.getElementById(iframeId) != null ){
				findFlag = true;					// 找到指定Iframe
				break;								// 跳出循环
			}else{     								// I3不再本层 继续 获取父窗口
				windowObj = windowObj.parent; 		// 获取父窗口并更新 window对象
			}
		}
		
		// 查找iframe失败 数据重置  windowObj 为 当前window  i 为0层 即无iframe
		if(!findFlag){
			windowObj = window;
			parentSize = 0;
		}else{
			parentSize--;
		}
		//alert("parentSize "+parentSize)
		return {windowObj:windowObj,parentSize:parentSize};
    }
    
	/*当前window所在的Iframe 在windowObj 页面内的 位置
	* 参数 parentSize  当前window 到 指定Iframe window的层数
	**/
    function getIframePositionInWindowObj(parentSize){
		var top = 0;			//上边距
		var left = 0;			//左边距
		
		var windowObj = window;
		for(var j = 0 ;j < parentSize ; j++){
			var $winIframe = $(windowObj.parent.document.getElementsByName($(windowObj.frames).first().attr('name'))[0]);
			//alert($(windowObj.frames).first().attr('name')[0]+" "+$winIframe.position().top);
			if($winIframe != null){
				top += $winIframe.position().top;
				left += $winIframe.position().left;
			}
			windowObj = windowObj.parent;
		}
		return {top:top,left:left};
    }
    
    /* 克隆表tableId表的表头 固定到iframeId的Iframe顶部
    **/
    function tableHeaderNavigator(iframeId,tableId){
    
    	var lockHeader = false;						//内部参数 表头浮动 实现标识 true 实现表头浮动 ；flase 取消表头浮动
    	var headerRows = 0;							//内部参数 表头浮动 浮动行数	 
    	var onResize = false;						//内部参数 表头浮动 同步窗口大小
    	
    	/* 表头浮动准备
    	**/
    	
    	//获取 数据列表
   		var $tableOrg = $("#"+tableId).last();
    	if( $tableOrg.length == 0){					//当无法获取到数据列表时，终止操作
    		return false;
    	}
    	
		//相关参数初始化
		lockHeader = headerFloating;			
		headerRows = headerMinRows;
		onResize = headerOnResize;
		//alert('headerRows '+headerRows);
		//是否实现表头浮动标识
		if(!lockHeader){
			return false;
		}
		
    	//克隆 数据列表 做浮动表头
    	var tableCloneId = tableId+'Clone';
		//var $tableClone = $tableOrg.clone(true);	//克隆表，做表头固定
		var $tableClone = $("#"+tableCloneId);
		//alert($tableClone.size());
		if($tableClone.size()==0){
			$tableClone = $tableOrg.clone(true);	//克隆表，做表头固定
		}
	    	$tableClone.attr('id',tableCloneId);		//添加属性 Id
		//$tableClone.append($tableOrg.find(">tr:lt(10)").clone(true));

	    /* 计算表头行数rowsSize 格式化克隆表（同步表头列宽、清理非表头数据、事件同步）
	    **/
	    var headerTop = $tableOrg.position().top;			// 表头上边距
	    var headerLeft = $tableOrg.position().left;			// 表头左边距
	    var headerHeight = 0;								// 表头高度
	    var headerWidht  = $tableOrg.width(); 				// 表头宽度
	    var rowsSize = headerRows;							// 表头行数
	    
	    var maxColHeight = 0;								// 表头单行高
	    var row=0,col=0;									// 行标  列标
	    var $colDataOrg = null;								// 原行TD数据
	    var $colDataClone = null;							// 克隆表行TD数据
    	//行列两层嵌套循环，对应设定克隆表列宽
    	//计算表头行数rowsSize、表头高度headerHeight
	    for(row;row<rowsSize;row++){	//行循环

	    	$colDataOrg = $tableOrg.find("tr").eq(row).children();		// 原数据表 取第row行数据 
	    	$colDataClone = $tableClone.find("tr").eq(row).children();	// 克隆表   取第row行数据

	    	for(col; col< $colDataClone.length;col++){	//列循环
	    	    var $cellClone = $colDataClone.eq(col);
	    		var $cellOrg = $colDataOrg.eq(col);
	    		//alert($cellOrg.width());
	    		//设置对应列宽
	    		$cellClone.width($cellOrg.width());			// 将原数据表的列宽设置到对应的克隆表中
	    		
	    		//Cell内checkbox事件同步
	    		$cellClone.find("input[type='checkbox']").each(function (){
	    		
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
	    		if($cellOrg.attr('rowspan')>rowsSize-row){				// 判断表头行数
	    			rowsSize = rowsSize + $cellOrg.attr('rowspan')-1;	// 更新表头行数
	    		}
	    		//获取 浮动表头 单行高度
	    		if($cellOrg.height()>maxColHeight){				// 判断单行行高
	    			maxColHeight = $cellOrg.height();
	    		}
	    	}
	    	
	    	headerHeight += maxColHeight;					// 行高累加
	    	maxColHeight = 0;								// 单行行高 重置
	    	col =0;											// 列  重置
	    }

    	// 删除 克隆表 非表头数据
		var $tableCloneRows = $tableClone.find('tr');
		for(var m = rowsSize; m < $tableCloneRows.length;m++){
				$tableCloneRows.eq(m).remove();
		}
    	
    	/* 获取 I3 window dom对象
    	**/
    	var wObj = getIframeWindow(iframeId);				// 获取Iframe window信息
    	var windowObj = wObj.windowObj; 					// I3的window dom对象 一下简称‘I3window’
		var parentSize = wObj.parentSize;					// iframe 所在的层数 当前window层为I3的 1 为子层 2 为子子层，3 为子子子层
		
		/**
		*  实现div表头固定
		**/
		
		//检查 表头是否已存在 删除已存在的 以备添加新表头
		var obj = window.document.getElementById(floatingDivId); // 获取 I3window 中的 floatingDivId 删除-更新 该div
	    if (obj) {//若存在 删除
	        $(obj).remove(); //删除 div
	    }
		
		//div 位置 固定 固定位置和宽高 px值
		var divT = 0;										// div top
		var divL = ($tableOrg.position().left);				// div left
		var divR = ($tableOrg.position().left+headerWidht);	// div right
		var divH = headerHeight;							// div height
		var divW = headerWidht;								// div width
		//alert($tableOrg.position().left);
		//生成表头div
		var headerDiv = window.document.createElement("div");//创建一个div 
	    var $headerDiv = $(headerDiv);
	    $headerDiv.css("top", divT+'px');			//显示的Y轴到上边框距离
		$headerDiv.css("left", divL+'px');		//显示的X轴到左边框距离
		$headerDiv.css("width", divW+'px');		//宽度
	    $headerDiv.css("height", divH+'px');		//高度

//      $headerDiv.css('border','1px');			//边框宽度
        $headerDiv.css("z-index", "20");			//div层 第20层 保证该层不被挤用
        $headerDiv.css('position','absolute');	//相对于窗口的位置固定 影响 top left的值
//		$headerDiv.css('position','fixed');		//相对于页面的位置固定 影响 top left的值 IE不好使
		$headerDiv.css('display','inline');		//inline的形式显示
		$headerDiv.css("overflow", "hidden");		//内容被剪裁，隐藏
//		$headerDiv.css('overflow','visible');	//内容不被剪裁，显示
        
        
	    headerDiv.appendChild($tableClone[0]);				// 将克隆的表头添加到div中
	    $headerDiv.attr("id", floatingDivId);  				// 为div设置Id 为 floatingDivId
	    $headerDiv.insertBefore($('body table',window.document).first());		// 将div加入页面中
		
		//获取当前 iframe 在全窗口位置 
		var iframePosition = getIframePositionInWindowObj(parentSize);
		var iframeTop = iframePosition.top;					// iframe 上边距
		var iframeLeft = iframePosition.left;				// iframe 左边距
		//alert(iframeTop+" "+iframeLeft);
		
		//iframe 已得到  tableHeader已得到
		//判断表头是否需要显示
		if(($(windowObj).scrollTop() - iframeTop - headerTop)< 0){
			if($headerDiv.css("display") != 'none'){
				$headerDiv.css("display",'none');
			}
			$headerDiv.css('top',headerTop+'px');
		}else{
			if($headerDiv.css("display") == 'none'){
				$headerDiv.css("display",'inline');
			}
			$headerDiv.css('top',($(windowObj).scrollTop() - iframeTop)+'px');
			if(($(windowObj).scrollTop() - iframeTop - headerTop)> ($tableOrg.height())){
				$headerDiv.css("display",'none');
				$headerDiv.css('top',(headerTop + $tableOrg.height())+'px');
			}
		}
				
		/**
		* 添加滚动条监听事件
		**/
		$(windowObj.window).scroll(function(){
			
			//alert(($(windowObj).scrollTop() - tableHeaderH));
			//当 滚动条滚动 偏移 大于 表头所在像素位置时，显示浮动表头div
			if(($(windowObj).scrollTop() - iframeTop - headerTop) < 0){
				//判断 表头div是否是隐藏状态，若否设置为隐藏
				if($(headerDiv).css("display") != 'none'){
					//将div设置为隐藏 none
					$(headerDiv).css("display",'none');
				}
				//重新固定 将表头固定到列表表头位置
				$(headerDiv).css('top',headerTop+'px');
			}else{
				//判断 表头div是否是显示状态，若否设置为显示
				if($headerDiv.css("display") == 'none'){
					//将div设置为显示 inline
					$headerDiv.css('display','inline');
				}
				//重新固定 表头div的垂直方向位置（位置范围为本层window.document scorllHeight）
				$headerDiv.css('top',($(windowObj).scrollTop() - iframeTop)+'px');
				if(($(windowObj).scrollTop() - iframeTop - headerTop)> ($tableOrg.height())){
					$headerDiv.css("display",'none');
					$headerDiv.css('top',(headerTop + $tableOrg.height())+'px');
				}
			}
		});
		
		/* 为父添加窗口大小更改触发事件
		**/
		if(headerOnResize){
		    $("#"+tableId).resize(function(){
		    
		     	$tableOrg = $("#"+tableId);
		     	$tableClone = $("#"+tableCloneId);
		     	
		     	//重设置header高度值
				headerTop = $tableOrg.position().top;			// 表头上边距
	    		headerLeft = $tableOrg.position().left;			// 表头左边距
				headerHeight = 0;								// 表头高度
	    		headerWidht  = $tableOrg.width(); 				// 表头宽度
	    		maxColHeight = 0;								// 表头单行高
	    		
	     	   	/**获取表头，并设定列宽
			    **/
			    var row=0,col=0;						//行标  列标
			    var rowsSize=headerMinRows;				//表头行数
			    
			    var $colDataOrg = null;					//原行TD数据
			    var $colDataClone = null;				//克隆表行TD数据
		    	
		    	/**重设置表头列宽
		    	*  计算表头行数rowsSize
		    	**/
			    for(row;row<rowsSize;row++){//行循环
		
			    	$colDataOrg = $tableOrg.find("tr").eq(row).children();		//原数据表 取第row行数据 
			    	$colDataClone = $tableClone.find("tr").eq(row).children();	//克隆表   取第row行数据
			    	
			    	for(col; col< $colDataClone.length;col++){//列循环
			    		var $cellClone = $colDataClone.eq(col);
			    		var $cellOrg = $colDataOrg.eq(col);
			    		//设置对应列宽
			    		$cellClone.width($cellOrg.width());	//将原数据表的列宽设置到对应的克隆表中
			    		//校正 浮动表头 行数
			    		if($cellOrg.attr('rowspan')>rowsSize-row){		//判断表头行数
			    			rowsSize = rowsSize + $cellOrg.attr('rowspan')-1;	//更新表头行数
			    		}
			    		//获取 浮动表头 单行高度
			    		if($cellOrg.height()>maxColHeight){				// 判断单行行高
			    			maxColHeight = $cellOrg.height();
			    		}
			    	}
			    	headerHeight += maxColHeight;					// 行高累加
			    	maxColHeight = 0;								// 单行行高 重置
			    	col =0;		//列  重置
			    }
			    
			    //获取当前 iframe 在全窗口位置 
				iframePosition = getIframePositionInWindowObj(parentSize);
				iframeTop = iframePosition.top;					// iframe 上边距
				iframeLeft = iframePosition.left;				// iframe 左边距
				//alert(iframeTop+" "+iframeLeft);

				//重设置headerdiv参数
				divT = 0;										// div top
				divL = ($tableOrg.position().left);				// div left
				divR = ($tableOrg.position().left+headerWidht);	// div right
				divH = headerHeight;							// div height
				divW = headerWidht;								// div width
				//alert($tableOrg.position().left);
				$headerDiv.css("top", '0px');		//宽度
				$headerDiv.css("top", divT+'px');			//显示的Y轴到上边框距离
				$headerDiv.css("left", divL+'px');		//显示的X轴到左边框距离
				$headerDiv.css("width", divW+'px');		//宽度
			    $headerDiv.css("height", divH+'px');		//高度
				//判断表头是否需要显示
				if(($(windowObj).scrollTop() - iframeTop - headerTop)< 0){
					if($headerDiv.css("display") != 'none'){
						$headerDiv.css("display",'none');
					}
					$headerDiv.css('top',headerTop+'px');
				}else{
					if($headerDiv.css("display") == 'none'){
						$headerDiv.css("display",'inline');
					}
					$headerDiv.css('top',($(windowObj).scrollTop() - iframeTop)+'px');
					
					if(($(windowObj).scrollTop() - iframeTop - headerTop)> ($tableOrg.height())){
						$headerDiv.css("display",'none');
						$headerDiv.css('top',(headerTop + $tableOrg.height())+'px');
					}
				}
		    });
		    
		     $(windowObj).resize(function(){
		    
		     	$tableOrg = $("#"+tableId);
		     	$tableClone = $("#"+tableCloneId);
		     	
		     	//重设置header高度值
				headerTop = $tableOrg.position().top;			// 表头上边距
	    		headerLeft = $tableOrg.position().left;			// 表头左边距
				headerHeight = 0;								// 表头高度
	    		headerWidht  = $tableOrg.width(); 				// 表头宽度
	    		maxColHeight = 0;								// 表头单行高
	    		
	     	   	/**获取表头，并设定列宽
			    **/
			    var row=0,col=0;						//行标  列标
			    var rowsSize=headerMinRows;				//表头行数
			    
			    var $colDataOrg = null;					//原行TD数据
			    var $colDataClone = null;				//克隆表行TD数据
		    	
		    	/**重设置表头列宽
		    	*  计算表头行数rowsSize
		    	**/
			    for(row;row<rowsSize;row++){//行循环
		
			    	$colDataOrg = $tableOrg.find("tr").eq(row).children();		//原数据表 取第row行数据 
			    	$colDataClone = $tableClone.find("tr").eq(row).children();	//克隆表   取第row行数据
			    	
			    	for(col; col< $colDataClone.length;col++){//列循环
			    		var $cellClone = $colDataClone.eq(col);
			    		var $cellOrg = $colDataOrg.eq(col);
			    		//设置对应列宽
			    		$cellClone.width($cellOrg.width());	//将原数据表的列宽设置到对应的克隆表中
			    		//校正 浮动表头 行数
			    		if($cellOrg.attr('rowspan')>rowsSize-row){		//判断表头行数
			    			rowsSize = rowsSize + $cellOrg.attr('rowspan')-1;	//更新表头行数
			    		}
			    		//获取 浮动表头 单行高度
			    		if($cellOrg.height()>maxColHeight){				// 判断单行行高
			    			maxColHeight = $cellOrg.height();
			    		}
			    	}
			    	headerHeight += maxColHeight;					// 行高累加
			    	maxColHeight = 0;								// 单行行高 重置
			    	col =0;		//列  重置
			    }
			    
			    //获取当前 iframe 在全窗口位置 
				iframePosition = getIframePositionInWindowObj(parentSize);
				iframeTop = iframePosition.top;					// iframe 上边距
				iframeLeft = iframePosition.left;				// iframe 左边距
				//alert(iframeTop+" "+iframeLeft);

				//重设置headerdiv参数
				divT = 0;										// div top
				divL = ($tableOrg.position().left);				// div left
				divR = ($tableOrg.position().left+headerWidht);	// div right
				divH = headerHeight;							// div height
				divW = headerWidht;								// div width
				//alert($tableOrg.position().left);
				$headerDiv.css("top", '0px');		//宽度
				$headerDiv.css("top", divT+'px');			//显示的Y轴到上边框距离
				$headerDiv.css("left", divL+'px');		//显示的X轴到左边框距离
				$headerDiv.css("width", divW+'px');		//宽度
			    $headerDiv.css("height", divH+'px');		//高度
				//判断表头是否需要显示
				if(($(windowObj).scrollTop() - iframeTop - headerTop)< 0){
					if($headerDiv.css("display") != 'none'){
						$headerDiv.css("display",'none');
					}
					$headerDiv.css('top',headerTop+'px');
				}else{
					if($headerDiv.css("display") == 'none'){
						$headerDiv.css("display",'inline');
					}
					$headerDiv.css('top',($(windowObj).scrollTop() - iframeTop)+'px');
					
					if(($(windowObj).scrollTop() - iframeTop - headerTop)> ($tableOrg.height())){
						$headerDiv.css("display",'none');
						$headerDiv.css('top',(headerTop + $tableOrg.height())+'px');
					}
				}
		    });
	    }
    }
    //初始化浮动表头参数
    //iframeId:框架最外层IframeId 本工程中为I3
    //dataListId: 数据列表的Id
	function initTableHeaderFloatParam(iframeId,dataListId){
		defaultIframeWindowId = iframeId;
		defaultDataListId = dataListId;
		return ;
	}
    //初始化浮动表头
	function headerFloatReset(){
		tableHeaderNavigator(defaultIframeWindowId,defaultDataListId);
		return ;
	}
    //初始化浮动表头
    //iframeId:框架最外层IframeId 本工程中为I3
    //dataListId: 数据列表的Id
	function initTableHeaderFloat(iframeId,dataListId){
		defaultIframeWindowId = iframeId;
		defaultDataListId = dataListId;
		tableHeaderNavigator(defaultIframeWindowId,defaultDataListId);
		return ;
	}