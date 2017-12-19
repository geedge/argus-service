$.extend( true, $.fn.dataTable.defaults, {
	"sPaginationType": "bootstrap",
			"oLanguage": {
				"oAria":{
              		"sSortAscending": " - click/return to sort ascending",
             		"sSortDescending": " - click/return to sort descending"
          		},
          		"sLengthMenu":'每页显示 <select>'+
				            '<option value="10">10</option>'+
				             '<option value="20">20</option>'+
				             '<option value="30">30</option>'+
				             '<option value="50">50</option>'+
				             '<option value="-1">显示所有数据</option>'+
				             '<option value="0">不显示数据</option>'+
				             '</select> 条记录', 
				"sZeroRecords": "对不起，查询不到任何相关数据", 
				"sEmptyTable":"没有相关数据",
				"sLoadingRecords":"正在加载数据-请等待...",
				"sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录。", 
				"sInfoEmpty": "当前显示0到0条，共0条记录", 
				"sInfoFiltered": "（数据库中共为 _MAX_ 条记录）", 
				"sProcessing": " 正在加载数据...", 
				"sSearch": "模糊查询：", 
				"oPaginate": { 
					"sPrevious": "上一页", 
					"sNext": "下一页"
				} 
			}, //多语言配置
			"bJQueryUI": false,//可以添加 jqury的ui theme 需要添加css
			"iDisplayStart":0,
			"bPaginate" : true,//是否显示每页条数下拉框
			"aLengthMenu": [10, 20,30,50, -1, 0],//设置每页显示记录的下拉菜单
			"bProcessing": true,//载入时，显示进度表示
			"bAutoWidth": false, //自适应宽度 
		
			"iDisplayLength":10,
			"bSort":false,//是否支持排序功能
			"sServerMethod":"post",
			"sZeroRecords":"无数据，请选择重新选择查询条件"
} );

