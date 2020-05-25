<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="jpcontentscheduler.integrations" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.components" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.admin.menu" />
    </li>
    <li class="page-title-container">
        <s:text name="title.contentList" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpcontentscheduler.admin.menu" />
                <span class="pull-right">
                    <s:text name="jpcontentscheduler.section.help" var="helpVar" />
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="${helpVar}" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.general" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.mail" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewUsers"/>">
                        <s:text name="jpcontentscheduler.admin.users" />
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="viewContentTypes"/>">
                        <s:text name="jpcontentscheduler.admin.contentTypes" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>
<div id="main" role="main">

    <s:form action="removeContentType">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <p class="sr-only">
            <wpsf:hidden name="contentType"/>
        </p>
        <div class="text-center">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline"><s:text name="jpcontentscheduler.note.removeContentType.areYouSure" />&#32;</p>

            <p class="esclamation-underline-text"><s:property value="%{contentType}" />&nbsp;?</p>

            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="viewContentTypes" />" ><s:text name="label.back" /></a>
                <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                    <s:text name="label.remove" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>

