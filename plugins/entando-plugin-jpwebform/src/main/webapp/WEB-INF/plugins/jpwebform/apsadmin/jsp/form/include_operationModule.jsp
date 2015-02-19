<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<div class="btn-group btn-group-xs">
    <wpsa:actionParam action="moveStepAttribute" var="actionName" >
        <wpsa:actionSubParam name="attributeName" value="%{#attributeConfigVar.name}" />
        <wpsa:actionSubParam name="movement" value="UP" />
        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
        <wpsa:actionSubParam name="attributeOrder" value="%{#iteratorStatusStepsAttribute.index}" />
        <wpsa:actionSubParam name="order" value="%{#iteratorStatusStep.index}" />
    </wpsa:actionParam>
    <s:set name="iconImage" id="iconImage">icon fa fa-sort-desc</s:set>
    <wpsf:submit cssClass="btn btn-default" disabled="%{#iteratorStatusStepsAttribute.first}" action="%{#actionName}" type="button" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}">
        <span class="<s:property value="%{#iconImage}"/>"></span>
    </wpsf:submit>

    <wpsa:actionParam action="moveStepAttribute" var="actionName" >
        <wpsa:actionSubParam name="attributeName" value="%{#attributeConfigVar.name}" />
        <wpsa:actionSubParam name="movement" value="DOWN" />
        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
        <wpsa:actionSubParam name="attributeOrder" value="%{#iteratorStatusStepsAttribute.index}" />
        <wpsa:actionSubParam name="order" value="%{#iteratorStatusStep.index}" />
    </wpsa:actionParam>
    <s:set name="iconImage" id="iconImage">icon fa fa-sort-asc</s:set>
    <wpsf:submit cssClass="btn btn-default" disabled="%{#iteratorStatusStepsAttribute.last}" action="%{#actionName}" type="button" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}">
        <span class="<s:property value="%{#iconImage}"/>"></span>
    </wpsf:submit>
</div>
<div class="btn-group btn-group-xs">

    <wpsa:actionParam action="removeStepAttribute" var="actionName" >
        <wpsa:actionSubParam name="attributeName" value="%{#attributeConfigVar.name}" />
        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
        <wpsa:actionSubParam name="attributeOrder" value="%{#iteratorStatusStepsAttribute.index}" />
        <wpsa:actionSubParam name="order" value="%{#iteratorStatusStep.index}" />
    </wpsa:actionParam>
    <s:set name="iconImage" id="iconImage">icon fa fa-times-circle-o</s:set>
    <wpsf:submit cssClass="btn btn-warning" disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}">
        <span class="<s:property value="%{#iconImage}"/>"></span>
    </wpsf:submit>    
</div>
