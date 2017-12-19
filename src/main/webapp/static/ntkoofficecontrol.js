// 请勿修改，否则可能出错
	var userAgent = navigator.userAgent, 
				rMsie = /(msie\s|trident.*rv:)([\w.]+)/, 
				rFirefox = /(firefox)\/([\w.]+)/, 
				rOpera = /(opera).+version\/([\w.]+)/, 
				rChrome = /(chrome)\/([\w.]+)/, 
				rSafari = /version\/([\w.]+).*(safari)/;			
				var height = $(document).height(); 
				var path;
				var clsid;
				var staticPath=$("#staticPath").val();
				if(window.navigator.platform == "Win32")
				{
					path=staticPath+"/OfficeControl.cab";
					clsid="A39F1330-3322-4a1d-9BF0-0BA2BB90E970";
				}else
				{
					path=staticPath+"/OfficeControlx64.cab";
					clsid="C9BC4DFF-4248-4a3c-8A49-63A7D317F404";
				}
				var browser;
				var version;
				var ua = userAgent.toLowerCase();
				function uaMatch(ua) {
					var match = rMsie.exec(ua);
					if (match != null) {
						return { browser : "IE", version : match[2] || "0" };
					}
					var match = rFirefox.exec(ua);
					if (match != null) {
						return { browser : match[1] || "", version : match[2] || "0" };
					}
					var match = rOpera.exec(ua);
					if (match != null) {
						return { browser : match[1] || "", version : match[2] || "0" };
					}
					var match = rChrome.exec(ua);
					if (match != null) {
						return { browser : match[1] || "", version : match[2] || "0" };
					}
					var match = rSafari.exec(ua);
					if (match != null) {
						return { browser : match[2] || "", version : match[1] || "0" };
					}
					if (match != null) {
						return { browser : "", version : "0" };
					}
				}
				var browserMatch = uaMatch(userAgent.toLowerCase());
				if (browserMatch.browser) {
					browser = browserMatch.browser;
					version = browserMatch.version;
				}
			

	function OnComplete2(type,code,html)
{   
		//生成报告关闭控件
		if(html!="定稿"&&html!="解除定稿"&&html!="仅保存"&&html!="签章完成")
			{
				var ntkoobj = document.getElementById("TANGER_OCX");
				var aId=$("#modelFileName",parent.document).val();
				$("#"+aId,parent.document).text(html);	
				ntkoobj.FullScreenMode=false;
				ntkoobj.close();
				window.parent.window.jBox.close();
			}
		else{
			var ntkoobj = document.getElementById("TANGER_OCX");
			var id = $('#checkbutton').val();
			if(html=='签章完成')
				{
				$("#"+id,parent.document).val(html);
				$("#"+id,parent.document).attr("disabled","disabled");				
				ntkoobj.FullScreenMode=false;
				ntkoobj.close();
				window.parent.window.jBox.close();
			 }else if(html=='仅保存'){  //关闭窗口
				ntkoobj.FullScreenMode=false;
				ntkoobj.close();
				window.parent.window.jBox.close();
			 }
		}
}
	



	/**
	 * firefox下点击按钮执行的操作
	 * 
	 * @param btnIdx 按钮索引
	 */
	function OnComplete4(btnIdx){
			if(btnIdx==0)
		{
			printVw();
		}
		else if(btnIdx==1)
		{
			printOt();
		}
		else if(btnIdx==2)
		{
			//-- 定稿
			defineDocument();
		}
		else if(btnIdx==3){
			// 解除定稿
			cancelDocument();
		}
		else if(btnIdx==4)
			{
			doSave();
			}
		else if(btnIdx==5)
			{
			//-- 提交
			doSign();
			}
		else if(btnIdx==6)
			{
				doSign();
			}
		else if(btnIdx==7)
			{
			var ntkoobj = document.getElementById("TANGER_OCX");
			ntkoobj.FullScreenMode=false;
			ntkoobj.close();
			window.parent.window.jBox.close();
			}
	}


	/**
	 * IE下点击按钮执行的操作
	 */
	document.write('<script language="javascript" for="TANGER_OCX" event="OnCustomToolBarCommand(btnIdx)">');
	document.write('if(btnIdx==0)');
	document.write('{');
	document.write('printVw();');
	document.write('}');
	document.write('else if(btnIdx==1)');
	document.write('{');
	document.write('printOt();');
	document.write('}');
	document.write('else if(btnIdx==2)');
	document.write('{');
	document.write('defineDocument();');
	document.write('}');
	document.write('else if(btnIdx==3)');
	document.write('{');
	document.write('cancelDocument();');
	document.write('}');
	document.write('else if(btnIdx==4)');
	document.write('{');
	document.write('doSave();');
	document.write('}');
	document.write('else if(btnIdx==5)');
	document.write('{');
	document.write('doSign();');
	document.write('}');
	document.write('else if(btnIdx==6)');
	document.write('{');
	document.write('doSign();');
	document.write('}');
	document.write('else if(btnIdx==7)');
	document.write('{');
	document.write('var ntkoobj = document.getElementById("TANGER_OCX");');
	document.write('ntkoobj.FullScreenMode=false;');
	document.write('ntkoobj.close();');
	document.write('window.parent.window.jBox.close();');
	document.write('}');
	document.write('</script>');




	/**
	 * firefox下文档打开后执行函数
	 * 
	 * @param str
	 * @param doc
	 */
	function OnComplete3(str, doc) {
		var ntkoobj = document.getElementById("TANGER_OCX");
		ntkoobj.FullScreenMode = true;
		var s = str.indexOf("readonly");
		if (s != -1) {
			ntkoobj.SetReadOnly(true, "123456");
		} else {
			ntkoobj.SetReadOnly(false, "123456");
		}
		
		initBookMarkValue(str);
		console.log(str);
		//alert(bookMarkIsExists("不予受理告知单当前日期"));
	}
	
	/**
	 * IE下文档打开后执行函数
	 */
	document.write('<script language="javascript" for="TANGER_OCX" event="Ondocumentopened(str, doc)">');
	document.write('var ntkoobj = document.getElementById("TANGER_OCX");');
	document.write('ntkoobj.FullScreenMode=true;');
	document.write('var s = str.indexOf("readonly");');
	document.write('if(s!=-1)');
	document.write('{');
	document.write('ntkoobj.SetReadOnly(true,"123456");');
	document.write('}');
	document.write('else');
	document.write('{');
	document.write('ntkoobj.SetReadOnly(false,"123456");');
	document.write('}');
	document.write('initBookMarkValue(str);');
	document.write('</script>');



	/**
	 * 取出url中指定参数的值
	 * @param name
	 * @returns
	 */
	function getUrlParam(url,name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
	    var r = url.substr(1).match(reg);
	    if (r != null) return unescape(r[2]); return null;
	}

    /**
     * 判断书签是否存在
     * 
     * @param bookMarkName
     * @returns
     */
    function bookMarkIsExists(bookMarkName){
    	
    	var ntkoobj = document.getElementById("TANGER_OCX");
    	return ntkoobj.ActiveDocument.Bookmarks.Exists(bookMarkName);
    }
    
    /**
     * 是否签章页
     * 
     * @param url
     * @returns
     */
    function isSignView(url){
    	
    	return getUrlParam(url,'signView')=='signView'?true:false;
    }
    

    /**
     * 初始化书签值(需要签章的文件已初始化过不需要在签章页再次初始化)  
     * 			
     *    				  需要签章的文件暂为:交办(非交办)办理情况报告和答复意见书	
     * @param url 
     */
    function initBookMarkValue(url){
    	
    	var ntkoobj = document.getElementById("TANGER_OCX");
    	
    	/*****受理告知单start***/
    	if(bookMarkIsExists("受理告知单信访人姓名")&&getUrlParam(url,'realName')!=""){
    	   ntkoobj.SetBookmarkValue("受理告知单信访人姓名",getUrlParam(url,'realName'));
    	 }
    		
    	if(bookMarkIsExists("受理告知单接收日期")&&getUrlParam(url,'receiveDate')!=""){
    	   ntkoobj.SetBookmarkValue("受理告知单接收日期",getUrlParam(url,'receiveDate'));
    	 } 
    	
    	if(bookMarkIsExists("受理告知单当前日期")&&getUrlParam(url,'currentDate')!=""){
     	   ntkoobj.SetBookmarkValue("受理告知单当前日期",getUrlParam(url,'currentDate'));
     	 } 
    	/*****受理告知单End***/
    	
    	
    	/*****不再受理告知单start***/
    	if(bookMarkIsExists("不再受理告知单信访人姓名")&&getUrlParam(url,'realName')!=""){
     	   ntkoobj.SetBookmarkValue("不再受理告知单信访人姓名",getUrlParam(url,'realName'));
     	 }
     		
     	if(bookMarkIsExists("不再受理告知单接收日期")&&getUrlParam(url,'receiveDate')!=""){
     	   ntkoobj.SetBookmarkValue("不再受理告知单接收日期",getUrlParam(url,'receiveDate'));
     	 } 
     	
     	if(bookMarkIsExists("不再受理告知单当前日期")&&getUrlParam(url,'currentDate')!=""){
      	   ntkoobj.SetBookmarkValue("不再受理告知单当前日期",getUrlParam(url,'currentDate'));
      	 } 
     	/*****不再受理告知单End***/
     	
     	
     	/*****不予受理告知单start***/
    	if(bookMarkIsExists("不予受理告知单信访人姓名")&&getUrlParam(url,'realName')!=""){
     	   ntkoobj.SetBookmarkValue("不予受理告知单信访人姓名",getUrlParam(url,'realName'));
     	 }
     		
     	if(bookMarkIsExists("不予受理告知单接收日期")&&getUrlParam(url,'receiveDate')!=""){
     	   ntkoobj.SetBookmarkValue("不予受理告知单接收日期",getUrlParam(url,'receiveDate'));
     	 } 
     	
     	if(bookMarkIsExists("不予受理告知单当前日期")&&getUrlParam(url,'currentDate')!=""){
      	   ntkoobj.SetBookmarkValue("不予受理告知单当前日期",getUrlParam(url,'currentDate'));
      	 } 
     	/*****不予受理告知单End***/
     	
     	
    	/*****交办办理情况报告start***/
    	if(!isSignView(url)){ //需要签章的文件已初始化过不需要在签章页再次初始化
    		
    		if(bookMarkIsExists("交办办理情况报告信访人姓名")&&getUrlParam(url,'realName')!=""){
    	       ntkoobj.SetBookmarkValue("交办办理情况报告信访人姓名",getUrlParam(url,'realName'));
    	     }
    		
    		if(bookMarkIsExists("交办办理情况报告信访人年龄")&&getUrlParam(url,'age')!=""){
     	       ntkoobj.SetBookmarkValue("交办办理情况报告信访人年龄",getUrlParam(url,'age'));
     	     }
    		
    		if(bookMarkIsExists("交办办理情况报告信访人性别")&&getUrlParam(url,'gender')!=""){
      	       ntkoobj.SetBookmarkValue("交办办理情况报告信访人性别",getUrlParam(url,'gender'));
      	     }
    	    	
    	    if(bookMarkIsExists("交办办理情况报告当前日期")&&getUrlParam(url,'currentDate')!=""){
    	       ntkoobj.SetBookmarkValue("交办办理情况报告当前日期",getUrlParam(url,'currentDate'));
    	     } 
    	}
    	/*****交办办理情况报告End***/
    	
    	
    	/*****非交办办理情况报告start***/
    	if(!isSignView(url)){ //需要签章的文件已初始化过不需要在签章页再次初始化
    		
    		if(bookMarkIsExists("非交办办理情况报告信访人姓名")&&getUrlParam(url,'realName')!=""){
    	       ntkoobj.SetBookmarkValue("非交办办理情况报告信访人姓名",getUrlParam(url,'realName'));
    	     }
    	    	
    	    if(bookMarkIsExists("非交办办理情况报告当前日期")&&getUrlParam(url,'currentDate')!=""){
    	       ntkoobj.SetBookmarkValue("非交办办理情况报告当前日期",getUrlParam(url,'currentDate'));
    	     } 
    	}
    	/*****非交办办理情况报告End***/
     	
     	
    	/*****答复意见书start***/
     	if(!isSignView(url)){ //需要签章的文件已初始化过不需要在签章页再次初始化
     		
     		if(bookMarkIsExists("答复意见书信访人姓名")&&getUrlParam(url,'realName')!=""){
          	   ntkoobj.SetBookmarkValue("答复意见书信访人姓名",getUrlParam(url,'realName'));
          	 }
          		
          	if(bookMarkIsExists("答复意见书当前日期")&&getUrlParam(url,'currentDate')!=""){
          	   ntkoobj.SetBookmarkValue("答复意见书当前日期",getUrlParam(url,'currentDate'));
          	 } 
     	}
     	/*****答复意见书End***/
     	
     	
    	/*****延期告知单start***/
    	if(bookMarkIsExists("延期告知单信访人姓名")&&getUrlParam(url,'realName')!=""){
     	   ntkoobj.SetBookmarkValue("延期告知单信访人姓名",getUrlParam(url,'realName'));
     	 }
     		
     	if(bookMarkIsExists("延期告知单接收日期")&&getUrlParam(url,'receiveDate')!=""){
     	   ntkoobj.SetBookmarkValue("延期告知单接收日期",getUrlParam(url,'receiveDate'));
     	 } 
     	
     	if(bookMarkIsExists("延期告知单当前日期")&&getUrlParam(url,'currentDate')!=""){
      	   ntkoobj.SetBookmarkValue("延期告知单当前日期",getUrlParam(url,'currentDate'));
      	 } 
     	/*****延期告知单End***/
    	
    	
    }
    
    
   /**
    * 以异步方式打开文档
    * 
    * @param bookMarkDatas  书签需要的数据
    * @param filepath		待打开的文件路径
    * @param isFinalized	是否定稿  0-未定稿 1-已定稿
    * @param readonly		有此参数文件则为只读
    * @param signView		有此参数则为签章页
    */ 
   function openfile(bookMarkDatas,filepath,isFinalized,readonly,signView){
	   
		var docURL;
		var ntkoobj = document.getElementById("TANGER_OCX");
		ntkoobj.FullScreenMode=true;
		var staticPath=$('#staticPath').val();
		ntkoobj.AddDocTypePlugin(".pdf","PDF.NtkoDocument","4,0,0,1",staticPath+"/ntkooledocall.cab",51,true);
		ntkoobj.AddCustomToolButton("打印预览",10);   	    //index 0
		ntkoobj.AddCustomToolButton("打印", 9);				//index 1
		ntkoobj.AddCustomToolButton("定稿", 14);				//index 2
		ntkoobj.AddCustomToolButton("解除定稿", 14);			//index 3
		ntkoobj.AddCustomToolButton("保存", 5);				//index 4
		ntkoobj.AddCustomToolButton("保存", 5);//生成报告		//index 5
		ntkoobj.AddCustomToolButton("提交", 11);				//index 6
		ntkoobj.AddCustomToolButton("关闭", 13);				//index 7
		ntkoobj.CustomToolBar=true;	
	
	 //控制显隐
	 if(isFinalized==0){ //当前状态为未定稿 此操作应为定稿  隐藏 解除定稿按钮
	 	ntkoobj.SetCustomToolButtonStatus(3,false,false);  
	 }else{
	 	ntkoobj.SetCustomToolButtonStatus(2,false,false);  
	 }
	
	 var getTimestamp=new Date().getTime();
	 var pathName=window.document.location.pathname;
     var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    
	 //定义查询参数
	 var queryParams = '?timestamp='+getTimestamp+'&realName='+bookMarkDatas.realName+'&receiveDate='+bookMarkDatas.receiveDate+'&currentDate='+bookMarkDatas.currentDate+'&age='+bookMarkDatas.age+'&gender='+bookMarkDatas.gender;
	 
	 //是否只读
	 if(readonly!= null && readonly!=""){
		 queryParams += '&readonly='+readonly;
	 }
	 
	 //是否签章页
	 if(signView!= null && signView!=""){
		 queryParams += '&signView='+signView;
	 }
	 
	 docURL = projectName +'/' + filepath + queryParams;
	 
	if(ntkoobj==null)
	{
		alert("NTKO OFFICE文档控件未能正确装载。请在本页面装载时窗口上方出现的提示条上点击安装ActiveX控件，并在随后出现的安装ActiveX的对话框中选择'是'。"); 
		return;
	}
	 if( (null == docURL) || (0 == docURL.length) )
		 {ntkoobj .CreateNew("Word.Document");} 
	 else
		{	
		 	var extStart=filepath.lastIndexOf(".");
		 	var ext=filepath.substring(extStart,filepath.length).toUpperCase();
		 	if(ext==".BMP"||ext==".PNG"||ext==".GIF"||ext==".JPG"||ext==".JPEG"){
		 	$("#ntko").hide();
		 	$(document.body).append("<div align='center'><img src ="+docURL+"/></div>");
	 	}else if(ext==".DOC"||ext==".DOCX"||ext==".XLS"||ext==".XLSX"||ext==".PPT"||ext==".PPTX"||ext==".PDF")
		  {
		  	ntkoobj.BeginOpenFromURL(docURL);
		  	$("#ntko").show();
		  }
	  else
		  {
		  $("#ntko").hide();
		  top.$.jBox.tip("此格式文件无法打开");	  	
		  }		 	
	}
}
	
	
	// 定稿
	function defineDocument(){
		var ntkoobj = document.getElementById("TANGER_OCX");
		var caseId=$('#caseId').val();
		var filepath=$("#filepath").val();
		var isFinalized = $("#isFinalized").val();
		var val =null;
		ntkoobj.SetReadOnly(true,"123456");
		ntkoobj.SetCustomToolButtonStatus(3,true,true);   //显示解除定稿
		ntkoobj.SetCustomToolButtonStatus(2,false,false);   //隐藏定稿
		val=ntkoobj.SaveToURL("CopyFile?caseId="+caseId+"&filepath="+filepath+"&isFinalized=defineDocument","NTKOfile","","",0);
	}

	// 解除定稿
	function cancelDocument(){
		var ntkoobj = document.getElementById("TANGER_OCX");
		var caseId=$('#caseId').val();
		var filepath=$("#filepath").val();
		var isFinalized = $("#isFinalized").val();
		var val =null;
		ntkoobj.SetReadOnly(false,"123456"); 
		ntkoobj.SetCustomToolButtonStatus(2,true,true);  //显示定稿
		ntkoobj.SetCustomToolButtonStatus(3,false,false);   //隐藏解除定稿
		val=ntkoobj.SaveToURL("CopyFile?caseId="+caseId+"&filepath="+filepath+"&isFinalized=cancelDocument","NTKOfile","","",0);
	}

	
	function ok(){
		var val =$("#isok").val();
		if(val=="定稿")
			{
				var ntkoobj = document.getElementById("TANGER_OCX");
				ntkoobj.SetReadOnly(true,"123456");
				$('#ok').val("1");
				$("#isok").val("草稿");
				$("#isok").removeAttr("btn btn-inverse");
				$("#isok").attr("class","btn btn-primary");
			}
		else
			{
				var ntkoobj = document.getElementById("TANGER_OCX");
				ntkoobj.SetReadOnly(false,"123456");
				$('#ok').val("0");
				$("#isok").val("定稿");
				$("#isok").removeAttr("btn btn-primary");
				$("#isok").attr('class',"btn btn-inverse");
			}		
	}
	
	
function offReadOnly(){
	var ntkoobj = document.getElementById("TANGER_OCX");
	ntkoobj.SetReadOnly(false,"123456");
} 

function Sign(id,menuID){
	
	var ntkoobj = document.getElementById("TANGER_OCX");
	var caseId=$('#caseId').val();
	var filepath=$("#filepath").val();
	var val =null;
	if(menuID==null)
		{
			val=ntkoobj.SaveToURL("CopyFile?caseId="+caseId+"&filepath="+filepath,"NTKOfile","","",0);
		}
	else
		{
			val=ntkoobj.SaveToURL("CopyFile?caseId="+caseId+"&filepath="+filepath+"&menuID="+menuID,"NTKOfile","","",0);
		}
	
	if(browser=="IE")
	{
		if(val=='签章完成')
		{
			$("#"+id,parent.document).val("签章完成");
			$("#"+id,parent.document).attr("disabled","disabled");
			ntkoobj.FullScreenMode=false;
			ntkoobj.close();
			window.parent.window.jBox.close();	
		}else if(val=='仅保存'){
			ntkoobj.FullScreenMode=false;
			ntkoobj.close();
			window.parent.window.jBox.close();	
		}
		
	}
}



function CreatRpt(){
	var reFileName=$("#reFileName").val();
	var ntkoobj = document.getElementById("TANGER_OCX");
	var name=ntkoobj.SaveToURL("CopyFile?menuID=2","NTKOfile","",reFileName,0);
	if(browser=="IE")
	{
		var aId=$("#modelFileName",parent.document).val();
		$("#"+aId,parent.document).text(name);
		ntkoobj.FullScreenMode=false;
		ntkoobj.close();
		window.parent.window.jBox.close();	
	}
}

function print(){//打印
	var ntkoobj = document.getElementById("TANGER_OCX");
	ntkoobj.PrintOut([true]);
}

function printV(){//打印预览
	var ntkoobj = document.getElementById("TANGER_OCX");
	ntkoobj.PrintPreview();
}

function CustomMenuCmd(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID){
	if(menuID==1)
	{
		var ntkoobj = document.getElementById("TANGER_OCX");
		ntkoobj.FullScreenMode=false;
		$("#ntko").hide();
	}
	else if(menuID==0)
	{
		var caseId=$('#caseId').val();
		var filepath=$("#filepath").val();
		var ntkoobj = document.getElementById("TANGER_OCX");
		ntkoobj.SaveToURL("CopyFile?caseId="+caseId+"&filepath="+filepath,"NTKOfile","","",0);	
	}
	else if(menuID==2)
		{
		var reFileName=$("#reFileName").val();
		var ntkoobj = document.getElementById("TANGER_OCX");
		ntkoobj.SaveToURL("CopyFile?menuID="+menuID,"NTKOfile","",reFileName,0);
		}
}

//var classid = 'C9BC4DFF-4248-4a3c-8A49-63A7D317F404';
if (browser=="IE"){
	document.write('<!-- 用来产生编辑状态的ActiveX控件的JS脚本-->   ');
	document.write('<!-- 因为微软的ActiveX新机制，需要一个外部引入的js-->   ');
	document.write('<object id="TANGER_OCX" classid="clsid:'+clsid+'"');
	document.write('codebase="'+path+'#version=5,0,2,5" width="100%" height="'+height+'px"');
	document.write('<param name="IsUseUTF8URL" value="-1">   ');
	document.write('<param name="IsUseUTF8Data" value="-1">   ');
	document.write('<param name="BorderStyle" value="1">   ');
	document.write('<param name="BorderColor" value="14402205">   ');
	document.write('<param name="TitlebarColor" value="15658734">   ');
	document.write('<param name="isoptforopenspeed" value="0">   ');
	document.write('<param name="TitlebarTextColor" value="0">   ');
	document.write('<param name="FileNew" value="0" />');
	document.write('<param name="FileClose" value="0" />');
	document.write('<param name="FileOpen" value="0" />');
	document.write('<param name="FileSave" value="0" />');
	document.write('<param name="FileSaveAs" value="0" />');
	document.write('<param name="Titlebar" value="0"> ');
	document.write('<param name="MenubarColor" value="14402205">   ');
	document.write('<param name="MenuButtonColor" VALUE="16180947">   ');
	document.write('<param name="MenuBarStyle" value="3">   ');
	document.write('<param name="MenuButtonStyle" value="7">   ');
	document.write('<param name="WebUserName" value="NTKO">   ');
	document.write('<SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>   ');
	document.write('</object>');
}
else if (browser=="firefox"){ 	
		document.write('<object id="TANGER_OCX" type="application/ntko-plug"  codebase="'+path+'#version=5,0,2,5" width="100%" height="'+height+'px" ForOnSaveToURL="OnComplete2" ForOnBeginOpenFromURL="OnComplete" ForOndocumentopened="OnComplete3" ForOnCustomToolBarCommand="OnComplete4"');
		document.write('ForOnpublishAshtmltourl="publishashtml"');
		document.write('ForOnpublishAspdftourl="publishaspdf"');
		document.write('ForOnSaveAsOtherFormatToUrl="saveasotherurl"');
		document.write('ForOnAddTemplateFromURL="addtemplatefromurl"');
		document.write('ForOnDoWebGet="dowebget"');
		document.write('ForOnDoWebExecute="webExecute"');
		document.write('ForOnDoWebExecute2="webExecute2"');
		document.write('ForOnFileCommand="FileCommand"');
		document.write('ForOnCustomMenuCmd2="CustomMenuCmd"');
		document.write('ForOnCustomFileMenuCmd="CustomFileMenuCmd"');
		document.write('_FileNew="0" ');
		document.write('_FileClose="0" ');
		document.write('_FileOpen="0" ');
		document.write('_FileSave="0" ');
		document.write('_FileSaveAs="0" ');
		document.write('_Titlebar ="0" ');
		document.write('_IsUseUTF8URL="-1"   ');
		document.write('_IsUseUTF8Data="-1"   ');
		document.write('_BorderStyle="1"   ');
		document.write('_BorderColor="14402205"   ');
		document.write('_MenubarColor="14402205"   ');
		document.write('_MenuButtonColor="16180947"   ');
		document.write('_MenuBarStyle="3"  ');
		document.write('_MenuButtonStyle="7"   ');
		document.write('_WebUserName="NTKO"   ');
		document.write('clsid="{'+clsid+'}" >');
		document.write('<SPAN STYLE="color:red">尚未安装NTKO Web FireFox跨浏览器插件。请点击<a href="'+staticPath+'/NtkoCrossBrowserSetup20150430.msi">安装组件</a></SPAN>   ');
		document.write('</object>');
}else if(browser=="chrome"){
		document.write('<object id="TANGER_OCX" clsid="{'+clsid+'}"  ForOnSaveToURL="OnComplete2" ForOnBeginOpenFromURL="OnComplete" ForOndocumentopened="OnComplete3"  ForOnCustomToolBarCommand="OnComplete4"');
		document.write('ForOnpublishAshtmltourl="publishashtml"');
		document.write('ForOnpublishAspdftourl="publishaspdf"');
		document.write('ForOnAddTemplateFromURL="addtemplatefromurl"');
		document.write('ForOnSaveAsOtherFormatToUrl="saveasotherurl"');
		document.write('ForOnDoWebGet="dowebget"');
		document.write('ForOnDoWebExecute="webExecute"');
		document.write('ForOnDoWebExecute2="webExecute2"');
		document.write('ForOnFileCommand="FileCommand"');
		document.write('ForOnCustomMenuCmd2="CustomMenuCmd"');
		document.write('ForOnCustomFileMenuCmd="CustomFileMenuCmd"');
		document.write('codebase="'+path+'#version=5,0,2,5" width="100%" height="'+height+'px" type="application/ntko-plug" ');
		document.write('_FileNew="0" ');
		document.write('_FileClose="0" ');
		document.write('_FileOpen="0" ');
		document.write('_FileSave="0" ');
		document.write('_FileSaveAs="0" ');
		document.write('_Titlebar ="0" ');
		document.write('_IsUseUTF8URL="-1"   ');
		document.write('_IsUseUTF8Data="-1"   ');
		document.write('_BorderStyle="1"   ');
		document.write('_BorderColor="14402205"   ');
		document.write('_MenubarColor="14402205"   ');
		document.write('_MenuButtonColor="16180947"   ');
		document.write('_MenuBarStyle="3"  ');
		document.write('_MenuButtonStyle="7"');
		document.write('_WebUserName="NTKO"  ');
		document.write('<SPAN STYLE="color:red">尚未安装NTKO Web Chrome跨浏览器插件。请点击<a href="'+staticPath+'/NtkoCrossBrowserSetup20150430.msi">安装组件</a></SPAN>   ');
		document.write('</object>');
	}else if (Sys.opera){
		document.getElementById("TANGER_OCX").ShowTipMessage("提示","sorry,ntko web印章暂时不支持opera!");
	}else if (Sys.safari){
		 document.getElementById("TANGER_OCX").ShowTipMessage("提示","sorry,ntko web印章暂时不支持safari!");
	}


//--  IE事件
		document.write('<script language="javascript" for="TANGER_OCX" event="OnCustomMenuCmd2(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID)">');
		document.write('if(menuID==0)');
		document.write('{');
		document.write('var buttonId = $("#checkbutton").val();');
		document.write('var caseId=$("#caseId").val();');
		document.write('var filepath=$("#filepath").val();');
		document.write('var ntkoobj = document.getElementById("TANGER_OCX");');
		document.write('ntkoobj.SaveToURL("CopyFile?caseId="+caseId+"&filepath="+filepath,"NTKOfile","","",0);');
		document.write('$("#"+buttonId).val("签章完成");');
		document.write('$("#"+buttonId).attr("disabled","disabled");');
		document.write('ntkoobj.FullScreenMode=false;');
		document.write('$("#ntko").hide();'); 
		document.write('}');
		document.write('if(menuID==1)');
		document.write('{');
		document.write('var ntkoobj = document.getElementById("TANGER_OCX");');
		document.write('ntkoobj.FullScreenMode=false;');
		document.write('$("#ntko").hide();');
		document.write('}');
		document.write('if(menuID==2)');
		document.write('{');
		document.write('var reFileName=$("#reFileName").val();');
		document.write('var ntkoobj = document.getElementById("TANGER_OCX");');
		document.write('var name=ntkoobj.SaveToURL("CopyFile?menuID="+menuID,"NTKOfile","",reFileName,0);');
		document.write('var aId=$("#modelFileName").val();'); 
		document.write('$("#"+aId).text(name);'); 
		document.write('ntkoobj.FullScreenMode=false;');
		document.write('$("#ntko").hide();'); 
		document.write('}');
		document.write('</script>');
		
		
		
		
	
	
	