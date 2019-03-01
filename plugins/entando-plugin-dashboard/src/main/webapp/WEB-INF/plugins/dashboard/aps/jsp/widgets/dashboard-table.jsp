<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>


<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>
<!-- <link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap.min.css" /> -->
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.5.2/css/buttons.dataTables.min.css"/>

<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js" crossorigin=""></script>
<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap.min.js" crossorigin=""></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/dataTables.buttons.min.js" crossorigin=""></script>

<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.flash.min.js" crossorigin=""></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js" crossorigin=""></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/pdfmake.min.js" crossorigin=""></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/vfs_fonts.js" crossorigin=""></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.html5.min.js" crossorigin=""></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.print.min.js" crossorigin=""></script>

<link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/table/table.css">
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/table/data/data.js"></script>
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/table/table.js"></script>

<wp:currentWidget param="config" configParam="config" var="configTable" />

        <script>
          $(document).ready(() => {
            console.log('jQuery ready Table');
            const config = ${configTable};
            console.log('config : ', config);
            const { title, columns, options } = config;
            $('#title-table').html(title.en);
             const CONFIG_TABLE = {
               options,
               columns,
               data: []
             }
             console.log('CONFIG_TABLE: ', CONFIG_TABLE);
             const table = new org.entando.dashboard.Table("table", CONFIG_TABLE);
             table.show();

          });
        </script>

        <div id="dashboard-table" class="container-fluid">
          <h3 id="title-table"></h3>
          <table id="table" class="table table-striped table-bordered" style="width:100%"></table>
        </div>
