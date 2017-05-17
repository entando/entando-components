<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="commentVar" value="comment"/>
<jacmswpsa:content contentId="%{#commentVar.contentId}" var="contentVar"/>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcontentfeedback.title.commentsManager"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />">
            <s:text name="jpcontentfeedback.title.comment.list"/>
        </a>
    </li>
    <li class="page-title-container"><s:text name="jpcontentfeedback.label.delete.comment.confirm"/></li>
</ol>

<div class="page-tabs-header">
    <h1>
        <s:text name="jpcontentfeedback.label.delete.comment.confirm"/>
        <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="TO be inserted" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
        </span>
    </h1>
</div>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main">
    <div class="col-sm-12 table-responsive">
        <table class="table table-bordered table-hover no-mb">
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.author"/></th>
                <td class="col-sm-10"><code><s:property value="#commentVar.username"/></code></td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.date.creation"/></th>
                <td class="col-sm-10">
                    <code><s:date name="#commentVar.creationDate" format="dd/MM/yyyy HH:mm"/></code>
                </td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.comment"/></th>
                <td class="col-sm-10"><s:property value="#commentVar.comment"/></td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.status"/></th>
                <td class="col-sm-10"><s:property value="getAllStatus().get(#commentVar.status)"/></td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.content.id"/></th>
                <td class="col-sm-10"><code><s:property value="#commentVar.contentId"/></code></td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.content.description"/></th>
                <td class="col-sm-10"><s:property value="#contentVar.descr"/></td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.content.type"/></th>
                <td class="col-sm-10">
                    <s:property value="#contentVar.typeDescr"/> (<s:property value="#contentVar.typeCode"/>)
                </td>
            </tr>
        </table>
    </div>

    <div class="text-center">
        <s:form action="delete">
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

            <wpsf:hidden name="selectedComment"/>
            <wpsf:hidden name="strutsAction"/>

            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline"><s:text name="label.delete"/></p>
            <p>
                <s:text name="jpcontentfeedback.label.delete.comment.confirm"/>
            </p>
            <div class="btn btn-danger button-fixed-width">
                <s:submit type="button">
                    <s:text name="label.delete"/>
                </s:submit>
            </div>
            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="list"/>">
                    <s:text name="label.back" />
                </a>
            </div>

        </s:form>
    </div>


</div>
