<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="currentAttachedFileVar" value="#attribute.attachedFile"></s:set>

<s:if test="#lang.default">
    <%-- Lingua di DEFAULT --%>
    <s:if test="#currentAttachedFileVar != null">
        <div class="panel panel-default">
            <div class="panel-body">
                <label class="col-sm-2 control-label"><s:property value="#currentAttachedFileVar.filename" /></label>
                <%-- file da scaricare --%>
                <div class="col-sm-10">
                    <a class="btn btn-primary btn-sm" href="<s:url action="viewFileResource" namespace="/do/jpfileattribute/Content/Resource" >
                           <s:param name="parentAttributeName" value="%{#parentAttribute.name}" />
                           <s:param name="attributeName" value="%{#attribute.name}" />
                           <s:param name="elementIndex" value="%{#elementIndex}" />
                           <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                       </s:url>">
                        <s:text name="label.download" />
                    </a>
                </div
                <s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
                    <span class="ml-20">
                        <wpsa:actionParam action="removeFileResource" var="removeFileResourceActionName" >
                            <wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
                            <wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
                            <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
                            <wpsa:actionSubParam name="contentOnSessionMarker" value="%{#contentOnSessionMarker}" />
                        </wpsa:actionParam>
                    </span>
                    <div class="col-xs-12">
                        <wpsf:submit cssClass="btn btn-danger btn-sm pull-right" useTabindexAutoIncrement="true" type="button" action="%{#removeFileResourceActionName}"
                                     value="%{getText('label.remove')}" title="%{getText('label.remove')}" src="%{#iconImagePath}" />
                    </div>
                </s:if>
            </div>
        </div>
    </s:if>

    <s:else>
        <wpsa:actionParam action="loadFileResource" var="loadFileResourceActionName" >
            <wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
            <wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
            <wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
            <wpsa:actionSubParam name="contentOnSessionMarker" value="%{#contentOnSessionMarker}" />
        </wpsa:actionParam>
        <wpsf:submit cssClass="btn btn-primary" useTabindexAutoIncrement="true" type="button" action="%{#loadFileResourceActionName}"
                     value="%{getText('jpfileattribute.label.load')}" title="%{#attribute.name + ': ' + getText('jpfileattribute.label.load')}" src="%{#iconImagePath}" />
    </s:else>
</s:if>

<s:else>
    <%-- Lingua NON di DEFAULT --%>
    <s:text name="note.editEntity.doThisInTheDefaultLanguage.must" />.
</s:else>
