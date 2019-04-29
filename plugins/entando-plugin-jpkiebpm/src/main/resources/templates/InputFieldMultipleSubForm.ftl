<div class="row">
    <div class='col-sm-12'>
        <div class="form-group">
            <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
                <#include "/FieldLabel.ftl">
                </label>
            <table id="JPKIE_${field.id}"  class="display" width="100%"></table>
        </div>
    </div>
    <script type="text/javascript">

        function pam2DataTable (array) {
            return array.map(function(item) {
                const fields = item[Object.keys(item)[0]];
                const values = Object.values(fields);
                //alert("fields "+ fields);
                //alert("values "+ values);
                return values;
            });
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
</div>
