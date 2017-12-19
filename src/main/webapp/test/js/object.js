/*
* author:wangxl
* time:2007-09-14
* 对象类createSelectObject(下拉菜单对象)
*/

//创建下拉菜单对象
function createSelectObject(name){
	this.name = name;
	this.xmlDoc;
	this.xmlObj;
	this.nodes;
}
  //第一种方法定义
  
 //加载xml
createSelectObject.prototype.loadXML = function(url){
	  xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
	  xmlDoc.async=false;
	  xmlDoc.load(url);
	  xmlObj=xmlDoc.documentElement; 
	  nodes = xmlDoc.documentElement.childNodes;
  }
  //得到结果集列表
  createSelectObject.prototype.getNodesList = function(){
  	return nodes;
  }
  
  //删除所有下拉菜单中所有节点
  createSelectObject.prototype.removeAll = function(){
     obj = document.getElementById(this.name);
  	 for(var i=obj.options.length-1;i>=0;i--){
      	 obj.remove(i);
      }
  	//document.getElementById(this.name).innerHTML="";
  }
  
  //添加所有选项//等于父编号的
  createSelectObject.prototype.addItems = function(selectObj,items,nodeid,nodename,nodepid,nodepvalue){
  	 if(nodepid==null||nodepid==""){
	  	 for (i=0;i<items.length;i++){
			  nodeId=items(i).getAttribute(nodeid);
			  nodeName = items(i).getAttribute(nodename);
			  var option = document.createElement("option");
			  option.value = nodeId;
			  option.innerText = nodeName;
			  document.getElementById(this.name).insertAdjacentElement("beforeEnd",option);
	    }
  	 }else{
  		for (i=0;i<items.length;i++){
		  nodeId=items(i).getAttribute(nodeid);
		  nodePid = items(i).getAttribute(nodepid);
		  nodeName = items(i).getAttribute(nodename);
		  if(nodePid==nodepvalue){
		  var option = document.createElement("option");
		  option.value = nodeId;
		  option.innerText = nodeName;
		  document.getElementById(this.name).insertAdjacentElement("beforeEnd",option);
		}
   }
   }
  }
  
 	/**(Q0101)
	 * 方法：author()；
	 * 说明：获得版本说明信息；
	 * 返回：Strig 信息字符串；
	 * 参数：Null；
	 */
  createSelectObject.prototype.author = function (){
	  str = "Database Information Application System\n";
	  str = str + "版本 1.0\n";
	  str = str + "版权所有（C） 2007-2008 Wangxl\n";
	  str = str + "\n";
	  str = str + "Author：Wang Xiaolei\n";
	  str = str + "Date   ：2007-09-14\n";	
	  return str;
	}
	
    //下拉菜单结束
    
    
 	////////////////////////////////////
 	
 	//创建xpo对象

	/**
	 * 定义createXMLPOSTObject类构造函数
	 * 方法：
	 * Q0801 author() 
	 * 获得版本说明信息；        
	 */
	function createPOSTObject(){
	  this.httpObject = "MSXML2.XMLHTTP";
	  this.url = "";
	  this.resultStr = "";
	  this.async = false;
	  this.httpmethod = "POST";
	}
	
    //设置XMLHTTP对象"MSXML2.XMLHTTP";
	createPOSTObject.prototype.setHTTPObject = function (value){
		this.httpObject = value;
	}
	
	//设置XMLHTTP提交方式POST/GET
	createPOSTObject.prototype.setHTTPMethod = function (value){
		this.httpmethod = value;
	}
		
	//设置XMLHTTP通讯方式同步（false）/异步（true）；
	createPOSTObject.prototype.setAsync = function (bl){
		this.async = bl;
	}
	
	//设置通讯的URL；			
	createPOSTObject.prototype.setURL = function (value){
		this.url = value;
	}
		
	//通讯响应提交；
	createPOSTObject.prototype.post = function (){
  	if (this.url.indexOf(".xml") != -1){
  	
  	alert(url);
  	
  	}else{	
  		try{
  			http = new ActiveXObject(this.httpObject);
  			http.open(this.httpmethod,this.url,this.async);
  			http.send("");
  			this.resultStr = http.responseText;
  		} catch(ex) {
		} 	
  	}
	}
	
	//获得通讯结果数据；
    createPOSTObject.prototype.getResultStr = function (){
  		return this.resultStr;
	}

	/**(Q0801)
	 * 方法：author()；
	 * 说明：获得版本说明信息；
	 * 返回：Strig 信息字符串；
	 * 参数：Null；
	 */
	createPOSTObject.prototype.author = function (){
	  str = "Database Information Application System\n";
	  str = str + "版本 1.0\n";
	  str = str + "版权所有（C） 2007-2008 Wangxl\n";
	  str = str + "\n";
	  str = str + "Author：wang Xiaolei\n";
	  str = str + "Date   ：2007-11-19\n";	
	  alert(str);
	}
	
	//xpo对象结束
	
	//日期时间对象
		function DateUtil(){
			var date = new Date();
			
			this.year = date.getYear();
			
			this.month = date.getMonth()+1;
			this.month = this.month<10?"0"+this.month:this.month;
			
			this.day = date.getDate();
			this.day = this.day<10?"0"+this.day:this.day;

			this.hour = date.getHours();
			this.hour = this.hour<10?"0"+this.hour:this.hour;
			
			this.minute = date.getMinutes();
			this.minute = this.minute<10?"0"+this.minute:this.minute;
			
			this.second = date.getSeconds();
			this.second = this.second<10?"0"+this.second:this.second;
			
			
			this.getDay = function(){
				return this.year+"-"+this.month+"-"+this.day;
			}
			
			this.getTime = function(){
				return this.year+"-"+this.month+"-"+this.day+" "+this.hour+":"+this.minute+":"+this.second;
			}
		}	
		//日期对象结束
		
		//对所选择的行进行高亮显示ejiaA1("名称","奇数行背景","偶数行背景","鼠标经过背景","点击后背景");
		function ejiaA1(o,a,b,c,d){
			var t=document.getElementById(o).getElementsByTagName("tr");
			for(var i=1;i<t.length;i++){
				t[i].style.backgroundColor=(t[i].sectionRowIndex%2==0)?a:b;
				t[i].onclick=function(){
					if(this.x!="1"){
						//还原其他对象
						for(var j=1;j<t.length;j++){
							if(j!=this.rowIndex){
								t[j].showMsg = 0;//判断焦点在当前行时，避免重复点击
								t[j].x="0";
								t[j].style.backgroundColor=(this.sectionRowIndex%2==0)?a:b;
								t[j].style.color="#42687F"
							}
						}
						//设置当前对象
						this.x="1";
						this.style.backgroundColor=d;
						this.style.color="#FFFFFF"
					}else{
						//this.x="0";
						//this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b;
						//this.style.color="#000000"
					}
				}
				t[i].onmouseover=function(){
					if(this.x!="1"){
						this.style.backgroundColor=c;
					}
				}
				t[i].onmouseout=function(){
					if(this.x!="1"){
						this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b;
					}
				}
			}
		}
		//数据转换函数---判断是否为null如果是，返回""
		function test(a){
			if(a==null){
				a = "";
			}
			return a;
		}

		  //创建Excel对象-----------start
		function createExcelObject(){
				this.sheetIndex = 1;//读取的Excel页
				this.rowCount = 0;
				this.columnCount = 0;
				this.filePath;
				this.sheet;
				this.excelObj;
				this.workBook;
		}
		
		createExcelObject.prototype.getShellIndex = function(){
				return sheetIndex;
		}
		
		createExcelObject.prototype.setShellIndex = function(index){
				this.sheetIndex = index;
		}
		
		createExcelObject.prototype.getRowCount = function(){
				return rowCount;
		}
		
		createExcelObject.prototype.getColumnCount = function(){
				return columnCount;
		}
		
		createExcelObject.prototype.readExcel = function(uri){
			filePath = uri.replace("\\","\\\\");
			excelObj = new ActiveXObject("Excel.Application"); 
		    workBook = excelObj.Workbooks.open(filePath);
		    sheet = workBook.ActiveSheet;
		    rowCount = workBook.Worksheets(this.sheetIndex).UsedRange.Cells.Rows.Count
			columnCount=workBook.Worksheets(this.sheetIndex).UsedRange.Columns.Count
		}
		
		createExcelObject.prototype.get = function(i,j){
				return sheet.Cells(i,j).value;
		}
		
		createExcelObject.prototype.close = function(){
				workBook.close();
		}
		//创建excel对象----end
		//创建弹出窗体-----------start
		
		function createWindowObject(){
			this.name="";
			this.url;
			this.width;
			this.height;
			this.fullscreen = 0;
			this.scrollbars = 0;
		}
		
		createWindowObject.prototype.init = function(name,url,width,height,fullscreen,scrollbars){
			this.name = name;
			this.url = url;
			this.width = width;
			this.height = height;
			this.fullscreen = fullscreen;
			this.scrollbars = scrollbars;
		}
		
		createWindowObject.prototype.setName = function(name){
			this.name = name;
		}
		
		
		createWindowObject.prototype.setURL = function(url){
			this.url = url;
		}
		
		createWindowObject.prototype.setWidth = function(width){
			this.width = width;
		}
		
		createWindowObject.prototype.setHeight = function(height){
			this.height = height;
		}
		
		createWindowObject.prototype.setFullscreen = function(fullscreen){
			this.fullscreen = fullscreen;
		}
		
		createWindowObject.prototype.show = function(){
			window.open(this.url,this.name,"fullscreen="+this.fullscreen+",scrollbars="+this.scrollbars+",toolbar=0,location=0,directories=0,status=0,menubar=0,resizable=1,width="+this.width+",height="+this.height);
		}
		//--创建弹出窗体-----end
		//弹出窗体居中对齐对象----start
		function alignWindowObject(width,height){
			this.width = width;
			this.height = height;
			
		}
		
		alignWindowObject.prototype.alignCenter = function(){
			var pWidth = window.screen.width;
			var pHeight = window.screen.height;
			window.moveTo((pWidth/2)-(this.width/2),(pHeight/2)-(this.height/2));
		} 
		//------------弹出窗体对齐end
	//常用函数操作类-----start
	function Operation()
	{
	  this.help=null;	
	} 
	//删除字符串首部空格、回车、分隔字符；
	Operation.prototype.ltrimAll = function (str){
			str=this.getValidStr(str);
			if (!str) return "";
			for(var i=0;i<=str.length-1;i++){
				if (str.charCodeAt(i, 10)!=32 && str.charCodeAt(i, 10)!=160 && str.charCodeAt(i, 10)!=10 && str.charCodeAt(i, 10)!=13) break;
			}
			return str.substr(i, str.length);
	}

	/**(A21)
	 * 方法：rtrimAll()；
	 * 说明：删除字符串尾部空格、回车、分隔字符；
	 * 返回：Strig 字符串；
	 * 参数：str String，需要处理的字符串； 
	 */
	Operation.prototype.rtrimAll = function (str){
		str=this.getValidStr(str);
		if (!str) return "";
		for(var i=str.length-1; i>=0; i--){
			if (str.charCodeAt(i, 10)!=32 && str.charCodeAt(i, 10)!=160 && str.charCodeAt(i, 10)!=10 && str.charCodeAt(i, 10)!=13) break;
		}
		return str.substr(0, i+1);
	}

	/**(A22)
	 * 方法：trimAll()；
	 * 说明：删除字符串首尾部空格、回车、分隔字符；
	 * 返回：Strig 字符串；
	 * 参数：str String，需要处理的字符串； 
	 */
	Operation.prototype.trimAll = function (str){
		str=this.getValidStr(str);
		str=this.ltrimAll(str);
		str=this.rtrimAll(str);
		return str;
	}
	
	/**(A05)
	 * 方法：getValidStr()；
	 * 说明：确定获得的字符串为有效字符串；
	 * 返回：Strig 字符串；
	 * 参数：str String，需要处理的字符串； 
	 */
	Operation.prototype.getValidStr = function (str){
		str+="";
		if (str=="undefined" || str=="null")
			return "";
		else
			return str;
	}
//-----------------------常用函数操作类end
//------------------------创建排序对象	------start
/*
* 常用三种类型转换(int float date string)：
* sValue 要转换的值
* sDataType 要转换的值的类型
*/
function convert(sValue, sDataType) {
	sValue = new Operation().trimAll(sValue);//转型前取出前后空格
	switch (sDataType) {
	  case "int"://整型
		return parseInt(sValue);
	  case "float"://浮点型
		return parseFloat(sValue);
	  case "date"://日期型
		return new Date(Date.parse(sValue));
	  default://任何其他类型返回字符串
		return sValue.toString();
	}
}
/*
* 创建比较函数方法，这里采了闭包的方式，生成的比较函数根据所比较的列编号
* 与列数组类型不同而不同。
* iCol 要进行比较的列编号
* sDataType 列数据类型
*/
function generateCompareTRs(iCol, sDataType) {
	/*真真的比较函数，供数组的sort方法调用，oTR1 oTR2两个参数由sort方法传进来
	oTR1为比较的第一行，oTR2为比较的第二行*/
	return function compareTRs(oTR1, oTR2) {
		var vValue1 = convert(" ",sDataType);
		var vValue2 = convert(" ",sDataType);
		//判断单元格是否有值，有值的情况下才赋值，否则为空
		if(oTR1){
			if(oTR1.cells[iCol]){
				vValue1 = convert(oTR1.cells[iCol].innerText, sDataType);
				//vValue1 = convert(oTR1.cells[iCol].firstChild.nodeValue, sDataType);
			}
		}
		//判断单元格是否有值，有值的情况下才赋值，否则为空
		if(oTR2){
			if(oTR2.cells[iCol]){
				vValue2 = convert(oTR2.cells[iCol].innerText, sDataType);
				//vValue2 = convert(oTR2.cells[iCol].firstChild.nodeValue, sDataType);
			}
		}
		//按升序比较，如果是日期类型时会自动调用其valueOf方法返date 的毫秒再进行比较
		if (vValue1 < vValue2) {
			return -1;
		} else {
			if (vValue1 > vValue2) {
				return 1;
			} else {
				return 0;
			}
		}
	};
}
/*
* 表格比较，由HTML点击事件调用
* sTableID 要比较的表格id
* iCol 要较的表格的列的编号
* sDataType 列的数据类型
*/
function sortTable(sTableID, iCol, sDataType) {
	
	//获取表格对象
	var oTable = document.getElementById(sTableID);
	//获取表格体
	var oTBody = oTable.tBodies[0];
	//获取表格体中所有行
	var colDataRows = oTBody.rows;
	//存储所有表格行，借且于数组来进行排序处理
	var aTRs = new Array;
	       
	//把所有的行存储到数组里
	for (var i = 0; i < colDataRows.length; i++) {
		aTRs[i] = colDataRows[i];
	}
	/*sortCol为表格的扩展属性，标示最后是根据哪列来进行排序的。
	如果要传进来的列与上次排序的列是同一列时，直接对数组进行
	reverse反序操作，这样排序的速度会更快*/
	if (oTable.sortCol == iCol) {
		aTRs.reverse();
	} else {
	//如果是第一次排序，则调用排序算法进行排序
		aTRs.sort(generateCompareTRs(iCol, sDataType));
	}
	       				
	//创建文档碎片，这样不用一个一个把行添加到表格对象中，而是一次就可以了
	var oFragment = document.createDocumentFragment();
	for (var i = 0; i < aTRs.length; i++) {
		oFragment.appendChild(aTRs[i]);
	}
	      
	//把所排序的行重样追加到表格对象中，注：这里没有单独先删除表格排序前的行
	//因为如果追加的行是一样的话，appendChild操作会先自动删除后再添加。
	oTBody.appendChild(oFragment);
	oTable.sortCol = iCol;
}

//------------------------创建排序对象	-----end

  
/**//*  
**    ==================================================================================================  
**    类名：CLASS_MSN_MESSAGE  
**    功能：提供类似MSN消息框  
**    示例：  
    ---------------------------------------------------------------------------------------------------  
  
            var MSG = new CLASS_MSN_MESSAGE("gg",200,120,"短消息提示：","您有1封消息","今天请我吃饭哈");  
                MSG.show();  
  
    ---------------------------------------------------------------------------------------------------  
  
  
/**//*  
*    消息构造  
*/  
function CLASS_MSN_MESSAGE(id,width,height,caption,title,message,url,target,action){  
    this.id     = id;  
    this.title  = title;  
    this.caption= caption;  
    this.message= message;  
    this.target = target;  
    this.action = action;  
    this.width    = width?width:200;  
    this.height = height?height:120;  
    this.timeout= 150;  
    this.speed    = 20; 
    this.step    = 1; 
    this.right    = screen.width -1;  
    this.bottom = screen.height; 
    this.left    = this.right - this.width; 
    this.top    = this.bottom - this.height; 
    this.timer    = 0; 
    this.pause    = false;
    this.close    = false;
    this.autoHide    = true;
    this.url = url;
}  
  
/**//*  
*    隐藏消息方法  
*/  
CLASS_MSN_MESSAGE.prototype.hide = function(){  
    if(this.onunload()){  

        var offset  = this.height>this.bottom-this.top?this.height:this.bottom-this.top; 
        var me  = this;  

        if(this.timer){   
            window.clearInterval(me.timer);  
        }  

        var fun = function(){  
            if(me.pause==false||me.close){
                var x  = me.left; 
                var y  = 0; 
                var width = me.width; 
                var height = 0; 
                if(me.offset){ 
                    height = me.offset; 
                } 
     
                y  = me.bottom - height; 
     
                if(y>=me.bottom){ 
                    window.clearInterval(me.timer);  
                    me.Pop.hide();  
                } else { 
                    me.offset = me.offset - me.step;  
                } 
                me.Pop.show(x,y,width,height);    
            }             
        }  

        this.timer = window.setInterval(fun,this.speed)      
    }  
}  
  
/**//*  
*    消息卸载事件，可以重写  
*/  
CLASS_MSN_MESSAGE.prototype.onunload = function() {  
    return true;  
}  
/**//*  
*    消息命令事件，要实现自己的连接，请重写它  
*  
*/  
CLASS_MSN_MESSAGE.prototype.oncommand = function(){  
    //this.close = true;
    this.hide();  
	window.parent.mainFrame.location = this.url;
   
} 
/**//*  
*    消息显示方法  
*/  
CLASS_MSN_MESSAGE.prototype.show = function(){  

    var oPopup = window.createPopup(); //IE5.5+  
    
    this.Pop = oPopup;  
  
    var w = this.width;  
    var h = this.height;  
  
    var str = "<DIV style='BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX: 99999; LEFT: 0px; BORDER-LEFT: #a6b4cf 1px solid; WIDTH: " + w + "px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: " + h + "px; BACKGROUND-COLOR: #c9d3f3'>"  
        str += "<TABLE style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid' cellSpacing=0 cellPadding=0 width='100%' bgColor=#cfdef4 border=0>"  
        str += "<TR>"  
        str += "<TD style='FONT-SIZE: 12px;COLOR: #0f2c8c' width=30 height=24></TD>"  
        str += "<TD style='PADDING-LEFT: 4px; FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px' vAlign=center width='100%'>" + this.caption + "</TD>"  
        str += "<TD style='PADDING-RIGHT: 2px; PADDING-TOP: 2px' vAlign=center align=right width=19>"  
        str += "<SPAN title=关闭 style='FONT-WEIGHT: bold; FONT-SIZE: 12px; CURSOR: hand; COLOR: red; MARGIN-RIGHT: 4px' id='btSysClose' >×</SPAN></TD>"  
        str += "</TR>"  
        str += "<TR>"  
        str += "<TD style='PADDING-RIGHT: 1px;PADDING-BOTTOM: 1px' colSpan=3 height=" + (h-28) + ">"  
        str += "<DIV style='BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 8px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 8px; FONT-SIZE: 12px; PADDING-BOTTOM: 8px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 8px; BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%'>" + this.title + "<BR><BR>"  
        str += "<DIV style='WORD-BREAK: break-all' align=left><A href='javascript:void(0)' hidefocus=false id='btCommand'><FONT color=#ff0000>" + this.message + "</FONT></A><A href='' hidefocus=false id='ommand'><FONT color=#ff0000></FONT></A></DIV>"  
        str += "</DIV>"  
        str += "</TD>"  
        str += "</TR>"  
        str += "</TABLE>"  
        str += "</DIV>"  
  
    oPopup.document.body.innerHTML = str; 
    
  
    this.offset  = 0; 
    var me  = this;  

    oPopup.document.body.onmouseover = function(){me.pause=true;}
    oPopup.document.body.onmouseout = function(){me.pause=false;}

    var fun = function(){  
        var x  = me.left; 
        var y  = 0; 
        var width    = me.width; 
        var height    = me.height; 

            if(me.offset>me.height){ 
                height = me.height; 
            } else { 
                height = me.offset; 
            } 

        y  = me.bottom - me.offset; 
        if(y<=me.top){ 
            me.timeout--; 
            if(me.timeout==0){ 
                window.clearInterval(me.timer);  
                if(me.autoHide){
                    me.hide(); 
                }
            } 
        } else { 
            me.offset = me.offset + me.step; 
        } 
        me.Pop.show(x,y,width,height);    

    }  
  
    this.timer = window.setInterval(fun,this.speed)      
  
     
  
    var btClose = oPopup.document.getElementById("btSysClose");  
  
    btClose.onclick = function(){  
        me.close = true;
        me.hide();  
    }  
  
    var btCommand = oPopup.document.getElementById("btCommand");  
    btCommand.onclick = function(){  
        me.oncommand();  
    }    
	 var ommand = oPopup.document.getElementById("ommand");  
      ommand.onclick = function(){  
       //this.close = true;
    me.hide();  
	window.open(ommand.href);
    }   
}  
/**//* 
** 设置速度方法 
**/ 
CLASS_MSN_MESSAGE.prototype.speed = function(s){ 
    var t = 20; 
    try { 
        t = praseInt(s); 
    } catch(e){} 
    this.speed = t; 
} 
/**//* 
** 设置步长方法 
**/ 
CLASS_MSN_MESSAGE.prototype.step = function(s){ 
    var t = 1; 
    try { 
        t = praseInt(s); 
    } catch(e){} 
    this.step = t; 
} 
  
CLASS_MSN_MESSAGE.prototype.rect = function(left,right,top,bottom){ 
    try { 
        this.left        = left    !=null?left:this.right-this.width; 
        this.right        = right    !=null?right:this.left +this.width; 
        this.bottom        = bottom!=null?(bottom>screen.height?screen.height:bottom):screen.height; 
        this.top        = top    !=null?top:this.bottom - this.height; 
    } catch(e){} 
} 