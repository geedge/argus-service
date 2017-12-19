/* $(function() { 
        $('#yhmm').keyup(function() {
        	$('#yhmm').next().html("*");
            var val = $(this).val(); 
            //isDigit(val);
            checkpass(val);
        }); 
         
        function isDigit(s) { 
            var pattern_d = /^\d+$/;            //全数字 
            var pattern_s = /^[A-Za-z_]+$/       //字母、下划线 
            var pattern_w = /^\w+$/;            //数字、字母、下划线
            var pattern_W = /^\W+$/             //全非数字也非字符 
            //var pattern_r = /^\w+[0-9]+\w+$|^[0-9]+\w+[0-9]+$/    //以字符或者数字开头结尾的字符串 
            var pattern_r = /^[A-Za-z_]+[0-9]+[A-Za-z_]+[A-Za-z_0-9]*?$|^[0-9]+[A-Za-z_]+[0-9]+[A-Za-z_0-9]*?$/    //以字符或者数字开头结尾的字符串

            var html = ''; 
            var x = 0; 
            var y = 0; 
            
            //先验证密码长度
            if(s.length<8){
            	html += '密码长度需大于等于8位&nbsp;';
            	$('#tj').attr('disabled','disabled'); 
            }//else
             if(s.length>50){
            	html += '密码长度需小于等于50位&nbsp;';
            	$('#tj').attr('disabled','disabled'); 
            }//else{
            	if(pattern_W.exec(s)) { //alert(1);//包含特殊字符
                    x = 0; 
                    y = 0; 
                } 
            	if(pattern_w.exec(s)) { //alert(2); //数字或字符
                    y = 1; 
                } 
                if(pattern_d.exec(s)) { //alert(3);全数字
                    x = 1; 
                    y = 0; 
                } 
                if(pattern_s.exec(s)) { //alert(4);全字符
                    x = 2; 
                    y = 0; 
                } 
                if(pattern_r.exec(s)) {// alert(5);'数字字符数字' 或者 '字符数字字符'        
                    x = 3; 
                    y = 2; 
                } 
                if( x === 0 && y === 0) { //alert(6);
                    html += '包含特殊字符&nbsp;禁止提交&nbsp;';
                    $('#tj').attr('disabled','disabled'); 
                } 
                if( x === 1 && y === 0) { //alert(7);
                    html += '全数字&nbsp;禁止提交&nbsp;';
                    $('#tj').attr('disabled','disabled'); 
                } 
                if( x === 2 && y === 0) { //alert(7);
                    html += '全字符&nbsp;禁止提交&nbsp;';
                    $('#tj').attr('disabled','disabled'); 
                } 
                if( y === 1 && s.length>=8 && s.length<13) { //alert(7);
                    html += '安全级别《低》禁止提交&nbsp;';
                    $('#tj').attr('disabled','disabled'); 
                } 
                if( x === 0 && y === 1 && s.length>=12) { //alert(8);
                     html += '安全级别《中》&nbsp;';  
                     $('#tj').attr('disabled',''); 
                } 
                if( x === 3 && y === 2 && s.length >= 8) { //alert(9);
                    html += '安全级别《高》&nbsp;';
                    $('#tj').attr('disabled',''); 
                } 
            //}
           // $('#password').html(html); 
        }; 
        
        function testpass(password,username){
            var score = 0;
            if (password.length < 8 ) { return -4; }

            if (typeof(username) != 'undefined' && password.toLowerCase() == username.toLowerCase()){return -2}
            score += password.length * 4;
            score += ( repeat(1,password).length - password.length ) * 1;
            score += ( repeat(2,password).length - password.length ) * 1;
            score += ( repeat(3,password).length - password.length ) * 1;
            score += ( repeat(4,password).length - password.length ) * 1;
            if (password.match(/(.*[0-9].*[0-9].*[0-9])/)){ score += 5;}
            if (password.match(/(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])/)){ score += 5 ;}
            if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)){ score += 10;}
            if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)){ score += 15;}
            if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([0-9])/)){ score += 15;}
            if (password.match(/([!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([a-zA-Z])/)){score += 15;}
            if (password.match(/^\w+$/) || password.match(/^\d+$/) ){ score -= 10;}
            if ( score < 0 ){score = 0;}
            if ( score > 100 ){ score = 100;}
            return score;
             
            function repeat(len,str){
	            var res = "";
	            for (var i = 0; i < str.length; i++ ){
	                var repeated = true;
	                for (var j = 0, max = str.length - i - len; j < len && j < max; j++){
	                    repeated = repeated && (str.charAt(j + i) == str.charAt(j + i + len));
	                }
	                if (j < len) repeated = false;
	                if (repeated) {
	                    i += len - 1;
	                    repeated = false;
	                }else{
	                    res += str.charAt(i);
	                }
	            }
	            return res;
            }
        }
        function checkpass(passVal){
            var username =document.getElementById("yhmc").value;
            var score = testpass(passVal,username);
            var password_label = document.getElementById('password');
            if(score == -4)    {
            	password_label.innerHTML = "<span style='color: red;'>密码太短,至少8位</span>";
            	$('#tj').attr('disabled','disabled'); 
            }else if(score == -2){
            	password_label.innerHTML = "<span style='color: red;'>与用户名相同</span>";
            	$('#tj').attr('disabled','disabled'); 	
            }else{
                var color = score < 34 ? '#edabab' : (score < 68 ? '#ede3ab' : '#d3edab');
                var text = score < 34 ? '弱' : (score < 68 ? '一般' : '很好');
                var width = score/2 + '%';
                password_label.innerHTML = "<span style='width:"+width+";display:block;overflow:hidden;height:20px;line-height:20px;background:"+color+";'>"+text+"</span>";
                if(score < 34){
                	$('#tj').attr('disabled','disabled'); 
                }else{
                	$('#tj').attr('disabled',''); 
                }
            }
        }
        
    }); */

$(function(){
	$('#passwordOK').hide();
	$('#password').hide();
});

$(function() {

	$('#yhmm').focus(function() {
		$('#pwdLevel_1').attr('class', 'strength1');
		$('#yhmm').keyup();
	});
});
$(function() {
	$('#yhmm').keyup(function() {
		var __th = $(this);

		if (!__th.val()) {
			$('#password').show();
			$('#passwordOK').hide();
			$('#tj').removeAttr("disabled");
			Primary();
			return;
		}
		if (__th.val().length < 8) {
			$('#tj').attr("disabled","disabled");
			$('#password').show();
			$('#passwordOK').hide();
			Weak();
			return;
		}
		var _r = checkPassword(__th);
		if (_r < 1) {
			$('#password').show();
			$('#passwordOK').hide();
			$('#tj').attr("disabled","disabled");
			Primary();
			return;
		}

		if (_r > 0 && _r < 2) {
			$('#password').show();
			$('#passwordOK').hide();
			$('#tj').attr("disabled","disabled");
			Weak();
		} else if (_r >= 2 && _r < 4) {
			$('#password').hide();
			$('#passwordOK').show();
			$('#tj').removeAttr("disabled");
			Medium();
		} else if (_r >= 4) {
			$('#password').hide();
			$('#passwordOK').show();
			$('#tj').removeAttr("disabled");
			Tough();	
		}

		
		
		
	});
});

function Primary() {
	$('#pwdLevel_1').attr('class', 'emptyColor');
	$('#pwdLevel_2').attr('class', 'emptyColor');
	$('#pwdLevel_3').attr('class', 'emptyColor');
}

function Weak() {
	$('#pwdLevel_1').attr('class', 'strength1');
	$('#pwdLevel_2').attr('class', 'emptyColor');
	$('#pwdLevel_3').attr('class', 'emptyColor');
}

function Medium() {
	
	$('#pwdLevel_1').attr('class', 'strength1');
	$('#pwdLevel_2').attr('class', 'strength2');
	$('#pwdLevel_3').attr('class', 'emptyColor');
}

function Tough() {
	$('#pwdLevel_1').attr('class', 'strength1');
	$('#pwdLevel_2').attr('class', 'strength2');
	$('#pwdLevel_3').attr('class', 'strength3');
}

function checkPassword(pwdinput) {
	var digital, lowercase, uppercase, symbol, cat, num;
	var str = $(pwdinput).val()
	var len = str.length;

	var cat = /.{16}/g
	if (len == 0)
		return 1;
	if (len > 16) {
		$(pwdinput).val(str.match(cat)[0]);
	}
	cat = /.*[\u4e00-\u9fa5]+.*$/
	if (cat.test(str)) {
		return -1;
	}
	cat = /\d/;
	var digital = cat.test(str);
	cat = /[a-z]/;
	var lowercase = cat.test(str);
	cat = /[A-Z]/;
	var uppercase = cat.test(str);
	var symbol = corpses(pwdinput);
	var num = digital + lowercase + uppercase + symbol;

	if (len < 8) {
		return 1;
	}

	if (len >= 8) {
		if (num <= 2)
			return 1;
		if (num == 3)
			return 2;
		if (num > 3)
			return 4;
	}
}

function corpses(pwdinput) {
	var cat = /./g
	var str = $(pwdinput).val();
	var sz = str.match(cat)
	for ( var i = 0; i < sz.length; i++) {
		cat = /\d/;
		digital01 = cat.test(sz[i]);
		cat = /[a-z]/;
		lowercase01 = cat.test(sz[i]);
		cat = /[A-Z]/;
		uppercase01 = cat.test(sz[i]);
		if (!digital01 && !lowercase01 && !uppercase01) {
			return true;
		}
	}
	return false;
}