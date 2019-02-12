<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="https://https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap.min.css" />

<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js" crossorigin=""></script>
<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap.min.js" crossorigin=""></script>

  <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/table/table.css">
  <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/table/data/data.js"></script>
  <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/table/table.js"></script>


<script>



    $(document).ready(() => {
        console.log('jQuery ready');
        console.log('CONFIG_DATA ', CONFIG_DATA);
        const table = new org.entando.dashboard.Table("table", CONFIG_DATA.DATASOURCE_PAYLOAD);
        table.show();
    });

</script>
<div id="dashboard-table" class="container-fluid">
    <table id="table" class="table table-striped table-bordered" style="width:100%" >
    </table>


</div>
