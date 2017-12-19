
var highlightcolor = "#c1ebff";
//此处clickcolor只能用win系统颜色代码才能成功,如果用#xxxxxx的代码就不行
var clickcolor = "#D9D8D8";
var colorT = "#000000";
var nochangeIndex = null;
function submenu(menuId) {
	//alert("menuIdt" + menuId);
	var parentNodes = new Array();
	var i = 0;
	var img = document.getElementById("image" + menuId);
	//alert("menuIdt" + menuId);
	//当前所点节点元素
	var currentDiv = document.getElementById(menuId);
	//判断当前菜单是否打开
	if (isOpen(currentDiv)) {
		//alert("current menu is open");
	   //关闭当前菜单
		closeMenu(currentDiv, img);
	} else {
		//alert("current menu is closed");
        //得到所有的table的元素
		var trNum = document.getElementsByTagName("span");
		//alert("trNum.lengtht"+trNum.length);
	    //将所有菜单都关闭
		closeAll();
		//alert("all menu are closed");
	    //得到当前节点的父节点集合
		getParentNode(currentDiv, "SPAN", parentNodes, i);
		//alert("parentNodest" + parentNodes.length);
	    //如果有父节点　打开父菜单
		if (parentNodes.length > 0) {
			for (var j = 0; j < parentNodes.length; j++) {
				var parentDiv = document.getElementById(parentNodes[j]);
				//if (parentDiv.id != "root") {
				var parentImg = document.getElementById("image" + parentDiv.id);
				openMenu(parentDiv, parentImg);
				//}
			}
		}
		//打开点击的菜单
		openMenu(currentDiv, img);
	}
}

//根据子节点找到对应的父节点trId
function getParentNode(currentNode, condition, parentNodes, i) {
	var parentNodeTemp = currentNode.parentNode;
	//alert("parentNodeTemp.nodeNamet" + parentNodeTemp.nodeName);
	if (parentNodeTemp.nodeName != "UL") {
		if (parentNodeTemp.nodeName == condition) {
			//alert(i + "===parentNodeTemp.idt" + parentNodeTemp.id);
			parentNodes[i] = parentNodeTemp.id;
			//alert("parentNodeIdt" + parentNodeTemp.id);
			i += 1;
		}
		getParentNode(parentNodeTemp, condition, parentNodes, i);
	}else if(parentNodeTemp.nodeName == "div"){
		return;
	}
}
//打开菜单
function openMenu(currentDiv, img) {
	//alert(currentDiv.style.display);
	//alert(currentDiv.id);
	//document.getElementById("402").style.display="";
	currentDiv.style.display = "block";	
	//alert(currentDiv.style.display);
	if (img != null) {
		img.src = "images/tree/open_image.gif";
	}
}
//关闭菜单
function closeMenu(currentDiv, img) {
	currentDiv.style.display = "none";
	if (img != null) {
		img.src = "images/tree/close_image.gif";
	}
}
//关闭所有菜单
function closeAll() {
	var trNum = document.getElementsByTagName("span");
	//alert("trNumt" + trNum.length);
	for (var i = 1; i < trNum.length; i++) {
		var div = document.getElementsByTagName("span")[i];
		//alert(i + "=t" + div.id);
		if ("" != div.id && null != div.id && "show"!=div.id) {
			var imgId = "image" + div.id;
			var imgTemp = document.getElementById(imgId);
			closeMenu(div, imgTemp);
		}
	}
}
//判断当前菜单是否打开
function isOpen(currentDiv) {
//alert(currentDiv+"::::"+currentDiv.style.display);
	if (currentDiv.style.display == "" || currentDiv.style.display == "block") {
		return true;
	}
	return false;
}
function mouseoverEvent() {
	source = event.srcElement;
	if (source.tagName == "li" || source.tagName == "div") {
		return;
	}
	while (source.tagName != "li") {
		source = source.parentElement;
	}
	source = source.parentElement;
	cs = source.children;
//alert(cs.length);
	if (cs[0].style.backgroundColor != highlightcolor && source.id != "nc" && cs[0].style.backgroundColor != clickcolor) {
		for (i = 0; i < cs.length; i++) {
			if (cs[i].style != null) {
				cs[i].style.backgroundColor = highlightcolor;
			}
		}
	}
}
function mouseoutEvent() {
    //alert("changeback");
	source = event.srcElement;
	if (source.tagName == "li" || source.tagName == "div") {
		return;
	}
	while (source.tagName != "li") {
		source = source.parentElement;
	}
	source = source.parentElement;
	if (nochangeIndex != null) {
		var trElement = document.getElementById("tr" + nochangeIndex);
		if (trElement == source) {
			return;
		}
	}
	if (event.fromElement.contains(event.toElement) || source.contains(event.toElement) || source.id == "nc") {
		return;
	}
	if (event.toElement != source && cs[0].style.backgroundColor != clickcolor) {
//source.style.backgroundColor=originalcolor
	}
	for (i = 0; i < cs.length; i++) {
			//alert("1");
		if (cs[i].style != null && cs[i].style.backgroundColor != clickcolor) {
			cs[i].style.backgroundColor = "#FFFFFF";
		}
	}
}
function clickLinkEvent() {
    //alert("click");
	//revertBgColor();
    //设置当前点中行的宽度为最大值
    //var tableMax=document.getElementById("root");
    //var widthMax=tableMax.width;
    //alert("widthMaxt"+widthMax);
	source = event.srcElement;
	//alert("tagName1t" + source.tagName);
	if ( source == "" ||source.tagName == "span" || source.tagName == "ul" || source.tagName == "IMG") {
		return;
	}
	while (source.tagName != "li") {//alert("source.tagName!=li:"+source.parentElement.tagName);
		//alert("source.tagName!=TD");
		source = source.parentElement;
		//alert("source.tagName="+source.tagName);
	}
	//TR
	source = source.parentElement;
	cs = source.children;
	//source.style.pixelLeft=document.body.clientLeft;
	//alert("cs.lengtht" + cs.length);
   //设置点击的行的颜色
   //source.width=widthMax;
	if (source.id != "nc" && cs[0].style.background != clickcolor) {
	  // alert("change color begin");
		for (i = 0; i < cs.length; i++) {
			//alert("dfd");
			if(cs[i].style.backgroundColor!=clickcolor)
			{
				cs[i].style.backgroundColor = clickcolor;
			}
		}
		//alert("change color end");
	}
}
//判断td中是否存在table
function tableIsExistInTr(trObj) {
	var tds = trObj.children;
	for (var i = 0; i < tds.length; i++) {
		if (tds[i].tagName == "li") {
			var tdcs = tds[i].children;
	      //alert(tdcs.length);
			for (var j = 0; j < tdcs.length; j++) {
				if (tdcs[j].tagName == "div") {
	                //alert(trObj.id);
					return true;
				}
			}
		}
	}
	return false;
}
//将所有的行的背景色设置为#FFFFFF
function revertBgColor() {
	var trObjs = document.getElementsByTagName("li");
   //alert(trObjs.length);
	for (var i = 0; i < trObjs.length; i++) {
		var trTemp = trObjs[i];
    // alert(trTemp.style.display);
		if (trTemp.style.display == "") {
			var trcs = trTemp.children;
       //alert(trcs.length);
			for (var j = 0; j < trcs.length; j++) {
				trcs[j].style.backgroundColor = "#FFFFFF";
			}
		}
	}
}

