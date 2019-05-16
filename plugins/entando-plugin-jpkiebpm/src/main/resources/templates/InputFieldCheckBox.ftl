<#include "/RowOpen.ftl">
<div class="col-md-${field.span} col-sm-12">
    <div class="ui-dform-div form-group">
        <input type="${field.typeHTML}" id="${field.id}" name="$data.${field.name}.type:${field.name}" labelkey="JPKIE_${field.name}" class="ui-widget " aria-required="true" value="true" <#include "/FieldDisabled.ftl" >>
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldLabel.ftl"></label>
        <input type="hidden" id="${field.id}_hiddenval"  value="${field.value}">
    </div>
    <script type="text/javascript">
        $( document ).ready(function() {
            var hiddenVal = $(${field.id}_hiddenval).val();
            if (hiddenVal =='true') {
                $(${field.id}).prop('checked', true);
            } else {
                $(${field.id}).prop('checked', false);
            }
            $(${field.id}_hiddenval).remove();
        });
    </script>
</div>
<#include "/RowClose.ftl">
