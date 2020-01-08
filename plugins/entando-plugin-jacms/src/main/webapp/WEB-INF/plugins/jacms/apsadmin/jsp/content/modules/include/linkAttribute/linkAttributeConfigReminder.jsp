<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmsapsadmin" uri="/jacms-apsadmin-core"%>
<s:if test="symbolicLink != null">
    <div class="alert alert-info mt-20 no-mb">
        <span class="pficon pficon-info"></span>
        <p>
            <s:text name="note.previousLinkSettings" />&#32;
            <s:if test="symbolicLink.destType == 1">
                <s:text name="note.URLLinkTo"></s:text>: <s:property value="symbolicLink.urlDest"/>.
            </s:if>
            <s:if test="symbolicLink.destType == 2 || symbolicLink.destType == 4">
                <wpsa:page key="%{symbolicLink.pageDest}" var="prevTargetPageVar" />
                <s:text name="note.pageLinkTo"></s:text>: <s:property value="getTitle(#prevTargetPageVar.code, #prevTargetPageVar.titles)" />
            </s:if>
            <s:if test="symbolicLink.destType == 4"> &ndash; </s:if>
            <s:if test="symbolicLink.destType == 3 || symbolicLink.destType == 4">
                <jacmsapsadmin:content var="prevTargetContentVoVar" contentId="%{symbolicLink.contentDest}" />
                <s:text name="note.contentLinkTo"></s:text>: <s:property value="symbolicLink.contentDest"/> &ndash; <s:property value="#prevTargetContentVoVar.descr"/>.
            </s:if>
        </p>
    </div>
</s:if>
