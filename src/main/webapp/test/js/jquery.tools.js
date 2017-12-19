(function($){
	$.fn.checkByteLength = function(max,info){
		var _cur =  getByteLen($(this).val());
		if (_cur > max) {
			alert(info+" 最大文本长度为："+max+",实际为："+_cur+"！请修改！");
			return false
		}
		return true;
	}
	$.fn.checkByteLengthOnblur = function(max,info){
			var _cur =  getByteLen($(this).val());
			if (_cur > max) {
				alert(info+" 最大文本长度为："+max+",实际为："+_cur+"！请修改！");
				$(this).focus();
			}
	}
	//返回val的字节长度 
	function getByteLen(val) { 
		var len = 0; 
		for (var i = 0; i < val.length; i++) { 	//alert(val.charAt(i));//	alert(val.length);			
			//alert(val[i]);
			//alert(val[i]+";"+val[i].match(/[^\x00-\xff]/ig));
			if (val.charAt(i).match(/[^\x00-\xff]/ig) != null) //全角 
			len += 2; 
			else 
			len += 1; 
		}
		return len; 
	}
})(jQuery);