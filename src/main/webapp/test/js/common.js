//全部选中/或不选
	function allSelect(obj){
 		var b = false;
	    if(obj.checked){
	     b = true;
	    }else{
	     b = false;
	    }
		checkbox = document.all.checkRecords;
		if(checkbox){
			if(checkbox.length){
			size = checkbox.length;
			for(i=0;i<size;i++){
				try{
					checkbox[i].checked=b;
				} catch(ex) {
					;
				} 
			}
			}else{
			 checkbox.checked = b;
			}
		}
	}
	//删除选择记录
	var delMsg = true;
	function delSelectRecords(url){
	if(delMsg){
	 if(!window.confirm("确认删除记录？"))return;
	}
	var b = false;
	 checkbox = document.all.checkRecords;
		size = checkbox.length;
		if(size){
		for(i=0;i<size;i++){
			try{
				if(checkbox[i].checked){
				 b = true;
				 break;
				}
			} catch(ex) {
				;
			} 
		}
		}else{
		 if(checkbox.checked){
		   b = true;
		 }
		}
	 if(!b){
	  alert("请选择要删除的记录!");
	  return;
	 }
	 document.forms(0).action = url;
	 document.forms(0).submit();
	}
	//删除某行记录
	function delRecord(obj,url){
	 if(!window.confirm("确认删除记录？"))return;
	 checkbox = document.all.checkRecords;
		if(checkbox){
			if(checkbox.length){
			size = checkbox.length;
			for(i=0;i<size;i++){
				try{
					checkbox[i].checked=false;
				} catch(ex) {
					;
				} 
			}
			}else{
			 checkbox.checked = false;
			}
		}
	 
	 delMsg = false;
	 var checkObj = obj.parentElement.parentElement.cells(0).childNodes(1);
	 //alert(obj.parentElement.parentElement.cells(0).childNodes(0).id);
	 checkObj.checked = true;
	 delSelectRecords(url);
	}
	
	 //选择数据/下拉菜单
	function checkSelect(obj,value){
		for(i=0;i<obj.options.length;i++){
			if(obj.options[i].value == value){
				obj.options[i].selected = true;
				break;
			}
		}
	}