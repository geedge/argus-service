<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>待办事项</title>
	<script type="text/javascript">
		
		$(document).ready(function() {
			
			
		   //参数设置，若用默认值可以省略以下面代
		   toastr.options = {
				"closeButton": true, //是否显示关闭按钮
				"debug": false, //是否使用debug模式
				"progressBar": false,
				"positionClass": "toast-bottom-right",//弹出窗的位置
				"showDuration": "300",//显示的动画时间
				"hideDuration": "1000",//消失的动画时间
				"timeOut": "300000", //展现时间
				"extendedTimeOut": "1000",//加长展示时间
				"showEasing": "swing",//显示时的动画缓冲方式
				"hideEasing": "linear",//消失时的动画缓冲方式
				"showMethod": "fadeIn",//显示时的动画方式
				"hideMethod": "fadeOut" //消失时的动画方式
			};	

			 
			ajaxToastr();
			
			setInterval(function(){
				ajaxToastr();
			},300000);
			
			
			function ajaxToastr() {
				
				toastr.remove();
				
				$.ajax({
					type:'post',
					url:'${ctx}/sys/user/ajaxToastr',
					async: false,
					dataType:'text',
					success:function(data){
						if(!(data==null || data=="")) {
							toastr.info(data,"提醒");
						}
						
					}
				});
			}
			
			
			$(".tabbable ul li a").click(function(){
				var todo = $(this).attr("todo");
				var caseIds = $(this).attr("caseIds");
				var src= "";
				if(todo=="待提交") {
					src = "${ctx}/letter/reg/list";
				}else if(todo=="待处理") {
					src = "${ctx}/letter/process/acceptList";
				}else if(todo=="待审批") {
					src = "${ctx}/letter/process/approvalList";
				}else if(todo=="待办理") {
					src = "${ctx}/letter/process/workList";
				}else if(todo=="待办结审核") {
					src = "${ctx}/letter/process/completeApprovalList";
				}
				
				$("#todoContentFrame").attr("src",src+"?caseIds="+caseIds);
				
			});
			
			
			//默认选中第一个tab
			$(".tabbable ul li a:first").click();
			

		})
		
		
	 var browserVersion = window.navigator.userAgent.toUpperCase();
	 var isOpera = false;
	 var isFireFox = false;
	 var isChrome = false;
	 var isSafari = false;
	 var isIE = false;
	 var iframeTime;
	 function reinitIframe(iframeId, minHeight) {
	     try {
	         var iframe = document.getElementById(iframeId);
	         var bHeight = 0;
	         if (isChrome == false && isSafari == false)
	             bHeight = iframe.contentWindow.document.body.scrollHeight;

	         var dHeight = 0;
	         if (isFireFox == true)
	             dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2;
	         else if (isIE == false && isOpera == false)
	             dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
	         else
	             bHeight += 3;
	         var height = Math.max(bHeight, dHeight);
	         if (height < minHeight) height = minHeight;
	         iframe.style.height = height + "px";
	     } catch (ex) { }
	 }
	 function startInit(iframeId, minHeight) {
	     isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
	     isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
	     isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
	     isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
	     if (!!window.ActiveXObject || "ActiveXObject" in window)
	         isIE = true;
	     reinitIframe(iframeId, minHeight);
	     if (iframeTime != null)
	         clearInterval(iframeTime)
	     iframeTime = window.setInterval("reinitIframe('" + iframeId + "'," + minHeight + ")", 100);
	 } 
	 
	 startInit('todoContentFrame', 560);

	</script>
	
	<style type="text/css">
		/*tab项覆盖样式*/
		.nav-tabs > li.active {
		    border-top: 0px;
		   
		}
		
		.nav-tabs > li > a:hover, .nav-tabs > li > a:focus {
	   		border:0px;
		}
		
		.nav-tabs > .active > a,
		.nav-tabs > .active > a:hover,
		.nav-tabs > .active > a:focus {
		  color: #fff;
		  cursor: default;
		  background-color: #27A9E3;
		  border-bottom-color: transparent;
		}
	
	</style>
</head>
<body>

	
	<sys:message content="${message}"/>
	
	<!--BEGIN TABS-->

	<div class="tabbable tabbable-custom">
		
		
		<c:choose>
			<c:when test="${fn:length(todoMap)>0}">
				
				<ul class="nav nav-tabs">
			
					<c:forEach items="${todoMap}" var="todoMap">
						<li><a href="#tab_${todoMap.key }" todo="${todoMap.key }" 
						caseIds="${todoMap.value }" data-toggle="tab">${todoMap.key }(${fn:length(fn:split(todoMap.value,','))})</a></li>
					</c:forEach>
		
				</ul>
		
				<div class="tab-content">
					<iframe id="todoContentFrame" name="todoContentFrame" scrolling="no" frameborder="no" width="100%" height="900px">
				</div>
				
			</c:when>
			<c:otherwise>
				&nbsp;&nbsp;暂无待办事项
			</c:otherwise>
		</c:choose>
		
		
		

	</div>

	<!--END TABS-->

	
	

</body>
</html>