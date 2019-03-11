var org = org || {};
org.entando = org.entando || {};
org.entando.datatable = org.entando.datatable || {};
org.entando.datatable.CustomDatatable = function (items, idTable, extraConfig, containerId) {


    function getConfigColumnDatatable(items, columnDefinition) {
                        
        if (columnDefinition) {
            columnDefinition = Array.isArray(columnDefinition) ? columnDefinition : [columnDefinition];
                  
            var columns = columnDefinition.map(function(col, i) {
                return {
                    title: col.title || col.data,
                    data: col.data,
                    visible: col.visible,
                    class: 'entando-datatable-row',
                    targets: col.position || i + 1
                };
            });
                    
            return columns;
        }
        console.log('Items received: ' + items);
        var keys = items.length ? Object.keys(items[0]) : [];
        var columns = keys.map(function(key, i) {
            return {
                title: key,
                data: key,
                visible: true,
                class: 'entando-datatable-row',
                targets: i + 1
            };
        });
        return columns;
    }

    function getJsonData(items, columns) {
        return items.map(function (el) {
            var obj = {};
            columns.forEach(function (key) {
                obj[key.data] = el[key.data] === undefined ? '' : el[key.data];
            });
            obj.containerId = containerId;
            return obj;
        });
    }

    var jsonColumns = getConfigColumnDatatable(items, extraConfig && extraConfig.columnDefinition);
    var buttonsColumnDef;
    if (extraConfig && extraConfig.buttons && Array.isArray(extraConfig.buttons)) {
        jsonColumns.push({});
        var buttonsStr = extraConfig.buttons.map(function(btn, i) {
            return $(btn.html).addClass('btn'+i)[0].outerHTML;
        }).join('');

        buttonsStr = '<div style="display: flex">' + buttonsStr + '</div>';

        buttonsColumnDef =[{
            "targets": -1,
            "orderable": false,
            "class": "buttons-col",
            "width": "300px",
            "data": null,
            "title": extraConfig.buttonsColumnTitle || '&nbsp;',
            "defaultContent": buttonsStr
        }];
    }

    var config = {
        destroy: true,
        responsive: false,
        processing: true,
        data: getJsonData(items, jsonColumns),
        columns: jsonColumns,
        columnDefs: buttonsColumnDef,
        scrollX: true,
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
        ],
        fixedColumns: {
            leftColumns: 0,
            rightColumns: 1
        },
    };


    if (extraConfig.language){
        config.language = extraConfig.language;
    }


    if (extraConfig.createdRow && typeof extraConfig.createdRow === 'function' ){
        config.createdRow = extraConfig.createdRow;
    }

    var table = $(idTable).DataTable(config);

    if (extraConfig && extraConfig.onClickRow && typeof extraConfig.onClickRow === 'function') {
        $(idTable + ' tbody').on('click', 'tr', function(ev) {
            extraConfig.onClickRow(ev, table.row(this).data());
        });
    }

    if (extraConfig && extraConfig.buttons && Array.isArray(extraConfig.buttons)) {
        extraConfig.buttons.forEach(function(btn, i) {
            if (!btn.onClick || typeof btn.onClick !== 'function') {
                return;
            }
            $(idTable+ '_wrapper tbody').on('click','.btn'+i,function(ev){
                ev.preventDefault();
                ev.stopPropagation();
                btn.onClick(ev, table.row($(this).closest('tr')).data());
            });
        });
    }
};
