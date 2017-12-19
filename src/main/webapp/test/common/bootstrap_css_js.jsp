<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="/jstl/c"%>
<%@ taglib prefix="fn" uri="/jstl/fn"%>
<%@ taglib prefix="fmt" uri="/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>




<!-- start: CSS -->
<!-- <link id="bootstrap-style" href="<c:url value='/test/css/bootstrap.min.css'/>"  rel="stylesheet"> -->
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link
	href="<c:url value='/test/css/font-awesome/css/font-awesome.min.css'/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/test/css/simple-line-icons/simple-line-icons.min.css'/>"
	rel="stylesheet" type="text/css">
<%-- <link href="<c:url value='/test/css/bootstrap/css/bootstrap.min.css'/>" --%>
<!-- 	rel="stylesheet" type="text/css"> -->

<link href="<c:url value='/test/page/css/bootstrap.min.css'/>"
	rel="stylesheet" type="text/css">



<link href="<c:url value='/test/css/uniform/css/uniform.default.css'/>"
	rel="stylesheet" type="text/css">

<link
	href="<c:url value='/test/css/bootstrap/css/daterangepicker-bs3.css'/>"
	rel="stylesheet" type="text/css">


<!-- END GLOBAL MANDATORY STYLES -->
<link
	href="<c:url value='/test/css/bootstrap-datepicker/css/datepicker3.css'/>"
	rel="stylesheet" type="text/css" />
<!-- BEGIN THEME STYLES -->
<!-- DOC: To use 'rounded corners' style just load 'components-rounded.css' stylesheet instead of 'components.css' in the below style tag -->
<link href="<c:url value='/test/css/components.css'/>"
	id="style_components" rel="stylesheet" type="text/css">
<link href="<c:url value='/test/css/plugins.css'/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value='/test/css/layout.css'/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value='/test/css/default.css'/>" rel="stylesheet"
	type="text/css" id="style_color">
<link href="<c:url value='/test/css/custom.css'/>" rel="stylesheet"
	type="text/css">
<!-- END THEME STYLES -->
<link rel="stylesheet" type="text/css"
	href="<c:url value='/test/css/datatables/plugins/bootstrap/dataTables.bootstrap.css'/>" />
<!-- end: CSS -->
<link rel="stylesheet"
	href="<c:url value='/test/css/bootstrap-select/bootstrap-select.css'/>"
	type="text/css"></link>


<link
	href="<c:url value='/test/css/bootstrap-tagsinput/bootstrap-tagsinput.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<c:url value='/test/css/select2/css/select2.min.css'/>"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value='/test/css/select2/css/select2-bootstrap.min.css'/>"
	rel="stylesheet" type="text/css" />



</head>
<body>

	<!-- start: JavaScript-->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
		<script src="<c:url value='/test/js/respond.min.js'/>"></script>
		<script src="<c:url value='/test/js/excanvas.min.js'/>"></script> 
		<![endif]-->
	<script src="<c:url value='/test/js/jquery.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/jquery-migrate.min.js'/>"
		type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script
		src="<c:url value='/test/js/jquery-ui/jquery-ui-1.10.3.custom.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/bootstrap/js/bootstrap.min.js'/>"
		type="text/javascript"></script>
	<script
		src="<c:url value='/test/js/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js'/>"
		type="text/javascript"></script>
	<script
		src="<c:url value='/test/js/jquery-slimscroll/jquery.slimscroll.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/jquery.blockui.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/jquery.cokie.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/uniform/jquery.uniform.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/jquery.sparkline.min.js'/>"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<script src="<c:url value='/test/js/components-pickers.js'/>"
		type="text/javascript"></script>
	<script
		src="<c:url value='/test/js/bootstrap-datepicker/js/bootstrap-datepicker.js'/>"
		type="text/javascript"></script>
	<script
		src="<c:url value='/test/js/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/test/js/datatables/media/js/jquery.dataTables.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/test/js/datatables/extensions/TableTools/js/dataTables.tableTools.min.js'/>"></script>
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="<c:url value='/test/js/metronic.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/layout.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/demo.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/test/js/table-advanced.js'/>"></script>
	<script src="<c:url value='/test/js/table-managed.js'/>"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<script type="text/javascript"
		src="<c:url value='/test/js/bootstrap-select/bootstrap-select.js'/>"></script>
	<script
		src="<c:url value='/test/js/bootstrap-menu/BootstrapMenu.min.js'/>"
		type="text/javascript"></script>

	<script
		src="<c:url value='/test/css/bootstrap-tagsinput/bootstrap-tagsinput.min.js'/>"
		type="text/javascript"></script>

	<script
		src="<c:url value='/test/css/select2/js/select2.full.min.js" type="text/javascript'/>"></script>
	<script
		src="<c:url value='/test/css/scripts/app.min.js" type="text/javascript'/>"></script>
	<script
		src="<c:url value='/test/css/scripts/components-select2.min.js" type="text/javascript'/>"></script>
	<script src="<c:url value='/test/js/bootstrap/js/moment.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/test/js/bootstrap/js/daterangepicker.js'/>"
		type="text/javascript"></script>


	<script>
		jQuery(document).ready(function() {
			Metronic.init(); // init metronic core components
			Layout.init(); // init current layout
			Demo.init(); // init demo features
			TableAdvanced.init();// init table advanced
			TableManaged.init();
			ComponentsPickers.init();
		});
	</script>
	<!-- end: JavaScript-->

</body>
</html>
