<div class="row">
<div class="col-md-5">

    <div class="ui-dform-div form-group">
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldRequired.ftl">$i18n.getLabel("JPKIE_${field.name}")</label>
        <input type="${field.typeHTML}" id="${field.id}" name="$data.${field.name}.type:${field.name}" labelkey="JPKIE_${field.name}" class="form-control ui-widget" aria-required="true" <#include "/FieldPlaceHolder.ftl">value="${field.value}">
    </div>

</div>
</div>
