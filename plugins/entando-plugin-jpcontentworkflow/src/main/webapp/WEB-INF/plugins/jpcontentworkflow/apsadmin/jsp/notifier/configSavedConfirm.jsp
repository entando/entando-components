<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="title.workflowManagement" /></li>
	<li class="page-title-container"><s:text
			name="title.workflowNotifierManagement.config" /></li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="title.workflowNotifierManagement.config" />
		<span class="pull-right"> <a tabindex="0" role="button"
			data-toggle="popover" data-trigger="focus" data-html="true" title=""
			data-content="TO be inserted" data-placement="left"
			data-original-title=""> <i class="fa fa-question-circle-o"
				aria-hidden="true"></i>
		</a>
		</span>
	</div>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<div class="alert alert-success fade in">
	<s:text name="note.workflowNotifierManagement.savedConfirm" />
</div>
