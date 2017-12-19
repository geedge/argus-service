	(function($) {

		$.suggest = function(input, options) {
	
			var $input = $(input).attr("autocomplete", "off");
			var $results;
			var needSrcoll= false;

			var timeout = false;		// hold timeout ID for suggestion results to appear	
			var prevLength = 0;			// last recorded length of $input.val()
			var cache = [];				// cache MRU list
			var cacheSize = 0;			// size of cache in chars (bytes?)
			
			if($.trim($input.val())=='' || $.trim($input.val())=='中文/拼音') $input.val('').css('color','#aaa');
			if( ! options.attachObject )
				options.attachObject = $(document.createElement("ul")).appendTo('body');

			$results = $(options.attachObject);
			$results.addClass(options.resultsClass);
			
			resetPosition();
			$(window)
				.load(resetPosition)		// just in case user is changing size of page while loading
				.resize(resetPosition);
			
			displayItems('');//显示热门城市列表
			
			$input.blur(function() {
				setTimeout(function() { $results.hide() }, 200);
			});
			
			$input.focus(function(){
				/*if($.trim($(this).val())=='中文/拼音'){
					$(this).val('').css('color','#000');
				}*/
				if($.trim($(this).val())==''){
					displayItems('');//显示热门城市列表
				}
			});
			/*$input.click(function(){
				var q=$.trim($(this).val());
				displayItems(q);
				$(this).select();
			});*/
						
			// help IE users if possible
			try {
				$results.bgiframe();
			} catch(e) { }

			$input.keyup(processKey);//
			
			function resetPosition() {
				// requires jquery.dimension plugin
				var offset = $input.offset();
				$results.css({
					top: (offset.top + input.offsetHeight) + 'px',
					left: offset.left + 'px'
				});
			}
			
			
			function processKey(e) {
				
				// handling up/down/escape requires results to be visible
				// handling enter/tab requires that AND a result to be selected
				if ((/27$|38$|40$/.test(e.keyCode) && $results.is(':visible')) ||
					(/^13$|^9$/.test(e.keyCode) )) {
		            
		            if (e.preventDefault)
		                e.preventDefault();
					if (e.stopPropagation)
		                e.stopPropagation();

	                e.cancelBubble = true;
	                e.returnValue = false;
				
					switch(e.keyCode) {
	
						/*case 38: // up
							prevResult();
							break;
				
						case 40: // down
							nextResult();
							break;*/
						case 13: // return
							selectCurrentResult();
							break;
							
						case 27: //	escape
							$results.hide();
							break;
	
					}
					
				} else if ($input.val().length != prevLength) {

					if (timeout) 
						clearTimeout(timeout);
					timeout = setTimeout(suggest, options.delay);
					prevLength = $input.val().length;
					
				}			
					
				
			}
			
			function suggest() {
			
				var q = $.trim($input.val());
				displayItems(q);
			}		
			function displayItems(items) {
				var html = '';
				if (items=='') {//热门城市遍历
					for(h in options.hot_list){
						if(options.showType=="scroll"){
							html+='<li rel=""><a href="#'+h+'">'+options.hot_list[h]+'</a></li>';
							if(h>15){
								needSrcoll = true;
							}
						}else{
							if(h<=15){
								html+='<li rel=""><a href="#'+h+'">'+options.hot_list[h]+'</a></li>';
						   }else if(h>15){
						   		html+='<li rel=""><a href="#'+h+'">......</a></li>';
						   		break;
						   }
						}
					   
					}
					if(needSrcoll){
						html='<div class="ac_results_main"><div class="gray ac_result_tip">请输入内容</div><ul>'+html+'</ul></div>';
					}else{
						html='<div class="gray ac_result_tip">请输入内容</div><ul>'+html+'</ul>';
					}
					
				}
				else 
				{
					/*if (!items)
					return;
					if (!items.length) {
						$results.hide();
						return;
					}*/
					for (var i = 0; i < options.source.length; i++) {//国内城市匹配
						var reg = new RegExp('^' + items + '.*$', 'im');
						if (reg.test(options.source[i])) {
							
							if(options.source[i].charAt(0)=="<"&&options.source[i].charAt(options.source[i].length-1)==">")
							{
								options.source[i] = "&lt"+options.source[i].slice(1,options.source[i].length-1)+"&gt";
							}
							else if(options.source[i].charAt(0)=="<")
							{
								options.source[i] = "&lt"+options.source[i].slice(1);
							}
							else if(options.source[i].charAt(options.source[i].length-1)==">")
							{
								options.source[i] = options.source[i].slice(0,options.source[i].length-1)+"&gt";
							}
							
							if(i<=15)
							{
								html += '<li rel=""><a href="#' + i + '">' + options.source[i] + '</a></li>';
							}
							else 
							{
								html+='<li rel=""><a href="#'+i+'">......</a></li>';
					   			break;
							}
						}
					}
					if (html == '') {
						//suggest_tip = '<div class="gray ac_result_tip">对不起，找不到：' + items + '</div>';
						
						
						html = 	'<ul>' + html + '</ul>';
					}
					else {
						suggest_tip = '<div class="gray ac_result_tip">' + items + '&nbsp;&nbsp;&nbsp;&nbsp;为输入的内容</div>';
						html = suggest_tip + '<ul>' + html + '</ul>';
					}
					
				}

				$results.html(html).show();
				if(needSrcoll){
					$results.children('div').children('ul').children('li:first-child').addClass(options.selectClass);
				
					$results.children('div').children('ul')
						.children('li')
						.mouseover(function() {
							$results.children('div').children('ul').children('li').removeClass(options.selectClass);
							$(this).addClass(options.selectClass);
						})
						.click(function(e) {
							e.preventDefault(); 
							e.stopPropagation();
							selectCurrentResult();
						});
				}else{
					$results.children('ul').children('li:first-child').addClass(options.selectClass);
				
					$results.children('ul')
						.children('li')
						.mouseover(function() {
							$results.children('ul').children('li').removeClass(options.selectClass);
							$(this).addClass(options.selectClass);
						})
						.click(function(e) {
							e.preventDefault(); 
							e.stopPropagation();
							selectCurrentResult();
						});
				}
				
			}
						
			function getCurrentResult() {
			
				if (!$results.is(':visible'))
					return false;
				
				var $currentResult = $results.children('ul').children('li.' + options.selectClass);
				if(needSrcoll){
					$currentResult = $results.children('div').children('ul').children('li.' + options.selectClass);
				}
				
				if (!$currentResult.length)
					$currentResult = false;
				
				return $currentResult;

			}
			
			function selectCurrentResult() {
			
				$currentResult = getCurrentResult();
			
				if ($currentResult) {
					$input.val($currentResult.children('a').text());
					
					$results.hide();

					if( $(options.dataContainer) ) {
						$(options.dataContainer).val($currentResult.attr('rel'));
					}
	
					if (options.onSelect) {
						options.onSelect.apply($input[0]);
					}
				}
			
			}
			
			function nextResult() {
			
				$currentResult = getCurrentResult();
			
				if ($currentResult)
					$currentResult
						.removeClass(options.selectClass)
						.next()
							.addClass(options.selectClass);
				else
					$results.children('ul').children('li:first-child').addClass(options.selectClass);
			
			}
			
			function prevResult() {
			
				$currentResult = getCurrentResult();
			
				if ($currentResult)
					$currentResult
						.removeClass(options.selectClass)
						.prev()
							.addClass(options.selectClass);
				else
					$results.children('ul').children('li:last-child').addClass(options.selectClass);
			alert($currentResult+":1");
			}
	
		}
		
		$.fn.suggest = function(source, options) {
		
			if (!source)
				return;
		
			options = options || {};
			options.source = source;
			options.hot_list=options.hot_list || [];
			options.delay = options.delay || 0;
			options.resultsClass = options.resultsClass || 'ac_results';
			options.selectClass = options.selectClass || 'ac_over';
			options.matchClass = options.matchClass || 'ac_match';
			options.minchars = options.minchars || 1;
			options.delimiter = options.delimiter || '\n';
			options.onSelect = options.onSelect || false;
			options.dataDelimiter = options.dataDelimiter || '\t';
			options.dataContainer = options.dataContainer || '#SuggestResult';
			options.attachObject = options.attachObject || null;
			options.showType = options.showType || null;
			
	
			this.each(function() {
				new $.suggest(this, options);
			});
	
			return this;
			
		};
		
	})(jQuery);