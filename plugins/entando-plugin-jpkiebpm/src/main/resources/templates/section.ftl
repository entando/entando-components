<div class="ui-dform-div">
    <fieldset class="ui-dform-fieldset">
        <#if section.name??>
        <legend class="control-label editLabel ui-dform-legend">
            $i18n.getLabel("JPKIE_FORM_${section.name}")
        </legend>
        <#else> 
        </#if>
<#if section.fields??>
        <#list section.fields as field>
                <div class="fields">
                <#switch field.typePAM>
                <#case "TextBox">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "TextArea">
                    <#include "/inputFieldTextArea.ftl">
                    <#break>
                <#case "DatePicker">
                    <#include "/inputFieldDatePicker.ftl">
                    <#break>
                <#case "IntegerBox">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "DecimalBox">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "CheckBox">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "Slider">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "RadioGroup">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "MultipleSelector">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "MultipleInput">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                <#case "Document">
                    <#include "/inputFieldTextBox.ftl">
                    <#break>      
                <#default>
                    <#include "/inputFieldTextBox.ftl">
                    <#break>
                </#switch>
                </div>
        </#list>
        <#else>

            </#if>
        </fieldset>
    </div>
