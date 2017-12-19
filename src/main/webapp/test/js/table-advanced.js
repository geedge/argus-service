var TableAdvanced = function () {
    var initTable4 = function () {
        var table = $('#sample_4');
        var oTable = table.dataTable({
            // Internationalisation. For more info refer to http://datatables.net/manual/i18n
        	"bSort":true,
        	"bPaginate":false,
            "language": {
                "emptyTable": "No data available in table",
                "info": "",
                "infoEmpty": "",
                "infoFiltered": "",
                "lengthMenu": "",
                "search": "",
                "zeroRecords": ""
            },
        });
        
        var tableWrapper = $('#sample_4_wrapper'); // datatable creates the table wrapper by adding with id {your_table_jd}_wrapper
        var tableColumnToggler = $('#sample_4_column_toggler');
        /* modify datatable control inputs */
        //tableWrapper.find('.dataTables_length select').select2(); // initialize select2 dropdown

        /* handle show/hide columns*/
        $('input[type="checkbox"]', tableColumnToggler).change(function () {
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
    }
    var initTable5 = function () {

        var table = $('#sample_5');

        /* Fixed header extension: http://datatables.net/extensions/scroller/ */

        var oTable = table.dataTable({
            "dom": "<'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>", // datatable layout without  horizobtal scroll
            "scrollY": "300",
            "deferRender": true,
            "bSort":true,
        	"bPaginate":false,
            "order": [
                [0, 'asc']
            ],
            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],
            "pageLength": 10, // set the initial value   
            "bLengthChange":false,
            "info":false
        });


        var tableWrapper = $('#sample_5_wrapper'); // datatable creates the table wrapper by adding with id {your_table_jd}_wrapper
        tableWrapper.find('.dataTables_length select').select2(); // initialize select2 dropdown
    }

    return {
        //main function to initiate the module
        init: function () {

            if (!jQuery().dataTable) {
                return;
            }
			//show/hide colunm function
            initTable4();
            initTable5();
        }

    };

}();