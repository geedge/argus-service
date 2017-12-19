<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<c:set var="tabmode" value="${empty cookie.tabmode.value ? '1' : cookie.tabmode.value}"/>
<c:set var="workNotifyRemindInterval" value="${fns:getStringProperty('work.notify.remind.interval','')}"/>
<c:if test="${tabmode eq '1'}">
  <link rel="Stylesheet" href="${pageContext.request.contextPath}/static/jerichotab/css/jquery.jerichotab.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/jerichotab/js/jquery.jerichotab.js"></script>
</c:if>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/mvc_frame.ico"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${fns:getStringProperty('productName','NIS')}</title>

<style type="text/css">
	#main {padding:0;margin:0;} 
	#main .container-fluid{padding:0 4px 0 4px;}
	#header {margin:0 0 2px;position:static;} 
	#header li {font-size:14px;_font-size:13px;}
	#header .brand {font-family:"Microsoft Yahei","微软雅黑",Helvetica, Arial, sans-serif, "宋体";font-size:26px;padding-left:33px;}
	#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
	#footer, #footer a {color:#999;} #left{overflow-x:hidden;overflow-y:auto;} 
	#left .collapse{position:static;}
	
	.accordion-heading .accordion-toggle .icon-chevron-left{
		margin-right:3px;
		display:inline;
		font-weight:300;
		float:right;
		display:inline;
		padding-top:2px;
		
	}
	
	.accordion-heading .accordion-toggle .icon-chevron-down{
		height:auto;
		margin-right:3px;
		display:inline;
		font-weight:300;
		float:right;
		display:inline;
		padding-top:2px;
	}
	#userControl {
		background-image: url(${ctxStatic}/images/bs_bannerright.jpg);
	    background-repeat: no-repeat;
		
	}
	#userControl>li>a{/*color:#fff;*/text-shadow:none;} 
	#userControl>li>a:hover, #user #userControl>li.open>a{background:transparent;}
	
	
	
	.header-left {
	  display: block;
  	  float: left;
  	  margin-left: 1px;
	  text-shadow: 0 1px 0 #ffffff;
	  width: 608px;
	  height:87px;
	  background-image: url(${ctxStatic}/images/bs_bannerleft.jpg);
	  background-repeat: no-repeat;
	}
	
	.nav-collapse {
	  width:100%;
	  height:87px;
	  background-image: url(${ctxStatic}/images/bs_bannercenter.jpg);
	  background-repeat: repeat;
	}
	#content {
		background: none repeat scroll 0 0 #eeeeee;
		margin-right: 0;
		position: relative;
		min-height:100%;
		width: auto;
	}
	
		
</style>


</head>
<body>
	
        
<div id="main">
       	<!-- header -->
    <div id="header" class="navbar navbar-fixed-top">
        <div class="navbar-inner">
        	<div class="header-left">
        		
        	 
        	<%--  <span id="productName"> ${fns:getStringProperty('productName','NIS')}</span> --%>
        	
        	</div>
                <ul id="userControl" class="nav pull-right">
                    
	              	<li class="divider-vertical hidden-phone hidden-tablet"></li>
	              	
	              	<li><a href="${pageContext.request.contextPath}/"  title="访问网站主页"><i class="icon-home"></i></a></li>
	              	
	              	<li id="userInfo" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息">您好,${fns:getUser().name}&nbsp;<span id="notifyNum" class="label label-info hide"></span><b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="${ctx}/sys/user/info" target="mainFrame"><i class="icon-user"></i>&nbsp; 个人信息</a></li>
							<li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><i class="icon-lock"></i>&nbsp;  修改密码</a></li>
							<li><a href="${ctx}oa/oaNotify/self" target="mainFrame"><i class="icon-bell"></i>&nbsp;  我的通知 <span id="notifyNum2" class="label label-info hide"></span></a></li>
						</ul>
					 </li>
	              
	              	<li id="themeSwitch" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="主题切换"><i class="icon-th-large"></i>&nbsp;<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<c:forEach items="${fns:getDictList('SYS_THEME')}" var="dict">
								<li <c:if test="${cookie.theme.value eq dict.itemCode}">class='active'</c:if>>
									<a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.itemCode}?url='+location.href">${dict.itemValue}</a>
								</li>
							</c:forEach>
							
							<li><a href="javascript:cookie('tabmode','${tabmode eq '1' ? '0' : '1'}');location=location.href">${tabmode eq '1' ? '关闭' : '开启'}页签模式</a></li>
						</ul>
						<!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
					</li>
		
		
					<li class="divider-vertical hidden-phone hidden-tablet"></li>
					
					<li><a href="javascript:void(0);" class="tip-bottom" title="显示/隐藏快捷菜单"><i id="show-hide-option" class="icon-eye-open"></i></a></li>
					
					<li><a href="${pageContext.request.contextPath }/logout" title="退出登录"><i class="icon-signout"></i>&nbsp;退出</a></li>
					<li>&nbsp;</li>
                </ul>
                     
                     <div class="nav-collapse">
                     	<ul id="menu" class="nav" style="*white-space:nowrap;float:none;">
	                       <c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
	                       		
		                       	<c:if test="${menu.parent.id eq 1 && menu.isShow eq 1}">
							  		<li class="menu ${idxStatus.index eq 0 ? 'active' : ''}">
								  		<c:if test="${menu.href != null && menu.href != ''}" var="leafNodeFlag">
								  			 <a class="menu" href="${ctx}${menu.href}" data-id="${menu.id}" target="mainFrame"><i class="${menu.icon }  icon-white"></i> ${menu.name }</a>
								  		</c:if>
								  		
								  		<c:if test="${!leafNodeFlag }">
								  			<a class="menu" href="javascript:void(0);" data-href="" data-id="${menu.id}"><i class="${menu.icon } icon-white"></i> ${menu.name}</a>
								  		</c:if>
								  		
								  		
							  		</li>
							  	</c:if>
	  	
							  
	                       </c:forEach>
	                         
	                         <li>
	                             <a href="documentation.html"><i class="icon-book icon-white"></i> 帮助</a>
	                         </li>
	                     </ul>
                     
                   </div>
                     
             </div>
     </div>
     
      <!-- main content -->
     <div class="container-fluid">
        <div id="content" class="row-fluid">
        	<div id="left">
        		<!-- sidebar -->
                <c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
          			<c:if test="${not empty menu.children }">
          				 <div id="menu-${menu.id }" class="accordion">
          				 	
          				 	<c:forEach  items="${menu.children}" var="secondMenu">
          				 			<div class="accordion-group">
          				 				<div class="accordion-heading">
				                            <a href="#collapse_${secondMenu.id}" data-parent="#menu-${menu.id }" data-toggle="collapse" class="accordion-toggle">
				                                ${secondMenu.name} <i class="icon-chevron-left"></i>
				                            </a>
			                      		</div>
			                      		
			                      		<div class="accordion-body collapse" id="collapse_${secondMenu.id}">
				                            <div class="accordion-inner">
				                                <ul class="nav nav-list">
										 			 <c:forEach  items="${secondMenu.children}" var="thirdMenu">
													    <c:if test="${thirdMenu.href != null && thirdMenu.href != '' }">
													    	<li>
													    		<a href="${ctx}${thirdMenu.href}" data-id="${thirdMenu.id}" target="mainFrame"><i class="${not empty thirdMenu.icon?thirdMenu.icon:'icon-hide'} icon-white"></i> ${thirdMenu.name }</a>
													    		<ul style="margin:0;padding-right:0;" class="nav nav-list hide"></ul>
													    	</li>
													   		
													    </c:if>
													    <!-- 
													     <li class="divider"></li>
													     -->
										 			 </c:forEach>
				                                	</ul>
				                            	</div>
				                   		</div>
          				 			
          				 			</div>
          				 		
          				 	
          				 	
          				 	</c:forEach>
          				 	
          						
          				 	</div>
          				</c:if>
          			</c:forEach>
    			</div>
    			
        		<div id="openClose" class="close">&nbsp;</div>
        		
        		<div id="right">
        		 <div class="quick-actions_homepage">
			     	<div id="quickMenu">
			     		<ul class="quick-actions">
			     		
			     			<!-- <li class="bg_lb"> <a  href="javascript:void(0);"  onclick="go_home()"> <i class="icon-check"></i> 待办事项</a> </li> -->
			     		
			     			<c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
			          			<c:if test="${not empty menu.children }">
			          				 <c:forEach  items="${menu.children}" var="secondMenu">
			          				 	
			          				 	<c:if test="${not empty secondMenu.href and secondMenu.quickAction ne 0}">
								  			<c:if test="${secondMenu.quickAction==1}">
								  				<li class="${secondMenu.menuBg }"> 
									  				<a  href="javascript:void(0);"  onclick="page_turn('${menu.id}'+'_'+'${secondMenu.id}')"> <i class="${secondMenu.icon }"></i> ${secondMenu.name } 
									  					<c:if test="${secondMenu.name eq '待办事项'}"><span id="notifyWorkedOption" class="label label-important hide"></span></c:if>
									  				</a> 
								  				</li>
								  			</c:if>
								  			
								  			<c:if test="${secondMenu.quickAction==2}">
								  				<li class="${secondMenu.menuBg } span3"> <a  href="javascript:void(0);"  onclick="page_turn('${menu.id}'+'_'+'${secondMenu.id}')"> 
								  				<i class="${secondMenu.icon }"></i> ${secondMenu.name } <c:if test="${secondMenu.name eq '待办事项'}"><span id="notifyWorkedOption" class="label label-important hide"></span></c:if></a> </li>
								  			</c:if>
								  		</c:if>		
													 		
								 		<c:forEach  items="${secondMenu.children}" var="thirdMenu">
											  <c:if test="${not empty thirdMenu.href and thirdMenu.quickAction ne 0}">
										  			<c:if test="${thirdMenu.quickAction==1}">
										  				<li class="${thirdMenu.menuBg }"> 
										  					<a  href="javascript:void(0);"  onclick="page_turn('${menu.id}'+'_'+'${secondMenu.id}'+'_'+'${thirdMenu.id}')"> <i class="${thirdMenu.icon }"></i> ${thirdMenu.name } 
										  						<c:if test="${thirdMenu.name eq '待办事项'}"><span id="notifyWorkedOption" class="label label-important hide"></span></c:if>
										  					</a> 
										  				</li>
										  			</c:if>
										  			
										  			<c:if test="${thirdMenu.quickAction==2}">
										  				<li class="${thirdMenu.menuBg } span3"> 
											  				<a  href="javascript:void(0);"  onclick="page_turn('${menu.id}'+'_'+'${secondMenu.id}'+'_'+'${thirdMenu.id}')"> <i class="${thirdMenu.icon }"></i> ${thirdMenu.name } 
											  					<c:if test="${thirdMenu.name eq '待办事项'}"><span id="notifyWorkedOption" class="label label-important hide"></span></c:if>
											  				</a> 
										  				</li>
										  			</c:if>
										  		</c:if>		
										   
							 			 </c:forEach>
												 			 
												 			 
						                                	
		          				 	</c:forEach>
		          				</c:if>
          					</c:forEach>
			     		
			     			  
			     			  
			     		</ul>
			     	</div>
			     </div>
	              <iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%">
	               	浏览器不支持嵌入式框架或配置为不显示嵌入式框架。
	               </iframe>
          		</div>
          	
         </div>
         
         <div id="footer" class="row-fluid">
        	 Copyright &copy; 2015-${fns:getStringProperty('copyrightYear','2015')} ${fns:getStringProperty('productName','NIS')} - Powered By <a href="${pageContext.request.contextPath }" target="_blank">NIS</a> ${fns:getStringProperty('version','1.0')}
		 </div>
          
      </div>
       
</div>

<script type="text/javascript"> 
		
		$(document).ready(function(){
			App.init();
		});
		
		
		function page_turn(ids) {
			var ids = ids.split("_");
			$("#menu a.menu").filter("[data-id='"+ids[0]+"']").click();
			
			var $secondMenu = $("#left .accordion-heading a").filter("[href='#collapse_"+ids[1]+"']");
			 if (!$($secondMenu.attr("href")).hasClass('in')){
				 $secondMenu.click();
			  }
			
			var $thirdMenu = $($secondMenu.attr("href")).find(".accordion-inner > ul > li > a ").filter("[data-id='"+ids[2]+"']");
			$thirdMenu.children("i").click();
		}
		
		
		var App = function() {
			
			var leftWidth = 160; // 左侧窗口大小
			var tabTitleHeight = 33; // 页签的高度
			var htmlObj = $("html"), mainObj = $("#main");
			var headerObj = $("#header"), footerObj = $("#footer");
			var centerObj = $("#left, #openClose, #right");
			var frameObj = $("#right iframe");
			var quickObj = $("#right .quick-actions_homepage");
			
			var	handleResponsiveOnResize = function(){
					
					$("#left").width(leftWidth);
					
					$("#openClose").click(function(){
						if($(this).hasClass("close")){
							
							$(this).removeClass("close").addClass("open");
							
							$("#left").animate({width:0,opacity:"hide"});
							
							$("#right").animate({width:$("#content").width()-$("#openClose").width()-5},function(){
									if(typeof openCloseClickCallBack=="function"){
										openCloseClickCallBack(true);
									}
									wSize();
								}
							)
						} else {
							$(this).removeClass("open").addClass("close");
							
							$("#left").animate({width:leftWidth,opacity:"show"});
							
							$("#right").animate({width:$("#content").width()-$("#openClose").width()-leftWidth-9},function(){
									if(typeof openCloseClickCallBack=="function"){
										openCloseClickCallBack(true);
									}
									wSize();
								}
							);
						}
					});
					
					if(!Array.prototype.map){
						Array.prototype.map=function(e,d){
							var a=[],b=0;
							for(var c=0,f=this.length;c<f;c++){
								if(c in this){
									a[b++]=e.call(d,this[c],c,this)
								}
							}
							return a;
						}
					}
					
					$(window).resize(function(){wSize()});
					
					wSize();
				}
			
			
			<c:if test="${tabmode eq '1'}"> 
				var openCloseClickCallBack = function(b){
					$.fn.jerichoTab.resize();
				} 
			</c:if>
			
			
			var getWindowSize = function(){
				return["Height","Width"].map(function(a){
					return window["inner"+a]||document.compatMode==="CSS1Compat"&&document.documentElement["client"+a]||document.body["client"+a]});
			}
			
			//主界面自适应大小
			var wSize = function(){
				var minHeight = 500, minWidth = 980;
				var strs = getWindowSize().toString().split(",");
				
				htmlObj.css({"overflow-x":strs[1] < minWidth ? "auto" : "hidden", "overflow-y":strs[0] < minHeight ? "auto" : "hidden"});
				mainObj.css("width",strs[1] < minWidth ? minWidth - 10 : "auto");
				centerObj.height((strs[0] < minHeight ? minHeight : strs[0]) - headerObj.height() - footerObj.height() - (strs[1] < minWidth ? 42 : 28));
				
				$("#openClose").height($("#openClose").height() - 5);
				
				frameObj.height($("#right").height()-quickObj.height());
				
				<c:if test="${tabmode eq '1'}"> 
					$(".jericho_tab iframe").height(0).height($("#right").height() - tabTitleHeight - quickObj.height()); 
					$('#jerichotab_contentholder').height(0).height($("#right").height() - tabTitleHeight - quickObj.height());
				</c:if>
				
				wSizeWidth();
				
			}
			
			var wSizeWidth = function (){
				if (!$("#openClose").is(":hidden")){
					var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
					$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -5);
				}else{
					$("#right").width("100%");
				}
			}
			
			var sidebar_init = function() {
				menu_action_init();
				system_menu_default_page();
			}
			
			//系统进入默认首页界面
			var system_menu_default_page = function() {
				var $topMenuFirst = $("#menu a.menu:first i").click();
				var menuId = "#menu-" + $topMenuFirst.parent().attr("data-id");
				 // 初始化点击第一个二级菜单
				 if (!$(menuId + " .accordion-body:first").hasClass('in')){
					$(menuId + " .accordion-heading:first a").click();
				}
				if (!$(menuId + " .accordion-body li:first ul:first").is(":visible")){
					var iobject = $(menuId + " .accordion-body a:first i");
					var aObject = $(menuId + " .accordion-body li:first a");
					$(menuId + " .accordion-body a:first i").click();
				} 
				// 初始化点击第一个三级菜单
				$(menuId + " .accordion-body li:first li:first a:first").click();
				
			}
 			
			
			
			//菜单选择触发事件
			var menu_action_init = function() {
				
				// 绑定菜单单击事件
				$("#menu a.menu").click(function(){
					// 一级菜单焦点
					$("#menu li.menu").removeClass("active");
					
					$(this).parent().addClass("active");
					
					// 左侧区域隐藏
					if ($(this).attr("target") == "mainFrame"){
						$("#left,#openClose").hide();
						wSizeWidth();
						<c:if test="${tabmode eq '1'}"> // 隐藏页签
							$(".jericho_tab").hide();
							$("#mainFrame").show();
						</c:if>
						return true;
					}
					
					// 左侧区域显示
					$("#left,#openClose").show();
					if(!$("#openClose").hasClass("close")){
						$("#openClose").click();
					}
					
					// 显示二级菜单
					var menuId = "#menu-" + $(this).attr("data-id");
					//默认加载所有菜单
					if ($(menuId).length){
						$("#left .accordion").hide();
						$(menuId).show();
					} else {
						
						// 获取二级菜单数据
						$.get($(this).attr("data-href"), function(data){
							if (data.indexOf("id=\"loginForm\"") != -1){
								alert('未登录或登录超时。请重新登录，谢谢！');
								top.location = "${pageContext.request.contextPath}/";
								return false;
							}
							
						});
						
					}
					
					// 大小宽度调整
					wSizeWidth();
					return false;
					
				});
				
				//快捷菜单显示隐藏动作
				$("#show-hide-option").click(function(event){
					
					var disp = $("#quickMenu").css("display");
					if(!disp || disp == "block"  ) {
						$("#quickMenu").slideUp(200,function(){
							$("#show-hide-option").removeClass().addClass("icon-eye-close");
							wSize();
						});
						
					}else {
						$("#quickMenu").slideDown(200,function(){
							$("#show-hide-option").removeClass().addClass("icon-eye-open");
							wSize();
						});
						
					}
					 event.preventDefault();
					
				});
				
				
				// 二级标题
				$("#left .accordion-heading a").click(function(){
					menu_header_active($(this));
				});
				
				// 二级内容
				$("#left .accordion-body a").click(function(){
					var $sub_li = $("#left .accordion-body li");
					$sub_li.removeClass("active");
					$sub_li.children("i").removeClass("icon-white");
					$(this).parent().addClass("active");
					$(this).children("i").addClass("icon-white");
				});
				
				// 展现三级
				$("#left .accordion-inner a").click(function(){
					var href = $(this).attr("data-href");
					if($(href).length > 0){
						$(href).toggle().parent().toggle();
						return false;
					}
					 <c:if test="${tabmode eq '1'}"> //打开显示页签
						return App.addTab($(this));
					 </c:if>
				});
				
				
				// 鼠标移动到边界自动弹出左侧菜单
				$("#openClose").mouseover(function(){
					if($(this).hasClass("open")){
						$(this).click();
					}
				});
				
				
			} 
			
			//左侧菜单标题点击时触发事件
			var menu_header_active = function($this){
				
				var $flag = $this.children("i");
				
				if($flag.hasClass("icon-chevron-down")){
					$flag.removeClass('icon-chevron-down').addClass('icon-chevron-left');
					return;
				}
				
				var thisAccordion = $('#left');
				thisAccordion.find('.accordion-heading a').children("i").removeClass('icon-chevron-down').addClass('icon-chevron-left');
				
				$flag.removeClass('icon-chevron-left').addClass('icon-chevron-down');
				
			}
			
			
			
			return {
				init:function(){
					
					handleResponsiveOnResize();
					
					// 链接去掉虚框
					$("#left a").bind("focus",function() {
						if(this.blur) {this.blur()};
					});
					
					
					<c:if test="${tabmode eq '1'}"> //初始化页签
						$.fn.initJerichoTab({
			                renderTo: '#right', uniqueId: 'jerichotab',
			                contentCss: { 'height': $('#right').height() - quickObj.height() - tabTitleHeight },
			                tabs: [], loadOnce: true, tabWidth: 110, titleHeight: tabTitleHeight, quickActionMenu: "#right .quick-actions_homepage"
			            });
					</c:if>
					
					sidebar_init();
					
					
					 <c:if test="${tabmode eq '1'}"> //下拉菜单以选项卡方式打开
						$("#userInfo .dropdown-menu a").mouseup(function(){
							return App.addTab($(this), true);
						});
					 </c:if>
					 
						
					App.getNotifyNum(); 
					
					<c:if test="${workNotifyRemindInterval ne '' && workNotifyRemindInterval ne '0'}">
						setInterval(App.getNotifyNum, '${workNotifyRemindInterval}'); 
					</c:if>
					
					
					
				},
				addTab:function($this,refresh) {
						$(".jericho_tab").show();
						$("#mainFrame").hide();
						$.fn.jerichoTab.addTab({
			                tabFirer: $this,
			                title: $this.text(),
			                closeable: true,
			                data: {
			                    dataType: 'iframe',
			                    dataLink: $this.attr('href')
			                }
			            }).loadData(refresh);
						return false;
			},
			getNotifyNum:function(){
				
				$.get("${ctx}/sys/user/getToDoTaskNum?updateSession=0&t="+new Date().getTime(),function(data){
					var num = parseFloat(data);
					if (num > 0){
						$("#notifyWorkedOption").show().html(num);
					}else{
						$("#notifyWorkedOption").hide()
					}
					
				});
			}
				
				
			};
			
		}();
		
		
</script>

        
</body>
</html>