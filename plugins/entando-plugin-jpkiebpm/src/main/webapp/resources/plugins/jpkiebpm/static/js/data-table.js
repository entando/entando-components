/*
 *
 *  * The MIT License
 *  *
 *  * Copyright 2017 Entando Inc..
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 */

var org = org || {};
org.entando = org.entando || {};
org.entando.datatable = org.entando.datatable || {};
org.entando.datatable.BPMDatatable = function (data, idTable, context, configId) {

    var json = {
        "columns": [],
        "data": [],
        "containerId": data.taskList.containerId,
        "processId": data.taskList.processId
    };

    json.columns = getConfigColumnDatatable(data);
    json.data = getJsonData(data)

    function getObjectColumnDefinition(field) {
        return {
            "title": field.title,
            "data": field.data,
            "visible": field.visible,
            "class": "class-open-modal-bpm-details",
            "targets": field.position + 0
        };
    }

    function getConfigColumnDatatable(result) {
        var columns = result.taskList["datatable-field-definition"].fields;
        var fields = new Array();
        if (Array.isArray(columns)) {
            columns.forEach(function (el) {
                //console.log("el : ", el);
                fields.push(getObjectColumnDefinition(el));
            });
        }
        else {
            fields.push(getObjectColumnDefinition(columns));
        }
        return fields;
    }

    function getJsonData(result) {
        var rowList = result.taskList.list;
        var obj = new Array();
        if (Array.isArray(rowList)) {
            rowList.forEach(function (el) {
                obj.push(getData(el));
            });
        }
        else if (rowList !== undefined) {
            obj.push(getData(rowList));

        }
        return obj;
    }


    function getData(el) {
        var obj = {};
        json.columns.forEach(function (key) {
            obj[key.data] = el[key.data] === undefined ? '' : el[key.data];
            /*
                        switch (id) {
                            case 'activated' :
                                data.push({id: new Date(el[key.data]).toLocaleString()});
                                break;
                            case 'created' :
                                data.push({id: new Date(el[key.data]).toLocaleString()});
                                break;
                            default  :
                                data.push({id: el[key.data] === undefined ? '' : el[key.data]});
                        }
            */
        });
        return obj;
    };

    json.columns.push({});
    var table = $(idTable).DataTable({
        destroy: true,
        responsive: false,
        processing: true,
        data: json.data,
        columns: json.columns,
        scrollX: true,
        columnDefs: [{
            "targets": -1,
            "orderable": false,
            "data": null,
            "defaultContent": "<button type=\"button\" class=\"class-open-modal-bpm-form-details btn btn-success btn-sm \" style=\"margin-right:10px;\">Complete</button>" +
            "<button type=\"button\" class=\" class-open-modal-bpm-diagram-details btn btn-info btn-sm \">Diagram</button>"
        }],
        dom: 'lfrtBip',
        buttons: [
            {
                "extend": 'copy',
                //"text": '',
                "titleAttr": 'Copy'
            },
            {
                "extend": 'excel',
                //"text": '<i class="fa fa-file-excel-o"></i>',
                "titleAttr": 'Excel'
            },
            {
                "extend": 'csv',
                //"text": '<i class="fa fa-file-text-o"></i>',
                "titleAttr": 'CSV'
            }
        ]

    });


    function addRow(data, label, index) {
        var html = "<tr><td>" + label + "</td><td>" + data[index] + "</td></tr>"
        return data[index] === undefined ? "" : html;
    }

    function getHtmlListFragment(src, label) {
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
            "<tr><td>Expiration Time</td>" + data['task-expiration-time'] + "<td></td></tr>\n" +
            "<tr><td>Skippable</td><td>" + data['task-skippable'] + "</td></tr>\n" +
            "<tr><td>Workitem Id</td><td>" + data['task-workitem-id'] + "</td></tr>\n" +
            "<tr><td>Process Instance</td>" + data['task-process-instance-id'] + "<td></td></tr>\n" +
            "<tr><td>Parent Id</td><td>" + data['task-parent-id'] + "</td></tr>\n" +
            "<tr><td>Container Id</td><td>" + data['task-container-id'] + "</td></tr>\n" +
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

    /* Modal Details*/

    $(idTable + ' tbody').on('click', 'td.class-open-modal-bpm-details', function (event) {
        event.preventDefault();
        var data = table.row(this).data();
        //console.log(data);
        $('#modal-bpm-data-table-tbody').empty();
        var url = context + "taskDetail.json?containerId=" + json.containerId + "&taskId=" + data.id;
        //console.log(url);
        $.get(url, function (data) {
            $('#modal-bpm-data-table-tbody').append(getTemplateTaskDetail(data.response.result.mainForm));
            optModal.title = "BPM Data";
            $('#modal-bpm-data').dialog(optModal);
        });


    });


    /* Modal Form*/
    $(idTable + ' tbody').on('click', 'button.class-open-modal-bpm-form-details', function (event) {
        event.preventDefault();
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        var data = row.data();
        var processId = data[9];
        var url = context + "taskForm.json?containerId=" + json.containerId + "&taskId=" + data.id;

        $.get(url, function (data) {
            $('#modal-bpm-form').empty();
            var jsonKie = data.response.result;
            jsonKie.mainForm.method = "post";
            org.entando.form.dynamicForm = new org.entando.form.DynamicForm(jsonKie);
            //org.entando.form.dynamicForm.json.html[0].value = processId;
            $("#modal-bpm-form").dform(org.entando.form.dynamicForm.json);
            optModal.title = "BPM Form Data";
            $('#modal-bpm-form').dialog(optModal);

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
                        $("#modal-bpm-form").dialog("close");
                        $('#modal-bpm-form').empty();
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

    });


    /* Modal diagram*/
    $(idTable + ' tbody').on('click', 'button.class-open-modal-bpm-diagram-details', function (event) {
        event.preventDefault();
        var data = table.row($(this).closest('tr')).data();
//      console.log(data);
        var url = context + "diagram.json?configId=" + configId + "&processInstanceId=" + data.processInstanceId;
        $('#modal-bpm-diagram-data').empty();
        $.get(url, function (data) {
            $('#modal-bpm-diagram-data').attr("src", "data:image/svg+xml;utf8," + data.response.result);
            optModal.title = "BPM Process Diagram";
            optModal.show.effect = "fold";
            optModal.position = {my: "center", at: "center"};
            $('#modal-bpm-diagram').dialog(optModal);
        });

    });

};

org.entando.datatable.loadDataTable = function (url, idTable, context, configId) {
    $.get(url, function (data) {
        //console.log(data);
        new org.entando.datatable.BPMDatatable(data.response.result, idTable, context, configId);
    });
}
