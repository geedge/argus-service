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
	
	
</style>


</head>
<body>

  			
        
<div id="main">
     <!-- header -->
      <div id="header" class="navbar navbar-fixed-top">
     	 <div class="navbar-inner">
	     	 <div  class="topleft">
	    		<a href="${pageContext.request.contextPath}/"  target="_parent"><img src="${ctxStatic}/images/loginlogo.png" title="系统首页" /></a>
	    	 </div>
	    	 <ul class="topnav">
	    	 	<c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
	                       		
                  <c:if test="${menu.parent.id eq 1 && menu.isShow eq 1}">
                  
			  	
			  			<c:if test="${menu.href != null && menu.href != ''}" var="leafNodeFlag">
				  			<li> 
					  			 <a href="${ctx}${menu.href}" id="${menu.id}" target="mainFrame" class="${idxStatus.index eq 0 ? 'selected' : ''}">
					  				<img src="${ctxStatic}/images/${menu.icon}.png" title="${menu.name }" />
					  				<h2>${menu.name }</h2>
				  				</a>
				  			</li>
				  		</c:if>
				  		
				  		<c:if test="${!leafNodeFlag }">
				  			<li>
				  				<a href="javascript:void(0);" id="${menu.id}"  class="${idxStatus.index eq 0 ? 'selected' : ''}">
				  					<img src="${ctxStatic}/images/${menu.icon}.png" title="${menu.name}" />
				  					<h2>${menu.name}</h2>
				  				</a>
				  			</li>
				  		</c:if>
			  			
			  		
			  		
			  	 </c:if>
	  	
							  
	           </c:forEach>
			  </ul>
			            
		    <div class="topright">    
			    <ul>
				    <li><a href="${pageContext.request.contextPath}/"  title="访问网站主页"><i class="icon-home"></i> 首页</a></li>
				    <li><a href="#"><i class="icon-question-sign"></i> 帮助</a></li>
				    <li><a href="${pageContext.request.contextPath }/logout" title="退出登录"><i class="icon-signout"></i> 退出</a></li>
			    </ul>
		     
		    	<div class="user">
		    		  
					<ul  class="nav pull-right">
		              	<li id="userInfo" class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息"><i class="icon-user"></i> 您好,${fns:getUser().name}&nbsp;<span id="notifyNum" class="label label-info hide"></span><b class="caret"></b></a>
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
						
						<li><a href="javascript:void(0);" class="tip-bottom" title="显示/隐藏快捷菜单"><i id="show-hide-option" class="icon-eye-open"></i></a></li>
						
               		 </ul>	
               		 
							
		    	</div>    
		    
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
          				 
          				 <div id="menu-${menu.id }" class="accordion ${idxStatus.index eq 0 ? '' : 'hide'}">
          				 	<div class="lefttop"><span></span>${menu.name }</div>
          				 		<dl class="leftmenu">
		          				 	
		          				 	<c:forEach  items="${menu.children}" var="secondMenu">
		          				 			<dd>
		          				 				 <div id="menu-${menu.id }-${secondMenu.id}" class="title">
		          				 				 	<img src="${ctxStatic}/images/${secondMenu.icon}.png" title="${secondMenu.name }"/>
											    	<%-- <span class="${secondMenu.icon } icon-white"></span> --%>${secondMenu.name} 
											     </div>
						                      	 <ul class="menuson">
											       
										        	
										 			 <c:forEach  items="${secondMenu.children}" var="thirdMenu" varStatus="thirdIdxStatus">
													    <c:if test="${thirdMenu.href != null && thirdMenu.href != '' }">
													    	
													    	 <li>
														    	 <span class="${not empty thirdMenu.icon?thirdMenu.icon:'icon-hide'} icon-white"></span>
														    	 <a  id="menu-${menu.id }-${secondMenu.id}-${thirdMenu.id}" data-id="${thirdMenu.id}" data-name="${menu.name }-${secondMenu.name}-${thirdMenu.name}"
														    	 href="${ctx}${thirdMenu.href}" target="mainFrame">${thirdMenu.name }<i></i></a>
													    	 </li>
													   		
													    </c:if>
										 			 </c:forEach>
							                    </ul>                 
			          				 			
			          				 			
		          				 			</dd>
		          				 		
		          				 	
		          				 	
		          				 	</c:forEach>
		          				 	
          				 		</dl>
          						
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
			   <%--   <div class="place">
				    <span><i class="icon-home"></i> </span>
				    <ul class="placeul">
				    <li><a href="${pageContext.request.contextPath}/" >首页</a></li>
				    <li><a href="javascript:void(0);" >首页</a></li>
				    </ul>
				 </div> --%>
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
			$("#"+ids[0]).click();
			
			
			
			var $secondMenu = $("#menu-"+ids[0]+"-"+ids[1]).siblings(".menuson");
			
			 if ($secondMenu.is(':hidden')){
				 $("#menu-"+ids[0]+"-"+ids[1]).click();
			  }
			
			 var $thirdMenu = $("#menu-"+ids[0]+"-"+ids[1]+"-"+ids[2]);
			 
			 if (!$thirdMenu.parent().hasClass("active")) {
				 $thirdMenu.children("i").click();
			 }
			 
		}
		
		
		var App = function() {
			
			var leftWidth = 190; // 左侧窗口大小
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
				var $topMenuFirst = $(".topnav li a:first").click();
				
				var menuId = "#menu-" + $topMenuFirst.attr("id");
				
				 // 初始化点击第一个二级菜单
				 if ($(menuId + " dd:first .menuson").is(":hidden")){
					$(menuId + " dd:first .title").click();
				}
				
				 // 初始化点击第一个三级菜单
				if (!$(menuId + " dd:first .menuson li:first").hasClass("active")){
					var liobject = $(menuId + " dd:first .menuson li:first a i");
					liobject.click();
					
				} 
				
				
			}
 			
			
			
			//菜单选择触发事件
			var menu_action_init = function() {
				
				
				//顶部导航切换
				$(".topnav li a").click(function(){
					// 一级菜单焦点
					$(".topnav li a.selected").removeClass("selected")
					$(this).addClass("selected");
					
					// 顶部为直接链接，左侧区域隐藏
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
					var menuId = "#menu-" + $(this).attr("id");
					
					//默认加载所有菜单
					if ($(menuId).length){
						$("#left>.accordion").addClass("hide");
						$(menuId).removeClass("hide");
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
					
				})	 
				
				
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
				$('.leftmenu .title').click(function(){
					menu_header_active($(this));
				});
				
				
				
				// 展现三级
				$(".menuson>li>a").click(function(){
					
					$(".menuson li.active").removeClass("active");
					
					$(this).parent().addClass("active");
					
					
					var href = $(this).attr("data-href");
					if($(href).length > 0){
						$(href).toggle().parent().toggle();
						return false;
					}
					
					//breadcrumb($(this));
					 <c:if test="${tabmode eq '1'}"> //打开显示页签
						return App.addTab($(this),true);
					 </c:if>
				});
				
				
				
				
				// 鼠标移动到边界自动弹出左侧菜单
				$("#openClose").mouseover(function(){
					if($(this).hasClass("open")){
						$(this).click();
					}
				});
				
				
			} 
			
			//生成当前位置栏目
			var breadcrumb = function($this) {
				var names = $this.attr("data-name").split("-");
				var breadHtml = '<li><a href="${pageContext.request.contextPath}/" >首页</a></li>';
			    
				for (i=0; i<names.length; i++) {
					breadHtml += '<li><a href="javascript:void(0);" >'+names[i]+'</a></li>';
				}
				
				$('.placeul').html(breadHtml);
			}
			
			//左侧菜单标题点击时触发事件
			var menu_header_active = function($this){
				var $ul = $this.next('ul');
				$('dd').find('.menuson').slideUp();
				if($ul.is(':visible')){
					$this.next('.menuson').slideUp();
				}else{
					$this.next('.menuson').slideDown();
				}
				
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
			}
				
			};
			
		}();
		
		
</script>

        
</body>
</html>