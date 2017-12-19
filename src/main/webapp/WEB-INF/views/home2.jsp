<%@ include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${fns:getStringProperty('productName','NIS')}</title>

<style type="text/css">
		
		#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
		#footer, #footer a {color:#999;}
		
	</style>
</head>
<body>
	 <div id="loading_layer" style="display:none"><img src="${pageContext.request.contextPath }/static/img/ajax_loader.gif" alt="" /></div>
	 <div class="style_switcher">
            <div class="sepH_c">
                <p>Colors:</p>
                <div class="clearfix">
                    <a href="javascript:void(0)" class="style_item jQclr blue_theme style_active" title="blue">blue</a>
                    <a href="javascript:void(0)" class="style_item jQclr dark_theme" title="dark">dark</a>
                    <a href="javascript:void(0)" class="style_item jQclr green_theme" title="green">green</a>
                    <a href="javascript:void(0)" class="style_item jQclr brown_theme" title="brown">brown</a>
                    <a href="javascript:void(0)" class="style_item jQclr eastern_blue_theme" title="eastern_blue">eastern blue</a>
                    <a href="javascript:void(0)" class="style_item jQclr tamarillo_theme" title="tamarillo">tamarillo</a>
                </div>
            </div>
            <div class="sepH_c">
                <p>Backgrounds:</p>
                <div class="clearfix">
                    <span class="style_item jQptrn style_active ptrn_def" title=""></span>
                    <span class="ssw_ptrn_a style_item jQptrn" title="ptrn_a"></span>
                    <span class="ssw_ptrn_b style_item jQptrn" title="ptrn_b"></span>
                    <span class="ssw_ptrn_c style_item jQptrn" title="ptrn_c"></span>
                    <span class="ssw_ptrn_d style_item jQptrn" title="ptrn_d"></span>
                    <span class="ssw_ptrn_e style_item jQptrn" title="ptrn_e"></span>
                </div>
            </div>
            <div class="sepH_c">
                <p>Layout:</p>
                <div class="clearfix">
                    <label class="radio inline"><input type="radio" name="ssw_layout" id="ssw_layout_fluid" value="" checked /> Fluid</label>
                    <label class="radio inline"><input type="radio" name="ssw_layout" id="ssw_layout_fixed" value="gebo-fixed" /> Fixed</label>
                </div>
            </div>
            <div class="sepH_c">
                <p>Sidebar position:</p>
                <div class="clearfix">
                    <label class="radio inline"><input type="radio" name="ssw_sidebar" id="ssw_sidebar_left" value="" checked /> Left</label>
                    <label class="radio inline"><input type="radio" name="ssw_sidebar" id="ssw_sidebar_right" value="sidebar_right" /> Right</label>
                </div>
            </div>
            <div class="sepH_c">
                <p>Show top menu on:</p>
                <div class="clearfix">
                    <label class="radio inline"><input type="radio" name="ssw_menu" id="ssw_menu_click" value="" checked /> Click</label>
                    <label class="radio inline"><input type="radio" name="ssw_menu" id="ssw_menu_hover" value="menu_hover" /> Hover</label>
                </div>
            </div>
            
            <div class="gh_button-group">
                <a href="#" id="showCss" class="btn btn-primary btn-mini">Show CSS</a>
                <a href="#" id="resetDefault" class="btn btn-mini">Reset</a>
            </div>
            <div class="hide">
                <ul id="ssw_styles">
                    <li class="small ssw_mbColor sepH_a" style="display:none">body {<span class="ssw_mColor sepH_a" style="display:none"> color: #<span></span>;</span> <span class="ssw_bColor" style="display:none">background-color: #<span></span> </span>}</li>
                    <li class="small ssw_lColor sepH_a" style="display:none">a { color: #<span></span> }</li>
                </ul>
            </div>
        </div>
        
        
        <div id="maincontainer" class="clearfix">
        	<!-- header -->
            <header>
                <div class="navbar navbar-fixed-top">
                    <div class="navbar-inner">
                        <div class="container-fluid">
                            <a class="brand" href="dashboard.html"><i class="icon-home icon-white"></i> Gebo Admin</a>
                            <ul class="nav user_menu pull-right">
                                <li class="hidden-phone hidden-tablet">
                                    <div class="nb_boxes clearfix">
                                        <a data-toggle="modal" data-backdrop="static" href="#myMail" class="label ttip_b" title="New messages">25 <i class="splashy-mail_light"></i></a>
                                        <a data-toggle="modal" data-backdrop="static" href="#myTasks" class="label ttip_b" title="New tasks">10 <i class="splashy-calendar_week"></i></a>
                                    </div>
                                </li>
                                <li class="divider-vertical hidden-phone hidden-tablet"></li>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle nav_condensed" data-toggle="dropdown"><i class="flag-gb"></i> <b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="javascript:void(0)"><i class="flag-de"></i> Deutsch</a></li>
                                        <li><a href="javascript:void(0)"><i class="flag-fr"></i> Français</a></li>
                                        <li><a href="javascript:void(0)"><i class="flag-es"></i> Español</a></li>
                                        <li><a href="javascript:void(0)"><i class="flag-ru"></i> Pусский</a></li>
                                    </ul>
                                </li>
                                <li class="divider-vertical hidden-phone hidden-tablet"></li>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><img src="${pageContext.request.contextPath }/static/img/user_avatar.png" alt="" class="user_avatar" /> Johny Smith <b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="user_profile.html">My Profile</a></li>
                                        <li><a href="javascrip:void(0)">Another action</a></li>
                                        <li class="divider"></li>
                                        <li><a href="${pageContext.request.contextPath }/logout">Log Out</a></li>
                                    </ul>
                                </li>
                            </ul>
                            <ul class="nav" id="mobile-nav">
                               
                              <c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
                              	
                              	<c:if test="${empty menu.children}" var="subMenuFlag">
							  		<c:if test="${menu.isLeaf eq 1}">
							  			<li>
							  			 	<a href="${pageContext.request.contextPath }/${menu.menuUrl}" data-id="${menu.id}" target="mainFrame"><i class="${menu.menuClass }  icon-white"></i> ${menu.menuName }</a>
							  			</li>
							  		</c:if>
							  	</c:if>
							  	
							  	<c:if test="${!subMenuFlag}">
								  	<li class="dropdown">
								  		<a data-toggle="dropdown" class="dropdown-toggle" data-id="${menu.id}" href="javascript:void(0);"><i class="${menu.menuClass}"></i> ${menu.menuName } <b class="caret"></b></a>
								  		<ul class="dropdown-menu">
								  			 <c:forEach  items="${menu.children}" var="secondMenu">
								  			 	<!-- 二级菜单 -->
									      	 	<c:if test="${empty secondMenu.children}" var="secondMenuFlag">
									      	 		<li><a href="${pageContext.request.contextPath }/${secondMenu.menuUrl}" data-id="${secondMenu.id}" target="mainFrame">${secondMenu.menuName }</a></li>
								  				</c:if>
								  				
								  				<c:if test="${!secondMenuFlag}">
								  				
								  					<li class="dropdown">
			                                            <a href="javascript:void(0);" data-id="${secondMenu.id}">${secondMenu.menuName } <b class="caret-right"></b></a>
			                                            <ul class="dropdown-menu">
			                                            	<!-- 第三级别菜单 -->
									  				    	<c:forEach  items="${secondMenu.children}" var="thirdMenu">
									  				    		<c:if test="${thirdMenu.isLeaf == 1}">
									  				    			<li><a href="${pageContext.request.contextPath }/${thirdMenu.menuUrl}" data-id="${thirdMenu.id}" target="mainFrame">${thirdMenu.menuName }</a></li>
									  				    		</c:if>
									  				    	</c:forEach>
			                                                
			                                            </ul>
			                                        </li>
								  					
									  				
								  				
							  					</c:if>
								  				
								  			 	
								  			 </c:forEach>
								  		</ul>	
								    </li>
							  	</c:if>
							  	
							  	
							  	
							  	
							  	
                              </c:forEach>
                                
                                <li class="dropdown">
                                    <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-th icon-white"></i> Components <b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="alerts_btns.html">Alerts & Buttons</a></li>
                                        <li><a href="icons.html">Icons</a></li>
                                        <li><a href="notifications.html">Notifications</a></li>
                                        <li><a href="tables.html">Tables</a></li>
                                        <li><a href="tables_more.html">Tables (more examples)</a></li>
                                        <li><a href="tabs_accordion.html">Tabs & Accordion</a></li>
                                        <li><a href="tooltips.html">Tooltips, Popovers</a></li>
                                        <li><a href="typography.html">Typography</a></li>
                                        <li><a href="widgets.html">Widget boxes</a></li>
                                        <li class="dropdown">
                                            <a href="#">Sub menu <b class="caret-right"></b></a>
                                            <ul class="dropdown-menu">
                                                <li><a href="#">Sub menu 1.1</a></li>
                                                <li><a href="#">Sub menu 1.2</a></li>
                                                <li><a href="#">Sub menu 1.3</a></li>
                                                <li>
                                                    <a href="#">Sub menu 1.4 <b class="caret-right"></b></a>
                                                    <ul class="dropdown-menu">
                                                        <li><a href="#">Sub menu 1.4.1</a></li>
                                                        <li><a href="#">Sub menu 1.4.2</a></li>
                                                        <li><a href="#">Sub menu 1.4.3</a></li>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                               
                                <li>
                                    <a href="documentation.html"><i class="icon-book icon-white"></i> Help</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="modal hide fade" id="myMail">
                    <div class="modal-header">
                        <button class="close" data-dismiss="modal">×</button>
                        <h3>New messages</h3>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-info">In this table jquery plugin turns a table row into a clickable link.</div>
                        <table class="table table-condensed table-striped" data-provides="rowlink">
                            <thead>
                                <tr>
                                    <th>Sender</th>
                                    <th>Subject</th>
                                    <th>Date</th>
                                    <th>Size</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Declan Pamphlett</td>
                                    <td><a href="javascript:void(0)">Lorem ipsum dolor sit amet</a></td>
                                    <td>23/05/2012</td>
                                    <td>25KB</td>
                                </tr>
                                <tr>
                                    <td>Erin Church</td>
                                    <td><a href="javascript:void(0)">Lorem ipsum dolor sit amet</a></td>
                                    <td>24/05/2012</td>
                                    <td>15KB</td>
                                </tr>
                                <tr>
                                    <td>Koby Auld</td>
                                    <td><a href="javascript:void(0)">Lorem ipsum dolor sit amet</a></td>
                                    <td>25/05/2012</td>
                                    <td>28KB</td>
                                </tr>
                                <tr>
                                    <td>Anthony Pound</td>
                                    <td><a href="javascript:void(0)">Lorem ipsum dolor sit amet</a></td>
                                    <td>25/05/2012</td>
                                    <td>33KB</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:void(0)" class="btn">Go to mailbox</a>
                    </div>
                </div>
                <div class="modal hide fade" id="myTasks">
                    <div class="modal-header">
                        <button class="close" data-dismiss="modal">×</button>
                        <h3>New Tasks</h3>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-info">In this table jquery plugin turns a table row into a clickable link.</div>
                        <table class="table table-condensed table-striped" data-provides="rowlink">
                            <thead>
                                <tr>
                                    <th>id</th>
                                    <th>Summary</th>
                                    <th>Updated</th>
                                    <th>Priority</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>P-23</td>
                                    <td><a href="javascript:void(0)">Admin should not break if URL&hellip;</a></td>
                                    <td>23/05/2012</td>
                                    <td class="tac"><span class="label label-important">High</span></td>
                                    <td>Open</td>
                                </tr>
                                <tr>
                                    <td>P-18</td>
                                    <td><a href="javascript:void(0)">Displaying submenus in custom&hellip;</a></td>
                                    <td>22/05/2012</td>
                                    <td class="tac"><span class="label label-warning">Medium</span></td>
                                    <td>Reopen</td>
                                </tr>
                                <tr>
                                    <td>P-25</td>
                                    <td><a href="javascript:void(0)">Featured image on post types&hellip;</a></td>
                                    <td>22/05/2012</td>
                                    <td class="tac"><span class="label label-success">Low</span></td>
                                    <td>Updated</td>
                                </tr>
                                <tr>
                                    <td>P-10</td>
                                    <td><a href="javascript:void(0)">Multiple feed fixes and&hellip;</a></td>
                                    <td>17/05/2012</td>
                                    <td class="tac"><span class="label label-warning">Medium</span></td>
                                    <td>Open</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:void(0)" class="btn">Go to task manager</a>
                    </div>
                </div>
            </header>
            
             <!-- main content -->
            <div id="contentwrapper" class="">
               
               
                <div class="main_content">
                    <iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650">
                    	浏览器不支持嵌入式框架或配置为不显示嵌入式框架。
                    </iframe>
                </div>
                
                 <!-- sidebar -->
            <a href="javascript:void(0)" class="sidebar_switch on_switch ttip_r" title="Hide Sidebar">Sidebar switch</a>
            <div class="sidebar">
                
                <div class="antiScroll">
                    <div class="antiscroll-inner">
                        <div class="antiscroll-content">
                    
                            <div class="sidebar_inner">
                                <form action="search_page.html" class="input-append" method="post" >
                                    <input autocomplete="off" name="query" class="search_query input-medium" size="16" type="text" placeholder="Search..." /><button type="submit" class="btn"><i class="icon-search"></i></button>
                                </form>
                               
                                <div id="side_accordion" class="accordion">
                                    
                                    <c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
                              			<div class="accordion-group">
                              				<div class="accordion-heading">
	                                            <a href="#collapse_${menu.id}" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
	                                                <i class="${menu.menuClass }"></i> ${menu.menuName}
	                                            </a>
                                        	</div>
                                        	
                                        	<div class="accordion-body collapse" id="collapse_${menu.id}">
	                                            <div class="accordion-inner">
	                                                <ul class="nav nav-list">
	                                                    
	                                                    <c:if test="${empty menu.children}" var="subMenuFlag">
													  		<c:if test="${menu.isLeaf eq 1}">
													  			<li>
													  			 	<a href="${pageContext.request.contextPath }/${menu.menuUrl}" data-id="${menu.id}" target="mainFrame"><i class="${menu.menuClass }"></i> ${menu.menuName }</a>
													  			</li>
													  		</c:if>
												  		</c:if>
												  		
												  		
												  		<c:if test="${!subMenuFlag}">
												  			 <c:forEach  items="${menu.children}" var="secondMenu">
												  			 	<!-- 二级菜单 -->
													      	 	<c:if test="${empty secondMenu.children}" var="secondMenuFlag">
													      	 		<li><a href="${pageContext.request.contextPath }/${secondMenu.menuUrl}" data-id="${secondMenu.id}" target="mainFrame">${secondMenu.menuName }</a></li>
												  					 <li class="divider"></li>
												  				</c:if>
												  				
												  				<c:if test="${!secondMenuFlag}">
												  					<li class="nav-header">${secondMenu.menuName }</li>
												  				
					                                            	<!-- 第三级别菜单 -->
											  				    	<c:forEach  items="${secondMenu.children}" var="thirdMenu">
											  				    		<c:if test="${thirdMenu.isLeaf == 1}">
											  				    			<li><a href="${pageContext.request.contextPath }/${thirdMenu.menuUrl}" data-id="${thirdMenu.id}" target="mainFrame">${thirdMenu.menuName }</a></li>
											  				    		</c:if>
											  				    	</c:forEach>
											  				    	<li class="divider"></li>
							                                                
													  				
												  				
											  					</c:if>
												  				
												  			 	
												  			 </c:forEach>
													  	</c:if>
												  		
												  		
												  		
	                                                
	                                                
	                                                </ul>
	                                                
	                                            </div>
			                                  </div>
			                                  
			                                  
                                        	
                                        	
                              				
                              			</div>
		                              	
		                              	
		                          
									  	
									  	
                              		</c:forEach>
                              		
                              		
                              		
   
                                    
                                    <div class="accordion-group">
                                        <div class="accordion-heading">
                                            <a href="#collapseFour" data-parent="#side_accordion" data-toggle="collapse" class="accordion-toggle">
                                                <i class="icon-cog"></i> Configuration
                                            </a>
                                        </div>
                                        <div class="accordion-body collapse" id="collapseFour">
                                            <div class="accordion-inner">
                                                <ul class="nav nav-list">
                                                    <li class="nav-header">People</li>
                                                    <li class="active"><a href="javascript:void(0)">Account Settings</a></li>
                                                    <li><a href="javascript:void(0)">IP Adress Blocking</a></li>
                                                    <li class="nav-header">System</li>
                                                    <li><a href="javascript:void(0)">Site information</a></li>
                                                    <li><a href="javascript:void(0)">Actions</a></li>
                                                    <li><a href="javascript:void(0)">Cron</a></li>
                                                    <li class="divider"></li>
                                                    <li><a href="javascript:void(0)">Help</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    
                                    
                                    
                                </div>
                                
                                <div class="push"></div>
                            </div>
                               
                            <div class="sidebar_info">
                                <ul class="unstyled">
                                    <li>
                                        <span class="act act-warning">65</span>
                                        <strong>New comments</strong>
                                    </li>
                                    <li>
                                        <span class="act act-success">10</span>
                                        <strong>New articles</strong>
                                    </li>
                                    <li>
                                        <span class="act act-danger">85</span>
                                        <strong>New registrations</strong>
                                    </li>
                                </ul>
                            </div> 
                        
                        </div>
                    </div>
                </div>
            
            </div>
                
                
                
            </div>
            
            
            
              <div id="footer" class="row-fluid">
	           	 Copyright &copy;  <a href="http://jeesite.com" target="_blank">JeeSite</a> 
				</div>
        </div>
        
       
	
	
	
</body>
</html>