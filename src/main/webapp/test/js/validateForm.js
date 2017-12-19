//--------------Form validate---------------

/**
	===验证所有表单元素的值是否为空或是否未选中===
	1.验证form必须加validate="true"
	2.验证form元素必须加required="true" msg="显示内容"
	3.submit提交自动验证
	4.button调用js form.submit()提交时必须得调用verification方法才能去验证
	5.verification()方法的参数需要传要验证的form对象，返回true或false
*/

//页面加载事件:对于非空验证的加星号，且设定提交时自动进行验证
$(function(){
	$("form").each(function(i,myForm){
		if(myForm.validate=="true") {
			//如果此表单要求验证，且表单中的元素要求非空验证，则给元素加红星号
			$("input[type=text],input[type=password],input[type=file],select,textarea",myForm).each(function(i,this_element){
				if(this_element.required=="true") {
					$(this).parent().append("<font color='red'>*</font>");
				}
			});
			
			//如果此表单要求验证，且表单中的元素要求非空验证，则提交事件发生时，自动进行验证（非空）
			$(myForm).submit( function () {
				return ckElements(myForm);
			}); 
				
		}
	});
});

//button调用js form.submit()提交时必须得调用verification方法才能去验证
function verification(myForm) {
	var flag = true;
	if(myForm.validate=="true") {
		flag = ckElements(myForm);
	}
	return flag;
}

//required="true":要求非空验证，blankMsg非空提示信息
//datatype="number"：要求数字验证，numberMsg非数字提示信息
//email="true":要求email格式验证，emailMsg非email格式提示信息
function ckElements(myForm) {
	var flag = true;
	$("input[type=text],input[type=password],input[type=file],select,textarea",myForm).each(function(i,this_element){
		//非空验证
		if(this_element.required=="true") {	
				
			if($.trim($(this).val())=="") {
				flag = false;
				alert(this_element.blankMsg);
				this_element.value="";
				this_element.focus();
				
				return false;
			}
		}
		
		//数字验证
		if(this_element.datatype=="number") {

			if(isNaN(this_element.value)) {
				flag = false;				
				alert(this_element.numberMsg);
				this_element.value="";
				this_element.focus();
				
				return false;
			}
		}
		
		//邮箱格式验证
		if(this_element.email=="true") {
			if(!isEmail(this_element.value)) {
				flag = false;				
				alert(this_element.emailMsg);
				//this_element.value="";
				this_element.focus();
				
				return false;
			}
		
		}
	});
	return flag;
}

//邮箱格式验证：匹配返回true，不匹配返回false
//验证规则：*@*.*
//至少以一个单词(包括字母数字下划线)开头  +   0个或多个 .单词 或者 -单词 
//@ + 一个以上字母或数字，0个或多个  .字母或数字  或者 -字母或数字 ，
//. + 一个以上字母或数字
//haiyuxia123@126.com.cn
function isEmail(str){
		var reg=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/ ;
		return reg.test(str);
}

//判断两个值的关系,暂时不可用
function (opr1,relationship,opr2,msg) {
	var start= document.getElementById(opr1).value;
	var end = document.getElementById(opr2).value;
	if(loopStartTime>loopEndTime) {
		alert(msg);
		return false;
	}
}

function checkAll(c)
{
	var checks = document.getElementsByTagName("input");
	for(i=0;i<checks.length;i++)
	{
	if(checks[i].type=='checkbox'&&checks[i].disabled!=true)
 	{
 	checks[i].checked=(c.checked)?'checked':''}
	}
}

//统计jsp页面上复选框选中的个数
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

/**判断选中的条数是否正确：
     修改edit只能选一条（=1），
     删除remove要选一条及以上—（>=1），
     其它操作multiDo（停用或启用等）可以一条及以上(>=1)
*/
function isRigthCheck(checkName,toDo){
	var selectedCount = countChecked(checkName);
	
	if(selectedCount > 0){
		if(toDo == "edit")//编辑只能选中一条,=1
			return selectedCount > 1 ? false : true;
		else if(toDo == 'remove')//删除要选中一条以上,>=1
			return true;
		else if(toDo == 'multiDo')//>=1
			return true;
	}else{
		return false;//一条也没选中
	}
}
//判断是否进行提交操作???
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

//判断是否进行提交操作？？？
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

//？？？？表单验证？？
function $id(name,type,show){
	name=document.getElementById(name).value;
	 if(name==''&& type!="remark"){
	 	alert('请输入'+show);
	 	return false;
	 }
	 if(type=='sz'){
		if(name.replace(/[\d+]/ig,"").length>0){
		 	alert(show+'请输入数字')
	        return false;
        }
	 }
	 if(type=='pzName'){
         if(name.length>100){
        	alert(show+'不能大于一百个字符')
	        return false;
        }
	 }
	  if(type=='port'){
		if(name.replace(/[\d+]/ig,"").length>0){
		 	alert(show+'请输入数字')
	        return false;
        }
         if(name<0){
        	alert(show+'不能小于零')
	        return false;
        }
         if(name>65535){
        	alert(show+'不能大于65535')
	        return false;
        }
	 }
	 if(type=='harm'){
		if(name.replace(/[\d+]/ig,"").length>0){
		 	alert(show+'请输入数字')
	        return false;
        }
        if(name<0){
        	alert(show+'不能小于零')
	        return false;
        }
         if(name>100){
        	alert(show+'不能大于一百个字符')
	        return false;
        }
	 }
	  if(type=='udpKey'){
         if(name.length>8){
        	alert(show+'不能大于8个字符')
	        return false;
        }else if(name.length<8){
       		 alert(show+'不能小于8个字符')
	        return false;
        }
	 }
	 if(type=='namesz'){
		if(name.replace(/[\d+]/ig,"").length>0){
		 	alert(show+'请输入数字')
	        return false;
        }
        if(name<0){
        	alert(show+'不能小于零')
	        return false;
        }
         if(name>100){
        	alert(show+'不能大于一百个字符')
	        return false;
        }
	 }
	 
	 if(type=='ip'){
	 	var arr=name.match(/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/);   
		 if(arr==null){
		 	alert("输入"+show+"不符合要求");
		 	return false;  
		 } 
		 for(i=1;i<arr.length;i++){
		     if(String(Number(arr[i]))!=arr[i]||Number(arr[i])>255){
		      alert("输入"+show+"不符合要求");
		     	return false; 
		     }
		 }
	  }   
	  if(type=='mask'){
	 	var arr=name.match(/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/);   
		 if(arr==null){
		 	alert("输入"+show+"不符合要求");
		 	return false;  
		 } 
		 for(i=1;i<arr.length;i++){
		     if(String(Number(arr[i]))!=arr[i]||Number(arr[i])>255){
		      alert("输入"+show+"不符合要求");
		     	return false; 
		     }
		 }
	  }   
	  if(type=='remark'){
         if(name.length>500){
        	alert(show+'不能大于五百个字符')
	        return false;
        }
	 }
	 if(type=='zmnr'){
         if(name.length>1000){
        	alert(show+'不能大于一千个字符')
	        return false;
        }
	 }
	
	 return true;
}