<#include "/RowOpen.ftl">
<div class="col-md-${field.span} col-sm-12">
    <div class="form-group">
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldLabel.ftl">
            </label>
        <table id="JPKIE_${field.id}"  class="display" width="100%"></table>
    </div>
</div>
<#include "/RowClose.ftl">
<script type="text/javascript">

    function pam2DataTable (array) {
        if (array) {
            return array.map(function(item) {
                const fields = item[Object.keys(item)[0]];
                const values = Object.values(fields);
                //alert("fields "+ fields);
                //alert("values "+ values);
                return values;
            });
        }
        else return new Array();
    }
    var dataSet = pam2DataTable(${field.value});
    var config = {
        destroy: true,
        responsive: true,
        processing: true,
        data: dataSet,
        columns: ${field.columns},
        scrollX: true,
        dom: 'lfrtBip'
    };

    $('#JPKIE_${field.id}').DataTable(config);
</script>

