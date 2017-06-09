<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpnewsletter.integrations"/></li>
    <li><s:text name="jpnewsletter.components"/></li>
    <li><s:text name="jpnewsletter.admin.menu"/></li>
    <li>
        <a href="<s:url action="list" />">
            <s:text name="jpnewsletter.title.newsletterQueue"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jpnewsletter.title.newsletterEntry"/>
    </li>
</ol>
<div class="page-tabs-header">
    <h1>
        <s:text name="jpnewsletter.title.newsletterEntry"/>
        <span class="pull-right">
			<a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
				<i class="fa fa-question-circle-o" aria-hidden="true"></i>
			</a>
		</span>
    </h1>
</div>

<s:include value="/WEB-INF/plugins/jpnewsletter/apsadmin/jsp/newsletter/include/content-detail.jsp"/>