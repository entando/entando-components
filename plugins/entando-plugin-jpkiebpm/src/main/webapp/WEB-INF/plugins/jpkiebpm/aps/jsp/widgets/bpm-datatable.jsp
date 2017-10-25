<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/jpkiebpm-aps-core" prefix="jpkie" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jpkie:datatable widgetConfigInfoIdVar="configId"/>
<c:set var = "id" value = "${configId}"/>

<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery.validate.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/additional-methods.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jpkie.css" media="screen"/>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/data-table.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/lib/dynamic-form/jquery.dform-1.1.0.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dynamic-form.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery.dataTables.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dataTables.buttons.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jszip.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/buttons.html5.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/buttons.colVis.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dataTables.responsive.min.js"></script>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jquery-ui.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/buttons.dataTables.min.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jquery.dataTables.min.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/responsive.dataTables.min.css" media="screen"/>

<script>
    $(document).ready(function () {
        var context = "<wp:info key="systemParam" paramName="applicationBaseURL" />api/rs/<wp:info key="currentLang"/>/jpkiebpm/";
        var url = context + "tasks.json?configId=${id}";
        //console.log(url);
        org.entando.datatable.loadDataTable(url, '#data-table-active', context,${id});
    });
</script>

<table id="data-table-active" class="display nowrap" cellspacing="0" width="100%"></table>

<div id="modal-bpm-data" >
    <table id="modal-bpm-data-table" class="table table-hover no-margins">
        <tbody id="modal-bpm-data-table-tbody"></tbody>
    </table>
</div>

<div id="modal-bpm-form"/>

<div id="modal-bpm-diagram">
    <img id="modal-bpm-diagram-data" class="img-responsive"/>
</div>
