<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/jpkiebpm-aps-core" prefix="jpkie" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jpkie:datatable widgetConfigInfoIdVar="configId"/>
<c:set var="id" value="${configId}"/>

<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery.validate.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/additional-methods.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jpkie.css" media="screen"/>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/custom-data-table.js"></script>
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
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/responsive.dataTables.min.css"
      media="screen"/>

<script>

    function addRow(data, label, index) {
        var html = "<tr><td>" + label + "</td><td>" + data[index] + "</td></tr>"
        return data[index] === undefined ? "" : html;
    }

    function getHtmlListFragment(src, label) {
        if (!src) {
            return '';
        }
        var html = "<ul>";
        if (Array.isArray(src)) {
            src.forEach(function (value) {
                html += "<li>" + value + "</li>";
            });
        }
        else if (Array.isArray(src[label])) {
            src[label].forEach(function (value) {
                html += "<li>" + value + "</li>";
            });
        }
        else {
            html += "<li>" + (src[label] === undefined ? "" : src[label]) + "</li>";
        }
        html += "</ul>"
        return html;
    }

    function getTemplateTaskDetail(data) {
        var template =
            "<tr><td>Task id</td><td>" + data['task-id'] + "</td></tr>\n" +
            "<tr><td>Task name</td><td>" + data['task-name'] + "</td></tr>\n" +
            "<tr><td>Task form</td><td>" + data['task-form'] + "</td></tr>\n" +
            "<tr><td>Task priority</td><td>" + data['task-priority'] + "</td></tr>\n" +
            "<tr><td>Task status</td><td>" + data['task-status'] + "</td></tr>\n" +
            "<tr><td>Actual Owner</td><td>" + data['task-actual-owner'] + "</td></tr>\n" +
            "<tr><td>Created by</td><td>" + data['task-created-by'] + "</td></tr>\n" +
            "<tr><td>Created On</td><td>" + data['task-created-on'] + "</td></tr>\n" +
            "<tr><td>Expiration Time</td><td>" + data['task-expiration-time'] + "</td></tr>\n" +
            "<tr><td>Skippable</td><td>" + data['task-skippable'] + "</td></tr>\n" +
            "<tr><td>Workitem Id</td><td>" + data['task-workitem-id'] + "</td></tr>\n" +
            "<tr><td>Process Instance</td><td>" + data['task-process-instance-id'] + "</td></tr>\n" +
            "<tr><td>Process Definition</td><td>" + data['task-process-id'] + "</td></tr>\n" +
            "<tr><td>Parent Id</td><td>" + data['task-parent-id'] + "</td></tr>\n" +
            "<tr><td>Deployment Unit</td><td>" + data['task-container-id'] + "</td></tr>\n" +
            "<tr><td>Potential Owners</td><td>" + getHtmlListFragment(data['potential-owners'], 'task-pot-owners') + "</td></tr>\n" +
            "<tr><td>Excluded Owner</td><td>" + getHtmlListFragment(data['excluded-owners'], 'task-exc-owners') + "</td></tr>\n" +
            "<tr><td>Business Admin</td><td>" + getHtmlListFragment(data['business-admins'], 'task-business-admins') + "</td></tr>";
        return template;
    }


    var optModal = {
        //appendTo: "#data-table-active",
        minWidth: 500,
        modal: true,
        show: {effect: "fadego", duration: 800},
        resizable: true,
        position: {my: "center top", at: "center top+10%"}
    }


    /* Modal Form*/
    function openModalForm(event, rowData, context) {

        var url = context + "taskForm.json?containerId=" + rowData.containerId + "&taskId=" + rowData.id;

        $.get(url, function (data) {
            $('#bpm-task-list-modal-form').empty();
            var jsonKie = data.response.result;
            jsonKie.mainForm.method = "post";
            org.entando.form.dynamicForm = new org.entando.form.DynamicForm(jsonKie);
            //org.entando.form.dynamicForm.json.html[0].value = processId;
            $("#bpm-task-list-modal-form").dform(org.entando.form.dynamicForm.json);
            optModal.title = "BPM Form Data";
            $('#bpm-task-list-modal-form').dialog(optModal);

            $('form.ui-dform-form').submit(function (event1) {
                event1.preventDefault();
                var postData = {
                    task: {
                        fields: []
                    }
                };
                $('form.ui-dform-form').serializeArray().forEach(function (el) {
                    var entry = {name: el.name, value: el.value};
                    postData.task.fields.push(entry);
                });
                postData.task.fields.push({name: "processId", value: rowData.processDefinitionId});
                postData.task.fields.push({name: "containerId", value: rowData.containerId});
                postData.task.fields.push({name: "taskId", value: rowData.id});
                var action = context + "taskForm.json";
                //console.log(postData);

                $('form.ui-dform-form checkbox')

                $.ajax({
                    url: action,
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify(postData),
                    dataType: 'json'
                }).done(function (result) {
                    if (result.response.result = "SUCCESS") {
                        $("#bpm-task-list-modal-form").dialog("close");
                        $('#bpm-task-list-modal-form').empty();
                        //row.remove().draw(false);
                        var url = context + "tasks.json?configId=" + configId;
                        $.get(url, function (data) {
                            json.data = getJsonData(data.response.result);
                            table.clear();
                            table.rows.add(json.data);
                            table.draw();
                        });

                    }
                });

            })
        });

    }


    $(document).ready(function () {


        var loadDataTable = function (url, idTable, extraConfig) {

            $.get(url, function (data) {
                var items = data.response.result.taskList.list || [];
                items = Array.isArray(items) ? items : [items];
                items = items.map(function (item) {
                    item['activated'] = new Date(item['activated']).toLocaleString();
                    item['created'] = new Date(item['created']).toLocaleString();
                    return item;
                });
                extraConfig.columnDefinition = data.response.result.taskList["datatable-field-definition"].fields;
                org.entando.datatable.CustomDatatable(items, idTable, extraConfig);
            });
        };

        var configId = "${id}";

        var context = "<wp:info key="systemParam" paramName="applicationBaseURL" />legacyapi/rs/<wp:info key="currentLang"/>/jpkiebpm/";
        var url = context + "tasks.json?configId=${id}";
        //console.log(url);
        //org.entando.datatable.loadDataTable(url, '#data-table-active', context,${id});


        var extraBtns = [
            {
                html: '<button type="button" class="class-open-bpm-task-list-modal-form-details btn btn-success btn-sm" style="margin-right:10px;">Complete</button>',
                onClick: function (ev, data) {

                    openModalForm(ev, data, context);
                }
            },
            {
                html: '<button type="button" class=" class-open-bpm-task-list-modal-diagram-details btn btn-info btn-sm ">Diagram</button>',
                onClick: function (event, rowData) {

                    var url = context + "diagram.json?configId=" + configId + "&processInstanceId=" + rowData.processInstanceId;
                    $('#bpm-task-list-modal-diagram-data').empty();
                    $.get(url, function (data) {
                        $('#bpm-task-list-modal-diagram-data').attr("src", "data:image/svg+xml;utf8," + data.response.result);
                        optModal.title = "BPM Process Diagram";
                        optModal.show.effect = "fold";
                        optModal.position = {my: "center", at: "center"};
                        $('#bpm-task-list-modal-diagram').dialog(optModal);
                    });

                }
            }
        ];

        var extraConfig = {
            buttons: extraBtns,
            onClickRow: function (ev, rowData) {
                $('#bpm-task-list-modal-data-table-tbody').empty();
                var url = context + "taskDetail.json?containerId=" + rowData.containerId + "&taskId=" + rowData.id;
                console.log(url);
                $.get(url, function (data) {
                    $('#bpm-task-list-modal-data-table-tbody').append(getTemplateTaskDetail(data.response.result.mainForm));
                    optModal.title = "BPM Data";
                    $('#bpm-task-list-modal-data').dialog(optModal);
                });
            }
        };


        loadDataTable(url, '#data-table-task-list', extraConfig);
    });
</script>

<table id="data-table-task-list" class="display nowrap" cellspacing="0" width="100%"></table>

<div id="bpm-task-list-modal-data">
    <table id="bpm-task-list-modal-data-table" class="table table-hover no-margins">
        <tbody id="bpm-task-list-modal-data-table-tbody"></tbody>
    </table>
</div>

<div id="bpm-task-list-modal-form"/>

<div id="bpm-task-list-modal-diagram">
    <img id="bpm-task-list-modal-diagram-data" class="img-responsive"/>
</div>