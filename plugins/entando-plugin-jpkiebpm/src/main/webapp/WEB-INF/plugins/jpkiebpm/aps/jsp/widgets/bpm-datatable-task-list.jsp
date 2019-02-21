<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/jpkiebpm-aps-core" prefix="jpkie" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jpkie:datatable widgetConfigInfoIdVar="configId"/>
<c:set var="id" value="${configId}"/>

<jpkie:tasklistwidgetinfo redirectOnClickRowVar="redirectOnClickRowVar"/>
<jpkie:tasklistwidgetinfo redirectDetailsPageVar="redirectDetailsPageVar"/>
<jpkie:tasklistwidgetinfo showClaimButtonVar="showClaimButtonVar"/>
<jpkie:tasklistwidgetinfo showCompleteButtonVar="showCompleteButtonVar"/>

<script src="<wp:resourceURL />administration/js/jquery-2.2.4.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery.validate.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/additional-methods.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jpkie.css" media="screen"/>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/custom-data-table.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/lib/dynamic-form/jquery.dform-1.1.0.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dynamic-form.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery.dataTables.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jszip.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/buttons.html5.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/buttons.colVis.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dataTables.responsive.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dataTables.fixedColumns.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/dataTables.buttons.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/lib/jquery-toast/jquery.toast.min.js"></script>

<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jquery-ui.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/buttons.dataTables.min.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jquery.dataTables.min.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/responsive.dataTables.min.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/fixedColumns.dataTables.min.css" media="screen"/>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jquery.toast.min.css" media="screen"/>
<script>            
    
    function capitalize(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
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
        } else if (Array.isArray(src[label])) {
            src[label].forEach(function (value) {
                html += "<li>" + value + "</li>";
            });
        } else {
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

    function getTemplateTaskData(data) {
        var length = data.fields.fieldset.field.length;
        var fields = data.fields.fieldset.field;
        var template = "<tr>zz";
        for (i = 0; i < length; i++) {
            template += "<td>" + capitalize(fields[i].name) + "<br/>" + fields[i].value + "</td>\n";
        }
        template +='</tr>';
        return template;
    }

    var windowWidth = $(window).width();
    var windowHeight = $(window).height();

    var optModal = {
        //appendTo: "#data-table-active",
        width: windowWidth - 100,
        height: windowHeight - 100,
        modal: true,
        show: {effect: "fadego", duration: 800},
        resizable: true,
        position: {my: "center", at: "center", of: window}

    }


    /* Modal Form*/
    function openModalForm(event, configId, rowData, context) {

        var url = context + "taskForm.json?configId=" + configId + "&containerId=" + rowData.containerId + "&taskId=" + rowData.id;
        $.get(url, function (data) {
            $('#bpm-task-list-modal-form').empty();
            var jsonKie = data.response.result;
            jsonKie.mainForm.method = "post";
            org.entando.form.dynamicForm = new org.entando.form.DynamicForm(jsonKie);
            //org.entando.form.dynamicForm.json.html[0].value = processId;
            $("#bpm-task-list-modal-form").dform(org.entando.form.dynamicForm.json);

            var urlTaskData = context + "taskData.json?configId=" + configId + "&containerId=" + rowData.containerId + "&taskId=" + rowData.id;
            appendTaskData(urlTaskData);

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
                postData.task.fields.push({name: "configId", value: configId});
                var action = context + "taskForm.json";

                $.ajax({
                    url: action,
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify(postData),
                    dataType: 'json',
                    success: function (data) {
                        $("#bpm-task-list-modal-form").dialog("close");
                        refreshDataTable();
                        return data;
                    },
                    error: function () {
                        //console.log('Error');
                    }
                });
            });
        });
    };
    
    
    
    
    
    function openDetailsPage(containerId, taskId)
    {
        var context = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
        var configId =${configId};
        var urlNewPage = context + "<wp:info key="currentLang" />/<c:out value="${redirectDetailsPageVar}" />.page?configId=" + configId + "&containerId=" + containerId + "&taskId=" + taskId;
        window.open(urlNewPage);
    }
    
    function refreshDataTable() {
        $("#data-table-task-list").empty();
        loadDataTable('#data-table-task-list');
    };

    function appendTaskData(urlTaskData){
        $("#bpm-task-data").empty();

                $.when(
                $.get(urlTaskData, function (data) {
                    var jsonKieTaskData = data.response.result;
                    jsonKieTaskData.mainForm.method = "none";
                    org.entando.form.dynamicFormInfo = new org.entando.form.DynamicForm(jsonKieTaskData);
                    $("#bpm-task-data").dform(org.entando.form.dynamicFormInfo.json);
                })
                ).done(
                function(){
                    var formFields = $("#bpm-task-data form").children().detach();
                    var taskDataTitle="<br/><br/><h2 id=\"task-data-title\"> Task data </h2>";
                    $('#bpm-task-list-modal-form').append(taskDataTitle);
                    formFields.appendTo('#bpm-task-list-modal-form');
                });
    }

    function claimTask(ev, configId, dataTaskId, context){
            var configId =${configId};
            //alert("configId "+configId+ " containerId: "+rowData.containerId + " taskId "+ rowData.id);

                ev.preventDefault();
                var postData = {
                    claimTask: {
                        taskId: dataTaskId,
                        configId:  configId
                    }
                };
                
               // alert("postData: "+JSON.stringify(postData));
                
                var action = context + "claimTask.json";

                $.ajax({
                    url: action,
                    type: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify(postData),
                    dataType: 'json',
                    success: function (data) {
                        $.toast({
                            heading: 'Success',
                            text: 'Task '+dataTaskId+ ' claimed successfully!',
                            showHideTransition: 'slide',
                            icon: 'success',
                            allowToastClose : true,
                            hideAfter : 5000,
                            stack : 5,
                            textAlign : 'left', 
                            position : 'top-right'
                        });
                        refreshDataTable();
                        return data;
                    },
                    error: function () {
                        $.toast({
                            heading: 'Error',
                            text: 'Failed to claim task '+dataTaskId+ ' ',
                            showHideTransition: 'fade',
                            icon: 'error',
                            allowToastClose : true,
                            hideAfter : 5000,
                            stack : 5,
                            textAlign : 'left', 
                            position : 'top-right'
                        })                       
                       
                    }
                });
    
    }

    function  loadDataTable(idTable) {
    var configId =${configId};
            var context = "<wp:info key="systemParam" paramName="applicationBaseURL" />legacyapi/rs/<wp:info key="currentLang"/>/jpkiebpm/";
            var url = context + "tasks.json?configId=${id}";
            var extraBtns = [
            <c:if test="${showClaimButtonVar == 'true'}">
            {
                html: '<button type="button" class="class-open-bpm-task-list-modal-form-details btn btn-success btn-sm" style="margin-right:10px;">Claim</button>',
                    onClick: function (ev, data) {
                        
                        if (data.status=='Ready'){
                            claimTask(ev, configId, data.id, context);
                        }
                        else {
                            $.toast({
                                heading: 'Information',
                                text: 'Task with '+data.id +' is already claimed. Only tasks in Ready can be claimed.',
                                showHideTransition: 'slide',
                                icon: 'info',
                                allowToastClose : true,
                                hideAfter : 5000,
                                stack : 5,
                                textAlign : 'left', 
                                position : 'top-right'
                            });
                        }
                    }
            },
            </c:if>                        
            <c:if test="${showCompleteButtonVar == 'true'}">
            {
                html: '<button type="button" class="class-open-bpm-task-list-modal-form-details btn btn-success btn-sm" style="margin-right:10px;">Complete</button>',
                    onClick: function (ev, data) {
                        openModalForm(ev, configId, data, context);
                    }
            },
            </c:if>            
            {
            html: '<button type="button" class=" class-open-bpm-task-list-modal-diagram-details btn btn-info btn-sm ">Diagram</button>',
                    onClick: function (event, rowData) {
                    var url = context + "diagram.json?configId=" + configId + "&processInstanceId=" + rowData.processInstanceId;
                        $('#bpm-task-list-modal-diagram-data').attr("src","");
                            $.get(url, function (data) {
                            $.when(
                                    $('#bpm-task-list-modal-diagram-data').attr("src", "data:image/svg+xml;utf8," + encodeURIComponent(data.response.result))).done(
                                function(){
                                    optModal.title = "BPM Process Diagram";
                                            optModal.show.effect = "fold";
                                            $('#bpm-task-list-modal-diagram').dialog(optModal);
                                    });
                            });
                    }
            }
            ];


            var extraConfig = {
                  <c:if test="${redirectOnClickRowVar == 'true'}">
                    buttons: extraBtns,
                        onClickRow: function (event, rowData) {
                        // Click Row details code
                        // $("#data-table-task-list tbody").on('click', 'tr td:not(:first-child)',function () {
                        openDetailsPage(rowData.containerId , rowData.id);                    
                        }
                    </c:if>
                    <c:if test="${redirectOnClickRowVar == 'false'}">
                    buttons: extraBtns,
                        onClickRow: function (ev, rowData) {
                            $('#bpm-task-list-modal-data-table-tbody').empty();

                            var url = context + "taskDetail.json?configId=" + configId +"&containerId=" + rowData.containerId + "&taskId=" + rowData.id;
                            $.get(url, function (data) {
                                $('#bpm-task-list-modal-data-table-tbody').append(getTemplateTaskDetail(data.response.result.mainForm));
                                optModal.title = "BPM Data";
                                $('#bpm-task-list-modal-data').dialog(optModal);
                                });
                            }                        
                    </c:if>                
               };
               
                $.get(url, function (data) {         
                    var items = data.response.result.taskList.list || [];
                        items = Array.isArray(items) ? items : [items];
                        items = items.map(function (item) {
                        item['activated'] = new Date(item['activated']).toLocaleString();
                        item['created'] = new Date(item['created']).toLocaleString();
                            const reduceKeyValuePairs = pairs => pairs.reduce((acc, pair) => ({
                                    ...acc,
                                    [pair.key]: pair.value,
                        }), {});
                            const kvpWithEntryField = {
                                    ...item,
                            ...reduceKeyValuePairs(item.processVariables.entry)
                        };
                            const {
                                    processVariables,
                            ...dest
                        } = kvpWithEntryField;
                            return dest;
                        });
                        var containerId = data.response.result.taskList.containerId;
                        extraConfig.columnDefinition = data.response.result.taskList["datatable-field-definition"].fields;
                        org.entando.datatable.CustomDatatable(items, idTable, extraConfig, containerId);                                    
                });
                

        };

<%--
// Click Row details code
/* Formatting function for row details*/
function format ( rowData ) {
                var context = "<wp:info key="systemParam" paramName="applicationBaseURL" />legacyapi/rs/<wp:info key="currentLang"/>/jpkiebpm/";

    var configId =${configId};
    var taskData='';
    var taskDataTable='';
    var urlTaskData = context + "taskData.json?configId=" + configId + "&containerId=" + rowData.containerId + "&taskId=" + rowData.id;

    $.ajax({
           url: urlTaskData,
           type: 'get',
           contentType: 'application/json',
           async: false,
           timeout: 30000,
           dataType: 'json',
           success: function (data) {
               taskData=getTemplateTaskData(data.response.result.mainForm);
               taskDataTable='<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+taskData+'</table>';                       
           },
           error: function () {
               console.log('Error reading taskData for id'+ rowData.id);
           }
       }); 
      
    return taskDataTable;
}
--%>
 
$(document).ready(function () {
    loadDataTable('#data-table-task-list');
});
    
</script>
<%-- 
<div class="showHideButtons">
    <button id="btn-show-all-children" type="button">Expand All</button>
    <button id="btn-hide-all-children" type="button">Collapse All</button>
</div>
--%>

<table id="data-table-task-list" class="display nowrap" cellspacing="0" width="100%"></table>

<div id="bpm-task-list-modal-data">
    <table id="bpm-task-list-modal-data-table" class="table table-hover no-margins">
        <tbody id="bpm-task-list-modal-data-table-tbody"></tbody>
    </table>
</div>

<div id="bpm-task-list-modal-diagram" style="z-index: 1000">
    <img id="bpm-task-list-modal-diagram-data" />
</div>

<div id="bpm-task-data-container">    
    <div id="bpm-task-data"></div>
</div>
<div id="bpm-task-list-modal-form"></div>


