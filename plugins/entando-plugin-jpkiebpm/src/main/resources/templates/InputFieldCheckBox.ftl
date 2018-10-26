<div class="row">
<div class="col-md-5">

    <div class="ui-dform-div form-group">

        <input type="${field.typeHTML}" id="${field.id}" name="$data.${field.name}.type:${field.name}" labelkey="JPKIE_${field.name}" class="ui-widget " aria-required="true" value="true">
        <label id="JPKIE_${field.name}" for="jpkieformparam_${field.name}" class="editLabel">
            <#include "/FieldRequired.ftl">$i18n.getLabel("JPKIE_${field.name}")</label>
    </div>

</div>
</div>
