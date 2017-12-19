(function($) {
	var arr = new Array();
	$.fn.selectList = function(options) {
		var o = $.extend( {}, $.fn.selectList.defaults, options);
		var $this = $(this);
		arr[$this.attr("id")] = o;
		var appDiv = $(o.appendDiv);
		$this.bind( {
			click : function() {
				var objId = $(this).attr("id");
				var curOpt = arr[objId];
				var ids = '';
				if (curOpt.type == 0) {
					ids = $('#' + objId + 'Id').val();
				} else {
					$('input[name^="' + curOpt.idsName + '"]', appDiv).each(
							function() {
								ids += "," + $(this).val();
							}
					)
				}
				var url = curOpt.url + "?type=" + curOpt.type 
						+ "&idsName='" + curOpt.idsName + "'&defaultLen=" + curOpt.defaultLen
						+"&substring=" + curOpt.substring + "&titleType=" + curOpt.titleType
						+ "&selectItemWidth=" + (curOpt.itemWidth + 30)+ '&date=' + new Date()
						+'&ids=' + ids+'&differ=' + curOpt.idsName; 
				if (curOpt.data) {
					url += "&" + curOpt.data;
				}
				/*var result = window.showModalDialog(url, ids, 'dialogWidth:'
						+ curOpt.width + 'px;dialogHeight:' + curOpt.height
						+ 'px;status:no;resizable:yes');*/
				
				document.getElementById("userrole").innerHTML="<iframe id='url'style='width: 600px;height: 400px;position: fixed;left: 50%;margin-left: -300px;z-index: 9999'></iframe>";
				$("#url").attr("src",url);
				$("#userrole").show();
			},
			keydown : function(event) {
				if (event.which == 192) {
					$(this).trigger("click");
				}
			}
		})
		return $this;
	}
	$.fn.selectList.defaults = {
		type : 0,
		url : "",
		itemClass : "item",
		idsName : "ids",
		appendDiv : "body",
		data : "",
		itemWidth : 60,
		width : 550,
		height : 400,
		substring : true,
		titleType : 1,
		defaultLen : 0
	};

	function addLabel(objId, id, name, title,index,name1) {
		var curOpt = arr[objId];
		var itemLabel = $("<div>", {
			"class" : curOpt.itemClass
		});
		var nameDiv = $("<span>", {
			width : curOpt.itemWidth,
			display : "block",
			text : name
		}).css( {
			"float" : "left"
		});
		if (curOpt.substring == true) {
			nameDiv.css( {
				"white-space" : "nowrap",
				"text-overflow" : "ellipsis",
				"overflow" : "hidden"
			})
		}
		if (title) {
			itemLabel.attr("title", title);
		} else {
			if (curOpt.titleType == 0) {
				nameDiv.attr("title", id);
			} else if (curOpt.titleType == 1) {
				nameDiv.attr("title", name);
			} else if (curOpt.titleType == 2) {
				nameDiv.attr("title", id + ":" + name);
			} else if (curOpt.titleType == 3) {
				nameDiv.attr("title", name + ":" + id);
			}
		}

		itemLabel.append(nameDiv);
		var delBtn = $("<span>", {
			"class" : "delBtn",
			text : "\xd7"
		}).css( {
			"cursor" : "pointer"
		})
		delBtn.bind("click", function() {
			
			if (curOpt.type == 2){
				var ind;
				$(this).parents().nextAll().each(function(i,itme){
					var input=$(this).find("input[name^='"+curOpt.idsName+"']");
					if(input.size() >0){
						var name=input.attr("name");
						ind = name.substring(name.length-2,name.length-1)-1;
						input.attr("name",curOpt.idsName+"["+ind+"]");
					}
				})
				curOpt.defaultLen = curOpt.defaultLen -1;
			}
			$(this).closest("." + curOpt.itemClass).remove();
		})
		itemLabel.append(delBtn);
		var hiddenId = $('<input type="hidden" name="' + curOpt.idsName
				+ '" value="' + id + '"/>');
		if (curOpt.type == 2){
			if(name1 != ""){
				hiddenId = $('<input type="hidden" name="' + name1
				+ '" value="' + id + '"/>');
			}
			if(index != ""){
				hiddenId = $('<input type="hidden" name="' + curOpt.idsName+"["+index
				+ ']" value="' + id + '"/>');
			}
			
		}
		itemLabel.append(hiddenId);
		itemLabel.css( {
			"float" : "left",
			"margin" : 1,
			"padding" : 2,
			"font-size":12,
			"background-color" : "#d3eaef",
			"border" : "solid 1px #b5d6e6"
		});
		$(curOpt.appendDiv).append(itemLabel);
		//alert($(curOpt.appendDiv).html());
	}

	$.fn.addItem = function(id, name, title,index) {
		var objId = $(this).attr("id");
		addLabel(objId, id, name, title,index);
		return $(this);
	}

	$.fn.setParam = function(attr, value) {
		var objId = $(this).attr("id");
		var curOpt = arr[objId];
		curOpt[attr] = value;
		return $(this);
	}
})(jQuery);
