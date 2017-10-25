<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <a href="<s:url action="edit" namespace="/do/jpkiebpm/Config" />" >
            <s:text name="jpkiebpm.admin.menu.config" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="label.delete" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="label.delete" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="delete">
        <p class="sr-only">
            <wpsf:hidden name="strutsAction" />
            <wpsf:hidden name="id" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="title.kiebpms.are.you.sure.delete" />
        </p>
        <p class="esclamation-underline-text">
            <s:property value="id"/>  ?
        </p>
        <div class="text-center margin-large-top">
            <a href="<s:url action="list"/>" class="btn btn-default button-fixed-width">
                <s:text name="note.back" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </wpsf:submit>
        </div>
    </s:form>
</div>
