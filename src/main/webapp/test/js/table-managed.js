var TableManaged = function () {

    var initTable2 = function () {
        var table = $('#sample_4');
        table.find('.group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).attr("checked", true);
                } else {
                    $(this).attr("checked", false);
                }
            });
            jQuery.uniform.update(set);
        });
        table.find('input[type=checkbox]').change(function () {
        	 var checked = jQuery(this).is(":checked");
        	 $(this).attr("checked", checked);
        });
    }

    return {

        //main function to initiate the module
        init: function () {
            if (!jQuery().dataTable) {
                return;
            }

            //initTable1();
            initTable2();
            //initTable3();
        }

    };

}();