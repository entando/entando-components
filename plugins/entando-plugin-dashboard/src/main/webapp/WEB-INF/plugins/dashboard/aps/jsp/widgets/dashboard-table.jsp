<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>


<link rel="stylesheet" href="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/css/jquery.dataTables.min.css"/>
<!-- <link rel="stylesheet" href="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/css/dataTables.bootstrap.min.css" /> -->
<link rel="stylesheet" href="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/css/buttons.dataTables.min.css"/>

<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/jquery.dataTables.min.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/dataTables.bootstrap.min.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/dataTables.buttons.min.js" crossorigin=""></script>

<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/buttons.flash.min.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/jszip.min.js" crossorigin=""></script>

<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/pdfmake.min.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/vfs_fonts.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/buttons.html5.min.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/buttons.print.min.js" crossorigin=""></script>

<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/Italian.js" crossorigin=""></script>
<script src="<wp:resourceURL/>plugins/dashboard/static/js/library/datatable/1.10.19/js/English.js" crossorigin=""></script>

<link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/table/table.css">
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/table/table.js"></script>

<wp:currentWidget param="config" configParam="config" var="configTable" />

<script>
  $(document).ready(() => {
    console.log('jQuery ready Table');
    const config = ${configTable};
    const lang = '<wp:info key="currentLang" />';
    console.log('config : ', config);
    const { title, columns, options } = config;
    const orderedColumns = {};
    Object.keys(columns)
      .sort((a,b)=> columns[a].order - columns[b].order)
      .forEach((colName)=>{
        orderedColumns[colName] = columns[colName]
      });
    $('#title-table').html(title.en);
    const CONFIG_TABLE = {
      serverName: config.serverName,
      datasource: config.datasource,
      accessToken: '<c:out value="${sessionScope.currentUser.accessToken}"/>',
      options,
      columns : orderedColumns,
      lang
    };
    console.log('CONFIG_TABLE: ', CONFIG_TABLE);
    const context = "<wp:info key="systemParam" paramName="applicationBaseURL"/>";
    new org.entando.dashboard.Table(context, "table", CONFIG_TABLE,).show();
  });
</script>
<div id="dashboard-table" class="container-fluid">
    <h3 id="title-table"></h3>
  <table id="table" class="table table-striped table-bordered" style="width:100%"></table>
</div>
