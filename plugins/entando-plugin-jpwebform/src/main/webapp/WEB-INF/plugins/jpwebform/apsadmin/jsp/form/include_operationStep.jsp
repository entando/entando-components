<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
    <wpsa:actionParam action="moveStep" var="actionName" >
        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
        <wpsa:actionSubParam name="order" value="%{#iteratorStatusStep.index}" />
        <wpsa:actionSubParam name="movement" value="UP" />
    </wpsa:actionParam>
    <s:set name="iconImage" id="iconImage">icon fa fa-sort-desc</s:set>
    <wpsf:submit cssClass="btn btn-default" disabled="%{#iteratorStatusStep.first}" action="%{#actionName}" type="button" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}">
        <span class="<s:property value="%{#iconImage}"/>"></span>
    </wpsf:submit>

    <wpsa:actionParam action="moveStep" var="actionName" >
        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
        <wpsa:actionSubParam name="order" value="%{#iteratorStatusStep.index}" />
        <wpsa:actionSubParam name="movement" value="DOWN" />
    </wpsa:actionParam>
    <s:set name="iconImage" id="iconImage">icon fa fa-sort-asc</s:set>
    <wpsf:submit cssClass="btn btn-default" disabled="%{#iteratorStatusStep.last}" action="%{#actionName}" type="button" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}">
        <span class="<s:property value="%{#iconImage}"/>"></span>
    </wpsf:submit>
</div>

<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
    <wpsa:actionParam action="removeStep" var="actionName" >
        <wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" />
        <wpsa:actionSubParam name="order" value="%{#iteratorStatusStep.index}" />
    </wpsa:actionParam>
    <s:set name="iconImage" id="iconImage"><wp:resourceURL/>icon fa fa-times-circle-o</s:set>
    <wpsf:submit cssClass="btn btn-warning" disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}">
        <span class="<s:property value="%{#iconImage}"/>"></span>
    </wpsf:submit>
</div>