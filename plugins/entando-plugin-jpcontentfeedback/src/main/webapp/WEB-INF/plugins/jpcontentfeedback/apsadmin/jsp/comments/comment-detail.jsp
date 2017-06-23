<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="commentVar" value="comment" />
<jacmswpsa:content contentId="%{#commentVar.contentId}" var="contentVar" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcontentfeedback.title.commentsManager"/></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />">
            <s:text name="jpcontentfeedback.title.comment.list"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jpcontentfeedback.title.comment.detail" />
    </li>
</ol>

<div class="page-tabs-header">
    <h1>
        <s:text name="jpcontentfeedback.title.comment.detail" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpcontentfeedback.title.comment.detail.help" />" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </h1>
</div>
<br>

<div id="main">

    <div class="table-responsive">
        <table class="table table-bordered table-hover no-mb">
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.author"/></th>
                <td class="col-sm-10"><s:property value="#commentVar.username"/></td>
            </tr>
            <tr>
                <th class="text-right col-sm-2"><s:text name="jpcontentfeedback.date.creation"/></th>
                <td class="col-sm-10">
                    <s:date name="#commentVar.creationDate" format="dd/MM/yyyy HH:mm"/>
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
                <td class="col-sm-10"><s:property value="#commentVar.contentId"/></td>
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

    <s:form action="updateStatus">

        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />

        <fieldset class="form-horizontal">
            <legend><s:text name="label.info" /></legend>
            <s:set var="listStatus" value="%{getAllStatus()}" />
            <div class="form-group">
                <label class="col-sm-2 control-label" for="status">
                    <s:text name="jpcontentfeedback.status" />
                </label>
                <div class="col-sm-10">
                    <wpsf:select  list="listStatus" name="status" id="status"  listKey="key" listValue="value" value="#commentVar.status" cssClass="form-control"/>
                </div>
            </div>
        </fieldset>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:hidden name="selectedComment" />
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="%{getText('jpcontentfeedback.label.update')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
