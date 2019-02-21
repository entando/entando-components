<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="dt" uri="/dashboard-core" %>

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

<dt:currentConfigDashboardTable param="config" var="configTable"/>

<script>
    $(document).ready(() => {
        console.log('jQuery ready');
        const config = "${configTable}".replace(new RegExp("=", "g"), ':');
        console.log('config : ', config);
        const configPropertyToJson = (config, property, type) => {
            const obj = type ==='array' ? [] : {};
            return config.split(',')
                .filter(f => f.includes(property))
                .reduce((acc, item) => {
                    const data = item.split('.');
                    el = data[1].split(':');
                    type === 'array' ?
                        acc.push({key: el[0], value: el[1]}) :
                        acc = {...acc,[el[0]]: el[1]}
                    return acc;
                }, obj);
        };

        const options = configPropertyToJson(config,'options','object');
        const columns = configPropertyToJson(config,'columns','array');
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
    <table id="table" class="table table-striped table-bordered" style="width:100%"></table>
</div>
